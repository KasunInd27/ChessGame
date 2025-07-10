package chess;

public class King extends Piece {

	public King(int row, int col, boolean isWhite) {
		super(row, col, isWhite);

	}

	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
		int dr = Math.abs(destRow - row);
		int dc = Math.abs(destCol - col);
		
		// Normal king move or castling
		return (dr <= 1 && dc <= 1) || (dr == 0 && dc == 2) 
				&& (board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite);
	}

	@Override
	public String getSymbol() {
		return isWhite ? "\u2654" : "\u265A";
	}

}
