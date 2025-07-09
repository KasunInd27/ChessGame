package chess;

public class Knight extends Piece {

	public Knight(int row, int col, boolean isWhite) {
		super(row, col, isWhite);

	}

	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		int dr = Math.abs(destRow - row);
		int dc = Math.abs(destCol - col);

		return (dr == 2 && dc == 1 || dr == 1 && dc == 2)
				&& (board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite);
	}

	@Override
	public String getSymbol() {

		return isWhite ? "\u2658" : "\u265E";
	}

}
