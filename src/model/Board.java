 package model;

import test.ErrorTracker;
import ui.MainFrame;

public class Board {

	public static final int EMPTY = 0;
	public static final int SELF = 2;
	public static final int ENEMY = 1;
	public static final int OUTER = 3;

	public static final int maxIndex = MainFrame.boardSize;
	public static final int minIndex = 1;
	public static final int index = maxIndex / 2;
	
	// 棋盘状态
	private static int[][] board = new int[maxIndex + 2][maxIndex + 2];
	
	public static void init() {
		for (int i = minIndex - 1; i <= maxIndex + 1; i++) {
			board[minIndex - 1][i] = OUTER;
			board[maxIndex + 1][i] = OUTER;
			board[i][minIndex - 1] = OUTER;
			board[i][maxIndex + 1] = OUTER;
		}
		for (int i = 1; i < maxIndex + 1; i++) {
			for (int j = 1; j < maxIndex + 1; j++) {
				board[i][j] = EMPTY;
			}
		}
		board[index][index] = SELF;
		board[index + 1][index + 1] = ENEMY;
	}
	
	public static void setBoard(int x, int y, int piece) {
		if(x >= 0 && x <= Board.maxIndex && y >= 0 && y <= Board.maxIndex) {
			board[x][y] = piece;
		}
		else System.out.println("设置点在棋盘外了");
		
		
		//test
		if(x == 2 && y == 3) {
			for(int i = 0;i < 5 ;i ++) {
				System.out.println(ErrorTracker.getTestMessage(i));
			}
		}
	}
	
	public static void setBoard(int[][] temp) {
		for (int i = 1; i < maxIndex + 1; i++) {
			for (int j = 1; j < maxIndex + 1; j++) {
				board[i][j] = temp[i][j];
			}
		}
	}
	
	public static int getBoard(int x, int y) {
		if(x >= 0 && x <= Board.maxIndex && y >= 0 && y <= Board.maxIndex) {
			return board[x][y];
		}
		else return Board.OUTER;
		
	}
	
	/**
	 * deep clone
	 * @return
	 */
	public static int[][] cloneBoard() {
		int[][] temp = new int[maxIndex + 2][maxIndex + 2];
		for(int i = 0;i < temp.length;i++){
            for(int j = 0;j < board[i].length;j++){
                temp[i][j] = board[i][j];
            }
        }
		return temp;
	}
	
	
	public static int[][] getBoard(){
		return board;
	}
	
	public static void display() {
		String c = "E";
		for(int i = 1; i <= maxIndex; i++) {
			System.out.print((char)(i + 'A' - 1) + " ");
			for(int j = 1; j <= maxIndex; j++) {
				if(board[j][i] == EMPTY) c = "·";
				if(board[j][i] == ENEMY) c = "●";
				if(board[j][i] == SELF) c = "○";
				System.out.print(c + " ");
			}
			System.out.println();
		}
		System.out.print("  ");
		for(int j = 0; j < maxIndex; j++) {
			System.out.printf("%1d ",j + 1);
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
}
