package abc.sound;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

public class SequencePlayerTest {

    @Test
    public void playPiece1() {
        try {
            int quarter = 12, whole = quarter * 4;
            int sixteenth = quarter / 4, triplet = quarter / 3;
            SequencePlayer player = new SequencePlayer(140, quarter);

            player.addNote(new Pitch('C').toMidiNote(), 0, quarter);
            player.addNote(new Pitch('C').toMidiNote(), quarter, quarter);
            player.addNote(new Pitch('C').toMidiNote(), quarter * 2, sixteenth * 3);
            player.addNote(new Pitch('D').toMidiNote(), quarter * 2 + sixteenth * 3, sixteenth);
            player.addNote(new Pitch('E').toMidiNote(), quarter * 3, quarter);

            player.addNote(new Pitch('E').toMidiNote(), whole, sixteenth * 3);
            player.addNote(new Pitch('D').toMidiNote(), whole + sixteenth * 3, sixteenth);
            player.addNote(new Pitch('E').toMidiNote(), whole + quarter, sixteenth * 3);
            player.addNote(new Pitch('F').toMidiNote(), whole + quarter + sixteenth * 3, sixteenth);
            player.addNote(new Pitch('G').toMidiNote(), whole + quarter * 2, quarter * 2);

            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), whole * 2, triplet);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), whole * 2 + triplet, triplet);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), whole * 2 + triplet * 2, triplet);
            player.addNote(new Pitch('G').toMidiNote(), whole * 2 + quarter, triplet);
            player.addNote(new Pitch('G').toMidiNote(), whole * 2 + quarter + triplet, triplet);
            player.addNote(new Pitch('G').toMidiNote(), whole * 2 + quarter + triplet * 2, triplet);
            player.addNote(new Pitch('E').toMidiNote(), whole * 2 + quarter * 2, triplet);
            player.addNote(new Pitch('E').toMidiNote(), whole * 2 + quarter * 2 + triplet, triplet);
            player.addNote(new Pitch('E').toMidiNote(), whole * 2 + quarter * 2 + triplet * 2, triplet);
            player.addNote(new Pitch('C').toMidiNote(), whole * 2 + quarter * 3, triplet);
            player.addNote(new Pitch('C').toMidiNote(), whole * 2 + quarter * 3 + triplet, triplet);
            player.addNote(new Pitch('C').toMidiNote(), whole * 2 + quarter * 3 + triplet * 2, triplet);

            player.addNote(new Pitch('G').toMidiNote(), whole * 3, sixteenth * 3);
            player.addNote(new Pitch('F').toMidiNote(), whole * 3 + sixteenth * 3, sixteenth);
            player.addNote(new Pitch('E').toMidiNote(), whole * 3 + quarter, sixteenth * 3);
            player.addNote(new Pitch('D').toMidiNote(), whole * 3 + quarter + sixteenth * 3, sixteenth);
            player.addNote(new Pitch('C').toMidiNote(), whole * 3 + quarter * 2, quarter * 2);

            System.out.println(player);

            // play!
            player.play();
        }
        catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
        catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        }
    }

    @Test
    public void playPiece2() {
        try {
            int quarter = 12, whole = quarter * 4;
            int eighth = quarter / 2, triplet = quarter * 2 / 3;
            SequencePlayer player = new SequencePlayer(200, quarter);

            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 0, eighth);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 0, eighth);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), eighth, eighth);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), eighth, eighth);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), quarter + eighth, eighth);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), quarter + eighth, eighth);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), quarter * 2 + eighth, eighth);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), quarter * 2 + eighth, eighth);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), quarter * 3, quarter);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), quarter * 3, quarter);

            player.addNote(new Pitch('G').toMidiNote(), whole, quarter);
            player.addNote(new Pitch('B').toMidiNote(), whole, quarter);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), whole, quarter);
            player.addNote(new Pitch('G').toMidiNote(), whole + quarter * 2, quarter);

            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), whole * 2, eighth * 3);
            player.addNote(new Pitch('G').toMidiNote(), whole * 2 + quarter + eighth, eighth);
            player.addNote(new Pitch('E').toMidiNote(), whole * 2 + quarter * 3, quarter);

            player.addNote(new Pitch('E').toMidiNote(), whole * 3, eighth);
            player.addNote(new Pitch('A').toMidiNote(), whole * 3 + eighth, quarter);
            player.addNote(new Pitch('B').toMidiNote(), whole * 3 + quarter + eighth, quarter);
            player.addNote(new Pitch('B').transpose(-1).toMidiNote(), whole * 3 + quarter * 2 + eighth, eighth);
            player.addNote(new Pitch('A').toMidiNote(), whole * 3 + quarter * 3, quarter);

            player.addNote(new Pitch('G').toMidiNote(), whole * 4, triplet);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), whole * 4 + triplet, triplet);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), whole * 4 + triplet * 2, triplet);
            player.addNote(new Pitch('A').transpose(Pitch.OCTAVE).toMidiNote(), whole * 4 + quarter * 2, quarter);
            player.addNote(new Pitch('F').transpose(Pitch.OCTAVE).toMidiNote(), whole * 4 + quarter * 3, eighth);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), whole * 4 + quarter * 3 + eighth, eighth);

            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), whole * 5 + eighth, quarter);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), whole * 5 + quarter + eighth, eighth);
            player.addNote(new Pitch('D').transpose(Pitch.OCTAVE).toMidiNote(), whole * 5 + quarter * 2, eighth);
            player.addNote(new Pitch('B').toMidiNote(), whole * 5 + quarter * 2 + eighth, eighth * 3 / 2);

            System.out.println(player);

            // play!
            player.play();
        }
        catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
        catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        }
    }

}
