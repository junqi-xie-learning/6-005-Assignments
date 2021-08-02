/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Board data structure.
 */
public class BoardTest {
    
    // Testing strategy
    //   dig(x, y)
    //     (x, y) is in the board, or out of the board
    //     (x, y) is _flagged_, _dug_, or _untouched_
    //     (x, y) contains a bomb or doesn't
    //     (x, y) has neighbor squares with bombs or doesn't
    //   flag(x, y)
    //     (x, y) is in the board, or out of the board
    //     (x, y) is _untouched_ or isn't
    //   deflag(x, y)
    //     (x, y) is in the board, or out of the board
    //     (x, y) is _flagged_ or isn't
    //   toString()
    //     squares are _flagged_, _dug_, or _untouched_

    private final int[][] boardRep = {
        { 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 1, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0 },
        { 1, 0, 0, 0, 0, 0, 0 }
    };

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testDigOutOfRange() {
        Board board = new Board(boardRep);
        boolean result = board.dig(-1, -1);
        String[] rep = {
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertFalse("expected no explosion", result);
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testDigUntouched() {
        Board board = new Board(boardRep);
        boolean result = board.dig(1, 3);
        String[] rep = {
            "- - - - - - -",
            "- - - 1 - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertFalse("expected no explosion", result);
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testDigBomb() {
        Board board = new Board(boardRep);
        boolean result = board.dig(1, 4);
        String[] rep = {
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "1 1          ",
            "- 1          "
        };
        assertTrue("expected explosion", result);
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testDigEmpty() {
        Board board = new Board(boardRep);
        boolean result = board.dig(0, 0);
        String[] rep = {
            "      1 - 1  ",
            "      1 - 1  ",
            "      1 1 1  ",
            "             ",
            "             ",
            "1 1          ",
            "- 1          "
        };
        assertFalse("expected no explosion", result);
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testDigDug() {
        Board board = new Board(boardRep);
        board.dig(0, 0);
        boolean result = board.dig(1, 3);
        String[] rep = {
            "      1 - 1  ",
            "      1 - 1  ",
            "      1 1 1  ",
            "             ",
            "             ",
            "1 1          ",
            "- 1          "
        };
        assertFalse("expected no explosion", result);
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }
    
    @Test
    public void testDigFlagged() {
        Board board = new Board(boardRep);
        board.dig(0, 0);
        board.flag(1, 4);
        boolean result = board.dig(1, 4);
        String[] rep = {
            "      1 - 1  ",
            "      1 F 1  ",
            "      1 1 1  ",
            "             ",
            "             ",
            "1 1          ",
            "- 1          "
        };
        assertFalse("expected no explosion", result);
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testFlagOutOfRange() {
        Board board = new Board(boardRep);
        board.flag(-1, -1);
        String[] rep = {
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testFlagUntouched() {
        Board board = new Board(boardRep);
        board.flag(1, 4);
        String[] rep = {
            "- - - - - - -",
            "- - - - F - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testFlagDug() {
        Board board = new Board(boardRep);
        board.dig(1, 3);
        board.flag(1, 3);
        String[] rep = {
            "- - - - - - -",
            "- - - 1 - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testDeflagFlagged() {
        Board board = new Board(boardRep);
        board.flag(1, 4);
        board.deflag(1, 4);
        String[] rep = {
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

    @Test
    public void testDeflagDug() {
        Board board = new Board(boardRep);
        board.dig(1, 3);
        board.deflag(1, 3);
        String[] rep = {
            "- - - - - - -",
            "- - - 1 - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -",
            "- - - - - - -"
        };
        assertEquals("expected initial board", String.join("\r\n", rep), board.toString());
    }

}
