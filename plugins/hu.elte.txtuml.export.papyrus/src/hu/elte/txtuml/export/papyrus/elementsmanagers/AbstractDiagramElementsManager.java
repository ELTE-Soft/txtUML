package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.uml2.uml.Element;

/**
 * An abstract class for adding/removing elements to diagrams.
 *
 * @author András Dobreff
 */
public abstract class AbstractDiagramElementsManager{
		
	/**
	 * The DiagramEditPart of the diagram which is to be handled
	 */
	protected DiagramEditPart diagramEditPart;
	
	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public AbstractDiagramElementsManager(DiagramEditPart diagramEditPart) {
		this.diagramEditPart = diagramEditPart;
	}
	
	/**
	 * Adds the Elements to the diagram handled by the instance.
	 * @param elements - The Elements that are to be added
	 */
	public abstract void addElementsToDiagram(List<Element> elements);
}