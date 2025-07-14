package chess;

//Abstract base class for all chess pieces
public abstract class Piece {
	
	protected int row, col;              // Current position of the piece on the board
	protected boolean isWhite;           // True if the piece belongs to the white player, false if black
	protected boolean hasMoved = false;  // Indicates whether the piece has moved before (used for castling, pawn double-move)

	// Constructor: Initializes a piece with its position and color
	public Piece(int row, int col, boolean isWhite) {
		this.row = row;
		this.col = col;
		this.isWhite = isWhite;
	}

	// Sets the position of the piece (used after a successful move)
	public void setPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}

	// Returns true if the piece is white
	public boolean isWhite() {
		return isWhite;
	}
	
	// Returns true if the piece has moved at least once
	public boolean hasMoved() {
		return hasMoved;
	}
	
	// Abstract method: must be implemented by each subclass to define movement rules
	public abstract boolean isValidMove(int destRow, int destCol, Piece[][] board);

	// Abstract method: must return the Unicode symbol representing the piece
	public abstract String getSymbol();

	// Sets the hasMoved flag (true after the piece moves)
	public void setMoved(boolean moved) {
		this.hasMoved = moved;
	}

}
