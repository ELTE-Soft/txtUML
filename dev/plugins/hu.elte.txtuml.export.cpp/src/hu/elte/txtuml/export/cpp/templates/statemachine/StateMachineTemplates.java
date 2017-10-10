package hu.elte.txtuml.export.cpp.templates.statemachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.statemachine.TransitionConditions;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.EntryExitNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HierarchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.StateMachineMethodNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.utils.Pair;

public class StateMachineTemplates {

	public static final String InitStateMachineProcedureName = GenerationNames.InitStateMachine;
	public static final String StateMachineBaseHeader = GenerationNames.StatemachineBaseHeaderName + "."
			+ FileNames.HeaderExtension;
	public static final String InitTransitionTable = "initTransitionTable";
	public static final String AllTransitionTableInitialProcName = "initTransitionTables";
	public static final String TransitionTableInitialSourceName = "init_maps";
	public static final String ProcessInitTransitionFunctionName = "processInitTransition";

	public enum EntryExit {
		Entry, Exit
	}

	public static String transitionActionDecl(String transitionActionName) {
		List<String> params = new LinkedList<String>();
		params.add(EventTemplates.EventPointerType);

		return FunctionTemplates.functionDecl(transitionActionName, params);
	}

	public static String transitionActionDef(String className, String transitionFunctionName,
			String transitionActionName, String body, boolean singalAcces) {
		List<Pair<String, String>> params = new LinkedList<Pair<String, String>>();
		if (singalAcces) {
			params.add(new Pair<String, String>(EventTemplates.EventPointerType, EventTemplates.EventParamName));
		} else {
			params.add(new Pair<String, String>(EventTemplates.EventPointerType, ""));

		}

		return FunctionTemplates.functionDef(className, transitionFunctionName, params,
				PrivateFunctionalTemplates.debugLogMessage(className, transitionActionName) + body);
	}

	public static String guardDeclaration(String guardFunctionName) {
		StringBuilder source = new StringBuilder(
				"bool " + guardFunctionName + "(" + EventTemplates.EventPointerType + ");\n");
		return source.toString();
	}

	public static String guardDefinition(String guardFunctionName, String constraint, String className,
			boolean eventParamUsage) {
		StringBuilder source = new StringBuilder(
				"bool " + className + "::" + guardFunctionName + "(" + EventTemplates.EventPointerType);
		if (eventParamUsage) {
			source.append(" " + EventTemplates.EventFParamName);

		}
		source.append(")\n{\n");

		return source + constraint + "}\n";
	}

	/*
	 * Map<String,String> state ,actionName
	 */
	public static String entry(String className, Map<String, String> states) {
		return StateMachineTemplates.entryExitTemplate(EntryExit.Entry, className, states);
	}

	/*
	 * Map<String,String> state ,action
	 */
	public static String exit(String className, Map<String, String> states) {
		return StateMachineTemplates.entryExitTemplate(EntryExit.Exit, className, states);
	}

	public static String stateEnum(Iterable<State> states, Optional<Pseudostate> initialState) {
		String stateType = "enum States : int";
		String finalStateList = "";
		StringBuilder statesDecl = new StringBuilder("");
		if(initialState.isPresent()) {
			statesDecl.append(GenerationNames.stateEnumName(initialState.get().getName()) + ",");
		}
		for (State item : states) {
			statesDecl.append(GenerationNames.stateEnumName(item.getName()) + ",");
		}
		String cuttedStateList = CppExporterUtils.cutOffTheLastCharcter(statesDecl.toString());
		if(!cuttedStateList.isEmpty()) {
			finalStateList = "{" + cuttedStateList + "}";
		}
		return stateType.toString() +  finalStateList + ";\n";
	}

	public static String setInitialState(String className, Optional<Pseudostate> initialState) {
		String body = initialState.isPresent() ? GenerationNames.SetStateFuncName + "(" + GenerationNames.stateEnumName(initialState.get().getName()) + ");" : "";
		return ModifierNames.NoReturn + " " + className + "::" + GenerationNames.SetInitialStateName + "(){" + body + "}\n";
	}
	public static String finalizeFunctionDecl() {
		return FunctionTemplates.functionDecl(StateMachineMethodNames.FinalizeFunctionName, EventTemplates.EventParamDeclList);
	}
	public static String initializeFunctionDecl() {
		return FunctionTemplates.functionDecl(StateMachineMethodNames.InitializeFunctionName, EventTemplates.EventParamDeclList);
	}
	
	public static String finalizeFunctionDef(String className) {
		return FunctionTemplates.functionDef(className, StateMachineMethodNames.FinalizeFunctionName, EventTemplates.EventParamDefList, GenerationNames.EntryExitNames.ExitInvoke);
	}
	
	public static String initializeFunctionDef(String className, Optional<Transition> initTransitionFunction) {
		String initTransitionInvoke = initTransitionFunction.isPresent() ?  
				ActivityTemplates.blockStatement  (ActivityTemplates.operationCall(initTransitionFunction.get().getName(), EventTemplates.EventParamVarList)) : "";
		String entryInvoke = GenerationNames.EntryExitNames.EntryInvoke;
		return FunctionTemplates.functionDef(className, StateMachineMethodNames.InitializeFunctionName, EventTemplates.EventParamDefList, initTransitionInvoke + entryInvoke);

	}
	
