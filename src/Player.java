// COMP 2080 - Gomoku Game Project
// Matthew Macalalad, 101510305
// Bramjot Singh, 101511990

// Simple Player object to hold names and symbols
public class Player {
    private String name;
    private char symbol;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }
}
