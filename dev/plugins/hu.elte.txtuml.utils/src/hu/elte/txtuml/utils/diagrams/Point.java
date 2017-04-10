package hu.elte.txtuml.utils.diagrams;

public class Point {
	private int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
}
