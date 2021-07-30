/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //   differentiate(expression, variable)
    //     expression type: Number, Variable, Operation
    //       Operation.op: +, *
    //       Operation.left, right type: Number, Variable, Operation
    //       Operations follow order of operations or don't
    //     expression contains the variable or doesn't
    //     expression contains other variables or doesn't
    //     expression and variable are valid or aren't
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testDifferentiateNumber() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("1", "x"), "0.0");
    }

    @Test
    public void testDifferentiateVariable() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("x", "x"), "1.0");
    }

    @Test
    public void testDifferentiatePlus() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("1 + x", "x"), "(0.0 + 1.0)");
    }

    @Test
    public void testDifferentiateMultiply() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("x * 1", "x"), "((1.0 * 1.0) + (x * 0.0))");
    }

    @Test
    public void testDifferentiateSingleSameVariable() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("(1.0 + x) * (x * 1.0)", "x"),
            "(((0.0 + 1.0) * (x * 1.0)) + ((1.0 + x) * ((1.0 * 1.0) + (x * 0.0))))");
    }

    @Test
    public void testDifferentiateSingleDifferentVariable() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("(1.0 + x) * (x * 1.0)", "y"),
            "(((0.0 + 0.0) * (x * 1.0)) + ((1.0 + x) * ((0.0 * 1.0) + (x * 0.0))))");
    }

    @Test
    public void testDifferentiateMultipleVariables() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("x * y", "x"), "((1.0 * y) + (x * 0.0))");
    }

    @Test
    public void testDifferentiateIllegalExpression() {
        try {
            Commands.differentiate("3 x", "x");
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testDifferentiateIllegalVariable() {
        try {
            Commands.differentiate("3 + x", "3");
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

}
