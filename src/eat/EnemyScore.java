package eat;

import model.Board;
import model.Point;
import model.Step;
import opening.Open;

public class EnemyScore {
	//存储每一个黑子的评分值
	private int[][] scores = new int[Board.maxIndex + 2][Board.maxIndex + 2];
	private int[][] board;
	boolean[][] flags = new boolean[Board.maxIndex + 2][Board.maxIndex + 2];
	/**
	 * 
	 * @param board	当前的棋盘数组
	 */
	public EnemyScore(int[][] board) {
		this.board = board.clone();
	}
	
	/**
	 * 
	 * @param x	查询点横坐标
	 * @param y	查询点纵坐标
	 * @return	该点敌方子的评分值
	 */
	public int getScore(int x, int y) {
		return scores[x][y];
	}
	
	/**
	 * 
	 * @param point	要查询的点
	 * @return	该点敌方子的评分值
	 */
	public int getScore(Point point) {
		return scores[point.x][point.y];
	}
	
	/**
	 * 
	 * @param step	在这一回合中双方的步信息。
	 */
	public void update(Step step) {
		
		clearFlags();
		
		if(step.self) {
			board[step.selfStart.x][step.selfStart.y] = Board.EMPTY;
			board[step.selfEnd.x][step.selfEnd.y] = Board.SELF;
			for(Point p: step.atePoint) {
				board[p.x][p.y] = Board.EMPTY;
			}
			updateScore(step.selfStart.x, step.selfStart.y);
			updateScore(step.selfEnd.x, step.selfEnd.y);
			for(Point p : step.atePoint) {
				updateScore(p.x, p.y);
			}
		}
		
		if(step.enemy) {
			board[step.enemyStart.x][step.enemyStart.y] = Board.EMPTY;
			board[step.enemyEnd.x][step.enemyEnd.y] = Board.ENEMY;
			for(Point p : step.BeeatenPoint) {
				board[p.x][p.y] = Board.EMPTY; 
			}
			updateScore(step.enemyStart.x, step.enemyStart.y);
			updateScore(step.enemyEnd.x, step.enemyEnd.y);
			for(Point p : step.BeeatenPoint) {
				updateScore(p.x, p.y);
			}
		}
	}
	
	
	private void updateScore(int x, int y) {
		for(int i = x - 1; i <= x + 1; i++) {
			for(int j = y - 1; y <= y + 1; y++) {
				if(board[i][j] == Board.ENEMY && flags[i][j]) {
					flags[i][j] = false;
					scores[i][j] = setSingleScore(i, j);
				}
			}
		}
	}
	
	private int setSingleScore(int x, int y) {
		if (board[x][y] != Board.ENEMY)
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
			switch (board[temp_x][y]) {
			case Board.OUTER:
				break;
			case Board.ENEMY:
				score_temp += score_base;
				break;
			case Board.SELF:
				score_temp += score_base >> 1;
				break;
			}
			// 右边
			switch (board[x][temp_y]) {
			case Board.OUTER:
				score_temp = 0;
				break;
			case Board.ENEMY:
				score_temp = getTwi(score_temp);
				break;
			case Board.SELF:
				score_temp = getHalf(score_temp);
				break;
			case Board.EMPTY:
				if (score_temp == Open.attackBase)
					score_temp = 0;
			}
			// 角落
			switch (board[temp_x][temp_y]) {
			case Board.OUTER:
				score_temp = 0;
				break;
			case Board.ENEMY:
				score_temp <<= 1;
				break;
			case Board.SELF:
				score_temp >>= 1;
				break;
			}
			score += score_temp;
		}
		return score;
	}
	
	public static int getSingleScore(int [][]board,int x, int y) {
		if (board[x][y] != Board.ENEMY)
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
			switch (board[temp_x][y]) {
			case Board.OUTER:
				break;
			case Board.ENEMY:
				score_temp += score_base;
				break;
			case Board.SELF:
				score_temp += score_base >> 1;
				break;
			}
			// 右边
			switch (board[x][temp_y]) {
			case Board.OUTER:
				score_temp = 0;
				break;
			case Board.ENEMY:
				score_temp = getTwi(score_temp);
				break;
			case Board.SELF:
				score_temp = getHalf(score_temp);
				break;
			case Board.EMPTY:
				if (score_temp == Open.attackBase)
					score_temp = 0;
			}
			// 角落
			switch (board[temp_x][temp_y]) {
			case Board.OUTER:
				score_temp = 0;
				break;
			case Board.ENEMY:
				score_temp <<= 1;
				break;
			case Board.SELF:
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
	
	private void clearFlags() {
		for(int i = 0; i < flags.length; i++) {
			for(int j = 0; j < flags[i].length; j++) {
				flags[i][j] = true;
			}
		}
	}

}
