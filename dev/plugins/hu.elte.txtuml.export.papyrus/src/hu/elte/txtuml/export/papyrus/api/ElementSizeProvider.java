package hu.elte.txtuml.export.papyrus.api;

import java.util.Collection;

import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

/**
 * Implementations provide calculations for the sizes of the given element 
 * 
 *
 */
public interface ElementSizeProvider {

	/**
	 * Sets the PixelWidth and PixelHeight for the given RectangleObject
	 * @param element
	 * @return Same element with different PixelWidth and PixelHeight values
	 */
	public RectangleObject setSizeForElement(RectangleObject element);
	
	/**
	 * Calculates the (minimum) width and height for the containing element of the given elements.
	 * The given elements should be already arranged so their positions and sizes can be used.
	 * @param elements - The Collection of the already arranged elements.
	 * @return Width and height for the element
	 */
	public Pair<Integer, Integer> CalcluateSizeForComposite(Collection<RectangleObject> elements);
	
}
