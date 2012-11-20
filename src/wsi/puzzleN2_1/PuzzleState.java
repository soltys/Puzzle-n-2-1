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

    public PuzzleState(State parent, byte[][] board) {
        super(parent);
        this.n = board.length;
        this.board = new byte[n][n];
        for (int row = 0; row < n; row++) {
            System.arraycopy(board[row], 0, this.board[row], 0, n);
        }
        printer = new PuzzlePrinter(board.length);
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
    }

    @Override
    public double computeHeuristicGrade() {
        double heurystyka = 0;
        int[] wspolrzedne = new int[2];
        for (int i = 0; i < n * n; i++) {
            wspolrzedne = znajdzWspolrzedne(i);
            heurystyka += Math.abs(wspolrzedne[0] - Math.floor(i / n)) + Math.abs(wspolrzedne[1] - (i % n));
        }

        setH(heurystyka / 2);

        return heurystyka / 2;

    }

    public int[] znajdzWspolrzedne(int liczba) {
        int[] tmp = new int[2];
        boolean isBreak = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == liczba) {
                    tmp[0] = i;
                    tmp[1] = j;
                    isBreak = true;
                    break;
                }
                if (isBreak) {
                    break;
                }
            }
        }
        return tmp;
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
        byte[][] board = new byte[3][3];

        board[0][0] = 1;
        board[0][1] = 2;
        board[0][2] = 3;

        board[1][0] = 4;
        board[1][1] = 0;
        board[1][2] = 5;

        board[2][0] = 6;
        board[2][1] = 7;
        board[2][2] = 8;

        PuzzleState state = new PuzzleState(null, board);

        System.out.println(state);
        state.Randomize();
        System.out.println(state);
    }
}
