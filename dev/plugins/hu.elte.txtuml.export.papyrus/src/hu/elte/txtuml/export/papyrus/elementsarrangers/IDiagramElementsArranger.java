package hu.elte.txtuml.export.papyrus.elementsarrangers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;

/**
 * An Interface for arranging elements of a diagram 
 */
public interface IDiagramElementsArranger {
	
	/**
	 * Arranges the elements of the diagram
	 * @param monitor 
	 * @throws ArrangeException - The arranging algorithms may throw this exception
	 */
	public void arrange(IProgressMonitor monitor) throws ArrangeException;

	public Rectangle getBoundsForElement(Element element);
}
