package hu.elte.txtuml.layout.visualizer.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;

/**
 * Class to represent a Diagram.
 */
public class Diagram {

	// Variables

	/**
	 * Type of the {@link Diagram}.
	 */
	public DiagramType Type;
	/**
	 * {@link RectangleObject}s in the {@link Diagram}
	 */
	public Set<RectangleObject> Objects;
	/**
	 * {@link LineAssociation}s in the {@link Diagram}.
	 */
	public Set<LineAssociation> Assocs;

	// end Variables

	// Ctors

	/**
	 * Create {@link Diagram}.
	 * @param ty Type of the {@link Diagram}.
	 */
	public Diagram(DiagramType ty) {
		Type = ty;
		Objects = new HashSet<RectangleObject>();
		Assocs = new HashSet<LineAssociation>();
	}

	/**
	 * Copy {@link Diagram}.
	 * @param diag Other {@link Diagram} to copy.
	 */
	public Diagram(final Diagram diag) {
		Type = diag.Type;
		Objects = new HashSet<RectangleObject>(diag.Objects);
		Assocs = new HashSet<LineAssociation>(diag.Assocs);
	}

	/**
	 * Create {@link Diagram}.
	 * @param ty Type of the {@link Diagram}.
	 * @param os {@link RectangleObject}s in the {@link Diagram}.
	 * @param as {@link LineAssociation}s in the {@link Diagram}.
	 */
	public Diagram(DiagramType ty, Set<RectangleObject> os, Set<LineAssociation> as) {
		Type = ty;

		if (os == null)
			Objects = new HashSet<RectangleObject>();
		else
			Objects = new HashSet<RectangleObject>(os);

		if (as == null)
			Assocs = new HashSet<LineAssociation>();
		else
			Assocs = new HashSet<LineAssociation>(as);
	}

	// end Ctors

	// Statics

	/**
	 * Method to clone a {@link Diagram}.
	 * 
	 * @param d
	 *            {@link Diagram} to clone.
	 * @return cloned {@link Diagram}.
	 */
	public static Diagram clone(Diagram d) {
		if (null == d)
			return null;
		else
			return new Diagram(d);
	}

	// end Statics

	// Publics

	/**
	 * Returns the Horizontal pixel-grid ratio.
	 * 
	 * @return the Horizontal pixel-grid ratio.
	 */
	public Integer getPixelGridHorizontal() {
		return getPixelGridRatio(box -> box.getWidth(), box -> box.getPixelWidth());
	}

	/**
	 * Returns the Vertical pixel-grid ratio.
	 * 
	 * @return the Vertical pixel-grid ratio.
	 */
	public Integer getPixelGridVertical() {
		return getPixelGridRatio(box -> box.getHeight(), box -> box.getPixelHeight());
	}

	/**
	 * Returns whether this {@link Diagram} has a layout applied or not.
	 * 
	 * @return whether this {@link Diagram} has a layout applied or not.
	 */
	public boolean hasValidLayout() {
		Boolean result = true;

		for (RectangleObject box1 : Objects) {
			if (!result)
				return result;

			for (RectangleObject box2 : Objects) {
				if (box1.equals(box2))
					continue;

				if (!result)
					return result;

				result = result && !box1.getPosition().equals(box2.getPosition());
			}
		}

		for (LineAssociation link : Assocs) {
			if (!result)
				return result;

			result = result && link.getRoute().size() > 2;
		}

		return result;
	}

	/**
	 * Returns the grid width of this {@link Diagram}. This method is
	 * calculation heavy.
	 * 
	 * @return the grid width of this {@link Diagram}.
	 * @throws InternalException 
	 */
	public Integer getWidth() {
		return Math.abs((getDimensions().get_right() - getDimensions().get_left()) + 1);
	}

	/**
	 * Returns the grid height of this {@link Diagram}. This method is
	 * calculation heavy.
	 * 
	 * @return the grid height of this {@link Diagram}.
	 * @throws InternalException 
	 */
	public Integer getHeight() {
		return Math.abs((getDimensions().get_top() - getDimensions().get_bottom()) + 1);
	}

	/**
	 * Returns the grid area boundaries that this diagram fits into (including
	 * extra barrier grid lines).
	 * 
	 * @return the grid area boundaries that this diagram fits into.
	 * @throws InternalException
	 */
	public Boundary getArea(){
		Boundary dim = getDimensions();

		return new Boundary(dim.get_top() + 1, dim.get_bottom() - 1, dim.get_left() - 1, dim.get_right() + 1);
	}

	@Override
	public String toString() {
		String result = "(O:";

		for (RectangleObject obj : Objects) {
			result += obj.getName() + ", ";
		}

		result += "| A: ";

		for (LineAssociation link : Assocs) {
			result += link.getId() + ", ";
		}

		result += ")";

		return result;
	}

	// end Publics

	// Privates

	private Integer getPixelGridRatio(Function<RectangleObject, Integer> gridSelector,
			Function<RectangleObject, Integer> pixelSelector) {
		Integer gridSum = 0;
		Integer pixelSum = 0;

		for (RectangleObject box : Objects) {
			if (box.isPixelDimensionsPresent()) {
				gridSum += (gridSelector.apply(box) - 1);
				pixelSum += pixelSelector.apply(box);
			} 
		}

		return (int) Math.round((double)pixelSum / (double)gridSum);
	}

	private Boundary getDimensions() {
		Integer left = Integer.MAX_VALUE;
		Integer right = Integer.MIN_VALUE;
		Integer top = Integer.MIN_VALUE;
		Integer bottom = Integer.MAX_VALUE;

		for (RectangleObject box : Objects) {
			if (box.getPosition().getX() < left)
				left = box.getPosition().getX();
			if ((box.getPosition().getX() + (box.getWidth() - 1)) > right)
				right = box.getPosition().getX() + (box.getWidth() - 1);
			if (box.getPosition().getY() > top)
				top = box.getPosition().getY();
			if ((box.getPosition().getY() - (box.getHeight() - 1)) < bottom)
				bottom = box.getPosition().getY() - (box.getHeight() - 1);
		}

		for (LineAssociation link : Assocs) {
			for (Point poi : link.getRoute()) {
				if (poi.getX() < left)
					left = poi.getX();
				if (poi.getX() > right)
					right = poi.getX();
				if (poi.getY() > top)
					top = poi.getY();
				if (poi.getY() < bottom)
					bottom = poi.getY();
			}
		}

		return new Boundary(top, bottom, left, right);
	}

	// end Privates

}
