package chess;

public class Queen extends Piece {

	// Constructor: initializes the queen's position and color
	public Queen(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	// Determines whether the queen can legally move to the specified destination
	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		// Reuse Rook and Bishop logic for horizontal/vertical/diagonal movement
		return new Rook(row, col, isWhite).isValidMove(destRow, destCol, board)
				|| new Bishop(row, col, isWhite).isValidMove(destRow, destCol, board);
	}

	// Returns the Unicode symbol for the queen
	@Override
	public String getSymbol() {
		return isWhite ? "\u2655" : "\u265B";
	}

}
