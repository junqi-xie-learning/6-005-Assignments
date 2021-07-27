package expressivo;

import java.util.Set;

class Operation implements Expression {
    private final char op;
    private final Expression left, right;
    
    // Abstraction function
    //   represents an operation on two expressions left & right
    // Rep invariant
    //   operation is + or *
    // Safety from rep exposure
    //   all fields are immutable and final
    
    /**
     * Make an Operation of left and right.
     * 
     * @param op operation (+ or *)
     * @param left left expression
     * @param right right expression
     */
    public Operation(char op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
        checkRep();
    }
    
    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        assert Set.of('+', '*').contains(op);
    }
    
    /**
     * @return a readable representation of this Operation
     *         whitespace and parentheses are added for readability
     */
    @Override public String toString() {
        return "(" + left.toString() + " " + op + " " + right.toString() + ")";
    }

    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Operation)) return false;
        Operation that = (Operation) thatObject;
        return this.op == that.op && this.left.equals(that.left) &&
            this.right.equals(that.right);
    }

    @Override public int hashCode() {
        return Character.hashCode(op) + left.hashCode() + right.hashCode();
    }

}
