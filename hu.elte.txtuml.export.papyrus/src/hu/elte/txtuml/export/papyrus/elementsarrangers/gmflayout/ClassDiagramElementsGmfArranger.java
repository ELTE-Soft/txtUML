package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicitySourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicityTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationNameEditPart;

public class ClassDiagramElementsGmfArranger extends AbstractDiagramElementsGmfArranger{

	public ClassDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void arrange() {
		super.arrangeChildren(diagep);
		@SuppressWarnings("unchecked")
		List<EditPart> listEp = diagep.getChildren();
		super.hideConnectionLabelsForEditParts(listEp, Arrays.asList(
				AssociationNameEditPart.class,
				AssociationMultiplicityTargetEditPart.class,
				AssociationMultiplicitySourceEditPart.class
				));
	}
	
}
