package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;

public class StateMachineDiagramPixelDimensionProvider implements IPixelDimensionProvider {

	private static final int DEFAULT_ELEMENT_WIDTH = 80;
	private static final int DEFAULT_ELEMENT_HEIGHT = 60;
	public static final int STATE_HEADER_HEIGHT = 25;
	public static final int DEFAULT_ELEMENT_BORDER = 20;

	private static final int PSEUDOSTATE_WIDTH = 20;
	private static final int PSEUDOSTATE_HEIGHT = 20;

	@Override
	public Dimension getPixelDimensionsFor(RectangleObject box) {
		int width;
		int height;
		int border = 0;
		int header = 0;
		if (!box.hasInner()) {
			if (box.isSpecial() && box.getSpecial().equals(SpecialBox.Initial)) {
				width = PSEUDOSTATE_WIDTH;
				height = PSEUDOSTATE_HEIGHT;
			} else {
				// TODO: search among simple elements / default value
				width = DEFAULT_ELEMENT_WIDTH;
				height = DEFAULT_ELEMENT_HEIGHT;
			}
		} else // if(box.hasInner())
		{
			border = DEFAULT_ELEMENT_BORDER;
			width = (int) Math.round(box.getInner().getWidth() * box.getInner().getPixelGridHorizontal());
			width += 2 * border;
			height = (int) Math.round(box.getInner().getHeight() * box.getInner().getPixelGridVertical() + STATE_HEADER_HEIGHT);
			height += 2 * border;
			header = STATE_HEADER_HEIGHT;
		}

		//return normalizeSizes(box, width, height, border); // normalizing the size can introduce corrupted pixel/grid ratios
		return new Dimension(width, height, border, header);
	}
}
