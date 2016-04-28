package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.papyrus.uml.diagram.composite.custom.utils.CompositeEditPartUtil;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineEditPart;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;

public class CompositeDiagramElementsTxtUmlArranger extends AbstractDiagramElementsTxtUmlArranger {

	public CompositeDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart,
			TxtUMLElementsRegistry txtUmlRegistry) {
		super(diagramEditPart, txtUmlRegistry);
	}

	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		this.arrangeChildren(this.diagep, monitor);
	}
	
	@Override
	protected void arrangeChildren(GraphicalEditPart parent, IProgressMonitor monitor) throws ArrangeException{
		ClassCompositeEditPart composite = (ClassCompositeEditPart)parent.getChildren().get(0);
		ClassCompositeCompartmentEditPart compartment = (ClassCompositeCompartmentEditPart)CompositeEditPartUtil.getCompositeCompartmentEditPart((ClassCompositeEditPart)composite);

		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> children = compartment.getChildren();
		
		super.arrangeChildren(compartment, monitor);

				
		Dimension preferredSize = this.calculatePreferredSize((List<GraphicalEditPart>) children);
		DiagramElementsModifier.resizeGraphicalEditPart(composite, preferredSize.width+15, preferredSize.height+35);
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
