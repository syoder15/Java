package chai;


import java.util.ArrayList;
import java.util.Random;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;


public class AlphaBetaAI implements ChessAI {

	ArrayList<Integer> valueList = new ArrayList<Integer>();
	public int nodesExplored = 0;
	public int maxDepth;
	public int tempDepth;
	
	///*
	public AlphaBetaAI(int depth){
		maxDepth = depth;
		tempDepth = depth;
	}
	//*/
		
	
public short getMove(Position position) {
	//globals
	boolean timeLeft = true;
	short [] moves = null;
	int bestMove = 0;
	
	for (int tempDepth = 1; tempDepth < maxDepth &&
			timeLeft; tempDepth++){ //iterative D search until time is up
								//or depth is reached
	
		moves = position.getAllMoves();
		shuffleArray(moves); //shuffle eliminating repeated moves
		
		for(short move : moves){ //for each of possible moves
			try {
				position.doMove(move); //do the move
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
			//add value of move to array
			valueList.add(alphaBeta(position, tempDepth,
					Integer.MIN_VALUE, Integer.MAX_VALUE, false));
			position.undoMove(); //return to actual position
		}
		
		int move = 0;
		int maxVal = valueList.get(0);
		bestMove = 0;
		
		//iterate through, find best value
		for(int value : valueList){
			if (value > maxVal) bestMove = move;
			move++;
		}
		
		tempDepth = maxDepth;
		valueList.clear();
	}
	
	//best value index in arraylist directly relates to best move index
	return moves[bestMove]; //do it
	
}
	
	
//Use Wikipedia Pseudo-Code
//Split the terminal and the depth reached though
	//because act differently depending on that
int alphaBeta(Position position, int depth, int alpha,
		int beta, boolean maximizingPlayer){
	if (position.isTerminal()){ //if it's end of game
		if (position.isMate()){ //check mate
			//AI made move, we won (return best possible value to ensure
			//move is made)
			if (maximizingPlayer) return Integer.MIN_VALUE;
			//otherwise we lost (return worst possible value)
			else return Integer.MAX_VALUE;
		}
		else return 0; //draw if it isn't a checkmate
	}
		
	if (depth == 0){ //if we reach depth
		//use getMaterial to return a value for the board
		if (maximizingPlayer) return position.getMaterial();
		else return  -1 * position.getMaterial();
	}
		
	short [] moves = position.getAllMoves();
	
	if (maximizingPlayer){ //alphabeta if it's AI's turn (maximizing
	    for (short move : moves){
		   	try {
				position.doMove(move);
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		       alpha = Math.max(alpha, alphaBeta(position, depth - 1,
		    		   alpha, beta, false));
		       position.undoMove();
		       if (beta <= alpha){
		    	   break; // beta cut-off
		       }
		  }
		  return alpha; //return max
	}
	else{ //alpha beta for opponents turn (minimizing)
	    for (short move : moves){
	    	try {
				position.doMove(move);
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
	        beta = Math.min(beta, alphaBeta(position, depth - 1,
	        		alpha, beta, true));
	        position.undoMove();
	        if (beta <= alpha){
	        	break; // alpha cut-off
	        }
	    }
	  return beta; //return min
	  }
	}


	//Fisher-Yates
	  static void shuffleArray(short[] ar){
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--){
	      int index = rnd.nextInt(i + 1);
	      short a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
}