package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Element;

/**
 * An abstract class for adding/removing elements to diagrams.
 */
public abstract class AbstractDiagramElementsManager{
		
	/**
	 * The Diagram that is to be populated
	 */
	protected Diagram diagram;
	
	public AbstractDiagramElementsManager(Diagram diagram) {
		this.diagram = diagram;
	}
	
	/**
	 * Adds the Elements to the diagram handled by the instance.
	 * @param elements - The Elements that are to be added
	 */
	public abstract void addElementsToDiagram(List<Element> elements);
}