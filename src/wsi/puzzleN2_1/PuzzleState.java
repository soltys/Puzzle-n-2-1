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
    public static IHeuristic ChosenHeuristic = PuzzleHeuristic.getInstance().manhattanDistance;

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
        setG(0);
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

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSolved() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class Position {

        public int row;
        public int column;
    }

    private boolean safeExchange(int startRow, int startColumn, int endRow, int endColumn) {
        byte tmp = board[startRow][startColumn];
        if (endRow < 0 || endColumn < 0
                || endRow >= board.length || endColumn >= board.length) {
            return false;
        }
        board[startRow][startColumn] = board[endRow][endColumn];
        board[endRow][endColumn] = tmp;
        return true;
    }

    public boolean MoveUp() {
        Position emptySpotPosition = findNumberPosition(0);
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row - 1, emptySpotPosition.column);
    }

    public boolean MoveDown() {
        Position emptySpotPosition = findNumberPosition(0);
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row + 1, emptySpotPosition.column);
    }

    public boolean MoveLeft() {
        Position emptySpotPosition = findNumberPosition(0);
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row, emptySpotPosition.column - 1);
    }

    public boolean MoveRight() {
        Position emptySpotPosition = findNumberPosition(0);
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row, emptySpotPosition.column + 1);
    }

    public void Randomize() {
        Randomize(1000);
    }

    public void Randomize(int factor) {
        Random generator = new Random();
        int movesNumber = factor;
        while (movesNumber > 0) {
            int value = generator.nextInt(4);
            if (makeMove(value)) {
                movesNumber--;
            }
        }
        computeHeuristicGrade();
    }

    private boolean makeMove(int value) {
        switch (value) {
            case 0:
                return MoveUp();
            case 1:
                return MoveDown();
            case 2:
                return MoveLeft();
            default:
                return MoveRight();
        }
    }

    @Override
    public double computeHeuristicGrade() {
        double heuristic = ChosenHeuristic.computeHeuristic(board);
        setH(heuristic);
        return heuristic;
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
    public boolean isAdmissible() {
        return true;
    }

    public static void main(String[] params) {


        PuzzleState state = new PuzzleState(null, 3);

        System.out.println(state);
        state.Randomize();
        System.out.println(state);
    }
}
