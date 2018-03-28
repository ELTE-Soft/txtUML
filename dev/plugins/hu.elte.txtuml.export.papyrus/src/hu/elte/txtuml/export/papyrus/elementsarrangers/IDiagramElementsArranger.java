package hu.elte.txtuml.export.papyrus.elementsarrangers;

import org.eclipse.core.runtime.IProgressMonitor;

import hu.elte.txtuml.layout.visualizer.exceptions.ArrangeException;

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
}
