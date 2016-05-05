package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The transformer class which transforms the object coordinates to fit the GMF diagram
 */
public class LayoutTransformer {
	
	private int scaleX;
	private int scaleY;

	
	/**
	 * Constructor for {@link LayoutTransformer}
	 * @param scaleValue - The scaling number. All coordinates will be multiplied by this value
	 */
	public LayoutTransformer(int scaleValueHorizontal, int scaleValueVertical ) {
		this.scaleX = scaleValueHorizontal;
		this.scaleY = scaleValueVertical;
	}
	
	/**
	 * Transforms the positions of objects and connections with the given settings (scaling, gap, axisflipping)
	 * @param objects - Map of objects and the representing Rectangular shapes which position
	 *  is to be modified, but the width and height have the real values  
	 * @param connections  -  Map of connections and the representing List of Points
	 */
	public void doTranformations(Map<?, Rectangle> objects, Map<?, List<Point>> connections) {
		flipYAxis(objects, connections);
		translateOrigo(objects, connections);
		scaleUpObjects(objects, connections);
	}
	
	
	/**
	 * Scales the objects according to the scales and gaps
	 * @param objects - The object that are to be scaled 
	 * @param connections - The connections that are to be scaled
	 */
	private void scaleUpObjects(Map<?, Rectangle> objects, Map<?, List<Point>> connections) {
		for(Rectangle rect : objects.values()){
			rect.setX(rect.x()*(this.scaleX));
			rect.setY(rect.y()*(this.scaleY));
		}
		
		for(List<Point> pointlist: connections.values()){
			for(Point p : pointlist){
				p.setX(p.x()*this.scaleX);
				p.setY(p.y()*this.scaleY);
			}
		}
	}
	
	/**
	 * Translates the objects to fit the origoConstraint
	 * @param objects - The object that are to be translated 
	 * @param connections - The connections that are to be translated 
	 */
	private void translateOrigo(Map<?, Rectangle> objects, Map<?, List<Point>> connections){
		int moveX = getBoundaryValue(objects, connections, r -> r.x(), p -> p.x());
		int moveY = getBoundaryValue(objects, connections, r -> r.y(), p -> p.y());
		
		for(Rectangle rect: objects.values()){
			rect.setX(rect.x()-moveX);
			rect.setY(rect.y()-moveY);
		}
		
		for(Entry<?, List<Point>> connection: connections.entrySet()){
			List<Point> pointlist = connection.getValue();
			for(Point p: pointlist){
				p.setX(p.x()-moveX);
				p.setY(p.y()-moveY);
			}
		}
	}
	
	private int getBoundaryValue(Map<?, Rectangle> objects, Map<?, List<Point>> connections,
			Function<Rectangle, Integer> objectMapping, Function<Point, Integer> connectionMapping) {
		
		Comparator<Integer> comparator = (p1, p2) -> {
			return Integer.compare(p1, p2);
		};
		
		int first = objects.values().stream().map(objectMapping).min(comparator).orElse(Integer.MAX_VALUE);
		int second = connections.values().stream()
			.map(list -> list.stream().map(connectionMapping).min(comparator).orElse(Integer.MAX_VALUE)).min(comparator)
			.orElse(Integer.MAX_VALUE);
		return (first < second) ? first : second;
	}
	
	/**
	 * Multiplies the y coordinates of the objects with -1. From now on they can be handled
	 *  as if the Y axis would be flipped
	 * @param objects - The object that's y coordinates are to be multiplied
	 * @param connections - The connections that's y coordinates are to be multiplied
	 */
	private void flipYAxis(Map<?, Rectangle> objects, Map<?, List<Point>> connections){
		for(Rectangle rect : objects.values()){
			rect.setY(-rect.y);
		}
		
		for(List<Point> pointlist : connections.values()){
			for(Point p: pointlist){
				p.setY(-p.y);
			}
		}
	}	
}
