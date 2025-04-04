// COMP 2080 - Gomoku Game Project
// Matthew Macalalad, 101510305
// Bramjot Singh, 101511990

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Gomoku (Five in a Row)!");
        System.out.println("Would you like to play against a friend or on your own?");
        System.out.println("Enter 1 for One Player (Player vs A.I.)");
        System.out.println("Enter 2 for Two Players (Player vs Player)");

        // Initialize the variable to hold game mode (single player = 1, player vs player = 2)
        int gameMode = 0;

        // Infinite loop to repeatedly get user input until a valid option is selected
        while (true) {
            // try catch in loop to check for int, then check that the int is one of the options
            try {
                gameMode = scanner.nextInt();
                if (gameMode < 1 || gameMode > 2) {
                    System.out.println("Invalid input. Try again (Enter 1 or 2).");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Try again (Enter 1 or 2).");
                scanner.next(); // Clears scanner input to break infinite loop
            }
        }

        System.out.println("Game mode selected: " + (gameMode == 1 ? "Single Player" : "Two Players"));

        scanner.nextLine(); // Clears any leftover input (ex. newline character left after nextInt())

        // Boolean to store if selected game mode is single player or not
        boolean singlePlayer = (gameMode == 1);
        // Declare Player objects and symbol variables
        Player player1, player2;
        String symbolChoice;
        char player1Symbol, player2Symbol;

        // Logic to get player name(s). Sets player2 to "Computer" if game mode is single player
        // Assigns second player symbol depending on player 1's choice
        System.out.print((singlePlayer ? "E" : "Player 1, e") + "nter your name: ");
        String playerName = scanner.nextLine();
        String player2Name;
        if (!singlePlayer) {
            System.out.print("Player 2, enter your name: ");
            player2Name = scanner.nextLine();
        } else {
            player2Name = "Computer";
        }
        System.out.println(playerName + ", choose your colour.");
        System.out.println("Enter B (for Black) to go first.");
        System.out.println("Enter W (for White) to go second.");
        while (true) {
            symbolChoice = scanner.next().toUpperCase();
            if (symbolChoice.equals("B") || symbolChoice.equals("W")) {
                player1Symbol = symbolChoice.charAt(0);
                player2Symbol = (player1Symbol == 'B' ? 'W' : 'B');
                break;
            } else {
                System.out.println("Invalid symbol chosen. Please enter B or W.");
            }
        }
        // Initializes the players using the given names and symbols
        player1 = new Player(playerName, player1Symbol);
        player2 = new Player(player2Name, player2Symbol);

        // Prints out symbols and turn order for the players
        if (player1.getSymbol() == 'B') {
            System.out.println(player1.getName() + " is B (Black) and " + player2.getName() + " is W (White).");
            System.out.println(player1.getName() + " will be going first.");
        } else {
            System.out.println(player1.getName() + " is W (White) and " + player2.getName() + " is B (Black).");
            System.out.println(player2.getName() + " will be going first.");
        }

        // Initializes the Board object
        Board gameBoard = new Board();

        // Variable to hold game state (game will keep running as long as gameOver is false)
        boolean gameOver = false;
        // Sets turn - if player1's symbol is B, then we know they should go first (therefore var = true) and vice versa
        boolean isP1Turn = (player1.getSymbol() == 'B');

        // Game loop
        while (!gameOver) {
            gameBoard.display(); // Displays board at start of loop
            Player currentPlayer = isP1Turn ? player1 : player2; // Sets current player based on isP1Turn variable
            System.out.println(currentPlayer.getName() + "'s turn (" + currentPlayer.getSymbol() + ")");

            // Check following conditions to see if computer needs to calculate a move
            // Only if game mode is single player, it is not player one's turn (in single player, would always be the human),
            // and if the currentPlayer is equal to "Computer" (Computer (aka second player's) turn).
            if (singlePlayer && !isP1Turn && currentPlayer.getName().equals("Computer")) {
                System.out.println("Computer is making a move..."); // Print out to show that best move is being calculated
                // Sets a variable to hold the best move found using the findBestMove function in the AI class
                    // Makes use of recursive minimax
                // Depth is set to 4, meaning computer will look up to 4 turns ahead.
                // Alpha-beta pruning optimized the time this takes, but any increase in depth would take exponentially longer
                int[] move = AI.findBestMove(gameBoard, 4, currentPlayer.getSymbol(), player1.getSymbol());
                if (move[0] == -1) { // Checks that a valid move was found
                    System.out.println("No valid moves remaining.");
                } else {
                    // If valid move is found, update the board by placing the computer's symbol into the found cell
                    gameBoard.setCell(currentPlayer.getSymbol(), move[0], move[1]);
                    System.out.println("Computer has made a move at row " + (move[0] + 1) + ", column " + (move[1] + 1));
                }
            } else { // runs when not computer's turn (aka player one's turn in single player, or when game mode is player vs player)
                int row, col;
                while (true) { // Loop to get a valid row and column number input
                    System.out.print("Enter row (1-" + Board.BOARD_SIZE + "): ");
                    try {
                        row = scanner.nextInt() - 1; // - 1, since input is based on displayed row number rather than actual index
                        if (row < 0 || row > 8) { // Bounds check
                            System.out.println("Invalid input. Try again (Enter a number between 1 and 9).");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Try again (Enter a number between 1 and 9).");
                        scanner.nextLine(); // Clears scanner input to break infinite loop
                        continue;
                    }
                    System.out.print("Enter column (1-" + Board.BOARD_SIZE + "): ");
                    try {
                        col = scanner.nextInt() - 1;
                        if (col < 0 || col > 8) { // Bounds check
                            System.out.println("Invalid input. Try again (Enter a number between 1 and 9).");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Try again (Enter a number between 1 and 9).");
                        scanner.nextLine(); // Clears scanner input to break infinite loop
                        continue;
                    }
                    if (gameBoard.isValidMove(row, col)) {
                        break; // if provided row and column points to a valid cell, breaks loop
                    } else {
                        // otherwise, repeats loop again
                        System.out.println("Invalid move. Cell is either out of bounds or already occupied. Try again.");
                    }
                }
                // Once loop is finished (valid move has been given), updates board accordingly
                gameBoard.setCell(currentPlayer.getSymbol(), row, col);
            }
            // Checks for a win
            if (gameBoard.checkWin(currentPlayer.getSymbol())) {
                gameBoard.display();
                // If a win is found, prints out winning player and breaks out of game loop
                System.out.println(currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ") wins!");
                gameOver = true;
                break;
            }
            // Checks if board is full
            if (gameBoard.isBoardFull()) {
                gameBoard.display();
                // If board is full, states a draw and breaks out of loop
                System.out.println("The game is a draw!");
                gameOver = true;
                break;
            }
            // If no win and board is not full after a valid move, swaps the player's turn, then loops again from the top
            isP1Turn = !isP1Turn;
        }
        scanner.close(); // Closes scanner once gameplay loop ends
    }
}