package hu.elte.txtuml.export.papyrus.elementsarrangers.gmflayout;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicitySourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicityTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationNameEditPart;

/**
 * Controls the arranging of a ClassDiagram with GMF algorithm
 */
public class ClassDiagramElementsGmfArranger extends AbstractDiagramElementsGmfArranger{

	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 */
	public ClassDiagramElementsGmfArranger(DiagramEditPart diagramEditPart) {
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
		super.arrangeChildren(this.diagep);
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> listEp = this.diagep.getChildren();
		DiagramElementsModifier.hideConnectionLabelsForEditParts(listEp, Arrays.asList(
				AssociationNameEditPart.class,
				AssociationMultiplicityTargetEditPart.class,
				AssociationMultiplicitySourceEditPart.class
				));
		monitor.worked(1);
	}
	
}
