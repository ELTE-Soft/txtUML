package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.utils.Pair;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;

class StateMachineExporter {
	
	protected Multimap<Pair<String, String>, Pair<String, String>> stateMachineMap;

	private List<String> subSubMachines;
	protected List<State> stateList;
	protected Region stateMachineRegion;
	private Pseudostate initialState;
	private boolean ownStateMachine;
	private GuardExporter guardExporter;
	
	StateMachineExporter(Region region) {
		stateMachineRegion = region;
	}

	StateMachineExporter(Class cls) {
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, cls.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		if(!smList.isEmpty()) {
			stateMachineRegion = smList.get(0).getRegions().get(0);
			createStateList();
			ownStateMachine = !stateList.isEmpty();
			
		} else {
			ownStateMachine = true;
		}
		
		if(isOwnStateMachine()) {
			searchInitialState();
			createMachine();
		}
	}

	public boolean isOwnStateMachine() {
		return ownStateMachine;
	}
	
	public Multimap<Pair<String, String>, Pair<String, String>> getStateMachine() {
		return stateMachineMap;
	}

	protected Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>

	public List<String> getSubmachines() {
		List<String> ret = new LinkedList<String>();
		if (submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
				ret.add(entry.getValue().getFirst());
			}
			ret.addAll(subSubMachines);
		}
		return ret;
	}

	protected Map<String, String> createStateActionMap(Map<String, Pair<String, String>> map) {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, String>> entry : map.entrySet()) {
			ret.put(entry.getValue().getFirst(), entry.getKey());
		}
		return ret;
	}

	protected Map<String, String> getEventSubmachineNameMap() {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			ret.put(entry.getKey(), entry.getValue().getFirst());
		}
		return ret;
	}
	
	private void  createMachine() {
		for (Transition item : stateMachineRegion.getTransitions()) {
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
	
	protected String getInitialStateName() {
		return initialState.getName();
	}

	private void createStateList() {
		List<State> stateList = new ArrayList<State>();
		for (Vertex item : stateMachineRegion.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.STATE)) {
				stateList.add((State) item);
			}
		}
	}
	
	private void searchInitialState() {
		for (Vertex item : stateMachineRegion.getSubvertices()) {

			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {

				Pseudostate pseduoState = (Pseudostate) item;
				if (pseduoState.getKind().equals(PseudostateKind.INITIAL_LITERAL)) {
					initialState = (Pseudostate) item;
				}

			}
		}
	}
}
