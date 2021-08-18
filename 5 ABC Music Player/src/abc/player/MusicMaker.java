package abc.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import abc.parser.AbcListener;
import abc.parser.AbcParser.BarContext;
import abc.parser.AbcParser.ChordContext;
import abc.parser.AbcParser.DurationContext;
import abc.parser.AbcParser.MusicContext;
import abc.parser.AbcParser.NoteContext;
import abc.parser.AbcParser.PitchContext;
import abc.parser.AbcParser.RepeatContext;
import abc.parser.AbcParser.RestContext;
import abc.parser.AbcParser.RootContext;
import abc.parser.AbcParser.SectionContext;
import abc.parser.AbcParser.TupletContext;
import abc.parser.AbcParser.VariedContext;
import abc.sound.*;

public class MusicMaker implements AbcListener {

    // Invariant: stack contains the Music value of each parse subtree that
    // has been fully-walked so far, but whose parent has not yet been exited by
    // the walk. The stack is ordered by recency of visit, so that the top of the
    // stack is the Music for the most recently walked subtree.
    //
    // At the start of the walk, the stack is empty, because no subtrees have
    // been fully walked.
    //
    // Whenever a node is exited by the walk, the Music values of its children
    // are on top of the stack, in order with the last child on top. To preserve
    // the invariant, we must pop those child Music values from the stack,
    // combine them with the appropriate Music producer, and push back an
    // Music value representing the entire subtree under the node.
    //
    // At the end of the walk, after all subtrees have been walked and the the
    // root has been exited, only the entire tree satisfies the invariant's
    // "fully walked but parent not yet exited" property, so the top of the stack
    // is the Music of the entire parse tree.

    private final Stack<Music> musicStack = new Stack<>();
    private final Stack<Pitch> pitchStack = new Stack<>();
    private final Stack<Integer> durationStack = new Stack<>();

    // Context of notes in the key signature.
    private final Map<String, Integer> defaultEnvironment;
    // Context of notes in the current bar.
    private Map<String, Integer> currentEnvironment;

    // Ticks per beat in the music.
    private final int ticksPerBeat;

    // Tuplet parameters
    private int counter = 0;
    private double speed = 1.0;

    /**
     * Construct music in specified key and speed.
     * 
     * @param environment semitone environment of the key
     * @param ticksPerBeat ticks per beat in the music
     */
    public MusicMaker(Map<String, Integer> environment, int ticksPerBeat) {
        this.defaultEnvironment = environment;
        this.ticksPerBeat = ticksPerBeat;
    }

    /**
     * Returns the music constructed by this listener object.
     * Requires that this listener has completely walked over an Music parse
     * tree using ParseTreeWalker.
     * 
     * @return Music for the parse tree that was walked
     */
    public Music getMusic() {
        return musicStack.get(0);
    }

    @Override
    public void exitRoot(RootContext context) {
        int notes = context.music().size() + context.repeat().size() +
                    context.varied().size();
        assert musicStack.size() >= notes;
        assert notes > 0;

        Music music = musicStack.pop();
        for (int i = 0; i < notes - 1; i++) {
            music = new Concat(musicStack.pop(), music);
        }
        musicStack.push(music);
    }

    @Override
    public void exitMusic(MusicContext context) {
        // do nothing, music has only one child so its value is 
        // already on top of the stack
    }

    @Override
    public void exitRepeat(RepeatContext context) {
        assert musicStack.size() >= 1;

        Music music = musicStack.pop();
        musicStack.push(new Concat(music, music));
    }
    
    @Override
    public void exitVaried(VariedContext context) {
        int notes = context.section().size();
        assert musicStack.size() >= notes;
        assert notes == 3;

        Music music2 = musicStack.pop(), music1 = musicStack.pop();
        Music music = musicStack.pop();

        music1 = new Concat(music, music1);
        music2 = new Concat(music, music2);
        musicStack.push(new Concat(music1, music2));
    }

    @Override
    public void exitSection(SectionContext context) {
        int notes = context.bar().size();
        assert musicStack.size() >= notes;
        assert notes > 0;

        Music music = musicStack.pop();
        for (int i = 0; i < notes - 1; i++) {
            music = new Concat(musicStack.pop(), music);
        }
        musicStack.push(music);
    }

    @Override
    public void enterBar(BarContext context) {
        currentEnvironment = new HashMap<>(defaultEnvironment);
    }

