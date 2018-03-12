package qirkat;

import static qirkat.PieceColor.*;
import static qirkat.Command.Type.*;

/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author Daniel Kim
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
        _prompt = myColor + ": ";
    }

    @Override
    Move myMove() {
        Command comm = game().getMoveCmnd(_prompt);
        if (game().stat() == Game.State.SETUP) {
            return null;
        }
        return Move.parseMove(comm.operands()[0]);
    }


    /** Identifies the player serving as a source of input commands. */
    private String _prompt;
}

