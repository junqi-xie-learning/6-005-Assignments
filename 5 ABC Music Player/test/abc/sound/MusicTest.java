package abc.sound;

import static org.junit.Assert.*;

import org.junit.Test;

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

}
