package ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import eat.BestEat;
import flying.FlyStep;
import model.Board;
import model.Point;
import opening.Open;
import test.ErrorTracker;
import ui.ChessState.EatStage;
import ui.ChessState.Round;
import ui.ChessState.Stage;
import ui.MainFrame.conPane;

public class CenterBorad extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = -1602322585809545383L;
	/**
	 * 创建棋盘类
	 * @param row 行数/2
	 */
	public CenterBorad(MainFrame owner, conPane parentComponent, int row) {
		setLayout(null);
		setFont(new Font("微软雅黑", Font.BOLD, 16));
		ownerFrame = owner;
		this.selfColor = owner.selfColor;
		this.enemyColor = owner.enemyColor;
		this.qipuText = parentComponent.rightText.qipuArea;
		this.mesText = parentComponent.rightText.messageArea;
		this.state = owner.state;
		this.bottom = parentComponent.bottomLabel;
		
		this.size = row*2*gridSize - gridSize + chessSize + letterSize + 2*marginWidth;
		this.boardSize = row*2*gridSize - gridSize;
		this.x_indentation = chessSize/2 + marginWidth + letterSize;
		this.y_indentation = chessSize/2 + marginWidth;
		this.gridrow = row * 2;
		
		setSize(this.size,this.size);
		addMouseMotionListener(this);
		
		/**test*/
		l1.setBounds(0, 0, 100, 20);
		l2.setBounds(0, 20, 100, 20);
		add(l1);
		add(l2);
		
		//预览棋子
		previewPiece = new Piece(owner, this, new Color(200,200,200));
		previewPiece.setVisible(false);
		//预览棋子监听器	
		MouseListener previewListener = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				previewPiece.setVisible(false);
				if(state.stage == Stage.OPEN) {
					//在开局阶段
					//玩家下棋
					state.addition ++;
					Point a = transToBack(new Point(previewPiece.getlocX(), previewPiece.getlocY()));
					Open.play(a.x, a.y, Board.ENEMY);
					formalPiece[previewPiece.getlocX()][previewPiece.getlocY()].setPiece(enemyColor);
					state.enemyPiece ++;
		  			state.emptyPiece --;
		  			state.round = Round.SELF;
		  			qipuText.append((enemyColor == Color.black? "B":"W") + state.addition +
		  					"：" + a.x + (char)(a.y - 1 + 'A') + "\n");
		  			
		  			bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
		  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
		  					"，空子数：" + state.emptyPiece + "。");
		  			
		  			if(state.emptyPiece > 0) {											//还在开局
		  				//电脑下
		  				state.addition ++;
		  				mesText.setText("布局阶段：\n轮到电脑下！\n请等待...\n");
		  	  			state.round = Round.SELF;
		  	  			ArrayList<Point> tArrayList = Open.getBestPoints_v3();
		  	  			int a1 = new Random().nextInt(tArrayList.size());
		  	  			Open.play(tArrayList.get(a1).x, tArrayList.get(a1).y, Board.SELF);
			  	  		a = transToFront(tArrayList.get(a1));
		  	  			formalPiece[a.x][a.y].setPiece(selfColor);
		  	  			state.selfPiece ++;
		  	  			state.emptyPiece --;
		  	  			qipuText.append((selfColor == Color.black? "B":"W") + state.addition +
		  	  					"：" + tArrayList.get(a1).x + (char)(tArrayList.get(a1).y - 1 + 'A') + "\n");
			  	  		bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
			  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
			  					"，空子数：" + state.emptyPiece + "。");
			  	  		
			  	  		if(state.emptyPiece == 0) {										//进入行棋阶段(电脑先下)
			  	  			
				  	  		Board.setBoard(MainFrame.boardSize/2, MainFrame.boardSize/2, Board.EMPTY);
				  			Board.setBoard(MainFrame.boardSize/2 + 1, MainFrame.boardSize/2 + 1, Board.EMPTY);
				  			switch (ownerFrame.rotateA) {
							case 0:
							case 2:
								formalPiece[MainFrame.boardSize/2][MainFrame.boardSize/2].setEmpty();
								formalPiece[MainFrame.boardSize/2 + 1][MainFrame.boardSize/2 + 1].setEmpty();
								break;
							case 1:
							case 3:
								formalPiece[MainFrame.boardSize/2][MainFrame.boardSize/2 + 1].setEmpty();
								formalPiece[MainFrame.boardSize/2 + 1][MainFrame.boardSize/2].setEmpty();
								break;
							default:
								formalPiece[MainFrame.boardSize/2][MainFrame.boardSize/2].setEmpty();
								formalPiece[MainFrame.boardSize/2 + 1][MainFrame.boardSize/2 + 1].setEmpty();
								break;
							}
				  			state.stage = Stage.EAT;
				  			state.enemyPiece --;
				  			state.selfPiece --;
				  			state.emptyPiece ++;
				  			state.emptyPiece ++;
				  			bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
				  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
				  					"，空子数：" + state.emptyPiece + "。");
				  			qipuText.append("------------行棋阶段------------\n");
			  	  			mesText.setText("行棋阶段：\n轮到电脑下！\n请等待...\n");
			  	  			
			  	  			//行棋第一步
			  	  			state.addition ++;
			  	  			BestEat bat = new BestEat();
			  	  			//初始点
			  				Point p = bat.getBestPoint();
			  				Board.setBoard(p.x, p.y, Board.EMPTY);
			  				a = transToFront(p);
			  				formalPiece[a.x][a.y].setEmpty();
			  				

			  				qipuText.append((selfColor == Color.white?"W":"B") + state.addition + "：" + p.x + (char)('A' + p.y - 1));
			  				
			  				Point last = null;
			  				for(Point i : bat.getPastPoint()) {
			  					qipuText.append("->" + i.x + (char)('A' + i.y - 1));
			  					last = i;
			  				}

			  				qipuText.append("\n");
			  				Board.setBoard(last.x, last.y, Board.SELF);
			  				a = transToFront(last);
			  				formalPiece[a.x][a.y].setPiece(selfColor);
			  				
			  				
			  				ArrayList<Point> eat = bat.getEatPoint();
			  				ArrayList<Point> feat = bat.getfangEatPoint();
			  				if(!eat.isEmpty()) {
			  					qipuText.append("TC:");
			  					for(Point i : eat) {
			  						qipuText.append("  " + i.x + (char)('A' + i.y - 1));
			  						Board.setBoard(i.x,i.y, Board.EMPTY);
			  						a = transToFront(i);
			  						formalPiece[a.x][a.y].setEmpty();
			  						state.enemyPiece --;
			  						state.emptyPiece ++;
			  					}
			  					qipuText.append("\n");
			  				}
			  				
			  				if(!feat.isEmpty()) {
			  					qipuText.append("FC:");
			  					for(Point i : feat) {
			  						qipuText.append("  " + i.x + (char)('A' + i.y - 1));
			  						Board.setBoard(i.x,i.y, Board.EMPTY);
			  						a = transToFront(i);
			  						formalPiece[a.x][a.y].setEmpty();
			  						state.enemyPiece --;
			  						state.emptyPiece ++;
			  					}
			  					qipuText.append("\n");
			  				}
			  				bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
				  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
				  					"，空子数：" + state.emptyPiece + "。");
			  				mesText.setText("行棋阶段：\n轮到你了！\n请选择要走的棋子\\n");
			  				state.eatStage = EatStage.SELECTING;
			  				state.round = Round.ENEMY;
			  				
			  	  		}
			  	  		else {															//依然在布局
			  	  			state.round = Round.ENEMY;
							mesText.setText("布局阶段：\n轮到你了！\n");
						}
		  	  			
		  			}
		  			else {																//进入行棋阶段(玩家先下)
		  				Board.setBoard(MainFrame.boardSize/2, MainFrame.boardSize/2, Board.EMPTY);
			  			Board.setBoard(MainFrame.boardSize/2 + 1, MainFrame.boardSize/2 + 1, Board.EMPTY);
			  			switch (ownerFrame.rotateA) {
						case 0:
						case 2:
							formalPiece[MainFrame.boardSize/2][MainFrame.boardSize/2].setEmpty();
							formalPiece[MainFrame.boardSize/2 + 1][MainFrame.boardSize/2 + 1].setEmpty();
							break;
						case 1:
						case 3:
							formalPiece[MainFrame.boardSize/2][MainFrame.boardSize/2 + 1].setEmpty();
							formalPiece[MainFrame.boardSize/2 + 1][MainFrame.boardSize/2].setEmpty();
							break;
						default:
							formalPiece[MainFrame.boardSize/2][MainFrame.boardSize/2].setEmpty();
							formalPiece[MainFrame.boardSize/2 + 1][MainFrame.boardSize/2 + 1].setEmpty();
							break;
						}
			  			state.stage = Stage.EAT;
			  			state.eatStage = EatStage.SELECTING;
			  			state.enemyPiece --;
			  			state.selfPiece --;
			  			state.emptyPiece ++;
			  			state.emptyPiece ++;
			  			bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
			  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
			  					"，空子数：" + state.emptyPiece + "。");
			  			qipuText.append("------------行棋阶段------------\n");
			  			mesText.setText("行棋阶段：\n轮到你了！\n请选择要走的棋子\n");
			  			
		  			}
				}
				else {
					//行棋阶段
					switch (state.eatStage) {
					case NOTEATING:			//发生bug
						qipuText.append("-------\n" +
								ErrorTracker.getTestMessage() +
								"ERROR_stage与eatStage不匹配\n" +
								"--------\n");
						break;
					case SELECTING:			//选择阶段
						state.addition ++;
						if(state.enemyPiece > MainFrame.boardSize) state.eatStage = EatStage.JUMPING;
						else state.eatStage = EatStage.FLYMOVE;
						state.setSelectPiece(previewPiece.getlocX(), previewPiece.getlocY());
						mesText.append("当前已选中：" + previewPiece.getlocX() + (char)(previewPiece.getlocY() + 'A' - 1) + "\n");
						break;
					case JUMPING:			//跳吃阶段（第一次，还不确定跳吃还是移动）
						if(Math.abs(previewPiece.getlocX() + previewPiece.getlocY() - state.getSelectPiece().x - state.getSelectPiece().y) == 2) {
							//jumpEat
							state.selfPiece --;
							Point a1 = state.getSelectPiece();
							Point a2 = new Point(previewPiece.getlocX(), previewPiece.getlocX());
							formalPiece[a1.x][a1.y].setEmpty();
							formalPiece[(a1.x + a2.x)/2][(a1.y + a2.y)/2].setEmpty();
							formalPiece[a2.x][a2.y].setPiece(enemyColor);
							state.setSelectPiece(a2.x, a2.y);
							
							a1 = transToBack(a1);
							a2 = transToBack(a2);
							Board.setBoard(a1.x,a1.y,Board.EMPTY);
							Board.setBoard(a2.x,a2.y,Board.ENEMY);
							Board.setBoard((a1.x + a2.x)/2, (a1.y + a2.y)/2, Board.EMPTY);
							state.eatStage = EatStage.JUMPINGJ;
							owner.mainPanel.leftButton.endEating.setVisible(true);
							mesText.append("您选择跳吃，若想结束跳吃，请点击左边的“结束跳吃”按钮\n");
							qipuText.append((selfColor == Color.white?"W":"B") + state.addition + "：" + a1.x + (char)('A' + a1.y - 1) + "->" + a2.x + (char)('A' + a2.y - 1) + "\n");
							state.tempTC.add(new Point((a1.x + a2.x)/2, (a1.y + a2.y)/2));
						}
						else if (previewPiece.getlocX() == state.getSelectPiece().x && previewPiece.getlocY() == state.getSelectPiece().y) {
							state.eatStage = EatStage.SELECTING; 
							mesText.append("当前已取消选中：" + previewPiece.getlocX() + (char)(previewPiece.getlocY() + 'A' - 1) + "\n");
							mesText.append("请继续选择");
							state.addition --;
						}
						else {
							//move
							Point a1 = state.getSelectPiece();
							Point a2 = new Point(previewPiece.getlocX(), previewPiece.getlocX());
							formalPiece[a1.x][a1.y].setEmpty();
							formalPiece[a2.x][a2.y].setPiece(enemyColor);
							
							a1 = transToBack(a1);
							a2 = transToBack(a2);
							Board.setBoard(a1.x,a1.y,Board.EMPTY);
							Board.setBoard(a2.x,a2.y,Board.ENEMY);
							qipuText.append((selfColor == Color.white?"W":"B") + state.addition + "：" + a1.x + (char)('A' + a1.y - 1) + "->" + a2.x + (char)('A' + a2.y - 1) + "\n");
							state.round = Round.SELF;
							mesText.setText("行棋阶段：\\n轮到电脑下！\\n请等待...\\n");
						}
						break;
					case JUMPINGJ:
						
						break;
					case FANGEATING:
						
						break;
					case FLYMOVE:
						
						break;
					default:
						break;
					}
					if(state.round == Round.SELF) {
						if(state.selfPiece > MainFrame.boardSize)
						{
							//正常跳吃
			  	  			state.addition ++;
			  	  			BestEat bat = new BestEat();
			  	  			//初始点
			  				Point p = bat.getBestPoint();
			  				Board.setBoard(p.x, p.y, Board.EMPTY);
			  				Point a = transToFront(p);
			  				formalPiece[a.x][a.y].setEmpty();
			  				

			  				qipuText.append((selfColor == Color.white?"W":"B") + state.addition + "：" + p.x + (char)('A' + p.y - 1));
			  				
			  				Point last = null;
			  				for(Point i : bat.getPastPoint()) {
			  					qipuText.append("->" + i.x + (char)('A' + i.y - 1));
			  					last = i;
			  				}

			  				qipuText.append("\n");
			  				Board.setBoard(last.x, last.y, Board.SELF);
			  				a = transToFront(last);
			  				formalPiece[a.x][a.y].setPiece(selfColor);
			  				
			  				
			  				ArrayList<Point> eat = bat.getEatPoint();
			  				ArrayList<Point> feat = bat.getfangEatPoint();
			  				if(!eat.isEmpty()) {
			  					qipuText.append("TC:");
			  					for(Point i : eat) {
			  						qipuText.append("  " + i.x + (char)('A' + i.y - 1));
			  						Board.setBoard(i.x,i.y, Board.EMPTY);
			  						a = transToFront(i);
			  						formalPiece[a.x][a.y].setEmpty();
			  						state.enemyPiece --;
			  						state.emptyPiece ++;
			  					}
			  					qipuText.append("\n");
			  				}
			  				
			  				if(!feat.isEmpty()) {
			  					qipuText.append("FC:");
			  					for(Point i : feat) {
			  						qipuText.append("  " + i.x + (char)('A' + i.y - 1));
			  						Board.setBoard(i.x,i.y, Board.EMPTY);
			  						a = transToFront(i);
			  						formalPiece[a.x][a.y].setEmpty();
			  						state.enemyPiece --;
			  						state.emptyPiece ++;
			  					}
			  					qipuText.append("\n");
			  				}
			  				bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
				  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
				  					"，空子数：" + state.emptyPiece + "。");
			  				mesText.setText("行棋阶段：\n轮到你了！\n请选择要走的棋子\\n");
			  				state.eatStage = EatStage.SELECTING;
			  				state.round = Round.ENEMY;
							
						}
						else {
							//飞子
							//行棋第一步
			  	  			state.addition ++;
			  	  			FlyStep bat = new FlyStep();
			  	  			//初始点
			  				Point p = bat.getBestPoint();
			  				Board.setBoard(p.x, p.y, Board.EMPTY);
			  				Point a = transToFront(p);
			  				formalPiece[a.x][a.y].setEmpty();
			  				

			  				qipuText.append((selfColor == Color.white?"W":"B") + state.addition + "：" + p.x + (char)('A' + p.y - 1));
			  				
			  				Point last = null;
			  				for(Point i : bat.getPastPoint()) {
			  					qipuText.append("->" + i.x + (char)('A' + i.y - 1));
			  					last = i;
			  				}

			  				qipuText.append("\n");
			  				Board.setBoard(last.x, last.y, Board.SELF);
			  				a = transToFront(last);
			  				formalPiece[a.x][a.y].setPiece(selfColor);
			  				
			  				
			  				ArrayList<Point> eat = bat.getEatPoint();
			  				ArrayList<Point> feat = bat.getfangEatPoint();
			  				if(!eat.isEmpty()) {
			  					qipuText.append("TC:");
			  					for(Point i : eat) {
			  						qipuText.append("  " + i.x + (char)('A' + i.y - 1));
			  						Board.setBoard(i.x,i.y, Board.EMPTY);
			  						a = transToFront(i);
			  						formalPiece[a.x][a.y].setEmpty();
			  						state.enemyPiece --;
			  						state.emptyPiece ++;
			  					}
			  					qipuText.append("\n");
			  				}
			  				
			  				if(!feat.isEmpty()) {
			  					qipuText.append("FC:");
			  					for(Point i : feat) {
			  						qipuText.append("  " + i.x + (char)('A' + i.y - 1));
			  						Board.setBoard(i.x,i.y, Board.EMPTY);
			  						a = transToFront(i);
			  						formalPiece[a.x][a.y].setEmpty();
			  						state.enemyPiece --;
			  						state.emptyPiece ++;
			  					}
			  					qipuText.append("\n");
			  				}
			  				bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
				  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
				  					"，空子数：" + state.emptyPiece + "。");
			  				mesText.setText("行棋阶段：\n轮到你了！\n请选择要走的棋子\\n");
			  				state.eatStage = EatStage.SELECTING;
			  				state.round = Round.ENEMY;
						}
					}
					
				}//行棋阶段结束
				
				qipuText.setCaretPosition(qipuText.getText().length());
				Board.display();
			}//click_end
		};
		previewPiece.addMouseListener(previewListener);
		add(previewPiece);
		
		//实体棋子
		for(int i = 1; i < MainFrame.boardSize + 1; i ++) {
			for(int j = 1; j < MainFrame.boardSize + 1;j ++) {
				formalPiece[i][j] = new formalPiece(owner,this);
				//实体点，鼠标位置
				Point o = grid_mouseConverse(i, j);
				formalPiece[i][j].setLocation(o.x,o.y);
				formalPiece[i][j].setloc(i, j);
				add(formalPiece[i][j]);
				int ti = i;
				int tj = j;
				/**“在正式棋子上显示预览棋子”所做的监听器*/
				MouseListener l = new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					//启用
					@Override
					public void mouseEntered(MouseEvent e) {
						if(state.eatStage == EatStage.SELECTING && getPieceState(ti, tj) == Board.ENEMY) {
							previewPiece.setLocation(o.x,o.y);
							previewPiece.setloc(ti, tj);
							previewPiece.setVisible(true);
						}
						else if(state.eatStage == EatStage.FANGEATING && getPieceState(ti, tj) == Board.SELF){
							previewPiece.setLocation(o.x,o.y);
							previewPiece.setloc(ti, tj);
							previewPiece.setVisible(true);
						}
						else if((state.eatStage == EatStage.JUMPING || state.eatStage == EatStage.FLYMOVE) && ti == state.getSelectPiece().x && tj == state.getSelectPiece().y){
							//取消
							previewPiece.setLocation(o.x,o.y);
							previewPiece.setloc(ti, tj);
							previewPiece.setVisible(true);
						}
						else previewPiece.setVisible(false);
					}
					@Override
					public void mouseClicked(MouseEvent e) {}
				};
				
				formalPiece[i][j].addMouseListener(l);
				
			}
		}
		formalPiece[gridrow / 2][gridrow / 2].setPiece(selfColor);
		formalPiece[gridrow / 2 + 1][gridrow / 2 + 1].setPiece(enemyColor);

		
		
	}
	
	

	//参数
	public static final int gridSize = 40;
	public static final int letterSize = 20;
	public static final int chessSize = 30;
	public static final int marginWidth = 40;
	
	
	private Label l1 = new Label();										//test
	private Label l2 = new Label();										//test
	/**预览棋子*/
	private Piece previewPiece;
	/**正式棋子*/
	public formalPiece formalPiece[][] = new formalPiece[MainFrame.boardSize + 1][MainFrame.boardSize + 1];
	
	
	private int gridrow;
	private int size;
	private int boardSize;
	private int x_indentation;											//特殊偏移量
	private int y_indentation;
	private MainFrame ownerFrame;
	private Color selfColor;
	private Color enemyColor;
	private JTextArea qipuText;
	private JTextArea mesText;
	private ChessState state;
	private JLabel bottom;
	
	/**
	 * 绘制棋盘的外观
	 */
	public void paint(Graphics g){
		Graphics a = g.create();
		int q = 0;
		//横线
		for(int i = y_indentation; i <= boardSize + y_indentation; i += gridSize){
			a.drawLine(x_indentation, i, boardSize + x_indentation, i);				//画线
			a.drawString(String.valueOf((char)('A' + q)),marginWidth,i + 5);		//坐标,相对，可改
			q++;
		}
		q = 0;
		//竖线
		for(int j = x_indentation; j <= boardSize + x_indentation; j += gridSize){
			a.drawLine(j, y_indentation, j, boardSize + y_indentation);				//画线
			a.drawString(String.valueOf(1 + q),j ,size - marginWidth);				//坐标,相对，可改
			q++;
		}
		a.drawLine(boardSize/2 - gridSize/2 + x_indentation,
				   boardSize/2 - gridSize/2 + y_indentation,
				   boardSize/2 + gridSize/2 + x_indentation,
				   boardSize/2 + gridSize/2 + y_indentation);
		a.fillOval(boardSize/2 - gridSize/2 + x_indentation - 4,boardSize/2 - gridSize/2 + y_indentation - 4,8,8);
		a.fillOval(boardSize/2 + gridSize/2 + x_indentation - 4,boardSize/2 + gridSize/2 + y_indentation - 4,8,8);
		a.dispose();
	}
	
	
	//棋盘作为监听器
	//Source:CenterBoard
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {}
	//启用
	@Override
	public void mouseMoved(MouseEvent e) {
		int px = e.getX() - letterSize - marginWidth;
		int py = e.getY() - marginWidth;
		l1.setText("x = " + String.valueOf(px));
		l2.setText("y = " + String.valueOf(py));
		if(isTempVisible(e.getX(), e.getY())) {
			previewPiece.setLocation(px/gridSize * gridSize + letterSize + marginWidth, py/gridSize * gridSize + marginWidth);
			previewPiece.setloc(px/gridSize + 1, py/gridSize + 1);		//若没旋转棋面
			previewPiece.setVisible(true);
		}
		else previewPiece.setVisible(false);
	}
	
	
	/**
	 * 判断是否使预览棋子显现
	 * @param px chessSize + boardSize中的实际坐标X
	 * @param py chessSize + boardSize中的实际坐标Y
	 * @return
	 */
	private boolean isTempVisible(int px, int py) {
		int x = px - letterSize - marginWidth;
		int y = py - marginWidth;
		if(ownerFrame.state.round == Round.SELF) return false;								//回合判定
		else if(x < 0 ||x > boardSize + chessSize || y < 0 || y > boardSize + chessSize) return false;	//棋盘外边界判定
		else if(x % gridSize > chessSize || y % gridSize > chessSize ) return false;					//棋盘内blank判定
		else {
			Point tempPoint = mouse_gridConverse(px, py);
			int piecestate = getPieceState(tempPoint.x, tempPoint.y);
			
			
//			//V1 有点慢，有点麻烦
//			if(state.stage == Stage.OPEN) {
//				if(piecestate == Board.EMPTY) return true;
//				else return false;
//			}
//			else {
//				if(state.eatStage == EatStage.NOTEATING) {
//					qipuText.append("-------\n" +
//							ErrorTracker.getTestMessage() +
//							"ERROR_stage与eatStage不匹配\n" +
//							"--------\n");
//					return false;
//				}
//				//在选择阶段，选择敌方棋
//				else if(state.eatStage == EatStage.SELECTING && piecestate == Board.ENEMY) return true;
//				//在跳棋阶段，可行的空点
//				else if(state.eatStage == EatStage.JUMPING && piecestate == Board.EMPTY) 
//				{
//					Point temp = ownerFrame.state.getJumpPiece();
//					if(
//							(temp.x == tempPoint.x && temp.y == tempPoint.y + 2 && getPieceState(temp.x, temp.y + 1) == Board.SELF)||
//							(temp.x == tempPoint.x && temp.y == tempPoint.y - 2 && getPieceState(temp.x, temp.y - 1) == Board.SELF)||
//							(temp.x == tempPoint.x + 2 && temp.y == tempPoint.y && getPieceState(temp.x + 1, temp.y) == Board.SELF)||
//							(temp.x == tempPoint.x - 2 && temp.y == tempPoint.y && getPieceState(temp.x - 1, temp.y) == Board.SELF)
//					) return true;
//				}
//				//方吃阶段，选择了我方棋子
//				else if(state.eatStage == EatStage.FANGEATING && piecestate == Board.SELF) return true;
//			}
			
			
			//V2 简单粗暴
			if (piecestate != Board.EMPTY) return false;
			else {
				if(state.stage == Stage.OPEN) return true;
				else if(state.eatStage == EatStage.NOTEATING) {
					//发生BUG
					qipuText.append("-------\n" +
							ErrorTracker.getTestMessage() +
							"ERROR_stage与eatStage不匹配\n" +
							"--------\n");
					return false;
				}
				else if(state.eatStage == EatStage.JUMPING) {
					Point JumpPiecetemp = state.getSelectPiece();
					if(
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y + 2 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y + 1) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y - 2 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y - 1) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x + 2 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x + 1, JumpPiecetemp.y) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x - 2 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x - 1, JumpPiecetemp.y) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y + 1 )||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y - 1 )||
							(JumpPiecetemp.x == tempPoint.x + 1 && JumpPiecetemp.y == tempPoint.y )||
							(JumpPiecetemp.x == tempPoint.x - 1 && JumpPiecetemp.y == tempPoint.y )
					) return true;
				}
				else if(state.eatStage == EatStage.JUMPINGJ) {
					Point JumpPiecetemp = state.getSelectPiece();
					if(
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y + 2 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y + 1) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y - 2 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y - 1) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x + 2 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x + 1, JumpPiecetemp.y) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x - 2 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x - 1, JumpPiecetemp.y) == Board.SELF)
							
					) return true;
				}
				else if(state.eatStage == EatStage.FLYMOVE) {
					return true;
				}
			}
			
			
			return false;
		}
	}
	
	/**
	 * 获取指定坐标的状态
	 * @param x 棋盘上的坐标X
	 * @param y 棋盘上的坐标Y
	 * @return 状态(import:Board)
	 */
	private int getPieceState(int x, int y) {
		if(x <= 0 || x > MainFrame.boardSize || y <= 0 || y > MainFrame.boardSize) return Board.OUTER;
		if(!formalPiece[x][y].isVisible()) return Board.EMPTY;
		Color a= formalPiece[x][y].getColor();
		if(a == enemyColor) return Board.ENEMY;
		else if((a == selfColor))return Board.SELF;
		
		//test
		qipuText.append("-------\n" +
				ErrorTracker.getTestMessage() +
				"无相同的颜色" +
				"--------\n");
		return 1000;
	}
	
	/**
	 * 将鼠标事件坐标转化为棋盘坐标
	 * @param x
	 * @param y
	 * @return
	 */
	private Point mouse_gridConverse(int x, int y) {
		return new Point((x - letterSize - marginWidth)/gridSize + 1, (y - marginWidth)/gridSize + 1);
	}

	/**
	 * 将棋盘坐标转化为鼠标事件坐标
	 * @param x 棋盘坐标x
	 * @param y 棋盘坐标y
	 * @return 鼠标事件坐标
	 */
	private Point grid_mouseConverse(int x, int y) {
		return new Point((x - 1)*gridSize + marginWidth + letterSize, (y - 1)*gridSize + marginWidth);
	}
	
	/**
	 * 将Board中的点转化为UI界面上的点
	 * @param backPoint
	 * @return
	 */
	private Point transToFront (Point backPoint) {
		switch (ownerFrame.rotateA) {
		case 0:
			return backPoint;
		case 1:
			return backPoint.clockWise();
		case 2:
			return backPoint.Centrosymmetric();
		case 3:
			return backPoint.NiclockWise();
		default:
			return backPoint;
		}
	}
	
	/**
	 * 将UI界面上的点转化为Board中的点
	 * @param backPoint
	 * @return
	 */
	private Point transToBack (Point FrontPoint) {
		switch (ownerFrame.rotateA) {
		case 0:
			return FrontPoint;
		case 1:
			return FrontPoint.NiclockWise();
		case 2:
			return FrontPoint.Centrosymmetric();
		case 3:
			return FrontPoint.clockWise();
		default:
			return FrontPoint;
		}
	}
	
	
	/**
	 * 主要在外部调用
	 * */
	public void setPiece(int x, int y, int t) {
		switch (t) {
		case Board.EMPTY:
			formalPiece[x][y].setEmpty();
			break;
		case Board.SELF:
			formalPiece[x][y].setPiece(selfColor);
			break;
		case Board.ENEMY:
			formalPiece[x][y].setPiece(enemyColor);
			break;
		}
		
	}
	
	
	/**
	 * 棋子的类
	 *color 0为黑棋，1为白棋
	 *棋子直径为30
	 */
	private class Piece extends Canvas {
		private static final long serialVersionUID = 7417352254643663272L;

		public Piece(MainFrame owner, Component parentComponent, Color color) {
			super();
//			setBackground(new Color(177,120,68));
			setSize(30, 30);
			this.tempcolor = color;
		}
		
		
		protected Color tempcolor;
		/**为棋盘上的坐标，不为实际坐标*/
		protected int loc_x;
		/**为棋盘上的坐标，不为实际坐标*/
		protected int loc_y;
		
		public void paint(Graphics g) {
			Graphics a = g.create();
			a.setColor(tempcolor);
			a.fillOval(0,0,30,30);
			a.dispose();
		}
		
		public Color getColor() {
			return tempcolor;
		}
		
		public void setloc (int x,int y) {
			loc_x = x;
			loc_y = y;
		}
		
		public int getlocX() {
			return loc_x;
		}
		
		public int getlocY() {
			return loc_y;
		}
	}
	
	
	/**
	 * 实体棋的类
	 * 默认为黑色
	 */
	private class formalPiece extends Piece{
		private static final long serialVersionUID = 3724233875572917594L;

		public formalPiece(MainFrame owner, Component parentComponent) {
			super(owner, parentComponent, Color.white);
			setVisible(false);
		}
		
		public void setColor(Color a) {
			this.tempcolor = a;
			repaint();
		}
		
		public void setPiece(Color a) {
			setColor(a);
			setVisible(true);
		}
		
		
		public void setEmpty() {
			setVisible(false);
		}
	}

		

}
