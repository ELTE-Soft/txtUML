package hu.elte.txtuml.layout.visualizer.model;

public class Point
{
	// Variables
	
	private Integer _x;
	private Integer _y;
	
	// end Variables
	
	// Getters, setters
	
	public Integer getX()
	{
		return _x;
	}
	
	public void setX(Integer v)
	{
		_x = v;
	}
	
	public Integer getY()
	{
		return _y;
	}
	
	public void setY(Integer v)
	{
		_y = v;
	}
	
	// end Getters, setters
	
	// Ctors
	
	public Point()
	{
		_x = 0;
		_y = 0;
	}
	
	public Point(Integer x, Integer y)
	{
		_x = x;
		_y = y;
	}
	
	public Point(Point p)
	{
		_x = p._x;
		_y = p._y;
	}
	
	// end Ctors
	
	// Statics
	
	public static Point Add(Point p1, Point p2)
	{
		return new Point(p1._x + p2._x, p1._y + p2._y);
	}
	
	public static Point Substract(Point p1, Point p2)
	{
		return new Point(p1._x - p2._x, p1._y - p2._y);
	}
	
	public static boolean Equals(Point p1, Point p2)
	{
		return p1.equals(p2);
	}
	
	public static Point Add(Point p1, Direction dir)
	{
		switch (dir)
		{
			case north:
				return Point.Add(p1, new Point(0, 1));
			case east:
				return Point.Add(p1, new Point(1, 0));
			case south:
				return Point.Add(p1, new Point(0, -1));
			case west:
				return Point.Add(p1, new Point(-1, 0));
			default:
				return null;
		}
	}
	
	public static Point Multiply(Point p1, Integer m)
	{
		return new Point(p1._x * m, p1._y * m);
	}
	
	public static Point Multiply(Direction dir, Integer m)
	{
		return new Point(Add(new Point(0, 0), dir)._x * m, Add(new Point(0, 0), dir)._y
				* m);
	}
	
	// end Statics
	
	// Equality
	
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
		if (this instanceof Point && obj instanceof Point)
		{
			Point p1 = (Point) this;
			Point p2 = (Point) obj;
			return p1._x.equals(p2._x) && p1._y.equals(p2._y);
		}
		
		return false;
	}
	
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _x.hashCode();
		result = prime * result + _y.hashCode();
		return result;
	}
	
	// end Equality
	
	// Methods
	
	public DoublePoint toDoublePoint()
	{
		return new DoublePoint(_x, _y);
	}
	
	public Object clone()
	{
		return new Point(_x, _y);
	}
	
	public String toString()
	{
		return "(" + _x.toString() + ", " + _y.toString() + ")";
	}
	
	// end Methods
}
