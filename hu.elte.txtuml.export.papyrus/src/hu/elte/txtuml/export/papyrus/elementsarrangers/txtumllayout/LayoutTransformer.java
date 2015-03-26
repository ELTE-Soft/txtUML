package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Represents a Transformer, considering the unity scale.
 * Applies transformations on the given Objects' coordinates
 * @author Dobreff András
 */
public class LayoutTransformer {

	/**
	 * Constraints to the Position of the Origo
	 * @author Andris
	 */
	public enum OrigoConstraint{
		UpperLeft, UpperRight, BottomLeft, BottomRight, None
	};

	private int scaleX;
	private int scaleY;
	private int gapX;
	private int gapY;
	private OrigoConstraint origoConstaint;
	private boolean flipXAxis;
	private boolean flipYAxis;
	/**
	 * Constructor for {@link LayoutTransformer}
	 * @param scaleX - Multiplier of the X coordinates
	 * @param scaleY - Multiplier of the Y coordinates
	 */
	public LayoutTransformer(int scaleX, int scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		init();
	}
	
	private void init(){
		this.gapX = 0;
		this.gapY = 0;
		this.origoConstaint = OrigoConstraint.None;
		this.flipXAxis = false;
		this.flipYAxis = false;
	}

	/**
	 * Sets the vertical gap between objects in neighborhood
	 * @param gapX
	 */
	public void setGapX(int gapX) {
		this.gapX = gapX;
	}
	
	/**
	 * Sets the horizontal gap between objects in neighborhood
	 * @param gapY
	 */
	public void setGapY(int gapY) {
		this.gapY = gapY;
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
	 * Transforms the Points with the given settings (scaling, gap, axisfilling)
	 * @param objects
	 */
	public void doTranformations(Map<?, Rectangle> objects, Map<?, List<Point>> connections) {
		translateOrigo(objects, connections);
		if(this.flipXAxis)
			flipXAxisTranformation(objects, connections);
		
		if(this.flipYAxis)
			flipYAxisTranformation(objects, connections);
		
		scaleUpObjects(objects, connections);
	}
	
	private void scaleUpObjects(Map<?, Rectangle> objects, Map<?, List<Point>> connections) {
		for(Rectangle rect : objects.values()){
			int shiftToCenterX = Math.abs(scaleX-rect.width())/2;
			int shiftToCenterY = Math.abs(scaleY-rect.height())/2;
			rect.setX(rect.x()*(this.scaleX+this.gapX)+shiftToCenterX);
			rect.setY(rect.y()*(this.scaleY+this.gapY)+shiftToCenterY);
		}
		
		for(List<Point> pointlist: connections.values()){
			for(Point p : pointlist){
				p.setX(p.x()*(this.scaleX+this.gapX)+this.scaleX/2);
				p.setY(p.y()*(this.scaleY+this.gapY)+this.scaleY/2);
			}
		}
	}

	private void translateOrigo(Map<?, Rectangle> objects, Map<?, List<Point>> connections){
		int moveX = 0;
		int moveY = 0;
		switch(this.origoConstaint){
			case BottomLeft:
					moveX = objects.values().stream().map(p -> p.x()).min((p1, p2) -> Integer.compare(p1, p2)).get();
					moveY = objects.values().stream().map(p -> p.y()).min((p1, p2) -> Integer.compare(p1, p2)).get();  
				break;
			case BottomRight:
					moveX = objects.values().stream().map(p -> p.x()).max((p1, p2) -> Integer.compare(p1, p2)).get();
					moveY = objects.values().stream().map(p -> p.y()).min((p1, p2) -> Integer.compare(p1, p2)).get();
				break;
			case  UpperRight:
					moveX = objects.values().stream().map(p -> p.x()).max((p1, p2) -> Integer.compare(p1, p2)).get();
					moveY = objects.values().stream().map(p -> p.y()).max((p1, p2) -> Integer.compare(p1, p2)).get();
				break;
			case UpperLeft:
					moveX = objects.values().stream().map(p -> p.x()).min((p1, p2) -> Integer.compare(p1, p2)).get();
					moveY = objects.values().stream().map(p -> p.y()).max((p1, p2) -> Integer.compare(p1, p2)).get();
				break;
			default: 
				moveX = 0;
				moveY = 0;
		}
		
		for(Rectangle rect: objects.values()){
			rect.setX(rect.x()-moveX);
			rect.setY(rect.y()-moveY);
		}
		
		for(Entry<?, List<Point>> connection: connections.entrySet()){
			List<Point> pointlist = (List<Point>) connection.getValue();
			for(Point p: pointlist){
				p.setX(p.x()-moveX);
				p.setY(p.y()-moveY);
			}
		}
	}
	
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
