package eat;

import java.util.ArrayList;

import model.Board;
import model.Point;

public class BestEat {
	
	private ArrayList<Integer> Best_Eat = new ArrayList<Integer>();	//走法（方向）
	private ArrayList<Point> pastPoint = new ArrayList<Point>();
	private ArrayList<Point> eatPoint = new ArrayList<Point>();
	private ArrayList<Point> fangEatPoint = new ArrayList<Point>();
	private Point Best_EatPoint;
	
	private int bestScore;
	
	private void move_board(int x, int y, int direct) {
		Board.setBoard(x, y, Board.EMPTY);
		switch(direct) {
		case JumpEat.UP:Board.setBoard(x + 1, y, Board.SELF);break;
		case JumpEat.DOWN:Board.setBoard(x - 1, y, Board.SELF);break;
		case JumpEat.RIGHT:Board.setBoard(x, y + 1, Board.SELF);break;
		case JumpEat.LEFT:Board.setBoard(x, y - 1, Board.SELF);break;
		}
	}
	
	/***
	 * 
	 * @param x
	 * @param y
	 * @param direct	原move方向
	 */
	private void remove_board(int x, int y, int direct) {
		Board.setBoard(x, y, Board.EMPTY);
		switch(direct) {
		case JumpEat.UP:Board.setBoard(x - 1, y, Board.SELF);break;
		case JumpEat.DOWN:Board.setBoard(x + 1, y, Board.SELF);break;
		case JumpEat.RIGHT:Board.setBoard(x, y - 1, Board.SELF);break;
		case JumpEat.LEFT:Board.setBoard(x, y + 1, Board.SELF);break;
		}
	}
	/***
	 * 
	 * @param x
	 * @param y
	 * @return	若max = 0，就是静止不动
	 */
	private singleScore_return singleScore(int x, int y) {
		int stayScore = SelfScore.getSelfHoldScore(x, y);
			
		ArrayList<Integer> eatStep = new ArrayList<Integer>();
		JumpEat p = new JumpEat(x,y);
		int max = p.getMaxScore() + stayScore;
		if(p.getBesteat().isEmpty()) {
			max = -10000;
		}
		else {
			eatStep = p.getBesteat();
		}
		
		//上方
		if(Board.getBoard(x + 1,y) == Board.EMPTY) {
			move_board(x,y,JumpEat.UP);
			int a = SelfScore.getSelfScore(x + 1, y);
			fangAdvice xAdvice = new fangAdvice(x + 1, y);
			remove_board(x + 1,y,JumpEat.UP);
			if(a + xAdvice.getFangEatScore()> max) {
				max = a + xAdvice.getFangEatScore();
				eatStep.clear();
				eatStep.add(JumpEat.MOVE);
				eatStep.add(JumpEat.UP);
			}
		}
		
		//下方
		if(Board.getBoard(x - 1,y) == Board.EMPTY) {
			move_board(x,y,JumpEat.DOWN);
			int a = SelfScore.getSelfScore(x - 1, y);
			fangAdvice xAdvice = new fangAdvice(x - 1, y);
			remove_board(x - 1,y,JumpEat.DOWN);
			if(a + xAdvice.getFangEatScore()> max) {
				max = a + xAdvice.getFangEatScore();
				eatStep.clear();
				eatStep.add(JumpEat.MOVE);
				eatStep.add(JumpEat.DOWN);
			}
		}
		
		//右方
		if(Board.getBoard(x,y + 1) == Board.EMPTY) {
			move_board(x,y,JumpEat.RIGHT);
			int a = SelfScore.getSelfScore(x, y + 1);
			fangAdvice xAdvice = new fangAdvice(x, y + 1);
			remove_board(x,y + 1,JumpEat.RIGHT);
			if(a + xAdvice.getFangEatScore()> max) {
				max = a + xAdvice.getFangEatScore();
				eatStep.clear();
				eatStep.add(JumpEat.MOVE);
				eatStep.add(JumpEat.RIGHT);
			}
		}
		
		//左方
		if(Board.getBoard(x,y - 1) == Board.EMPTY) {
			move_board(x,y,JumpEat.LEFT);
			int a = SelfScore.getSelfScore(x, y - 1);
			fangAdvice xAdvice = new fangAdvice(x, y - 1);
			remove_board(x,y - 1,JumpEat.LEFT);
			if(a + xAdvice.getFangEatScore()> max) {
				max = a + xAdvice.getFangEatScore();
				eatStep.clear();
				eatStep.add(JumpEat.MOVE);
				eatStep.add(JumpEat.LEFT);
			}
		}
		
		if(eatStep.size() > 0) {
			return (new singleScore_return(max - stayScore,eatStep));
		}
		else return (new singleScore_return( -10000 ,eatStep));
	}
	
