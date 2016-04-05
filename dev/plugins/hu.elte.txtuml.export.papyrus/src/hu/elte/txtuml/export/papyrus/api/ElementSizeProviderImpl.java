package hu.elte.txtuml.export.papyrus.api;

import java.util.Collection;

import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public class ElementSizeProviderImpl implements ElementSizeProvider {

	@Override
	public RectangleObject setSizeForElement(RectangleObject element) {
		element.setPixelWidth(100);
		element.setPixelHeight(100);
		return element;
	}

	@Override
	public Pair<Integer, Integer> CalcluateSizeForComposite(Collection<RectangleObject> elements) {
		return new Pair<>(400,300);
	}

}
