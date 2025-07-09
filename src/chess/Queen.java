package chess;

public class Queen extends Piece {

	public Queen(int row, int col, boolean isWhite) {
		super(row, col, isWhite);
	}

	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		return new Rook(row, col, isWhite).isValidMove(destRow, destCol, board)
				|| new Bishop(row, col, isWhite).isValidMove(destRow, destCol, board);
	}

	@Override
	public String getSymbol() {
		return isWhite ? "\u2655" : "\u265B";
	}

}
