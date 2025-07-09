package chess;

public class Rook extends Piece {

	public Rook(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		if (row != destRow && col != destCol)
			return false;

		int stepRow = Integer.compare(destRow, row);
		int stepCol = Integer.compare(destCol, col);

		int r = row + stepRow, c = col + stepCol;
		while (r != destRow || c != destCol) {
			if (board[r][c] != null)
				return false;
			r += stepRow;
			c += stepCol;
		}

		return board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite;
	}

	@Override
	public String getSymbol() {
		return isWhite ? "\u2656" : "\u265C";
	}

}
