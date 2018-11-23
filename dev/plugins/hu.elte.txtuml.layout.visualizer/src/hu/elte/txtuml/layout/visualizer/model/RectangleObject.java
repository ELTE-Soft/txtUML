package hu.elte.txtuml.layout.visualizer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class that represents a box in a diagram.
 */
public class RectangleObject {
	// Variables

	private String _name;
	private Point _position;
	private Integer _width;
	private Integer _height;

	private Integer _pixelWidth;
	private Integer _pixelHeight;

	private Boolean _phantom;
	private Boolean _virtualPhantom;
	private SpecialBox _special;
	private Integer _linkNumber;

	private Diagram _inner;

	// private Set<Point> _points;

	// end Variables

	// Getters, setters

	/**
	 * Getter for the name of the {@link RectangleObject}.
	 * 
	 * @return Name of the {@link RectangleObject}.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Returns whether this {@link RectangleObject} has any inner
	 * {@link Diagram}s or not.
	 * 
	 * @return whether this {@link RectangleObject} has any inner
	 *         {@link Diagram}s or not.
	 */
	public Boolean hasInner() {
		return (null != _inner);
	}

	/**
	 * Returns the inner diagram if it's present or null.
	 * 
	 * @return the inner diagram if it's present or null.
	 */
	public Diagram getInner() {
		return _inner;
	}

	/**
	 * Sets the inner diagram.
	 * 
	 * @param diag
	 *            Diagram to set.
	 */
	public void setInner(Diagram diag) {
		_inner = diag;
	}

	/**
	 * Getter for the top-left position of the {@link RectangleObject}.
	 * 
	 * @return top-left position of the {@link RectangleObject}.
	 */
	public Point getPosition() {
		return _position;
	}

	/***
	 * Getter for the area that the {@link RectangleObject} covers.
	 * 
	 * @return Set of {@link Point}s about the area the {@link RectangleObject}
	 *         occupies.
	 */
	public Set<Point> getPoints() {
		Set<Point> result = new HashSet<Point>();

		for (int i = 0; i < this.getWidth(); ++i) {
			Point temp = Point.Add(_position, Point.Multiply(Direction.east, i));
			for (int j = 0; j < this.getHeight(); ++j) {
				result.add(Point.Add(temp, Point.Multiply(Direction.south, j)));
			}
		}

		return result;
	}

	/**
	 * Returns all the {@link Point}s on the perimiter of the
	 * {@link RectangleObject}.
	 * 
	 * @return Set of {@link Point}s.
	 */
	public Set<Point> getPerimiterPoints() {
		Set<Point> result = new HashSet<Point>();

		Point tl = getTopLeft();
		Point br = getBottomRight();

		for (int i = 0; i < this.getWidth(); ++i) {
			result.add(Point.Add(tl, Point.Multiply(Direction.east, i)));
			result.add(Point.Add(br, Point.Multiply(Direction.west, i)));
		}

		for (int i = 0; i < this.getHeight(); ++i) {
			result.add(Point.Add(tl, Point.Multiply(Direction.south, i)));
			result.add(Point.Add(br, Point.Multiply(Direction.north, i)));
		}

		return result;
	}

	/**
	 * Returns the center points of each side.
	 * 
	 * @return the center points of each side.
	 */
	public Set<Point> getCenterPoints() {
		Set<Point> result = new HashSet<Point>();

		int horizontalHalf = getWidth() / 2;
		int verticalHalf = getWidth() / 2;

		result.add(Point.Add(getBottomRight(), Point.Multiply(Direction.west, horizontalHalf)));
		result.add(Point.Add(getBottomRight(), Point.Multiply(Direction.north, verticalHalf)));

		if (getWidth() % 2 == 0)
			horizontalHalf = horizontalHalf - 1;
		result.add(Point.Add(getTopLeft(), Point.Multiply(Direction.east, horizontalHalf)));
		if (getWidth() % 2 == 0)
			verticalHalf = verticalHalf - 1;
		result.add(Point.Add(getTopLeft(), Point.Multiply(Direction.south, verticalHalf)));

		return result;
	}

	/**
	 * Getter for the top left point of the objects.
	 * 
	 * @return Point of the top left corner.
	 */
	public Point getTopLeft() {
		return new Point(_position);
	}

	/**
	 * Getter for the bottom right point of the objects.
	 * 
	 * @return Point of the bottom right corner.
	 */
	public Point getBottomRight() {
		return new Point(_position.getX() + (getWidth() - 1), _position.getY() - (getHeight() - 1));
	}

