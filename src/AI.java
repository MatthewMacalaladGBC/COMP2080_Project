public class AI {
    // Sets a very high score to be returned if a win is found
    public static final int WIN_SCORE = 1000000;
    // Simple score table to represent the value of a certain move
    // Maps to number of consecutive number of symbols in a given segment (ex. 0 in a row = 0 scode, 1 = 1, 2, = 10, ...)
    public static final int[] SCORE_TABLE = {0, 1, 10, 100, 1000};

    // Minimax recursive function to help with finding the best possible move for the computer
    public static int minimax(Board boardObj, int depth, boolean isMaximizing,
                              char computerSymbol, char playerSymbol, int alpha, int beta) {
        // Gets the current state of the board at the time the function is called
        char[][] gameBoard = boardObj.getBoard();

        // Base conditions
        // Checks if computer has won, returning a very high possible score (to guarantee computer selects winning moves)
        if (boardObj.checkWin(computerSymbol)) {
            return WIN_SCORE;
        }
        // Checks if player has won, returning a very high negative score (to guide computer in avoiding this path)
        if (boardObj.checkWin(playerSymbol)) {
            return -WIN_SCORE;
        }
        // If search depth reaches zero (or if board is full after the move check), evaluates the score at that point
        if (depth == 0 || boardObj.isBoardFull()) {
            return evaluateBoard(gameBoard, computerSymbol, playerSymbol);
        }

        // Checks to see if we are trying to maximize score (computer's turn) or minimize (player's turn)
        if (isMaximizing) {
            // Initialize bestScore to a minimum integer, to guarantee that any move will be higher (so that at least one move is selected)
            int bestScore = Integer.MIN_VALUE;
            // Loop through all board positions
            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    // Checks if the space is empty
                    if (gameBoard[i][j] == Board.EMPTY_SPACE) {
                        // If empty, set the cell to the computer's symbol to simulate the move
                        gameBoard[i][j] = computerSymbol;
                        // Recursively evaluates the board with a reduced depth, and flipping isMaximizing,
                        // switching to player's turn (minimizing score)
                        int score = minimax(boardObj, (depth - 1), false, computerSymbol, playerSymbol, alpha, beta);
                        // Undoes the simulated move (so that the move does not persist)
                        gameBoard[i][j] = Board.EMPTY_SPACE;
                        // If the score pulled from the evaluation is higher than the current bestScore, update bestScore accordingly
                        bestScore = Math.max(bestScore, score);
                        // Alpha holds the maximum score from all already checked paths at a given point
                        // Updates alpha if current bestScore is better than the current alpha value
                        alpha = Math.max(alpha, bestScore);
                        // Alpha-beta pruning: If beta is less than or equal to the alpha value at this point, then the branch is pruned (no need for further checks, so exits)
                        // This is because, if beta is lower, than this alpha branch will never be taken due to the player turn simulation trying to minimize score
                        if (beta <= alpha) {
                            return bestScore;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            // All the following logic is essentially the same as above, but a focus on minimizing score instead
            // Simulates the minimizer's (human player's) turn
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    if (gameBoard[i][j] == Board.EMPTY_SPACE) {
                        gameBoard[i][j] = playerSymbol;
                        // Sets isMaximizing to true for recursive call (next turn would be maximizer's - Computer)
                        int score = minimax(boardObj, depth - 1, true, computerSymbol, playerSymbol, alpha, beta);
                        gameBoard[i][j] = Board.EMPTY_SPACE;
                        // Takes min score instead of max
                        bestScore = Math.min(bestScore, score);
                        // Updates beta instead of alpha, taking a minimum again to see if this path provides a better (smaller)
                        // score value than all previously checked paths
                        beta = Math.min(beta, bestScore);
                        // Alpha-beta pruning again: prunes branch if beta is less than or equal to alpha
                        // This is because when this is the case, the computer will presumably never choose the path that leads
                        // to this in the first place
                        if (beta <= alpha) {
                            return bestScore;
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    public static int evaluateBoard(char[][] board, char computerSymbol, char playerSymbol) {
        int score = 0;
        // Horizontal evaluation: checks every possible horizontal segment in the current board state
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            // Limit passed starting column position up to column index 4 (9 - 5) to remove unnecessary checks.
            // Column index 5 and onwards don't have space for 5 in a row horizontally
            for (int j = 0; j <= Board.BOARD_SIZE - Board.NUM_TO_WIN; j++) {
                // Updates score based on evaluations of each segment
                score += evaluateSegment(board, i, j, 0, 1, computerSymbol, playerSymbol);
            }
        }
        // Vertical evaluation: checks every possible vertical segment in the current board state
        // Limit passed row up to index 4
        for (int i = 0; i <= Board.BOARD_SIZE - Board.NUM_TO_WIN; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                score += evaluateSegment(board, i, j, 1, 0, computerSymbol, playerSymbol);
            }
        }
        // Diagonal evaluation: checks every possible diagonal segment in the current board state
        // Limit both row and column to index 4 and below
        for (int i = 0; i <= Board.BOARD_SIZE - Board.NUM_TO_WIN; i++) {
            for (int j = 0; j <= Board.BOARD_SIZE - Board.NUM_TO_WIN; j++) {
                score += evaluateSegment(board, i, j, 1, 1, computerSymbol, playerSymbol);
            }
        }
        // Anti-diagonal evaluation: checks every possible anti-diagonal segment in the current board state
        // Limit row to index 4 and below
        for (int i = 0; i <= Board.BOARD_SIZE - Board.NUM_TO_WIN; i++) {
            // Limit column to index 4 and above (check is moving to the left)
            for (int j = Board.NUM_TO_WIN - 1; j < Board.BOARD_SIZE; j++) {
                score += evaluateSegment(board, i, j, 1, -1, computerSymbol, playerSymbol);
            }
        }
        return score;
    }

    public static int evaluateSegment(char[][] board, int row, int col, int rowDelta, int colDelta, char computerSymbol, char playerSymbol) {
        // initialize variable to count number of each player's symbols in a certain segment
        int computerCount = 0, playerCount = 0;
        // Loops up to 5 times
        for (int i = 0; i < Board.NUM_TO_WIN; i++) {
            // Finds number of each symbol within a given segment of 5 (starting from the passed position, and based on the given direction vector)
            char cell = board[row + (i * rowDelta)][col + (i * colDelta)];
            if (cell == computerSymbol) {
                computerCount++;
            } else if (cell == playerSymbol) {
                playerCount++;
            }
        }
        // If both symbols exist in a segment, then we know that that segment cannot contain five in a row of either (blocked, no score)
        if (computerCount > 0 && playerCount > 0) {
            return 0;
        }

        // Initialize variable to determine whether a given segment is blocked on either end
        boolean openEnded = false;
        // Finds the cell right before the segment, and right after the segment
        int preRow = row - rowDelta;
        int preCol = col - colDelta;
        int postRow = row + Board.NUM_TO_WIN * rowDelta;
        int postCol = col + Board.NUM_TO_WIN * colDelta;

        // Determines if the segment is open before the start of the segment by checking bounds and if pre- and post- cells are empty
        boolean openPre = (preRow < 0 || preCol < 0 || preRow >= Board.BOARD_SIZE || preCol >= Board.BOARD_SIZE
                || board[preRow][preCol] == Board.EMPTY_SPACE);
        // Same as above, but determines if the segment is open at the end of the segment instead of start
        boolean openPost = (postRow < 0 || postCol < 0 || postRow >= Board.BOARD_SIZE || postCol >= Board.BOARD_SIZE
                || board[postRow][postCol] == Board.EMPTY_SPACE);
        openEnded = openPre && openPost; // Set openEnded to true if both ends are open (thus making it a more valuable segment)

        // If the segment contains no human player symbols, returns a positive score value
        if (computerCount > 0) {
            int evalScore = SCORE_TABLE[computerCount]; // Score is pulled from the score table; higher value for more symbols in the segment
            if (openEnded) {
                evalScore *= 2; // Applies a value multiplier for an open-ended segment (more possibilities)
            }
            return evalScore; // Returns the positive score
        }
        // If segment contains no computer symbols, returns a negative score
        if (playerCount > 0) {
            int evalScore = SCORE_TABLE[playerCount]; // Once again, pulls absolute value from table
            if (openEnded) {
                evalScore *= 3; // Provides a higher multiplier to avoid symmetry
                // Ex. computer will prioritize blocking an open-ended streak of three player symbols over setting up their own streak of 3
            }
            return -evalScore; // Returns the negative score
        }
        return 0; // If no symbols found, returns 0
    }

    // Function that is called directly in the game logic, to recursively call minimax
    public static int[] findBestMove(Board boardObj, int depth, char computerSymbol, char playerSymbol) {
        char[][] gameBoard = boardObj.getBoard(); // Gets board state again
        int bestScore = Integer.MIN_VALUE; // Sets best score to a minimum, to guarantee a move will be chosen
        int[] bestMove = { -1, -1 }; // Initializes variable to hold the best move (initially out of bounds)

        // Loop through the game board
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (gameBoard[i][j] == Board.EMPTY_SPACE) { // Checks if space is empty
                    gameBoard[i][j] = computerSymbol; // If empty, simulates a move for the computer
                    // Then, passes the board object (with the newly simulated move) into the minimax function
                    // Starts with maximizing false (minimizer / human player's simulated turn is next)
                    // alpha and beta are set to integer min and max respectively, for alpha-beta pruning
                    // (ensures full range of scores is considered initially, by setting worst case for both maximizer and minimizer)
                    int moveScore = minimax(boardObj, depth - 1, false, computerSymbol, playerSymbol,
                            Integer.MIN_VALUE, Integer.MAX_VALUE);
                    // After recursive function ends, reset the board state
                    gameBoard[i][j] = Board.EMPTY_SPACE;
                    if (moveScore > bestScore) {
                        // If the found score for the simulated move is greater than the current best score,
                        // the old score is replaced, and the coordinates of the initial move are stored
                        bestScore = moveScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        // Returns the found best move after all simulations are run
        return bestMove;
    }

}
