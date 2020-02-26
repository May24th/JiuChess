package eat;

import model.Board;

public class SelfScore {
		
	private static final int allpow = 8;
	private static final int movepow = 0;
	private static final int eatpow = 0;
	private static final int fangpow = 16;
	private static final int defangpow = 8;
	private static final int eatenpow = -4;
	
	/**
	 * 对已经存在在棋盘上的己方子进行评估
	 * （若想改，须对eatenable方法进行修改）
	 * */
	public static int getSelfScore(int x, int y) {
		return (movepow * moveable(x,y) + eatpow * eatable(x,y) + fangpow * fangable(x,y) + defangpow * defangable(x,y) + eatenpow * eatenable(x,y)) * allpow;
	}
	public static int getSelfHoldScore(int x, int y) {
		return (movepow * moveable(x,y) + eatpow * eatable(x,y)  + defangpow * defangable(x,y) + eatenpow * eatenable(x,y)) * allpow;
	}
	
	private static int moveable(int x,int y) {
		int add = 0;
		if(Board.getBoard(x + 1,y) == Board.EMPTY) add++;
		if(Board.getBoard(x - 1,y) == Board.EMPTY) add++;
		if(Board.getBoard(x,y + 1) == Board.EMPTY) add++;
		if(Board.getBoard(x,y - 1) == Board.EMPTY) add++;
		return add;
	}
	
	private static int eatable(int x,int y) {
		int add = 0;
		if(Board.getBoard(x + 1,y) == Board.ENEMY && Board.getBoard(x + 2,y) == Board.EMPTY) add++;
		if(Board.getBoard(x - 1,y) == Board.ENEMY && Board.getBoard(x - 2,y) == Board.EMPTY) add++;
		if(Board.getBoard(x,y + 1) == Board.ENEMY && Board.getBoard(x,y + 2) == Board.EMPTY) add++;
		if(Board.getBoard(x,y - 1) == Board.ENEMY && Board.getBoard(x,y - 2) == Board.EMPTY) add++;
		return add;
	}
	private static int fangable(int x,int y) {
		int add = 0;
		if(Board.getBoard(x + 1,y) == Board.SELF && Board.getBoard(x + 1,y + 1) == Board.SELF && Board.getBoard(x,y + 1) == Board.SELF) add++;
		if(Board.getBoard(x - 1,y) == Board.SELF && Board.getBoard(x - 1,y - 1) == Board.SELF && Board.getBoard(x,y - 1) == Board.SELF) add++;
		if(Board.getBoard(x,y + 1) == Board.SELF && Board.getBoard(x - 1,y + 1) == Board.SELF && Board.getBoard(x - 1,y) == Board.SELF) add++;
		if(Board.getBoard(x,y - 1) == Board.SELF && Board.getBoard(x + 1,y - 1) == Board.SELF && Board.getBoard(x + 1,y) == Board.SELF) add++;
		return add;
	}
	private static int defangable(int x,int y) {
		int add = 0;
		if(Board.getBoard(x + 1,y) == Board.ENEMY && Board.getBoard(x + 1,y + 1) == Board.ENEMY && Board.getBoard(x,y + 1) == Board.ENEMY) add++;
		if(Board.getBoard(x - 1,y) == Board.ENEMY && Board.getBoard(x - 1,y - 1) == Board.ENEMY && Board.getBoard(x,y - 1) == Board.ENEMY) add++;
		if(Board.getBoard(x,y + 1) == Board.ENEMY && Board.getBoard(x - 1,y + 1) == Board.ENEMY && Board.getBoard(x - 1,y) == Board.ENEMY) add++;
		if(Board.getBoard(x,y - 1) == Board.ENEMY && Board.getBoard(x + 1,y - 1) == Board.ENEMY && Board.getBoard(x + 1,y) == Board.ENEMY) add++;
		return add;
	}
	
