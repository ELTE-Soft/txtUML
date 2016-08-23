package hu.elte.txtuml.export.cpp.structural;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.utils.Pair;

class StateMachineExporter {
	
	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */
	private Multimap<Pair<String, String>, Pair<String, String>> stateMachineMap;
	List<State> stateList;
	private Pseudostate initialState;
	
	GuardExporter guardExporter;
	
	StateMachineExporter(Region region, GuardExporter guardExporter) {
		this.guardExporter = guardExporter;
		stateMachineMap = HashMultimap.create();
		stateList = new LinkedList<State>();
		searchInitialState(region);
		createMachine(region);
		createStateList(region);
	}
	
	public Multimap<Pair<String, String>, Pair<String, String>> getMachine() {
		return stateMachineMap;
	}
	
	public String getInitialStateName() {
		return initialState.getName();
	}
	
	public List<State> getStateList() {
		return stateList;
	}
	

	private void  createMachine(Region region) {
		for (Transition item : region.getTransitions()) {
			Pair<String, String> eventSignalPair = null;

			if (item.getSource().getName().equals(getInitialStateName())) {
				eventSignalPair = new Pair<String, String>(GenerationTemplates.InitSignal, item.getSource().getName());
			}

			for (Trigger tri : item.getTriggers()) {
				Event e = tri.getEvent();
				if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
					SignalEvent se = (SignalEvent) e;
					if (se != null) {
						eventSignalPair = new Pair<String, String>(se.getSignal().getName(),
								item.getSource().getName());
					}
				}
			}
			if (eventSignalPair != null) {
				Pair<String, String> guardTransitionPair = null;
				if (item.getGuard() != null) {
					guardTransitionPair = new Pair<String, String>(guardExporter.getGuard(item.getGuard()),
							item.getName());

				} else {
					guardTransitionPair = new Pair<String, String>(null, item.getName());
				}
				stateMachineMap.put(eventSignalPair, guardTransitionPair);
			}
		}
	}
	
	private void createStateList(Region region) {
		for (Vertex item : region.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.STATE)) {
				stateList.add((State) item);
			}
		}
	}
	
	private void searchInitialState(Region region) {
		for (Vertex item : region.getSubvertices()) {

			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {

				Pseudostate pseduoState = (Pseudostate) item;
				if (pseduoState.getKind().equals(PseudostateKind.INITIAL_LITERAL)) {
					initialState = (Pseudostate) item;
				}

			}
		}
	}
}
