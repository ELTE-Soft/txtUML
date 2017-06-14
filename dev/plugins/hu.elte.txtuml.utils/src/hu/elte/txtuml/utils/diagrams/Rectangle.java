package hu.elte.txtuml.utils.diagrams;

public class Rectangle {
	private int x, y, width, height;

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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

	public int width() {
		return this.width;
	}
	

	public int height() {
		return this.height;
	}

	public Point getTopLeft() {
		return new Point(x, y);
	}
}
