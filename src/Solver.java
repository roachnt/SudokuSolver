import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    /**
     * Backtracking algorithm to solve sudoku. Prints the final board if solvable
     * @param b the board
     * @return board is solvable
     */
    public static boolean backtrack(Board b) {

        // Get the squares that do not have a value yet,
        // apply minimum remaining values heuristic
        ArrayList<Square> unassignedSquares = b.getUnassignedSquares();
        Collections.sort(unassignedSquares);

        // If the board is finished, return true
        if (b.isComplete()) {
            System.out.println(b);
            return true;
        }

        // Take the first square off of the list
        // of unassigned squares and copy its domain
        Square square = unassignedSquares.remove(0);
        ArrayList<Integer> domain = new ArrayList<>(square.domain);

        // Iterate through the list of domain values
        for (Integer i : domain) {

            // Assign the square a value
            square.assign(i);

            // Get the squares that were affected by the assignment
            ArrayList<Square> affectedSquares = b.deleteFromDomains(square, i);

            // If any square has an empty domain, restore it
            if (b.hasEmptyDomains()) b.restoreDomains(affectedSquares, i);

            // Otherwise, if the board is valid
            else if (isValid(b)){

                // Continue with backtracking until false
                if (backtrack(b)) return true;

                // then restore if an issue is reached
                else b.restoreDomains(affectedSquares, i);
            }

            // If the board isn't valid, restore domain
            else b.restoreDomains(affectedSquares, i);
        }

        // Reset the domain of the square
        square.domain = domain;

        // Report false
        return false;
    }

    /**
     * Checks to make sure every row, column, and subsection is valid
     * @param b the board
     * @return board is valid
     */
    public static boolean isValid (Board b) {
        for (int rowNum = 0; rowNum < 9; rowNum++)
            for (int colNum = 0; colNum < 9; colNum++)
                if (!(rowValid(b, rowNum) && colValid(b, colNum) && subsectionValid(b, b.getSubsection(rowNum, colNum)))) return false;
        return true;
    }

    /**
     * Determines whether a given row in a board is valid
     * @param b the board
     * @param rowNum row number of the given board
     * @return row is valid
     */
    public static boolean rowValid(Board b, int rowNum) {
        Square[] row = b.getRow(rowNum);
        for (Square square1 : row) {
            for (Square square2 : row) {
                if (square1.equals(square2)) continue;
                if (square1.domain.size()==1 && square2.domain.size()==1) {
                    if (square1.domain.get(0).equals(square2.domain.get(0))) return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether a given column in a board is valid
     * @param b the board
     * @param colNum column number of the given board
     * @return column is valid
     */
    public static boolean colValid(Board b, int colNum) {
        Square[] col = b.getColumn(colNum);
        for (Square square1 : col) {
            for (Square square2 : col) {
                if (square1.equals(square2)) continue;
                if (square1.domain.size()==1 && square2.domain.size()==1) {
                    if (square1.domain.get(0).equals(square2.domain.get(0))) return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether a given subsection in a board is valid
     * @param b the board
     * @param subsection subsection of the given board
     * @return subsection is valid
     */
    public static boolean subsectionValid(Board b, ArrayList<Square> subsection) {
        for (Square square1 : subsection) {
            for (Square square2 : subsection) {
                if (square1.equals(square2)) continue;
                if (square1.domain.size()==1 && square2.domain.size()==1) {
                    if (square1.domain.get(0).equals(square2.domain.get(0))) return false;
                }
            }
        }
        return true;
    }

    /**
     * Takes in the initial board and reduces the domain
     * of all squares before implementing the backtracking algorithm
     * @param b the board
     */
    public static void reduceInitialDomain(Board b) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (b.board[row][col].domain.size() == 1) {
                    b.deleteFromDomains(b.board[row][col], b.board[row][col].domain.get(0));
                }
            }
        }
    }

    /**
     * Final solving method for sudoku, runs reducer
     * and backtracker in succession
     * @param b the board
     */
    public static void solve(Board b) {
        reduceInitialDomain(b);
        backtrack(b);
    }

    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {
        Board b1 = new Board("ExampleBoards/board1.txt");
        Board b2 = new Board("ExampleBoards/board2.txt");
        Board b3 = new Board("ExampleBoards/board3.txt");
        Board b4 = new Board("ExampleBoards/board4.txt");
        Board b5 = new Board("ExampleBoards/board5.txt");
        Board b6 = new Board("ExampleBoards/hardest_ever.txt");

        long startTime1 = System.currentTimeMillis();
        solve(b1);
        long endTime1 = System.currentTimeMillis();
        System.out.println((endTime1 - startTime1) + " Milliseconds");
        System.out.println("---------------------");

        long startTime2 = System.currentTimeMillis();
        solve(b2);
        long endTime2 = System.currentTimeMillis();
        System.out.println((endTime2 - startTime2) + " Milliseconds");
        System.out.println("---------------------");

        long startTime3 = System.currentTimeMillis();
        solve(b3);
        long endTime3 = System.currentTimeMillis();
        System.out.println((endTime3 - startTime3) + " Milliseconds");
        System.out.println("---------------------");

        long startTime4 = System.currentTimeMillis();
        solve(b4);
        long endTime4 = System.currentTimeMillis();
        System.out.println((endTime4 - startTime4) + " Milliseconds");
        System.out.println("---------------------");

        long startTime5 = System.currentTimeMillis();
        solve(b5);
        long endTime5 = System.currentTimeMillis();
        System.out.println((endTime5 - startTime5) + " Milliseconds");
        System.out.println("---------------------");

        long startTime6 = System.currentTimeMillis();
        solve(b6);
        long endTime6 = System.currentTimeMillis();
        System.out.println((endTime6 - startTime6) + " Milliseconds");
        System.out.println("---------------------");
    }
}
