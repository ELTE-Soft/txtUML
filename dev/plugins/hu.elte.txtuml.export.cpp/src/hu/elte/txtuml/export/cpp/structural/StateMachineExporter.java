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

import com.google.common.collect.HashMultimap;
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
	
	StateMachineExporter(Region region, String name) {
		this.stateMachineRegion = region;
		this.className = name;
		createStateList();
		init();
		
	}

	StateMachineExporter(String className,int poolId, Class cls) {	
		createStateMachineRegion(cls);
		if(isOwnStateMachine()) {			
			init();
			entryExitFunctionExporter.createEntryFunctionTypeMap();
			entryExitFunctionExporter.createExitFunctionTypeMap();
		}		
		this.poolId = poolId;
		this.className = className;
	}
	
	private void init() {
		stateMachineMap = HashMultimap.create();
		submachineMap = new HashMap<String, Pair<String, Region>>();
		subSubMachines = new ArrayList<String>();	
		guardExporter = new GuardExporter();
		transitionExporter = new TransitionExporter(className,stateMachineRegion.getTransitions(),guardExporter);
		entryExitFunctionExporter = new EntryExitFunctionExporter(className,stateList);		
		searchInitialState();
		createMachine();
	}
	
	private void createStateMachineRegion(Class cls) {
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, cls.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		if(!smList.isEmpty()) {
			stateMachineRegion = smList.get(0).getRegions().get(0);
			createStateList();
			ownStateMachine = !stateList.isEmpty();
			
		} else {
			ownStateMachine = true;
		}
	}
	
	public String createStateMachineRelatedHeadedDeclerationCodes() {
		StringBuilder source = new StringBuilder("");
		

		source.append(guardExporter.declareGuardFunctions(stateMachineRegion));
		source.append(transitionExporter.createTransitionFunctionDecl());
		
		return source.toString();
	}
	
	public String createStateMachineRelatedCppSourceCodes() {
		StringBuilder source = new StringBuilder("");
		

		if (submachineMap.isEmpty()) {
			source.append(GenerationTemplates.simpleStateMachineInitialization(className, getInitialStateName(), true,
					poolId, getStateMachine()));
			source.append(GenerationTemplates.simpleStateMachineFixFunctionDefnitions(className, getInitialStateName(),
					false));

		} else {
			source.append(GenerationTemplates.hierachialStateMachineInitialization(className, getInitialStateName(),
					true, poolId, getStateMachine(), getEventSubmachineNameMap()));
			source.append(GenerationTemplates.hiearchialStateMachineFixFunctionDefinitions(className,
					getInitialStateName(), false));

		}

		source.append(GenerationTemplates.destructorDef(className, true));
		source.append(guardExporter.defnieGuardFunctions(className));
		source.append(entryExitFunctionExporter.createEntryFunctionsDef());
		source.append(entryExitFunctionExporter.createExitFunctionsDef());
		source.append(transitionExporter.createTransitionFunctionsDef());

		source.append(GenerationTemplates.entry(className, createStateActionMap(entryExitFunctionExporter.getEntryMap())) + "\n");
		source.append(GenerationTemplates.exit(className, createStateActionMap(entryExitFunctionExporter.getExitMap())) + "\n");
		
		return source.toString();
	}
	
	public String createStateEnumCode() {
		return GenerationTemplates.stateEnum(stateList, getInitialStateName());
	}

	public boolean isOwnStateMachine() {
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
			StateMachine m = state.getSubmachine();
			if (m != null) {
				submachineMap.put(state.getName(), new Pair<String, Region>(m.getName(), m.getRegions().get(0)));
			} else {
				List<Region> r = state.getRegions();
				if (!r.isEmpty()) {
					submachineMap.put(state.getName(), new Pair<String, Region>(state.getName() + "_subSM", r.get(0)));
				}
			}
		}
		return submachineMap;
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
	
	private void createMachine() {
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
		stateList = new ArrayList<State>();
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
