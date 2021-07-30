package expressivo;

import java.util.Map;
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

    @Override public Expression differentiate(String variable) {
        Expression dLeft = left.differentiate(variable);
        Expression dRight = right.differentiate(variable);

        return op == '+' ? new Operation('+', dLeft, dRight) :
            new Operation('+', new Operation('*', dLeft, right), new Operation('*', left, dRight));
    }

    @Override public Expression simplify(Map<String, Double> environment) {
        Expression sLeft = left.simplify(environment);
        Expression sRight = right.simplify(environment);

        if (sLeft instanceof Number && sRight instanceof Number) {
            double nLeft = ((Number)sLeft).getNumber();
            double nRight = ((Number)sRight).getNumber();

            return op == '+' ? new Number(nLeft + nRight) : new Number(nLeft * nRight);
        }
        else {
            return new Operation(op, sLeft, sRight);
        }
    }

}
