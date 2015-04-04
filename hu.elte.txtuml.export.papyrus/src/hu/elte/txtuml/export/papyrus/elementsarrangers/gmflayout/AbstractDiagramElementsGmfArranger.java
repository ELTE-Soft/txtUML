package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.papyrus.uml.diagram.menu.actions.SizeAction;

public abstract class  AbstractDiagramElementsGmfArranger extends AbstractDiagramElementsArranger{
	
	public AbstractDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	protected void arrangeChildren(EditPart parent) {
		@SuppressWarnings("unchecked")
		List<EditPart> elements = parent.getChildren();
	
		if(!elements.isEmpty()){
			ArrangeRequest arrangeRequest = new ArrangeRequest(ActionIds.ACTION_ARRANGE_ALL);
			List<EditPart> l = new ArrayList<EditPart>();
			l.addAll(elements);
			arrangeRequest.setPartsToArrange(l);
			Command cmd = parent.getCommand(arrangeRequest);
			cmd.execute();
		}
	}

	public void autoresizeGraphicalEditPart(GraphicalEditPart graphicalEditPart) {
		List<IGraphicalEditPart> l = new ArrayList<IGraphicalEditPart>();
		l.add((IGraphicalEditPart) graphicalEditPart);
		SizeAction action = new SizeAction(SizeAction.PARAMETER_AUTOSIZE, l);
		Command cmd = action.getCommand();
		
		if (cmd != null && cmd.canExecute()){
			cmd.execute();
		}
	}
}
