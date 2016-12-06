package hu.elte.txtuml.export.papyrus.diagrams;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.gmf.runtime.notation.Diagram;

import hu.elte.txtuml.export.papyrus.arrange.ArrangeException;
import hu.elte.txtuml.export.papyrus.arrange.IDiagramElementsArranger;
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

	protected IDiagramElementsArranger arranger;

	protected AbstractDiagramElementsManager(Diagram diagram, IProgressMonitor monitor) {
		this.diagram = diagram;
		this.monitor = monitor;
	}

	protected void arrangeWithErrorHandling() {
		try {
			this.arranger.arrange(SubMonitor.convert(monitor).newChild(100));
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