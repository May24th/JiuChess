package ui;

import model.Point;

public class ChessState {
	public  ChessState() {
		
		stage = Stage.OPEN;		
		round = Round.ENEMY;			
		eatStage = EatStage.NOTEATING;	
		enemyPiece = 1;
		selfPiece = 1;
		emptyPiece = MainFrame.boardSize * MainFrame.boardSize - 2;
		addition = 2;
	}
	
	/**阶段：开局，吃子*/
	public enum Stage{
		OPEN,EAT
	};
	/**回合：敌方，己方*/
	public enum Round{
		SELF,ENEMY
	};
	/**对手阶段判断与记录*/
	public enum EatStage{
		NOTEATING,
		SELECTING,
		JUMPING,
		FANGEATING
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
	/**已选择的跳吃的点，暂时储存*/
	private Point jumpPiece;
	
	public void setJumpPiece (int x,int y) {
		jumpPiece = new Point(x, y);
	}
	
	public Point getJumpPiece() {
		return jumpPiece.clone();
	}
}
