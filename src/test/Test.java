package test;

import eat.JumpEat;
import model.Board;

public class Test {
	
	
	  public static  void test_1() {
		for (int i = 1; i <= Board.maxIndex; i++) {
			Board.setBoard(1, i, Board.SELF);
			Board.setBoard(i, 1, Board.SELF);
			Board.setBoard(Board.maxIndex, i, Board.SELF);
			Board.setBoard(i, Board.maxIndex, Board.SELF);
			if(i%2 == 1) {
				Board.setBoard(2, i, Board.SELF);
				Board.setBoard(i, 2, Board.SELF);
				Board.setBoard(Board.maxIndex - 1, i, Board.SELF);
				Board.setBoard(i, Board.maxIndex - 1, Board.SELF);
				
			}
			
		}
		Board.setBoard(7, 3, Board.SELF);
		Board.setBoard(6, 3, Board.ENEMY);
		Board.setBoard(4, 3, Board.ENEMY);
		Board.setBoard(3, 4, Board.ENEMY);
		new JumpEat(7, 3);
	}
}
