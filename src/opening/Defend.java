package opening;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import back.Version;
import model.Board;
import model.Point;

public class Defend {
	private static int[][] square = new int[Board.maxIndex + 2][Board.maxIndex + 2];
//	/**需要增加的分数*/
//	private static int[][] addition = new int[Board.maxIndex + 2][Board.maxIndex + 2];
	/**需要增加值的点*/
	private static Stack<ArrayList<Point>> defenseAddition = new Stack<ArrayList<Point>>();
	public static void init() {
		square[Board.index][Board.index + 1] = Open.defenseBase;
		square[Board.index + 1][Board.index + 1] = Open.defenseBase;
		square[Board.index + 1][Board.index] = Open.defenseBase;
	}
	
	
	/***
	 * 得到防御效果最好的那一批点
	 * 
	 * @return
	 */
	public static ArrayList<Point> getBestDefensePoints() {
		int max = 0;
		ArrayList<Point> bestPoints = new ArrayList<Point>();
		
		int[][] addition = new int[Board.maxIndex + 2][Board.maxIndex + 2];
		for(ArrayList<Point> arr : defenseAddition) {
			for(Point p : arr) {
				addition[p.x][p.y] ++;
			}
		}
		
		
		for (int i = Board.maxIndex; i >= Board.minIndex; i--) {
			for (int j = Board.minIndex; j <= Board.maxIndex; j++) {
				int temp = getDefenseScore(i, j);
				if (temp > 0) {
					temp += addition[i][j] * 50;
					if (temp > max) {
						max = temp;
						bestPoints.clear();
						bestPoints.add(new Point(i, j, temp));
					} else if (temp == max) {
						bestPoints.add(new Point(i, j, temp));
					}
				}
			}
		}
		return bestPoints;
	}
	
//	public ArrayList<Point> getBetterDefensePoints() {
//	int max = 0;
////	PriorityQueue<Point> subPoints = new PriorityQueue<Point>();
////	for(int i = maxIndex; i >= minIndex; i--) {
////		for(int j = minIndex; j <= maxIndex; j++) {
////			int temp = getDefenseScore(i,j);
////			if(temp > 0)
////			{
////				if(temp > max) {
////					if(temp*acceptableRate/100 > max) subPoints.clear();
////					max = temp;
////				}
////			subPoints.add(new Point(i,j,temp));
////			}
////		}
////	}
////	max = max*acceptableRate/100;
////	Point a = new Point(0,0,0);
////	ArrayList<Point> betterPoints = new ArrayList<Point>();
////	while((a = subPoints.poll()).getScore() >= max ){		//在可接受范围内
////		betterPoints.add(a);
////	}
//	return betterPoints;
//}

	/***
	 * 防御评估模型的实现，对于每次落子，都通过此方法去更新每一个方的防御评分值。
	 * 
	 * @param x     落子点横坐标
	 * @param y     落子点纵坐标
	 * @param piece 落子点的状态 ENEMY SELF
	 */

	public static void updateDefenseScore(int x, int y, int piece) {
		if (piece == Board.SELF) {
			square[x - 1][y - 1] = 0;
			square[x - 1][y] = 0;
			square[x][y - 1] = 0;
			square[x][y] = 0;
		} else if (piece == Board.ENEMY) {
			// 分别看8个子
			if (Board.getBoard(x - 1, y) == Board.SELF) { // 下子异
				square[x - 1][y - 1] = 0;
				square[x - 1][y] = 0;
				if (Board.getBoard(x + 1, y) == Board.SELF) { // 上子异
					square[x][y - 1] = 0;
					square[x][y] = 0;
					return;
				} else { // 上子同
					if (Board.getBoard(x, y - 1) == Board.SELF || Board.getBoard(x + 1, y - 1) == Board.SELF)
						square[x][y - 1] = 0;
					else
						square[x][y - 1] += Open.defenseBase;
					if (Board.getBoard(x, y + 1) == Board.SELF || Board.getBoard(x + 1, y + 1) == Board.SELF)
						square[x][y] = 0;
					else
						square[x][y] += Open.defenseBase;
				}
			} else { // 下子同
				if (Board.getBoard(x, y - 1) == Board.SELF) { // 左子异
					square[x][y - 1] = 0;
					square[x - 1][y - 1] = 0;
					if (Board.getBoard(x, y + 1) == Board.SELF) { // 右子异
						square[x - 1][y] = 0;
						square[x][y] = 0;
					} else { // 右子同
						if (Board.getBoard(x + 1, y + 1) == Board.SELF
								|| Board.getBoard(x + 1, y) == Board.SELF)
							square[x][y] = 0;
						else
							square[x][y] += Open.defenseBase;
						if (Board.getBoard(x - 1, y + 1) == Board.SELF)
							square[x - 1][y + 1] = 0;
						else
							square[x - 1][y + 1] += Open.defenseBase;
					}
				} else { // 左子同
					if (Board.getBoard(x + 1, y) == Board.SELF) { // 上子异
						square[x][y - 1] = 0;
						square[x][y] = 0;
						if (Board.getBoard(x - 1, y - 1) == Board.SELF)
							square[x - 1][y - 1] = 0;
						else
							square[x - 1][y - 1] += Open.defenseBase;
						if (Board.getBoard(x, y + 1) == Board.SELF
								|| Board.getBoard(x - 1, y + 1) == Board.SELF)
							square[x - 1][y] = 0;
						else
							square[x - 1][y] += Open.defenseBase;
					} else { // 上子同
						if (Board.getBoard(x, y + 1) == Board.SELF) {// 右子异
							square[x][y] = 0;
							square[x - 1][y] = 0;
							if (Board.getBoard(x - 1, y - 1) == Board.SELF)
								square[x - 1][y - 1] = 0;
							else
								square[x - 1][y - 1] += Open.defenseBase;
							if (Board.getBoard(x + 1, y - 1) == Board.SELF)
								square[x][y - 1] = 0;
							else
								square[x][y - 1] += Open.defenseBase;
						} else { // 右子同
							if (Board.getBoard(x - 1, y - 1) == Board.SELF)
								square[x - 1][y - 1] = 0;
							else
								square[x - 1][y - 1] += Open.defenseBase;
							if (Board.getBoard(x + 1, y - 1) == Board.SELF)
								square[x][y - 1] = 0;
							else
								square[x][y - 1] += Open.defenseBase;
							if (Board.getBoard(x - 1, y + 1) == Board.SELF)
								square[x - 1][y] = 0;
							else
								square[x - 1][y] += Open.defenseBase;
							if (Board.getBoard(x + 1, y + 1) == Board.SELF)
								square[x][y] = 0;
							else
								square[x][y] += Open.defenseBase;
						}
					}
				}
			}
		}
	}
	
