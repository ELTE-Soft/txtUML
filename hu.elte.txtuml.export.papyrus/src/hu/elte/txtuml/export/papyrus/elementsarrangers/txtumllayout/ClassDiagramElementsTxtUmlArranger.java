package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;

public class ClassDiagramElementsTxtUmlArranger extends AbstractDiagramElementsTxtUmlArranger{
	
	public ClassDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void arrange() {
		@SuppressWarnings("unchecked")
		List<EditPart> listEp = diagep.getChildren();
		super.arrangeAll(diagep, listEp);
		super.hideConnectionLabelsForEditParts(listEp);
	}
	
}
