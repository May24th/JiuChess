package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import model.Board;
import model.Point;
import opening.Open;
import ui.ChessState.Round;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 5520531127962754351L;

	public MainFrame() {
		super();
		state = new ChessState();
		setSize(FrameSizeW, FrameSizeH);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
//	    showOpenDialog(this, this);
		mainPanel = new conPane(this, this);
	    setContentPane(mainPanel);
	    revalidate();
	    
	    //开局
  		Open.init();
  		mainPanel.rightText.messageArea.append("初始化完成！\n");
  		mainPanel.rightText.qipuArea.append("欢迎来到久棋对战平台...\n------------行棋阶段------------\n");
  		mainPanel.rightText.qipuArea.append("W1：4D\n");
  		mainPanel.rightText.qipuArea.append("B2：5E\n");
  		
  		if(selfColor == Color.white) {
  			mainPanel.rightText.messageArea.setText("布局阶段：\n轮到电脑下！\n请等待...\n");
  			state.round = Round.SELF;
  			ArrayList<Point> tArrayList = Open.getBestPoints_v3();
  			int a = new Random().nextInt(tArrayList.size());
  			Open.play(tArrayList.get(a).x, tArrayList.get(a).y, Board.SELF);
  			mainPanel.centerBoard.setPiece(tArrayList.get(a).x, tArrayList.get(a).y, Board.SELF);
  			state.selfPiece ++;
  			state.emptyPiece --;
  			state.round = Round.ENEMY;
  			state.addition ++;
  			mainPanel.rightText.qipuArea.append((selfColor == Color.black? "B":"W") + state.addition +
  					"：" + tArrayList.get(a).x + String.valueOf((char)(tArrayList.get(a).y - 1 + 'A')) + "\n");
  			mainPanel.bottomLabel.setText("当前棋盘上黑子数：" + state.enemyPiece + "，白子数：" + state.selfPiece + "，空子数：" + state.emptyPiece + "。");
  		}
  		mainPanel.rightText.messageArea.setText("布局阶段：\n轮到你了！\n");
  		
	}
	
	public static final int boardSize = 8;
	static final int FrameSizeW = 900;
	static final int FrameSizeH = 900;
	
	
	public ChessState state;
	public int rotateA = 0;					//0代表原始，1~3代表顺时针旋转90°~270°
	public Color enemyColor = Color.black;
	public Color selfColor = Color.white;
	
	//组件
	public conPane mainPanel;
	
	/**
	 * 显示开局的对话框
	 *
	 * @param owner 对话框的拥有者
	 * @param parentComponent 对话框的父级组件
	 */
	private void showOpenDialog(MainFrame owner, Component parentComponent) {
	    // 创建一个模态对话框
	    final JDialog dialog = new JDialog(owner, "开局", true);
	    // 设置对话框的宽高
	    dialog.setSize(250, 125);
	    // 设置对话框大小不可改变
	    dialog.setResizable(false);
	    // 设置对话框相对显示的位置
	    dialog.setLocationRelativeTo(parentComponent);
	
	    // 创建一个标签显示消息内容
	    JLabel messageLabel = new JLabel("请选择黑子或白子");
	    
	    // 创建两个选项用于选择
	    JRadioButton black = new JRadioButton("黑子");
	    JRadioButton white = new JRadioButton("白子");
	    ButtonGroup group = new ButtonGroup();
	    group.add(black);
	    group.add(white);
	    black.setSelected(true);//默认黑子选中
	    
	    // 创建一个按钮用于关闭对话框
	    JButton okBtn = new JButton("确定");
	    okBtn.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // 关闭对话框
	            if(black.isSelected()) {
	            	enemyColor = Color.black;
	            	selfColor = Color.white;
	            }
	            else {
	            	enemyColor = Color.white;
	            	selfColor = Color.black;
	            }
	            dialog.dispose();
	            owner.remove(dialog);
	            owner.repaint();
	        }
	    });
	
	    // 创建对话框的内容面板
	    JPanel panel = new JPanel();
	
	    // 添加组件到面板
	    panel.add(messageLabel);
	    panel.add(black);
	    panel.add(white);
	    panel.add(okBtn);
	
	    // 设置对话框的内容面板
	    dialog.setContentPane(panel);
	    // 显示对话框
	    dialog.setVisible(true);
	}

	
	/**
	 * 初始化
	 * 用于restart
	 */
	public void mainframeInit() {
		state = new ChessState();
		rotateA = 0;
		enemyColor = Color.black;
		selfColor = Color.white;
	}
	
	/**
	 * 主要的UI界面，实现普通main功能
	 * 
	 */
	public class conPane extends JPanel{
		
		private static final long serialVersionUID = -2227702225492644207L;

		public conPane(MainFrame owner,Component parentComponent) {
			super(null);
			
			/**testJLabel*/
			test = new JLabel((enemyColor == Color.white?"白棋":"黑棋"),SwingConstants.CENTER);
			test.setFont(new Font("微软雅黑", Font.BOLD, 70));
			test.setBounds((FrameSizeW - 300)/2, 10, 300, 100);
			test.setOpaque(true);
			test.setBackground(Color.gray);
		    add(test);
		    lx.setBounds(0, 0, 100, 20);
			ly.setBounds(0, 20, 100, 20);
			add(lx);
			add(ly);
			addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseMoved(MouseEvent e) {
					lx.setText("x = " + String.valueOf(e.getX()));
					ly.setText("y = " + String.valueOf(e.getY()));
				}
				@Override
				public void mouseDragged(MouseEvent e) {}
			});
		    
		    
		    /**左边组件*/
		    leftButton = new LeftPanel(owner, this);
		    leftButton.setLocation(75, 250);
		    add(leftButton);
		    
		    /**右边组件*/
		    rightText = new RightPanel(owner, this);
		    rightText.setLocation(635, 150);
		    add(rightText);
		    
		    /**底部组件*/
		    bottomLabel = new JLabel("当前棋盘上黑子数：" + state.enemyPiece + "，白子数：" + state.selfPiece + "。",SwingConstants.CENTER);
			bottomLabel.setBounds(75, 600, 500, 50);
			bottomLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
			bottomLabel.setOpaque(true);
			bottomLabel.setBackground(Color.gray);
		    add(bottomLabel);
		    
		    /**中心棋盘*/
		    centerBoard = new CenterBorad(owner,this,boardSize/2);
		    centerBoard.setLocation((int) (FrameSizeW/2 - centerBoard.getSize().getWidth()/2), 200);	//居中
		    add(centerBoard);
		    
		    Board.display();
		}
		
		
		
		
		//组件
		
		public CenterBorad centerBoard;
		public LeftPanel leftButton;
		public RightPanel rightText;
		public JLabel bottomLabel;
		
		//test
		private Label lx = new Label();						//
		private Label ly = new Label();						//
		public JLabel test;									//
	}

	

}
