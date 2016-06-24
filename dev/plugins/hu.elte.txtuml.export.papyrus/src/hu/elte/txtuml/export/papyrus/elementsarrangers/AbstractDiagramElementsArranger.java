package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider.Height;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider.Width;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

public abstract class AbstractDiagramElementsArranger implements IDiagramElementsArranger {

	protected IPixelDimensionProvider pixelDimensionProvider;

	@Override
	public abstract void arrange(IProgressMonitor monitor) throws ArrangeException;

	@Override
	public abstract Rectangle getBoundsForElement(Element element);
	
	protected void setPixelSizes(Set<RectangleObject> objects) {
		objects.forEach(object -> {
			Pair<Width, Height> dimension = this.pixelDimensionProvider.getPixelDimensionsFor(object);
			object.setPixelWidth(dimension.getFirst().Value);
			object.setPixelHeight(dimension.getSecond().Value);
		});
	}
	
	protected static DiagramType convertDiagramType(hu.elte.txtuml.layout.export.DiagramType type) {
		switch (type) {
		case Class:
			return DiagramType.Class;
		case StateMachine:
			return DiagramType.State;
		default:
			return DiagramType.unknown;
		}
	}
}
