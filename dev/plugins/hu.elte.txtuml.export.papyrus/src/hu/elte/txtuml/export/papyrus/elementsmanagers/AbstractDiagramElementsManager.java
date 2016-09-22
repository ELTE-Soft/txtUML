package hu.elte.txtuml.export.papyrus.elementsmanagers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.notation.Diagram;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.utils.Logger;

/**
 * An abstract class for adding/removing elements to diagrams.
 */
public abstract class AbstractDiagramElementsManager {

	/**
	 * The Diagram that is to be populated
	 */
	protected Diagram diagram;

	protected IProgressMonitor monitor;

	protected AbstractDiagramElementsArranger arranger;

	protected AbstractDiagramElementsManager(Diagram diagram, IProgressMonitor monitor) {
		this.diagram = diagram;
		this.monitor = monitor;
	}

	protected void arrangeWithErrorHandling() {
		try {
			this.arranger.arrange(monitor);
		} catch (ArrangeException e) {
			String msg = "Error during the arrangement of elements: " + e.getMessage();
			Logger.sys.fatal(msg);
			throw new RuntimeException(msg);
		}
	}

	/**
	 * Adds the Elements to the diagram handled by the instance.
	 * 
	 * @param elements
	 *            - The Elements that are to be added
	 */
	public abstract void addElementsToDiagram();
}