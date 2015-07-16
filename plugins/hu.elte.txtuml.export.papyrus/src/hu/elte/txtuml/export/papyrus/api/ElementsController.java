package hu.elte.txtuml.export.papyrus.api;

import java.util.Collection;

import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ActivityEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ModelEditPart;
import org.eclipse.uml2.uml.Element;

/**
 *
 * @author András Dobreff
 */
public class ElementsController {
	
	/**
	 * @param diagramEditPart
	 * @param element
	 */
	public static void addElementToClassDiagram(ModelEditPart diagramEditPart, Element element){
		ElementsManagerUtils.addElementToEditPart(diagramEditPart, element);
	}
	
	/**
	 * @param diagramEditPart
	 * @param elements
	 */
	public static void addElementsToClassDiagram(ModelEditPart diagramEditPart, Collection<Element> elements){
		ElementsManagerUtils.addElementsToEditpart(diagramEditPart, elements);
	}
	
	/**
	 * @param diagramEditPart
	 * @param element
	 */
	public static void addElementToActivityDiagram(ActivityEditPart diagramEditPart, Element element){
		ElementsManagerUtils.addElementToEditPart(diagramEditPart, element);
	}
	
	/**
	 * @param diagramEditPart
	 * @param elements 
	 */
	public static void addElementsToActivityDiagram(ActivityEditPart diagramEditPart,Collection<Element> elements){
		ElementsManagerUtils.addElementsToEditpart(diagramEditPart, elements);
	}
	
	
}