	public static void twoCheck(int x, int y, int piece) {
		if (piece == Board.ENEMY) {
			// 下方检测
			if (Board.getBoard(x - 1, y) == Board.ENEMY && Board.getBoard(x, y - 1) != Board.OUTER
					&& Board.getBoard(x, y + 1) != Board.OUTER) {
				if (((Board.getBoard(x + 1, y) != Board.SELF && Board.getBoard(x + 1, y) != Board.OUTER)
						|| (Board.getBoard(x - 2, y) != Board.SELF
								&& Board.getBoard(x - 2, y) != Board.OUTER))
						&& Board.getBoard(x - 1, y - 1) != Board.SELF
						&& Board.getBoard(x - 1, y + 1) != Board.SELF
						&& Board.getBoard(x, y - 1) != Board.SELF 
						&& Board.getBoard(x, y + 1) != Board.SELF) {
					ArrayList<Point> toDefend = new ArrayList<Point>();
					if (Board.getBoard(x - 1, y - 1) == Board.EMPTY)
						toDefend.add(new Point(x - 1, y - 1));
					if (Board.getBoard(x - 1, y + 1) == Board.EMPTY)
						toDefend.add(new Point(x - 1, y + 1));
					if (Board.getBoard(x, y - 1) == Board.EMPTY)
						toDefend.add(new Point(x, y - 1));
					if (Board.getBoard(x, y + 1) == Board.EMPTY)
						toDefend.add(new Point(x, y + 1));
					if (!toDefend.isEmpty())
						defenseAddition.push(toDefend);
				}
			}
			// 上方检测
			if (Board.getBoard(x + 1, y) == Board.ENEMY && Board.getBoard(x, y - 1) != Board.OUTER
					&& Board.getBoard(x, y + 1) != Board.OUTER) {
				if (((Board.getBoard(x + 2, y) != Board.SELF && Board.getBoard(x + 2, y) != Board.OUTER)
						|| (Board.getBoard(x - 1, y) != Board.SELF
								&& Board.getBoard(x - 1, y) != Board.OUTER))
						&& Board.getBoard(x + 1, y - 1) != Board.SELF
						&& Board.getBoard(x + 1, y + 1) != Board.SELF
						&& Board.getBoard(x, y - 1) != Board.SELF 
						&& Board.getBoard(x, y + 1) != Board.SELF) {
					ArrayList<Point> toDefend = new ArrayList<Point>();
					if (Board.getBoard(x + 1, y - 1) == Board.EMPTY)
						toDefend.add(new Point(x + 1, y - 1));
					if (Board.getBoard(x + 1, y + 1) == Board.EMPTY)
						toDefend.add(new Point(x + 1, y + 1));
					if (Board.getBoard(x, y - 1) == Board.EMPTY)
						toDefend.add(new Point(x, y - 1));
					if (Board.getBoard(x, y + 1) == Board.EMPTY)
						toDefend.add(new Point(x, y + 1));
					if (!toDefend.isEmpty())
						defenseAddition.push(toDefend);
				}
			}
			// 左方检测
			if (Board.getBoard(x, y - 1) == Board.ENEMY && Board.getBoard(x - 1, y) != Board.OUTER
					&& Board.getBoard(x + 1, y) != Board.OUTER) {
				if (((Board.getBoard(x, y - 2) != Board.SELF && Board.getBoard(x, y - 2) != Board.OUTER)
						|| (Board.getBoard(x, y + 1) != Board.SELF
								&& Board.getBoard(x, y + 1) != Board.OUTER))
						&& Board.getBoard(x + 1, y - 1) != Board.SELF
						&& Board.getBoard(x + 1, y) != Board.SELF
						&& Board.getBoard(x - 1, y - 1) != Board.SELF
						&& Board.getBoard(x - 1, y) != Board.SELF) {
					ArrayList<Point> toDefend = new ArrayList<Point>();
					if (Board.getBoard(x + 1, y - 1) == Board.EMPTY)
						toDefend.add(new Point(x + 1, y - 1));
					if (Board.getBoard(x + 1, y) == Board.EMPTY)
						toDefend.add(new Point(x + 1, y));
					if (Board.getBoard(x - 1, y - 1) == Board.EMPTY)
						toDefend.add(new Point(x - 1, y - 1));
					if (Board.getBoard(x - 1, y) == Board.EMPTY)
						toDefend.add(new Point(x - 1, y));
					if (!toDefend.isEmpty())
						defenseAddition.push(toDefend);
				}
			}
			// 右方检测
			if (Board.getBoard(x, y + 1) == Board.ENEMY && Board.getBoard(x - 1, y) != Board.OUTER
					&& Board.getBoard(x + 1, y) != Board.OUTER) {
				if (((Board.getBoard(x, y + 2) != Board.SELF && Board.getBoard(x, y + 2) != Board.OUTER)
						|| (Board.getBoard(x, y - 1) != Board.SELF
								&& Board.getBoard(x, y - 1) != Board.OUTER))
						&& Board.getBoard(x + 1, y + 1) != Board.SELF
						&& Board.getBoard(x + 1, y) != Board.SELF
						&& Board.getBoard(x - 1, y + 1) != Board.SELF
						&& Board.getBoard(x - 1, y) != Board.SELF) {
					ArrayList<Point> toDefend = new ArrayList<Point>();
					if (Board.getBoard(x + 1, y + 1) == Board.EMPTY)
						toDefend.add(new Point(x + 1, y + 1));
					if (Board.getBoard(x + 1, y) == Board.EMPTY)
						toDefend.add(new Point(x + 1, y));
					if (Board.getBoard(x - 1, y + 1) == Board.EMPTY)
						toDefend.add(new Point(x - 1, y + 1));
					if (Board.getBoard(x - 1, y) == Board.EMPTY)
						toDefend.add(new Point(x - 1, y));
					if (!toDefend.isEmpty())
						defenseAddition.push(toDefend);
				}
			}
			
		} else {	//落子為己方
			List<ArrayList<Point>> toDelete = new ArrayList<ArrayList<Point>>();
			for(ArrayList<Point> arr : defenseAddition) {
				for(Point p : arr) {
					if(p.x == x && p.y == y) {
						toDelete.add(arr);
						break;
					}
				}
			}
			defenseAddition.removeAll(toDelete);
		}
		
	}
	
