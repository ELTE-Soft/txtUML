package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicitySourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicityTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationNameEditPart;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

/**
 * Controls the arranging of a ClassDiagram with txtUML algorithm
 */
public class ClassDiagramElementsTxtUmlArranger extends AbstractDiagramElementsTxtUmlArranger {

	/**
	 * Arranges the children of an {@link EditPart} with the txtUML arranging
	 * algorithm
	 * 
	 * @param diagramEditPart
	 *            - The children of this EditPart will be arranged
	 */
	public ClassDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart, DiagramExportationReport report,
			TxtUMLElementsMapper mapper) {
		super(diagramEditPart, report, mapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger#
	 * arrange()
	 */
	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		super.arrangeChildren(this.diagep, monitor);
		@SuppressWarnings("unchecked")
		List<EditPart> children = this.diagep.getChildren();
		DiagramElementsModifier.hideConnectionLabelsForEditParts(children, Arrays.asList(AssociationNameEditPart.class,
				AssociationMultiplicityTargetEditPart.class, AssociationMultiplicitySourceEditPart.class));
	}
}
