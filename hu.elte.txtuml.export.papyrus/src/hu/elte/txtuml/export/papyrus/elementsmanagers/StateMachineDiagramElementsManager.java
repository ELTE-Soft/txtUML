package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.ModelManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateEditPart;
import org.eclipse.uml2.uml.*;

/**
 * An abstract class for adding/removing elements to StateMachineDiagrams.
 *
 * @author András Dobreff
 */
public class StateMachineDiagramElementsManager extends AbstractDiagramElementsManager {

	private PreferencesManager preferencesManager;
	
	private List<java.lang.Class<?>> elementsToBeAdded;
	private List<java.lang.Class<?>> edgesToBeAdded;

	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public StateMachineDiagramElementsManager(ModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		preferencesManager = new PreferencesManager();
		elementsToBeAdded = generateElementsToBeAdded();
		edgesToBeAdded = generateEdgesToBeAdded();
	}
	
	/**
	 * Returns the types of elements that are to be added
	 * @return Returns the types of elements that are to be added
	 */
	private List<java.lang.Class<?>> generateElementsToBeAdded() {
		List<java.lang.Class<?>> nodes = new LinkedList<java.lang.Class<?>>(
				Arrays.asList(
						FinalState.class,
						State.class,
						Pseudostate.class						
				));
		
		if(preferencesManager.getBoolean(PreferencesManager.STATEMACHINE_DIAGRAM_CONSTRAINT_PREF))
			nodes.add(Constraint.class);
		if(preferencesManager.getBoolean(PreferencesManager.STATEMACHINE_DIAGRAM_COMMENT_PREF))
			nodes.add(Comment.class);
		
		return nodes;
	}
	
	/**
	 * Returns the types of connectors that are to be added
	 * @return Returns the types of connectors that are to be added
	 */
	private List<java.lang.Class<?>> generateEdgesToBeAdded() {
		List<java.lang.Class<?>> edges = Arrays.asList(Transition.class);
		return edges;
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
		
		super.removeEditParts(diagramEditPart.getEditingDomain(), Arrays.asList(stateMachineEditpart));
		super.addElementsToEditpart(diagramEditPart, Arrays.asList(smElement));
		
		stateMachineEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		fillState(stateMachineEditpart);
	}
	
	/**
	 * Adds the children of a state to the state.
	 * (Calls the {@link #addSubElements(EditPart)} for every region) 
	 * @param state - The state
	 */
	private void fillState(EditPart state){
		EditPart stateCompartmentEditPart = (EditPart) state.getChildren().get(1);
		@SuppressWarnings("unchecked")
		List<EditPart> regions = stateCompartmentEditPart.getChildren();
		
		for(EditPart region : regions){
			EditPart regionCompartment = (EditPart) region.getChildren().get(0);
			this.addSubElements(regionCompartment);
		}
	}
	
	/**
	 * Adds the subElements to an EditPart. Then calls the {@link #fillState(EditPart)}
	 * for every state. 
	 * @param ep - The EditPart
	 */
	private void addSubElements(EditPart ep){
		EObject parent = ((View) ep.getModel()).getElement();
		List<Element> list = ((Element) parent).getOwnedElements();
		
		List<Element> nodes = modelManager.getElementsOfTypesFromList(list, elementsToBeAdded);
		List<Element> transitions = modelManager.getElementsOfTypesFromList(list, edgesToBeAdded);
	
		addElementsToEditpart(ep, nodes);
		addElementsToEditpart(ep, transitions);

		@SuppressWarnings("unchecked")
		List<EditPart> subEPs = ep.getChildren();
		for(EditPart subEP : subEPs){
			if(subEP instanceof StateEditPart){
				fillState(subEP);
			}
		}
	}
}
