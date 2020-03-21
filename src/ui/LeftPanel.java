package ui;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;

import ui.FontImport.SourceHanSansCN;

public class LeftPanel extends JPanel{
	private static final long serialVersionUID = 143401199332655953L;

	public LeftPanel(MainFrame owner,Component parentComponent) {
		super();
		setLayout(null);
		setSize(sizeW, sizeH);
		Font tempfont = FontImport.fzxkbxkjFont().deriveFont(Font.PLAIN,30);
		
		restart.setBounds(0, 0, sizeW, 75);
		restart.setFont(tempfont);
		
		huiqi.setBounds(0, buttonH + buttonInterval, sizeW, 75);
		huiqi.setFont(tempfont);
		
		rotButton.setBounds(0, (buttonH + buttonInterval) * 2, sizeW, 75);
		rotButton.setFont(tempfont);
		
		endEating.setBounds(0, (buttonH + buttonInterval) * 3, sizeW, 75);
		endEating.setFont(tempfont);
		endEating.setVisible(false);
		
		add(huiqi);
		add(restart);
		add(endEating);
		add(rotButton);
	}
	
	public final static int buttonH = 75;
	public final static int buttonW = 150;
	public final static int buttonInterval = 10;
	
	public JButton huiqi = new JButton("悔棋");
	public JButton restart = new JButton("重新开始");
	public JButton endEating = new JButton("吃子结束");
	public JButton rotButton  = new JButton("旋转棋面");
	
	private int sizeH = buttonH * 4 + buttonInterval * 3;
	private int sizeW = buttonW;
	
	
}
