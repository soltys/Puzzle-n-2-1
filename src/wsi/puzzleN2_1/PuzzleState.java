/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

import java.util.Random;
import klesk.math.search.State;
import klesk.math.search.StateImpl;

/**
 *
 * @author Soltys
 */
public final class PuzzleState extends StateImpl {

    private final int n;
    private byte[][] board;
    private final PuzzlePrinter printer;

    public PuzzleState(State parent, int n) {
        super(parent);
        this.n = n;
        this.board = new byte[n][n];
        for (int index = 0; index < n * n; index++) {
            int row = index / n;
            int column = index % n;
            board[row][column] = (byte) index;
        }
        printer = new PuzzlePrinter(board.length);
        setG(1);
        computeHeuristicGrade();
    }

    public PuzzleState(State parent) {
        super(parent);

        if (parent == null) {
            throw new NullPointerException();
        }

        PuzzleState puzzleState = (PuzzleState) parent;

        this.n = puzzleState.getN();
        this.board = new byte[n][n];
        for (int row = 0; row < n; row++) {
            System.arraycopy(puzzleState.getBoard()[row], 0, this.board[row], 0, n);
        }
        setG(puzzleState.getG() + 1);
        printer = new PuzzlePrinter(board.length);
    }

    public int getN() {
        return n;
    }

    public byte[][] getBoard() {
        return board;
    }

    public class Position {

        public int row;
        public int column;
    }

    private Position getEmptySpotPosition() {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {
                if (board[row][column] == 0) {
                    Position pos = new Position();
                    pos.row = row;
                    pos.column = column;
                    return pos;
                }
            }
        }
        return null;
    }

    private void safeExchange(int startRow, int startColumn, int endRow, int endColumn) {
        byte tmp = board[startRow][startColumn];
        if (endRow < 0 || endColumn < 0
                || endRow >= board.length || endColumn >= board.length) {
            return;
        }
        board[startRow][startColumn] = board[endRow][endColumn];
        board[endRow][endColumn] = tmp;
    }

    public void MoveUp() {
        Position emptySpotPosition = getEmptySpotPosition();
        safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row - 1, emptySpotPosition.column);
    }

    public void MoveDown() {
        Position emptySpotPosition = getEmptySpotPosition();
        safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row + 1, emptySpotPosition.column);
    }

    public void MoveLeft() {
        Position emptySpotPosition = getEmptySpotPosition();
        safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row, emptySpotPosition.column - 1);
    }

    public void MoveRight() {
        Position emptySpotPosition = getEmptySpotPosition();
        safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row, emptySpotPosition.column + 1);
    }

    public void Randomize() {
        Randomize(1000);
    }

    public void Randomize(int factor) {
        Random generator = new Random();
        for (int i = 0; i < 1000; i++) {
            int value = generator.nextInt(4);
            switch (value) {
                case 0:
                    MoveUp();
                    break;
                case 1:
                    MoveDown();
                    break;
                case 2:
                    MoveLeft();
                    break;
                case 3:
                    MoveRight();
                    break;
            }
        }
        computeHeuristicGrade();
    }

    @Override
    public double computeHeuristicGrade() {
        double heurystyka = 0;

        for (int i = 0; i < n * n; i++) {
            Position position = findNumberPosition(i);
            heurystyka += Math.abs(position.row - Math.floor(i / n)) + Math.abs(position.column - (i % n));
        }

        setH(heurystyka / 2);

        return heurystyka / 2;
    }

    public Position findNumberPosition(int number) {
        Position position = new Position();
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                if (board[row][column] == number) {
                    position.row = row;
                    position.column = column;
                    return position;
                }
            }
        }
        return position;
    }

    @Override
    public String toString() {
        return printer.printSudoku(board);
    }

    @Override
    public String getHashCode() {
        return printer.printSudoku(board);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isSolved() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] params) {


        PuzzleState state = new PuzzleState(null, 3);

        System.out.println(state);
        state.Randomize();
        System.out.println(state);
    }
}
