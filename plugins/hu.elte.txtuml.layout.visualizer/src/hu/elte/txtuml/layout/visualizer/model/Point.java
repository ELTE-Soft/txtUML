package hu.elte.txtuml.layout.visualizer.model;

import java.util.HashSet;
import java.util.Set;

/**
 * The class representing an abstract Integer Point.
 * 
 * @author Balázs Gregorics
 */
public class Point
{
	// Variables
	
	/**
	 * Coordinate X.
	 */
	private Integer _x;
	/**
	 * Coordinate Y.
	 */
	private Integer _y;
	
	// end Variables
	
	// Getters, setters
	
	/***
	 * Getter for the X coordinate.
	 * 
	 * @return {@link Integer} of X coordinate.
	 */
	public Integer getX()
	{
		return _x;
	}
	
	/***
	 * Setter for the X coordinate.
	 * 
	 * @param v
	 *            {@link Integer} value of X coordinate to be set.
	 */
	public void setX(Integer v)
	{
		_x = v;
	}
	
	/***
	 * Getter for the Y coordinate.
	 * 
	 * @return {@link Integer} of Y coordinate.
	 */
	public Integer getY()
	{
		return _y;
	}
	
	/***
	 * Setter for the Y coordinate.
	 * 
	 * @param v
	 *            {@link Integer} value of Y coordinate to be set.
	 */
	public void setY(Integer v)
	{
		_y = v;
	}
	
	/**
	 * Getter for the coordinate of the {@link Point} which is horizontal to the
	 * {@link Direction}.
	 * 
	 * @param dir
	 *            {@link Direction} which's coordinate we want.
	 * @return Value of the coordinate.
	 */
	public Integer get(Direction dir)
	{
		if (dir.equals(Direction.east) || dir.equals(Direction.west))
			return getX();
		else
			return getY();
	}
	
	// end Getters, setters
	
	// Ctors
	
	/***
	 * Create a {@link Point} with (0, 0).
	 */
	public Point()
	{
		_x = 0;
		_y = 0;
	}
	
	/***
	 * Create a {@link Point} with (x, y).
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
	 * Create a {@link Point} copying p.
	 * 
	 * @param p
	 *            {@link Point} to copy.
	 */
	public Point(Point p)
	{
		_x = p._x;
		_y = p._y;
	}
	
	// end Ctors
	
	// Statics
	
	/**
	 * Addition of two {@link Point}s.
	 * 
	 * @param p1
	 *            First argument to add.
	 * @param p2
	 *            Second argument to add.
	 * @return A {@link Point} from the addition of p1 and p2.
	 */
	public static Point Add(Point p1, Point p2)
	{
		return new Point(p1._x + p2._x, p1._y + p2._y);
	}
	
	/***
	 * Substraction of two {@link Point}s.
	 * 
	 * @param p1
	 *            First argument to substract.
	 * @param p2
	 *            Second argument to substract.
	 * @return A {@link Point} from the substraction of p1 and p2.
	 */
	public static Point Substract(Point p1, Point p2)
	{
		return new Point(p1._x - p2._x, p1._y - p2._y);
	}
	
	/***
	 * Equality of two {@link Point}s.
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
	 * Addition of a {@link Point} and a {@link Direction}.
	 * 
	 * @param p1
	 *            {@link Point} to add.
	 * @param dir
	 *            {@link Direction} to add.
	 * @return A {@link Point} from the addition of p1 and dir.
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
	
	/**
	 * Multiplication of a {@link Point}.
	 * 
	 * @param p1
	 *            {@link Point} to multiply.
	 * @param m
	 *            Amount of multiplication.
	 * @return A {@link Point} from the multiplication of (p1 * m).
	 */
	public static Point Multiply(Point p1, Integer m)
	{
		return new Point(p1._x * m, p1._y * m);
	}
	
	/**
	 * Divide a {@link Point}.
	 * 
	 * @param p1
	 *            {@link Point} to divide
	 * @param m
	 *            Amount of division.
	 * @return A {@link Point} from the division of (p1 / m).
	 */
	public static Point Divide(Point p1, Integer m)
	{
		return new Point(p1._x / m, p1._y / m);
	}
	
	/***
	 * Multiply a {@link Direction} by an {@link Integer} amount to get a
	 * {@link Point}.
	 * 
	 * @param dir
	 *            {@link Direction} to multiply.
	 * @param m
	 *            Amount of multiplication.
	 * @return A {@link Point} from the multiplication
	 */
	public static Point Multiply(Direction dir, Integer m)
	{
		return new Point(Add(new Point(0, 0), dir)._x * m, Add(new Point(0, 0), dir)._y
				* m);
	}
	
