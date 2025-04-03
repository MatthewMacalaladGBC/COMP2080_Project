// COMP 2080 - Gomoku Game Project
// Matthew Macalalad, 101510305
// Bramjot Singh,

import java.util.Scanner;

public class Main {

    static final int BOARD_SIZE = 9;
    static final char EMPTY_SPACE = '*';
    static final int NUM_TO_WIN = 5;

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

        String player1Name = "";
        String player2Name = "";
        char player1Symbol = ' ';
        char player2Symbol = ' ';
        boolean singlePlayer = gameMode == 1;

        if (singlePlayer) {
            System.out.println("A.I. opponent logic not yet implemented.");
        } else {
            System.out.print("Player 1, enter your name: ");
            player1Name = scanner.nextLine();
            System.out.print("Player 2, enter your name: ");
            player2Name = scanner.nextLine();

            System.out.println(player1Name + ", choose your colour.");
            System.out.println("Enter B (for Black) to go first.");
            System.out.println("Enter W (for White) to go second.");
            String symbolChoice = "";
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

            if (player1Symbol == 'B') {
                System.out.println(player1Name + " is B (Black) and " + player2Name + " is W (White).");
                System.out.println(player1Name + " will be going first.");
            } else {
                System.out.println(player1Name + " is W (White) and " + player2Name + " is B (Black).");
                System.out.println(player2Name + " will be going first.");
            }

        }

        char[][] gameBoard = new char[BOARD_SIZE][BOARD_SIZE];
        intializeBoard(gameBoard);

        boolean gameOver = false;
        boolean isP1Turn = (player1Symbol == 'B');

        while (!gameOver) {
            displayBoard(gameBoard);

            System.out.println("Enter a line to exit (temp stopping point):");
            String temp = scanner.nextLine();
            gameOver = true;
        }

    }

    public static void intializeBoard(char[][] gameBoard) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameBoard[i][j] = EMPTY_SPACE;
            }
        }
    }

    public static void displayBoard(char[][] gameBoard) {
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

}