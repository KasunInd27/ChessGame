package chess;

import javax.swing.JOptionPane;

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
		
		//Cannot move empty square or wrong team's turn
		if (piece == null || piece.isWhite() != whiteTurn || !piece.isValidMove(toRow, toCol, board)) {
			return false;
		}
		
		//Block capturing your own piece (this is the critical fix)
	    if (board[toRow][toCol] != null && board[toRow][toCol].isWhite() == piece.isWhite()) {
	        return false;
	    }

		// Castling logic
		if (piece instanceof King && Math.abs(toCol - fromCol) == 2 && fromRow == toRow && !piece.hasMoved()) {
			int rookCol = (toCol > fromCol) ? 7 : 0;
			int newRookCol = (toCol > fromCol) ? 5 : 3;
			Piece rook = board[fromRow][rookCol];
			
			if (rook instanceof Rook && !rook.hasMoved() && !isKingInCheck(whiteTurn)) {
				int dir = (toCol - fromCol) > 0 ? 1 : -1;
				
				// Check all squares between king and rook
				for (int c = fromCol + dir; c != toCol; c += dir) {
					if (board[fromRow][c] != null || squareUnderAttack(fromRow, c, !whiteTurn)) {
						return false;
					}
				}
				// Perform castling
				board[toRow][toCol] = piece;
				board[fromRow][fromCol] = null;
				board[fromRow][rookCol] = null;
				board[fromRow][newRookCol] = rook;
				
				piece.setPosition(toRow, toCol);
				rook.setPosition(fromRow, newRookCol);
				piece.setMoved(true);
				rook.setMoved(true);
				whiteTurn = !whiteTurn;
				return true;
			} else {
				return false;
			}
		}

		//Perform normal move
		Piece captured = board[toRow][toCol];
		board[toRow][toCol] = piece;
		board[fromRow][fromCol] = null;
		piece.setPosition(toRow, toCol);

		//Undo move if it results in your king being in check
		if (isKingInCheck(whiteTurn)) {
			board[fromRow][fromCol] = piece;
			board[toRow][toCol] = captured;
			piece.setPosition(fromRow, fromCol);
			return false;
		}

		//Handle manual pawn promotion
		if (piece instanceof Pawn && (toRow == 0 || toRow == 7)) {
			String[] options = { "Queen", "Rook", "Bishop", "Knight" };
			String choice = (String) JOptionPane.showInputDialog(null, "Promote pawn to:", "Pawn Promotion",
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

			switch (choice) {
			case "Rook":
				board[toRow][toCol] = new Rook(toRow, toCol, piece.isWhite());
				break;
			case "Bishop":
				board[toRow][toCol] = new Bishop(toRow, toCol, piece.isWhite());
				break;
			case "Knight":
				board[toRow][toCol] = new Knight(toRow, toCol, piece.isWhite());
				break;
			default:
				board[toRow][toCol] = new Queen(toRow, toCol, piece.isWhite());
				break;
			}
		}

		piece.setMoved(true);
		whiteTurn = !whiteTurn;
		return true;
	}

	// Check detection
	public boolean isKingInCheck(boolean white) {
		int kingRow = -1, kingCol = -1;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece instanceof King && piece.isWhite() == white) {
					kingRow = row;
					kingCol = col;
				}
			}
		}

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece != null && piece.isWhite() != white && piece.isValidMove(kingRow, kingCol, board)) {
					return true;
				}
			}
		}
		return false;
	}

	// Checkmate detection
	public boolean isCheckmate() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece != null && piece.isWhite() == whiteTurn) {
					for (int r = 0; r < 8; r++) {
						for (int c = 0; c < 8; c++) {
							if (piece.isValidMove(r, c, board)) {
								Piece backup = board[r][c];
								board[r][c] = piece;
								board[row][col] = null;
								piece.setPosition(r, c);
								boolean stillInCheck = isKingInCheck(whiteTurn);
								board[row][col] = piece;
								board[r][c] = backup;
								piece.setPosition(row, col);
								if (!stillInCheck)
									return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	// Stalemate detection
	public boolean isStalemate() {
		if (isKingInCheck(whiteTurn))
			return false;

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece != null && piece.isWhite() == whiteTurn) {
					for (int r = 0; r < 8; r++) {
						for (int c = 0; c < 8; c++) {
							if (piece.isValidMove(r, c, board)) {
								Piece backup = board[r][c];
								board[r][c] = piece;
								board[row][col] = null;
								piece.setPosition(r, c);
								boolean stillInCheck = isKingInCheck(whiteTurn);
								board[row][col] = piece;
								board[r][c] = backup;
								piece.setPosition(row, col);
								if (!stillInCheck)
									return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	public boolean isWhiteTurn() {
		return whiteTurn;
	}

	// Utility: detect if a square is under attack
	public boolean squareUnderAttack(int row, int col, boolean byWhite) {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece piece = board[r][c];
				if (piece != null && piece.isWhite() == byWhite && piece.isValidMove(row, col, board)) {
					return true;
				}
			}
		}
		return false;
	}
}
