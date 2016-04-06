package hu.elte.txtuml.layout.visualizer.model;

import java.util.HashSet;
import java.util.Set;

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

	// end Publics

	// Privates

	// end Privates

}
