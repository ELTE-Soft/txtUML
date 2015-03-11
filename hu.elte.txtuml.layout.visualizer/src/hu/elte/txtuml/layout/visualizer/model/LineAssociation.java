package hu.elte.txtuml.layout.visualizer.model;

import java.util.ArrayList;

public class LineAssociation
{
	private String _id;
	private String _from;
	private String _to;
	private AssociationType _type;
	private ArrayList<Point> _route;
	private Integer _turns;
	private Integer _extends;
	
	// Getters, setters
	
	public String getId()
	{
		return _id;
	}
	
	public void setId(String value)
	{
		_id = value;
	}
	
	public String getFrom()
	{
		return _from;
	}
	
	public void setFrom(String value)
	{
		_from = value;
	}
	
	public String getTo()
	{
		return _to;
	}
	
	public void setTo(String value)
	{
		_to = value;
	}
	
	public AssociationType getType()
	{
		return _type;
	}
	
	public void setType(AssociationType value)
	{
		_type = value;
	}
	
	public ArrayList<Point> getRoute()
	{
		return _route;
	}
	
	public enum RouteConfig
	{
		START,
		END
	}
	
	public Point getRoute(RouteConfig conf)
	{
		if (conf.equals(RouteConfig.START))
		{
			try
			{
				return _route.get(0);
			}
			catch (IndexOutOfBoundsException e)
			{
				return null;
			}
		}
		if (conf.equals(RouteConfig.END))
		{
			try
			{
				return _route.get(_route.size() - 1);
			}
			catch (IndexOutOfBoundsException e)
			{
				return null;
			}
		}
		
		return null;
	}
	
	public void setRoute(ArrayList<Point> value)
	{
		_route = value;
	}
	
	public Integer getTurns()
	{
		return _turns;
	}
	
	public void setTurns(Integer value)
	{
		_turns = value;
	}
	
	public Integer getExtends()
	{
		return _extends;
	}
	
	public void setExtends(Integer value)
	{
		_extends = value;
	}
	
	// End Getters, setters
	
	// Ctors
	
	public LineAssociation(String n, String f, String t)
	{
		_id = n;
		_from = f;
		_to = t;
		_route = new ArrayList<Point>();
		_turns = 0;
		_type = AssociationType.normal;
	}
	
	public LineAssociation(String n, String f, String t, AssociationType ty)
	{
		this(n, f, t);
		_type = ty;
	}
	
	public LineAssociation(String n, RectangleObject f, RectangleObject t)
	{
		_id = n;
		_from = f.getName();
		_to = t.getName();
		_route = new ArrayList<Point>();
		_route.add(f.getPosition());
		_route.add(t.getPosition());
		_turns = 0;
		_type = AssociationType.normal;
	}
	
	public LineAssociation(String n, RectangleObject f, RectangleObject t,
			AssociationType ty)
	{
		this(n, f, t);
		_type = ty;
	}
	
	public LineAssociation(String n, String f, String t, Point s, Point e)
	{
		_id = n;
		_from = f;
		_to = t;
		_route = new ArrayList<Point>();
		_route.add(s);
		_route.add(e);
		_turns = 0;
		_type = AssociationType.normal;
	}
	
	public LineAssociation(String n, String f, String t, Point s, Point e,
			AssociationType ty)
	{
		this(n, f, t, s, e);
		_type = ty;
	}
	
	// End Ctors
	
	// Statics
	
	public static boolean Equals(LineAssociation a1, LineAssociation a2)
	{
		return a1.equals(a2);
	}
	
	// End Statics
	
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
		if (this instanceof LineAssociation && obj instanceof LineAssociation)
		{
			LineAssociation a1 = (LineAssociation) this;
			LineAssociation a2 = (LineAssociation) obj;
			return a1._id.equals(a2._id);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _id.hashCode();
		result = prime * result + _from.hashCode();
		result = prime * result + _to.hashCode();
		result = prime * result + _type.hashCode();
		return result;
	}
	
	public String toString()
	{
		String result = _id + ": ";
		result += "(" + _from + " - " + _to + ")";
		result += " (" + _type.toString() + ")";
		if (_route != null && _route.size() > 0)
		{
			result += ", #Turns: " + _turns + ", #Extends: " + _extends;
			result += ", Route: ";
			for (Point p : _route)
				result += p.toString() + "->";
			// result.substring(0, result.length() - 2);
		}
		
		return result;
	}
	// End Equality
	
}
