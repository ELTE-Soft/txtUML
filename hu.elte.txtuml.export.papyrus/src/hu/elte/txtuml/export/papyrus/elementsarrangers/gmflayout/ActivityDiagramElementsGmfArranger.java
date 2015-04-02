package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;

public class ActivityDiagramElementsGmfArranger extends AbstractDiagramElementsGmfArranger {

	public ActivityDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void arrange() {
		EditPart activityEditpart = (EditPart) diagep.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		@SuppressWarnings("unchecked")
		List<EditPart> listEp =  activityContentEditpart.getChildren();
		super.arrangeAll(activityContentEditpart, listEp);
		super.hideConnectionLabelsForEditParts(listEp);	}	
}
