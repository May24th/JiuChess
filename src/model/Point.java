package model;


public class Point implements Comparable{
	public int x;
	public int y;
	private int score;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int x, int y, int score) {
		this.x = x;
		this.y = y;
		this.score = score;
	}
	
	
	public Point clone() {
		return new Point(x,y,score);
	}
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	/***
	 * 优先队列降序
	 * 该过了！***
	 */
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Point that = (Point)o;
		if(this.score < that.score) return -1;
		if(this.score > that.score) return 1;
		return 0;
	}
	
	
	public Point clockWise() {
		return new Point(y, Board.maxIndex + 1 - x);
	}
	
	public Point NiclockWise() {
		return new Point(Board.maxIndex + 1 - y, x);
	}
	
	public Point Centrosymmetric() {
		return new Point(Board.maxIndex + 1 - x, Board.maxIndex + 1 - y);
	}
	
	public static Point clockWise(int x,int y) {
		return new Point(y, Board.maxIndex + 1 - x);
	}
	
	public static Point NiclockWise(int x,int y) {
		return new Point(Board.maxIndex + 1 - y, x);
	}
	
	public static Point Centrosymmetric(int x,int y) {
		return new Point(Board.maxIndex + 1 - x, Board.maxIndex + 1 - y);
	}
}
