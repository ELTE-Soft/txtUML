package hu.elte.txtuml.layout.visualizer.model;

public class DoublePoint
{
	// Variables
	
	// private static final double EPSILON = 1.0E-10;
	
	private Double _x;
	private Double _y;
	
	// end Variables
	
	// Getters, setters
	
	public Double getX()
	{
		return _x;
	}
	
	public void setX(Double v)
	{
		_x = v;
	}
	
	public Double getY()
	{
		return _y;
	}
	
	public void setY(Double v)
	{
		_y = v;
	}
	
	// end Getters, setters
	
	// Ctors
	
	public DoublePoint()
	{
		_x = 0.0;
		_y = 0.0;
	}
	
	public DoublePoint(Integer x, Integer y)
	{
		_x = (double) x;
		_y = (double) y;
	}
	
	public DoublePoint(Double x, Double y)
	{
		_x = x;
		_y = y;
	}
	
	public DoublePoint(DoublePoint p)
	{
		_x = p._x;
		_y = p._y;
	}
	
	// end Ctors
	
	// Statics
	
	public static DoublePoint Add(DoublePoint p1, DoublePoint p2)
	{
		return new DoublePoint(p1._x + p2._x, p1._y + p2._y);
	}
	
	public static DoublePoint Add(DoublePoint p1, Point p2)
	{
		return new DoublePoint(p1._x + p2.getX(), p1._y + p2.getY());
	}
	
	public static DoublePoint Substract(DoublePoint p1, DoublePoint p2)
	{
		return new DoublePoint(p1._x - p2._x, p1._y - p2._y);
	}
	
	public static DoublePoint Substract(DoublePoint p1, Point p2)
	{
		return new DoublePoint(p1._x - p2.getX(), p1._y - p2.getY());
	}
	
	public static boolean Equals(DoublePoint p1, DoublePoint p2)
	{
		return p1.equals(p2);
	}
	
	public static DoublePoint Add(DoublePoint p1, Direction dir)
	{
		switch (dir)
		{
			case north:
				return DoublePoint.Add(p1, new DoublePoint(0.0, 0.1));
			case east:
				return DoublePoint.Add(p1, new DoublePoint(0.1, 0.0));
			case south:
				return DoublePoint.Add(p1, new DoublePoint(0.0, -0.1));
			case west:
				return DoublePoint.Add(p1, new DoublePoint(-0.1, 0.0));
			default:
				return null;
		}
	}
	
	public static DoublePoint Multiply(DoublePoint p1, Integer m)
	{
		return new DoublePoint(p1._x * m, p1._y * m);
	}
	
	public static DoublePoint Multiply(DoublePoint p1, Double m)
	{
		return new DoublePoint(p1._x * m, p1._y * m);
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
		if (this instanceof DoublePoint && obj instanceof DoublePoint)
		{
			DoublePoint p1 = (DoublePoint) this;
			DoublePoint p2 = (DoublePoint) obj;
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
	
	public String toString()
	{
		return "(" + _x.toString() + ", " + _y.toString() + ")";
	}
	
	// end Methods
}
