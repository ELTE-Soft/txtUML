package hu.elte.txtuml.export.papyrus.arrange;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.diagrams.common.arrange.ArrangeException;

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
	
	public List<Point> getRouteForConnection(Element transition);

	public String getSourceAnchorForConnection(Element transition);

	public String getTargetAnchorForConnection(Element generalization);
}
