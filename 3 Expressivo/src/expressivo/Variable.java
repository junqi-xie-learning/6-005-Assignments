package expressivo;

import java.util.Map;

class Variable implements Expression {
    private final String var;
    
    // Abstraction function
    //   represents the variable var
    // Rep invariant
    //   var is a nonempty string of letters
    // Safety from rep exposure
    //   all fields are immutable and final
    
    /** 
     * Make a Variable.
     * 
     * @param var nonempty nonempty to represent
     */
    public Variable(String var) {
        this.var = var;
        checkRep();
    }

    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        assert var.matches("[a-zA-Z]+");
    }
    
    @Override public String toString() {
        return var;
    }

    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Variable)) return false;
        Variable that = (Variable) thatObject;
        return this.var.equals(that.var);
    }

    @Override public int hashCode() {
        return var.hashCode();
    }

    @Override public Expression differentiate(String variable) {
        return var.equals(variable) ? new Number(1) : new Number(0);
    }

    @Override public Expression simplify(Map<String, Double> environment) {
        return environment.containsKey(var) ? new Number(environment.get(var)) : this;
    }

}
