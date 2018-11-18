package hu.elte.txtuml.export.javascript.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

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
	
	
	/**
	 * Creates the Rectangle set for containing the dimensions of the RectangleObjects.
	 * Flattens the RectangleObject hierarchy, bringing all inner diagrams on one level.
	 * Also applies the position offset of the hierarchy.
	 * @param ros
	 * 			the RectangleObjects to create the map from
	 * @return the flattened map
	 */
	public static Map<String, Rectangle> getFlattenedAndOffsetRectMapfromROCollection(Collection<RectangleObject> ros) {
		Map<String, Rectangle> res = new HashMap<>();
		for(RectangleObject r : ros){
			processNodeInto(r,new Point(0,0), res);
		}
		return res;
	}
	
	private static void processNodeInto(RectangleObject node, Point offset, Map<String, Rectangle> container){
		container.put(node.getName(), new Rectangle(node.getTopLeft().getX() + offset.getX(), node.getTopLeft().getY() + offset.getY(), node.getPixelWidth(), node.getPixelHeight()));
		if(node.hasInner()){
			for(RectangleObject r : node.getInner().Objects){
				processNodeInto(r, new Point(offset.getX() + node.getPosition().getX(), offset.getY() + node.getPosition().getY() - HEIGHT_PADDING), container);
			}
		}
	}
		
	/**
	 * For some reason, inner diagrams need to be pushed down by this amount to display properly.
	 * Possible correlation: StateMachineDiagramPixelDimensionProvider.PSEUDOSTATE_HEIGHT
	 */
	public static Integer HEIGHT_PADDING = 20;
}
