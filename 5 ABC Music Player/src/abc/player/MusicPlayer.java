package abc.player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import abc.sound.Music;
import abc.sound.SequencePlayer;

/**
 * MusicPlayer can play a Music expression on the MIDI synthesizer.
 */
public class MusicPlayer {
    
    /**
     * Play music.
     * @param music music to play
     */
    public static void play(Music music, int beatsPerMinute, int ticksPerBeat) {
        try {
            final SequencePlayer player = new SequencePlayer(beatsPerMinute, ticksPerBeat);
            
            // load the player with a sequence created from music
            music.play(player, 0);
            // start playing
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
