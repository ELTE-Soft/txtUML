package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;

public class ClassDiagramElementsArranger extends AbstractDiagramElementsArranger{

	@Override
	public void arrange(DiagramEditPart diagep) {
		@SuppressWarnings("unchecked")
		List<EditPart> listEp = diagep.getChildren();
		super.arrangeAll(diagep, listEp);
		super.hideConnectionLabelsForEditParts(listEp);
	}
	
}
