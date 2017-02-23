package hu.elte.txtuml.export.javascript.utils;

import java.util.List;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

/***
 * 
 * Provides helper functions for LineAssociations
 *
 */
public class LinkUtils {
	/***
	 * A function to get turning points from the given LineAssociation
	 *  
	 * @param link
	 *            LineAssocitaion with a valid route set (containing at least one Point)
	 * @return The turning points of the given LineAssociation (or a point near
	 *         the middle if it's straight)
	 */
	public static List<Point> getTurningPoints(LineAssociation link) {
		// this code assumes that at least one coordinate is provided (the
		// actual minimum is more than two) also that the links are orthogonal
		if (link.getTurns() == 0) {
			// in case of links without turns we need one "turning point" to
			// define the link's position
			List<Point> points = link.getRoute();
			int center = points.size() / 2;
			return points.subList(center, center + 1);
		} else {
			// in case of links with turns we need the turning points
			List<Point> points = link.getMinimalRoute();
			return points.subList(1, points.size() - 1);
		}
	}
}
