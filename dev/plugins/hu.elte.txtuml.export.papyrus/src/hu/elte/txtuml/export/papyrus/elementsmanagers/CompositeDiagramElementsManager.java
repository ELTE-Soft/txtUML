package hu.elte.txtuml.export.papyrus.elementsmanagers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.notation.Diagram;

public class CompositeDiagramElementsManager extends AbstractDiagramElementsManager {

	protected CompositeDiagramElementsManager(Diagram diagram) {
		super(diagram);
	}

	protected CompositeDiagramElementsManager(Diagram diagram, IProgressMonitor monitor) {
		super(diagram, monitor);
	}

	@Override
	public void addElementsToDiagram() {
		// TODO: Implement
	}
}
