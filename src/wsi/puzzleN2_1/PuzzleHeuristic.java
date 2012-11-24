/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

/**
 *
 * @author Soltys
 */
public class PuzzleHeuristic {

    class MisplacedTiles implements IHeuristic {

        @Override
        public double computeHeuristic(byte[][] board) {
            double heuristic = 0;
            for (int i = 0; i < board.length * board.length; i++) {
                int row = i / board.length;
                int column = i % board.length;
                if (board[row][column] != 0) {
                    if (board[row][column] != i) {
                        heuristic++;
                    }
                }
            }
            return heuristic;
        }

        @Override
        public String getHeuristicName() {
            return "Misplaced Tiles";
        }
    }

    class ManhatanDistanceHeuristic implements IHeuristic {

        @Override
        public double computeHeuristic(byte[][] board) {
            double heuristic = 0;
            MatrixPosition pos;            
            int n = board.length;
            for (int i = 0; i < board.length * board.length; i++) {
                pos = findNumberPosition(i, board);
                heuristic += Math.abs(pos.row - Math.floor(i / n)) + Math.abs(pos.column - (i % n));
            }
            return heuristic;
        }

        public MatrixPosition findNumberPosition(int number, byte[][] board) {
            MatrixPosition position = new MatrixPosition();
            for (int row = 0; row < board.length; row++) {
                for (int column = 0; column < board.length; column++) {
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
        public String getHeuristicName() {
          return "Manhatan distance";
        }
    }
    // należy zwrócić uwagę na użycie słowa kluczowego volatile
    private static volatile PuzzleHeuristic instance = null;

    public static PuzzleHeuristic getInstance() {
        if (instance == null) {
            synchronized (PuzzleHeuristic.class) {
                if (instance == null) {
                    instance = new PuzzleHeuristic();
                }
            }
        }
        return instance;
    }

    public final IHeuristic misplacedTiles =  new MisplacedTiles();
    public final IHeuristic manhattanDistance = new ManhatanDistanceHeuristic();
    private PuzzleHeuristic() {
    }
}
