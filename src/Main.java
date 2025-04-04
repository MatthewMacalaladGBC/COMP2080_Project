// COMP 2080 - Gomoku Game Project
// Matthew Macalalad, 101510305
// Bramjot Singh,

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Gomoku (Five in a Row)!");
        System.out.println("Would you like to play against a friend or on your own?");
        System.out.println("Enter 1 for One Player (Player vs A.I.)");
        System.out.println("Enter 2 for Two Players (Player vs Player)");

        int gameMode = 0;
        while (true) {
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

        boolean singlePlayer = (gameMode == 1);
        Player player1, player2;
        String symbolChoice;
        char player1Symbol, player2Symbol;

        if (singlePlayer) {
            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();

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
            player1 = new Player(playerName, player1Symbol);
            player2 = new Player("Computer", player2Symbol);
        } else {
            System.out.print("Player 1, enter your name: ");
            String name1 = scanner.nextLine();
            System.out.print("Player 2, enter your name: ");
            String name2 = scanner.nextLine();

            System.out.println(name1 + ", choose your colour.");
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
            player1 = new Player(name1, player1Symbol);
            player2 = new Player(name2, player2Symbol);
        }

        if (player1.getSymbol() == 'B') {
            System.out.println(player1.getName() + " is B (Black) and " + player2.getName() + " is W (White).");
            System.out.println(player1.getName() + " will be going first.");
        } else {
            System.out.println(player1.getName() + " is W (White) and " + player2.getName() + " is B (Black).");
            System.out.println(player2.getName() + " will be going first.");
        }

        Board gameBoard = new Board();

        boolean gameOver = false;
        boolean isP1Turn = (player1.getSymbol() == 'B');

        while (!gameOver) {
            gameBoard.display();
            Player currentPlayer = isP1Turn ? player1 : player2;
            System.out.println(currentPlayer.getName() + "'s turn (" + currentPlayer.getSymbol() + ")");

            if (singlePlayer && !isP1Turn && currentPlayer.getName().equals("Computer")) {
                System.out.println("Not implemented yet.");
                gameOver = true;
            } else {
                int row, col;
                while (true) {
                    System.out.print("Enter row (1-" + Board.BOARD_SIZE + "): ");
                    try {
                        row = scanner.nextInt() - 1;
                        if (row < 0 || row > 8) {
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
                        if (col < 0 || col > 8) {
                            System.out.println("Invalid input. Try again (Enter a number between 1 and 9).");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Try again (Enter a number between 1 and 9).");
                        scanner.nextLine(); // Clears scanner input to break infinite loop
                        continue;
                    }
                    if (gameBoard.isValidMove(row, col)) {
                        break;
                    } else {
                        System.out.println("Invalid move. Cell is either out of bounds or already occupied. Try again.");
                    }
                }
                gameBoard.setCell(currentPlayer.getSymbol(), row, col);
            }

            if (gameBoard.checkWin(currentPlayer.getSymbol())) {
                gameBoard.display();
                System.out.println(currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ") wins!");
                gameOver = true;
                break;
            }
            if (gameBoard.isBoardFull()) {
                gameBoard.display();
                System.out.println("The game is a draw!");
                gameOver = true;
                break;
            }

            isP1Turn = !isP1Turn;

        }

        scanner.close();

    }

}