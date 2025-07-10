package chess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private Board board;
	private JButton[][] squares;
	private int selectedRow = -1;
	private int selectedCol = -1;
	private List<Point> legalMoves = new ArrayList<>();

	public GUI() {
		board = new Board();
		squares = new JButton[8][8];
		setTitle("Chess Game");
		setLayout(new GridLayout(8, 8));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeBoard();
		pack();
		setVisible(true);
	}

	private void initializeBoard() {
		Piece[][] b = board.getBoard();

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				JButton btn = new JButton();
				btn.setFont(new Font("serif", Font.PLAIN, 32));
				updateButtonIcon(btn, b[row][col]);
				final int r = row, c = col;

				btn.addActionListener(e -> handleClick(r, c));
				squares[row][col] = btn;
				add(btn);
			}
		}
		refreshBoard();

	}

	private void handleClick(int row, int col) {
		Piece[][] b = board.getBoard();
		Piece piece = b[row][col];

		if (selectedRow == -1) {
			// First click - select piece
			if (piece != null && piece.isWhite() == board.isWhiteTurn()) {
				selectedRow = row;
				selectedCol = col;
				legalMoves = getLegalMoves(piece, row, col);
			}
		} else {
			// Second click - attempt move
			if (row == selectedRow && col == selectedCol) {
				// Clicked same square - deselect
				selectedRow = -1;
				selectedCol = -1;
				legalMoves.clear();
			} else {
				// Store current turn before move
				boolean wasWhiteTurn = board.isWhiteTurn();

				if (board.movePiece(selectedRow, selectedCol, row, col)) {
					refreshBoard();

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
		refreshBoard();
	}

	private void refreshBoard() {
		Piece[][] b = board.getBoard();

		// ✅ Detect which king is in check
		Point checkedKing = board.isKingInCheck(board.isWhiteTurn()) ? findKingPosition(board.isWhiteTurn()) : null;

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				JButton btn = squares[row][col];
				updateButtonIcon(btn, b[row][col]);

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

				//Highlight checked king
				if (checkedKing != null && checkedKing.x == row && checkedKing.y == col) {
					btn.setBackground(Color.RED);
				}
			}
		}

	}

	private Point findKingPosition(boolean isWhite) {
		Piece[][] b = board.getBoard();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = b[row][col];
				if (piece instanceof King && piece.isWhite() == isWhite) {
					return new Point(row, col);
				}
			}
		}
		return null;
	}

	private void updateButtonIcon(JButton btn, Piece piece) {
		btn.setText(piece == null ? "" : piece.getSymbol());
	}

	private List<Point> getLegalMoves(Piece piece, int row, int col) {
		List<Point> moves = new ArrayList<>();
		Piece[][] b = board.getBoard();

		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				// Skip current position
				if (r == row && c == col)
					continue;

				// Skip squares with own pieces (already handled in isValidMove)
				if (piece.isValidMove(r, c, b)) {
					// Simulate move to check for self-check
					Piece captured = b[r][c];
					b[r][c] = piece;
					b[row][col] = null;
					piece.setPosition(r, c);

					boolean legal = !board.isKingInCheck(piece.isWhite());

					// Undo simulation
					b[row][col] = piece;
					b[r][c] = captured;
					piece.setPosition(row, col);

					if (legal) {
						moves.add(new Point(r, c));
					}
				}
			}
		}
		return moves;
	}

}