package com.codingheaven.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeAI extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final short SIZE = 6; // size of the game, n x n tic-tac-toe!
	private final static int IMAGE_SIZE = 150; // size of an image, images are squares

	private static final int WIDTH = IMAGE_SIZE * SIZE; // width of panel
	private static final int HEIGHT = WIDTH; // height of panel, panel is a square

	private char grid[][]; // main data structure
	private final static char PLAYER_CHAR = 'x'; // player is always x
	private final static char AI_CHAR = 'o'; // computer is always o
	private final static int EMPTY_CHAR = ' '; // placeholder for an empty cell

	private short freeCells; // number of cells that are empty
	private BufferedImage xpic, opic; // images of x and o

	/**
	 * Constructor
	 */
	public TicTacToeAI() {

		loadImages();
		panelSetup();
		initGrid();

	}

	/**
	 * Load all the images out of memory, x and o images done in Microsoft paint
	 */
	private void loadImages() {
		try {
			xpic = ImageIO.read(new File("res/xpic.png"));
			opic = ImageIO.read(new File("res/opic.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setup the panel size, settings, and events
	 */
	private void panelSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		this.setFocusable(true);

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				short i = (short) (e.getPoint().getX() / IMAGE_SIZE);
				short j = (short) (e.getPoint().getY() / IMAGE_SIZE);

				cellClicked(i, j);

				repaint();
			}

		});
	}

	/**
	 * initialize the game grid, the data structure
	 */
	private void initGrid() {
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
	private void cellClicked(short i, short j) {
		char cell = grid[i][j];

		if (cell != EMPTY_CHAR)
			return;

		grid[i][j] = PLAYER_CHAR;
		freeCells--;

		repaint();

		if (winCheck())
			return;

		if (tieCheck())
			return;

		triggerAI();

		repaint();

		winCheck();

		tieCheck();

	}

	/**
	 * Check if the game is a tie
	 * 
	 * @return true if the game is a tie, false if otherwise
	 */
	private boolean tieCheck() {
		if (freeCells == 0) {
			repaint();
			int y = JOptionPane.showConfirmDialog(this, "It's a Tie. Restart? ", " Tic Tac Toe ",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (y == JOptionPane.YES_OPTION)
				initGrid();
			return true;
		}

		return false;

	}

	/**
	 * Checks if one of the 2 opponents won the game, displays message box if yes
	 * 
	 * @return true if a winner exits, false otherwise
	 */
	private boolean winCheck() {
		char winnerChar = EMPTY_CHAR;

		int j, i;

		String winTextPlayer = Character.toString(PLAYER_CHAR);
		String winTextAI = Character.toString(AI_CHAR);
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
		if (winnerChar != EMPTY_CHAR) {
			JOptionPane.showMessageDialog(this, Character.toUpperCase(winnerChar) + " Won! Restarting.",
					" Tic Tac Toe ", JOptionPane.INFORMATION_MESSAGE);

			initGrid();
			return true;
		}

		return false;

	}

	/**
	 * Computer's turn to play
	 */
	private void triggerAI() {

		int celli = 0, cellj = 0;
		boolean cellFound = false;

		while (!cellFound) {

			celli = (int) (Math.random() * SIZE);
			cellj = (int) (Math.random() * SIZE);

			cellFound = (grid[celli][cellj] == EMPTY_CHAR);
		}

		grid[celli][cellj] = AI_CHAR;

		freeCells--;

	}

	@Override
	public void paintComponent(Graphics g) {
		drawBackground(g);
		drawGame(g);
	}

	/**
	 * Draw white background
	 * 
	 * @param g, tool to draw
	 */
	private void drawBackground(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * Draws game using the main data structure and the provided pictures of x and o
	 * 
	 * @param g, tool to draw
	 */
	private void drawGame(Graphics g) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int x = i * IMAGE_SIZE;
				int y = j * IMAGE_SIZE;

				if (grid[i][j] == PLAYER_CHAR)
					g.drawImage(xpic, x, y, IMAGE_SIZE, IMAGE_SIZE, null);
				else if (grid[i][j] == AI_CHAR)
					g.drawImage(opic, x, y, IMAGE_SIZE, IMAGE_SIZE, null);

				g.setColor(Color.BLACK);
				g.drawRect(x, y, IMAGE_SIZE, IMAGE_SIZE);
			}
		}
	}

	public static void main(String args[]) {

		/*
		 * frame to add the game into it, since the game is a subclass of JPanel and can
		 * support graphics
		 */
		JFrame frame = new JFrame("Tic Tac Toe");

		TicTacToeAI game = new TicTacToeAI();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.setDoubleBuffered(true);

		game.repaint();
	}
}
