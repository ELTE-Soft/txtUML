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
		public final Integer TopBorder;
		/**
		 * If the box contains inner elements this amount of pixels must be left
		 * empty at the inner diagram's left side.
		 */
		public final Integer LeftBorder;

		/**
		 * If the box contains inner elements this amount of pixels must be left
		 * empty at the inner diagram's top.
		 */
		public final Integer BottomBorder;
		/**
		 * If the box contains inner elements this amount of pixels must be left
		 * empty at the inner diagram's left side.
		 */
		public final Integer RightBorder;

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
		 * @param tborder
		 *            TopBorder
		 * @param rborder
		 *            RightBorder
		 * @param bborder
		 *            BottomBorder
		 * @param lborder
		 *            LeftBorder
		 * @param header
		 *            Header
		 */
		public Dimension(Integer w, Integer h, Integer tborder, Integer rborder, Integer bborder, Integer lborder,
				Integer header) {
			Width = w;
			Height = h;
			TopBorder = tborder;
			LeftBorder = lborder;
			BottomBorder = bborder;
			RightBorder = rborder;
			Header = header;
		}

		/**
		 * Creates a basic Dimension object with the given parameters.
		 * 
		 * @param w
		 *            Width.
		 * @param h
		 *            Height.
		 * @param tborder
		 *            TopBorder
		 * @param rborder
		 *            RightBorder
		 * @param bborder
		 *            BottomBorder
		 * @param lborder
		 *            LeftBorder
		 */
		public Dimension(Integer w, Integer h, Integer tborder, Integer rborder, Integer bborder, Integer lborder) {
			this(w, h, tborder, rborder, bborder, lborder, 0);
		}

		/**
		 * Creates a basic Dimension object with the given parameters.
		 * 
		 * @param w
		 *            Width.
		 * @param h
		 *            Height.
		 * @param border
		 *            Border
		 * @param header
		 *            Header
		 */
		public Dimension(Integer w, Integer h, Integer border, Integer header) {
			this(w, h, border, border, border, border, header);
		}

		/**
		 * Creates a basic Dimension object with the given parameters.
		 * 
		 * @param w
		 *            Width.
		 * @param h
		 *            Height.
		 * @param border
		 *            Border
		 */
		public Dimension(Integer w, Integer h, Integer border) {
			this(w, h, border, border, border, border, 0);
		}

		/**
		 * Creates a basic Dimension object with the given parameters.
		 * 
		 * @param w
		 *            Width.
		 * @param h
		 *            Height.
		 */
		public Dimension(Integer w, Integer h) {
			this(w, h, 0, 0, 0, 0, 0);
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
