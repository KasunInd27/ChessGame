package chess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

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
			// select only if it's current player's piece
			if (piece != null && piece.isWhite() == board.isWhiteTurn()) {
				selectedRow = row;
				selectedCol = col;
				legalMoves = getLegalMoves(piece, row, col);
			}

		} else {
			if (board.movePiece(selectedRow, selectedCol, row, col)) {
				refreshBoard();
			}
			selectedRow = -1;
			selectedCol = -1;
			legalMoves.clear();
		}
		refreshBoard();
	}

	private void refreshBoard() {
		Piece[][] b = board.getBoard();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				JButton btn = squares[row][col];
				updateButtonIcon(squares[row][col], b[row][col]);
				
				//Reset background
				btn.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
				
				//Highlight selected piece
				if (row == selectedRow && col == selectedCol) {
					btn.setBackground(Color.YELLOW);
				}
				
				//Highlight legal moves
				for (Point p : legalMoves) {
					if (p.x == row && p.y == col) {
						btn.setBackground(Color.GREEN);
					}
				}
			}
		}

	}

	private void updateButtonIcon(JButton btn, Piece piece) {
		btn.setText(piece == null ? "" : piece.getSymbol());
	}

	private List<Point> getLegalMoves(Piece piece, int row, int col) {
		List<Point> moves = new ArrayList<>();
		Piece[][] b = board.getBoard();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (piece.isValidMove(r, c, b)) {
					// Check if move doesn't result in self-check
					Piece captured = b[r][c];
					b[r][c] = piece;
					b[row][col] = null;
					piece.setPosition(r, c);
					boolean legal = !boardIsKingInCheckSim(board, piece.isWhite());
					b[row][col] = piece;
					b[r][c] = captured;
					piece.setPosition(row, col);
					if (legal)
						moves.add(new Point(r, c));
				}
			}
		}
		return moves;
	}

	private boolean boardIsKingInCheckSim(Board board, boolean isWhite) {
		try {
			// This assumes isKingInCheck is accessible.
			// If it's private, make it package-private or expose it via a method.
			java.lang.reflect.Method m = Board.class.getDeclaredMethod("isKingInCheck", boolean.class);
			m.setAccessible(true);
			return (boolean) m.invoke(board, isWhite);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
}