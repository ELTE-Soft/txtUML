package hu.elte.txtuml.export.javascript.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hu.elte.txtuml.export.diagrams.common.Point;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

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
	 *            LineAssocitaion with a valid route set (containing at least
	 *            one Point)
	 * @return The turning points of the given LineAssociation (or a point near
	 *         the middle if it's straight)
	 */
	public static List<hu.elte.txtuml.layout.visualizer.model.Point> getTurningPoints(LineAssociation link) {
		// this code assumes that at least one coordinate is provided (the
		// actual minimum is more than two) also that the links are orthogonal
		if (link.getTurns() == 0) {
			// in case of links without turns we need one "turning point" to
			// define the link's position
			List<hu.elte.txtuml.layout.visualizer.model.Point> points = link.getRoute();
			int center = points.size() / 2;
			return points.subList(center, center + 1);
		} else {
			// in case of links with turns we need the turning points
			List<hu.elte.txtuml.layout.visualizer.model.Point> points = link.getMinimalRoute();
			return points.subList(1, points.size() - 1);
		}
	}

	/**
	 * Creates a Map<String, List<Point>> from a collection of LineAssociation.
	 * Keyset will be formed from the ids of the LineAssociations, the values
	 * from the turning points of the LineAssociations.
	 * 
	 * note: Point refers to hu.elte.txtuml.export.diagrams.common.Point;
	 * 
	 * @param las
	 *            the LinekAssociations to create the map from
	 * @return the created map
	 */
	public static Map<String, List<Point>> getPointMapfromLACollection(Collection<LineAssociation> las) {
		return las.stream().collect(Collectors.toMap(LineAssociation::getId, la -> getTurningPoints(la).stream()
				.map(p -> new Point(p.getX(), p.getY())).collect(Collectors.toList())));
	}
}
