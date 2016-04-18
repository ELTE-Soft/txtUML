package hu.elte.txtuml.layout.visualizer.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import hu.elte.txtuml.utils.Pair;

public class Diagram {

	// Variables

	public Set<RectangleObject> Objects;
	public Set<LineAssociation> Assocs;

	// end Variables

	// Ctors

	public Diagram() {
		Objects = new HashSet<RectangleObject>();
		Assocs = new HashSet<LineAssociation>();
	}

	public Diagram(Diagram diag) {
		Objects = new HashSet<RectangleObject>(diag.Objects);
		Assocs = new HashSet<LineAssociation>(diag.Assocs);
	}

	public Diagram(Set<RectangleObject> os, Set<LineAssociation> as) {
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
	public boolean hasLayout() {
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
	 */
	public Integer getWidth() {
		return getDimensions().getFirst();
	}

	/**
	 * Returns the grid height of this {@link Diagram}. This method is
	 * calculation heavy.
	 * 
	 * @return the grid height of this {@link Diagram}.
	 */
	public Integer getHeight() {
		return getDimensions().getSecond();
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
		
		for(RectangleObject box : Objects)
		{
			if(!box.isSpecial())
			{
				gridSum += (gridSelector.apply(box) - 1);
				pixelSum += pixelSelector.apply(box);
			}
		}
				
		return pixelSum / gridSum;
	}
	
	private Pair<Integer, Integer> getDimensions() {
		Pair<Integer, Integer> horizontal = new Pair<Integer, Integer>(0, 0);
		Pair<Integer, Integer> vertical = new Pair<Integer, Integer>(0, 0);

		for (RectangleObject box : Objects) {
			if (box.getPosition().getX() < horizontal.getFirst())
				horizontal = Pair.of(box.getPosition().getX(), horizontal.getSecond());
			if ((box.getPosition().getX() + box.getWidth()) > horizontal.getSecond())
				horizontal = Pair.of(horizontal.getFirst(), box.getPosition().getX());
			if (box.getPosition().getY() > vertical.getFirst())
				vertical = Pair.of(box.getPosition().getY(), vertical.getSecond());
			if ((box.getPosition().getY() - box.getHeight()) < horizontal.getSecond())
				vertical = Pair.of(vertical.getFirst(), box.getPosition().getY());
		}

		for (LineAssociation link : Assocs) {
			for (Point poi : link.getRoute()) {
				if (poi.getX() < horizontal.getFirst())
					horizontal = Pair.of(poi.getX(), horizontal.getSecond());
				if (poi.getX() > horizontal.getSecond())
					horizontal = Pair.of(horizontal.getFirst(), poi.getX());
				if (poi.getY() > vertical.getFirst())
					vertical = Pair.of(poi.getY(), vertical.getSecond());
				if (poi.getY() < horizontal.getSecond())
					vertical = Pair.of(vertical.getFirst(), poi.getY());
			}
		}

		return Pair.of(Math.abs(horizontal.getSecond() - horizontal.getFirst()), 
				Math.abs(vertical.getSecond() - vertical.getFirst()));
	}

	
	// end Privates

}
