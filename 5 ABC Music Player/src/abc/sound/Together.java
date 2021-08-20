package abc.sound;

/**
 * Together represents two pieces of music playing at the same time.
 * The pieces start at the same instant, but may end at different times.
 */
public class Together implements Music {
    
    private Music top;
    private Music bottom;

    // Abstraction function
    //   represents two pieces of music playing at the same time
    // Rep invariant
    //   none
    // Safety from rep exposure
    //   all fields are immutable and final

    private void checkRep() {
        assert top != null;
        assert bottom != null;
    }
    
    /**
     * Make a Together of two pieces of music.
     * @param top one piece of music
     * @param bottom another piece of music
     */
    public Together(Music top, Music bottom) {
        this.top = top;
        this.bottom = bottom;
        checkRep();
    }
    
    /**
     * @return one of the pieces of music in this together
     */
    public Music top() {
        return top;
    }
    
    /**
     * @return the other piece of music in this together
     */
    public Music bottom() {
        return bottom;
    }
    
    /**
     * @return duration of this piece of music, the time of the top note
     */
    public int duration() {
        return top.duration();
    }
    
    /**
     * Transpose the pieces in this Together.
     */
    public Music transpose(int semitonesUp) {
        return new Together(top.transpose(semitonesUp), bottom.transpose(semitonesUp));
    }
    
    /**
     * Play the pieces of this Together, together.
     */
    public void play(SequencePlayer player, int atBeat) {
        top.play(player, atBeat);
        bottom.play(player, atBeat);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        return top.hashCode() + prime * bottom.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        final Together other = (Together) obj;
        return top.equals(other.top) && bottom.equals(other.bottom);
    }

    @Override
    public String toString() {
        return "together(" + top + " |||| " + bottom + ")";
    }
}
