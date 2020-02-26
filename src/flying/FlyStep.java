package flying;

import java.util.ArrayList;

import eat.BestEat;
import eat.EnemyScore;
import eat.SelfScore;
import model.Board;
import model.Point;

public class FlyStep {
	
	private static final int limit_1 = 0;
	private static final int limit_2 = 0;	//第二限制 
	
	private ArrayList<Point> pastPoint = new ArrayList<Point>();
	private ArrayList<Point> eatPoint = new ArrayList<Point>();
	private ArrayList<Point> fangEatPoint = new ArrayList<Point>();
	private Point Best_EatPoint;
	
	public FlyStep() {
		Judge flyTemp = new Judge();
		//判断己方子是否大于4
		int sum1 = 0;
		for (int i = 0; i < Board.maxIndex; i++) {
			for (int j = 0; j < Board.maxIndex; j++) {
				if(Board.getBoard(i,j) == Board.SELF) sum1++;
			}
		}
		
		//统计结束
		if(!flyTemp.getSelf_square().isEmpty() && sum1 > 3) {
			
			Point bestPoint_square = new Point(-1,-1,-10000);
			/**最好的初始点*/
			Point bestPoint = new Point(0,0);
			
			//遍历最好的成方的点，写在bestPoint_square中 ，score为价值
			for(Point P : flyTemp.getSelf_square()) {
				int tempx = 0;
				int tempy = 0;
				switch(P.getScore()){
				case 1:
					tempx = P.x + 1;
					tempy = P.y;
					break;
				case 2:
					tempx = P.x + 1;
					tempy = P.y + 1;
					break;
				case 3:
					tempx = P.x;
					tempy = P.y;
					break;
				case 4:
					tempx = P.x;
					tempy = P.y + 1;
					break;
				}
				Point bestPoint_temp = getLowestSelfPoint(P);
				
				Board.setBoard(bestPoint_temp.x, bestPoint_temp.y, Board.EMPTY);
				int Ax = Board.getBoard(tempx, tempy);
				Board.setBoard(tempx, tempy, Board.SELF);
				int Bx = SelfScore.getSelfScore(tempx, tempy) - bestPoint_temp.getScore();
				Board.setBoard(tempx, tempy, Ax);				
				Board.setBoard(bestPoint_temp.x, bestPoint_temp.y, Board.SELF);
				
				if(Bx > bestPoint_square.getScore()) {
					bestPoint_square = new Point(tempx,tempy,Bx);
					bestPoint = bestPoint_temp;
				}
			}
			//结束遍历
			pastPoint.clear();
			pastPoint.add(bestPoint_square);
			Best_EatPoint = bestPoint;
			//找到吃的子
			int qwertyuio = Board.getBoard(bestPoint_square.x, bestPoint_square.y);
			Board.setBoard(bestPoint_square.x, bestPoint_square.y, Board.SELF);
			Board.setBoard(bestPoint.x, bestPoint.y, Board.EMPTY);
			
			fangAdvice tAdvice = new fangAdvice(bestPoint_square.x, bestPoint_square.y);
			fangEatPoint = tAdvice.getFangEatAdvice();
			
			Board.setBoard(bestPoint.x, bestPoint.y, Board.SELF);
			Board.setBoard(bestPoint_square.x, bestPoint_square.y, qwertyuio);
			
		}
		else if(!flyTemp.getEnemy_square().isEmpty()) {
			Best_EatPoint = getLowestSelfPoint();	//最好的初始点
			Board.setBoard(Best_EatPoint.x, Best_EatPoint.y, Board.EMPTY);
			Point bestPoint_square = new Point(-1,-1,-10000);
			//遍历最好的破方的点，写在bestPoint_square中 ，score为价值
			for(Point P : flyTemp.getEnemy_square()) {
				int tempx = 0;
				int tempy = 0;
				switch(P.getScore()){
				case 1:
					tempx = P.x + 1;
					tempy = P.y;
					break;
				case 2:
					tempx = P.x + 1;
					tempy = P.y + 1;
					break;
				case 3:
					tempx = P.x;
					tempy = P.y;
					break;
				case 4:
					tempx = P.x;
					tempy = P.y + 1;
					break;
				}
				int qwertyuio = Board.getBoard(tempx, tempy);
				Board.setBoard(tempx, tempy, Board.SELF);
				int temp1 = SelfScore.getSelfScore(tempx, tempy);
				Board.setBoard(tempx, tempy, qwertyuio);
				if(temp1 > bestPoint_square.getScore() && !(Best_EatPoint.x == tempx && Best_EatPoint.y == tempy)) {
					bestPoint_square = new Point(tempx,tempy,temp1);
				}
			}
			//结束遍历
			pastPoint.add(bestPoint_square);
			Board.setBoard(Best_EatPoint.x, Best_EatPoint.y, Board.SELF);
		}
		else {
			//首先正常跳吃
			BestEat Eat_Jump = new BestEat();
			if(Eat_Jump.getBestScore() > limit_1) {
				Best_EatPoint = Eat_Jump.getBestPoint();
				pastPoint = Eat_Jump.getPastPoint();
				fangEatPoint = Eat_Jump.getfangEatPoint();
				eatPoint = Eat_Jump.getEatPoint();
			}
			
			
			else if(!flyTemp.getSelf_door().isEmpty()) {			
				//跳吃分数过低就寻找棋门
				ArrayList<Point> EmptyPoint = new ArrayList<Point>();	//储存可行的点
				
				Best_EatPoint = getLowestSelfPoint();	//最好的初始点
				Board.setBoard(Best_EatPoint.x, Best_EatPoint.y, Board.EMPTY);
				
				for(Point P : flyTemp.getSelf_door()) {
					
					//EMPTY:
					//(i + 1, j)(i + 1, j + 1)
					//(i, j)(i, j + 1)
					//(i, j + 1)(i + 1, j + 1)
					//(i, j)(i + 1, j)
					//(i, j + 1)(i + 1, j)
					//(i, j)(i + 1, j + 1)
					switch(P.getScore()) {
					case 1:
						EmptyPoint.add(new Point(P.x + 1,P.y));
						EmptyPoint.add(new Point(P.x + 1,P.y + 1));
						break;
					case 2:
						EmptyPoint.add(new Point(P.x,P.y));
						EmptyPoint.add(new Point(P.x,P.y + 1));
						break;
					case 3:
						EmptyPoint.add(new Point(P.x,P.y + 1));
						EmptyPoint.add(new Point(P.x + 1,P.y + 1));
						break;
					case 4:
						EmptyPoint.add(new Point(P.x,P.y));
						EmptyPoint.add(new Point(P.x + 1,P.y));
						break;
					case 5:
						EmptyPoint.add(new Point(P.x + 1,P.y));
						EmptyPoint.add(new Point(P.x,P.y + 1));
						break;
					case 6:
						EmptyPoint.add(new Point(P.x,P.y));
						EmptyPoint.add(new Point(P.x + 1,P.y + 1));
						break;
					}
				}
				int max_Score = -10000;
				Point a = new Point(0,0);
				for(Point P : EmptyPoint) {
					Board.setBoard(P.x, P.y, Board.SELF);
					int temp = SelfScore.getSelfScore(P.x, P.y);
					Board.setBoard(P.x, P.y, Board.EMPTY);
					if(temp > max_Score) {
						max_Score = temp;
						a = P;
					}
				}
				pastPoint.add(a);
				Board.setBoard(Best_EatPoint.x, Best_EatPoint.y, Board.SELF);
			}
			
			
			else if(Eat_Jump.getBestScore() > limit_2) {
				Best_EatPoint = Eat_Jump.getBestPoint();
				pastPoint = Eat_Jump.getPastPoint();
				fangEatPoint = Eat_Jump.getfangEatPoint();
				eatPoint = Eat_Jump.getEatPoint();
			}
			else {
				Best_EatPoint = getLowestSelfPoint();	//最好的初始点
				Board.setBoard(Best_EatPoint.x, Best_EatPoint.y, Board.EMPTY);
				
				int max_Score = -10000;
				Point a = new Point(0,0);
				for(int i = 1; i <= Board.maxIndex; i++) {
					for(int j = 1; j <=Board.maxIndex; j++) {
						if(Board.getBoard(i,j) == Board.EMPTY) {
							int temp = SelfScore.getSelfScore(i, j);
							if(temp > max_Score && !(Best_EatPoint.x == i && Best_EatPoint.y == j)) {
								max_Score = temp;
								a = new Point(i,j);
							}
						}
					}
				}
				pastPoint.add(a);
				Board.setBoard(Best_EatPoint.x, Best_EatPoint.y, Board.SELF);
			}
		}
	}
	
