package opening;

import java.util.ArrayList;

import model.Board;
import model.Point;

public class Open {
	
	public static final int attackBase = 32;	
	public static final int defenseBase = 1;	

	public static final int defenseEL = 6;
//	private final int attackEL = 100;
	
	public static final int attackPower = 16;
	public static final int defensePower = 1;


	public static final int acceptableRate = 62;	//百分比,v2,无用

	public static void init() {
		Board.init();
		Defend.init();
		Attack.init();
	}
	
	//有问题
	public static void play(int x, int y, int piece) {
		Board.setBoard(x, y, piece);
		Defend.twoCheck(x, y, piece);
		Attack.updateAttackScore(x, y);
		Defend.updateDefenseScore(x, y, piece);
	}
	


	/******************************************************************************************/
	/***
	 * vision 1 简单地对进攻和防守的bestPoint进行筛选 当“防守必要”时，进行防守 否则，进行进攻
	 * 
	 * @return
	 */
	
	public static ArrayList<Point> getBestPoints() {
		ArrayList<Point> attacks = Attack.getBestAttackPoints();
		ArrayList<Point> defenses = Defend.getBestDefensePoints();
//		ArrayList<Point> common = new ArrayList<Point>();
//		for(int i = 0; i < attacks.size(); i++) {
//			for(int j = 0; j < defenses.size(); j++) {
//				if(attacks.get(i).equals(defenses.get(j)) && !common.contains(defenses.get(j))) common.add(defenses.get(j));
//			}
//		}
//		if(!common.isEmpty()) return common;
		if(!defenses.isEmpty() && defenses.get(0).getScore() > 100) return defenses;
		else return attacks;
	}
	
	public static ArrayList<Point> getBestPoints_v1() {
		ArrayList<Point> attacks = Attack.getBestAttackPoints();
		ArrayList<Point> defenses = Defend.getBestDefensePoints();
		ArrayList<Point> common = new ArrayList<Point>();
		for (int i = 0; i < attacks.size(); i++) {
			for (int j = 0; j < defenses.size(); j++) {
				if (attacks.get(i).equals(defenses.get(j))
						&& !common.contains(defenses.get(j)))
					common.add(defenses.get(j));
			}
		}
		if (!common.isEmpty())
			return common;
		if (defenses.isEmpty() && attacks.isEmpty())
			return null;
		else if (defenses.isEmpty())
			return attacks;
		else if (attacks.isEmpty())
			return defenses;
		else {
			if (defenses.get(0).getScore() >= defenseEL) {
				ArrayList<Point> bestPoints = new ArrayList<Point>();
				int max = 0;
				for (int i = 0; i < defenses.size(); i++) {
					int temp = Attack.getAttackScores(defenses.get(i).x, defenses
							.get(i).y);
					if (temp > 0 && temp > max) {
						max = temp;
						bestPoints.clear();
						bestPoints.add(new Point(defenses.get(i).x,
								defenses.get(i).y));
					} else if (temp == max) {
						bestPoints.add(new Point(defenses.get(i).x,
								defenses.get(i).y));
					}
				}
				return bestPoints;
			}
//			if(attacks.get(0).getScore() >= attackEL)	//可能没必要，所以先删了
//			{

//				return attacks;
//			}
			else {
				ArrayList<Point> bestPoints = new ArrayList<Point>();
				int max = 0;
				for (int i = 0; i < attacks.size(); i++) {
					int temp = Defend.getDefenseScore(attacks.get(i).x,
							attacks.get(i).y);
					if (temp > 0 && temp > max) {
						max = temp;
						bestPoints.clear();
						bestPoints.add(
								new Point(attacks.get(i).x, attacks.get(i).y));
					} else if (temp == max) {
						bestPoints.add(
								new Point(attacks.get(i).x, attacks.get(i).y));
					}
				}
				return bestPoints;
			}
		}
	}

	/***
	 * vision 2
	 * 对进攻和防守的bestPoint进行筛选
	 * 当“防守必要”时，进行防守
	 * 否则，
	 * @return
	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public ArrayList<Point> getBestPoints_v2() {
//		ArrayList<Point> attacks = getBetterAttackPoints();
//		ArrayList<Point> defenses = getBetterDefensePoints();
//		Comparator<? super Point> com = new Comparator() {
//
//			@Override
//			public int compare(Object obj1, Object obj2) {
//				// TODO Auto-generated method stub
//				Point pt1 = (Point)obj1;
//				Point pt2 = (Point)obj2;
//				return pt2.compareTo(pt1);
//			}
//			
//		};
//		attacks.sort(com);		//降序
//		defenses.sort(com);		//降序
//		ArrayList<Point> common = new ArrayList<Point>();
//		for(int i = 0; i < attacks.size(); i++) {
//			for(int j = 0; j < defenses.size(); j++) {
//				if(attacks.get(i).equals(defenses.get(j)) && !common.contains(defenses.get(j))) common.add(defenses.get(j));
//			}
//		}
//		if(!common.isEmpty()) return common;
//		else if
//	}
	
	
	public static ArrayList<Point> getBestPoints_v3() {
		ArrayList<Point> defenses = Defend.getBestDefensePoints();
		if(!defenses.isEmpty()){
			if(defenses.get(0).getScore() >= defenseEL)
			{
				ArrayList<Point> bestPoints = new ArrayList<Point>();
				int max = 0;		
				for(Point i : defenses){
					int temp = Attack.getAttackScores(i.x, i.y);
					if(temp > max) {
						max = temp;
						bestPoints.clear();
						bestPoints.add(new Point(i.x, i.y));
					} 
					else if(temp == max) {
						bestPoints.add(new Point(i.x, i.y));
					}
				}
				return bestPoints;
			}
		}
		ArrayList<Point> bestPoints = Defend.getBestDefensePoints();
		int max = 0;
		for(int i = 1;i <= Board.maxIndex;i ++) {
			for(int j = 1;j <= Board.maxIndex;j ++) {
				if(Board.getBoard(i,j) == Board.EMPTY) {
					int attackTemp = 0;
					int defenseTemp = 0;
					if(Attack.getAttackScores(i, j) > 0) attackTemp = Attack.getAttackScores(i, j);
					if(Defend.getDefenseScore(i,j) > 0) defenseTemp = Defend.getDefenseScore(i,j);
					int Temp = attackTemp*attackPower + defenseTemp*defensePower;
					if(max < Temp) {
						max = Temp;
						bestPoints.clear();
						bestPoints.add(new Point(i, j, Temp));
					} else if(Temp == max) {
							bestPoints.add(new Point(i, j, Temp));
					}
				}
				
			}
		}
		
		return bestPoints;
	}
}
