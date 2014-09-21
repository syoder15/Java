package cannibals;

import java.util.ArrayList;
import java.util.Arrays;

import cannibals.UUSearchProblem.UUSearchNode;


// for the first part of the assignment, you might not extend UUSearchProblem,
//  since UUSearchProblem is incomplete until you finish it.

public class CannibalProblem extends UUSearchProblem{

	// the following are the only instance variables you should need.
	//  (some others might be inherited from UUSearchProblem, but worry
	//  about that later.)

	private int goalm, goalc, goalb;
	private int totalMissionaries, totalCannibals; 
	//public CannibalNode startNode;
	
/*	public static void main (String args[]){
		
		CannibalProblem mcProblem = new CannibalProblem (2, 0, 1, 0, 0, 0);
		
		System.out.println(mcProblem.startNode.getSuccessors());
	}*/

	
	public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
		// I (djb) wrote the constructor; nothing for you to do here.

		startNode = new CannibalNode(sm, sc, 1, 0);
		goalm = gm;
		goalc = gc;
		goalb = gb;
		totalMissionaries = sm;
		totalCannibals = sc;
		
	}
	
	// node class used by searches.  Searches themselves are implemented
	//  in UUSearchProblem.
	private class CannibalNode implements UUSearchNode {

		// do not change BOAT_SIZE without considering how it affect
		// getSuccessors. 
		
		private final static int BOAT_SIZE = 2;
	
		// how many missionaries, cannibals, and boats
		// are on the starting shore
		private int[] state; 
		
		// how far the current node is from the start.  Not strictly required
		//  for search, but useful information for debugging, and for comparing paths
		private int depth;  

		public CannibalNode(int m, int c, int b, int d) {
			state = new int[3];
			this.state[0] = m;
			this.state[1] = c;
			this.state[2] = b;
			
			depth = d;

		}

		//Not implementing BFS here, literally use this just to figure out what the next possible moves are.
		//Making this pretty long, maybe post in Piazza and see whether or not this is correct
		public ArrayList<UUSearchNode> getSuccessors() {
			
			ArrayList<UUSearchNode> successorsList = new ArrayList<UUSearchNode>();
				
			
			// add actions (denoted by how many missionaries and cannibals to put
			// in the boat) to current state. 
			// You write this method.  Factoring is usually worthwhile.  In my
			//  implementation, I wrote an additional private method 'isSafeState',
			//  that I made use of in getSuccessors.  You may write any method
			//  you like in support of getSuccessors.
						
			ArrayList<UUSearchNode> successorList = new ArrayList<UUSearchNode>();
			int missionaries;
			int cannibals;
			
			if (state[2] == 1){ //if there is a boat on this side
				missionaries = state[0];
				cannibals = state[1];
			}
			else{
				missionaries = 3 - state[0]; //boat on other side
				cannibals = 3 - state[1];
			}
			if (missionaries >= 2){
				if (state[2] == 1){
					CannibalNode twoMissState = new CannibalNode(state[0] - 2, state[1], 0, depth + 1);
					if (isSafeState(twoMissState)){
						successorList.add(twoMissState);
					}
				}
				else{
					CannibalNode twoMissInverse = new CannibalNode(state[0] + 2, state[1], 1, depth + 1);
					if (isSafeState(twoMissInverse)){
						successorList.add(twoMissInverse);
					}
				}
			}
			if (cannibals >= 2){
				if (state[2] == 1){
					CannibalNode twoCannState = new CannibalNode(state[0], state[1] - 2, 0, depth + 1);
					if (isSafeState(twoCannState)){
						successorList.add(twoCannState);
					}
				}
				else{
					CannibalNode twoCannInverse = new CannibalNode(state[0], state[1] + 2, 1, depth + 1);
					if (isSafeState(twoCannInverse)){
						successorList.add(twoCannInverse);
					}
				}
			}
			if (missionaries >= 1 && cannibals >= 1){
				if (state[2] == 1){
					CannibalNode equality = new CannibalNode(state[0] - 1, state[1] - 1, 0, depth + 1);
					if (isSafeState(equality)){
						successorList.add(equality);
					}
				}
				else{
					CannibalNode equalityInverse = new CannibalNode(state[0] + 1, state[1] + 1, 1, depth + 1);
					if (isSafeState(equalityInverse)){
						successorList.add(equalityInverse);
					}
				}	
			}
			if (missionaries >= 1){
				if (state[2] == 1){
					CannibalNode soloMiss = new CannibalNode(state[0] - 1, state[1], 0, depth + 1);
					if (isSafeState(soloMiss)){
						successorList.add(soloMiss);
					}
				}
				else{
					CannibalNode soloMissInverse = new CannibalNode(state[0] + 1, state[1], 1, depth + 1);
					if (isSafeState(soloMissInverse)){
						successorList.add(soloMissInverse);
					}
				}
			}
			if (cannibals >= 1){
				if (state[2] == 1){
					CannibalNode soloCann = new CannibalNode(state[0], state[1] - 1, 0, depth + 1);
					if (isSafeState(soloCann)){
						successorList.add(soloCann);
					}
				}
				else{
					CannibalNode soloCannInverse = new CannibalNode(state[0], state[1] + 1, 1, depth + 1);
					if (isSafeState(soloCannInverse)){
						successorList.add(soloCannInverse);
					}
				}
			}
			return successorList;
		}
		

		//Test whether you have reached the goal node or not. If the state is 0, 0, 0 that means you've gotten each monk
		//and cannibal across the river safely
		@Override
		public boolean goalTest() {
			// TODO Auto-generated method stub
			return this.state[0] == 0 && this.state[1] == 0 && this.state[2] == 0;
		}
		
        //You might need this method when you start writing 
        //(and debugging) UUSearchProblem.
		@Override
		public int getDepth() {
			return depth;
		}
			
		
		//Private method that determines whether the next state is a safe state to make
		//More monks than cans on this side
		//More monks than cans on other side
		//0 monks is ok
		private boolean isSafeState (CannibalNode potentialState){
			int overRiverMiss = (3 - potentialState.state[0]);
			int overRiverCan = (3 - potentialState.state[1]);
			
			return (potentialState.state[0] >= potentialState.state[1] || potentialState.state[0] == 0) &&
					(overRiverMiss >= overRiverCan || overRiverMiss == 0);
		}
		

		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(this.state, ((CannibalNode) other).state);
		}

		@Override
		public int hashCode() {
			return state[0] * 100 + state[1] * 10 + state[2];
		}

		@Override
		public String toString() {
			return Integer.toString(this.state[0]) + " " +
					Integer.toString(this.state[1]) + " " +
					Integer.toString(this.state[2]);
		}
		

	}
	

}
