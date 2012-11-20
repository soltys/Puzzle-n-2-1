package klesk.math.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A general engine performing the A* algorithm. 
 * Specific searchers (for particular problems) are meant to extend this class. 
 * 
 * @author Przemyslaw Klesk (<a href="mailto:pklesk@wi.zut.edu.pl">pklesk@wi.zut.edu.pl</a>)
 */
public abstract class AStarSearcher {

	/**
	 * Boolean showing wether to stop the algorithm after the first solution is found.
	 */
	protected boolean isStopAfterFirstSolution = true;

	/**
	 * Boolean showing wether to stop the algorithm after the second solution is found.
	 */
	protected boolean isStopAfterSecondSolution = true;	
	
	/**
	 * Open set = states to be visited (in a priority queue).
	 */
	protected Queue<State> open = null;
	
	/**
	 * Closed set = map of pairs (hash code of state, reference to that state)
	 * keeping all the visited states.
	 */
	protected Map<String, State> closed = null;
	
	/**
	 * List of solutions found. 
	 */
	protected List<State> solutions = null;	
	
	/**
	 * Reference to a initial state.
	 */
	protected State initialState = null;
	
	
	/**
	 * Creates a new instance of a AStarSearcher.
	 * 
	 * @param aInitialState reference to a initial state
	 * @param aIsStopAfterFirstSolution boolean stating wether to stop the algorithm after the first solution is found
	 * @param aIsStopAfterSecondSolution boolean stating wether to stop the algorithm after the first solution is found 
	 */
	public AStarSearcher(State aInitialState, boolean aIsStopAfterFirstSolution, boolean aIsStopAfterSecondSolution) {
		open = new PriorityQueue<State>();
		closed = new HashMap<String, State>();
		solutions = new LinkedList<State>();		
		initialState = aInitialState;
		isStopAfterFirstSolution = aIsStopAfterFirstSolution;;
		isStopAfterSecondSolution = aIsStopAfterSecondSolution;;
	}
	
	/**
	 * Returns the list of all solutions found.
	 * 
	 * @return list of all solutions found
	 */
	public List<State> getSolutions() {
		return solutions;
	}
	
	/**
	 * Returns the closed set (map).
	 * 
	 * @return closed 
	 */
	public Map<String, State> getClosed() {
		return closed;
	}	
	
	
	/**
	 * Registers a solution. (More acctions can be added here, by overriding this method).
	 *
	 * @param aSolutionState given state
	 */
	protected void registerSolution(State aSolutionState) {
		solutions.add(aSolutionState);
	}
	
	/**
	 * Performs the A* search.
	 */
	public void doSearch() {
		State currentState = initialState;
		while (true) {
			boolean isSolution = isSolution(currentState);
			if (isSolution) {
				registerSolution(currentState);
				if (isStopAfterFirstSolution) break;
				if ((solutions.size() == 2) && (isStopAfterSecondSolution)) break;
			}
			else {
				buildChildren(currentState);
				
				List<State> children = currentState.getChildren();
				for (State child : children) {
					if (closed.containsKey(child.getHashCode())) continue;
					if (!open.contains(child)) {
						open.add(child);
					}
					else {
						boolean isNewBetter = false;
						State theExisting = null;
						//finding existing in the open set
						for (State existing : open) {
							if (existing.equals(child)) {
								if (child.getG() < existing.getG()) {
									isNewBetter = true;
									theExisting = existing;
								}
								break;
							}
						}
						if (isNewBetter) {
							open.remove(theExisting);
							open.add(child);
						}
					}
				}
			}
			closed.put(currentState.getHashCode(), currentState);
			if (open.isEmpty()) break; //stop condition of the algorithm 
			currentState = open.poll();			
		}
	}		
	
	/**
	 * Returns the boolean stating wether a given state is regarded as a solution.
	 * 
	 * @param aState reference to the given state
	 * @return boolean stating wether a given state is regarded as a solution
	 */
	public abstract boolean isSolution(State aState);
	
	/**
	 * This method is meant to contain all operations needed
	 * by the user to populate a given state with suitable (in some sense)
	 * children.  
	 * 
	 * @param aParent reference to the parent state 
	 */
	public abstract void buildChildren(State aParent);	
}