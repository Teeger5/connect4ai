import java.util.Scanner;

public class Connect4 {
	private static final int ROWS = 6;
	private static final int COLS = 7;
	private static final char EMPTY = '.';

	private char[][] board = new char[ROWS][COLS];

	public Connect4() {
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

	public static void start () {
		Connect4 game = new Connect4();
		Scanner scanner = new Scanner(System.in);
		char currentPlayer = 'X';
		boolean gameWon = false;

		while (!gameWon) {
			game.printBoard();
			System.out.println("Játékos " + currentPlayer + ", válassz egy oszlopot (0-6):");
			int col = scanner.nextInt();

			if (col < 0 || col >= COLS || !game.makeMove(col, currentPlayer)) {
				System.out.println("Érvénytelen lépés, próbáld újra!");
				continue;
			}

			if (game.checkWin(currentPlayer)) {
				game.printBoard();
				System.out.println("Játékos " + currentPlayer + " nyert!");
				gameWon = true;
			} else {
				currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
			}
		}

		scanner.close();
	}
}
