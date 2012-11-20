/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

import java.util.ArrayList;
import java.util.List;
import klesk.math.search.State;
import klesk.math.search.StateImpl;

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
            switch (index) {
                case 0:
                    childState.MoveUp();
                    break;
                case 1:
                    childState.MoveDown();
                    break;
                case 2:
                    childState.MoveLeft();
                    break;
                case 3:
                    childState.MoveRight();
                    break;
            }
            childState.computeHeuristicGrade();
            children.add(childState);
        }
        parent.setChildren(children);
    }

    @Override
    public boolean isSolution(State aState) {
        return aState.getH() == 0;

    }

    public static void main(String[] args) {
        

        PuzzleState puzzle = new PuzzleState(null, 3);
        System.out.println(puzzle);
        puzzle.Randomize();
        System.out.println(puzzle);
        PuzzleSolver solver = new PuzzleSolver(puzzle, true, true);
        solver.doSearch();
        PuzzleState temp = (PuzzleState) solver.getSolutions().get(0);
        List<PuzzleState> steps = new ArrayList<PuzzleState>();

        while (temp.getParent() != null) {
            steps.add(temp);
            temp = (PuzzleState) temp.getParent();
        }
        

        System.out.println("Ilosc krokow: " + steps.size());

        System.out.println("Ilosc odwiedzonych stanow: " + solver.getClosed().size());
    }
}
