package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.layout.IDiagramElementsMapper;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public class ClassDiagramPixelDimensionProvider implements IPixelDimensionProvider {

	private static final int DEFAULT_ELEMENT_WIDTH = 100;
	private static final int DEFAULT_ELEMENT_HEIGHT = 100;

	private static final int PIXELWIDTH_OF_CHARACTER = 12;
	private static final int PIXELHEIGHT_OF_PROPERTY = 25;
	private static final int MIN_CLASS_WIDTH = 100;
	private static final int MAX_CLASS_WIDTH = 200;
	private static final int MIN_CLASS_HEIGHT = 100;
	private static final int MAX_CLASS_HEIGHT = 200;

	private IDiagramElementsMapper elementsMapper;

	public ClassDiagramPixelDimensionProvider(ClassDiagramElementsMapper mapper) {
		this.elementsMapper = mapper;
	}

	@Override
	public Pair<Width, Height> getPixelDimensionsFor(RectangleObject box) {
		Integer width = DEFAULT_ELEMENT_WIDTH;
		Integer height = DEFAULT_ELEMENT_HEIGHT;

		Element elem = this.elementsMapper.findNode(box.getName());
		if (elem != null && elem instanceof Classifier) {
			width = ((Classifier) elem).getFeatures().stream()
					.mapToInt((attribute) -> attribute.getName().length() * PIXELWIDTH_OF_CHARACTER).max().orElse(0);
			width = width < MIN_CLASS_WIDTH ? MIN_CLASS_WIDTH : width;
			width = width > MAX_CLASS_WIDTH ? MAX_CLASS_WIDTH : width;

			height = ((Classifier) elem).getFeatures().size() * PIXELHEIGHT_OF_PROPERTY;
			height = height < MIN_CLASS_HEIGHT ? MIN_CLASS_HEIGHT : height;
			height = height > MAX_CLASS_HEIGHT ? MAX_CLASS_HEIGHT : height;
		}
		return Pair.of(new Width(width), new Height(height));
	}
}
