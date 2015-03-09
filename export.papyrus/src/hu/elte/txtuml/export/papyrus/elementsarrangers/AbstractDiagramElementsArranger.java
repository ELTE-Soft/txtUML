package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.common.commands.SemanticAdapter;
import org.eclipse.papyrus.uml.diagram.common.commands.ShowHideLabelsRequest;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.commands.CustomStateMachineResizeCommand;

public abstract class  AbstractDiagramElementsArranger implements IDiagramElementsArranger{
	
	protected void resizeGraphicalEditPart(GraphicalEditPart graphEP, int new_width, int new_height){
		Dimension figuredim = graphEP.getFigure().getSize();
		ChangeBoundsRequest resize_req = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
		resize_req.setSizeDelta(new Dimension(new_width-figuredim.width(), new_height-figuredim.height()));
		resize_req.setEditParts(graphEP);
		
		Command cmd = graphEP.getCommand(resize_req);
		if(cmd != null){
			cmd.execute();
		}else{
			System.out.println("Command NULL");
		}
	}
	
	protected void hideConnectionLabelsForEditParts(List<EditPart> elements){
		for(EditPart editpart: elements){
			GraphicalEditPart ep = ((GraphicalEditPart) editpart);
			@SuppressWarnings("unchecked")
			List<ConnectionNodeEditPart> connections = ep.getSourceConnections();
			for(ConnectionNodeEditPart connection : connections){
				@SuppressWarnings("unchecked")
				List<ConnectionNodeEditPart> labels = ((ConnectionNodeEditPart) connection).getChildren();
				for(EditPart label : labels){
					ShowHideLabelsRequest request = new ShowHideLabelsRequest(false, ((View) label.getModel()));
					Command com = connection.getCommand(request);
					com.execute();
				}
				
			}
		}
	}
	
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, GraphicalEditPart child, Rectangle constraint){

		IAdaptable adaptable = new SemanticAdapter(null, (View) child.getModel());
		PreferencesHint prefHint =  child.getDiagramPreferencesHint();
		TransactionalEditingDomain editingDomain = child.getEditingDomain();
		String label = DiagramUIMessages.CreateCommand_Label;
		
		CompositeTransactionalCommand cc = new CompositeTransactionalCommand(editingDomain, DiagramUIMessages.AddCommand_Label);
		CustomStateMachineResizeCommand resizeStateMachine =
			new CustomStateMachineResizeCommand(adaptable, prefHint, editingDomain, label, request, constraint, true);
		
		cc.add(resizeStateMachine);
		return new ICommandProxy(cc.reduce());
	}
	
	protected void moveGraphicalEditPart(GraphicalEditPart graphEP, int new_X, int new_Y){
		Rectangle figurebounds = graphEP.getFigure().getBounds();
		ChangeBoundsRequest move_req = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
		move_req.setMoveDelta(new Point(new_X-figurebounds.x(), new_Y-figurebounds.y()));
		move_req.setEditParts(graphEP);
		Command cmd = graphEP.getCommand(move_req);
		if(cmd != null)
			cmd.execute();
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
