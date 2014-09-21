
// CLEARLY INDICATE THE AUTHOR OF THE FILE HERE (YOU),
//  AND ATTRIBUTE ANY SOURCES USED (INCLUDING THIS STUB, BY
//  DEVIN BALKCOM).
//Author: Sam Yoder
//Contributing Sources: Devin Balkcom, Stack Overflow, Jack Terwilliger, Piazza

package cannibals;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public abstract class UUSearchProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;
	
	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
	}

	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.
	
	public List<UUSearchNode> breadthFirstSearch(){
		resetStats();
		// You will write this method

		UUSearchNode currentNode; //current node
		UUSearchNode nextNode; //next node
		Iterator<UUSearchNode> i; //iteration for the successors
		Queue<UUSearchNode> BFSqueue = new LinkedList<UUSearchNode>(); //queue to push successors onto
		HashMap<UUSearchNode, UUSearchNode> visitedNodes = new HashMap<UUSearchNode, UUSearchNode>(); //hashmap to trace through graph
		ArrayList<UUSearchNode> successorList; //successors

		currentNode = startNode;
		BFSqueue.add(currentNode); //add startNode to queue
		visitedNodes.put(startNode, null); //add into the list
		
		while (!currentNode.goalTest()){ //while we haven't found the goal
			currentNode = BFSqueue.poll(); //pull from the queue
			successorList = currentNode.getSuccessors(); //get its successors
			i = successorList.iterator(); //and iterate through it
			
			while (i.hasNext()){ //while there is a next successor
				nextNode = i.next(); //set the next node
				
				if (visitedNodes.get(nextNode) == null){ //if there isn't anything for that node
					visitedNodes.put(nextNode, currentNode); //put it into the map
					BFSqueue.add(nextNode); //add it to the queue
					incrementNodeCount(); //count up
				}
			}
			updateMemory(visitedNodes.size());
		}
		return backchain(currentNode, visitedNodes); //backchain
	}
	
	// backchain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {
		//iterate through the visited by finding the predecessor
		//then finding its predecessor, etc. until you
		//reach the start node. Add each node to the path
		//as you go
		Stack<UUSearchNode> backchainPath = new Stack<UUSearchNode>();
		UUSearchNode currentNode = node;
		backchainPath.push(currentNode);
		
		while (currentNode != startNode){ //while we haven't reached the start node
			currentNode = visited.get(currentNode); //set the predecessor to the current node
			backchainPath.push(currentNode); //push onto the stack
		}
		return backchainPath;
		
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats();
		HashMap<UUSearchNode, Integer> visitedNodes = new HashMap<UUSearchNode, Integer>(); //list of visited nodes
		visitedNodes.put(startNode, 0); //placing the start node in map with depth of 0
		
		return dfsrm(startNode, visitedNodes, 0, maxDepth); //call recursive function to do the work
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {
		
		// keep track of stats; these calls charge for the current node
		updateMemory(visited.size());
		incrementNodeCount();
		ArrayList<UUSearchNode> successorList; //list of successors
		Stack<UUSearchNode> backtrackList; //tracing back so we know path
		
		//base case
		if (currentNode.goalTest()){ //if we have found the goal
			backtrackList = new Stack<UUSearchNode>(); //initialize the path
			backtrackList.push(currentNode); //add the current node to it
			return backtrackList; 
		}
		
		//recursive
		else{
			successorList = currentNode.getSuccessors(); //list of successors
			for (UUSearchNode successor : successorList){ //for each successor
				if (((!visited.containsKey(successor)) || (visited.get(successor) > depth))){ //if visited doesn't contain the successor or if it's a greater depth
					visited.put(successor, depth); //place into the map
					backtrackList = (Stack<UUSearchNode>) dfsrm(successor, visited, depth + 1, maxDepth); //recurse
					if (backtrackList != null){ //if it doesn't return something null
						backtrackList.push(currentNode); //add to the list
						return backtrackList;
					}
				}
			}
		}
		return null; //should never reach here
	}
	
	
	// set up the iterative deepening search, and make use of dfsrpc
	//fix this, do it at 1, 2, 3, etc depth
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		
		HashSet<UUSearchNode> visitedNodes = new HashSet<UUSearchNode>(); //visited node set
		List<UUSearchNode> backtrackList = new ArrayList<UUSearchNode>(); //path back to the goal
		int depth = 0; //depth
		boolean looping; //keep the while loop going
		looping = true; //set it true until I tell it otherwise
		while (looping){
			backtrackList = dfsrpc(startNode, visitedNodes, depth, maxDepth); //call recursive function
			if (backtrackList != null){ //if it doesn't return something null
				looping = false; //break the loop
			}
			depth++; //increment depth
		}
		return backtrackList; //return list
		
	}

	// set up the depth-first-search (path-checking version), 
	//  but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		
		// I wrote this method for you.  Nothing to do.

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();

		return dfsrpc(startNode, currentPath, 0, maxDepth);

	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {
		
		
		// keep track of stats; these calls charge for the current node
		updateMemory(currentPath.size());
		incrementNodeCount();
		
		ArrayList<UUSearchNode> successorList; //successors
		Stack<UUSearchNode> backtrackList; //path back
		currentPath.add(currentNode); //add current node to the path
		
		//base case
		if (currentNode.goalTest()){ //if the current node is the goal
			backtrackList = new Stack<UUSearchNode>(); //initialize stack
			backtrackList.push(currentNode); //add the current node to it
			return backtrackList;
		}
		
		//recursive Case
		else {
			successorList = currentNode.getSuccessors(); //set the successors
			for (UUSearchNode successor : successorList){ //for each successor
				if (!currentPath.contains(successor) && depth < maxDepth){ //if the current path doesn't contain this node and depth is fine
					backtrackList = (Stack<UUSearchNode>) dfsrpc(successor, currentPath, depth + 1, maxDepth); //set recursion
					if (backtrackList != null){ //if it doesn't return something null
						backtrackList.push(currentNode); //add to the path
						return backtrackList;
					}
				}
			}
		}
		return null; //should never reach here
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
