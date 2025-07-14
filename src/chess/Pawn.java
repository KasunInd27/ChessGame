package chess;

public class Pawn extends Piece {

	// Constructor: initializes the pawn's position and color
	public Pawn(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	// Determines whether the pawn can legally move to the destination square
	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		// Set movement direction: white moves up (-1), black moves down (+1)
		int direction = isWhite ? -1 : 1;
		
		//Case 1: Move forward to empty square (1 step)
		if (col == destCol && board[destRow][destCol] == null) {
			// Single step forward
			if (destRow - row == direction)
				return true;
			
			//Case 2: First move can be 2 steps forward if both squares are empty
			if ((isWhite && row == 6 || !isWhite && row == 1) && destRow - row == 2 * direction
					&& board[row + direction][col] == null)
				return true;
		}
		
		//Case 3: Capturing diagonally (must be opponent's piece)
		if (Math.abs(destCol - col) == 1 && destRow - row == direction && board[destRow][destCol] != null
				&& board[destRow][destCol].isWhite() != isWhite) {
			return true;
		}
		return false; //Invalid move
	}

	// Returns the Unicode symbol for the pawn
	@Override
	public String getSymbol() {
		return isWhite ? "\u2659" : "\u265F";
	}

}
