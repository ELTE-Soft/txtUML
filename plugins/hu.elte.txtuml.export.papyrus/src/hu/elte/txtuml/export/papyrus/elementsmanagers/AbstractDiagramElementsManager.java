package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.UMLModelManager;

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
	 * The ModelManager which serves the model elements
	 */
	protected UMLModelManager modelManager;
	
	/**
	 * The DiagramEditPart of the diagram which is to be handled
	 */
	protected DiagramEditPart diagramEditPart;
	
	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public AbstractDiagramElementsManager(UMLModelManager modelManager, DiagramEditPart diagramEditPart) {
		this.modelManager = modelManager;
		this.diagramEditPart = diagramEditPart;
	}
	
	/**
	 * Adds the Elements to the diagram handled by the instance.
	 * @param elements - The Elements that are to be added
	 */
	public abstract void addElementsToDiagram(List<Element> elements);
}