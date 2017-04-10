package hu.elte.txtuml.export.javascript.json;

import javax.xml.bind.annotation.XmlAttribute;

import hu.elte.txtuml.utils.diagrams.Point;

/**
 * 
 * Simple wrapper for point which has proper annotation for marshaling
 * 
 */
public class MarshalablePoint {
	private Point point;

	protected MarshalablePoint() {
	}

	/**
	 * Creates a marshalable wrapper for the given point
	 * 
	 * @param p
	 *            The underlaying point
	 */
	public MarshalablePoint(Point p) {
		this.point = p;
	}

	@XmlAttribute(name = "x")
	/**
	 * 
	 * @return the x coordinate of the point
	 */
	public int getX() {
		return point.x();
	}

	/**
	 * 
	 * @return the y coordinate of the point
	 */
	@XmlAttribute(name = "y")
	public int getY() {
		return point.y();
	}

}
