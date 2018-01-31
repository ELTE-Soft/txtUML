package hu.elte.txtuml.export.cpp.statemachine;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.utils.Pair;

public abstract class StateMachineExporterBase {


	protected Multimap<TransitionConditions, Pair<String, String>> stateMachineMap;
	protected Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>
	protected List<State> stateList;
	protected Region stateMachineRegion;
	protected GuardExporter guardExporter;
	protected TransitionExporter transitionExporter;
	protected EntryExitFunctionExporter entryExitFunctionExporter;
	protected SubStateMachineExporter subStateMachineExporter;

	private List<String> allSubMachineName;	
	
	private void createMachine() {
		for (Transition item : stateMachineRegion.getTransitions()) {
			TransitionConditions transitionCondition = null;
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
	
	public void createSubMachineSources(String detiniation) throws FileNotFoundException, UnsupportedEncodingException {
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			subStateMachineExporter = new SubStateMachineExporter(entry.getValue().getFirst(), entry.getValue().getSecond(), getActualCompilationUnit().getUnitName());
			subStateMachineExporter.createSubSmSource(detiniation);
			
			allSubMachineName.add(entry.getValue().getFirst());
			allSubMachineName.addAll(subStateMachineExporter.getAllSubmachineName());
		}
	}
	
	public List<String> getAllSubmachineName() {
		return allSubMachineName;
	}
	
	abstract protected ICppCompilationUnit getActualCompilationUnit();

	protected void init() {
		
		allSubMachineName = new LinkedList<>();
		stateMachineMap = HashMultimap.create();
		guardExporter = new GuardExporter(getActualCompilationUnit());
		transitionExporter = new TransitionExporter(getActualCompilationUnit(), stateMachineRegion.getTransitions(), guardExporter);
		
		createStateList();
		entryExitFunctionExporter = new EntryExitFunctionExporter(getActualCompilationUnit(), stateList);
		entryExitFunctionExporter.createEntryFunctionTypeMap();
		entryExitFunctionExporter.createExitFunctionTypeMap();
		
		createMachine();
		createSubMachines();

	}
	
	protected void createSubMachines() {
		submachineMap = new HashMap<String, Pair<String, Region>>();
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
	}

	protected String createTransitionTableInitRelatedCodes() {
		StringBuilder source = new StringBuilder("");
		source.append(PrivateFunctionalTemplates.transitionTableDef(getActualCompilationUnit().getUnitName()));
		source.append(FunctionTemplates.functionDef(getActualCompilationUnit().getUnitName(), StateMachineTemplates.InitTransitionTable,
				StateMachineTemplates.transitionTableInitilizationBody(getActualCompilationUnit().getUnitName(), stateMachineMap)));

		return source.toString();
	}

	protected Map<String, String> createStateActionMap(List<EntryExitFunctionDescription> functionList) {
		Map<String, String> stateActionMap = new HashMap<String, String>();
		for (EntryExitFunctionDescription entry : functionList) {
			stateActionMap.put(entry.getStateName(), entry.getFunctionName());
		}
		return stateActionMap;
	}

	protected Map<String, String> getStateToSubMachineNameMap() {
		Map<String, String> eventSubMachineMap = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			eventSubMachineMap.put(entry.getKey(), entry.getValue().getFirst());
		}
		return eventSubMachineMap;
	}
	
	protected void createStateList() {
		stateList = new ArrayList<State>();
		for (Vertex item : stateMachineRegion.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.STATE)) {
				stateList.add((State) item);
			}
		}
	}
	
	
	protected static Optional<Pseudostate> getInitialState(Region stateMachineRegion) {
		for (Vertex item : stateMachineRegion.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {
				Pseudostate pseduoState = (Pseudostate) item;
				if (pseduoState.getKind().equals(PseudostateKind.INITIAL_LITERAL)) {
					return Optional.of((Pseudostate) item);
				}

			}
		}		
		return Optional.empty();
	}
	
	protected static Optional<Transition> getInitialTransition(Region stateMachineRegion) {
		Optional<Pseudostate> initialState = getInitialState(stateMachineRegion);
		if (initialState.isPresent()) {
			for (Transition transition : stateMachineRegion.getTransitions()) {
				if(transition.getSource().equals(initialState.get())) {
					return Optional.of(transition);
				}
			}
		}		
		return Optional.empty();
	}
}
