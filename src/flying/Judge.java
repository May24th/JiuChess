package flying;

import java.util.ArrayList;

import model.Board;
import model.Point;

public class Judge {
	
	public Judge() {
		for(int i = 1; i <= Board.maxIndex - 1; i++) {
			for (int j = 1; j <= Board.maxIndex - 1; j ++) {
				int empty = 0;
				int enemy = 0;
				int self = 0;
				switch(Board.getBoard(i,j)) {
				case Board.EMPTY:empty ++;break;
				case Board.ENEMY:enemy ++;break;
				case Board.SELF:self ++;break;
				}
				switch(Board.getBoard(i + 1,j)) {
				case Board.EMPTY:empty ++;break;
				case Board.ENEMY:enemy ++;break;
				case Board.SELF:self ++;break;
				}
				switch(Board.getBoard(i,j + 1)) {
				case Board.EMPTY:empty ++;break;
				case Board.ENEMY:enemy ++;break;
				case Board.SELF:self ++;break;
				}
				switch(Board.getBoard(i + 1,j + 1)) {
				case Board.EMPTY:empty ++;break;
				case Board.ENEMY:enemy ++;break;
				case Board.SELF:self ++;break;
				}
				int stereotypes = 0;
				if(empty ==1) {
					if(self == 3) {
						if(Board.getBoard(i + 1,j) == Board.EMPTY) {
							stereotypes = 1;
						}
						if(Board.getBoard(i + 1,j + 1) == Board.EMPTY) {
							stereotypes = 2;
						}
						if(Board.getBoard(i,j) == Board.EMPTY) {
							stereotypes = 3;
						}
						if(Board.getBoard(i,j + 1) == Board.EMPTY) {
							stereotypes = 4;
						}
						Self_square.add(new Point(i,j,stereotypes));
					}
					else if(enemy == 3) {
						if(Board.getBoard(i + 1,j) == Board.EMPTY && (Board.getBoard(i + 2,j) == Board.ENEMY || Board.getBoard(i + 1,j - 1) == Board.ENEMY) ) {
							stereotypes = 1;
						}
						if(Board.getBoard(i + 1,j + 1) == Board.EMPTY && (Board.getBoard(i + 2,j + 1) == Board.ENEMY || Board.getBoard(i + 1,j  + 2) == Board.ENEMY)) {
							stereotypes = 2;
						}
						if(Board.getBoard(i,j) == Board.EMPTY && (Board.getBoard(i - 1,j) == Board.ENEMY || Board.getBoard(i,j - 1) == Board.ENEMY)) {
							stereotypes = 3;
						}
						if(Board.getBoard(i,j + 1) == Board.EMPTY && (Board.getBoard(i,j + 2) == Board.ENEMY || Board.getBoard(i - 1,j + 1) == Board.ENEMY)) {
							stereotypes = 4;
						}
						Enemy_square.add(new Point(i,j,stereotypes));
					}
				}
				else if(empty == 2 && self == 2) {
					if(Board.getBoard(i,j) == Board.EMPTY) {
						
						//(i, j)(i + 1, j)
						if(Board.getBoard(i + 1,j) == Board.EMPTY) {
							stereotypes = 4;
						}
						
						//(i, j)(i, j + 1)
						if(Board.getBoard(i,j + 1) == Board.EMPTY) {
							stereotypes = 2;
						}
						
						//(i, j)(i + 1, j + 1)
						if(Board.getBoard(i + 1,j + 1) == Board.EMPTY) {
							stereotypes = 6;
						}
					}
					else {
						
						//(i, j + 1)(i + 1, j + 1)
						if(Board.getBoard(i + 1,j) == Board.SELF) {
							stereotypes = 3;
						}
						
						//(i + 1, j)(i + 1, j + 1)
						if(Board.getBoard(i,j + 1) == Board.SELF) {
							stereotypes = 1;
						}
						
						//(i, j + 1)(i + 1, j)
						if(Board.getBoard(i + 1,j + 1) == Board.SELF) {
							stereotypes = 5;
						}
					}
					Self_door.add(new Point(i,j,stereotypes));
				}
				
				
				
			}
		}
	}
	
	private ArrayList<Point> Self_square = new ArrayList<Point>();
	private ArrayList<Point> Enemy_square = new ArrayList<Point>();
	private ArrayList<Point> Self_door = new ArrayList<Point>();
	
	public ArrayList<Point> getSelf_square(){
		return Self_square;
	}
	
	public ArrayList<Point> getEnemy_square(){
		return Enemy_square;
	}
	
	public ArrayList<Point> getSelf_door(){
		return Self_door;
	}
	
}
