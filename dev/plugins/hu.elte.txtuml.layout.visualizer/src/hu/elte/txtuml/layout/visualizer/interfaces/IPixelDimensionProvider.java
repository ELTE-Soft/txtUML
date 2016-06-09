package hu.elte.txtuml.layout.visualizer.interfaces;

import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

/**
 * Interface for a defining pixel dimension values for a certain box.
 *
 */
public interface IPixelDimensionProvider {

	/**
	 * Class that represents the Width of a box.
	 */
	public class Width {
		/**
		 * Value of Width.
		 */
		public Integer Value;
		
		/**
		 * Create Width.
		 * @param val Value of Width.
		 */
		public Width(Integer val){
			Value = val;
		}
	}
	
	/**
	 * Class that represents the Height of a box.
	 */
	public class Height {
		/**
		 * Value of Height.
		 */
		public Integer Value;
		
		/**
		 * Create Height.
		 * @param val Value of Height.
		 */
		public Height(Integer val){
			Value = val;
		}
	}
	
	/**
	 * Returns the Pixel values for the box's width and height in
	 * a format of Pair<WIDTH, HEIGHT>.
	 * Parameter box might be flat, or might contain inner {@link Diagram} 
	 * whose layout is already applied.
	 * @param box {@link RectangleObject} to get the dimensions for.
	 * @return Width and Height of the box in pixels.
	 */
	public Pair<Width, Height> getPixelDimensionsFor(RectangleObject box);
}
