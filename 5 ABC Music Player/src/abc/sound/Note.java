package abc.sound;

/**
 * Note represents a note. 
 */
public class Note implements Music {
    
    private final int duration;
    private final Pitch pitch;
    
    // Abstraction function
    //   represents a note in pitch with duration
    // Rep invariant
    //   duration is nonnegative
    // Safety from rep exposure
    //   all fields are immutable and final
    
    private void checkRep() {
        assert duration >= 0;
        assert pitch != null;
    }
    
    /**
     * Make a Note played for duration beats.
     * @param duration duration in beats, must be >= 0
     * @param pitch pitch to play
     */
    public Note(int duration, Pitch pitch) {
        this.duration = duration;
        this.pitch = pitch;
        checkRep();
    }
    
    /**
     * @return pitch of this note
     */
    public Pitch pitch() {
        return pitch;
    }
    
    /**
     * @return duration of this note
     */
    public int duration() {
        return duration;
    }
    
    /**
     * Transpose this note.
     */
    public Music transpose(int semitonesUp) {
        return new Note(duration, pitch.transpose(semitonesUp));
    }
    
    /**
     * Play this note.
     */
    @Override
    public void play(SequencePlayer player, int atBeat) {
        player.addNote(pitch.toMidiNote(), atBeat, duration);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(duration) + pitch.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        final Note other = (Note) obj;
        return duration == other.duration
                && pitch.equals(other.pitch);
    }

    @Override
    public String toString() {
        return pitch.toString() + duration;
    }
}
