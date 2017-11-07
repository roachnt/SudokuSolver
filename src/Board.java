import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Board {

    Square[][] board;
    ArrayList<Square> topLeft = new ArrayList<>();
    ArrayList<Square> topCenter = new ArrayList<>();
    ArrayList<Square> topRight = new ArrayList<>();
    ArrayList<Square> middleLeft = new ArrayList<>();
    ArrayList<Square> middleCenter = new ArrayList<>();
    ArrayList<Square> middleRight = new ArrayList<>();
    ArrayList<Square> bottomLeft = new ArrayList<>();
    ArrayList<Square> bottomCenter = new ArrayList<>();
    ArrayList<Square> bottomRight = new ArrayList<>();

    ArrayList<ArrayList<Square>> subSections = new ArrayList<>();

    /**
     * Constuctor
     * @param file
     */
    public Board(String file) {

        board = new Square[9][9];


        try {
            Scanner fileIn = new Scanner(new File(file));
            int row = 0;
            while (fileIn.hasNextLine()) {
                String line = fileIn.nextLine();
                for (int col = 0; col < 9; col++) {

                    // Parse integer values from lines, dashes become zeros
                    String valueStr = Character.toString(line.charAt(col));
                    int value;
                    try {
                        value = Integer.parseInt(valueStr);
                    }
                    catch (Exception e) {
                        value = 0;
                    }
                    board[row][col] = new Square(value, row, col);
                }
                row++;
            }
            subSections.add(topLeft);
            subSections.add(topCenter);
            subSections.add(topRight);
            subSections.add(middleLeft);
            subSections.add(middleCenter);
            subSections.add(middleRight);
            subSections.add(bottomLeft);
            subSections.add(bottomCenter);
            subSections.add(bottomRight);
            createSubsections();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error reading file");
        }
    }

    /**
     * Creates the 9 subsquares on the board
     */
    private void createSubsections() {
        int maxcol = 3;
        int col = 0;
        int row = 0;
        int maxrow = 3;
        for (ArrayList<Square> subsection : subSections) {
            for (; row < maxrow; row++) {
                for (; col < maxcol; col++) {
                    subsection.add(board[row][col]);
                }
                col -= 3;
            }
            row -= 3;
            col += 3;
            maxcol += 3;
            if (maxcol > 9) {
                col = 0;
                maxcol = 3;
                row += 3;
                maxrow += 3;
            }
        }
    }

    /**
     * A method for adding a domain value to a given list of squares
     * @param squares the squares to be affected
     * @param i the domain value
     */
    public void restoreDomains(ArrayList<Square> squares, int i) {
        for (Square square : squares) square.addToDomain(i);
    }

    /**
     * Gets the squares that share a row, column, or subsection with the
     * given square and removes the given value from their domains
     * @param square a square on the board
     * @param value the value to delete from the domains
     * @return list of affected squares
     */
    public ArrayList<Square> deleteFromDomains(Square square, int value) {
        ArrayList<Square> affectedSquares = new ArrayList<>();

        for (Square rowSquare : getRow(square.row)) {
            if (!square.equals(rowSquare))
            if (rowSquare.domain.remove(new Integer(value))) {
                affectedSquares.add(rowSquare);
            }
        }
        for (Square colSquare : getColumn(square.col)) {
            if (!square.equals(colSquare))
            if (colSquare.domain.remove(new Integer(value))) {
                affectedSquares.add(colSquare);
            }
        }
        for (Square secSquare : getSubsection(square.row, square.col)) {
            if (!square.equals(secSquare))
            if (secSquare.domain.remove(new Integer(value))) {
                affectedSquares.add(secSquare);
            }
        }

        return affectedSquares;
    }

    /**
     * Checks if the domain of any square on the board is empty
     * @return at least one square has an empty domain
     */
    public boolean hasEmptyDomains() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col].domain.size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns all squares that have more than one value in their domain
     * @return list of unassigned squares
     */
    public ArrayList<Square> getUnassignedSquares() {
        ArrayList<Square> result = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col].domain.size() > 1) result.add(board[row][col]);
            }
        }
        return result;
    }

    /**
     * Returns whether every square has been assigned a value
     * @return all squares have an assignment
     */
    public boolean isComplete() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col].domain.size() != 1) return false;
            }
        }
        return true;
    }

    /**
     * Get the given row on the board
     * @param row the row number
     * @return the row
     */
    public Square[] getRow(int row) {
        return board[row];
    }

    /**
     * Get the given column on the board
     * @param col the column number
     * @return the column
     */
    public Square[] getColumn(int col) {
        Square[] result = new Square[9];
        for (int row = 0; row < 9; row++) {
            result[row] = board[row][col];
        }
        return result;
    }

    /**
     * Gets the subsection that the row and column provided
     * reside in
     * @param row the row number
     * @param col the column number
     * @return the subsection
     */
    public ArrayList<Square> getSubsection(int row, int col) {
        if (row < 3 && col < 3) return topLeft;
        if (row < 3 && col >= 3 && col < 6) return topCenter;
        if (row < 3 && col >= 6 && col < 9) return topRight;

        if (row >= 3 && row < 6 && col < 3) return middleLeft;
        if (row >= 3 && row < 6 && col >= 3 && col < 6) return middleCenter;
        if (row >= 3 && row < 6 && col >= 6 && col < 9) return middleRight;

        if (row >= 6 && row < 9 && col < 3) return bottomLeft;
        if (row >= 6 && row < 9 && col >= 3 && col < 6) return bottomCenter;
        return bottomRight;
    }

    @Override
    public String toString() {
        String result = "";
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                result += board[row][col] + " ";
            }
            result += '\n';
        }
        return result;
    }

}
