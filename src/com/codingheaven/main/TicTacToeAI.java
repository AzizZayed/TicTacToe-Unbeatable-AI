package com.codingheaven.main;

public class TicTacToeAI {

	/**
	 * Just play randomly
	 * 
	 * @param grid        - the n*n tic-tac-toe grid
	 * @param sizeOfBoard - the size of the grid (n, usually 3 but can be increased)
	 * @param emptyChar   - the symbol of an empty cell in the grid (' ')
	 * @param AIChar      - the symbol of an AI cell in the grid ('o')
	 */
	public static void randomPlay(char[][] grid, int size, char empty, char AI) {
		int celli = 0, cellj = 0;
		boolean cellFound = false;

		while (!cellFound) {

			celli = (int) (Math.random() * size);
			cellj = (int) (Math.random() * size);

			cellFound = (grid[celli][cellj] == empty);
		}

		grid[celli][cellj] = AI;
	}

	/**
	 * 
	 * @param board     - TICTACTOE Board
	 * @param grid      - the n*n tic-tac-toe grid
	 * @param size      - the size of the grid (n, usually 3 but can be increased)
	 * @param empty     - the symbol of an empty cell in the grid (' ')
	 * @param AI        - the symbol of an AI cell in the grid ('o')
	 * @param player    - the symbol of a player in the grid ('x')
	 * @param movesLeft - the number of moves left
	 * @param pruning   - enable or disable alpha beta pruning
	 * @param maxDepth  - maximum depth possible
	 */
	public static void smartPlay2(TicTacToeBoard board, char[][] grid, int size, char empty, char AI, char player,
			int movesLeft, boolean pruning, int maxDepth) {

		int best = -Integer.MAX_VALUE;

		int x = -1;
		int y = -1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = AI;

					int value;
					if (pruning) {
						value = minimaxPruning(board, grid, size, empty, AI, player, movesLeft - 1, 0, maxDepth,
								-Integer.MAX_VALUE, Integer.MAX_VALUE, false);
					} else {
						value = minimaxNormal(board, grid, size, empty, AI, player, movesLeft - 1, false);
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
	 * the AI plays using a minimax algorithm, without alpha-beta pruning
	 * 
	 * @param board     - TICTACTOE Board
	 * @param grid      - the n*n tic-tac-toe grid
	 * @param size      - the size of the grid (n, usually 3 but can be increased)
	 * @param empty     - the symbol of an empty cell in the grid (' ')
	 * @param AI        - the symbol of an AI cell in the grid ('o')
	 * @param player    - the symbol of a player in the grid ('x')
	 * @param movesLeft - the number of moves left
	 */
	public static void smartPlay(TicTacToeBoard board, char[][] grid, int size, char empty, char AI, char player,
			int movesLeft) {

		int best = -Integer.MAX_VALUE;

		int x = -1;
		int y = -1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = AI;
					movesLeft--;

					int value = minimaxNormal(board, grid, size, empty, AI, player, movesLeft, false);

					movesLeft++;
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
	private static int minimaxNormal(TicTacToeBoard board, char[][] grid, int size, char empty, char AI, char player,
			int movesLeft, boolean maximizing) {

		if (board.winCheck()) {
			if (maximizing) {
				return -10;
			} else {
				return 10;
			}
		} else if (movesLeft == 0) {
			return 0;
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
					movesLeft--;

					best = mult * Math.min(mult * best,
							mult * minimaxNormal(board, grid, size, empty, AI, player, movesLeft, !maximizing));

					movesLeft++;
					grid[i][j] = empty;
				}
			}
		}

		return best;

	}

	/**
	 * the AI plays using a minimax algorithm, without alpha-beta pruning
	 * 
	 * @param board     - TICTACTOE Board
	 * @param grid      - the n*n tic-tac-toe grid
	 * @param size      - the size of the grid (n, usually 3 but can be increased)
	 * @param empty     - the symbol of an empty cell in the grid (' ')
	 * @param AI        - the symbol of an AI cell in the grid ('o')
	 * @param player    - the symbol of a player in the grid ('x')
	 * @param movesLeft - the number of moves left
	 */
	public static void superSmartPlay(TicTacToeBoard board, char[][] grid, int size, char empty, char AI, char player,
			int movesLeft) {

		int best = -Integer.MAX_VALUE;

		int x = -1;
		int y = -1;

		int depth = 1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = AI;

					int value = minimaxPruning(board, grid, size, empty, AI, player, movesLeft - 1, depth, 10,
							-Integer.MAX_VALUE, Integer.MAX_VALUE, false);

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
	private static int minimaxPruning(TicTacToeBoard board, char[][] grid, int size, char empty, char AI, char player,
			int movesLeft, int depth, int maxDepth, int alpha, int beta, boolean maximizing) {

		if (board.winCheck()) {
			if (maximizing) {
				return -10;
			} else {
				return 10;
			}
		} else if (movesLeft == 0) {
			return 0;
		} else if (depth >= maxDepth) {
//			return static eval
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
		outer: for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					grid[i][j] = current;

					int value = minimaxPruning(board, grid, size, empty, AI, player, movesLeft - 1, depth + 1, maxDepth,
							alpha, beta, !maximizing);
					best = mult * Math.min(mult * best, mult * value);

					if (maximizing) {
						alpha = Math.max(alpha, best);
					} else {
						beta = Math.min(beta, best);
					}

					grid[i][j] = empty;

					// Alpha Beta Pruning
					if (beta <= alpha)
						break outer;
				}
			}
		}

		return best;

	}

}
