/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    //   toString()
    //     Expression type: Number, Variable, Operation
    //       Operation.op: +, *
    //       Operation.left, right type: Number, Variable, Operation
    //   equals(thatObject)
    //     Expression type: Number, Variable, Operation
    //       Number.n type: int, double
    //       Variable.var: differs in case or doesn't
    //       Operation.op: equals or doesn't
    //       Operation.left, right: in the same order or not
    //   parse(input)
    //     Expression type: Number, Variable, Operation
    //       Operation.op: +, *
    //       Operation.left, right type: Number, Variable, Operation
    //       Operations follow order of operations or don't
    //     input is a valid expression or isn't

    private final Expression one = new Number(1);
    private final Expression x = new Variable("x");

    private final Expression exp1 = new Operation('+', one, x);
    private final Expression exp2 = new Operation('*', x, one);
    private final Expression exp3 = new Operation('*', exp1, exp2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testToStringNumber() {
        assertEquals("expected number", "1.0", one.toString());
    }

    @Test
    public void testToStringVariable() {
        assertEquals("expected variable", "x", x.toString());
    }

    @Test
    public void testToStringPlus() {
        assertEquals("expected sum", "(1.0 + x)", exp1.toString());
    }

    @Test
    public void testToStringMultiply() {
        assertEquals("expected product", "(x * 1.0)", exp2.toString());
    }

    @Test
    public void testToStringExpressions() {
        assertEquals("expected expressions", "((1.0 + x) * (x * 1.0))", exp3.toString());
    }

    @Test
    public void testEqualityNumber() {
        Expression exp = new Number(1.0);
        assertEquals("expected equality", one, exp);
        assertEquals("expected equal hashcode", one.hashCode(), exp.hashCode());
    }

    @Test
    public void testEqualityVariable() {
        Expression exp = new Variable("x");
        assertEquals("expected equality", x, exp);
        assertEquals("expected equal hashcode", x.hashCode(), exp.hashCode());
    }

    @Test
    public void testEqualityPlus() {
        Expression exp = new Operation('+', new Number(1.0), new Variable("x"));
        assertEquals("expected equality", exp1, exp);
        assertEquals("expected equal hashcode", exp1.hashCode(), exp.hashCode());
    }

    @Test
    public void testEqualityMultiply() {
        Expression exp = new Operation('*', new Variable("x"), new Number(1.0));
        assertEquals("expected equality", exp2, exp);
        assertEquals("expected equal hashcode", exp2.hashCode(), exp.hashCode());
    }

    @Test
    public void testEqualityExpressions() {
        Expression left = new Operation('+', new Number(1.0), new Variable("x"));
        Expression right = new Operation('*', new Variable("x"), new Number(1.0));
        Expression exp = new Operation('*', left, right);
        assertEquals("expected equality", exp3, exp);
        assertEquals("expected equal hashcode", exp3.hashCode(), exp.hashCode());
    }

    @Test
    public void testInequalityNumber() {
        Expression exp = new Number(2);
        assertNotEquals("expected inequality", one, exp);
    }

    @Test
    public void testInequalityVariable() {
        Expression exp = new Variable("X");
        assertNotEquals("expected inequality", x, exp);
    }

    @Test
    public void testInequalityOperator() {
        Expression exp = new Operation('*', new Number(1.0), new Variable("x"));
        assertNotEquals("expected inequality", exp1, exp);
    }

    @Test
    public void testInequalityDifferentOrder() {
        Expression exp = new Operation('*', new Number(1.0), new Variable("x"));
        assertNotEquals("expected inequality", exp2, exp);
    }

    @Test
    public void testInequalityDifferentGrouping() {
        Expression left = new Operation('*', exp1, x);
        Expression exp = new Operation('*', left, one);
        assertNotEquals("expected inequality", exp3, exp);
    }

    @Test
    public void testParseNumber() {
        Expression exp = Expression.parse("1");
        assertEquals("expected parsed expression", one, exp);
    }

    @Test
    public void testParseVariable() {
        Expression exp = Expression.parse("x");
        assertEquals("expected parsed expression", x, exp);
    }

    @Test
    public void testParsePlus() {
        Expression exp = Expression.parse("1 + x");
        assertEquals("expected parsed expression", exp1, exp);
    }

    @Test
    public void testParseMultiply() {
        Expression exp = Expression.parse("x * 1");
        assertEquals("expected parsed expression", exp2, exp);
    }

    @Test
    public void testParseExpressions() {
        Expression exp = Expression.parse("(1 + x) * (x * 1)");
        assertEquals("expected parsed expression", exp3, exp);
    }

    @Test
    public void testParseIlligal() {
        try {
            Expression exp = Expression.parse("3 x");
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

}
