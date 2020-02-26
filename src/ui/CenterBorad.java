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
import java.util.Random;

import javax.swing.JPanel;

import model.Board;
import model.Point;
import opening.Open;

public class CenterBorad extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = -1602322585809545383L;
	/**
	 * 创建棋盘类
	 * @param row 行数/2
	 */
	public CenterBorad(MainFrame owner, Component parentComponent, int row) {
		Open.init();
		Random rand = new Random();
		setLayout(null);
		setFont(new Font("微软雅黑", Font.BOLD, 16));
		ownerFrame = owner;
		this.selfColor = owner.selfColor;
		this.enemyColor = owner.enemyColor;
		
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
					@Override
					public void mouseEntered(MouseEvent e) {
//						if(ownerFrame.state.eatStage == chessState.EatStage.SELECTING && Board.getBoard(ti, tj) == Board.ENEMY) {
						if(ownerFrame.state.eatStage == ChessState.EatStage.SELECTING && getPieceState(ti, tj) == Board.ENEMY) {
							previewPiece.setLocation(o.x,o.y);
							previewPiece.setVisible(true);
						}
//						else if(ownerFrame.state.eatStage == chessState.EatStage.FANGEATING && Board.getBoard(ti, tj) == Board.SELF){
						else if(ownerFrame.state.eatStage == ChessState.EatStage.FANGEATING && getPieceState(ti, tj) == Board.SELF){
							previewPiece.setLocation(o.x,o.y);
							previewPiece.setVisible(true);
						}
						else previewPiece.setVisible(false);
					}
					@Override
					public void mouseClicked(MouseEvent e) {}
				};
				
				formalPiece[i][j].addMouseListener(l);
				
//				formalPiece[i][j].setVisible(true);
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
		if(ownerFrame.state.round == ChessState.Round.SELF) return false;								//回合判定
		else if(x < 0 ||x > boardSize + chessSize || y < 0 || y > boardSize + chessSize) return false;	//棋盘外边界判定
		else if(x % gridSize > chessSize || y % gridSize > chessSize ) return false;					//棋盘内blank判定
		else {
			Point tempPoint = mouse_gridConverse(px, py);
			int piecestate = getPieceState(tempPoint.x, tempPoint.y);
			
			
//			//V1 有点慢，有点麻烦
//			if(ownerFrame.state.stage == chessState.Stage.OPEN) {
//				if(piecestate == Board.EMPTY) return true;
//				else return false;
//			}
//			else {
//				if(ownerFrame.state.eatStage == chessState.EatStage.NOTEATING) {
//					ownerFrame.mainPanel.rightText.messageArea.setText("你妈炸了！ERROR_stage与eatStage不匹配");
//					return false;
//				}
//				//在选择阶段，选择敌方棋
//				else if(ownerFrame.state.eatStage == chessState.EatStage.SELECTING && piecestate == Board.ENEMY) return true;
//				//在跳棋阶段，可行的空点
//				else if(ownerFrame.state.eatStage == chessState.EatStage.JUMPING && piecestate == Board.EMPTY) 
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
//				else if(ownerFrame.state.eatStage == chessState.EatStage.FANGEATING && piecestate == Board.SELF) return true;
//			}
			
			
			//V2 简单粗暴
			if (piecestate != Board.EMPTY) return false;
			else {
				if(ownerFrame.state.stage == ChessState.Stage.OPEN) return true;
				else if(ownerFrame.state.eatStage == ChessState.EatStage.NOTEATING) {
					ownerFrame.mainPanel.rightText.messageArea.setText("你妈炸了！ERROR_stage与eatStage不匹配");
					return false;
				}
				else if(ownerFrame.state.eatStage == ChessState.EatStage.JUMPING) {
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
		ownerFrame.mainPanel.rightText.messageArea.setText("你妈炸了！CenterBoard_getPieceState");
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
	
	
	//接下去都是与Board相关的程序
	/**
	 * 在后台下棋
	 * @param x 前端虚伪的参数x
	 * @param y
	 * @param state
	 */
	private void set_Board(int x, int y, int state){
		switch (ownerFrame.rotateA) {
		case 0:
			Board.setBoard(x, y, state);
			break;
		case 1:
			Board.setBoard(y, gridrow - x + 1, state);
			break;
		case 2:
			Board.setBoard(gridrow - x + 1,gridrow - y + 1, state);
			break;
		case 3:
			Board.setBoard(gridrow - y + 1, x, state);
			break;
		default:
			break;
		}
	}
	private int get_Board(int x,int y) {
		switch (ownerFrame.rotateA) {
		case 0:
			return Board.getBoard(x, y);
		case 1:
			return Board.getBoard(y, gridrow - x + 1);
		case 2:
			return Board.getBoard(gridrow - x + 1,gridrow - y + 1);
		case 3:
			return Board.getBoard(gridrow - y + 1, x);
		default:
			return -1;
		}
	}
	private void set_Board(Point o, int state){
		int x = o.x;
		int y = o.y;
		switch (ownerFrame.rotateA) {
		case 0:
			Board.setBoard(x, y, state);
			break;
		case 1:
			Board.setBoard(y, gridrow - x + 1, state);
			break;
		case 2:
			Board.setBoard(gridrow - x + 1,gridrow - y + 1, state);
			break;
		case 3:
			Board.setBoard(gridrow - y + 1, x, state);
			break;
		default:
			break;
		}
	}
	private int get_Board(Point o) {
		int x = o.x;
		int y = o.y;
		switch (ownerFrame.rotateA) {
		case 0:
			return Board.getBoard(x, y);
		case 1:
			return Board.getBoard(y, gridrow - x + 1);
		case 2:
			return Board.getBoard(gridrow - x + 1,gridrow - y + 1);
		case 3:
			return Board.getBoard(gridrow - y + 1, x);
		default:
			return -1;
		}
	}
	//安全
	
	
	
	
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
			super(owner, parentComponent, Color.black);
			setVisible(false);
//			addMouseListener(l);
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
