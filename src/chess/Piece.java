package chess;

public abstract class Piece {
	protected int row, col;
	protected boolean isWhite;

	public Piece(int row, int col, boolean isWhite) {
		this.row = row;
		this.col = col;
		this.isWhite = isWhite;
	}

	public void setPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public abstract boolean isValidMove(int destRow, int destCol, Piece[][] board);

	public abstract String getSymbol();

}
