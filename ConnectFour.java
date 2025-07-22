//Taise Nish
//06/01/2024
//CSE 123 B 
//TA: Evan Wu
//C1: Abstract Abstract Strategy Games
//Implements a strategy game, connect four,in which players make a sequence of moves
//according to a set of rules hoping to achieve a particular outcome

import java.util.*;

/**
 * A class to represent a game of Connect Four that implements the 
 * AbstractStrategyGame interface.
 */
public class ConnectFour extends AbstractStrategyGame {
    private char[][] board;
    private boolean isRedTurn;
    private final int rows = 6;
    private final int columns = 7;

   // Constructs a new ConnectFour game.
    public ConnectFour() {
        board = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], '-');
        }
        isRedTurn = true;
    }

    // Method which instructs users on how to play the game. It returns a string describing how to
    //play the game. It includes how to read the game state, how to make a move, how the game 
    //ends, and who wins.Return type is String
    public String instructions() {
        return "Connect Four:\n" +
                "Players take turns dropping their tokens into one of the columns.\n" +
                "Player 1 is Red (R) and Player 2 is Yellow (Y).\n" +
                "The goal is to connect four of your tokens in a row either horizontally, vertically, or diagonally.\n" +
                "On each turn, enter the column number (0 to 6) to drop your token.\n" +
                "The game ends when a player connects four tokens or the board is full.";
    }

    // Constructs and returns a string representation called result of the current game state 
    //which should contain all information that should be known to players at any point in the game
    //Return type is String.
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result += board[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    // Returns the index of the player who has won the game, or -1 if the game is not over.
    //If the game ended in a tie, returns 0. Return type is integer.
    @Override
    public int getWinner() {
        // Check for a winner in all directions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] != '-' && (checkDirection(i, j, 1, 0) || // horizontal
                                           checkDirection(i, j, 0, 1) || // vertical
                                           checkDirection(i, j, 1, 1) || // diagonal \
                                           checkDirection(i, j, 1, -1))) { // diagonal /
                    return board[i][j] == 'R' ? 1 : 2;
                }
            }
        }

        // Check for a tie
        for (int i = 0; i < columns; i++) {
            if (board[0][i] == '-') {
                return -1; // Game is not over
            }
        }
        return 0; // It's a tie
    }

    // Helper method to check for a sequence of four tokens in a given direction.Parameters 
    //include 4 integers, row,col,rowDir and col Dir which represents the starting row position,
    //the starting column position, the row direction to check and the column direction to check
    //respectively.If the sequence of four tokens is  not found, false is returned ; if it is, true
    //is returned.
    private boolean checkDirection(int row, int col, int rowDir, int colDir) {
        char token = board[row][col];
        for (int i = 1; i < 4; i++) {
            int newRow = row + i * rowDir;
            int newCol = col + i * colDir;
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= columns || board[newRow][newCol] != token) {
                return false;
            }
        }
        return true;
    }

    // Returns the index of the player who will take the next turn. If the game is over, returns
    // -1. Return type is integer.
    @Override
    public int getNextPlayer() {
        if (isGameOver()) {
            return -1;
        }
        return isRedTurn ? 1 : 2;
    }

    //Takes input from the parameter to specify the move the player
    //with the next turn wishes to make, then executes that move. 
    //If any part of the move is illegal, throws an IllegalArgumentException.
    //Parameter is a Scanner object called input which accepts user input.
    public void makeMove(Scanner input) {
        char currPlayer = isRedTurn ? 'R' : 'Y';

        System.out.print("Do you want to place or remove a token? (p/r): ");
        char action = input.next().charAt(0);

        if (action == 'r') {
            removeToken(input, currPlayer);
        } else if (action == 'p') {
            System.out.print("Column? ");
            int col = input.nextInt();
            makeMove(col, currPlayer);
            isRedTurn = !isRedTurn;
        } else {
            System.out.println("Invalid action. Please enter 'p' to place or 'r' to remove a token.");
        }
    }

    // Private helper method for makeMove. Given a column and player, drops the token in the 
    //appropriate row. Parameters include an integer col which represents the gien column and 
    // a character player which represents the player.
    private void makeMove(int col, char player) {
        if (col < 0 || col >= columns) {
            throw new IllegalArgumentException("Invalid column: " + col);
        }

        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == '-') {
                board[row][col] = player;
                return;
            }
        }
        throw new IllegalArgumentException("Column is full: " + col);
    }

    //Removes a token from the bottom row of any column.Parameters include  a character player
    //which represents the player and Scanner object called input which accepts user input.
    private void removeToken(Scanner input, char player) {
        System.out.print("Enter the column number to remove the token from: ");
        int col = input.nextInt();

        if (col < 0 || col >= columns) {
            throw new IllegalArgumentException("Invalid column: " + col);
        }

        // Find the bottom-most token in the column and remove it
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == player) {
                board[row][col] = '-';
                // Shift all tokens above this row down by one position
                for (int r = row - 1; r >= 0; r--) {
                    board[r + 1][col] = board[r][col];
                    board[r][col] = '-';
                }
                return;
            }
        }

        // If no token of the player is found in the column
        throw new IllegalArgumentException("No token of player " + player + " found in column " + col);
    }
}

