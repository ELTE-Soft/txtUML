package hu.elte.txtuml.export.javascript.utils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.diagrams.Rectangle;

public class NodeUtils {

	/**
	 * Creates a Map<String, Rectangle> from a collection of RectangleObjects.
	 * Keyset will be formed from the ids of the RectangleObjects, the values
	 * from the RectangleObjects dimensions.
	 * 
	 * 
	 * @param ros
	 *            the RectangleObjects to create the map from
	 * @return the created map
	 */
	public static Map<String, Rectangle> getRectMapfromROCollection(Collection<RectangleObject> ros) {
		return ros.stream().collect(Collectors.toMap(RectangleObject::getName, rect -> {
			Point p = rect.getTopLeft();
			return new Rectangle(p.getX(), p.getY(), rect.getPixelWidth(), rect.getPixelHeight());
		}));
	}
}
