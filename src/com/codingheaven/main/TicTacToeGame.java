package com.codingheaven.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TicTacToeGame extends JPanel {

	private static final long serialVersionUID = 1L;

	TicTacToeBoard board;

	/**
	 * Constructor
	 */
	public TicTacToeGame() {
		board = new TicTacToeBoard(this);
		panelSetup();
	}

	/**
	 * Setup the panel size, settings, and events
	 */
	private void panelSetup() {
		int gameSize = board.getGridSize() * board.getSize();

		this.setPreferredSize(new Dimension(gameSize, gameSize));
		this.setMaximumSize(new Dimension(gameSize, gameSize));
		this.setMinimumSize(new Dimension(gameSize, gameSize));

		this.setFocusable(true);

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				board.cellClicked(e.getX(), e.getY());

				repaint();
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {
		drawBackground(g);
		board.draw(g);
	}

	/**
	 * Draw white background
	 * 
	 * @param g, tool to draw
	 */
	private void drawBackground(Graphics g) {
		int gameSize = board.getGridSize() * board.getSize();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gameSize, gameSize);
	}

	public static void main(String args[]) {

		/*
		 * frame to add the game into it, since TicTacToeAIGame is a subclass of JPanel,
		 * it supports graphics
		 */
		JFrame frame = new JFrame("Tic Tac Toe");

		TicTacToeGame game = new TicTacToeGame();

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
