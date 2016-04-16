package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateEditPart;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.api.StateMachineDiagramElementsController;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

/**
 * An abstract class for adding/removing elements to StateMachineDiagrams.
 */
public class StateMachineDiagramElementsManager extends AbstractDiagramElementsManager {

	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public StateMachineDiagramElementsManager(Diagram diagram, TransactionalEditingDomain domain) {
		super(diagram);
		//TODO: ElementCreator field needed
	}
	
	public StateMachineDiagramElementsManager(Diagram diagram, TransactionalEditingDomain domain, IProgressMonitor monitor) {
		this(diagram, domain);
		this.monitor = monitor;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(){
		
		/* The diagram creation creates an empty StateMachine, but it may have more Regions. 
		 * So we delete it and place it on the diagram again.  */
/*
		EditPart stateMachineEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		View smModel = (View) stateMachineEditpart.getModel();
		Element smElement = (Element) smModel.getElement();
		
		ElementsManagerUtils.removeEditParts(diagramEditPart.getEditingDomain(), Arrays.asList(stateMachineEditpart));
		ElementsManagerUtils.addElementsToEditPart(diagramEditPart, Arrays.asList(smElement));
		
		stateMachineEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		fillState(stateMachineEditpart);
*/
	}
	
	/**
	 * Adds the children of a state to the state.
	 * (Calls the {@link #addSubElements(EditPart)} for every region) 
	 * @param state - The state
	 */
	private void fillState(EditPart state){
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
			this.addSubElements(region);
		}
	}
	
	/**
	 * Adds the subElements to an EditPart. Then calls the {@link #fillState(EditPart)}
	 * for every state. 
	 * @param region - The EditPart
	 */
	private void addSubElements(RegionEditPart region){
		EObject parent = ((View) region.getModel()).getElement();
		List<Element> list = ((Element) parent).getOwnedElements();
		
		List<State> states = UMLModelManager.getElementsOfTypeFromList(list, State.class);
		List<Pseudostate> pseudostates = UMLModelManager.getElementsOfTypeFromList(list, Pseudostate.class);
		List<FinalState> finalstates = UMLModelManager.getElementsOfTypeFromList(list, FinalState.class);
		List<Transition> transitions = UMLModelManager.getElementsOfTypeFromList(list, Transition.class);
	
		StateMachineDiagramElementsController.addPseudostatesToRegion(region, pseudostates);
		StateMachineDiagramElementsController.addStatesToRegion(region, states);
		StateMachineDiagramElementsController.addFinalStatesToRegion(region, finalstates);
		StateMachineDiagramElementsController.addTransitionsToRegion(region, transitions);
	
		if(PreferencesManager.getBoolean(PreferencesManager.STATEMACHINE_DIAGRAM_CONSTRAINT_PREF)){
			List<Constraint> constraints = UMLModelManager.getElementsOfTypeFromList(list, Constraint.class);
			StateMachineDiagramElementsController.addElementsToRegion(region, constraints);
		}
		
		if(PreferencesManager.getBoolean(PreferencesManager.STATEMACHINE_DIAGRAM_COMMENT_PREF)){
			List<Comment> comments = UMLModelManager.getElementsOfTypeFromList(list, Comment.class);
			StateMachineDiagramElementsController.addElementsToRegion(region, comments);
		}
		
		@SuppressWarnings("unchecked")
		List<EditPart> subEPs = StateMachineDiagramElementsController.getRegionCompatementEditPart(region).getChildren();
		
		for(EditPart subEP : subEPs){
			if(subEP instanceof StateEditPart){
				fillState(subEP);
			}
		}
	}
}
