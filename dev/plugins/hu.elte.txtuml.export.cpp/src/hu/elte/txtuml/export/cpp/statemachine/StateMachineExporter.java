package hu.elte.txtuml.export.cpp.statemachine;

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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.VariableTemplates;
import hu.elte.txtuml.utils.Pair;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;

public class StateMachineExporter {

	protected Multimap<Pair<String, String>, Pair<String, String>> stateMachineMap;
	protected Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>
	protected List<State> stateList;
	protected Region stateMachineRegion;

	private List<String> subSubMachines;
	private Pseudostate initialState;
	private boolean ownStateMachine;
	protected GuardExporter guardExporter;
	protected TransitionExporter transitionExporter;
	protected EntryExitFunctionExporter entryExitFunctionExporter;
	protected String className;
	private int poolId;
	
	private Shared shared;

	public StateMachineExporter() {
		shared = new Shared();
	}

	public void setName(String name) {
		this.className = name;
	}

	public void setStateMachineThreadPoolId(int id) {
		this.poolId = id;
	}

	public <E extends Element> void createStateMachineRegion(E element) {
		List<StateMachine> smList = new ArrayList<StateMachine>();
		shared.setModelElements(element.allOwnedElements());
		shared.getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE);
		if (!smList.isEmpty()) {
			stateMachineRegion = smList.get(0).getRegions().get(0);
			createStateList();
			ownStateMachine = !stateList.isEmpty();

		} else {
			ownStateMachine = false;
		}
	}

	public void init() {
		stateMachineMap = HashMultimap.create();
		submachineMap = getSubMachines();
		subSubMachines = new ArrayList<String>();
		guardExporter = new GuardExporter();
		transitionExporter = new TransitionExporter(className, stateMachineRegion.getTransitions(), guardExporter);
		entryExitFunctionExporter = new EntryExitFunctionExporter(className, stateList);
		entryExitFunctionExporter.createEntryFunctionTypeMap();
		entryExitFunctionExporter.createExitFunctionTypeMap();

	}

	public String createStateMachineRelatedHeadedDeclerationCodes() {
		StringBuilder source = new StringBuilder("");

		source.append(guardExporter.declareGuardFunctions(stateMachineRegion));
		source.append(transitionExporter.createTransitionFunctionDecl());
		source.append(entryExitFunctionExporter.createEntryFunctionsDecl());
		source.append(entryExitFunctionExporter.createExitFunctionsDecl());

		return source.toString();
	}

	public String createStateMachineRelatedCppSourceCodes() {
		StringBuilder source = new StringBuilder("");
		source.append(PrivateFunctionalTemplates.transitionTableDef(className));
		source.append(FunctionTemplates.functionDef(className, StateMachineTemplates.InitTransitionTable,
				StateMachineTemplates.transitionTableInitilizationBody(className, getStateMachine())));
		if (submachineMap.isEmpty()) {
			source.append(StateMachineTemplates.simpleStateMachineInitializationDefinition(className, getInitialStateName(), true,
					poolId));
			source.append(StateMachineTemplates.simpleStateMachineFixFunctionDefnitions(className, getInitialStateName(),
					false));

		} else {
			source.append(StateMachineTemplates.hierachialStateMachineInitialization(className, getInitialStateName(),
					true, poolId, getEventSubmachineNameMap()));
			source.append(StateMachineTemplates.hiearchialStateMachineFixFunctionDefinitions(className,
					getInitialStateName(), false));

		}

		source.append(guardExporter.defnieGuardFunctions(className));
		source.append(entryExitFunctionExporter.createEntryFunctionsDef());
		source.append(entryExitFunctionExporter.createExitFunctionsDef());
		source.append(transitionExporter.createTransitionFunctionsDef());

		source.append(
				StateMachineTemplates.entry(className, createStateActionMap(entryExitFunctionExporter.getEntryMap()))
						+ "\n");
		source.append(StateMachineTemplates.exit(className, createStateActionMap(entryExitFunctionExporter.getExitMap()))
				+ "\n");

		return source.toString();
	}

	public String createStateEnumCode() {
		return StateMachineTemplates.stateEnum(stateList, getInitialStateName());
	}

	public boolean ownStateMachine() {
		return ownStateMachine;
	}

	public Multimap<Pair<String, String>, Pair<String, String>> getStateMachine() {
		return stateMachineMap;
	}

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

	public Map<String, Pair<String, Region>> getSubMachines() {
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

	public boolean ownSubMachine() {
		return submachineMap.isEmpty();
	}

	public Map<String, Pair<String, Region>> getSubMachineMap() {
		return submachineMap;
	}
	
	public void searchInitialState() {
		for (Vertex item : stateMachineRegion.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {

				Pseudostate pseduoState = (Pseudostate) item;
				if (pseduoState.getKind().equals(PseudostateKind.INITIAL_LITERAL)) {
					initialState = (Pseudostate) item;
				}

			}
		}
	}
	
	public void createMachine() {
		for (Transition item : stateMachineRegion.getTransitions()) {
			Pair<String, String> eventSignalPair = null;

			if (item.getSource().getName().equals(getInitialStateName())) {
				eventSignalPair = new Pair<String, String>(EventTemplates.InitSignal, item.getSource().getName());
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

	protected Map<String, String> createStateActionMap(Map<String, Pair<String, String>> map) {
		Map<String, String> stateActionMap = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, String>> entry : map.entrySet()) {
			stateActionMap.put(entry.getValue().getFirst(), entry.getKey());
		}
		return stateActionMap;
	}

	protected Map<String, String> getEventSubmachineNameMap() {
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
