package hu.elte.txtuml.export.cpp.statemachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;


import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.utils.Pair;
import org.eclipse.uml2.uml.Element;

public class StateMachineExporter extends StateMachineExporterBase {

	private boolean ownStateMachine;
	private int poolId;
	

	public StateMachineExporter(Shared shared) {
		super(shared);
	}

	public void setStateMachineThreadPoolId(int id) {
		this.poolId = id;
	}

	public <E extends Element> void createStateMachineRegion(E element) {
		List<StateMachine> smList = new ArrayList<StateMachine>();
		shared.getTypedElements(smList, UMLPackage.Literals.STATE_MACHINE);
		if (!smList.isEmpty()) {
			stateMachineRegion = smList.get(0).getRegions().get(0);
			createStateList();
			ownStateMachine = !stateList.isEmpty();

		} else {
			ownStateMachine = false;
		}
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
		if (submachineMap.isEmpty()) {
			source.append(StateMachineTemplates.simpleStateMachineInitializationDefinition(ownerClassName, getInitialStateName(), true,
					poolId));
			source.append(StateMachineTemplates.simpleStateMachineFixFunctionDefinitions(ownerClassName, getInitialStateName(),
					false));

		} else {
			source.append(StateMachineTemplates.hierachialStateMachineInitialization(ownerClassName, getInitialStateName(),
					true, poolId, getEventSubMachineNameMap()));
			source.append(StateMachineTemplates.hiearchialStateMachineFixFunctionDefinitions(ownerClassName,
					getInitialStateName(), false));

		}
		source.append(guardExporter.defnieGuardFunctions(ownerClassName));
		source.append(entryExitFunctionExporter.createEntryFunctionsDef());
		source.append(entryExitFunctionExporter.createExitFunctionsDef());
		source.append(transitionExporter.createTransitionFunctionsDef());

		source.append(
				StateMachineTemplates.entry(ownerClassName, createStateActionMap(entryExitFunctionExporter.getEntryMap()))
						+ "\n");
		source.append(StateMachineTemplates.exit(ownerClassName, createStateActionMap(entryExitFunctionExporter.getExitMap()))
				+ "\n");

		return source.toString();
	}

	public String createStateEnumCode() {
		return StateMachineTemplates.stateEnum(stateList, getInitialStateName());
	}

	public boolean ownStateMachine() {
		return ownStateMachine;
	}


	public boolean ownSubMachine() {
		return submachineMap.isEmpty();
	}

	public Map<String, Pair<String, Region>> getSubMachineMap() {
		return submachineMap;
	}

}