	/**
	 * 主要用于找出最不合适的点，Best_EatPoint
	 * */
	private Point getLowestSelfPoint() {
		int minScore_temp = 30000;
		Point minPoint = new Point(-1,-1);
		for(int o = 1;o <= Board.maxIndex; o ++) {
			for(int p = 1; p <= Board.maxIndex; p++) {
				if(Board.getBoard(o,p) == Board.SELF) {
					int temp = SelfScore.getSelfScore(o,p);
					if(temp < minScore_temp) {
						minScore_temp = temp;
						minPoint = new Point(o,p);
					}
				}
			}
		}
		return new Point(minPoint.x,minPoint.y,minScore_temp);
	}
	private Point getLowestSelfPoint(Point a) {
		int minScore_temp = 30000;
		Point minPoint = new Point(-1,-1);
		for(int o = 1;o <= Board.maxIndex; o ++) {
			for(int p = 1; p <= Board.maxIndex; p++) {
				//若不在那个方里
				if(Board.getBoard(o,p) == Board.SELF && !(o <= a.x + 1 && o >= a.x && p <= a.y + 1 && p >= a.y)) {
					int temp = SelfScore.getSelfScore(o,p);
					if(temp < minScore_temp) {
						minScore_temp = temp;
						minPoint = new Point(o,p);
					}
				}
			}
		}
		return new Point(minPoint.x,minPoint.y,minScore_temp);
	}
	
	private Point getHightestEmenyPoint() {
		int maxScore_temp = 0;
		Point maxPoint = new Point(-1,-1);
		for(int o = 1;o <= Board.maxIndex; o ++) {
			for(int p = 1; p <= Board.maxIndex; p++) {
				int temp = EnemyScore.getSingleScore(Board.getBoard(), o, p);
				if(temp > maxScore_temp) {
					maxScore_temp = temp;
					maxPoint = new Point(o,p);
				}
			}
		}
		return new Point(maxPoint.x,maxPoint.y,maxScore_temp);
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
}
