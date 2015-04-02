package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;

public abstract class  AbstractDiagramElementsGmfArranger extends AbstractDiagramElementsArranger{
	
	public AbstractDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

		protected void arrangeAll(EditPart EP, List<EditPart> elements) {
		if(!elements.isEmpty()){
			ArrangeRequest arrangeRequest = new ArrangeRequest(ActionIds.ACTION_ARRANGE_ALL);
			List<EditPart> l = new ArrayList<EditPart>();
			l.addAll(elements);
			arrangeRequest.setPartsToArrange(l);
			Command cmd = EP.getCommand(arrangeRequest);
			cmd.execute();
		}
	}
}