	/***
	 * 针对于防御评估模型，获取对该点的防御评分，评分越高，越急需防御
	 * 
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return score|该点落子的得分
	 */
	static int getDefenseScore(int x, int y) {
		int score = 0;
		if (Board.getBoard(x, y) != Board.EMPTY)
			return -1;
		if (x - 1 > 0) {
			if (y - 1 > 0)
				score += square[x - 1][y - 1]; // 左下
			if (y < Board.maxIndex)
				score += square[x - 1][y]; // 右下
		}
		if (x < Board.maxIndex) {
			if (y - 1 > 0)
				score += square[x][y - 1]; // 左上
			if (y < Board.maxIndex)
				score += square[x][y]; // 右上
		}
		return score * score;
	}

	public static int[][] getSquare(){
		int[][] T = new int[Board.maxIndex + 2][Board.maxIndex + 2];
		for (int i = 0; i < Board.maxIndex + 2; i++) {
			for (int j = 0; j < Board.maxIndex + 2; j++) {
				T[i][j] = square[i][j];
			}
		}
		return T;
		
	}
	
	public static Stack<ArrayList<Point>> getDefendAddition(){
		Stack<ArrayList<Point>> T = new Stack<ArrayList<Point>>();
		for (ArrayList<Point> arr : defenseAddition) {
			 ArrayList<Point> A = new ArrayList<Point>();
			for(Point P : arr) {
				A.add(P.clone());
			}
			T.push(A);
		}
		return T;
		 
	}
	
	public static void pop(Version H) {
		defenseAddition.clear();
		Stack<ArrayList<Point>> T = H.getDefenseAddition();
		for (ArrayList<Point> arr : T) {
			 ArrayList<Point> A = new ArrayList<Point>();
			for(Point P : arr) {
				A.add(P.clone());
			}
			defenseAddition.push(A);
		}
		int[][] T2 = H.getSquare();
		for (int i = 0; i < Board.maxIndex + 2; i++) {
			for (int j = 0; j < Board.maxIndex + 2; j++) {
				square[i][j] = T2[i][j];
			}
		}
		
	}
}
