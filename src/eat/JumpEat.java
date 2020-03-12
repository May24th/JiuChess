package eat;

import model.Board;
import model.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/***
 * 对于一个点的可跳吃的分析
 * @author May
 *
 */
public class JumpEat {
	
	
	//方向，不能乱改，会用到- 1，-2
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int MOVE = 5;
	
	public JumpEat(int x, int y) {  //初始化
		this.x = x;
		this.y = y;
		maxScore = 0;
		bestEat.clear();
		best.clear();
		setBestEat();
	}
	
	private int x;
	private int y;
	/**二维数组，储存最好的吃法*/
	private ArrayList<ArrayList<Integer>> bestEat= new ArrayList<ArrayList<Integer>>();	
	/**随机的一种吃法*/
	private ArrayList<Integer> best = new ArrayList<Integer>();
	private int maxScore;
	
	private void eat_board(int x, int y, int direct) {
		switch(direct) {
		case UP:Board.setBoard(x + 1, y, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x + 2, y, Board.SELF);
				break;
		case DOWN:Board.setBoard(x - 1, y, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x - 2, y, Board.SELF);
				break;
		case RIGHT:Board.setBoard(x, y + 1, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y + 2, Board.SELF);
				break;
		case LEFT:Board.setBoard(x, y - 1, Board.EMPTY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y - 2, Board.SELF);
				break;
		}

	} 
	
	/***
	 * 回到过去
	 * @param x	现横坐标
	 * @param y	现纵坐标
	 * @param direct	原方向
	 */
	private void reeat_board(int x, int y, int direct) {
		switch(direct) {
		case DOWN:Board.setBoard(x + 1, y, Board.ENEMY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x + 2, y, Board.SELF);
				break;
		case UP:Board.setBoard(x - 1, y, Board.ENEMY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x - 2, y, Board.SELF);
				break;
		case LEFT:Board.setBoard(x, y + 1, Board.ENEMY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y + 2, Board.SELF);
				break;
		case RIGHT:Board.setBoard(x, y - 1, Board.ENEMY);
				Board.setBoard(x, y, Board.EMPTY);
				Board.setBoard(x, y - 2, Board.SELF);
				break;
		}
	} 
	
	/***
	 * 
	 * @param x 原横坐标
	 * @param y 原纵坐标
	 * @return 
	 */
	private int eatEnemyScore(int x,int y,int direction) {	
		switch(direction) {
		case JumpEat.UP: return EnemyScore.getSingleScore(Board.getBoard(), x + 1, y);
		case JumpEat.DOWN: return EnemyScore.getSingleScore(Board.getBoard(), x - 1, y);
		case JumpEat.RIGHT: return EnemyScore.getSingleScore(Board.getBoard(), x, y + 1);
		case JumpEat.LEFT: return EnemyScore.getSingleScore(Board.getBoard(), x, y - 1);
		}
		return EnemyScore.getSingleScore(Board.getBoard(), x, y);
	}
	
