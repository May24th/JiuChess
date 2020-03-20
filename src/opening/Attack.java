package opening;

import java.util.ArrayList;

import back.Version;
import model.Board;
import model.Point;

public class Attack {
	private static int[][] attackScores = new int[Board.maxIndex + 2][Board.maxIndex + 2];
	
	public static void init() {
		updateAttackScore(Board.index, Board.index);
		updateAttackScore(Board.index + 1, Board.index + 1);
	}
	
	public static ArrayList<Point> getBestAttackPoints() {
		int max = 0;
		ArrayList<Point> bestPoints = new ArrayList<Point>();
		for (int i = Board.maxIndex; i >= Board.minIndex; i--) {
			for (int j = Board.minIndex; j <= Board.maxIndex; j++) {
				int temp = attackScores[i][j];
				if (temp > 0 && temp > max) {
					max = temp;
					bestPoints.clear();
					bestPoints.add(new Point(i, j, temp));
				} else if (temp == max) {
					bestPoints.add(new Point(i, j, temp));
				}
			}
		}
		return bestPoints;
	}
	

	/***
	 * 得到进攻效果较好的那一批点
	 * 
	 * @return
	 */

//	private ArrayList<Point> getBetterAttackPoints() {
//		int max = 0;
////		PriorityQueue<Point> subPoints = new PriorityQueue<Point>();
////		for(int i = maxIndex; i >= minIndex; i--) {
////			for(int j = minIndex; j <= maxIndex; j++) {
////				int temp = attackScores[i][j];
////				if(temp > 0)
////				{
////					if(temp > max) {
////						if(temp*acceptableRate/100 > max) subPoints.clear();
////						max = temp;
////					}
////				subPoints.add(new Point(i,j,temp));
////				}
////			}
////		}
////		max = max*acceptableRate/100;
////		Point a = new Point(0,0,0);
////		ArrayList<Point> betterPoints = new ArrayList<Point>();
////		while((a = subPoints.poll()).getScore() >= max ){		//在可接受范围内
////			betterPoints.add(a);
////		}
//		return betterPoints;
//	}
	
	/***
	 * 对当前已落子点周围点的评估更新，分数保存于attackScores
	 * 
	 * @param x 当前已落子点的横坐标
	 * @param y 当前已落子点的纵坐标
	 */
	static void updateAttackScore(int x, int y) {
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				attackScores[i][j] = setSingleAttackScore(i, j);
			}
		}
	}

	private static int setSingleAttackScore(int x, int y) {
		if (Board.getBoard(x, y) != Board.EMPTY)
			return -1;
		int score = 0;
		int score_base = Open.attackBase << 1; // 50
		int score_temp = 0;
		int temp_x = 0, temp_y = 0;
		for (int i = 1; i <= 4; i++) {
			switch (i) {
			case 1:// 右上角
				temp_x = x + 1;
				temp_y = y + 1;
				break;
			case 2:// 右下角
				temp_x = x - 1;
				temp_y = y + 1;
				break;
			case 3:// 左上角
				temp_x = x + 1;
				temp_y = y - 1;
				break;
			case 4:// 左下角
				temp_x = x - 1;
				temp_y = y - 1;
				break;
			}
			// 上面
			score_temp = 0;
			switch (Board.getBoard(temp_x, y)) {
			case Board.OUTER:
				break;
			case Board.SELF:
				score_temp += score_base;
				break;
			case Board.ENEMY:
				score_temp += score_base >> 1;
				break;
			}
			// 右边
			switch (Board.getBoard(x, temp_y)) {
			case Board.OUTER:
				score_temp = 0;
				break;
			case Board.SELF:
				score_temp = getTwi(score_temp);
				break;
			case Board.ENEMY:
				score_temp = getHalf(score_temp);
				break;
			case Board.EMPTY:
				if (score_temp == Open.attackBase)
					score_temp = 0;
			}
			// 角落
			switch (Board.getBoard(temp_x, temp_y)) {
			case Board.OUTER:
				score_temp = 0;
				break;
			case Board.SELF:
				score_temp <<= 1;
				break;
			case Board.ENEMY:
				score_temp >>= 1;
				break;
			}
			score += score_temp;
		}
		return score;
	}
	
	private static int getTwi(int pre) {
		if (pre == 0)
			return Open.attackBase << 1;
		if (pre == Open.attackBase)
			return Open.attackBase;
		return pre << 1;
	}

	private static int getHalf(int pre) {
		if (pre == Open.attackBase)
			return 0;
		return pre >> 1;
	}

	public static int getAttackScores(int x, int y) {
		return attackScores[x][y];
	}

	public static void setAttackScores(int x, int y, int attackScore) {
		Attack.attackScores[x][y] = attackScore;
	}
	
	public static int[][] getAttackScore(){
		int[][] T = new int[Board.maxIndex + 2][Board.maxIndex + 2];
		for (int i = 0; i < Board.maxIndex + 2; i++) {
			for (int j = 0; j < Board.maxIndex + 2; j++) {
				T[i][j] = attackScores[i][j]; 
			}
		}
		return T;
	}
	
	public static void pop(Version H) {
		int[][] T = H.getAttackScore();
		for (int i = 0; i < Board.maxIndex + 2; i++) {
			for (int j = 0; j < Board.maxIndex + 2; j++) {
				attackScores[i][j] = T[i][j]; 
			}
		}
	}
}
