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
		previewPiece.addMouseListener(
				//预览棋子监听器	
				new MouseListener() {
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
							Point a;
							switch (ownerFrame.rotateA) {
							case 0:
								a = new Point(previewPiece.getlocX(), previewPiece.getlocY());
								break;
							case 1:
								a = (new Point(previewPiece.getlocX(), previewPiece.getlocY())).NiclockWise();
								break;
							case 2:
								a = (new Point(previewPiece.getlocX(), previewPiece.getlocY())).Centrosymmetric();
								break;
							case 3:
								a = (new Point(previewPiece.getlocX(), previewPiece.getlocY())).clockWise();
							default:
								a = new Point(previewPiece.getlocX(), previewPiece.getlocY());
								break;
							}
							Open.play(a.x, a.y, Board.ENEMY);
							formalPiece[previewPiece.getlocX()][previewPiece.getlocY()].setPiece(enemyColor);
							state.enemyPiece ++;
				  			state.emptyPiece --;
				  			state.round = Round.SELF;
				  			state.addition ++;
				  			qipuText.append((enemyColor == Color.black? "B":"W") + state.addition +
				  					"：" + previewPiece.getlocX() + String.valueOf((char)(previewPiece.getlocY() - 1 + 'A')) + "\n");
				  			bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
				  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
				  					"，空子数：" + state.emptyPiece + "。");
				  			
				  			if(state.emptyPiece > 0) {
				  				mesText.setText("布局阶段：\n轮到电脑下！\n请等待...\n");
				  				//还在开局
				  	  			state.round = Round.SELF;
				  	  			ArrayList<Point> tArrayList = Open.getBestPoints_v3();
				  	  			int a1 = new Random().nextInt(tArrayList.size());
				  	  			Open.play(tArrayList.get(a1).x, tArrayList.get(a1).y, Board.SELF);
					  	  		switch (ownerFrame.rotateA) {
								case 0:
									a = tArrayList.get(a1);
									break;
								case 1:
									a = tArrayList.get(a1).clockWise();
									break;
								case 2:
									a = tArrayList.get(a1).Centrosymmetric();
									break;
								case 3:
									a = tArrayList.get(a1).NiclockWise();
								default:
									a = tArrayList.get(a1);
									break;
								}
				  	  			formalPiece[a.x][a.y].setPiece(selfColor);
				  	  			state.selfPiece ++;
				  	  			state.emptyPiece --;
				  	  			state.addition ++;
				  	  			qipuText.append((selfColor == Color.black? "B":"W") + state.addition +
				  	  					"：" + a.x + String.valueOf((char)(a.y - 1 + 'A')) + "\n");
					  	  		bottom.setText("当前棋盘上黑子数：" + (selfColor == Color.black?state.selfPiece:state.enemyPiece) +
					  					"，白子数：" + (selfColor == Color.white?state.selfPiece:state.enemyPiece) +
					  					"，空子数：" + state.emptyPiece + "。");
					  	  		if(state.emptyPiece == 0) {
					  	  			//进入行棋阶段
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
					  	  			mesText.setText("行棋阶段：\n轮到电脑下！\n请等待...\n");
					  	  			//行棋第一步
					  	  			//！！！
					  	  		}
					  	  		else {
					  	  			//依然在布局
					  	  			state.round = Round.ENEMY;
									mesText.setText("布局阶段：\n轮到你了！\n");
								}
				  	  			
				  			}
				  			else {
				  				//开始行棋
				  				//进入行棋阶段
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
				  			}
							
						}
						else {
							//行棋阶段
							
						}
						
						
						Board.display();
					}
				}
		);
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
							previewPiece.setVisible(true);
						}
						else if(state.eatStage == EatStage.FANGEATING && getPieceState(ti, tj) == Board.SELF){
							previewPiece.setLocation(o.x,o.y);
							previewPiece.setVisible(true);
						}
						else if(state.eatStage == EatStage.FLYSELECTING && getPieceState(ti, tj) == Board.ENEMY) {
							previewPiece.setLocation(o.x,o.y);
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
		// TODO 自动生成的方法存根
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
			
			
			//V1 有点慢，有点麻烦
			if(state.stage == Stage.OPEN) {
				if(piecestate == Board.EMPTY) return true;
				else return false;
			}
			else {
				if(state.eatStage == EatStage.NOTEATING) {
					qipuText.append("-------\n" +
							ErrorTracker.getTestMessage() +
							"ERROR_stage与eatStage不匹配\n" +
							"--------\n");
					return false;
				}
				//在选择阶段，选择敌方棋
				else if(state.eatStage == EatStage.SELECTING && piecestate == Board.ENEMY) return true;
				//在跳棋阶段，可行的空点
				else if(state.eatStage == EatStage.JUMPING && piecestate == Board.EMPTY) 
				{
					Point temp = ownerFrame.state.getJumpPiece();
					if(
							(temp.x == tempPoint.x && temp.y == tempPoint.y + 2 && getPieceState(temp.x, temp.y + 1) == Board.SELF)||
							(temp.x == tempPoint.x && temp.y == tempPoint.y - 2 && getPieceState(temp.x, temp.y - 1) == Board.SELF)||
							(temp.x == tempPoint.x + 2 && temp.y == tempPoint.y && getPieceState(temp.x + 1, temp.y) == Board.SELF)||
							(temp.x == tempPoint.x - 2 && temp.y == tempPoint.y && getPieceState(temp.x - 1, temp.y) == Board.SELF)
					) return true;
				}
				//方吃阶段，选择了我方棋子
				else if(state.eatStage == EatStage.FANGEATING && piecestate == Board.SELF) return true;
			}
			
			
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
					Point JumpPiecetemp = ownerFrame.state.getJumpPiece();
					if(
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y + 2 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y + 1) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y - 2 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y - 1) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x + 2 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x + 1, JumpPiecetemp.y) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x - 2 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x - 1, JumpPiecetemp.y) == Board.SELF)||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y + 1 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y + 1) == Board.EMPTY)||
							(JumpPiecetemp.x == tempPoint.x && JumpPiecetemp.y == tempPoint.y - 1 && getPieceState(JumpPiecetemp.x, JumpPiecetemp.y - 1) == Board.EMPTY)||
							(JumpPiecetemp.x == tempPoint.x + 1 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x + 1, JumpPiecetemp.y) == Board.EMPTY)||
							(JumpPiecetemp.x == tempPoint.x - 1 && JumpPiecetemp.y == tempPoint.y && getPieceState(JumpPiecetemp.x - 1, JumpPiecetemp.y) == Board.EMPTY)
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
