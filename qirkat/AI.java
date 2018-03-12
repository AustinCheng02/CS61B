package qirkat;

import static qirkat.PieceColor.*;

/** A Player that computes its own moves.
 *  @author Daniel Kim
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 8;
    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        if (myColor() == WHITE) {
            String msg = "White moves " + move.toString() + ".";
            game().reportMove(msg);
        } else {
            String msg = "Black moves " + move.toString() + ".";
            game().reportMove(msg);
        }
        return move;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (myColor() == WHITE) {
            findMove(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            findMove(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;


    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */

    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        Move best;
        best = null;
        if (depth == 0) {
            return staticScore(board);
        } else if (board.gameOver()) {
            return INFTY * (-sense);
        }
        int bestSofar = INFTY * -sense;
        if (sense == 1) {
            for (Move mov : board.getMoves()) {
                board.makeMove(mov);
                int resp = findMove(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undo();
                if (resp >= bestSofar) {
                    best = mov;
                    bestSofar = resp;
                    alpha = Math.max(alpha, resp);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        if (sense == -1) {
            for (Move mov : board.getMoves()) {
                board.makeMove(mov);
                int resp = findMove(board, depth - 1, false,
                        -sense, alpha, beta);
                board.undo();
                if (resp <= bestSofar) {
                    best = mov;
                    bestSofar = resp;
                    beta = Math.min(beta, resp);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }


        if (saveMove) {
            _lastFoundMove = best;
        }

        return bestSofar;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int ret = 0;
        for (int i = 0; i <= 5 * 5 - 1; i++) {
            if (board.get(i) == WHITE) {
                ret += 1;
            } else if (board.get(i) == BLACK) {
                ret -= 1;
            }
        }

        return ret;
    }
}
