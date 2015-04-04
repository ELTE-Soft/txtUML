package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ControlFlowGuardEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ObjectFlowGuardEditPart;

public class ActivityDiagramElementsGmfArranger extends AbstractDiagramElementsGmfArranger {

	public ActivityDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void arrange() {
		EditPart activityEditpart = (EditPart) diagep.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		super.arrangeChildren(activityContentEditpart);
		@SuppressWarnings("unchecked")
		List<EditPart> listEp =  activityContentEditpart.getChildren();
		super.hideConnectionLabelsForEditParts(listEp, Arrays.asList(ObjectFlowGuardEditPart.class, ControlFlowGuardEditPart.class));
	}	
}
