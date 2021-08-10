package abc.sound;

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
