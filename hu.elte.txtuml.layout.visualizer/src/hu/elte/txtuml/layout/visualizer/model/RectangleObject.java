package hu.elte.txtuml.layout.visualizer.model;

import java.util.HashSet;
import java.util.Set;

/**
 * The class that represents an object in a diagram.
 * 
 * @author Balázs Gregorics
 */
public class RectangleObject
{
	// Variables
	
	private String _name;
	private Point _position;
	private Integer _width;
	
	// private Set<Point> _points;
	
	// end Variables
	
	// Getters, setters
	
	/***
	 * Getter for the name of the object.
	 * 
	 * @return Name of the object.
	 */
	public String getName()
	{
		return _name;
	}
	
	/***
	 * Gett for the position of the object.
	 * 
	 * @return Point of the object.
	 */
	public Point getPosition()
	{
		return _position;
	}
	
	/***
	 * Getter for the area that the object covers.
	 * 
	 * @return Set of Points about the area the object occupies.
	 */
	public Set<Point> getPoints()
	{
		Set<Point> result = new HashSet<Point>();
		
		result.add(new Point(_position));
		
		Integer toBounds = (_width - 1) / 2;
		
		for (int i = 0; i <= toBounds; ++i)
		{
			for (int j = 0; j <= toBounds; ++j)
			{
				Point eastP = Point.Multiply(Direction.east, i);
				Point westP = Point.Multiply(Direction.west, i);
				result.add(Point.Add(Point.Add(_position, eastP),
						Point.Multiply(Direction.north, j)));
				result.add(Point.Add(Point.Add(_position, eastP),
						Point.Multiply(Direction.south, j)));
				result.add(Point.Add(Point.Add(_position, westP),
						Point.Multiply(Direction.north, j)));
				result.add(Point.Add(Point.Add(_position, westP),
						Point.Multiply(Direction.south, j)));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns all the points on the perimiter of the {@link RectangleObject}.
	 * 
	 * @return Set of {@link Point}s.
	 */
	public Set<Point> getPerimiterPoints()
	{
		Set<Point> result = new HashSet<Point>();
		
		Point tl = getTopLeft();
		Point br = getBottomRight();
		result.add(tl);
		result.add(br);
		for (int i = 1; i < _width; ++i)
		{
			result.add(Point.Add(tl, Point.Multiply(Direction.east, i)));
			result.add(Point.Add(tl, Point.Multiply(Direction.south, i)));
			result.add(Point.Add(br, Point.Multiply(Direction.north, i)));
			result.add(Point.Add(br, Point.Multiply(Direction.west, i)));
		}
		
		return result;
	}
	
	/**
	 * Getter for the top left point of the object.
	 * 
	 * @return Point of the top left corner.
	 */
	public Point getTopLeft()
	{
		Set<Point> occupied = this.getPoints();
		Integer left = occupied.stream().map(p -> p.getX())
				.min((p1, p2) -> Integer.compare(p1, p2)).get();
		Integer top = occupied.stream().map(p -> p.getY())
				.max((p1, p2) -> Integer.compare(p1, p2)).get();
		return new Point(left, top);
	}
	
	/***
	 * Getter for the bottom right point of the objects.
	 * 
	 * @return Point of the bottom right corner.
	 */
	public Point getBottomRight()
	{
		Set<Point> occupied = this.getPoints();
		Integer right = occupied.stream().map(p -> p.getX())
				.max((p1, p2) -> Integer.compare(p1, p2)).get();
		Integer bottom = occupied.stream().map(p -> p.getY())
				.min((p1, p2) -> Integer.compare(p1, p2)).get();
		return new Point(right, bottom);
	}
	
	/***
	 * Setter for the position of the object.
	 * 
	 * @param p
	 *            Point to set the object's position.
	 */
	public void setPosition(Point p)
	{
		_position = p;
	}
	
	/***
	 * Getter for the width of the object.
	 * 
	 * @return Integer of the grid-width of the object.
	 */
	public Integer getWidth()
	{
		return _width;
	}
	
	/***
	 * Setter for the width of the object.
	 * 
	 * @param value
	 *            Amount which the object's width should be set.
	 */
	public void setWidth(Integer value)
	{
		if (value >= 1)
		{
			_width = value;
		}
	}
	
	// end Getters, setters
	
	// Ctors
	
	/***
	 * Create a RectanlgeObject with a name.
	 * 
	 * @param n
	 *            Name of the object.
	 */
	public RectangleObject(String n)
	{
		_name = n;
		_position = new Point();
		_width = 1;
	}
	
	/***
	 * Create a RectangleObject with a name and a position.
	 * 
	 * @param n
	 *            Name of the object.
	 * @param p
	 *            Position of the object.
	 */
	public RectangleObject(String n, Point p)
	{
		_name = n;
		_position = p;
		_width = 1;
	}
	
	/***
	 * Create a RectangleObject copying another one.
	 * 
	 * @param o
	 *            RectangleObject to copy.
	 */
	public RectangleObject(RectangleObject o)
	{
		_name = new String(o._name);
		_position = new Point(o._position);
		_width = new Integer(o._width);
	}
	
	// end Ctors
	
	// Statics
	
	/***
	 * Equality method for comparing two RectangleObjects.
	 * 
	 * @param o1
	 *            First RectangleObject to compare.
	 * @param o2
	 *            Second RectangleObject to compare.
	 * @return Boolean value of (o1 == o2).
	 */
	public static boolean Equals(RectangleObject o1, RectangleObject o2)
	{
		return o1.equals(o2);
	}
	
	// EndStatics
	
	// Methods
	
	/***
	 * Equality method for comparing a RectangleObject.
	 * 
	 * @param obj
	 *            RectangleObject to compare.
	 * @return Boolean value of (this == obj).
	 */
	public boolean equals(RectangleObject obj)
	{
		return (this._name.equals(obj._name) && this._position.equals(obj._position));
	}
	
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
		if (this instanceof RectangleObject && obj instanceof RectangleObject)
		{
			RectangleObject o1 = (RectangleObject) this;
			RectangleObject o2 = (RectangleObject) obj;
			return o1.equals(o2);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _name.hashCode();
		result = prime * result + _position.hashCode();
		return result;
	}
	
	@Override
	public String toString()
	{
		return _name + ": " + _position.toString() + "[" + _width.toString() + "]";
	}
	
	// end Methods
	
}
