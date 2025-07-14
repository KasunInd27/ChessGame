package chess;

public class Knight extends Piece {

	// Constructor: initializes the knight's position and color
	public Knight(int row, int col, boolean isWhite) {
		super(row, col, isWhite);

	}

	// Determines whether the knight can legally move to the destination square
	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		int dr = Math.abs(destRow - row);  // Row difference
		int dc = Math.abs(destCol - col);  // Column difference


		//Knight moves in an "L" shape: 2 in one direction and 1 in the other
		//Destination must be empty or contain an opponent's piece
		return (dr == 2 && dc == 1 || dr == 1 && dc == 2)
				&& (board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite);
	}

	// Returns the Unicode symbol for the knight
	@Override
	public String getSymbol() {

		return isWhite ? "\u2658" : "\u265E";
	}

}
