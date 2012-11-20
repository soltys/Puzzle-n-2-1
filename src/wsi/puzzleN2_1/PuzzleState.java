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
        Position emptySpotPosition = getEmptySpotPosition();
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row - 1, emptySpotPosition.column);
    }

    public boolean MoveDown() {
        Position emptySpotPosition = getEmptySpotPosition();
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row + 1, emptySpotPosition.column);
    }

    public boolean MoveLeft() {
        Position emptySpotPosition = getEmptySpotPosition();
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
                emptySpotPosition.row, emptySpotPosition.column - 1);
    }

    public boolean MoveRight() {
        Position emptySpotPosition = getEmptySpotPosition();
        return safeExchange(emptySpotPosition.row, emptySpotPosition.column,
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
        double heuristic = 0;        
        for (int i = 0; i < board.length * board.length; i++) {            
            int row = i/n;
            int column = i % n;
            if (board[row][column]!=0) {
                if (board[row][column] != i) {
                    heuristic++;
                }
            }
        }
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
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isAdmissible() {
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
