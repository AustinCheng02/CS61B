package qirkat;


import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Formatter;
import java.util.Observer;
import java.util.Arrays;

import static qirkat.PieceColor.*;
import static qirkat.Move.*;

/** A Qirkat board.   The squares are labeled by column (a char value between
 *  'a' and 'e') and row (a char value between '1' and '5'.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (with row 0 being the bottom row)
 *  counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author Daniel Kim
 */
class Board extends Observable {

    /** A new, cleared board at the start of the game. */
    Board() {
        _initial = " w w w w w w w w w w b b - w w b b b b b b b b b b";
        clear();
    }

    /** A copy of B. */
    Board(Board b) {
        internalCopy(b);
    }

    /** Return a constant view of me (allows any access method, but no
     *  method that modifies it). */
    Board constantView() {
        return this.new ConstantBoard();
    }

    /** Updates the initial setup of the board to the string given.
     *
     * @param str - The setup of the board.
     */
    void updateInit(String str) {
        _initial = str;
    }

    /** The method @return the Initial setup as String. */
    String initial() {
        return _initial;
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions. */
    void clear() {
        _whoseMove = WHITE;
        _gameOver = false;
        _map = new HashMap<>();
        _track = new int[MAX_INDEX + 1];
        setPieces(_initial, _whoseMove);
        _boards = new Stack<Board>();
        _boards.push(new Board(this));
    }

    /** Copy B into me. */
    void copy(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        _whoseMove = b.whoseMove();
        _map = (HashMap<Integer, PieceColor>) b.map().clone();
        _gameOver = b.gameOver();
        _track = b.track().clone();
        _boards = (Stack<Board>) b.boards().clone();
        _initial = b.initial();
    }

    /** Set my contents as defined by STR.  STR consists of 25 characters,
     *  each of which is b, w, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board in row-major order, starting
     *  with the bottom row (row 1) and left column (column a). All squares
     *  are initialized to allow horizontal movement in either direction.
     *  NEXTMOVE indicates whose move it is.
     */
    void setPieces(String str, PieceColor nextMove) {
        if (nextMove == EMPTY || nextMove == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }


        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
            case '-':
                set(k, EMPTY);
                break;
            case 'b': case 'B':
                set(k, BLACK);
                break;
            case 'w': case 'W':
                set(k, WHITE);
                break;
            default:
                break;
            }
        }
        _whoseMove = nextMove;
        if (!isMove()) {
            _gameOver = true;
        }
        _whoseMove = nextMove;
        String view = toString();
        setChanged();
        notifyObservers();
    }

    /** The method returns the array track of moving left or right.
     */
    int[] track() {
        return _track;
    }

    /** Returns the current map of the pieces.
     */
    HashMap<Integer, PieceColor> map() {
        return _map;
    }

    /** Return true iff the game is over: i.e., if the current player has
     *  no moves. */
    boolean gameOver() {
        return _gameOver;
    }

    /** Returns a stack of boards for past moves.
     */
    Stack<Board> boards() {
        return _boards;
    }

    /** Return the current contents of square C R, where 'a' <= C <= 'e',
     *  and '1' <= R <= '5'.  */
    PieceColor get(char c, char r) {
        assert validSquare(c, r);
        return get(index(c, r));
    }

    /** Return the current contents of the square at linearized index K. */
    PieceColor get(int k) {
        assert validSquare(k);
        return _map.get(k);
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'e', and
     *  '1' <= R <= '5'. */
    private void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);
    }

    /** Set get(K) to V, where K is the linearized index of a square. */
    private void set(int k, PieceColor v) {
        assert validSquare(k);
        _map.remove(k);
        _map.put(k, v);
    }

    /** Return true iff MOV is legal on the current board. */
    boolean legalMove(Move mov) {
        if (get(mov.fromIndex()) != _whoseMove) {
            return false;
        }

        if (mov.isJump()) {
            if (jumpPossible(mov.fromIndex())) {
                return checkJump(mov, false);
            } else {
                return false;
            }

        } else {
            if (mov.isVestigial() || (mov.isRightMove()
                    && _track[mov.fromIndex()] < 0) || (mov.isLeftMove()
                    && _track[mov.fromIndex()] > 0)) {
                return false;
            }
            if (_whoseMove == WHITE) {
                if (mov.fromIndex() - mov.toIndex() > 1) {
                    return false;
                }
            } else {
                if (mov.toIndex() - mov.fromIndex() > 1) {
                    return false;
                }
            }
            ArrayList<Move> move = new ArrayList<>();
            getMoves(move, mov.fromIndex());
            return move.contains(mov);
        }
    }

    /** Return a list of all legal moves from the current position. */
    ArrayList<Move> getMoves() {
        ArrayList<Move> result = new ArrayList<>();
        getMoves(result);
        if (result.isEmpty()) {
            _gameOver = true;
        }
        return result;
    }

    /** Add all legal moves from the current position to MOVES. */
    void getMoves(ArrayList<Move> moves) {
        if (jumpPossible()) {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                if (get(k) == _whoseMove) {
                    getJumps(moves, k);
                }
            }
        } else {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                if (get(k) == _whoseMove) {
                    getMoves(moves, k);
                }
            }
        }
    }

    /** Add all legal non-capturing moves from the position
     *  with linearized index K to MOVES. */
    private void getMoves(ArrayList<Move> moves, int k) {
        if (get(k) != _whoseMove || (_whoseMove == WHITE && k >= MAX_INDEX - 4)
                || (_whoseMove == BLACK && k <= 4)) {
            return;
        }
        if (jumpPossible(k)) {
            return;
        }
        if (_track[k] >= 0) {
            if (validSquare((char) (col(k) + 1), row(k))
                    && (get(k + 1) == EMPTY)) {
                moves.add(Move.move(col(k), row(k), col(k + 1), row(k + 1)));
            }
        }
        if (_track[k] <= 0) {
            if (validSquare((char) (col(k) - 1),
                    row(k)) && (get(k - 1) == EMPTY)) {
                moves.add(Move.move(col(k), row(k), col(k - 1), row(k - 1)));
            }
        }
        if (_whoseMove == WHITE) {
            if (k % 2 == 0) {
                if (validSquare((char) (col(k) + 1),
                        (char) (row(k) + 1)) && (get(k + 6) == EMPTY)) {
                    moves.add(Move.move(col(k), row(k),
                            col(k + 6), row(k + 6)));
                }
                if (validSquare((char) (col(k) - 1),
                        (char) (row(k) + 1)) && (get(k + 4) == EMPTY)) {
                    moves.add(Move.move(col(k), row(k),
                            col(k + 4), row(k + 4)));
                }
            }
            if (validSquare(k + 5) && (get(k + 5) == EMPTY)) {
                moves.add(Move.move(col(k), row(k),
                        col(k + 5), row(k + 5)));
            }
        } else {
            if (k % 2 == 0) {
                if (validSquare((char) (col(k) + 1),
                        (char) (row(k) - 1)) && (get(k - 4) == EMPTY)) {
                    moves.add(Move.move(col(k), row(k),
                            col(k - 4), row(k - 4)));
                }
                if (validSquare((char) (col(k) - 1),
                        (char) (row(k) - 1)) && (get(k - 6) == EMPTY)) {
                    moves.add(Move.move(col(k), row(k),
                            col(k - 6), row(k - 6)));
                }
            }
            if (validSquare(k - 5) && (get(k - 5) == EMPTY)) {
                moves.add(Move.move(col(k), row(k),
                        col(k - 5), row(k - 5)));
            }
        }
    }

    /** Finds valid list of indexes to jump to.
     *
     * @param k Is a index number.
     * @return ArrayList of valid indexes.
     */
    private ArrayList<Integer> findValid(int k) {
        ArrayList<Integer> validSq = new ArrayList<>();
        if (validSquare((char) (col(k) - 2), row(k))
                && (get(k - 2) == EMPTY)
                && (_whoseMove.opposite() == get(k - 1))) {
            validSq.add(k - 2);
        }
        if (validSquare((char) (col(k) + 2), row(k))
                && (get(k + 2) == EMPTY)
                && (_whoseMove.opposite() == get(k + 1))) {
            validSq.add(k + 2);
        }
        if (validSquare(k + 10) && (get(k + 10) == EMPTY)
                && (_whoseMove.opposite() == get(k + 5))) {
            validSq.add(k + 10);
        }
        if (validSquare(k - 10) && (get(k - 10) == EMPTY)
                && (_whoseMove.opposite() == get(k - 5))) {
            validSq.add(k - 10);
        }

        if (k % 2 == 0) {
            if (validSquare((char) (col(k) - 2), (char) (row(k) - 2))
                    && (get(k - 12) == EMPTY)
                    && (_whoseMove.opposite() == get(k - 6))) {
                validSq.add(k - 12);
            }
            if (validSquare((char) (col(k) + 2), (char) (row(k) + 2))
                    && (get(k + 12) == EMPTY)
                    && (_whoseMove.opposite() == get(k + 6))) {
                validSq.add(k + 12);
            }
            if (validSquare((char) (col(k) - 2), (char) (row(k) + 2))
                    && (get(k + 8) == EMPTY)
                    && (_whoseMove.opposite() == get(k + 4))) {
                validSq.add(k + 8);
            }
            if (validSquare((char) (col(k) + 2), (char) (row(k) - 2))
                    && (get(k - 8) == EMPTY)
                    && (_whoseMove.opposite() == get(k - 4))) {
                validSq.add(k - 8);
            }
        }
        return validSq;
    }

    /** Add all legal captures from the position with linearized index K
     *  to MOVES. */
    private void getJumps(ArrayList<Move> moves, int k) {
        if (get(k) != _whoseMove) {
            return;
        }
        ArrayList<Integer> validSq = findValid(k);
        for (int i : validSq) {
            Move jump = Move.move(col(k), row(k), col(i), row(i));
            setPre(jump);
            getJumps(moves, i, jump);
            reset(jump);
        }
    }

    /** This is a helper function that traverses a tree
     * and adds the move to the arraylist.
     * @param moves The arraylist that is being added.
     * @param k The index number.
     * @param store The move that is being added.
     */
    private void getJumps(ArrayList<Move> moves, int k, Move store) {
        ArrayList<Integer> validSq = findValid(k);
        if (validSq.size() == 0) {
            moves.add(store);
            return;
        }
        for (int i : validSq) {
            Move jump = Move.move(col(k), row(k), col(i), row(i));
            Move backup = store;
            store = Move.move(store, jump);
            setPre(jump);
            getJumps(moves, i, store);
            reset(jump);
            store = backup;
        }
    }

    /** Sets the pieces for each move.
     *
     * @param mov This is a move that is being set up.
     */
    void setPre(Move mov) {
        set(mov.fromIndex(), EMPTY);
        set(mov.toIndex(), _whoseMove);
        set(mov.jumpedIndex(), EMPTY);
    }

    /** Restores the pieces.
     *
     * @param mov This is a move that is being restored.
     */
    void reset(Move mov) {
        set(mov.fromIndex(), _whoseMove);
        set(mov.toIndex(), EMPTY);
        set(mov.jumpedIndex(), _whoseMove.opposite());
    }

    /** Return true iff MOV is a valid jump sequence on the current board.
     *  MOV must be a jump or null.  If ALLOWPARTIAL, allow jumps that
     *  could be continued and are valid as far as they go.  */
    boolean checkJump(Move mov, boolean allowPartial) {
        if (mov == null) {
            return true;
        }
        if ((get(mov.col1(), mov.row1()) != EMPTY)
                || (get(mov.jumpedIndex())
                != get(mov.fromIndex()).opposite())) {
            return false;
        } else if (allowPartial) {
            return allowPartial;
        }
        boolean ret;
        setPre(mov);
        if ((mov.jumpTail() == null) && (jumpPossible(mov.toIndex()))) {
            ret = false;
        } else {
            ret = checkJump(mov.jumpTail(), allowPartial);
        }
        reset(mov);

        return ret;
    }

    /** Return true iff a jump is possible for a piece at position C R. */
    boolean jumpPossible(char c, char r) {
        return jumpPossible(index(c, r));
    }

    /** Return true iff a jump is possible for a piece at position with
     *  linearized index K. */
    boolean jumpPossible(int k) {
        if (get(k) != _whoseMove) {
            return false;
        }
        if (validSquare((char) (col(k) - 2), row(k)) && (get(k - 2) == EMPTY)
                && (_whoseMove.opposite() == get(k - 1))) {
            return true;
        }
        if (validSquare((char) (col(k) + 2), row(k)) && (get(k + 2) == EMPTY)
                && (_whoseMove.opposite() == get(k + 1))) {
            return true;
        }
        if (validSquare(k + 10) && (get(k + 10) == EMPTY)
                && (_whoseMove.opposite() == get(k + 5))) {
            return true;
        }
        if (validSquare(k - 10) && (get(k - 10) == EMPTY)
                && (_whoseMove.opposite() == get(k - 5))) {
            return true;
        }

        if (k % 2 == 0) {
            if (validSquare((char) (col(k) - 2), (char) (row(k) - 2))
                    && (get(k - 12) == EMPTY)
                    && (_whoseMove.opposite() == get(k - 6))) {
                return true;
            }
            if (validSquare((char) (col(k) + 2), (char) (row(k) + 2))
                    && (get(k + 12) == EMPTY)
                    && (_whoseMove.opposite() == get(k + 6))) {
                return true;
            }
            if (validSquare((char) (col(k) - 2), (char) (row(k) + 2))
                    && (get(k + 8) == EMPTY)
                    && (_whoseMove.opposite() == get(k + 4))) {
                return true;
            }
            if (validSquare((char) (col(k) + 2), (char) (row(k) - 2))
                    && (get(k - 8) == EMPTY)
                    && (_whoseMove.opposite() == get(k - 4))) {
                return true;
            }
        }

        return false;
    }

    /** Return true iff a jump is possible from the current board. */
    boolean jumpPossible() {
        for (int k = 0; k <= MAX_INDEX; k += 1) {
            if (jumpPossible(k)) {
                return true;
            }
        }
        return false;
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Perform the move C0R0-C1R1. Assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        makeMove(Move.move(c0, r0, c1, r1, null));
    }

    /** Make the multi-jump C0 R0-C1 R1..., where NEXT is C1R1....
     *  Assumes the result is legal. */
    void makeMove(char c0, char r0, char c1, char r1, Move next) {
        makeMove(Move.move(c0, r0, c1, r1, next));
    }

    /** Make the Move MOV on this Board, assuming it is legal. */
    void makeMove(Move mov) {
        Boolean asse = legalMove(mov);
        if (!asse) {
            return;
        }

        Board stac = new Board(this);
        _boards.push(stac);
        if (mov.isJump()) {
            Move jump = mov;
            while (jump != null) {
                _track[mov.jumpedIndex()] = 0;
                setPre(jump);
                jump = jump.jumpTail();
            }
        } else {
            if (mov.isLeftMove()) {
                _track[mov.toIndex()] -= 1;
            } else if (mov.isRightMove()) {
                _track[mov.toIndex()] += 1;
            }
            set(mov.toIndex(), _whoseMove);
            set(mov.fromIndex(), EMPTY);
        }
        _track[mov.fromIndex()] = 0;
        _whoseMove = _whoseMove.opposite();
        if (!isMove()) {
            _gameOver = true;
        }
        setChanged();
        notifyObservers();
    }

    /** Undo the last move, if any. */
    void undo() {
        internalCopy(_boards.pop());
        setChanged();
        notifyObservers();
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return _boards.equals(b.boards()) && Arrays.equals(_track, b.track())
                && _initial.equals(b.initial()) && _gameOver == b.gameOver()
                && _whoseMove == b.whoseMove() && _map.equals(b.map());
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /** Return a text depiction of the board.  If LEGEND, supply row and
     *  column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();
        for (int i = MAX_INDEX - 4; i > 0; i++) {
            if (i % 5 == 0) {
                out.format(" ");
            }
            out.format(" " + _map.get(i).shortName());
            if (i % 5 == 4) {
                out.format("\n");
                i = i - 10;
            }
        }
        out.format(" ");
        for (int i = 0; i <= 4; i++) {
            out.format(" " + _map.get(i).shortName());
        }
        return out.toString();
    }
    /** Stores a stack of past moves. */
    private Stack<Board> _boards;

    /** Stores locations for each pieces. */
    private int[] _track;

    /** create two for undo?
     * Stores Color values to keys, index
     */
    private HashMap<Integer, PieceColor> _map;

    /** A String of board pieces for the initial setup.
     */
    private String _initial;

    /** Return true iff there is a move for the current player. */
    private boolean isMove() {
        return !getMoves().isEmpty();
    }

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Set true when game ends. */
    private boolean _gameOver;

    /** Convenience value giving values of pieces at each ordinal position. */
    static final PieceColor[] PIECE_VALUES = PieceColor.values();

    /** One cannot create arrays of ArrayList<Move>, so we introduce
     *  a specialized private list type for this purpose. */
    private static class MoveList extends ArrayList<Move> {
    }

    /** A read-only view of a Board. */
    private class ConstantBoard extends Board implements Observer {
        /** A constant view of this Board. */
        ConstantBoard() {
            super(Board.this);
            Board.this.addObserver(this);
        }

        @Override
        void copy(Board b) {
            assert false;
        }

        @Override
        void clear() {
            assert false;
        }

        @Override
        void makeMove(Move move) {
            assert false;
        }

        /** Undo the last move. */
        @Override
        void undo() {
            assert false;
        }

        @Override
        public void update(Observable obs, Object arg) {
            super.copy((Board) obs);
            setChanged();
            notifyObservers(arg);
        }
    }
}
