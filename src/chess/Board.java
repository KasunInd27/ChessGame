package chess;

import javax.swing.JOptionPane;

public class Board {
	private Piece[][] board;    // 2D array representing the 8x8 chess board
	private boolean whiteTurn;  // true if it's white's turn, false if black's

	// Constructor initializes the board and sets white to start
	public Board() {
		board = new Piece[8][8];
		setup();                  // Place all pieces in starting positions
		whiteTurn = true;         // White starts the game
	}

	// Places all chess pieces at their standard starting positions
	private void setup() {
		// Setup black pieces on row 0 and pawns on row 1
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

		// Setup white pieces on row 7 and pawns on row 6
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

	// Returns the current state of the board
	public Piece[][] getBoard() {
		return board;
	}

	// Attempts to move a piece from one position to another
	public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
		Piece piece = board[fromRow][fromCol];
		
		// Invalid move: empty square, wrong turn, or invalid move rule
		if (piece == null || piece.isWhite() != whiteTurn || !piece.isValidMove(toRow, toCol, board)) {
			return false;
		}
		
		// Prevent capturing your own piece
	    if (board[toRow][toCol] != null && board[toRow][toCol].isWhite() == piece.isWhite()) {
	        return false;
	    }

		// Castling logic
		if (piece instanceof King && Math.abs(toCol - fromCol) == 2 && fromRow == toRow && !piece.hasMoved()) {
			int rookCol = (toCol > fromCol) ? 7 : 0;
			int newRookCol = (toCol > fromCol) ? 5 : 3;
			Piece rook = board[fromRow][rookCol];
			
			// Ensure castling conditions: rook is present and neither piece has moved
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
				whiteTurn = !whiteTurn;  // Switch turn
				return true;
			} else {
				return false;
			}
		}

		//Perform normal move
		Piece captured = board[toRow][toCol];    // Backup captured piece (if any)
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

			// Replace pawn with chosen piece
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

		piece.setMoved(true);    // Mark piece as moved (important for castling/pawns)
		whiteTurn = !whiteTurn;  // Switch turn
		return true;
	}

	// Check detection
	public boolean isKingInCheck(boolean white) {
		int kingRow = -1, kingCol = -1;
		
		// Find king's position
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece instanceof King && piece.isWhite() == white) {
					kingRow = row;
					kingCol = col;
				}
			}
		}

		// Check if any opponent piece can attack the king
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
	public boolean isCheckmateFor(boolean isWhite) {
	    // First verify the king is actually in check
	    if (!isKingInCheck(isWhite)) {
	        return false;
	    }

	 // Try all legal moves for that player to see if any escape check
	    for (int row = 0; row < 8; row++) {
	        for (int col = 0; col < 8; col++) {
	            Piece piece = board[row][col];
	            if (piece != null && piece.isWhite() == isWhite) {
	                // Check all possible moves for this piece
	                for (int r = 0; r < 8; r++) {
	                    for (int c = 0; c < 8; c++) {
	                        if (piece.isValidMove(r, c, board)) {
	                            // Simulate the move
	                            Piece captured = board[r][c];
	                            board[r][c] = piece;
	                            board[row][col] = null;
	                            piece.setPosition(r, c);
	                            
	                            boolean stillInCheck = isKingInCheck(isWhite);
	                            
	                            // Undo the move
	                            board[row][col] = piece;
	                            board[r][c] = captured;
	                            piece.setPosition(row, col);
	                            
	                            if (!stillInCheck) {
	                                return false; // Found at least one legal move
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return true; // No legal moves found
	}

	// Stalemate detection
	public boolean isStalemate() {
		// If the king is in check, it's not stalemate
		if (isKingInCheck(whiteTurn))
			return false;

		// Check if current player has any legal moves
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
		return true;  // No legal moves and not in check = stalemate
	}

	// Returns whose turn it is
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
