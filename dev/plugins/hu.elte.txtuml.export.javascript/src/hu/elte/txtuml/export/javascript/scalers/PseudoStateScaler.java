package hu.elte.txtuml.export.javascript.scalers;

import org.eclipse.uml2.uml.PseudostateKind;

import hu.elte.txtuml.export.javascript.json.model.smd.PseudoState;

/**
 * 
 * A class to estimate the size of a PseudoState
 *
 */
public class PseudoStateScaler extends NodeScaler {

	private PseudoState node;
	private final static int MIN_SIZE = 25;
	private final static double FONT_WIDTH = 7.2;
	private final static double FONT_HEIGHT = 12;
	private final static int CHOICE_TEXT_PADING = 5;

	/**
	 * 
	 * Creates a NodeScaler to estimate the given node size
	 * 
	 * @param node
	 *            node for size estimation
	 */
	public PseudoStateScaler(PseudoState node) {
		this.node = node;
	}

	@Override
	protected void estimateHeight() {
		height = this.getWidth();
	}

	@Override
	protected void estimateWidth() {
		if (node.getKind() == PseudostateKind.CHOICE_LITERAL) {
			double totalwidth = node.getName().length() * FONT_WIDTH + CHOICE_TEXT_PADING * 2 + FONT_HEIGHT
					+ CHOICE_TEXT_PADING * 2;
			width = (int) Math.ceil(Math.max(totalwidth, MIN_SIZE));
		} else {
			width = MIN_SIZE;
		}
	}

}
