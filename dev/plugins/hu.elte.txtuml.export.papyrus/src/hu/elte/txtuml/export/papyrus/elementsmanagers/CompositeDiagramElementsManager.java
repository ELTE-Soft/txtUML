package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.composite.custom.utils.CompositeEditPartUtil;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.papyrus.api.CompositeDiagramElementsController;

public class CompositeDiagramElementsManager extends AbstractDiagramElementsManager {

	public CompositeDiagramElementsManager(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void addElementsToDiagram(List<Element> elements) {
		DiagramEditPart ep = this.diagramEditPart;
		ClassCompositeEditPart composite = (ClassCompositeEditPart) ep.getChildren().get(0);
		
		ClassCompositeCompartmentEditPart compartment = (ClassCompositeCompartmentEditPart) CompositeEditPartUtil.getCompositeCompartmentEditPart(composite);
		
		elements.forEach(e -> {
			if(e instanceof Property){
				CompositeDiagramElementsController.addPropertyToClassCompositeCompartementEditPart(compartment, (Property)e);
			}
		});
	}
}
