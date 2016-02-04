package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.papyrus.uml.diagram.statemachine.StateMachineDiagramCreationCondition;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomRegionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomRegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateCompartmentEditPart;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.api.StateMachineDiagramElementsController;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;

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
		GraphicalEditPart regionCompaEditPart = getRegionCompartementEditPart();
		this.arrangeChildren(regionCompaEditPart, monitor);
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> children = regionCompaEditPart.getChildren();
		DiagramElementsModifier.hideConnectionLabelsForEditParts(children, null);
		CustomStateMachineEditPart sm = (CustomStateMachineEditPart)this.diagep.getChildren().get(0);
		
		Dimension preferredSize = this.calculatePreferredSize((List<GraphicalEditPart>) children);
		DiagramElementsModifier.resizeGraphicalEditPart(sm, preferredSize.width, preferredSize.height);
	}
	
	private GraphicalEditPart getRegionCompartementEditPart(){
		for(Object customStateMachineEditPart : this.diagep.getChildren()){
			if(customStateMachineEditPart instanceof CustomStateMachineEditPart){
				for(Object customSMCompartementEditPart : ((CustomStateMachineEditPart)customStateMachineEditPart).getChildren()){
					if(customSMCompartementEditPart instanceof CustomStateMachineCompartmentEditPart){
						for(Object customRegionEditPart : ((CustomStateMachineCompartmentEditPart)customSMCompartementEditPart).getChildren()){
							if(customRegionEditPart instanceof CustomRegionEditPart){
								for(Object customRegionCompartmentEditPart : ((CustomRegionEditPart)customRegionEditPart).getChildren()){
									if(customRegionCompartmentEditPart instanceof CustomRegionCompartmentEditPart){
										return (CustomRegionCompartmentEditPart) customRegionCompartmentEditPart;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected void arrangeChildren(GraphicalEditPart parent, IProgressMonitor monitor) throws ArrangeException{
		boolean isCompositeState = false;
		
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> editparts = parent.getChildren();
		for(GraphicalEditPart ep : editparts){
			if(isValidStateClass(ep)){
				isCompositeState = true;
				StateCompartmentEditPart stateCompartement = StateMachineDiagramElementsController.getStateCompartmentEditPart(ep);
				if(stateCompartement.getChildren() != null && stateCompartement.getChildren().size() != 0){
					RegionEditPart region = (RegionEditPart) stateCompartement.getChildren().get(0);
					RegionCompartmentEditPart regionCompartement = StateMachineDiagramElementsController.getRegionCompatementEditPart(region);
					this.arrangeChildren(regionCompartement, monitor);
				}
			}
		}
		
		
		if(isCompositeState){
			super.arrangeChildren(parent, monitor);
			Dimension d = calculatePreferredSize(editparts);
			DiagramElementsModifier.resizeGraphicalEditPart(parent, d.width, d.height);
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
}