	/**
	 * eat:吃的方法
	 * */
	private class singleScore_return {
		public int Score;
		public ArrayList<Integer> Eat;
		public singleScore_return(int Score,ArrayList<Integer> Eat) {
			this.Score = Score;
			this.Eat = Eat;
		}
	}
	
	public BestEat() {
		int maxScore = -1000;	//最好的分数
		Point best = new Point(0,0);
		ArrayList<Integer> step = new ArrayList<Integer>();
		for(int i = 1;i <= Board.maxIndex; i++) {
			for(int j = 1;j <= Board.maxIndex; j++) {
				if(Board.getBoard(i, j) == Board.SELF) {
					singleScore_return a = singleScore(i,j);
					if(a.Score >= maxScore) {
						maxScore = a.Score;
						step = a.Eat;
						best= new Point(i,j);
					}
				}
			}
		}
		Best_Eat = step;
		Best_EatPoint = best;
		int tempx = best.x;
		int tempy = best.y;
		if(step.get(0) == JumpEat.MOVE) {
			switch(step.get(1)) {
				case JumpEat.UP:pastPoint.add(new Point(best.x + 1, best.y));tempx ++;break;
				case JumpEat.DOWN:pastPoint.add(new Point(best.x - 1, best.y));tempx --;break;
				case JumpEat.RIGHT:pastPoint.add(new Point(best.x, best.y + 1));tempy ++;break;
				case JumpEat.LEFT:pastPoint.add(new Point(best.x, best.y - 1));tempy --;break;
			}
		}
		else {
			
			for(Integer s : step) {
				switch(s) {
				case JumpEat.UP:
					eatPoint.add(new Point(tempx + 1,tempy));
					pastPoint.add(new Point(tempx + 2,tempy));
					tempx += 2;
					break;
				case JumpEat.DOWN:
					eatPoint.add(new Point(tempx - 1,tempy));
					pastPoint.add(new Point(tempx - 2,tempy));
					tempx -= 2;
					break;
				case JumpEat.RIGHT:
					eatPoint.add(new Point(tempx,tempy + 1));
					pastPoint.add(new Point(tempx,tempy + 2));
					tempy += 2;
					break;
				case JumpEat.LEFT:
					eatPoint.add(new Point(tempx,tempy - 1));
					pastPoint.add(new Point(tempx,tempy - 2));
					tempy -= 2;
					break;
				}
			}
		}
		
		
		//计算成方的数目存在add中
		Board.setBoard(best.x, best.y, Board.EMPTY);				//2020.2.23修正
		Board.setBoard(tempx, tempy, Board.SELF);
		for(Point c:pastPoint) {
			Board.setBoard(c.x, c.y, Board.EMPTY);
		}
		
		fangAdvice tAdvice = new fangAdvice(tempx,tempy);
		fangEatPoint = tAdvice.getFangEatAdvice();
		
		
		for(Point c:pastPoint) {
			Board.setBoard(c.x, c.y, Board.ENEMY);
		}
		Board.setBoard(tempx, tempy, Board.EMPTY);
		Board.setBoard(best.x, best.y, Board.SELF);					//2020.2.23修正
		
		
		
		bestScore = maxScore;
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
		
		public ArrayList<Point> getFangEatAdvice(){
			return fangEatPoint;
		}
	}
	
	public ArrayList<Integer> getBestEat(){
		return Best_Eat;
	}
	
	public Point getBestPoint() {
		return Best_EatPoint;
	}
	
	public ArrayList<Point> getEatPoint(){
		return eatPoint;
	}
	
	public ArrayList<Point> getPastPoint(){
		return pastPoint;
	}
	public ArrayList<Point> getfangEatPoint(){
		return fangEatPoint;
	}
	public int getBestScore() {
		return bestScore;
	}
}
