package abc.sound;

/**
 * Concat represents two pieces of music played one after the other.
 */
public class Concat implements Music {
    
    private final Music first;
    private final Music second;
    
    // Abstraction function
    //   represents two pieces of music played one after the other
    // Rep invariant
    //   none
    // Safety from rep exposure
    //   all fields are immutable and final
    
    private void checkRep() {
        assert first != null;
        assert second != null;
    }
    
    /**
     * Make a Music sequence that plays first followed by second.
     * @param first music to play first
     * @param second music to play second
     */
    public Concat(Music first, Music second) {
        this.first = first;
        this.second = second;
        checkRep();
    }
    
    /**
     * @return first piece in this concatenation
     */
    public Music first() {
        return first;
    }
    
    /**
     * @return second piece in this concatenation
     */
    public Music second() {
        return second;
    }
    
    /**
     * @return duration of this concatenation
     */
    public int duration() {
        return first.duration() + second.duration();
    }
    
    /**
     * Transpose the pieces in this concatenation.
     */
    public Music transpose(int semitonesUp) {
        return new Concat(first.transpose(semitonesUp), second.transpose(semitonesUp));
    }
    
    /**
     * Play this concatenation.
     */
    public void play(SequencePlayer player, int atBeat) {
        first.play(player, atBeat);
        second.play(player, atBeat + first.duration());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return first.hashCode() + prime * second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        final Concat other = (Concat) obj;
        return first.equals(other.first) && second.equals(other.second);
    }

    @Override
    public String toString() {
        return first + " " + second;
    }
}
