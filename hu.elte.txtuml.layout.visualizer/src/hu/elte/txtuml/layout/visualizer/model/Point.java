package hu.elte.txtuml.layout.visualizer.model;

/**
 * The class representing an abstract Integer Point.
 * 
 * @author Balázs Gregorics
 */
public class Point
{
	// Variables
	
	private Integer _x;
	private Integer _y;
	
	// end Variables
	
	// Getters, setters
	
	/***
	 * Getter for the X coordinate.
	 * 
	 * @return Integer of X coordinate.
	 */
	public Integer getX()
	{
		return _x;
	}
	
	/***
	 * Setter for the X coordinate.
	 * 
	 * @param v
	 *            Integer value of X coordinate to be set.
	 */
	public void setX(Integer v)
	{
		_x = v;
	}
	
	/***
	 * Getter for the Y coordinate.
	 * 
	 * @return Integer of Y coordinate.
	 */
	public Integer getY()
	{
		return _y;
	}
	
	/***
	 * Setter for the Y coordinate.
	 * 
	 * @param v
	 *            Integer value of Y coordinate to be set.
	 */
	public void setY(Integer v)
	{
		_y = v;
	}
	
	// end Getters, setters
	
	// Ctors
	
	/***
	 * Create a Point with (0, 0).
	 */
	public Point()
	{
		_x = 0;
		_y = 0;
	}
	
	/***
	 * Create a Point with (x, y).
	 * 
	 * @param x
	 *            X coordinate.
	 * @param y
	 *            Y coordinate.
	 */
	public Point(Integer x, Integer y)
	{
		_x = x;
		_y = y;
	}
	
	/***
	 * Create a Point copying p.
	 * 
	 * @param p
	 *            Point to copy.
	 */
	public Point(Point p)
	{
		_x = p._x;
		_y = p._y;
	}
	
	// end Ctors
	
	// Statics
	
	/***
	 * Addition of two Points.
	 * 
	 * @param p1
	 *            First argument to add.
	 * @param p2
	 *            Second argument to add.
	 * @return A Point from the addition of p1 and p2.
	 */
	public static Point Add(Point p1, Point p2)
	{
		return new Point(p1._x + p2._x, p1._y + p2._y);
	}
	
	/***
	 * Substraction of two Points.
	 * 
	 * @param p1
	 *            First argument to substract.
	 * @param p2
	 *            Second argument to substract.
	 * @return A Point from the substraction of p1 and p2.
	 */
	public static Point Substract(Point p1, Point p2)
	{
		return new Point(p1._x - p2._x, p1._y - p2._y);
	}
	
	/***
	 * Equality of two Points.
	 * 
	 * @param p1
	 *            First argument to compare.
	 * @param p2
	 *            Second argument to compare.
	 * @return Boolean value of (p1 == p2).
	 */
	public static boolean Equals(Point p1, Point p2)
	{
		return p1.equals(p2);
	}
	
	/***
	 * Addition of a Point and a Direction
	 * 
	 * @param p1
	 *            Point to add.
	 * @param dir
	 *            Direction to add.
	 * @return A Point from the addition of p1 and dir.
	 */
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
	
	/***
	 * Multiplication of a Point.
	 * 
	 * @param p1
	 *            Point to multiply.
	 * @param m
	 *            Amount of multiplication.
	 * @return A Point from the multiplication of (p1 * m).
	 */
	public static Point Multiply(Point p1, Integer m)
	{
		return new Point(p1._x * m, p1._y * m);
	}
	
	/***
	 * Multiply a Direction by an Integer amount to get a Point.
	 * 
	 * @param dir
	 *            Direction to multiply.
	 * @param m
	 *            Amount of multiplication.
	 * @return A Point from the multiplication
	 */
	public static Point Multiply(Direction dir, Integer m)
	{
		return new Point(Add(new Point(0, 0), dir)._x * m, Add(new Point(0, 0), dir)._y
				* m);
	}
	
	// end Statics
	
	// Equality
	
	@Override
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
	
	@Override
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
	
	@Override
	public Point clone()
	{
		return new Point(_x, _y);
	}
	
	@Override
	public String toString()
	{
		return "(" + _x.toString() + ", " + _y.toString() + ")";
	}
	
	// end Methods
}
