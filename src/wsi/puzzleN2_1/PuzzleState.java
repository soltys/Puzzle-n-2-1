/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

import klesk.math.search.State;
import klesk.math.search.StateImpl;

/**
 *
 * @author Soltys
 */
public final class PuzzleState extends StateImpl
{

    private final int n;
    private byte[][] board;
    private final PuzzlePrinter printer;

    public PuzzleState(State parent, byte[][] board)
    {
        super(parent);
        this.n = board.length;
        this.board = new byte[n][n];
        for (int row = 0; row < n; row++)
        {
            System.arraycopy(board[row], 0, this.board[row], 0, n);
        }
        printer = new PuzzlePrinter(board.length);
        computeHeuristicGrade();
    }

    public PuzzleState(State parent)
    {

        super(parent);

        if (parent == null)
        {
            throw new NullPointerException();
        }

        PuzzleState puzzleState = (PuzzleState) parent;

        this.n = puzzleState.getN();
        this.board = new byte[n][n];
        for (int row = 0; row < n; row++)
        {
            System.arraycopy(puzzleState.getBoard()[row], 0, this.board[row], 0, n);
        }

        printer = new PuzzlePrinter(board.length);
    }

    public int getN()
    {
        return n;
    }

    public byte[][] getBoard()
    {
        return board;
    }

    @Override
    public double computeHeuristicGrade()
    {
        setH(0);
        return 0;
    }

    @Override
    public String toString()
    {
        return printer.printSudoku(board);
    }

    @Override
    public String getHashCode()
    {
        return printer.printSudoku(board);
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public boolean isSolved()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] params)
    {
        byte[][] board = new byte[3][3];

        board[0][0] = 0;
        board[0][1] = 1;
        board[0][2] = 2;

        board[1][0] = 3;
        board[1][1] = 4;
        board[1][2] = 5;

        board[2][0] = 6;
        board[2][1] = 7;
        board[2][2] = 8;

        PuzzleState state = new PuzzleState(null, board);

        System.out.println(state);


    }
}