    @Override
    public void exitBar(BarContext context) {
        int notes = context.note().size() + context.rest().size() +
                    context.chord().size() + context.tuplet().size();
        assert musicStack.size() >= notes;
        assert notes > 0;

        Music music = musicStack.pop();
        for (int i = 0; i < notes - 1; i++) {
            music = new Concat(musicStack.pop(), music);
        }
        musicStack.push(music);
    }

    @Override
    public void exitNote(NoteContext context) {
        assert durationStack.size() >= 1;
        assert pitchStack.size() >= 1;

        int duration = durationStack.pop();
        Pitch pitch = pitchStack.pop();
        musicStack.push(new Note(duration, pitch));
    }

    @Override
    public void exitRest(RestContext context) {
        assert durationStack.size() >= 1;

        int duration = durationStack.pop();
        musicStack.push(new Rest(duration));
    }

    @Override
    public void exitChord(ChordContext context) {
        int notes = context.note().size();
        assert musicStack.size() >= notes;
        assert notes > 0;

        Music music = musicStack.pop();
        for (int i = 0; i < notes - 1; i++) {
            music = new Together(musicStack.pop(), music);
        }
        musicStack.push(music);
    }

    @Override
    public void enterTuplet(TupletContext context) {
        counter = Integer.parseInt(context.NUMBER().getText());
        switch (counter) {
            case 2:
                speed = 3.0 / 2;
                return;
            case 3:
                speed = 2.0 / 3;
                return;
            case 4:
                speed = 3.0 / 4;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void exitTuplet(TupletContext context) {
        // do nothing, tuplet has only one child so its value is 
        // already on top of the stack
        speed = 1.0;
    }

    @Override
    public void exitPitch(PitchContext context) {
        String symbol = context.LETTER().getText();
        Pitch pitch = new Pitch(symbol.toUpperCase().charAt(0));

        if (Character.isLowerCase(symbol.charAt(0))) {
            pitch = pitch.transpose(Pitch.OCTAVE);
        }
        for (int i = 0; i < context.OCTAVE().size(); i++) {
            switch (context.OCTAVE(i).getText()) {
                case "\'":
                    pitch = pitch.transpose(Pitch.OCTAVE);
                    symbol += "\'";
                    break;
                case ",":
                    pitch = pitch.transpose(-Pitch.OCTAVE);
                    symbol += ",";
                    break;
            }
        }

        int semitones = 0;
        if (context.ACCIDENTAL().size() != 0) {
            for (int i = 0; i < context.ACCIDENTAL().size(); i++) {
                switch (context.ACCIDENTAL(i).getText()) {
                    case "_":
                        semitones -= 1;
                        break;
                    case "^":
                        semitones += 1;
                        break;
                    default:
                        break;
                }
            }
            currentEnvironment.put(symbol, semitones);
        }
        else {
            currentEnvironment.putIfAbsent(symbol, 0);
            semitones = currentEnvironment.get(symbol);
        }

        pitchStack.push(pitch.transpose(semitones));
    }

    @Override
    public void exitDuration(DurationContext context) {
        Matcher m = Pattern.compile("(\\d+)?(/)?(\\d+)?").matcher(context.getText());

        double duration = 1.0;
        if (m.find()){
            if (m.group(1) != null) {
                duration *= Integer.valueOf(m.group(1));
            }
            if (m.group(2) != null) {
                if (m.group(3) != null) {
                    duration /= Integer.valueOf(m.group(3));
                }
                else {
                    duration /= 2;
                }
            }
        }

        if (counter > 0) {
            duration *= speed;
            counter--;
        }
        durationStack.push((int) (duration * ticksPerBeat));
    }

    @Override public void enterRoot(RootContext context) { }
    @Override public void enterMusic(MusicContext context) { }
    @Override public void enterRepeat(RepeatContext context) { }
    @Override public void enterVaried(VariedContext context) { }
    @Override public void enterSection(SectionContext context) { }
    @Override public void enterNote(NoteContext context) { }
    @Override public void enterRest(RestContext context) { }
    @Override public void enterChord(ChordContext context) { }
    @Override public void enterPitch(PitchContext context) { }
    @Override public void enterDuration(DurationContext context) { }

    @Override public void visitTerminal(TerminalNode terminal) { }
    @Override public void enterEveryRule(ParserRuleContext context) { }
    @Override public void exitEveryRule(ParserRuleContext context) { }
    @Override public void visitErrorNode(ErrorNode node) { }

}
