package chess;

public class King extends Piece {

	public King(int row, int col, boolean isWhite) {
		super(row, col, isWhite);

	}

	@Override
	public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
	    int dr = Math.abs(destRow - row);
	    int dc = Math.abs(destCol - col);
	    
	    // Prevent moving to squares occupied by own pieces
	    if (board[destRow][destCol] != null && 
	        board[destRow][destCol].isWhite() == this.isWhite()) {
	        return false;
	    }
	    
	    // Normal king move (1 square in any direction)
	    if (dr <= 1 && dc <= 1) {
	        return true;
	    }
	    
	    // Castling (2 squares horizontally)
	    if (dr == 0 && dc == 2 && !this.hasMoved()) {
	        // Additional castling validation logic remains the same
	        int rookCol = (destCol > col) ? 7 : 0;
	        Piece rook = board[row][rookCol];
	        return (rook instanceof Rook && !rook.hasMoved());
	    }
	    
	    return false;
	}

	@Override
	public String getSymbol() {
		return isWhite ? "\u2654" : "\u265A";
	}

}