	/**
	 * Check if p2 is in the dir {@link Direction} of p1.
	 * 
	 * @param p1
	 *            The {@link Point} to relate to.
	 * @param p2
	 *            The {@link Point} to check.
	 * @param dir
	 *            The {@link Direction} to check in.
	 * @return Boolean of p2 is in the dir {@link Direction} of p1.
	 */
	public static boolean isInTheDirection(Point p1, Point p2, Direction dir)
	{
		return isInTheDirection(p1, p2, dir, false);
	}
	
	/**
	 * Check if p2 is in the dir {@link Direction} of p1.
	 * 
	 * @param p1
	 *            The {@link Point} to relate to.
	 * @param p2
	 *            The {@link Point} to check.
	 * @param dir
	 *            The {@link Direction} to check in.
	 * @param inLineCounts
	 *            Whether to allow {@link Point}s that are in line with p1.
	 * @return Boolean of p2 is in the dir {@link Direction} of p1.
	 */
	public static boolean isInTheDirection(Point p1, Point p2, Direction dir,
			Boolean inLineCounts)
	{
		return p1.isInTheDirection(p2, dir, inLineCounts);
	}
	
	/**
	 * Method to compute in which {@link Direction} does relative is from p.
	 * 
	 * @param p
	 *            {@link Point} to check.
	 * @param relative
	 *            {@link Point} to relate.
	 * @return the {@link Direction} of p from relative.
	 */
	public static Direction directionOf(Point p, Point relative)
	{
		Point temp = Point.Substract(p, relative);
		if (Math.abs(temp.getX()) > Math.abs(temp.getY()))
		{
			if (temp.getX() > 0)
			{
				return Direction.east;
			}
			else if (temp.getX() < 0)
			{
				return Direction.west;
			}
		}
		else if (Math.abs(temp.getX()) < Math.abs(temp.getY()))
		{
			if (temp.getY() > 0)
			{
				return Direction.north;
			}
			else if (temp.getY() < 0)
			{
				return Direction.south;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to compute in which {@link Direction}s does relative is from p.
	 * 
	 * @param p
	 *            {@link Point} to check.
	 * @param relative
	 *            {@link Point} to relate.
	 * @return the {@link Direction}s of p from relative.
	 */
	public static Set<Direction> directionOfAll(Point p, Point relative)
	{
		Set<Direction> result = new HashSet<Direction>();
		
		Point temp = Point.Substract(p, relative);
		
		if (temp.getX() > 0)
			result.add(Direction.east);
		else if (temp.getX() < 0)
			result.add(Direction.west);
		
		if (temp.getY() > 0)
			result.add(Direction.north);
		else if (temp.getY() < 0)
			result.add(Direction.south);
		
		return result;
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
		if (obj instanceof Point)
		{
			Point p1 = this;
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
	
	/**
	 * Checks if a {@link Point} is in the given {@link Direction} from this
	 * point.
	 * 
	 * @param p
	 *            The {@link Point} to check.
	 * @param dir
	 *            The {@link Direction} to check.
	 * @return True if p is in the dir {@link Direction} of this.
	 */
	public boolean isInTheDirection(Point p, Direction dir)
	{
		Point dv = Point.Substract(p, this);
		
		if (dv.getX() > 0 && dir.equals(Direction.east))
			return true;
		if (dv.getX() < 0 && dir.equals(Direction.west))
			return true;
		if (dv.getY() > 0 && dir.equals(Direction.north))
			return true;
		if (dv.getY() < 0 && dir.equals(Direction.south))
			return true;
		
		return false;
	}
	
	/**
	 * Checks if a point is in the given {@link Direction} from this
	 * {@link Point}.
	 * 
	 * @param p
	 *            The {@link Point} to check.
	 * @param dir
	 *            The {@link Direction} to check.
	 * @param inLineCounts
	 *            Whether to allow {@link Point}s that are in line with this
	 *            point.
	 * @return True if p is in the dir {@link Direction} of this.
	 */
	public boolean isInTheDirection(Point p, Direction dir, Boolean inLineCounts)
	{
		Point dv = Point.Substract(p, this);
		
		if (dv.getX() >= 0 && dir.equals(Direction.east))
			return true;
		if (dv.getX() <= 0 && dir.equals(Direction.west))
			return true;
		if (dv.getY() >= 0 && dir.equals(Direction.north))
			return true;
		if (dv.getY() <= 0 && dir.equals(Direction.south))
			return true;
		
		return false;
	}
	
	/**
	 * Returns the traditional euklidean length of the {@link Point} (Vector).
	 * 
	 * @return The length of the {@link Point}.
	 */
	public Double length()
	{
		return Math.sqrt(Math.pow(_x, 2) + Math.pow(_y, 2));
	}
	
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
