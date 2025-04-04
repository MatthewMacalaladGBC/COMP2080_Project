public class AI {
    public static final int WIN_SCORE = 1000000;
    public static final int[] SCORE_TABLE = {0, 1, 10, 100, 1000};

    public static int minimax(Board boardObj, int depth, boolean isMaximizing, char computerSymbol, char playerSymbol) {
        char[][] gameBoard = boardObj.getBoard();

        if (boardObj.checkWin(computerSymbol)) {
            return WIN_SCORE;
        }

        if (boardObj.checkWin(playerSymbol)) {
            return -WIN_SCORE;
        }

        if (depth == 0 || boardObj.isBoardFull()) {
            return evaluateBoard(gameBoard, computerSymbol, playerSymbol);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    if (gameBoard[i][j] == Board.EMPTY_SPACE) {
                        gameBoard[i][j] = computerSymbol;
                        int score = minimax(boardObj, (depth - 1), false, computerSymbol, playerSymbol);
                        gameBoard[i][j] = Board.EMPTY_SPACE;
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    if (gameBoard[i][j] == Board.EMPTY_SPACE) {
                        gameBoard[i][j] = playerSymbol;
                        int score = minimax(boardObj, depth - 1, true, computerSymbol, playerSymbol);
                        gameBoard[i][j] = Board.EMPTY_SPACE;
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
            return bestScore;
        }
    }

    public static int evaluateBoard(char[][] board, char computerSymbol, char playerSymbol) {
        int score = 0;
        // Horizontal evaluation
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j <= Board.BOARD_SIZE - Board.NUM_TO_WIN; j++) {
                score += evaluateSegment(board, i, j, 0, 1, computerSymbol, playerSymbol);
            }
        }
        // Vertical evaluation
        for (int i = 0; i <= Board.BOARD_SIZE - Board.NUM_TO_WIN; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                score += evaluateSegment(board, i, j, 1, 0, computerSymbol, playerSymbol);
            }
        }
        // Diagonal evaluation
        for (int i = 0; i <= Board.BOARD_SIZE - Board.NUM_TO_WIN; i++) {
            for (int j = 0; j <= Board.BOARD_SIZE - Board.NUM_TO_WIN; j++) {
                score += evaluateSegment(board, i, j, 1, 1, computerSymbol, playerSymbol);
            }
        }
        // Anti-diagonal evaluation
        for (int i = 0; i <= Board.BOARD_SIZE - Board.NUM_TO_WIN; i++) {
            for (int j = Board.NUM_TO_WIN - 1; j < Board.BOARD_SIZE; j++) {
                score += evaluateSegment(board, i, j, 1, -1, computerSymbol, playerSymbol);
            }
        }
        return score;
    }

    public static int evaluateSegment(char[][] board, int row, int col, int rowDelta, int colDelta, char computerSymbol, char playerSymbol) {
        int computerCount = 0, playerCount = 0;
        for (int i = 0; i < Board.NUM_TO_WIN; i++) {
            char cell = board[row + (i * rowDelta)][col + (i * colDelta)];
            if (cell == computerSymbol) {
                computerCount++;
            } else if (cell == playerSymbol) {
                playerCount++;
            }
        }
        if (computerCount > 0 && playerCount > 0) {
            return 0;
        }

        boolean openEnded = false;
        int preRow = row - rowDelta;
        int preCol = col - colDelta;
        int postRow = row + Board.NUM_TO_WIN * rowDelta;
        int postCol = col + Board.NUM_TO_WIN * colDelta;

        boolean openPre = (preRow < 0 || preCol < 0 || preRow >= Board.BOARD_SIZE || preCol >= Board.BOARD_SIZE
                || board[preRow][preCol] == Board.EMPTY_SPACE);
        boolean openPost = (postRow < 0 || postCol < 0 || postRow >= Board.BOARD_SIZE || postCol >= Board.BOARD_SIZE
                || board[postRow][postCol] == Board.EMPTY_SPACE);
        openEnded = openPre && openPost;

        if (computerCount > 0) {
            int evalScore = SCORE_TABLE[computerCount];
            if (openEnded) {
                evalScore *= 2;
            }
            return evalScore;
        }
        if (playerCount > 0) {
            int evalScore = SCORE_TABLE[playerCount];
            if (openEnded) {
                evalScore *= 2;
            }
            return -evalScore;
        }
        return 0;
    }

    public static int[] findBestMove(Board boardObj, int depth, char computerSymbol, char playerSymbol) {
        char[][] gameBoard = boardObj.getBoard( );;
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = { -1, -1 };

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (gameBoard[i][j] == Board.EMPTY_SPACE) {
                    gameBoard[i][j] = computerSymbol;
                    int moveScore = minimax(boardObj, depth - 1, false, computerSymbol, playerSymbol);
                    gameBoard[i][j] = Board.EMPTY_SPACE;
                    if (moveScore > bestScore) {
                        bestScore = moveScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

}
