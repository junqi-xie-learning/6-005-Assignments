/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

/**
 * Square represents a mutable and thread-safe board with a grid of squares.
 */
public class Board {

    private final Square[][] board;

    // Abstraction function:
    //   represents a grid of squares as board[X][Y]
    // Rep invariant:
    //   X, Y are positive
    // Safety from rep exposure:
    //   board is never returned, and is declared as private final
    // Thread safety argument:
    //   all accesses to board happen within Board methods,
    //     which are all guarded by Board's lock
    
    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        assert getColumn() > 0;
        assert getRow() > 0;
    }

    /**
     * Get number of columns in the board.
     * 
     * @return number of columns in the board
     */
    public int getColumn() {
        return board.length;
    }

    /**
     * Get number of rows in the board.
     * 
     * @return number of rows in the board
     */
    public int getRow() {
        return board[0].length;
    }

    /**
     * Check whether the square is in the range of the board.
     * 
     * @param x x coordinate of the square
     * @param y y coordinate of the square
     * @return whether the square is in the range of the board
     */
    private boolean checkRange(int x, int y) {
        return 0 <= x && x < getColumn() && 0 <= y && y < getRow();
    }

    /**
     * Count bombs in the neighbors of square (x, y), including itself.
     * 
     * @param boardRep board representation in integers
     * @param x x coordinate of the square, 0 <= x < X
     * @param y y coordinate of the square, 0 <= y < Y
     * @return number of bombs in its neighbors
     */
    private int countNeighbors(int[][] boardRep, int x, int y) {
        assert checkRange(x, y);

        int count = 0;
        for (int i = Math.max(0, x - 1); i < Math.min(getColumn(), x + 2); i++) {
            for (int j = Math.max(0, y - 1); j < Math.min(getRow(), y + 2); j++) {
                count += boardRep[i][j];
            }
        }
        return count;
    }

    /**
     * Make a board according to its representation in integers.
     * 
     * @param boardRep board representation in integers
     */
    public Board(int[][] boardRep) {
        assert boardRep.length > 0;
        assert boardRep[0].length > 0;
        board = new Square[boardRep.length][boardRep[0].length];
        
        for (int i = 0; i < getColumn(); i++) {
            for (int j = 0; j < getRow(); j++) {
                board[i][j] = new Square(
                    Square.Status.UNTOUCHED,
                    boardRep[i][j] == 1 ? true : false,
                    countNeighbors(boardRep, i, j));
            }
        }
        checkRep();
    }

    /**
     * Recursively dig neighboring squares of count 0.
     * 
     * @param x x coordinate of the square
     * @param y y coordinate of the square
     */
    private void digNeighbors(int x, int y) {
        if (checkRange(x, y) && board[x][y].getStatus() == Square.Status.UNTOUCHED) {
            board[x][y].setStatus(Square.Status.DUG);
            if (board[x][y].getCount() == 0) {
                for (int i = Math.max(0, x - 1); i < Math.min(getColumn(), x + 2); i++) {
                    for (int j = Math.max(0, y - 1); j < Math.min(getRow(), y + 2); j++) {
                        digNeighbors(i, j);
                    }
                }
            }
        }
    }

    /**
     * Handle DIG request from the user.
     * 
     * @param x x coordinate of the square to dig
     * @param y y coordinate of the square to dig
     * @return whether this request cause a bomb to explode
     */
    public synchronized boolean dig(int x, int y) {
        if (checkRange(x, y) && board[x][y].getStatus() == Square.Status.UNTOUCHED) {
            if (board[x][y].getBomb()) {
                board[x][y].setBomb(false);
                for (int i = Math.max(0, x - 1); i < Math.min(getColumn(), x + 2); i++) {
                    for (int j = Math.max(0, y - 1); j < Math.min(getRow(), y + 2); j++) {
                        board[i][j].setCount(board[i][j].getCount() - 1);
                    }
                }
                digNeighbors(x, y);
                return true;
            }
            else if (board[x][y].getCount() == 0) {
                digNeighbors(x, y);
            }
            else {
                board[x][y].setStatus(Square.Status.DUG);
            }
        }
        return false;
    }

    /**
     * Handle FLAG request from the user.
     * 
     * @param x x coordinate of the square to flag
     * @param y y coordinate of the square to flag
     */
    public synchronized void flag(int x, int y) {
        if (checkRange(x, y) && board[x][y].getStatus() == Square.Status.UNTOUCHED) {
            board[x][y].setStatus(Square.Status.FLAGGED);
        }
    }

    /**
     * Handle DEFLAG request from the user.
     * 
     * @param x x coordinate of the square to deflag
     * @param y y coordinate of the square to deflag
     */
    public synchronized void deflag(int x, int y) {
        if (checkRange(x, y) && board[x][y].getStatus() == Square.Status.FLAGGED) {
            board[x][y].setStatus(Square.Status.UNTOUCHED);
        }
    }

    @Override
    public synchronized String toString() {
        String[][] boardRep = new String[getColumn()][getRow()];
        for (int i = 0; i < getColumn(); i++) {
            for (int j = 0; j < getRow(); j++) {
                boardRep[i][j] = board[i][j].toString();
            }
        }
        
        String[] lineRep = new String[getColumn()];
        for (int i = 0; i < getColumn(); i++) {
            lineRep[i] = String.join(" ", boardRep[i]);
        }
        return String.join("\r\n", lineRep);
    }
    
}

/**
 * Square represents a mutable and thread-unsafe square in the board.
 * Each square is either _flagged_, _dug_, or _untouched_.
 * Each square either contains a bomb, or does not contain a bomb.
 * Each square stores the count of its neighbors that have a bomb.
 */
class Square {

    public enum Status {
        UNTOUCHED, FLAGGED, DUG
    }

    private Status status;
    private boolean bomb;
    private int count;

    // Abstraction function:
    //   represents a square with status and may contain a bomb
    // Rep invariant:
    //   count is nonnegative
    // Safety from rep exposure:
    //   Status, boolean and int are immutable, and are declared as private

    /**
     * Make a square.
     * 
     * @param status initial status of the square
     * @param bomb initial bomb indication of the square
     * @param count initial count of neighbors that have a bomb
     * @return a square with status and may contain a bomb
     */
    public Square(Status status, boolean bomb, int count) {
        this.status = status;
        this.bomb = bomb;
        this.count = count;
        checkRep();
    }

    /**
     * Check the rep invariant.
     */
    private void checkRep() {
        assert count >= 0;
    }

    /**
     * Get status of the square.
     * 
     * @return current status of the square
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set status of the square.
     * 
     * @param status new status of the square
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Get bomb indication of the square.
     * 
     * @return current bomb indication of the square
     */
    public boolean getBomb() {
        return bomb;
    }

    /**
     * Set bomb indication of the square.
     * 
     * @param bomb new bomb indication of the square
     */
    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    /**
     * Get count of neighbors that have a bomb.
     * 
     * @return current count of neighbors that have a bomb
     */
    public int getCount() {
        return count;
    }

    /**
     * Set count of neighbors that have a bomb.
     * 
     * @param count new count of neighbors that have a bomb
     */
    public void setCount(int count) {
        this.count = count;
        checkRep();
    }

    @Override
    public String toString() {
        switch (status) {
            case UNTOUCHED:
                return "-";
            case FLAGGED:
                return "F";
            case DUG:
                return count == 0 ? " " : Integer.toString(count);
            default:
                assert false;  // should not reach here
                return super.toString();
        }
    }

}
