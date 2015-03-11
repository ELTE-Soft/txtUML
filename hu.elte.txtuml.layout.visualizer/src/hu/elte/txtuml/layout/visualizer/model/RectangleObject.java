package hu.elte.txtuml.layout.visualizer.model;

public class RectangleObject
{
	// Variables
	
	private String _name;
	private Point _position;
	
	// end Variables
	
	// Getters, setters
	
	public String getName()
	{
		return _name;
	}
	
	public Point getPosition()
	{
		return _position;
	}
	
	public void setPosition(Point p)
	{
		_position = p;
	}
	
	// end Getters, setters
	
	// Ctors
	
	public RectangleObject(String n)
	{
		_name = n;
		_position = new Point();
	}
	
	public RectangleObject(String n, Point p)
	{
		_name = n;
		_position = p;
	}
	
	// end Ctors
	
	// Statics
	
	public static boolean Equals(RectangleObject o1, RectangleObject o2)
	{
		return o1.equals(o2);
	}
	
	// EndStatics
	
	// Methods
	
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
			return o1._name.equals(o2._name);
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
	
	public String toString()
	{
		return _name + ": " + _position.toString();
	}
	
	// end Methods
	
}
