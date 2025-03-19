import java.util.Scanner;

public class Connect4AI2 {
	private static final int ROWS = 6;
	private static final int COLS = 7;
	private static final char EMPTY = '.';
	private static final int MAX_DEPTH = 5;

	private char[][] board = new char[ROWS][COLS];

	public Connect4AI2() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				board[i][j] = EMPTY;
			}
		}
	}

	public void printBoard() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	public boolean makeMove(int col, char player) {
		for (int i = ROWS - 1; i >= 0; i--) {
			if (board[i][col] == EMPTY) {
				board[i][col] = player;
				return true;
			}
		}
		return false;
	}

	public boolean undoMove(int col) {
		for (int i = 0; i < ROWS; i++) {
			if (board[i][col] != EMPTY) {
				board[i][col] = EMPTY;
				return true;
			}
		}
		return false;
	}

	public boolean checkWin(char player) {
		// Vízszintes ellenőrzés
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS - 3; j++) {
				if (board[i][j] == player && board[i][j + 1] == player &&
						board[i][j + 2] == player && board[i][j + 3] == player) {
					return true;
				}
			}
		}

		// Függőleges ellenőrzés
		for (int i = 0; i < ROWS - 3; i++) {
			for (int j = 0; j < COLS; j++) {
				if (board[i][j] == player && board[i + 1][j] == player &&
						board[i + 2][j] == player && board[i + 3][j] == player) {
					return true;
				}
			}
		}

		// Átlós ellenőrzés (balról jobbra)
		for (int i = 0; i < ROWS - 3; i++) {
			for (int j = 0; j < COLS - 3; j++) {
				if (board[i][j] == player && board[i + 1][j + 1] == player &&
						board[i + 2][j + 2] == player && board[i + 3][j + 3] == player) {
					return true;
				}
			}
		}

		// Átlós ellenőrzés (jobbról balra)
		for (int i = 0; i < ROWS - 3; i++) {
			for (int j = 3; j < COLS; j++) {
				if (board[i][j] == player && board[i + 1][j - 1] == player &&
						board[i + 2][j - 2] == player && board[i + 3][j - 3] == player) {
					return true;
				}
			}
		}

		return false;
	}

	public int minimaxWithAlphaBeta(int depth, int alpha, int beta, boolean isMaximizing, char aiPlayer, char humanPlayer) {
		if (depth == 0 || isGameOver()) {
			return evaluateBoard(aiPlayer, humanPlayer);
		}

		if (isMaximizing) {
			int maxEval = Integer.MIN_VALUE;
			for (int col = 0; col < COLS; col++) {
				if (board[0][col] == EMPTY) {
					makeMove(col, aiPlayer);
					int eval = minimaxWithAlphaBeta(depth - 1, alpha, beta, false, aiPlayer, humanPlayer);
					undoMove(col);
					maxEval = Math.max(maxEval, eval);
					alpha = Math.max(alpha, eval);
					if (beta <= alpha) {
						break; // Alfa-béta vágás
					}
				}
			}
			return maxEval;
		} else {
			int minEval = Integer.MAX_VALUE;
			for (int col = 0; col < COLS; col++) {
				if (board[0][col] == EMPTY) {
					makeMove(col, humanPlayer);
					int eval = minimaxWithAlphaBeta(depth - 1, alpha, beta, true, aiPlayer, humanPlayer);
					undoMove(col);
					minEval = Math.min(minEval, eval);
					beta = Math.min(beta, eval);
					if (beta <= alpha) {
						break; // Alfa-béta vágás
					}
				}
			}
			return minEval;
		}
	}

	public int bestMoveWithAlphaBeta(char aiPlayer, char humanPlayer) {
		int bestScore = Integer.MIN_VALUE;
		int bestCol = -1;

		for (int col = 0; col < COLS; col++) {
			if (board[0][col] == EMPTY) {
				makeMove(col, aiPlayer);

				// Ellenőrizzük, hogy nyer-e az AI
				if (checkWin(aiPlayer)) {
					undoMove(col);
					return col; // Azonnali győzelem lépése
				}

				// Ellenőrizzük, hogy a játékos nyerhet-e
				makeMove(col, humanPlayer);
				if (checkWin(humanPlayer)) {
					undoMove(col);
					undoMove(col);
					return col; // Blokkoló lépés
				}
				undoMove(col);

				// Ha nincs nyerő vagy blokkoló lépés, folytatjuk a minimaxot
				int score = minimaxWithAlphaBeta(MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiPlayer, humanPlayer);
				undoMove(col);

				if (score > bestScore) {
					bestScore = score;
					bestCol = col;
				}
			}
		}

		return bestCol;
	}

	public int evaluateBoard(char aiPlayer, char humanPlayer) {
		int score = 0;

		// Vízszintes sorok értékelése
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS - 3; col++) {
				int aiCount = 0, humanCount = 0;

				for (int k = 0; k < 4; k++) { // Négy mező egymás mellett
					if (board[row][col + k] == aiPlayer) aiCount++;
					if (board[row][col + k] == humanPlayer) humanCount++;
				}

				if (humanCount == 0) score += Math.pow(10, aiCount);
				if (aiCount == 0) score -= Math.pow(10, humanCount);
			}
		}

		// Függőleges és átlós értékelést hasonló módon alkalmazhatunk.

		return score;
	}

	public boolean isGameOver() {
		for (int i = 0; i < COLS; i++) {
			if (board[0][i] == EMPTY) {
				return false;
			}
		}
		return true;
	}

	public static void start () {
		Connect4AI2 game = new Connect4AI2();
		Scanner scanner = new Scanner(System.in);
		char humanPlayer = 'X';
		char aiPlayer = 'O';

		System.out.println("Üdvözöllek a továbbfejlesztett Connect4 játékban!");

		while (!game.isGameOver()) {
			game.printBoard();

			System.out.println("Válassz egy oszlopot (0-6):");
			int humanMove = scanner.nextInt();
			if (!game.makeMove(humanMove, humanPlayer)) {
				System.out.println("Ez az oszlop tele van. Próbáld újra!");
				continue;
			}

			if (game.checkWin(humanPlayer)) {
				game.printBoard();
				System.out.println("Gratulálok, nyertél!");
				return;
			}

			int aiMove = game.bestMoveWithAlphaBeta(aiPlayer, humanPlayer);
			System.out.println("AI lépése: " + aiMove);
			game.makeMove(aiMove, aiPlayer);

			if (game.checkWin(aiPlayer)) {
				game.printBoard();
				System.out.println("Az AI nyert!");
				return;
			}
		}
		game.printBoard();
		System.out.println("Döntetlen! A játék véget ért.");
		scanner.close();
	}
}
