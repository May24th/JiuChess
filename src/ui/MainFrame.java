package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import model.Board;
import opening.Open;

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
	}
	
	public static final int boardSize = 8;
	static final int FrameSizeW = 900;
	static final int FrameSizeH = 900;
	
	
	public ChessState state;
	public int rotateA = 0;					//0代表原始，1~3代表顺时针旋转90°~270°
	public Color enemyColor = Color.black;	//黑0，白1
	public Color selfColor = Color.white;	//黑0，白1
	
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
		    
		    
		    /**中心棋盘*/
		    centerBoard = new CenterBorad(owner,this,boardSize/2);
		    centerBoard.setLocation((int) (FrameSizeW/2 - centerBoard.getSize().getWidth()/2), 200);	//居中
		    add(centerBoard);
		    
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
		    
		    display();
		}
		
		public void display() {
			String c = "E";
			for(int i = 1; i <= Board.maxIndex; i++) {
				System.out.printf("%2d ",i);
				for(int j = 1; j <= Board.maxIndex; j++) {
					if(Board.getBoard(i, j) == Board.EMPTY) c = "·";
					if(Board.getBoard(i, j) == Board.ENEMY) c = "○";
					if(Board.getBoard(i, j) == Board.SELF) c = "●";
					System.out.print(c + " ");
				}
				System.out.println();
			}
			System.out.print("   ");
			for(int j = 0; j < Board.maxIndex; j++) {
				System.out.print((char)(j + 'A') + " ");
			}
			System.out.println();
			System.out.println();
			System.out.println();
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
