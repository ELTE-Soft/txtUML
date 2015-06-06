package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicitySourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicityTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationNameEditPart;

/**
 * Controls the arranging of a ClassDiagram with txtUML algorithm
 *
 * @author András Dobreff
 */
public class ClassDiagramElementsTxtUmlArranger extends AbstractDiagramElementsTxtUmlArranger{
	
	/**
	 * Arranges the children of an EditPart with the GMF arranging algorithm 
	 * @param diagramEditPart - The children of this EditPart will be arranged
	 */
	public ClassDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger#arrange()
	 */
	@Override
	public void arrange() {
		@SuppressWarnings("unchecked")
		List<EditPart> listEp = diagep.getChildren();
		super.arrangeChildren(diagep, listEp);
		super.hideConnectionLabelsForEditParts(listEp, Arrays.asList(
				AssociationNameEditPart.class,
				AssociationMultiplicityTargetEditPart.class,
				AssociationMultiplicitySourceEditPart.class
				));
	}
	
}
