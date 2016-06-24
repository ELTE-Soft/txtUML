package hu.elte.txtuml.export.papyrus.elementsarrangers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;

public abstract class AbstractDiagramElementsArranger implements IDiagramElementsArranger {

	protected IPixelDimensionProvider pixelDimensionProvider;

	@Override
	public abstract void arrange(IProgressMonitor monitor) throws ArrangeException;

	@Override
	public abstract Rectangle getBoundsForElement(Element element);
	
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
