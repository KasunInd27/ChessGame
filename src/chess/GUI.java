package chess;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
	private Board board;
	private JButton[][] squares;
	private int selectedRow = -1;
	private int selectedCol = -1;

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

	}

	private void handleClick(int row, int col) {
		if (selectedRow == -1) {
			selectedRow = row;
			selectedCol = col;
		} else {
			if (board.movePiece(selectedRow, selectedCol, row, col)) {
				refreshBoard();
			}
			selectedRow = -1;
			selectedCol = -1;
		}

	}

	private void refreshBoard() {
		Piece[][] b = board.getBoard();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				updateButtonIcon(squares[row][col], b[row][col]);
			}
		}
		
	}

	private void updateButtonIcon(JButton btn, Piece piece) {
		btn.setText(piece == null ? "" : piece.getSymbol());
	}

}
