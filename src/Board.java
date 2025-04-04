// COMP 2080 - Gomoku Game Project
// Matthew Macalalad, 101510305
// Bramjot Singh, 101511990

public class Board {

    // Final integers that will not be changed in the game (but exist as variables in case we want to try other options)
    public static final int BOARD_SIZE = 9; // Board size (9 x 9)
    public static final char EMPTY_SPACE = '.'; // Empty space character
    public static final int NUM_TO_WIN = 5; // How many in a row to win (5)

    // Declare variable that will hold a representation of the board as a 2D array
    private char[][] gameBoard;

    // Initializes the board size, then calls initialize function to fill the empty array
    public Board() {
        gameBoard = new char[BOARD_SIZE][BOARD_SIZE];
        initialize();
    }

    // Fills every space in the array with the character representing an empty space ('.')
    public void initialize() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameBoard[i][j] = EMPTY_SPACE;
            }
        }
    }

    // Displays the state of the board when called (with proper spacing, and row / column numbers)
    public void display() {
        System.out.print("  ");
        // Note "+ 1" so that row and column are shown as 1 to 9, but in the code it's the indices (0 - 8)
        for (int j = 0; j < BOARD_SIZE; j++) {
            System.out.print((j + 1) + " "); // Print column numbers
        }
        System.out.println();
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " "); // Print row number
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(gameBoard[i][j] + " "); // Print actual cell value
            }
            System.out.println();
        }
    }

    // Logic to check if a given move is valid
    public boolean isValidMove(int row, int col) {
        // Checks that the input row and column are within the bounds
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        // Returns true if space is empty, false if already occupied (or out of bounds in previous check)
        return gameBoard[row][col] == EMPTY_SPACE;
    }

    // Checks the entire board sequentially to see if it is full
    public boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // As soon as a single empty space is found, it returns false to signify that there are still remaining spaces
                if (gameBoard[i][j] == EMPTY_SPACE) {
                    return false;
                }
            }
        }
        return true; // Only returns if all cells are filled
    }

    // Checks if the most recent move resulted in a game win
    public boolean checkWin(char symbol) {
        // Loop through every space on the board, utilizing the checkDirection function
        // If checkDirection returns true and the other condition(s) for that call match, a win has been found
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Applies bounds to prevent unnecessary cell checks (makes sure that there is room for 5 in a row)
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
        return false; // No condition met, so no win found
    }

    // Function to check for a win provided a certain direction vector (represented by rowDelta and colDelta)
    public boolean checkDirection(int row, int column, char symbol, int rowDelta, int colDelta) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            // Loops up to 5 times, checking provided direction for symbol.
            // Row and column increment from the starting point based on the provided direction
            // Ex. for down right (rowDelta = 1, and colDelta = 1), every loop increases both row and column by one
            // For down left (rowDelta = 1, colDelta = -1), row will increase while column will decrease
            int currRow = row + (rowDelta * i);
            int currCol = column + (colDelta * i);
            // Checks to make sure the row and column you are checking are within the bounds
            // No currRow check because no provided direction vector decreases row value
            if (currCol < 0 ||
                currRow >= BOARD_SIZE ||
                currCol >= BOARD_SIZE ||
                    // If row or column are out of bonds, or the space at the current coordinates does not match the symbol
                    // you are checking for, then we can exit (we know there cannot be 5 in a row in that direction)
                gameBoard[currRow][currCol] != symbol) { return false; }
            // Otherwise, increment count and continue the loop
            count++;
        }
        // Returns true if count is equal to 5, otherwise false
        return count == 5;
    }

    // Simple function to get the actual board array rather than the full Board object
    public char[][] getBoard() {
        return gameBoard;
    }

    // Function to change the value held in a certain cell
    public void setCell(char symbol, int row, int col) {
        gameBoard[row][col] = symbol;
    }

}
