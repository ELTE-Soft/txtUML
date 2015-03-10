package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;

public class ClassDiagramElementsGmfArranger extends AbstractDiagramElementsGmfArranger{

	@Override
	public void arrange(DiagramEditPart diagep) {
		@SuppressWarnings("unchecked")
		List<EditPart> listEp = diagep.getChildren();
		super.arrangeAll(diagep, listEp);
		super.hideConnectionLabelsForEditParts(listEp);
	}
	
}
