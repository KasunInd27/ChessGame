package chess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
//GUI class represents the graphical user interface for the chess game
public class GUI extends JFrame {
	private Board board;                 // Game logic handler
	private JButton[][] squares;         // 2D array of buttons representing the board
	private int selectedRow = -1;        // Coordinates of the selected piece
	private int selectedCol = -1;       
	private List<Point> legalMoves = new ArrayList<>();      // Valid destinations for selected piece

	// Constructor to sets up the window, layout, and initializes board
	public GUI() {
		board = new Board();
		squares = new JButton[8][8];
		setTitle("Chess Game");
		setLayout(new GridLayout(8, 8));        // 8x8 chess board layout          
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeBoard();        // Build board UI
		pack();                   // Resize window to fit components
		setVisible(true);         // Display the window
	}

	// Initializes buttons and places piece symbols on the board
	private void initializeBoard() {
		Piece[][] b = board.getBoard();    // Get current board state

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				JButton btn = new JButton();        // Create a button for each square
				btn.setFont(new Font("serif", Font.PLAIN, 32));     // Set font size for symbols
				updateButtonIcon(btn, b[row][col]);         // Show piece symbol
				final int r = row, c = col;

				// Add click listener for the square
				btn.addActionListener(e -> handleClick(r, c));
				squares[row][col] = btn;
				add(btn);        // Add button to the board UI
			}
		}
		refreshBoard();    // Apply initial colors and highlights

	}

	// Handles player clicking a square (selecting or moving a piece)
	private void handleClick(int row, int col) {
		Piece[][] b = board.getBoard();
		Piece piece = b[row][col];      // Get the clicked square's piece

		if (selectedRow == -1) {
			// First click - select piece if it's the player's turn
			if (piece != null && piece.isWhite() == board.isWhiteTurn()) {
				selectedRow = row;
				selectedCol = col;
				legalMoves = getLegalMoves(piece, row, col);   // Get valid moves
			}
		} else {
			// Second click - attempt move
			if (row == selectedRow && col == selectedCol) {
				// Deselect the piece if clicked again
				selectedRow = -1;
				selectedCol = -1;
				legalMoves.clear();
			} else {
				// Store current turn before move
				boolean wasWhiteTurn = board.isWhiteTurn();

				// Try to move the piece
				if (board.movePiece(selectedRow, selectedCol, row, col)) {
					refreshBoard();  // Update the board visually

					// Check the PREVIOUS player's status (the one who just moved)
					boolean opponentCheckmate = board.isCheckmateFor(!wasWhiteTurn);

					if (opponentCheckmate) {
						String winner = wasWhiteTurn ? "White" : "Black";
						JOptionPane.showMessageDialog(this, "Checkmate! " + winner + " wins the game!", "Game Over",
								JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
			// Reset selection regardless of move success
			selectedRow = -1;
			selectedCol = -1;
			legalMoves.clear();
		}
		refreshBoard(); // Always refresh to update highlights
	}

	// Visually updates the board: symbols, square colors, highlights
	private void refreshBoard() {
		Piece[][] b = board.getBoard();

		// Detect if current player's king is in check
		Point checkedKing = board.isKingInCheck(board.isWhiteTurn()) ? findKingPosition(board.isWhiteTurn()) : null;

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				JButton btn = squares[row][col];
				updateButtonIcon(btn, b[row][col]); // Set piece symbol

				//Default square color
				btn.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);

				//Highlight selected piece
				if (row == selectedRow && col == selectedCol) {
					btn.setBackground(Color.YELLOW);
				}

				//Highlight legal move destinations
				for (Point p : legalMoves) {
					if (p.x == row && p.y == col) {
						btn.setBackground(Color.GREEN);
					}
				}

				// Highlight king in red if it's in check
				if (checkedKing != null && checkedKing.x == row && checkedKing.y == col) {
					btn.setBackground(Color.RED);
				}
			}
		}

	}

	// Finds the current position of the specified color's king
	private Point findKingPosition(boolean isWhite) {
		Piece[][] b = board.getBoard();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = b[row][col];
				if (piece instanceof King && piece.isWhite() == isWhite) {
					return new Point(row, col);  // Return the king's coordinates
				}
			}
		}
		return null;  // Should never happen in a valid game
	}

	// Sets the button's label to the piece's Unicode symbol (or blank)
	private void updateButtonIcon(JButton btn, Piece piece) {
		btn.setText(piece == null ? "" : piece.getSymbol());
	}

	// Returns a list of valid move destinations for a given piece
	private List<Point> getLegalMoves(Piece piece, int row, int col) {
		List<Point> moves = new ArrayList<>();
		Piece[][] b = board.getBoard();

		// Try every square on the board
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				// Skip current position
				if (r == row && c == col)
					continue;

				// Check if the piece is allowed to move there
				if (piece.isValidMove(r, c, b)) {
					// Simulate move to check for self-check
					Piece captured = b[r][c];
					b[r][c] = piece;
					b[row][col] = null;
					piece.setPosition(r, c);

					// Check if the king would be in check after this move
					boolean legal = !board.isKingInCheck(piece.isWhite());

					// Undo simulation
					b[row][col] = piece;
					b[r][c] = captured;
					piece.setPosition(row, col);

					// Only add it if it's legal
					if (legal) {
						moves.add(new Point(r, c));
					}
				}
			}
		}
		return moves;
	}

}