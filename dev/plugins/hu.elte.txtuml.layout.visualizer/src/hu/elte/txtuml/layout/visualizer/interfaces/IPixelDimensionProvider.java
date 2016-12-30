package hu.elte.txtuml.layout.visualizer.interfaces;

import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * Interface for a defining pixel dimension values for a certain box.
 *
 */
public interface IPixelDimensionProvider {

	/**
	 * Dimension of a box.
	 *
	 */
	public class Dimension {
		/**
		 * Width of the box in pixels.
		 */
		public final Integer Width;
		/**
		 * Height of the box in pixels.
		 */
		public final Integer Height;
		/**
		 * If the box contains inner elements this amount of pixels must be left
		 * empty at the inner diagram's top.
		 */
		public final Integer TopExtra;
		/**
		 * If the box contains inner elements this amount of pixels must be left
		 * empty at the inner diagram's left side.
		 */
		public final Integer LeftExtra;

		/**
		 * If the box has a header this amount of pixels is the header thickness
		 */
		public final Integer Header;

		/**
		 * Creates a basic Dimension object with the given parameters.
		 * 
		 * @param w
		 *            Width.
		 * @param h
		 *            Height.
		 * @param textra
		 *            TopExtra
		 * @param lextra
		 *            LeftExtra
		 * @param header
		 *            Header
		 */
		public Dimension(Integer w, Integer h, Integer textra, Integer lextra, Integer header) {
			Width = w;
			Height = h;
			TopExtra = textra;
			LeftExtra = lextra;
			Header = header;
		}
	}

	/**
	 * Returns the Pixel values for the box's width, height and the extra
	 * boundaries. Parameter box might be flat, or might contain inner
	 * {@link Diagram} whose layout is already applied.
	 * 
	 * @param box
	 *            {@link RectangleObject} to get the dimensions for.
	 * @return Width and Height of the box in pixels.
	 */
	public Dimension getPixelDimensionsFor(RectangleObject box);
}
