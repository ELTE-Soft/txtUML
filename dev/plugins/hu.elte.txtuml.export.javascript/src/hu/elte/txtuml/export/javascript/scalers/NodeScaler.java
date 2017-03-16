package hu.elte.txtuml.export.javascript.scalers;

/**
 * 
 * BaseClass for estimating sizes of diagram nodes
 *
 */
public abstract class NodeScaler {
	protected Integer width;
	protected Integer height;

	/**
	 * Estimates the given node width (if needed) then returns it's width
	 * 
	 * @return the width of the given node
	 */
	public Integer getWidth() {
		if (width == null) {
			estimateWidth();
		}
		return width;
	}

	/**
	 * Estimates the given node height (if needed) then returns it's height
	 * 
	 * @return the height of the given node
	 */
	public Integer getHeight() {
		if (height == null) {
			estimateHeight();
		}
		return height;
	}

	/**
	 * Implementation must set height to non null
	 */
	protected abstract void estimateHeight();

	/**
	 * Implementation must set width to non null
	 */
	protected abstract void estimateWidth();
}
