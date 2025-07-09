package chess;

public class Board {
	private Piece[][] board;
	private boolean whiteTurn;

	public Board() {
		board = new Piece[8][8];
		setup();
		whiteTurn = true;
	}

	private void setup() {
		// Setup black pieces
		board[0][0] = new Rook(0, 0, false);
		board[0][1] = new Knight(0, 1, false);
		board[0][2] = new Bishop(0, 2, false);
		board[0][3] = new Queen(0, 3, false);
		board[0][4] = new King(0, 4, false);
		board[0][5] = new Bishop(0, 5, false);
		board[0][6] = new Knight(0, 6, false);
		board[0][7] = new Rook(0, 7, false);
		for (int i = 0; i < 8; i++)
			board[1][i] = new Pawn(1, i, false);

		// Setup white pieces
		board[7][0] = new Rook(7, 0, true);
		board[7][1] = new Knight(7, 1, true);
		board[7][2] = new Bishop(7, 2, true);
		board[7][3] = new Queen(7, 3, true);
		board[7][4] = new King(7, 4, true);
		board[7][5] = new Bishop(7, 5, true);
		board[7][6] = new Knight(7, 6, true);
		board[7][7] = new Rook(7, 7, true);
		for (int i = 0; i < 8; i++)
			board[6][i] = new Pawn(6, i, true);
	}

	public Piece[][] getBoard() {
		return board;
	}

	public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
		Piece piece = board[fromRow][fromCol];
		if (piece == null || piece.isWhite() != whiteTurn || !piece.isValidMove(toRow, toCol, board)) {
			return false;
		}

		board[toRow][toCol] = piece;
		board[fromRow][fromCol] = null;
		piece.setPosition(toRow, toCol);
		whiteTurn = !whiteTurn;
		return true;
	}
}
