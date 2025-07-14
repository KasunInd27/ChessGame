package chess;

public class Rook extends Piece {

	// Constructor: initializes the rook's position and color
	public Rook(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	// Checks if moving to (destRow, destCol) is a valid move for the rook
	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		if (row != destRow && col != destCol)
			return false;

		// Determine direction of movement
		int stepRow = Integer.compare(destRow, row);
		int stepCol = Integer.compare(destCol, col);

		// Start checking one square after the current position
		int r = row + stepRow, c = col + stepCol;
		
		// Traverse the path toward destination
		// If any square in the path is occupied, the move is invalid
		while (r != destRow || c != destCol) {
			if (board[r][c] != null)
				return false; // Blocked by another piece
			r += stepRow;
			c += stepCol;
		}

		// Destination must be empty or occupied by an opponent's piece
		return board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite;
	}

	// Returns the Unicode symbol for the rook
	@Override
	public String getSymbol() {
		return isWhite ? "\u2656" : "\u265C";
	}

}
