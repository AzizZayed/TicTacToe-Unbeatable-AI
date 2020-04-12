package com.codingheaven.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class TicTacToeBoard {

	private final short SIZE = 3; // size of the game, n x n tic-tac-toe!
	private int gridSize = 150; // size of an image, images are squares

	private char grid[][]; // main data structure
	private final char PLAYER_CHAR = 'x'; // player is always x
	private final char AI_CHAR = 'o'; // computer is always o
	private final char EMPTY_CHAR = ' '; // placeholder for an empty cell

	private char winner = EMPTY_CHAR; // winner

	private short freeCells; // number of cells that are empty
	private BufferedImage xpic, opic; // images of x and o

	private TicTacToeGame gameReference;

	public TicTacToeBoard(TicTacToeGame game) {
		gameReference = game;
		loadImages();
		initGrid();
	}

	/**
	 * @return the gridSize
	 */
	public int getGridSize() {
		return gridSize;
	}

	/**
	 * @return the size
	 */
	public short getSize() {
		return SIZE;
	}

	/**
	 * Load all the images out of memory, x and o images done in Microsoft paint
	 */
	private void loadImages() {
		try {
			xpic = ImageIO.read(new File("res/xpic.png"));
			opic = ImageIO.read(new File("res/opic.png"));
			gridSize = opic.getWidth();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialize the game grid, the data structure
	 */
	private void initGrid() {
		winner = EMPTY_CHAR;
		grid = new char[SIZE][SIZE];

		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				grid[i][j] = EMPTY_CHAR;

		freeCells = SIZE * SIZE;
	}

	/**
	 * called when a cell is clicked, checks if cell is empty and then triggers AI
	 * 
	 * @param i, horizontal index of cell clicked
	 * @param j, vertical index of cell clicked
	 */
	public void cellClicked(int x, int y) {
		int i = x / gridSize;
		int j = y / gridSize;

		char cell = grid[i][j];

		if (cell != EMPTY_CHAR)
			return;

		grid[i][j] = PLAYER_CHAR;
		freeCells--;

		gameReference.repaint();

		if (winCheck()) {
			gameWon();
			return;
		}

		if (tieCheck()) {
			gameTied();
			return;
		}

		triggerAI();

		gameReference.repaint();

		if (winCheck()) {
			gameWon();
		}

		if (tieCheck()) {
			gameTied();
		}

	}

	/**
	 * Check if the game is a tie
	 * 
	 * @return true if the game is a tie, false if otherwise
	 */
	public boolean tieCheck() {
		return (freeCells == 0);
	}

	private void gameTied() {
		int y = JOptionPane.showConfirmDialog(gameReference, "It's a Tie. Restart? ", " Tic Tac Toe ",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (y == JOptionPane.YES_OPTION)
			initGrid();
	}

	/**
	 * Checks if one of the 2 opponents won the game
	 * 
	 * @return true if a winner exits, false otherwise
	 */
	public boolean winCheck() {
		char winnerChar = EMPTY_CHAR;

		int j, i;

		String winTextPlayer = Character.toString(PLAYER_CHAR); // = "xxx" after the for loop below
		String winTextAI = Character.toString(AI_CHAR); // = "ooo" after the for loop below
		String row;

		for (i = 1; i < SIZE; i++) {
			winTextPlayer += PLAYER_CHAR;
			winTextAI += AI_CHAR;
		}

		// Horizontal checks
		j = 0;
		while (winnerChar == EMPTY_CHAR && j < SIZE) {
			row = "";

			for (i = 0; i < SIZE; i++)
				row += grid[i][j];

			if (row.equals(winTextPlayer))
				winnerChar = PLAYER_CHAR;
			else if (row.equals(winTextAI))
				winnerChar = AI_CHAR;

			j++;
		}

		// vertical checks
		i = 0;
		while (winnerChar == EMPTY_CHAR && i < SIZE) {
			row = "";

			for (j = 0; j < SIZE; j++)
				row += grid[i][j];

			if (row.equals(winTextPlayer))
				winnerChar = PLAYER_CHAR;
			else if (row.equals(winTextAI))
				winnerChar = AI_CHAR;

			i++;
		}

		// diagonal checks
		row = "";
		for (i = 0, j = 0; i < SIZE && j < SIZE; i++, j++)
			row += grid[i][j];

		if (row.equals(winTextPlayer))
			winnerChar = PLAYER_CHAR;
		else if (row.equals(winTextAI))
			winnerChar = AI_CHAR;

		row = "";
		for (i = 0, j = SIZE - 1; i < SIZE && j >= 0; i++, j--)
			row += grid[i][j];

		if (row.equals(winTextPlayer))
			winnerChar = PLAYER_CHAR;
		else if (row.equals(winTextAI))
			winnerChar = AI_CHAR;

		// message box
		winner = winnerChar;
		return (winnerChar != EMPTY_CHAR);
	}

	private void gameWon() {
		JOptionPane.showMessageDialog(gameReference, Character.toUpperCase(winner) + " Won! Restarting.",
				" Tic Tac Toe ", JOptionPane.INFORMATION_MESSAGE);

		initGrid();
	}

	/**
	 * Computer's turn to play
	 */
	public void triggerAI() {
		TicTacToeAI.smartPlay(this, grid, SIZE, EMPTY_CHAR, AI_CHAR, PLAYER_CHAR, freeCells);
		freeCells--;
	}

	/**
	 * Draws game using the main data structure and the provided pictures of x and o
	 * 
	 * @param g, tool to draw
	 */
	public void draw(Graphics g) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int x = i * gridSize;
				int y = j * gridSize;

				if (grid[i][j] == PLAYER_CHAR)
					g.drawImage(xpic, x, y, gridSize, gridSize, null);
				else if (grid[i][j] == AI_CHAR)
					g.drawImage(opic, x, y, gridSize, gridSize, null);

				g.setColor(Color.BLACK);
				g.drawRect(x, y, gridSize, gridSize);
			}
		}
	}

}
