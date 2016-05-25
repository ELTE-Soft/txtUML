package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.api.StateMachineDiagramElementsController;
import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

/**
 * An abstract class for adding/removing elements to StateMachineDiagrams.
 */
public class StateMachineDiagramElementsManager extends AbstractDiagramElementsManager {

	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public StateMachineDiagramElementsManager(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(List<Element> elements){
		
		/* The diagram creation creates an empty StateMachine, but it may have more Regions. 
		 * So we delete it and place it on the diagram again.  */
		EditPart stateMachineEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		View smModel = (View) stateMachineEditpart.getModel();
		Element smElement = (Element) smModel.getElement();
		
		ElementsManagerUtils.removeEditParts(diagramEditPart.getEditingDomain(), Arrays.asList(stateMachineEditpart));
		ElementsManagerUtils.addElementsToEditPart(diagramEditPart, Arrays.asList(smElement));
		
		EditPart newStateMachineEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		fillState(newStateMachineEditpart, elements);
	}
	
	/**
	 * Adds the children of a state to the state.
	 * (Calls the {@link #addSubElements(EditPart)} for every region) 
	 * @param state - The state
	 */
	private void fillState(EditPart state, List<Element> elements){
		EditPart ep = StateMachineDiagramElementsController.getStateCompartmentEditPart(state);
		if(ep == null) {
			ep = StateMachineDiagramElementsController.getCustomStateMachineCompartmentEditPart(state);
		}
		if(ep == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<RegionEditPart> regions = ep.getChildren();
		
		for(RegionEditPart region : regions){
			this.addSubElements(region, elements);
		}
	}
	
	/**
	 * Adds the subElements to an EditPart. Then calls the {@link #fillState(EditPart)}
	 * for every state. 
	 * @param region - The EditPart
	 */
	private void addSubElements(RegionEditPart region, List<Element> elements){
		
		List<State> states = this.filterSubelementsOfRegion(region, UMLModelManager.getElementsOfTypeFromList(elements, State.class));
		List<Pseudostate> pseudostates = this.filterSubelementsOfRegion(region, UMLModelManager.getElementsOfTypeFromList(elements, Pseudostate.class));
		List<FinalState> finalstates = this.filterSubelementsOfRegion(region, UMLModelManager.getElementsOfTypeFromList(elements, FinalState.class));
		List<Transition> transitions = this.filterSubelementsOfRegion(region, UMLModelManager.getElementsOfTypeFromList(elements, Transition.class));
	
		StateMachineDiagramElementsController.addPseudostatesToRegion(region, pseudostates);
		StateMachineDiagramElementsController.addStatesToRegion(region, states);
		StateMachineDiagramElementsController.addFinalStatesToRegion(region, finalstates);
		StateMachineDiagramElementsController.addTransitionsToRegion(region, transitions);
	
		@SuppressWarnings("unchecked")
		List<EditPart> subEPs = StateMachineDiagramElementsController.getRegionCompatementEditPart(region).getChildren();
		
		for(EditPart subEP : subEPs){
			if(subEP instanceof StateEditPart){
				fillState(subEP, elements);
			}
		}
	}
	
	private <T> List<T> filterSubelementsOfRegion(RegionEditPart region, List<T> elements){
		return elements.stream().filter((elem) -> {
			return ((Element)((View)region.getModel()).getElement()).getOwnedElements().stream().anyMatch((child) -> child.equals(elem));
		}).collect(Collectors.toList());
	}
}
