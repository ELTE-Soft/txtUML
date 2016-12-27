package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;

import hu.elte.txtuml.export.papyrus.layout.IDiagramElementsMapper;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;

public class StateMachineDiagramPixelDimensionProvider implements IPixelDimensionProvider {

	private static final int DEFAULT_ELEMENT_WIDTH = 80;
	private static final int DEFAULT_ELEMENT_HEIGHT = 60;
	public static final int STATE_HEADER_HEIGHT = 25;
	public static final int DEFAULT_ELEMENT_BORDER = 20;

	private static final int MIN_STATE_WIDTH = 40;
	private static final int MAX_STATE_WIDTH = 800;
	private static final int MIN_STATE_HEIGHT = 40;
	private static final int MAX_STATE_HEIGHT = 800;

	private static final int PSEUDOSTATE_WIDTH = 20;
	private static final int PSEUDOSTATE_HEIGHT = 20;

	private IDiagramElementsMapper elementsMapper;

	public StateMachineDiagramPixelDimensionProvider(StateMachineDiagramElementsMapper mapper) {
		this.elementsMapper = mapper;
	}

	@Override
	public Dimension getPixelDimensionsFor(RectangleObject box) {
		int width;
		int height;
		int border = 0;

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
			width = box.getInner().getWidth() * box.getInner().getPixelGridHorizontal();
			width += 2 * border;
			height = box.getInner().getHeight() * box.getInner().getPixelGridVertical() + STATE_HEADER_HEIGHT;
			height += 2 * border;
		}

		return normalizeSizes(box, width, height, border);
	}

	private Dimension normalizeSizes(RectangleObject box, int width, int height, 
			int border) {

		Element elem = this.elementsMapper.findNode(box.getName());
		if (elem != null && elem instanceof State) {
			width = width < MIN_STATE_WIDTH ? MIN_STATE_WIDTH : width;
			width = width > MAX_STATE_WIDTH ? MAX_STATE_WIDTH : width;

			height = height < MIN_STATE_HEIGHT ? MIN_STATE_HEIGHT : height;
			height = height > MAX_STATE_HEIGHT ? MAX_STATE_HEIGHT : height;
		} else if (elem != null && elem instanceof Pseudostate) {
			width = PSEUDOSTATE_WIDTH;
			height = PSEUDOSTATE_HEIGHT;
		}
		
		return new Dimension(width, height, border, border);
	}
}
