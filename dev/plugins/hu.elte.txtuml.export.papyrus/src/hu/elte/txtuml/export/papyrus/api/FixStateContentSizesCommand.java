package hu.elte.txtuml.export.papyrus.api;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.helpers.Zone;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateEditPart;

/**
 * A Command that fixes the inappropriate layoutConstraints for the child nodes
 * of a State 
 */
public class FixStateContentSizesCommand extends AbstractTransactionalCommand {

	private static final int STATELABEL_HEIGHT = 20;
	private StateEditPart stateEditPart;
	
	public FixStateContentSizesCommand(StateEditPart stateEditPart) {
		super(stateEditPart.getEditingDomain(), "", null);
		this.stateEditPart = stateEditPart; 
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info){
		View state = (View) stateEditPart.getModel();
    	View stateLabel = (View) state.getChildren().get(0);
		View stateCompartment = CustomStateEditPart.getStateCompartmentView(state);

		addLayoutConstraintForAllChildren(state);
		
		Zone.setHeight(stateLabel, STATELABEL_HEIGHT);
		Zone.setHeight(stateCompartment, Zone.getWidth(state)-STATELABEL_HEIGHT);
		
		Zone.setWidth(stateLabel, Zone.getWidth(state));
		Zone.setWidth(stateCompartment, Zone.getWidth(state));
		
		if (stateCompartment.getChildren().size() == 1) {
			// we need to resize the region
			View defaultRegion = (View) stateCompartment.getChildren().get(0);
			Zone.setWidth(defaultRegion, Zone.getWidth(stateCompartment));
			Zone.setHeight(defaultRegion, Zone.getHeight(stateCompartment));
		}
		return CommandResult.newOKCommandResult();
	}

	private void addLayoutConstraintForAllChildren(View state) {
		Iterator<?> it = state.getChildren().iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof Node) {
				Node currentNode = (Node) next;
				if (currentNode.getLayoutConstraint() == null) {
					currentNode.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
				}
			}
		}
	}
}
