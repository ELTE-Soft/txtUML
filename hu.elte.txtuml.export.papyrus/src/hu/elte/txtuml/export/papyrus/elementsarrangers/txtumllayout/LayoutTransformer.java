package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.awt.BufferCapabilities.FlipContents;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NoPermissionException;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;

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
	public void doTranformations(Map<?, Point> objects) {
		translateOrigo(objects);
		if(this.flipXAxis)
			flipXAxistranformation(objects);
		
		if(this.flipYAxis)
			flipYAxistranformation(objects);
	}
	
	private void translateOrigo(Map<?, Point> objects){
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
		
		for(Entry<?, Point> object: objects.entrySet()){
			Point p = object.getValue();
			p.setX(p.x()-moveX);
			p.setY(p.y()-moveY);
		}
	}
	
	private void flipYAxistranformation(Map<?, Point> objects){
		//objects.values().stream().map(p -> p.y = -p.y);
		for(Entry<?, Point> object : objects.entrySet()){
			Point p = object.getValue();
			p.setY(-p.y);
		}
	}
	
	private void flipXAxistranformation(Map<?, Point> objects){
		objects.values().stream().map(
					p -> p.x = -p.x
				);
	}
}
