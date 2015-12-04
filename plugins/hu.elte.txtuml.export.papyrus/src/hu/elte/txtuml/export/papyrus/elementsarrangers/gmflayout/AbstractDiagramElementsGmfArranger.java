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

/**
 * An abstract class for arranging the elements with the GMF arranging algorithm. 
 *
 * @author András Dobreff
 */
public abstract class  AbstractDiagramElementsGmfArranger extends AbstractDiagramElementsArranger{

	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 */
	public AbstractDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	/**
	 * Arranges the children of an EditPart with the GMF arranging algorithm 
	 * @param parent - The children of this EditPart will be arranged
	 */
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

	/**
	 * Calls an autosize on the given GraphicalEditPart
	 * @param graphicalEditPart - The GraphicalEditPart that is to be resized
	 */
	public void autoresizeGraphicalEditPart(GraphicalEditPart graphicalEditPart) {
		List<IGraphicalEditPart> l = new ArrayList<IGraphicalEditPart>();
		l.add(graphicalEditPart);
		SizeAction action = new SizeAction(SizeAction.PARAMETER_AUTOSIZE, l);
		Command cmd = action.getCommand();
		
		if (cmd != null && cmd.canExecute()){
			cmd.execute();
		}
	}
}
