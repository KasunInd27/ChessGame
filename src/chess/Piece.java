package chess;

public abstract class Piece {
	protected int row, col;
	protected boolean isWhite;
	protected boolean hasMoved = false;

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
	
	public boolean hasMoved() {
		return hasMoved;
	}
	public abstract boolean isValidMove(int destRow, int destCol, Piece[][] board);

	public abstract String getSymbol();

	public void setMoved(boolean moved) {
		this.hasMoved = moved;
	}

}
