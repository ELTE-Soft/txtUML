package hu.elte.txtuml.export.cpp.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.utils.Pair;

public class StateMachineExporterBase {

	private List<String> subSubMachines;

	protected String ownerClassName;
	protected Pseudostate initialState;
	protected Multimap<TransitionConditions, Pair<String, String>> stateMachineMap;
	protected Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>
	protected List<State> stateList;
	protected Region stateMachineRegion;
	protected GuardExporter guardExporter;
	protected TransitionExporter transitionExporter;
	protected EntryExitFunctionExporter entryExitFunctionExporter;

	public StateMachineExporterBase() {
	}

	public void createMachine() {
		init();
		searchInitialState();
		for (Transition item : stateMachineRegion.getTransitions()) {
			TransitionConditions transitionCondition = null;

			if (item.getSource().getName().equals(getInitialStateName())) {
				transitionCondition = new TransitionConditions(EventTemplates.InitSignal, item.getSource().getName(),
						PortTemplates.NO_PORT);
			}

			for (Trigger tri : item.getTriggers()) {
				Event e = tri.getEvent();

				if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
					SignalEvent se = (SignalEvent) e;
					if (se != null) {
						List<Port> ports = tri.getPorts();
						assert (ports.size() == 0 || ports.size() == 1);
						String port = ports.size() == 0 ? PortTemplates.NO_PORT : ports.get(0).getName();
						transitionCondition = new TransitionConditions(se.getSignal().getName(),
								item.getSource().getName(), port);

					}
				}
			}
			if (transitionCondition != null) {
				Pair<String, String> guardTransitionPair = null;
				if (item.getGuard() != null) {
					guardExporter.exportConstraintToMap(item.getGuard());
					guardTransitionPair = new Pair<String, String>(guardExporter.getGuard(item.getGuard()),
							item.getName());

				} else {
					guardTransitionPair = new Pair<String, String>(null, item.getName());
				}
				stateMachineMap.put(transitionCondition, guardTransitionPair);
			}
		}
	}

	public void setName(String name) {
		this.ownerClassName = name;
	}

	public List<String> getSubMachineNameList() {
		List<String> ret = new LinkedList<String>();
		if (submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
				ret.add(entry.getValue().getFirst());
			}
			ret.addAll(subSubMachines);
		}
		return ret;
	}

	protected Multimap<TransitionConditions, Pair<String, String>> getStateMachine() {
		return stateMachineMap;
	}

	protected Map<String, Pair<String, Region>> getSubMachines() {
		Map<String, Pair<String, Region>> submachineMap = new HashMap<String, Pair<String, Region>>();
		for (State state : stateList) {
			// either got a submachine or a region, both is not permitted
			StateMachine stateMachine = state.getSubmachine();
			if (stateMachine != null) {
				submachineMap.put(state.getName(),
						new Pair<String, Region>(stateMachine.getName(), stateMachine.getRegions().get(0)));
			} else {
				List<Region> region = state.getRegions();
				if (!region.isEmpty()) {
					Region subMachineRegion = region.get(0);
					submachineMap.put(state.getName(),
							new Pair<String, Region>(state.getName() + "_subSM", subMachineRegion));
				}
			}
		}
		return submachineMap;
	}

	protected void init() {
		stateMachineMap = HashMultimap.create();
		submachineMap = getSubMachines();
		subSubMachines = new ArrayList<String>();
		guardExporter = new GuardExporter();
		transitionExporter = new TransitionExporter(ownerClassName, stateMachineRegion.getTransitions(), guardExporter);
		entryExitFunctionExporter = new EntryExitFunctionExporter(ownerClassName, stateList);
		entryExitFunctionExporter.createEntryFunctionTypeMap();
		entryExitFunctionExporter.createExitFunctionTypeMap();

	}

	protected void searchInitialState() {
		for (Vertex item : stateMachineRegion.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {

				Pseudostate pseduoState = (Pseudostate) item;
				if (pseduoState.getKind().equals(PseudostateKind.INITIAL_LITERAL)) {
					initialState = (Pseudostate) item;
				}

			}
		}
	}

	protected String createTransitionTableInitRelatedCodes() {
		StringBuilder source = new StringBuilder("");
		source.append(PrivateFunctionalTemplates.transitionTableDef(ownerClassName));
		source.append(FunctionTemplates.functionDef(ownerClassName, StateMachineTemplates.InitTransitionTable,
				StateMachineTemplates.transitionTableInitilizationBody(ownerClassName, getStateMachine())));

		return source.toString();
	}

	protected Map<String, String> createStateActionMap(List<EntryExitFunctionDescription> functionList) {
		Map<String, String> stateActionMap = new HashMap<String, String>();
		for (EntryExitFunctionDescription entry : functionList) {
			stateActionMap.put(entry.getStateName(), entry.getFunctionName());
		}
		return stateActionMap;
	}

	protected Map<String, String> getEventSubMachineNameMap() {
		Map<String, String> eventSubMachineMap = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			eventSubMachineMap.put(entry.getKey(), entry.getValue().getFirst());
		}
		return eventSubMachineMap;
	}

	protected String getInitialStateName() {
		return initialState.getName();
	}

	protected void createStateList() {
		stateList = new ArrayList<State>();
		for (Vertex item : stateMachineRegion.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.STATE)) {
				stateList.add((State) item);
			}
		}
	}
}
