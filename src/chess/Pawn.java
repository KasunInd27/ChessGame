package chess;

public class Pawn extends Piece {

	public Pawn(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		int direction = isWhite ? -1 : 1;
		if (col == destCol && board[destRow][destCol] == null) {
			if (destRow - row == direction)
				return true;
			if ((isWhite && row == 6 || !isWhite && row == 1) && destRow - row == 2 * direction
					&& board[row + direction][col] == null)
				return true;
		}
		if (Math.abs(destCol - col) == 1 && destRow - row == direction && board[destRow][destCol] != null
				&& board[destRow][destCol].isWhite() != isWhite) {
			return true;
		}
		return false;
	}

	@Override
	public String getSymbol() {
		return isWhite ? "\u2659" : "\u265F";
	}

}
