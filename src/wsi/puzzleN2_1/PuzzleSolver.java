/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import klesk.math.search.State;

/**
 *
 * @author Soltys
 */
public final class PuzzleSolver extends klesk.math.search.AStarSearcher {

    public PuzzleSolver(State aInitialState, boolean aIsStopAfterFirstSolution, boolean aIsStopAfterSecondSolution) {
        super(aInitialState, aIsStopAfterFirstSolution, aIsStopAfterSecondSolution);
    }

    @Override
    public void buildChildren(State aParent) {

        PuzzleState parent = (PuzzleState) aParent;

        List<State> children = new ArrayList<State>();
        for (int index = 0; index < 4; index++) {
            PuzzleState childState = new PuzzleState(parent);
            boolean changed = false;
            switch (index) {
                case 0:
                    changed = childState.MoveUp();
                    break;
                case 1:
                    changed = childState.MoveDown();
                    break;
                case 2:
                    changed = childState.MoveLeft();
                    break;
                case 3:
                    changed = childState.MoveRight();
                    break;
            }
            if (changed) {
                childState.computeHeuristicGrade();
                children.add(childState);
            }

        }
        parent.setChildren(children);
    }

    @Override
    public boolean isSolution(State aState) {
        return aState.getH() == 0;

    }

    public static void main(String[] args) {
        final int boardSize = 3;
        final int scrambleFactor = 25;
        boolean showSteps = false;
        test(boardSize, scrambleFactor, showSteps, PuzzleHeuristic.getInstance().misplacedTiles);
        test(boardSize, scrambleFactor, showSteps, PuzzleHeuristic.getInstance().manhattanDistance);
    }

    private static void test(int puzzleSize, int scrambleFacotor, boolean showSteps,
            IHeuristic heuristic) {

        PuzzleState.ChosenHeuristic = heuristic;
        System.out.println("+++ " + heuristic.getHeuristicName() + " +++");
        PuzzleState puzzle = new PuzzleState(null, puzzleSize);
        puzzle.Randomize(scrambleFacotor);
        System.out.println(puzzle);
        PuzzleSolver solver = new PuzzleSolver(puzzle, true, true);
        long startTime = System.currentTimeMillis();
        solver.doSearch();
        long endTime = System.currentTimeMillis();
        System.out.println("Czas rozwiązywania: " + (endTime-startTime) + "ms");
        PuzzleState temp = (PuzzleState) solver.getSolutions().get(0);
        List<PuzzleState> steps = new ArrayList<PuzzleState>();

        while (temp.getParent() != null) {
            steps.add(temp);
            temp = (PuzzleState) temp.getParent();
        }
        System.out.println("Ilosc krokow: " + steps.size());
        System.out.println("Ilosc odwiedzonych stanow: " + solver.getClosed().size());
        if (showSteps) {
            System.out.println("Kroki:");
            Collections.reverse(steps);
            for (State step : steps) {
                System.out.println(step);
            }

        }
    }
}
