package game2048;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Daniel Kim
 */
class Model extends Observable {

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to _board[c][r].  Be careful! This is not the usual 2D matrix
     * numbering, where rows are numbered from the top, and the row
     * number is the *first* index. Rather it works like (x, y) coordinates.
     */

    /** Largest piece value. */
    static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    Model(int size) {
        _board = new Tile[size][size];
        _score = _maxScore = 0;
        _gameOver = false;
        _has2048 = false;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there. */
    Tile tile(int col, int row) {
        return _board[col][row];
    }

    /** Return the number of squares on one side of the board. */
    int size() {
        return _board.length;
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current score. */
    int score() {
        return _score;
    }

    /** Return the current maximum game score (updated at end of game). */
    int maxScore() {
        return _maxScore;
    }

    /** Clear the board to empty and reset the score. */
    void clear() {
        _score = 0;
        _gameOver = false;
        for (Tile[] column : _board) {
            Arrays.fill(column, null);
        }
        setChanged();
    }

    /** Add TILE to the board.  There must be no Tile currently at the
     *  same position. */
    void addTile(Tile tile) {
        assert _board[tile.col()][tile.row()] == null;
        _board[tile.col()][tile.row()] = tile;
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *  @author Daniel Kim Slide all the cells to one direction.
     *  Merge tiles if possible and slide again.*/
    boolean tilt(Side side) {
        boolean changed;
        changed = false;
        _beforeTilt = new Tile[size()][size()];
        for (int col1 = 0; col1 < size(); col1++) {
            for (int row1 = 0; row1 < size(); row1++) {
                _beforeTilt[col1][row1] = tile(col1, row1);
            }
        }
        slideAll(side);
        mergeAll(side);
        slideAll(side);
        checkGameOver();
        for (int col1 = 0; col1 < size(); col1++) {
            for (int row1 = 0; row1 < size(); row1++) {
                if (_beforeTilt[col1][row1] != tile(col1, row1)) {
                    changed = true;
                    if (changed) {
                        break;
                    }
                }
            }
            if (changed) {
                break;
            }
        }
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** @author Daniel Kim
     *  Slide the tile towards the coordination found,
     *  in the direction of SIDE, using locate_position.
     *  Starting from the top, slide every single tile. */
    void slideAll(Side side) {
        Tile current;
        int moveRow;
        for (int col1 = 0; col1 < size(); col1++) {
            for (int row1 = size() - 1; row1 >= 0; row1--) {
                current = vtile(col1, row1, side);
                if (current != null) {
                    moveRow = locatePosition(col1, row1, side);
                    setVtile(col1, moveRow, side, current);
                }
            }
        }
    }

    /** @author Daniel Kim
     *  Facing SIDE, if the current tile and the tile below it
     *  have the same value, merge the previous tile to the current tile.
     *  Whenever a merge happens, check if a 2048 is formed and
     *  change the has_2048 instance variable and _score instance
     *  variable accordingly.*/
    void mergeAll(Side side) {
        Tile current, previous;
        int currentValue;
        for (int col1 = 0; col1 < size(); col1++) {
            for (int row1 = size() - 1; row1 >= 1; row1--) {
                current = vtile(col1, row1, side);
                previous = vtile(col1, row1 - 1, side);
                if (current != null && previous != null) {
                    currentValue = current.value();
                    if (currentValue == previous.value()) {
                        setVtile(col1, row1, side, previous);
                        _score = _score + currentValue * 2;

                        if (currentValue == MAX_PIECE / 2) {
                            _has2048 = true;
                        }
                    }

                }
            }
        }
    }

    /** @author Daniel Kim
     *  For the tile in column, COL, and row, ROW, the function
     *  returns the row the current tile should move to, when facing SIDE. */
    int locatePosition(int col, int row, Side side) {
        if ((row == size() - 1) || (vtile(col, row + 1, side) != null)) {
            return row;
        } else {
            return locatePosition(col, row + 1, side);
        }
    }



    /** Return the current Tile at (COL, ROW), when sitting with the board
     *  oriented so that SIDE is at the top (farthest) from you. */
    private Tile vtile(int col, int row, Side side) {
        return _board[side.col(col, row, size())][side.row(col, row, size())];
    }

    /** Move TILE to (COL, ROW), merging with any tile already there,
     *  where (COL, ROW) is as seen when sitting with the board oriented
     *  so that SIDE is at the top (farthest) from you. */
    private void setVtile(int col, int row, Side side, Tile tile) {
        int pcol = side.col(col, row, size()),
            prow = side.row(col, row, size());
        if (tile.col() == pcol && tile.row() == prow) {
            return;
        }
        Tile tile1 = vtile(col, row, side);
        _board[tile.col()][tile.row()] = null;

        if (tile1 == null) {
            _board[pcol][prow] = tile.move(pcol, prow);
        } else {
            _board[pcol][prow] = tile.merge(pcol, prow, tile1);
        }
    }

    /** @author Daniel Kim
     *  Deternmine whether game is over and update _gameOver and _maxScore
     *  accordingly. Check if the _has2048 instance variable has changed
     *  and check full_or_merge to see if every tile is
     *  filled, with no possibility of merging.*/
    private void checkGameOver() {
        if (_has2048 || fullMerge()) {
            _gameOver = true;
            if (_score > _maxScore) {
                _maxScore = _score;
            }
        }
    }

    /** @author Daniel Kim
     * For every cell, check whether it is filled, and if
     * it is, check if it can be merged by
     * a surrounding cell. The function returns a boolean value
     * of whether there is a valid move left.*/
    boolean fullMerge() {
        Tile current, nextCol, nextRow;
        int curV;
        boolean noMove = true;

        for (int col1 = 0; col1 < size(); col1++) {
            for (int row1 = 0; row1 < size(); row1++) {
                current = tile(col1, row1);
                if (current == null) {
                    noMove = false;
                    break;
                }
                curV = current.value();
                if (row1 < size() - 1) {
                    nextRow = tile(col1, row1 + 1);
                    if ((nextRow == null) || (curV == nextRow.value())) {
                        noMove = false;
                        break;
                    }
                }
                if (col1 < size() - 1) {
                    nextCol = tile(col1 + 1, row1);
                    if ((nextCol == null) || (curV == nextCol.value())) {
                        noMove = false;
                        break;
                    }
                }
            }
            if (!noMove) {
                break;
            }
        }
        return noMove;
    }
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        out.format("] %d (max: %d)", score(), maxScore());
        return out.toString();
    }

    /** Current contents of the board. */
    private Tile[][] _board;
    /** Current score. */
    private int _score;
    /** Maximum score so far.  Updated when game ends. */
    private int _maxScore;
    /** True iff game is ended. */
    private boolean _gameOver;
    /** @author Daniel Kim
     * True iff the value of a tile has reached 2048. */
    private boolean _has2048;
    /** @author Daniel Kim
     * Contents of the board before the board has been tilted. */
    private Tile[][] _beforeTilt;
}