	/***
	 * 
	 * @param x		当前横坐标
	 * @param y		当前纵坐标
	 * @param preDirect
	 * @param presentScore
	 * @return	当前数组指向，若无添加，则返回-1
	 */
	private int eat(int x,int y,int preDirect,int presentScore,int tempHold) {
		int flag = 0;	//判断是否可跳吃
		int preIt = -1;
		/**前一个方向的分数*/
		int premax = -1;
		
		if(Board.getBoard(x + 1,y) == Board.ENEMY && Board.getBoard(x + 2,y) == Board.EMPTY) {
			int temp = presentScore + eatEnemyScore(x,y,UP) - tempHold + (new fangAdvice(x + 2, y)).getFangEatScore();
			eat_board(x,y,UP);
			int next = SelfScore.getSelfScore(x + 2, y);
			temp += next;
			int a = eat(x + 2,y,UP,temp,next);
			
			if(a != -1) {
				preIt = a;
				premax = maxScore;
			}
			
			reeat_board(x + 2,y,UP);
			flag = 1;
		}
		if(Board.getBoard(x - 1,y) == Board.ENEMY && Board.getBoard(x - 2,y) == Board.EMPTY) {
			int temp = presentScore + eatEnemyScore(x,y,DOWN) - tempHold + (new fangAdvice(x - 2, y)).getFangEatScore();
			eat_board(x,y,DOWN);
			int next = SelfScore.getSelfScore(x - 2, y);
			temp += next;
			int a = eat(x - 2,y,DOWN,temp,next);
			
			if(a != -1) {
				if(premax == maxScore) {
					//此时的情况为两个最优解分支的汇合点，把前一个的末尾指向现在的末尾
					bestEat.get(preIt).add(-1);
					bestEat.get(preIt).add(a);
					bestEat.get(preIt).add(bestEat.get(a).size());
					preIt = a;
				}
				else {
					//比前一种方向的最优解要好
					preIt = a;
					premax = maxScore; 
				}
			}
			
			reeat_board(x - 2,y,DOWN);
			flag = 1;
		}
		if(Board.getBoard(x,y + 1) == Board.ENEMY && Board.getBoard(x,y + 2) == Board.EMPTY) {
			int temp = presentScore + eatEnemyScore(x,y,RIGHT) - tempHold + (new fangAdvice(x, y + 2)).getFangEatScore();
			eat_board(x,y,RIGHT);
			int next = SelfScore.getSelfScore(x, y + 2);
			temp += next;
			int a = eat(x,y + 2,RIGHT,temp,next);
			
			if(a != -1) {
				if(premax == maxScore) {
					bestEat.get(preIt).add(-1);
					bestEat.get(preIt).add(a);
					bestEat.get(preIt).add(bestEat.get(a).size());
					preIt = a;
				}
				else {
					preIt = a;
					premax = maxScore; 
				}
			}
			
			reeat_board(x,y + 2,RIGHT);
			flag = 1;
		}
		if(Board.getBoard(x,y - 1) == Board.ENEMY && Board.getBoard(x,y - 2) == Board.EMPTY) {
			int temp = presentScore + eatEnemyScore(x,y,LEFT) - tempHold + (new fangAdvice(x, y - 2)).getFangEatScore();
			eat_board(x,y,LEFT);
			int next = SelfScore.getSelfScore(x, y - 2);
			temp += next;
			int a = eat(x,y - 2,LEFT,temp,next);
			
			if(a != -1) {
				if(premax == maxScore) {
					bestEat.get(preIt).add(-1);
					bestEat.get(preIt).add(a);
					bestEat.get(preIt).add(bestEat.get(a).size());
					preIt = a;
				}
				else {
					preIt = a;
					premax = maxScore; 
				}
			}
			
			reeat_board(x,y - 2,LEFT);
			flag = 1;
		}
		
		
		if(flag == 0) {
			//如果不能跳吃
			if (maxScore < presentScore) {
				//找到更好的点了，即当前点是最好的点
				maxScore = presentScore;
				bestEat.clear();
				bestEat.add(new ArrayList<Integer>());
				bestEat.get(0).add(preDirect);
				return 0;
			}
			
			else if((maxScore == presentScore)&&(maxScore > 0)) {
				//当前点也是一种最优解之一
				bestEat.add(new ArrayList<Integer>());
				int size = bestEat.size() - 1;
				bestEat.get(size).add(preDirect);
				return size;
			}
			else return -1;
		}
		else {
			//如果能跳吃
			if (maxScore < presentScore) {
				//当前即最优解（没有之一），跳吃无意义
				maxScore = presentScore;
				bestEat.clear();
				bestEat.add(new ArrayList<Integer>());
				bestEat.get(0).add(preDirect);
				return 0;
			}
			else if(maxScore == presentScore) {
				if(maxScore == premax) {
					//当前即最优解之一，跳吃其中一项和当前分数相同，爱吃不吃
					bestEat.add(new ArrayList<Integer>());
					int size = bestEat.size() - 1;
					bestEat.get(preIt).add(-1);
					bestEat.get(preIt).add(size);
					bestEat.get(preIt).add(bestEat.get(size).size());
					bestEat.get(size).add(preDirect);
					return size;
				}
				else {
					//premax < maxscore 能跳吃，但跳吃分数太低，导致premax没被赋值，此时此节点不错
					bestEat.add(new ArrayList<Integer>());
					int size = bestEat.size() - 1;
					bestEat.get(size).add(preDirect);
					return size;
				}
			}
			else {
				//当前不是最优解
				if(preIt == -1) {
					return -1;
				}
				else {
					//是最优解中的中间部分，做常规记录
					bestEat.get(preIt).add(preDirect);
					return preIt;
				}
			}
		}
	}