	public static String entryExitTemplate(EntryExit type, String className, Map<String, String> states) {

		String parameter;
		if (states == null || states.isEmpty()) {
			parameter = EventTemplates.EventPointerType;
		} else {
			parameter = EventTemplates.EventPointerType + " " + EventTemplates.EventFParamName;
		}
		String functionName = "";
		switch (type) {
		case Entry:
			functionName = EntryExitNames.EntryName;
			break;
		case Exit:
			functionName = EntryExitNames.ExitName;
			break;
		default:
			assert (false);
			break;

		}
		StringBuilder source = new StringBuilder(
				ModifierNames.NoReturn + " " + className + "::" + functionName + "(" + parameter + ")\n{\n");
		if (states != null && !states.isEmpty()) {
			List<String> eventParameter = new LinkedList<String>();
			eventParameter.add(EventTemplates.EventFParamName);
			source.append("switch(" + GenerationNames.CurrentStateName + ")\n{\n");
			for (Map.Entry<String, String> entry : states.entrySet()) {
				source.append("case(" + GenerationNames.stateEnumName(entry.getKey()) + "):{");
				source.append(ActivityTemplates.operationCall(entry.getValue(), eventParameter) + ";");

				source.append("break;}\n");
			}
			source.append("}\n");
		}
		source.append("}\n");
		return source.toString();
	}

	public static String stateMachineInitializationSharedBody(boolean isTopStateMachine, Integer poolId) {
		StringBuilder source = new StringBuilder("");

		if (poolId != null) {
			source.append(GenerationNames.PoolIdSetter + "(" + poolId + ");\n");
		}
		if (isTopStateMachine) {
			source.append(RuntimeTemplates.initStateMachineForRuntime());
		}
		source.append(GenerationNames.SetInitialStateName + "();\n");

		return source.toString();
	}

	public static String transitionTableInitilizationBody(String className,
			Multimap<TransitionConditions, Pair<String, String>> machine) {
		StringBuilder source = new StringBuilder("");
		for (TransitionConditions key : machine.keySet()) {
			for (Pair<String, String> value : machine.get(key)) {
				source.append(className + "::" + GenerationNames.TransitionTableName + ".emplace("
						+ GenerationNames.EventStateTypeName + "(" + EventTemplates.EventsEnumName + "::");
				source.append(GenerationNames.eventEnumName(key.getEvent()) + ","
						+ GenerationNames.stateEnumName(key.getState()) + "," + key.getPort() + "),");
				String guardName = GenerationNames.DefaultGuardName;
				if (value.getFirst() != null) {
					guardName = value.getFirst();
				}
				source.append(GenerationNames.GuardActionName + "(" + GenerationNames.GuardFuncTypeName + "(&"
						+ className + "::" + guardName + ")," + GenerationNames.FunctionPtrTypeName + "(&" + className
						+ "::" + value.getSecond() + ")));\n");
			}

		}

		return source.toString();
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 * 
	 * Map<String,String> <event,SubmachineName>
	 */
	public static String hierarchicalStateMachineClassConstructorSharedBody(Map<String, String> subMachines,
			Boolean topMachine, Integer poolId) {
		StringBuilder source = new StringBuilder("");
		source.append(stateMachineInitializationSharedBody(topMachine,poolId));
		String parent = topMachine ? PointerAndMemoryNames.Self : HierarchicalStateMachineNames.ParentSmMemberName;
		for (Map.Entry<String, String> entry : subMachines.entrySet()) {
			source.append(
					HierarchicalStateMachineNames.CompositeStateMapName + ".emplace(" + GenerationNames.stateEnumName(entry.getKey())
							+ "," + HierarchicalStateMachineNames.CompositeStateMapSmType + "(" + PointerAndMemoryNames.MemoryAllocator
							+ " " + entry.getValue() + "(" + parent + ")" + "));\n");
		}
		return source.toString();
	}

	public static String setState(String state) {
		return GenerationNames.SetStateFuncName + "(" + GenerationNames.stateEnumName(state) + ");\n";
	}
	public static String stateMachineInitializationDefinition(String className, Integer poolId, Optional<Map<String, String>> optionalSubMachines) {
		if(!optionalSubMachines.isPresent()) {
			return FunctionTemplates.functionDef(className, GenerationNames.InitStateMachine, stateMachineInitializationSharedBody(true, poolId));
		} else {
			Map<String, String> subMachines = optionalSubMachines.get();
			StringBuilder body = new StringBuilder("");
			body.append(HierarchicalStateMachineNames.CurrentMachineName + " = " + PointerAndMemoryNames.NullPtr + ";\n");
			body.append(hierarchicalStateMachineClassConstructorSharedBody(subMachines, true, poolId));
			return FunctionTemplates.functionDef(className, GenerationNames.InitStateMachine, body.toString());
		}
	}
	
	public static String stateMachineFixFunctionDefitions(String className, Optional<Pseudostate> initialState, Boolean subSM, Boolean simple) {
		if(simple) {
			return simpleStateMachineFixFunctionDefinitions (className, initialState, subSM);
		} else {
			return hiearchialStateMachineFixFunctionDefinitions (className, initialState, subSM);
		}

	}
	
	private static String hiearchialStateMachineFixFunctionDefinitions(String className,Optional<Pseudostate>intialState,
			Boolean subM) {

		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.hierachicalProcessEventDef(className) + "\n");
		if (!subM) {
			source.append(RuntimeTemplates.rtFunctionDef(className) + "\n");
		}

		source.append(
				GenerationNames.actionCallerDef(className) + "\n" + GenerationNames.hierachicalSetStateDef(className)
						+ "\n" + setInitialState(className, intialState) + "\n");

		return source.toString();
	}

	private static String simpleStateMachineFixFunctionDefinitions(String className, Optional<Pseudostate> initialState, Boolean subM) {
		StringBuilder source = new StringBuilder("");

		source.append(GenerationNames.simpleProcessEventDef(className));
		if (!subM) {
			source.append(RuntimeTemplates.rtFunctionDef(className));
		}

		source.append(GenerationNames.simpleSetStateDef(className) + "\n");
		source.append(setInitialState(className, initialState));

		return source.toString();
	}

}
