package ui;

import java.util.ArrayList;

import back.History;
import back.Version;
import model.Point;

public class ChessState implements History{
	public  ChessState() {
		
		stage = Stage.OPEN;		
		round = Round.ENEMY;		
		eatStage = EatStage.NOTEATING;	
		enemyPiece = 1;
		selfPiece = 1;
		emptyPiece = MainFrame.boardSize * MainFrame.boardSize - 2;
		addition = 2;
		tempTC = new ArrayList<Point>();
	}
	
	/**阶段：开局，吃子*/
	public enum Stage{
		OPEN,
		EAT
	};
	/**回合：敌方，己方*/
	public enum Round{
		SELF,
		ENEMY
	};
	/**对手阶段判断与记录*/
	public enum EatStage{
		NOTEATING,
		SELECTING,
		JUMPING,
		JUMPINGJ,
		FANGEATING,
		FLYMOVE
	};
	
	public Stage stage;
	public Round round;
	public EatStage eatStage;
	/**当前对方子数*/
	public int enemyPiece;
	/**当前己方子数*/
	public int selfPiece;
	/**空的数目*/
	public int emptyPiece;
	/**记录回合数*/
	public int addition;
	/**已选择的跳吃的点，暂时储存，前端的值*/
	private Point jumpPiece = null;
	/**暂存方吃的数量*/
	public int FangEatNum;
	/**暂存跳吃的子,为了棋谱的作用，所以存的是后端棋盘的值*/
	public ArrayList<Point> tempTC;
	
	public void setSelectPiece (int x,int y) {
		jumpPiece = new Point(x, y);
	}
	
	public Point getSelectPiece() {
		
		try {
			return new Point(jumpPiece.x,jumpPiece.y);
		} catch (Exception e) {
			return new Point(0,0);
		}
		
	}
	
	public ChessState getChessState() {
		ChessState a = new ChessState();
		a.addition = addition;
		a.eatStage = eatStage;
		a.emptyPiece = emptyPiece;
		a.enemyPiece = enemyPiece;
		a.FangEatNum = FangEatNum;
		try {
			a.jumpPiece = jumpPiece.clone();
		} catch (Exception e) {
			a.jumpPiece = new Point(0,0);
		}
		
		a.round = round;
		a.selfPiece = selfPiece;
		a.stage = stage;
		for (Point p : tempTC) {
			a.tempTC.add(p.clone());
		}
		
		return a;
	}

	@Override
	public void pop(Version H) {
		ChessState a = H.getChessState();
		addition = a.addition;
		eatStage = a.eatStage;
		emptyPiece = a.emptyPiece;
		enemyPiece = a.enemyPiece;
		FangEatNum = a.FangEatNum;
		jumpPiece = a.jumpPiece.clone();
		round = a.round;
		selfPiece = a.selfPiece;
		stage = a.stage;
		tempTC.clear();
		for (Point p : a.tempTC) {
			tempTC.add(p.clone());
		}
	}
}
