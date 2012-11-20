/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

/**
 *
 * @author Soltys
 */
public class SudokuPrinter {

    private final int requiredSpace;
    private final int topNumber;
    private final int boxSize;

    public SudokuPrinter(int topNumber) {
        this.topNumber = topNumber;
        this.boxSize = (int) Math.sqrt(topNumber);
        requiredSpace = (int) (Math.log10(topNumber)) + 1;
    }

    private String sudokuNumberFormat() {
        return "%" + requiredSpace + "s";
    }

    private int getHorizontalLineLength() {
        return topNumber * (requiredSpace + 1) + 1;
    }

    public String getHorizontalLine() {
        StringBuilder sb = new StringBuilder();
        final int horizontalLineLength = getHorizontalLineLength();
        for (int column = 0; column < horizontalLineLength; column++) {
            sb.append("-");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String getNumberSeparator(int column) {
        if (column != 0 && ((column + 1) % boxSize == 0)) {
            return "|";
        } else {
            if (column < topNumber - 1) {
                return ",";
            }
        }
        return "";
    }

    public String printSudoku(byte[][] board) {
        StringBuilder sb = new StringBuilder();
        final String horizontalLine = getHorizontalLine();
        sb.append(horizontalLine);
        for (int row = 0; row < topNumber; row++) {
            sb.append("|");
            for (int column = 0; column < topNumber; column++) {
                sb.append(String.format(sudokuNumberFormat(), board[row][column]));
                sb.append(getNumberSeparator(column));
            }
            sb.append("\n");

            if (row != 0 && ((row + 1) % boxSize == 0)) {
                sb.append(horizontalLine);
            }
        }
        return sb.toString();
    }
}
