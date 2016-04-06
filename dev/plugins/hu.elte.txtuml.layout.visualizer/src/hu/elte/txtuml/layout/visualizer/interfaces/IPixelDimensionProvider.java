package hu.elte.txtuml.layout.visualizer.interfaces;

import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public interface IPixelDimensionProvider {

	/**
	 * Returns the Pixel values for the box's width and height in
	 * a format of Pair<WIDTH, HEIGHT>.
	 * Parameter box might be flat, or might contain inner {@link Diagram} 
	 * whose layout is already applied.
	 * @param box {@link RectangleObject} to get the dimensions for.
	 * @return Width and Height of the box in pixels.
	 */
	public Pair<Integer, Integer> getPixelDimensionsFor(RectangleObject box);
}
