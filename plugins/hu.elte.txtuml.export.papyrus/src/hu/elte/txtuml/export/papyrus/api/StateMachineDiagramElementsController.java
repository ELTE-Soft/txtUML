package hu.elte.txtuml.export.papyrus.api;

import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

/**
 *
 * @author András Dobreff
 */
public class StateMachineDiagramElementsController {
	
	//**ELEMENT**//
	/**
	 * @param regionEditPart
	 * @param element
	 */
	public static void addElementToRegion(RegionEditPart regionEditPart, Element element){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementToEditPart(regComEp, element);
	}
	
	/**
	 * @param regionEditPart
	 * @param elements
	 */
	public static void addElementsToRegion(RegionEditPart regionEditPart, Collection<? extends Element> elements){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementsToEditpart(regComEp, elements);
	}
	
	//**STATE**//
	/**
	 * @param regionEditPart
	 * @param state
	 */
	public static void addStateToRegion(RegionEditPart regionEditPart, State state){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementToEditPart(regComEp, state);
	}
	
	/**
	 * @param regionEditPart
	 * @param states
	 */
	public static void addStatesToRegion(RegionEditPart regionEditPart, Collection<State> states){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementsToEditpart(regComEp, states);
	}
	
	//**FINALSTATE**//
	
	/**
	 * @param regionEditPart
	 * @param finalstate
	 */
	public static void addFinalStateToRegion(RegionEditPart regionEditPart, FinalState finalstate){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementToEditPart(regComEp, finalstate);
	}
	
	/**
	 * @param regionEditPart
	 * @param finalstates
	 */
	public static void addFinalStatesToRegion(RegionEditPart regionEditPart, Collection<FinalState> finalstates){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementsToEditpart(regComEp, finalstates);
	}
	
	//**PSEUDOSTATE**//
	
	/**
	 * @param regionEditPart
	 * @param pseudostate
	 */
	public static void addPseudostateToRegion(RegionEditPart regionEditPart, Pseudostate pseudostate){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementToEditPart(regComEp, pseudostate);
	}
	
	/**
	 * @param regionEditPart
	 * @param pseudostates
	 */
	public static void addPseudostatesToRegion(RegionEditPart regionEditPart, Collection<Pseudostate> pseudostates){
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementsToEditpart(regComEp, pseudostates);
	}

	//**TRANSITIONS**//
	
	/**
	 * @param regionEditPart
	 * @param transition
	 */	
	public static void addTransitionsToRegion(RegionEditPart regionEditPart, Transition transition) {
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementToEditPart(regComEp, transition);
	}
	
	/**
	 * @param regionEditPart
	 * @param transitions
	 */	
	public static void addTransitionsToRegion(RegionEditPart regionEditPart, List<Transition> transitions) {
		RegionCompartmentEditPart regComEp = getRegionCompatementEditPart(regionEditPart);
		ElementsManagerUtils.addElementsToEditpart(regComEp, transitions);
	}
	
	/**
	 * @param regionEditPart
	 * @return
	 */
	public static RegionCompartmentEditPart getRegionCompatementEditPart(RegionEditPart regionEditPart){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = regionEditPart.getChildren();
		Assert.isTrue(compartements.size() == 1);
		Assert.isTrue(compartements.get(0) instanceof RegionCompartmentEditPart);
		RegionCompartmentEditPart regComEp = (RegionCompartmentEditPart) compartements.get(0);
		return regComEp;
	}
}
