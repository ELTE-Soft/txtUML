package hu.elte.txtuml.export.javascript.scalers;

import hu.elte.txtuml.export.javascript.json.model.smd.State;

/**
 * 
 * A class to estimate the size of a State
 *
 */
public class StateScaler extends NodeScaler {

	private State node;
	private final static int STATE_HEIGHT = 50;
	private final static int STATE_HEADER_PADING = 15;
	private final static double STATE_HEADER_FONT_WIDTH = 8.8;

	/**
	 * 
	 * Creates a NodeScaler to estimate the given node size
	 * 
	 * @param node
	 *            node for size estimation
	 */
	public StateScaler(State node) {
		this.node = node;
	}

	@Override
	protected void estimateHeight() {
		this.height = STATE_HEIGHT;
	}

	@Override
	protected void estimateWidth() {
		int headerLength = node.getName().length();
		this.width = (int) Math.ceil(headerLength * STATE_HEADER_FONT_WIDTH + STATE_HEADER_PADING * 2);
	}

}
