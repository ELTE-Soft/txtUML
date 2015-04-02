package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ControlFlowGuardEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ObjectFlowGuardEditPart;

public class ActivityDiagramElementsTxtUmlArranger extends AbstractDiagramElementsTxtUmlArranger {

	public ActivityDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void arrange() {
		EditPart activityEditpart = (EditPart) diagep.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		@SuppressWarnings("unchecked")
		List<EditPart> listEp =  activityContentEditpart.getChildren();
		super.arrangeAll(activityContentEditpart, listEp);
		super.hideConnectionLabelsForEditParts(listEp, Arrays.asList(ObjectFlowGuardEditPart.class, ControlFlowGuardEditPart.class));
	}	
}
