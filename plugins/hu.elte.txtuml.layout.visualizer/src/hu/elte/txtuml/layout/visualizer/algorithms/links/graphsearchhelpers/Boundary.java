package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.Point;

/**
 * This class represents the bounds of a diagram.
 * 
 * @author Balázs Gregorics
 *
 */
public class Boundary
{
	private Integer _top;
	private Integer _bottom;
	private Integer _left;
	private Integer _right;
	
	/**
	 * Returns the top bound.
	 * 
	 * @return the top bound.
	 */
	public Integer get_top()
	{
		return _top;
	}
	
	/**
	 * Returns the bottom bound.
	 * 
	 * @return the bottom bound.
	 */
	public Integer get_bottom()
	{
		return _bottom;
	}
	
	/**
	 * Returns the left bound.
	 * 
	 * @return the left bound.
	 */
	public Integer get_left()
	{
		return _left;
	}
	
	/**
	 * Returns the right bound.
	 * 
	 * @return the right bound.
	 */
	public Integer get_right()
	{
		return _right;
	}
	
	/**
	 * Creates a {@link Boundary}.
	 * 
	 * @param tl
	 *            top-left point of the {@link Boundary}.
	 * @param br
	 *            bottom-rigth point of the {@link Boundary}.
	 * @throws InternalException
	 *             Throws if the bounds are not correct (negative interval).
	 */
	public Boundary(Point tl, Point br) throws InternalException
	{
		this(tl.getY(), br.getY(), tl.getX(), br.getX());
	}
	
	/**
	 * Creates a {@link Boundary}.
	 * 
	 * @param t
	 *            top bound.
	 * @param b
	 *            bottom bound.
	 * @param l
	 *            left bound.
	 * @param r
	 *            right bound.
	 * @throws InternalException
	 *             Throws if the bounds are not correct (negative interval).
	 */
	public Boundary(Integer t, Integer b, Integer l, Integer r)
			throws InternalException
	{
		_top = t;
		_bottom = b;
		_left = l;
		_right = r;
		
		if (_top < _bottom || _left > _right)
			throw new InternalException("Bounds are not correct!");
	}
	
	/**
	 * Returns whether the given {@link Point} is in the bounds.
	 * 
	 * @param p
	 *            the {@link Point} to check.
	 * @return whether the given {@link Point} is in the bounds.
	 */
	public Boolean isIn(Point p)
	{
		return _top >= p.getY() && p.getY() >= _bottom && _left <= p.getX()
				&& p.getX() <= _right;
	}
	
	/**
	 * Returns whether the given {@link Point} is out of the bounds.
	 * 
	 * @param p
	 *            the {@link Point} to check.
	 * @return whether the given {@link Point} is out of the bounds.
	 */
	public Boolean isOut(Point p)
	{
		return !isIn(p);
	}
	
	/**
	 * Adds a relative amount of extra "error" field around the bounds.
	 * 
	 * @param percent
	 *            error percent to add, relative to current bounds.
	 * @param fallback
	 *            value to expand the boundary by minimum
	 */
	public void addError(Integer percent, Integer fallback)
	{
		Integer extraWidth = (int) ((_right - _left) * (percent / 100.0));
		if (extraWidth < fallback)
			extraWidth = fallback;
		Integer extraHeight = (int) ((_top - _bottom) * (percent / 100.0));
		if (extraHeight < fallback)
			extraHeight = fallback;
		
		_top = _top + extraHeight;
		_bottom = _bottom - extraHeight;
		_left = _left - extraWidth;
		_right = _right + extraWidth;
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		result += "     " + _top + "\n";
		result += _left + "     " + _right + "\n";
		result += "     " + _bottom;
		
		return result;
	}
	
}
