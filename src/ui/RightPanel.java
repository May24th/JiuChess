package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import back.History;
import back.Version;

public class RightPanel extends JPanel implements History{
	private static final long serialVersionUID = -1112354778507113141L;

	public RightPanel(MainFrame owner,Component parentComponent) {
		super();
		setLayout(null);
		Font tempfont = new Font("微软雅黑", Font.BOLD, 20);
		setSize(sizeW,sizeH);
		setBackground(new Color(0xECE2D0));
		
		//消息标签
		xiaoxiLabel = new JLabel("消息：");
		xiaoxiLabel.setBounds(0, 0, sizeW, LabelH);
		xiaoxiLabel.setFont(tempfont);
		
		//消息文本
		messageArea = new JTextArea("初始化中...");
		messageArea.setEditable(false);
		messageArea.setBackground(new Color(0xD5B9B2));
		messageArea.setLineWrap(true);
		messageArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		JScrollPane messageJScrollPane = new JScrollPane(messageArea);
		messageJScrollPane.setBounds(0, LabelH + areaIntervel, sizeW, messageAreaH);
		
		//棋谱标签
		qipuLabel = new JLabel("棋谱：");
		qipuLabel.setBounds(0, LabelH + areaIntervel*2 + messageAreaH + areaIntervel_big, sizeW, LabelH);
		qipuLabel.setFont(tempfont);
		
		//棋谱文本
		qipuArea = new JTextArea();
		qipuArea.setEditable(false);
		qipuArea.setBackground(new Color(0xBFB5AF));
		qipuArea.setLineWrap(true);
		qipuArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		JScrollPane qipuJScrollPane = new JScrollPane(qipuArea);
		qipuJScrollPane.setBounds(0, LabelH*2 + areaIntervel*3 + messageAreaH + areaIntervel_big, sizeW, qipuAreaH);
		
		add(messageJScrollPane);
		add(qipuLabel);
		add(qipuJScrollPane);
		add(xiaoxiLabel);
	}
	
	
	public final static int LabelH = 30;
	public final static int messageAreaH = 110;
	public final static int qipuAreaH = 400;
	public final static int	areaIntervel = 5;
	public final static int	areaIntervel_big = 40;
	
	public JTextArea messageArea;
	public JTextArea qipuArea;
	public JLabel xiaoxiLabel;
	public JLabel qipuLabel;
	
	private int sizeH = LabelH*2 + messageAreaH + qipuAreaH + areaIntervel*3 + areaIntervel_big;
	private int sizeW = 225;

	@Override
	public void pop(Version H) {
		messageArea.setText(H.getMesText());
		qipuArea.setText(H.getQipuText());
	}
	
	public String getMes() {
		return messageArea.getText();
	}
	
	public String getQipu() {
		return qipuArea.getText();
	}
	
}
