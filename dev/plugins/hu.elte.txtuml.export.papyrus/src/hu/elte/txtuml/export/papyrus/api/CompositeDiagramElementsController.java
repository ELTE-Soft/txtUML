package hu.elte.txtuml.export.papyrus.api;

import org.eclipse.papyrus.uml.diagram.common.editparts.RoundedCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeCompartmentEditPart;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

@Deprecated
public class CompositeDiagramElementsController {
	/**
	 * @param diagramEditPart
	 * @param element
	 */
	public static void addPropertyToClassCompositeCompartementEditPart(ClassCompositeCompartmentEditPart diagramEditPart, Property element){
		ElementsManagerUtils.addElementToEditPart(diagramEditPart, element);
	}

	public static void addPortToCompartmentEditPart(RoundedCompartmentEditPart classCompositeNameEditPart,
			Port p) {
		ElementsManagerUtils.addElementToEditPart(classCompositeNameEditPart, p);
	}
}
