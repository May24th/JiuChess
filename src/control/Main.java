package control;

import eat.JumpEat;
import model.Board;

public class Main {

    public static void main(String[] args) throws Exception {
    	
//    	new MainFrame();
//    	test();
    }
    
    
    public static  void test() {
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

    	display();
	}
    
    
    public static void display() {
		String c = "E";
		for(int i = 1; i <= Board.maxIndex; i++) {
			System.out.printf("%2d ",i);
			for(int j = 1; j <= Board.maxIndex; j++) {
				if(Board.getBoard(i, j) == Board.EMPTY) c = "·";
				if(Board.getBoard(i, j) == Board.ENEMY) c = "○";
				if(Board.getBoard(i, j) == Board.SELF) c = "●";
				System.out.print(c + " ");
			}
			System.out.println();
		}
		System.out.print("   ");
		for(int j = 0; j < Board.maxIndex; j++) {
			System.out.print((char)(j + 'A') + " ");
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}
    
}
