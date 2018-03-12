
public class MoreBoardTests {
	
	// the string representation of this is
	// "  b b b b b\n  b b b b b\n  b b - w w\n  w w w w w\n  w w w w w"
	// feel free to modify this to however you want to represent your board.
    private final char[][] boardRepr = new char[][]{
        {'-', '-', '-', 'w', '-'},
        {'-', 'w', 'w', '-', 'w'},
        {'-', 'w', 'b', '-', '-'},
        {'-', '-', '-', '-', '-'},
        {'-', '-', '-', '-', '-'}
    };

    private final PieceColor currMove = PieceColor.BLACK;

    /**
     * @return the String representation of the initial state. This will
     * be a string in which we concatenate the values from the bottom of 
     * board upwards, so we can pass it into setPieces. Read the comments
     * in Board#setPieces for more information.
     * 
     * For our current boardRepr, the String returned by getInitialRepresentation is
     * "  w w w w w\n  w w w w w\n  b b - w w\n  b b b b b\n  b b b b b"
     *
     * We use a StringBuilder to avoid recreating Strings (because Strings
     * are immutable).
     */
    private String getInitialRepresentation() {
    	StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = boardRepr.length - 1; i >= 0; i--) {
            for (int j = 0; j < boardRepr[0].length; j++) {
                sb.append(boardRepr[i][j] + " ");
            }
            sb.deleteCharAt(sb.length() - 1);
            if (i != 0) {
                sb.append("\n  ");
            }
        }
        return sb.toString();
    }

    // create a new board with the initial state.
    private Board getBoard() {
    	Board b = new Board();
    	b.setPieces(getInitialRepresentation(), currMove);
    	return b;
    }

    // reset board b to initial state.
    private void resetToInitialState(Board b) {
    	b.setPieces(getInitialRepresentation(), currMove);
    }

    @Test
    public void testSomething() {
    	Board b = getBoard();
    	String[] moves1 = { "c3-c1-c5-e5-e3", "c4-d4"}
    	for (String s : moves1) {
            b.makeMove(Move.parseMove(s));
        }
    	String board1 = " - - - - -\n - - - w -\n - - - - b\n - - - - -\n - - - - -";
    	assertEquals(board1, b.toString);
    	assertEquals(BLACK, b.whosemove());
    	resetToInitialState(b);
    	String[] moves2 = {"c3-c1-c5-c3", "e4-d4", "c3-e5-c3"}
    	for (String s : moves2) {
            b.makeMove(Move.parseMove(s));
        }
    	String board2 = " - - b - -\n - - - - -\n - - - - -\n - - - - -\n - - - - -";
    	assertEquals(board2, b.toString);
    };
    	// write things to test here
    }
}