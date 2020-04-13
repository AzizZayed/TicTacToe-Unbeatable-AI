package com.codingheaven.main;

/**
 * the AI tic tac toe solver
 * 
 * @author Zayed
 *
 */
public class TicTacToeAI {

	/**
	 * Just play randomly
	 * 
	 * @param grid  - the n*n tic-tac-toe grid
	 * @param size  - the size of the grid (n, usually 3 but can be increased)
	 * @param empty - the symbol of an empty cell in the grid (' ')
	 * @param AI    - the symbol of an AI cell in the grid ('o')
	 */
	public static void randomPlay(char[][] grid, int size, char empty, char AI) {
		int celli = -1, cellj = -1;
		boolean cellFound = false;

		while (!cellFound) {

			celli = (int) (Math.random() * size);
			cellj = (int) (Math.random() * size);

			cellFound = (grid[celli][cellj] == empty);
		}

		grid[celli][cellj] = AI;
	}

	/**
	 * Super smart unbeatable AI, includes MiniMax algorithm and alpha beta pruning
	 * 
	 * @param board     - TICTACTOE Board
	 * @param grid      - the n*n tic-tac-toe grid
	 * @param size      - the size of the grid (n, usually 3 but can be increased)
	 * @param empty     - the symbol of an empty cell in the grid (' ')
	 * @param AI        - the symbol of an AI cell in the grid ('o')
	 * @param player    - the symbol of a player in the grid ('x')
	 * @param movesLeft - the number of moves left
	 * @param pruning   - enable or disable alpha beta pruning
	 * @param maxDepth  - maximum desired tree depth
	 */
	public static void smartPlay(char[][] grid, int size, char empty, char AI, char player, int movesLeft,
			boolean pruning, int maxDepth) {

		int best = -Integer.MAX_VALUE;

		int x = -1;
		int y = -1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = AI;

					int value;
					if (pruning) {
						value = minimaxPruning(grid, size, empty, AI, player, movesLeft - 1, 0, maxDepth,
								-Integer.MAX_VALUE, Integer.MAX_VALUE, false);
					} else {
						value = minimaxNormal(grid, size, empty, AI, player, movesLeft - 1, 0, maxDepth, false);
					}

					grid[i][j] = empty;

					if (value > best) {
						x = i;
						y = j;
						best = value;
					}
				}
			}
		}

		grid[x][y] = AI;
	}

	/**
	 * the minimax algorithm without alpha beta pruning
	 * 
	 * @param board      - TICTACTOE Board
	 * @param grid       - the n*n tic-tac-toe grid (char array)
	 * @param size       - the size of the grid (n, usually 3 but can be increased)
	 * @param empty      - the symbol of an empty cell in the grid (' ')
	 * @param AI         - the symbol of an AI cell in the grid ('o')
	 * @param player     - the symbol of a player in the grid ('x')
	 * @param movesLeft  - the number of moves left
	 * @param maximizing - if the algorithm is maximizing or not
	 * @return the score of the scenario
	 */
	private static int minimaxNormal(char[][] grid, int size, char empty, char AI, char player, int movesLeft,
			int depth, int maxDepth, boolean maximizing) {

		if (simpleWinCheck(grid, size, empty, AI, player)) {
			if (maximizing) {
				return -10;
			} else {
				return 10;
			}
		} else if (movesLeft == 0) {
			return 0;
		} else if (depth >= maxDepth) {
			return finalNodeEval(maximizing);
		}

		int mult;
		char current;

		if (maximizing) {
			mult = -1;
			current = AI;
		} else {
			mult = 1;
			current = player;
		}

		int best = mult * Integer.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = current;

					int value = minimaxNormal(grid, size, empty, AI, player, movesLeft - 1, depth + 1, maxDepth,
							!maximizing);
					best = mult * Math.min(mult * best, mult * value);

					grid[i][j] = empty;
				}
			}
		}

		return best;

	}

	/**
	 * the minimax algorithm with alpha beta pruning
	 * 
	 * @param board      - TICTACTOE Board
	 * @param grid       - the n*n tic-tac-toe grid (char array)
	 * @param size       - the size of the grid (n, usually 3 but can be increased)
	 * @param empty      - the symbol of an empty cell in the grid (' ')
	 * @param AI         - the symbol of an AI cell in the grid ('o')
	 * @param player     - the symbol of a player in the grid ('x')
	 * @param movesLeft  - the number of moves left
	 * @param maximizing - if the algorithm is maximizing or not
	 * @return the score of the scenario
	 */
	private static int minimaxPruning(char[][] grid, int size, char empty, char AI, char player, int movesLeft,
			int depth, int maxDepth, int alpha, int beta, boolean maximizing) {

		if (simpleWinCheck(grid, size, empty, AI, player)) {
			if (maximizing) {
				return -10;
			} else {
				return 10;
			}
		} else if (movesLeft == 0) {
			return 0;
		} else if (depth >= maxDepth) {
			return finalNodeEval(maximizing);
		}

		int mult;
		char current;

		if (maximizing) {
			mult = -1;
			current = AI;
		} else {
			mult = 1;
			current = player;
		}

		int best = mult * Integer.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = current;

					int value = minimaxPruning(grid, size, empty, AI, player, movesLeft - 1, depth + 1, maxDepth, alpha,
							beta, !maximizing);
					best = mult * Math.min(mult * best, mult * value);

					if (maximizing) {
						alpha = Math.max(alpha, best);
					} else {
						beta = Math.min(beta, best);
					}

					grid[i][j] = empty;

					// The Alpha-Beta pruning magic lies below this comment, only 2 lines of code
					if (beta <= alpha)
						i = j = size; // terminate nested loops
				}
			}
		}

		return best;

	}

	/**
	 * checks if any player won
	 * 
	 * @param grid   - the n*n tic-tac-toe grid (char array)
	 * @param size   - the size of the grid (n, usually 3 but can be increased)
	 * @param empty  - the symbol of an empty cell in the grid (' ')
	 * @param AI     - the symbol of an AI cell in the grid ('o')
	 * @param player - the symbol of a player in the grid ('x')
	 * @return true if a player won
	 */
	private static boolean simpleWinCheck(char[][] grid, int size, char empty, char AI, char player) {
		int j, i;

		String winTextPlayer = Character.toString(player); // = "xxx" after the for loop below
		String winTextAI = Character.toString(AI); // = "ooo" after the for loop below
		String row;

		for (i = 1; i < size; i++) {
			winTextPlayer += player;
			winTextAI += AI;
		}

		// Horizontal checks
		j = 0;
		while (j < size) {
			row = "";

			for (i = 0; i < size; i++)
				row += grid[i][j];

			if (row.equals(winTextPlayer) || row.equals(winTextAI))
				return true;

			j++;
		}

		// vertical checks
		i = 0;
		while (i < size) {
			row = "";

			for (j = 0; j < size; j++)
				row += grid[i][j];

			if (row.equals(winTextPlayer) || row.equals(winTextAI))
				return true;

			i++;
		}

		// diagonal checks
		row = "";
		for (i = 0, j = 0; i < size && j < size; i++, j++)
			row += grid[i][j];

		if (row.equals(winTextPlayer) || row.equals(winTextAI))
			return true;

		row = "";
		for (i = 0, j = size - 1; i < size && j >= 0; i++, j--)
			row += grid[i][j];

		if (row.equals(winTextPlayer) || row.equals(winTextAI))
			return true;

		return false;
	}

	/*
	 * static evaluation function when going too deep
	 */
	private static int finalNodeEval(boolean max) {
		if (max)
			return -1;

		return 1;
	}

}
