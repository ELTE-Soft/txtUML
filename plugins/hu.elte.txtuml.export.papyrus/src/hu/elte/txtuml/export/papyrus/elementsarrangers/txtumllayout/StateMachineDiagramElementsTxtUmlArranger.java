package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.tooling.runtime.linklf.LinkLFShapeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.RoundedCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionEditPart;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.api.StateMachineDiagramElementsController;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * Controls the arranging of a StateMachineDiagram with GMF algorithm
 *
 * @author Andr�s Dobreff
 */
public class StateMachineDiagramElementsTxtUmlArranger extends
		AbstractDiagramElementsTxtUmlArranger {

	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 * @param txtUmlRegistry 
	 */
	public StateMachineDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart, TxtUMLElementsRegistry txtUmlRegistry) {
		super(diagramEditPart, txtUmlRegistry);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger#arrange()
	 */
	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		CustomStateMachineEditPart stateMachineEditPart = getStateMachineEditPart();
		this.arrangeChildren(stateMachineEditPart, monitor);
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> children = getRegionCompatementEditPart(stateMachineEditPart).getChildren();
		DiagramElementsModifier.hideConnectionLabelsForEditParts(children, new LinkedList<java.lang.Class<?>>());
		CustomStateMachineEditPart sm = (CustomStateMachineEditPart)this.diagep.getChildren().get(0);
		
		Dimension preferredSize = this.calculatePreferredSize((List<GraphicalEditPart>) children);
		DiagramElementsModifier.resizeGraphicalEditPart(sm, preferredSize.width, preferredSize.height);
	}
	
	private RegionCompartmentEditPart getRegionCompatementEditPart(RoundedCompartmentEditPart state) {
		LinkLFShapeCompartmentEditPart stateCompartement = StateMachineDiagramElementsController.getCustomStateMachineCompartmentEditPart((RoundedCompartmentEditPart) state);
		if(stateCompartement != null && stateCompartement.getChildren() != null && stateCompartement.getChildren().size() != 0){ //there can be pseudostates
			RegionEditPart region = (RegionEditPart) stateCompartement.getChildren().get(0);
			RegionCompartmentEditPart regionCompartement = StateMachineDiagramElementsController.getRegionCompatementEditPart(region);
			return regionCompartement;
		}
		return null;
	}

	private CustomStateMachineEditPart getStateMachineEditPart(){
		for(Object customStateMachineEditPart : this.diagep.getChildren()){
			if(customStateMachineEditPart instanceof CustomStateMachineEditPart){
				return (CustomStateMachineEditPart) customStateMachineEditPart;
			}
		}
		return null;
	}
	
	@Override
	protected void arrangeChildren(GraphicalEditPart state, IProgressMonitor monitor) throws ArrangeException{
		assert state instanceof RoundedCompartmentEditPart;
		boolean isCompositeState = false;
		
		RegionCompartmentEditPart regionCompartement = getRegionCompatementEditPart((RoundedCompartmentEditPart)state);
		
		if(regionCompartement != null){
			@SuppressWarnings("unchecked")
			List<GraphicalEditPart> editparts = regionCompartement.getChildren();
			for(GraphicalEditPart ep : editparts){
				if(isValidStateClass(ep)){
					isCompositeState = true;
					this.arrangeChildren(ep, monitor);
				}
			}
	
			if(isCompositeState){
				super.arrangeChildren(regionCompartement, monitor);
				Dimension d = calculatePreferredSize(editparts);
				DiagramElementsModifier.resizeState(state, d.width, d.height);
			}
		}
	}

	private boolean isValidStateClass(GraphicalEditPart ep) {
		return (ep instanceof CustomStateEditPart);
	}
	
	private Dimension calculatePreferredSize(List<? extends GraphicalEditPart> editparts){
		
		FreeFormLayoutEx manager = (FreeFormLayoutEx) editparts.get(0).getFigure().getParent().getLayoutManager();
		
		Point topLeft = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point bottomRight = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		
		for(GraphicalEditPart ep : editparts){
			Rectangle constraint = (Rectangle) manager.getConstraint(ep.getFigure());
			Dimension preferredSize = ep.getFigure().getPreferredSize();
			constraint.width = constraint.width < preferredSize.width ? preferredSize.width : constraint.width;
			constraint.height = constraint.height < preferredSize.height ? preferredSize.height : constraint.height;
					
			topLeft.x = constraint.x < topLeft.x ? constraint.x : topLeft.x;
			topLeft.y = constraint.y < topLeft.y ? constraint.y : topLeft.y;
			
			bottomRight.x = constraint.getBottomRight().x > bottomRight.x ? constraint.getBottomRight().x : bottomRight.x;
			bottomRight.y = constraint.getBottomRight().y > bottomRight.y ? constraint.getBottomRight().y : bottomRight.y;
		}
		
		return new Dimension(bottomRight.x-topLeft.x, bottomRight.y-topLeft.y);
	}
	
	@Override
	protected Optional<? extends Element> findConnection(LineAssociation la){
		Optional<? extends Element> e = txtUmlRegistry.findTransition(la.getId());
		return e;
	}
}


