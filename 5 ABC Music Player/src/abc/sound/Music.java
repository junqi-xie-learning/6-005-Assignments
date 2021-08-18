package abc.sound;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import abc.parser.AbcLexer;
import abc.parser.AbcParser;
import abc.player.MusicMaker;

/**
 * Music represents a piece of music played by multiple instruments.
 */
public interface Music {

    // Datatype definition
    //   Music = Note(duration: int, pitch: Pitch) + Rest(duration: int) + 
    //           Concat(first: Music, second: Mudic) +
    //           Together(top: Music, bottom: Music)
    
    /**
     * Generate an empty music.
     * 
     * @return the representation of an empty music
     */
    public static Music empty() {
        return new Rest(0);
    }
    
    /**
     * Parse the body of an abc file.
     * 
     * @param input abc body to parse.
     * @return music AST for the input
     */
    public static Music parse(String input, String key, int ticksPerBeat) {
        CharStream stream = new ANTLRInputStream(input);
        AbcLexer lexer = new AbcLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);
        AbcParser parser = new AbcParser(tokens);
        parser.reportErrorsAsExceptions();
        
        ParseTree tree = parser.root();
        MusicMaker maker = new MusicMaker(getEnvironment(key), ticksPerBeat);
        new ParseTreeWalker().walk(maker, tree);
        return maker.getMusic();
    }

    /**
     * Generate the semitone environment for the desired key.
     * 
     * @param key string representation of the key
     * @return the semitone environment corresponding to the key
     */
    public static Map<String, Integer> getEnvironment(String key) {
        Map<String, Integer> environment = new HashMap<>();
        // Intentionally not breaking after cases
        switch (key) {
            // Scales with sharp key signatures
            case "C#": case "A#m":
                environment.put("B", 1);
            case "F#": case "D#m":
                environment.put("E", 1);
            case "B": case "G#m":
                environment.put("A", 1);
            case "E": case "C#m":
                environment.put("D", 1);
            case "A": case "F#m":
                environment.put("G", 1);
            case "D": case "Bm":
                environment.put("C", 1);
            case "G": case "Em":
                environment.put("F", 1);
                break;
            // Scales with flat key signatures
            case "Cb": case "Abm":
                environment.put("F", -1);
            case "Gb": case "Ebm":
                environment.put("C", -1);
            case "Db": case "Bbm":
                environment.put("G", -1);
            case "Ab": case "Fm":
                environment.put("D", -1);
            case "Eb": case "Cm":
                environment.put("A", -1);
            case "Bb": case "Gm":
                environment.put("E", -1);
            case "F": case "Dm":
                environment.put("B", -1);
                break;
        }

        Set<String> symbols = new HashSet<>(environment.keySet());
        for (String symbol : symbols) {
            int semitones = environment.get(symbol);
            environment.put(symbol + ",", semitones);
            environment.put(symbol + ",,", semitones);
            environment.put(symbol.toLowerCase(), semitones);
            environment.put(symbol.toLowerCase() + "\'", semitones);
            environment.put(symbol.toLowerCase() + "\'\'", semitones);
        }
        return environment;
    }
    
    /**
     * @return total duration of this piece in beats
     */
    int duration();
    
    /**
     * Transpose all notes upward or downward in pitch.
     * 
     * @param semitonesUp semitones by which to transpose
     * @return for Music m, return m' such that for all notes n in m, the
     *         corresponding note n' in m' has
     *         n'.pitch() == n.pitch().transpose(semitonesUp), and m' is
     *         otherwise identical to m
     */
    Music transpose(int semitonesUp);
    
    /**
     * Play this piece.
     * 
     * @param player player to play on
     * @param atBeat when to play
     */
    void play(SequencePlayer player, int atBeat);
    
}