	/***
	 * 
	 * @param x
	 * @param y
	 * @return	是否可被吃
	 */
	private static int eatenable(int x,int y) {
		if(Board.getBoard(x + 1,y) == Board.ENEMY) {
			if(Board.getBoard(x - 1,y) == Board.EMPTY) {
				return 1;
			}
		}
		else if(Board.getBoard(x + 1,y) == Board.EMPTY) {
			if(Board.getBoard(x - 1,y) == Board.ENEMY) {
			return 1;
			}
			else if(Board.getBoard(x - 1,y) == Board.EMPTY) {
				//左右两个点都是空的情况
				//两个点都做一下反吃子分析
				int a = 0;
				Board.setBoard(x, y, Board.EMPTY);
				
				Board.setBoard(x + 1, y, Board.ENEMY);
				a = abEat(x + 1, y);
				Board.setBoard(x + 1, y, Board.EMPTY);
				
				Board.setBoard(x - 1, y, Board.ENEMY);
				a += abEat(x - 1, y);
				Board.setBoard(x - 1, y, Board.EMPTY);
				
				Board.setBoard(x, y, Board.SELF);
				if(a > 0)return 1;
			}
		}
		
		if(Board.getBoard(x,y + 1) == Board.ENEMY) {
			if(Board.getBoard(x,y - 1) == Board.EMPTY) {
				return 1;
			}
		}
		else if(Board.getBoard(x,y + 1) == Board.EMPTY) {
			if(Board.getBoard(x,y - 1) == Board.ENEMY) {
			return 1;
			}
			else if(Board.getBoard(x,y - 1) == Board.EMPTY) {
				//两个点都做一下反吃子分析
				int a = 0;
				Board.setBoard(x, y, Board.EMPTY);
				
				Board.setBoard(x, y + 1, Board.ENEMY);
				a = abEat(x, y + 1);
				Board.setBoard(x, y + 1, Board.EMPTY);
				
				Board.setBoard(x, y - 1, Board.ENEMY);
				a += abEat(x, y - 1);
				Board.setBoard(x, y - 1, Board.EMPTY);
				
				Board.setBoard(x, y, Board.SELF);
				if(a > 0)return 1;
			}
		}
		
		return 0;
	}
	
	private static int abEat(int x,int y){
		if(Board.getBoard(x + 1,y) == Board.SELF) {
			if(Board.getBoard(x + 2,y) == Board.ENEMY) {
				return 1;
			}
			else if(Board.getBoard(x + 2,y) == Board.EMPTY) {
				eat_board(x, y, JumpEat.UP);
				int a = abEat(x + 2, y);
				reeat_board(x + 2, y, JumpEat.UP);
				if(a == 1) return 1;
			}
		}
		if(Board.getBoard(x - 1,y) == Board.SELF) {
			if(Board.getBoard(x - 2,y) == Board.ENEMY) {
				return 1;
			}
			else if(Board.getBoard(x - 2,y) == Board.EMPTY) {
				eat_board(x, y, JumpEat.DOWN);
				int a = abEat(x - 2, y);
				reeat_board(x - 2, y, JumpEat.DOWN);
				if(a == 1) return 1;
			}
		}
		if(Board.getBoard(x,y + 1) == Board.SELF) {
			if(Board.getBoard(x,y + 2) == Board.ENEMY) {
				return 1;
			}
			else if(Board.getBoard(x,y + 2) == Board.EMPTY) {
				eat_board(x, y, JumpEat.RIGHT);
				int a = abEat(x, y + 2);
				reeat_board(x, y + 2, JumpEat.RIGHT);
				if(a == 1) return 1;
			}
		}
		if(Board.getBoard(x,y - 1) == Board.SELF) {
			if(Board.getBoard(x,y - 2) == Board.ENEMY) {
				return 1;
			}
			else if(Board.getBoard(x,y - 2) == Board.EMPTY) {
				eat_board(x, y, JumpEat.LEFT);
				int a = abEat(x, y - 2);
				reeat_board(x, y - 2, JumpEat.LEFT);
				if(a == 1) return 1;
			}
		}
		return 0;
	}
	
	//对黑子做分析
	private static void eat_board(int x, int y, int direct) {
		switch(direct) {
		case JumpEat.UP:Board.setBoard(x + 1, y, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x + 2, y, Board.ENEMY);
				break;
		case JumpEat.DOWN:Board.setBoard(x - 1, y, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x - 2, y, Board.ENEMY);
				break;
		case JumpEat.RIGHT:Board.setBoard(x, y + 1, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y + 2, Board.ENEMY);
				break;
		case JumpEat.LEFT:Board.setBoard(x, y - 1, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y - 2, Board.ENEMY);
				break;
		}

	} 
	
	/***
	 * 回到过去
	 * @param x	现横坐标
	 * @param y	现纵坐标
	 * @param direct	原方向
	 */
	private static void reeat_board(int x, int y, int direct) {
		switch(direct) {
		case JumpEat.DOWN:Board.setBoard(x + 1, y, Board.SELF);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x + 2, y, Board.ENEMY);
				break;
		case JumpEat.UP:Board.setBoard(x - 1, y, Board.SELF);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x - 2, y, Board.ENEMY);
				break;
		case JumpEat.LEFT:Board.setBoard(x, y + 1, Board.SELF);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y + 2, Board.ENEMY);
				break;
		case JumpEat.RIGHT:Board.setBoard(x, y - 1, Board.SELF);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y - 2, Board.ENEMY);
				break;
		}
	} 
	
}

