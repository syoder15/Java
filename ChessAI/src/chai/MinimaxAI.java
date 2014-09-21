package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class MinimaxAI implements ChessAI{

	boolean maximizingPlayer = true;
	int depth = 2;
	ArrayList <Integer> valueList = new ArrayList<Integer>();
	

	
@Override
public short getMove(Position position) {
	short [] moves = position.getAllMoves();
	shuffleArray(moves);
	for (short move : moves){
		try {
			position.doMove(move);
		} catch (IllegalMoveException e) {
			e.printStackTrace();
		}
		maximizingPlayer = !maximizingPlayer;
		int bestValue = minimax (position, depth, maximizingPlayer);
		valueList.add((Integer) bestValue);
		position.undoMove();
		maximizingPlayer = true;
		depth = 2;
	}
	//iterate through valueList, return index of best value
	//use that index in moves to do move
	//position.doMove(moves[])
	
	int move = 0;
	int maxValue = valueList.get(0);
	int bestValIndex = 0;
	
	for (int value : valueList){
		if (value > maxValue)
			bestValIndex = move;
		move++;
	}
	
	valueList.clear();
	return moves[bestValIndex];
}


public int minimax (Position position,
		int depth, boolean maximizingPlayer){
	if (position.isTerminal()){ //if game has ended
		//if it was AI's turn when we won
		if (position.isMate()){
			if (maximizingPlayer == true){ 
				int bestValue = 3000; //return a huge value because 
				return bestValue; //we want to make that move
			}
			else{ //if it is NOT the AI's turn
				int bestValue = -3000; //return a teeny value because
				return bestValue; //we don't want to ever make that move
			}
		}
		else
			return 0;
	}
	//depth reached but not terminal
	else if ((!position.isTerminal()) && depth == 0){
		if (maximizingPlayer == true){ //if it's AI's turn
			int bestValue = position.getMaterial();
			return bestValue; //return the state of the board
		}
		else{ //if it's not AI's turn
			int bestValue = 0 - position.getMaterial();
			return bestValue; //return state of board in AI's perspective
		}
	}
	else { //if depth isn't reached and haven't found a terminal node
		if (maximizingPlayer == true){ //AI's turn
			int bestValue = -10000; //teeny value for beginning comparison
			//for every possible move
			for (short move : position.getAllMoves()){ 
				try {
					position.doMove(move); //do the move
				} catch (IllegalMoveException e) {
					e.printStackTrace();
				}
				//create new value
				int value = minimax (position, depth - 1,
						!maximizingPlayer); 
				bestValue = max(bestValue, value); //get larger value
				position.undoMove(); //return to correct position
			}
			return bestValue; //return the best value
		}
		else {//Not AI's turn
			int bestValue = 10000; //huge number to be compared
			for (short move : position.getAllMoves()){//get possible moves
				try {
					position.doMove(move); //do the move
				} catch (IllegalMoveException e) {
					e.printStackTrace();
				}
				//create new value
				int value = minimax (position, depth - 1,
						!maximizingPlayer); 
				bestValue = min(bestValue, value);//get smaller value
				position.undoMove(); //return to correct position
			}
			return bestValue; //return best value
		}
	}
}

public int max (int bestVal, int value){
	if (bestVal > value)
		return bestVal;
	else
		return value;
}

public int min (int bestVal, int value){
	if (bestVal < value)
		return bestVal;
	else
		return value;
}

 // Implementing Fisher–Yates shuffle
  static void shuffleArray(short[] moves)
  {
    Random rnd = new Random();
    for (int i = moves.length - 1; i > 0; i--)
    {
      int index = rnd.nextInt(i + 1);
      // Simple swap
      short a = moves[index];
      moves[index] = moves[i];
      moves[i] = a;
    }
  }
	
	
}