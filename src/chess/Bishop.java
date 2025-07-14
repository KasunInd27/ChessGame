package chess;

public class Bishop extends Piece {

	// Constructor: initializes bishop with its position and color
	public Bishop(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	// Checks if a move to (destRow, destCol) is valid for a bishop
	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		// Bishops move diagonally, so the absolute difference between rows and cols must be equal
		if (Math.abs(destRow - row) != Math.abs(destCol - col))
			return false;

		// Determine the direction of movement (positive or negative step)
		int stepRow = Integer.compare(destRow, row);
		int stepCol = Integer.compare(destCol, col);

		// Start checking from the square next to the current position
		int r = row + stepRow, c = col + stepCol;
		
		// Check that all squares between current position and destination are empty
		while (r != destRow && c != destCol) {
			if (board[r][c] != null)
				return false;
			r += stepRow;
			c += stepCol;
		}

		// Return true if the destination is empty or contains an opponent’s piece
		return board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite;
	}

	// Returns the Unicode symbol for the bishop 
	@Override
	public String getSymbol() {
		return isWhite ? "\u2657" : "\u265D";
	}

}
