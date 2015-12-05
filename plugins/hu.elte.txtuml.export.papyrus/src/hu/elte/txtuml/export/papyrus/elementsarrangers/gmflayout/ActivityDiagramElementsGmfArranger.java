package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ControlFlowGuardEditPart;
import org.eclipse.papyrus.uml.diagram.activity.edit.parts.ObjectFlowGuardEditPart;

/**
 * Controls the arranging of an ActivityDiagram with GMF algorithm
 *
 * @author András Dobreff
 */
public class ActivityDiagramElementsGmfArranger extends AbstractDiagramElementsGmfArranger {
	
	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 */
	public ActivityDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger#arrange()
	 */
	@Override
	public void arrange(IProgressMonitor monitor) {
		monitor.beginTask("Arrange", 1);
		monitor.subTask("Arranging elements...");
		EditPart activityEditpart = (EditPart) diagep.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		super.arrangeChildren(activityContentEditpart);
		@SuppressWarnings("unchecked")
		List<EditPart> listEp =  activityContentEditpart.getChildren();
		DiagramElementsModifier.hideConnectionLabelsForEditParts(listEp, Arrays.asList(ObjectFlowGuardEditPart.class, ControlFlowGuardEditPart.class));
		monitor.worked(1);
	}	
}