	/***
	 * Setter for the position of the object.
	 * 
	 * @param value
	 *            Point to set the object's position.
	 */
	public void setPosition(Point value) {

		if (hasInner()) {
			// Move inner boxes
			for (RectangleObject innerBox : _inner.Objects) {
				Point diff = Point.Substract(innerBox.getPosition(), _position);
				innerBox.setPosition(Point.Add(value, diff));
			}

			// Move inner links
			for (LineAssociation innerLink : _inner.Assocs) {
				List<Point> newRoute = new ArrayList<Point>();
				for (Point routePoint : innerLink.getRoute()) {
					Point diff = Point.Substract(routePoint, _position);
					Point newPoint = Point.Add(value, diff);
					newRoute.add(newPoint);
				}
				innerLink.setRoute(newRoute);
			}
		}

		_position = value;
	}

	/**
	 * Getter for the width of the object.
	 * 
	 * @return Integer of the grid-width of the object.
	 */
	public Integer getWidth() {
		if (hasInner() && getInner().hasValidLayout()) {
			double ratio = getInner().getPixelGridHorizontal();
			int leftborder = (int) Math.round(getInner().getLeftPixelBorder() / ratio);
			int rightborder = (int) Math.round(getInner().getLeftPixelBorder() / ratio);
			return getInner().getWidth() + 2 + leftborder + rightborder;
		}

		return _width;
	}

	/**
	 * Setter for the width of the object.
	 * 
	 * @param value
	 *            Amount which the object's width should be set.
	 */
	public void setWidth(Integer value) {
		if (value >= 1) {
			_width = value;
		}
	}

	/**
	 * Getter for the height of the object.
	 * 
	 * @return Integer of the grid-height of the object.
	 */
	public Integer getHeight() {
		if (hasInner() && getInner().hasValidLayout()) {
			double ratio = getInner().getPixelGridVertical();
			int topborder = (int) Math.round(getInner().getTopPixelBorder() / ratio);
			int bottomborder = (int) Math.round(getInner().getBottomPixelBorder() / ratio);
			int header = (int) Math.round(getInner().getPixelHeader() / getInner().getPixelGridVertical());
			return getInner().getHeight() + 2 + topborder + bottomborder + header;
		}

		return _height;
	}

	/**
	 * Setter for the height of the object.
	 * 
	 * @param value
	 *            Amount which the object's height should be set.
	 */
	public void setHeight(Integer value) {
		if (value >= 1) {
			_height = value;
		}
	}

	/**
	 * Whether the pixel values (any) are set on this {@link RectangleObject} or
	 * not.
	 * 
	 * @return true if set, false else.
	 */
	public boolean isPixelDimensionsPresent() {
		return (_pixelWidth != -1 && _pixelHeight != -1);
	}

	/**
	 * Getter for the width amount of the rectangle in pixels.
	 * 
	 * @return the width in pixels.
	 */
	public Integer getPixelWidth() {
		return new Integer(_pixelWidth);
	}

	/**
	 * Setter for the width of the rectangle in pixels.
	 * 
	 * @param value
	 *            the width in pixels.
	 */
	public void setPixelWidth(Integer value) {
		_pixelWidth = new Integer(value);
	}

	/**
	 * Getter for the height amount of the rectangle in pixels.
	 * 
	 * @return the height in pixels.
	 */
	public Integer getPixelHeight() {
		return new Integer(_pixelHeight);
	}

	/**
	 * Setter for the height of the rectangle in pixels.
	 * 
	 * @param value
	 *            the height in pixels.
	 */
	public void setPixelHeight(Integer value) {
		_pixelHeight = new Integer(value);
	}

	/**
	 * Returns the area of this box.
	 * 
	 * @return the area of this box.
	 */
	public Integer getPixelArea() {
		return new Integer(_pixelWidth * _pixelHeight);
	}

	/**
	 * Returns whether this box is a phantom box or not.
	 * 
	 * @return whether this box is a phantom box or not.
	 */
	public Boolean isPhantom() {
		return _phantom;
	}
	
	/**
	 * Returns whether this box is a virtual phantom box or not.
	 * 
	 * @return whether this box is a virtual phantom box or not.
	 */
	public Boolean isVirtualPhantom() {
		return _virtualPhantom;
	}

	/**
	 * Sets the box to phantom or not.
	 * 
	 * @param value
	 *            whether this box should be a phantom or not.
	 */
	public void setPhantom(Boolean value) {
		_phantom = value;
	}
	
