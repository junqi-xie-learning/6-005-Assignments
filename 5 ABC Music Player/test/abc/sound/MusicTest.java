package abc.sound;

import static org.junit.Assert.*;

import org.junit.Test;

import abc.player.MusicPlayer;

/**
 * Tests for the Music abstract data type.
 */
public class MusicTest {

    // Testing strategy
    //   Music AST
    //     Note.transpose(semitones):
    //       semitones: -Pitch.OCTAVE, -1, 0, 1, Pitch.OCTAVE
    //       observe with toString()
    //     Rest:
    //       duration: 0, >0
    //       observe with duration(), toString()
    //     Concat:
    //       first, second type: Note, Rest, Concat, Together
    //       first, second are of the same duration or aren't
    //       observe with duration(), toString()
    //     Together:
    //       top, bottom type: Note, Rest, Concat, Together
    //       top, bottom are of the same duration or aren't
    //       observe with duration(), toString()
    //   parse(input)
    //     input type:
    //       Notes with keys, lengths, accidentals and rests,
    //       Chords, Tuplets, Repeats
    //     observe with toString(), play()

    private final Music C1 = new Note(1, Pitch.MIDDLE_C);
    private final Music E2 = new Note(2, new Pitch('E'));
    private final Music c2 = new Note(2, Pitch.MIDDLE_C.transpose(Pitch.OCTAVE));
    private final Music z1 = new Rest(1);

    private final Music music1 = new Concat(E2, z1);
    private final Music music2 = new Together(C1, c2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testNoteInit() {
        assertEquals("expected middle C", "C1", C1.toString());
        assertEquals("expected middle E", "E2", E2.toString());
    }

    @Test
    public void testNoteTranspose() {
        assertEquals("expected treble C", "c2", c2.transpose(0).toString());
        assertEquals("expected high C", "c'2", c2.transpose(Pitch.OCTAVE).toString());
        assertEquals("expected low C", "C,1", C1.transpose(-Pitch.OCTAVE).toString());
        
        assertEquals("expected middle C#", "^C1", C1.transpose(1).toString());
        assertEquals("expected low B", "B,1", C1.transpose(-1).toString());
        assertEquals("expected middle F", "F2", E2.transpose(1).toString());
        assertEquals("expected middle D#", "^D2", E2.transpose(-1).toString());
    }

    @Test
    public void testRestEmpty() {
        assertEquals("expected rest", "z0", Music.empty().toString());
        assertEquals("expected duration 0", 0, Music.empty().duration());
    }

    @Test
    public void testRestNonempty() {
        assertEquals("expected rest", "z1", z1.toString());
        assertEquals("expected duration 1", 1, z1.duration());
    }

    @Test
    public void testConcatNotes() {
        assertEquals("expected concatenated notes", "E2 z1", music1.toString());
        assertEquals("expected concatinating duration", 3, music1.duration());
    }

    @Test
    public void testTogetherNotes() {
        assertEquals("expected overlapping notes", "together(C1 |||| c2)", music2.toString());
        assertEquals("expected overlapping duration", 1, music2.duration());
    }

    @Test
    public void testConcatTogether() {
        Music music = new Concat(music2, music1);
        assertEquals("expected concatenated notes", "together(C1 |||| c2) E2 z1", music.toString());
        assertEquals("expected concatinating duration", 4, music.duration());
    }

    @Test
    public void testTogetherConcat() {
        Music music = new Together(music1, music2);
        assertEquals("expected overlapping notes", "together(E2 z1 |||| together(C1 |||| c2))", music.toString());
        assertEquals("expected overlapping duration", 3, music.duration());
    }

    @Test
    public void testParseNotesPlain() {
        Music music = Music.parse(
            "C, D, E, F, | G, A, B, C | D E F G | A B c d | e f g a | b c' d' e' | f' g' a' b' |]",
            "C", 1);
        assertEquals(
            "C,1 D,1 E,1 F,1 G,1 A,1 B,1 C1 D1 E1 F1 G1 A1 B1 c1 d1 e1 f1 g1 a1 b1 c'1 d'1 e'1 f'1 g'1 a'1 b'1",
            music.toString());
        MusicPlayer.play(music, 120, 1);
    }

    @Test
    public void testParseNotesWithKeys() {
        Music music = Music.parse(
            "A,, B,, C, D, | E, F, G, A, | B, C D E | F G A B | c d e f | g a b c' | d' e' f' g' |]",
            "A", 1);
        assertEquals(
            "A,,1 B,,1 ^C,1 D,1 E,1 ^F,1 ^G,1 A,1 B,1 ^C1 D1 E1 ^F1 ^G1 A1 B1 ^c1 d1 e1 ^f1 ^g1 a1 b1 ^c'1 d'1 e'1 ^f'1 ^g'1",
            music.toString());
        MusicPlayer.play(music, 120, 1);
    }

    @Test
    public void testParseNotesWithLengths() {
        Music music = Music.parse(
            "A1/4 A/4 A/ A A2 A3 A4 A6 A8 | A,1/4 A,/4 A,/ A, A,2 A,3 A,4 A,6 A,8 |]",
            "C", 4);
        assertEquals(
            "A1 A1 A2 A4 A8 A12 A16 A24 A32 A,1 A,1 A,2 A,4 A,8 A,12 A,16 A,24 A,32",
            music.toString());
        MusicPlayer.play(music, 120, 4);
    }

    @Test
    public void testParseNotesWithAccidentals() {
        Music music = Music.parse("^C D c C |]", "C", 1);
        assertEquals("^C1 D1 c1 ^C1", music.toString());
        MusicPlayer.play(music, 120, 1);
    }

    @Test
    public static void testParseNotesWithRests() {
        Music music = Music.parse(
            "z2 Gc eGce z2 Gc eGce | z2 Ad fAdf z2 Ad fAdf |]",
            "C", 1);
        assertEquals(
            "z2 G1 c1 e1 G1 c1 e1 z2 G1 c1 e1 G1 c1 e1 z2 A1 d1 f1 A1 d1 f1 z2 A1 d1 f1 A1 d1 f1",
            music.toString());
        MusicPlayer.play(music, 120, 1);
    }

    @Test
    public void testParseChords() {
        Music music = Music.parse("[CEG] [C2E4] G2 |]", "C", 1);
        assertEquals("together(C1 |||| together(E1 |||| G1)) together(C2 |||| E4) G2", music.toString());
        MusicPlayer.play(music, 120, 1);
    }

    @Test
    public void testParseTuplets() {
        Music music = Music.parse("(2CD (3GAB (4cdef |]", "C", 12);
        assertEquals("C18 D18 G8 A8 B8 c9 d9 e9 f9", music.toString());
        MusicPlayer.play(music, 120, 12);
    }

    @Test
    public void testParseRepeats() {
        Music music = Music.parse("C D E F | G A B c :|]", "C", 1);
        assertEquals("C1 D1 E1 F1 G1 A1 B1 c1 C1 D1 E1 F1 G1 A1 B1 c1", music.toString());
        MusicPlayer.play(music, 120, 1);
    }

    @Test
    public void testParseRepeatsVaried() {
        Music music = Music.parse("C D E F | [1 G A B c | G A B B :| [2 F E D C |]", "C", 1);
        assertEquals("C1 D1 E1 F1 G1 A1 B1 c1 G1 A1 B1 B1 C1 D1 E1 F1 F1 E1 D1 C1", music.toString());
        MusicPlayer.play(music, 120, 1);
    }
    
}
