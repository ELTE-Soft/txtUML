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
	
	/**
	 * Defines where should the origin be form the diagram elements 
	 */
	@SuppressWarnings("javadoc")
	public enum OrigoConstraint{
		UpperLeft, UpperRight, 
		BottomLeft,BottomRight, None
	}
	
	private int scaleX;
	private int scaleY;

	private OrigoConstraint origoConstaint;
	private boolean flipXAxis;
	private boolean flipYAxis;
	
	/**
	 * Constructor for {@link LayoutTransformer}
	 * @param scaleValue - The scaling number. All coordinates will be multiplied by this value
	 */
	public LayoutTransformer(int scaleValue ) {
		this.scaleX = scaleValue;
		this.scaleY = scaleValue;
		init();
	}
	
	private void init(){
		this.origoConstaint = OrigoConstraint.None;
		this.flipXAxis = false;
		this.flipYAxis = false;
	}
	
	/**
	 * Flips the X Axis
	 */
	public void flipXAxis(){
		this.flipXAxis = !this.flipXAxis;
	}
	
	/**
	 * Flips the Y Axis
	 */
	public void flipYAxis(){
		this.flipYAxis = !this.flipYAxis;
	}

	/**
	 * Sets a Constraint where the Origo should be
	 * @param origo - {@link OrigoConstraint}
	 */
	public void setOrigo(OrigoConstraint origo) {
		this.origoConstaint = origo;
	}

	/**
	 * Transforms the positions of objects and connections with the given settings (scaling, gap, axisflipping)
	 * @param objects - Map of objects and the representing Rectangular shapes which position
	 *  is to be modified, but the width and height have the real values  
	 * @param connections  -  Map of connections and the representing List of Points
	 */
	public void doTranformations(Map<?, Rectangle> objects, Map<?, List<Point>> connections) {
		translateOrigo(objects, connections);
		if(this.flipXAxis)
			flipXAxisTranformation(objects, connections);
		
		if(this.flipYAxis)
			flipYAxisTranformation(objects, connections);
		
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
		int moveX = 0;
		int moveY = 0;
		switch(this.origoConstaint){
			case BottomLeft:
				moveX = getBoundaryValue(objects, connections, r -> r.x(), p -> p.x(), true);
				moveY = getBoundaryValue(objects, connections, r -> r.y(), p -> p.y(), true);
				break;
			case BottomRight:
				moveX = getBoundaryValue(objects, connections, r -> r.x(), p -> p.x(), false);
				moveY = getBoundaryValue(objects, connections, r -> r.y(), p -> p.y(), true);
				break;
			case UpperRight:
				moveX = getBoundaryValue(objects, connections, r -> r.x(), p -> p.x(), false);
				moveY = getBoundaryValue(objects, connections, r -> r.y(), p -> p.y(), false);
				break;
			case UpperLeft:
				moveX = getBoundaryValue(objects, connections, r -> r.x(), p -> p.x(), true);
				moveY = getBoundaryValue(objects, connections, r -> r.y(), p -> p.y(), false);
				break;
			default: 
				return;
		}
		
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
			Function<Rectangle, Integer> objectMapping, Function<Point, Integer> connectionMapping, Boolean isMin) {
		Comparator<Integer> comparator = (p1, p2) -> {
			return Integer.compare(p1, p2);
		};

		if (isMin) {
			int first = objects.values().stream().map(objectMapping).min(comparator).orElse(Integer.MAX_VALUE);
			int second = connections.values().stream()
					.map(list -> list.stream().map(connectionMapping).min(comparator).orElse(Integer.MAX_VALUE)).min(comparator)
					.orElse(Integer.MAX_VALUE);
			return (first > second) ? first : second;
		} else {
			int first = objects.values().stream().map(objectMapping).max(comparator).orElse(Integer.MIN_VALUE);
			int second = connections.values().stream()
					.map(list -> list.stream().map(connectionMapping).max(comparator).orElse(Integer.MIN_VALUE)).max(comparator)
					.orElse(Integer.MIN_VALUE);
			return (first > second) ? first : second;
		}
	}
	
	/**
	 * Multiplies the y coordinates of the objects with -1. From now on they can be handled
	 *  as if the Y axis would be flipped
	 * @param objects - The object that's y coordinates are to be multiplied
	 * @param connections - The connections that's y coordinates are to be multiplied
	 */
	private void flipYAxisTranformation(Map<?, Rectangle> objects, Map<?, List<Point>> connections){
		for(Rectangle rect : objects.values()){
			rect.setY(-rect.y);
		}
		
		for(List<Point> pointlist : connections.values()){
			for(Point p: pointlist){
				p.setY(-p.y);
			}
		}
	}
	
	/**
	 * Multiplies the x coordinates of the objects with -1. From now on they can be handled
	 *  as the X axis would be flipped
	 * @param objects - The object that's x coordinates are to be multiplied
	 * @param connections - The connections that's x coordinates are to be multiplied
	 */
	private void flipXAxisTranformation(Map<?, Rectangle> objects, Map<?, List<Point>> connections){
		for(Rectangle rect : objects.values()){
			rect.setX(-rect.x);
		}
		
		for(List<Point> pointlist : connections.values()){
			for(Point p: pointlist){
				p.setX(-p.x);
			}
		}
	}
}
