package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;

import hu.elte.txtuml.export.papyrus.layout.txtuml.IDiagramElementsMapper;
import hu.elte.txtuml.export.papyrus.layout.txtuml.StateMachineDiagramElementsMapper;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public class StateMachineDiagramPixelDimensionProvider implements IPixelDimensionProvider {

	private static final int DEFAULT_ELEMENT_WIDTH = 80;
	private static final int DEFAULT_ELEMENT_HEIGHT = 60;

	private static final int MIN_STATE_WIDTH = 40;
	private static final int MAX_STATE_WIDTH = 200;
	private static final int MIN_STATE_HEIGHT = 40;
	private static final int MAX_STATE_HEIGHT = 200;

	private IDiagramElementsMapper elementsMapper;
	
	public StateMachineDiagramPixelDimensionProvider(StateMachineDiagramElementsMapper mapper) {
		this.elementsMapper = mapper;
	}

	@Override
	public Pair<Width, Height> getPixelDimensionsFor(RectangleObject box) {
		Integer width = DEFAULT_ELEMENT_WIDTH;
		Integer height = DEFAULT_ELEMENT_HEIGHT;

		Element elem = this.elementsMapper.findNode(box.getName());
		if (elem != null && elem instanceof State) {
			width = width < MIN_STATE_WIDTH ? MIN_STATE_WIDTH : width;
			width = width > MAX_STATE_WIDTH ? MAX_STATE_WIDTH : width;

			height = height < MIN_STATE_HEIGHT ? MIN_STATE_HEIGHT : height;
			height = height > MAX_STATE_HEIGHT ? MAX_STATE_HEIGHT : height;
		}else if(elem != null && elem instanceof Pseudostate){
			width = 20;
			height = 20;
		}
		return Pair.of(new Width(width), new Height(height));
	}

}
