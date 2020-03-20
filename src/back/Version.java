package back;

import java.util.ArrayList;
import java.util.Stack;

import model.Point;
import ui.ChessState;

public class Version {
	private ChessState a;
	private int[][] b;
	private String c;
	private String d;
	
	private Stack<ArrayList<Point>> e;
	private int[][] f;
	private int[][] g;
	
	public Version(
			ChessState state,
			int[][] board,
			String mesText,
			String qipuText,
			Stack<ArrayList<Point>>open_defenseAddition,
			int[][] open_square,
			int[][] open_attackScores
			) {
		a = state;
		b = board;
		c = mesText;
		d = qipuText;
		e = open_defenseAddition;
		f = open_square;
		g = open_attackScores;
	}
	
	public ChessState getChessState() {
		return a;
	}
	
	public int[][] getBoard(){
		return b;
	}
	
	public String getMesText() {
		return c;
	}
	
	public String getQipuText() {
		return d;
	}
	
	public Stack<ArrayList<Point>> getDefenseAddition(){
		return e;
	}
	
	public int[][] getSquare() {
		return f;
	}
	
	public int[][] getAttackScore(){
		return g;
	}
	
}
