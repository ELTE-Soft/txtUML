package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicitySourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicityTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationNameEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomRegionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomRegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateMachineEditPart;

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
		
		super.arrangeChildren(getRegionCompartementEditPart(), monitor);
		@SuppressWarnings("unchecked")
		List<EditPart> children = this.diagep.getChildren();
		DiagramElementsModifier.hideConnectionLabelsForEditParts(children, Arrays.asList(
				AssociationNameEditPart.class,
				AssociationMultiplicityTargetEditPart.class,
				AssociationMultiplicitySourceEditPart.class
				));
	}
	
	private EditPart getRegionCompartementEditPart(){
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
}


