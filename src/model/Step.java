package model;

import java.util.ArrayList;

import eat.JumpEat;

public class Step {
	public boolean self;
	public boolean enemy;
	
	public Point selfStart;
	public Point selfEnd;
	public ArrayList<Point> atePoint = null;
	
	public Point enemyStart;
	public Point enemyEnd;
	public ArrayList<Point> BeeatenPoint = null;
	
	public Step() {
		self = false;
		enemy = false;
		atePoint = new ArrayList<Point>();
		BeeatenPoint = new ArrayList<Point>();
	}
	
	//仅仅适用于己方一步跳吃
	public Step(Point o,int direction) {
			self = true;
			selfStart = o;
			switch(direction) {
			case JumpEat.UP:selfEnd = new Point(o.x + 2,o.y);
							atePoint.add(new Point(o.x + 1,o.y));
							break;
			case JumpEat.DOWN:selfEnd = new Point(o.x - 2,o.y);
							atePoint.add(new Point(o.x - 1,o.y));
							break;
			case JumpEat.LEFT:selfEnd = new Point(o.x,o.y - 2);
							atePoint.add(new Point(o.x,o.y - 1));
							break;
			case JumpEat.RIGHT:selfEnd = new Point(o.x,o.y + 2);
							atePoint.add(new Point(o.x,o.y + 1));
							break;		
			}
		
	}
}
