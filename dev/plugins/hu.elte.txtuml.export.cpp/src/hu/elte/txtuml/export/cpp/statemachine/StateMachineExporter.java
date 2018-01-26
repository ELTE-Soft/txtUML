package hu.elte.txtuml.export.cpp.statemachine;


import java.util.Map;
import java.util.Optional;

import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.utils.Pair;
import org.eclipse.uml2.uml.Element;

public class StateMachineExporter extends StateMachineExporterBase {

	private int poolId;
	private StateMachine sm;

	public StateMachineExporter(StateMachine sm, ICppCompilationUnit owner) {
		super(owner);
		this.sm = sm;
	}

	public void setStateMachineThreadPoolId(int id) {
		this.poolId = id;
	}

	public <E extends Element> void createStateMachineRegion(E element) {
		stateMachineRegion = sm.getRegions().get(0);
		createStateList();

	}

	public String createStateMachineRelatedHeadedDeclarationCodes() {
		StringBuilder source = new StringBuilder("");

		source.append(guardExporter.declareGuardFunctions(stateMachineRegion));
		source.append(transitionExporter.createTransitionFunctionDecl());
		source.append(entryExitFunctionExporter.createEntryFunctionsDecl());
		source.append(entryExitFunctionExporter.createExitFunctionsDecl());

		return source.toString();
	}

	public String createStateMachineRelatedCppSourceCodes() {
		StringBuilder source = new StringBuilder("");
		source.append(createTransitionTableInitRelatedCodes());
		source.append(StateMachineTemplates.stateMachineInitializationDefinition(owner.getUnitName(), poolId, 
				submachineMap.isEmpty() ? Optional.empty() : Optional.of(getStateToSubMachineNameMap())));
		source.append(StateMachineTemplates.stateMachineFixFunctionDefitions(owner.getUnitName(), 
				getInitialState(stateMachineRegion) ,false, submachineMap.isEmpty()));
		source.append(guardExporter.defnieGuardFunctions(owner.getUnitName()));
		source.append(entryExitFunctionExporter.createEntryFunctionsDef());
		source.append(entryExitFunctionExporter.createExitFunctionsDef());
		source.append(transitionExporter.createTransitionFunctionsDef());

		source.append(StateMachineTemplates.entry(owner.getUnitName(),
				createStateActionMap(entryExitFunctionExporter.getEntryMap())) + "\n");
		source.append(
				StateMachineTemplates.exit(owner.getUnitName(), 
						createStateActionMap(entryExitFunctionExporter.getExitMap()))
						+ "\n");
		
		source.append(StateMachineTemplates.finalizeFunctionDef(owner.getUnitName()));
		source.append(StateMachineTemplates.initializeFunctionDef(owner.getUnitName(), getInitialTransition(stateMachineRegion)));


		return source.toString();
	}

	public String createStateEnumCode() {		
		return StateMachineTemplates.stateEnum(stateList, getInitialState(stateMachineRegion));
	}

	public boolean ownSubMachine() {
		return !submachineMap.isEmpty();
	}

	public Map<String, Pair<String, Region>> getSubMachineMap() {
		return submachineMap;
	}


}
