package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomTransitionGuardEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateEditPart;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;

/**
 * Controls the arranging of a StateMachineDiagram with GMF algorithm
 */
public class StateMachineDiagramElementsGmfArranger extends
		AbstractDiagramElementsGmfArranger {

	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 */
	public StateMachineDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
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
		CustomStateMachineEditPart stateMachineEP = (CustomStateMachineEditPart) diagep.getChildren().get(0);
		DiagramElementsModifier.resizeGraphicalEditPart(stateMachineEP, 400, 200);
		arrange_recurively(stateMachineEP);
		arrange_and_resize_recursively(stateMachineEP);
		monitor.worked(1);
	}
	
	/**
	 * Calls an {@link AbstractDiagramElementsGmfArranger#autoresizeGraphicalEditPart(GraphicalEditPart) autosize}
	 * and {@link AbstractDiagramElementsGmfArranger#arrangeChildren(EditPart) arrangeChildren} 
	 * on the given statemachine.
	 * The method is called recursively for all children which are {@link StateEditPart}s
	 * @param stateEP - The StateEditPart
	 */
	private void arrange_and_resize_recursively(GraphicalEditPart stateEP) {
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> stateCompartements = stateEP.getChildren();

		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> regions =  ((EditPart) stateCompartements.get(1)).getChildren();
		
		for(GraphicalEditPart region: regions){		
			GraphicalEditPart regioncompartement = (GraphicalEditPart) region.getChildren().get(0);
			@SuppressWarnings("unchecked")
			List<EditPart> listEp = regioncompartement.getChildren();
			
			for(EditPart Ep : listEp){
				if(Ep instanceof StateEditPart){
					arrange_and_resize_recursively((StateEditPart) Ep);				
				}
			}
			super.autoresizeGraphicalEditPart(stateEP);
			super.arrangeChildren(regioncompartement);
		}
	}

	/**
	 * Calls an {@link AbstractDiagramElementsGmfArranger#arrangeChildren(EditPart) arrangeChildren}
	 *  and a hideConnectionLabels on the children of the given GraphicalEditPart
	 * @param stateEP - The GraphicalEditPart
	 */
	private void arrange_recurively(GraphicalEditPart stateEP) {
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> stateCompartements = stateEP.getChildren();

		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> regions =  ((EditPart) stateCompartements.get(1)).getChildren();
		
		for(GraphicalEditPart region: regions){		
			GraphicalEditPart regioncompartement = (GraphicalEditPart) region.getChildren().get(0);
			@SuppressWarnings("unchecked")
			List<EditPart> listEp = regioncompartement.getChildren();
			
			for(EditPart Ep : listEp){
				if(Ep instanceof StateEditPart){
					arrange_recurively((GraphicalEditPart) Ep);				
				}
			}
			super.arrangeChildren(regioncompartement);
			DiagramElementsModifier.hideConnectionLabelsForEditParts(listEp, Arrays.asList(CustomTransitionGuardEditPart.class));	
		}
	}
	
}