	private class fangAdvice{
		public fangAdvice(int tempx, int tempy) {
			int add = 0;
			KmaxScore = 0;
			if(Board.getBoard(tempx + 1,tempy) == Board.SELF && Board.getBoard(tempx + 1,tempy + 1) == Board.SELF && Board.getBoard(tempx,tempy + 1) == Board.SELF) add++;
			if(Board.getBoard(tempx - 1,tempy) == Board.SELF && Board.getBoard(tempx - 1,tempy - 1) == Board.SELF && Board.getBoard(tempx,tempy - 1) == Board.SELF) add++;
			if(Board.getBoard(tempx,tempy + 1) == Board.SELF && Board.getBoard(tempx - 1,tempy + 1) == Board.SELF && Board.getBoard(tempx - 1,tempy) == Board.SELF) add++;
			if(Board.getBoard(tempx,tempy - 1) == Board.SELF && Board.getBoard(tempx + 1,tempy - 1) == Board.SELF && Board.getBoard(tempx + 1,tempy) == Board.SELF) add++;
			
			/**成方了*/
			for(int i = 0; i < add; i++) {
				int maxScore_temp = 0;
				Point maxPoint = new Point(-1,-1);
				for(int o = 1;o <= Board.maxIndex; o ++) {
					for(int p = 1; p <= Board.maxIndex; p++) {
						if(Board.getBoard(o, p) == Board.ENEMY) {
							int temp = EnemyScore.getSingleScore(Board.getBoard(), o, p);
							if(temp > maxScore_temp) {
								maxScore_temp = temp;
								maxPoint = new Point(o,p);
							}
						}
					}
				}
				KmaxScore += maxScore_temp;												//2020.2.25
				fangEatPoint.add(maxPoint);
				Board.setBoard(maxPoint.x, maxPoint.y, Board.EMPTY);
			}
			
			for(Point a : fangEatPoint) {
				Board.setBoard(a.x, a.y, Board.ENEMY);
			}
		}
		
		private int KmaxScore;
		private ArrayList<Point> fangEatPoint = new ArrayList<Point>();
		
		public int getFangEatScore() {
			return KmaxScore;
		}
	}
	
	private void setBestEat() {
		int next = SelfScore.getSelfScore(x, y);
		eat(x,y,-2,0,next);
		Random r=new Random();
		int i;
		
		if(!bestEat.isEmpty()) i = r.nextInt(bestEat.size());
		else return;
		int j = 0;
		while(bestEat.get(i).get(j) != -2)
		{
			if(bestEat.get(i).get(j) != -1) {
				best.add(bestEat.get(i).get(j));
				j++;
			}
			else {
				int o = i;
				i = bestEat.get(o).get(j + 1);
				j = bestEat.get(o).get(j + 2);
			}
//			//检查BUG
//			try {
//				bestEat.get(i).get(j);
//			}
//			catch (IndexOutOfBoundsException e) {
//				System.out.println("i = " + i + ",j = " + j);
//				System.out.println();
//				Board.display();
//				for(ArrayList<Integer> x: bestEat) {
//					for(Integer y : x) {
//						System.out.print(y);
//					}
//					System.out.println();
//				}
//			}
		}
		Collections.reverse(best);
	}
	
	public ArrayList<Integer> getBesteat() {
		return best;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
}
