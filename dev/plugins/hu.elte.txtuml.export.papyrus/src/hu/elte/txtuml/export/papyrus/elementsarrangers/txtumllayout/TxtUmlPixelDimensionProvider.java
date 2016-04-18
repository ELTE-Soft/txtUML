package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public class TxtUmlPixelDimensionProvider implements IPixelDimensionProvider {

	@Override
	public Pair<Integer, Integer> getPixelDimensionsFor(RectangleObject box) {
		// TODO Auto-generated method stub
		return new Pair<Integer, Integer>(10,10);
	}

}