	/**
	 * Sets the box to virtual phantom or not.
	 * 
	 * @param value
	 *            whether this box should be a virtual phantom or not.
	 */
	public void setVirtualPhantom(Boolean value) {
		_virtualPhantom = value;
	}

	/**
	 * Returns the number of links connected to this {@link RectangleObject}.
	 * 
	 * @return the number of links connected to this {@link RectangleObject}.
	 */
	public Integer getLinkNumber() {
		return _linkNumber;
	}

	/**
	 * Adds to the number of links connected to this {@link RectangleObject}.
	 * 
	 * @param value
	 *            number to add.
	 */
	public void addLinkNumber(Integer value) {
		_linkNumber = _linkNumber + value;
	}

	/**
	 * Returns whether this {@link RectangleObject} is a special type of box or
	 * not.
	 * 
	 * @return whether this {@link RectangleObject} is a special type of box or
	 *         not.
	 */
	public Boolean isSpecial() {
		return !_special.equals(SpecialBox.None);
	}

	/**
	 * Returns the {@link SpecialBox} type of this {@link RectangleObject}.
	 * 
	 * @return the {@link SpecialBox} type of this {@link RectangleObject}.
	 */
	public SpecialBox getSpecial() {
		return _special;
	}

	/**
	 * Sets the {@link SpecialBox} type of this {@link RectangleObject}.
	 * 
	 * @param spec
	 *            the {@link SpecialBox} type of this {@link RectangleObject}.
	 */
	public void setSpecial(SpecialBox spec) {
		_special = spec;
	}

	// end Getters, setters

	// Ctors

	/***
	 * Create a RectanlgeObject with a name.
	 * 
	 * @param n
	 *            Name of the object.
	 */
	public RectangleObject(String n) {
		_name = n;
		_position = new Point();
		_width = 1;
		_height = 1;
		_pixelWidth = -1;
		_pixelHeight = -1;
		_phantom = false;
		_virtualPhantom = false;
		_special = SpecialBox.None;
		_linkNumber = 0;
		_inner = null;
	}

	/***
	 * Create a RectanlgeObject with a name and special property.
	 * 
	 * @param n
	 *            Name of the object.
	 * @param sb
	 *            SpecialBox value.
	 */
	public RectangleObject(String n, SpecialBox sb) {
		this(n);
		_special = sb;
	}

	/***
	 * Create a RectangleObject with a name and a position.
	 * 
	 * @param n
	 *            Name of the object.
	 * @param p
	 *            Position of the object.
	 */
	public RectangleObject(String n, Point p) {
		this(n);
		_position = p;
	}

	/***
	 * Create a RectangleObject copying another one.
	 * 
	 * @param other
	 *            RectangleObject to copy.
	 */
	public RectangleObject(RectangleObject other) {
		_name = new String(other._name);
		_position = new Point(other._position);
		_width = new Integer(other._width);
		_height = new Integer(other._height);
		_pixelWidth = new Integer(other._pixelWidth);
		_pixelHeight = new Integer(other._pixelHeight);
		_phantom = new Boolean(other._phantom);
		_virtualPhantom = new Boolean(other._virtualPhantom);
		_special = other._special;
		_linkNumber = new Integer(other._linkNumber);
		_inner = Diagram.clone(other._inner);
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
	public static boolean Equals(RectangleObject o1, RectangleObject o2) {
		return o1.equals(o2);
	}

	// EndStatics

	// Methods

	/**
	 * Equality method for comparing a RectangleObject.
	 * 
	 * @param obj
	 *            RectangleObject to compare.
	 * @return Boolean value of (this == obj).
	 */
	public boolean equals(RectangleObject obj) {
		return (this._name.equals(obj._name) && this._position.equals(obj._position));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		if (obj instanceof RectangleObject) {
			RectangleObject o1 = this;
			RectangleObject o2 = (RectangleObject) obj;
			return o1.equals(o2);
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _name.hashCode();
		result = prime * result + _position.hashCode();
		result = prime * result + _phantom.hashCode();
		result = prime * result + _virtualPhantom.hashCode();
		result = prime * result + _special.hashCode();
		return result;
	}

	@Override
	public String toString() {
		String specialString = isSpecial() ? ("(" + _special.toString() + ")") : "";
		String innerString = hasInner() ? _inner.toString() : "";

		return _name + specialString + innerString + ": " + _position.toString() + "[w:" + _width.toString() + "("
				+ _pixelWidth.toString() + "px), h:" + _height.toString() + "(" + _pixelHeight.toString() + "px)]";
	}

	// end Methods

}
