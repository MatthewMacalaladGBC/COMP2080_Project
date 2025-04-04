public class Board {

    public static final int BOARD_SIZE = 9;
    public static final char EMPTY_SPACE = '*';
    public static final int NUM_TO_WIN = 5;

    private char[][] gameBoard;

    public Board() {
        gameBoard = new char[BOARD_SIZE][BOARD_SIZE];
        initialize();
    }

    public void initialize() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameBoard[i][j] = EMPTY_SPACE;
            }
        }
    }

    public void display() {
        System.out.print("  ");
        for (int j = 0; j < BOARD_SIZE; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isValidMove(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        return gameBoard[row][col] == EMPTY_SPACE;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (gameBoard[i][j] == EMPTY_SPACE) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin(char symbol) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Applies bounds to prevent unnecessary cell checks
                // Vertical (downwards) check [if row > 4, cannot get five in a row vertically (max row = 8)]
                if (i <= BOARD_SIZE - 5 && checkDirection(i, j, symbol, 1, 0)) return true;
                // Horizontal (right) check [if col > 4, cannot get five in a row horizontally (max col = 8)]
                if (j <= BOARD_SIZE - 5 && checkDirection(i, j, symbol, 0, 1)) return true;
                // Diagonal (down-right) check [both the above checks apply, going down AND right]
                if (i <= BOARD_SIZE - 5 && j <= BOARD_SIZE - 5 && checkDirection(i, j, symbol, 1, 1)) return true;
                // Diagonal (down-right) check [same vertical bound, but going left instead of right so col must be >= 4 (min col = 0)]
                if (i <= BOARD_SIZE - 5 && j >= 4 && checkDirection(i, j, symbol, 1, -1)) return true;
            }
        }
        return false;
    }

    public boolean checkDirection(int row, int column, char symbol, int rowDelta, int colDelta) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            // Loops up to 5 times, checking provided direction for symbol.
            // Row and column increment from the starting point based on the provided direction
            // Ex. for down right (rowDelta = 1, and colDelta = 1), every loop increases both row and column by one
            int currRow = row + (rowDelta * i);
            int currCol = column + (colDelta * i);
            if (currCol < 0 ||
                currRow >= BOARD_SIZE ||
                currCol >= BOARD_SIZE ||
                gameBoard[currRow][currCol] != symbol) { return false; }
            count++;
        }
        return count == 5;
    }

    public char[][] getBoard() {
        return gameBoard;
    }

    public void setCell(char symbol, int row, int col) {
        gameBoard[row][col] = symbol;
    }

}
