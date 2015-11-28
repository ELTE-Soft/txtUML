package hu.elte.txtuml.export.papyrus.api;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.papyrus.uml.diagram.activity.edit.part.CustomActivityActivityContentCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ActivityDiagramEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ActivityEditPart;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

/**
 *
 */
public class ActivityDiagramElementsController {
	
	/**
	 * @param diagramEditPart
	 * @param element
	 */
	public static void addElementToActivityDiagram(ActivityDiagramEditPart diagramEditPart, Element element){
		Assert.isTrue(diagramEditPart.getChildren().get(0) instanceof ActivityEditPart);
		ActivityEditPart activityEditpart = (ActivityEditPart) diagramEditPart.getChildren().get(0);
		Assert.isTrue(activityEditpart.getChildren().get(5) instanceof CustomActivityActivityContentCompartmentEditPart);
		CustomActivityActivityContentCompartmentEditPart activityContentEditpart = (CustomActivityActivityContentCompartmentEditPart) activityEditpart.getChildren().get(5);
		ElementsManagerUtils.addElementToEditPart(activityContentEditpart, element);
	}
	
	/**
	 * @param diagramEditPart
	 * @param elements 
	 */
	public static void addElementsToActivityDiagram(ActivityDiagramEditPart diagramEditPart,Collection<Element> elements){
		Assert.isTrue(diagramEditPart.getChildren().get(0) instanceof ActivityEditPart);
		ActivityEditPart activityEditpart = (ActivityEditPart) diagramEditPart.getChildren().get(0);
		Assert.isTrue(activityEditpart.getChildren().get(5) instanceof CustomActivityActivityContentCompartmentEditPart);
		CustomActivityActivityContentCompartmentEditPart activityContentEditpart = (CustomActivityActivityContentCompartmentEditPart) activityEditpart.getChildren().get(5);
		ElementsManagerUtils.addElementsToEditPart(activityContentEditpart, elements);
	}
}
