package hu.elte.txtuml.export.cpp.templates.statemachine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.uml2.uml.State;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.statemachine.TransitionConditions;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.EntryExitNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
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
	public static String entry(String className, Map<String, String> states, Boolean hiearchicalStateMachine) {
		return StateMachineTemplates.entryExitTemplate(EntryExit.Entry, className, states, hiearchicalStateMachine);
	}

	/*
	 * Map<String,String> state ,action
	 */
	public static String exit(String className, Map<String, String> states,Boolean hiearchicalStateMachine) {
		return StateMachineTemplates.entryExitTemplate(EntryExit.Exit, className, states, hiearchicalStateMachine);
	}

	public static String stateEnum(Iterable<State> states, String initialState) {
		StringBuilder StateList = new StringBuilder("enum States {");

		StateList.append(GenerationNames.stateEnumName(initialState) + ",");
		for (State item : states) {
			StateList.append(GenerationNames.stateEnumName(item.getName()) + ",");
		}
		return StateList.substring(0, StateList.length() - 1) + "};\n";
	}

	public static String setInitialState(String className, String initialState) {

		return ModifierNames.NoReturn + " " + className + "::" + GenerationNames.SetInitialStateName + "(){"
				+ GenerationNames.SetStateFuncName + "(" + GenerationNames.stateEnumName(initialState) + ");}\n";
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
	
	public static String initializeFunctionDef(String className, String initFuction) {
		String body = ActivityTemplates.blockStatement(ActivityTemplates.operationCall(initFuction, EventTemplates.EventParamVarList)) + GenerationNames.EntryExitNames.EntryInvoke;
		return FunctionTemplates.functionDef(className, StateMachineMethodNames.InitializeFunctionName, EventTemplates.EventParamDefList,body);

	}
	
	public static String entryExitTemplate(EntryExit type, String className, Map<String, String> states, Boolean hierachicalMachine) {

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
			if(hierachicalMachine && type == EntryExit.Exit) {
				source.append(ActivityTemplates.simpleIf(GenerationNames.CurrentMachineName,
						ActivityTemplates.operationCallOnPointerVariable(GenerationNames.CurrentMachineName,
						StateMachineMethodNames.FinalizeFunctionName,
						Arrays.asList(EventTemplates.EventFParamName))));
			}
			source.append("switch(" + GenerationNames.CurrentStateName + ")\n{\n");
			for (Map.Entry<String, String> entry : states.entrySet()) {
				source.append("case(" + GenerationNames.stateEnumName(entry.getKey()) + "):{");
				source.append(ActivityTemplates.operationCall(entry.getValue(), eventParameter) + ";");

				source.append("break;}\n");
			}
			source.append("}\n");
		}
		if(hierachicalMachine && type == EntryExit.Entry) {		
			source.append(ActivityTemplates.simpleIf(GenerationNames.CurrentMachineName, 
					ActivityTemplates.operationCallOnPointerVariable(GenerationNames.CurrentMachineName,
					StateMachineMethodNames.InitializeFunctionName,
					Arrays.asList(EventTemplates.EventFParamName))));
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

	public static String stateMachineClassFixPublicParts(String className, Boolean ownStateMachine) {
		StringBuilder source = new StringBuilder("");
		source.append(ModifierNames.StaticModifier + " " + FunctionTemplates.functionDecl(InitTransitionTable));
		source.append(GenerationNames.ProcessEventDecl + GenerationNames.SetInitialStateDecl + "\n");
		if (ownStateMachine) {
			source.append("//RuntimeFunctions\n" + RuntimeTemplates.processEventVirtualDecl()
					+ RuntimeTemplates.processInitTransitionDecl() + "\n");
		}
		return source.toString();
	}

	public static String hierarchicalStateMachineClassFixPrivateParts(String className, List<String> subMachines) {
		return "//Hierarchical Machine Parts\n" + GenerationNames.ActionCallerDecl + GenerationNames.CurrentMachine
				+ GenerationNames.CompositeStateMap + SubStateMachineTemplates.subMachineFriendDecls(subMachines)
				+ "//Simple Machine Parts\n" + StateMachineTemplates.stateMachineClassFixPrivateParts(className);
	}

	public static String stateMachineClassFixPrivateParts(String className) {
		return FunctionTemplates.functionDecl(GenerationNames.InitStateMachine) + "\n" + GenerationNames.SetStateDecl
				+ EntryExitNames.EntryDecl + EntryExitNames.ExitDecl + "\n"  + "int "
				+ GenerationNames.CurrentStateName + ";\n" + initializeFunctionDecl() + finalizeFunctionDecl();
	}

	public static String simpleStateMachineClassFixProtectedParts(String className) {
		return PrivateFunctionalTemplates.typedefs(className)
				+ PrivateFunctionalTemplates.transitionTableDecl(className);
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
		source.append(stateMachineInitializationSharedBody(topMachine, poolId));
		String parent = topMachine ? PointerAndMemoryNames.Self : GenerationNames.ParentSmMemberName;
		for (Map.Entry<String, String> entry : subMachines.entrySet()) {
			source.append(GenerationNames.CompositeStateMapName + ".emplace("
					+ GenerationNames.stateEnumName(entry.getKey()) + "," + GenerationNames.CompositeStateMapSmType
					+ "(" + PointerAndMemoryNames.MemoryAllocator + " " + entry.getValue() + "(" + parent + ")"
					+ "));\n");
		}
		return source.toString();
	}

	public static String setState(String state) {
		return GenerationNames.SetStateFuncName + "(" + GenerationNames.stateEnumName(state) + ");\n";
	}

	public static String simpleStateMachineInitializationDefinition(String className, String intialState, Boolean rt,
			Integer poolId) {
		return FunctionTemplates.functionDef(className, GenerationNames.InitStateMachine,
				stateMachineInitializationSharedBody(rt, poolId));
	}

	public static String hierachialStateMachineInitialization(String className, String intialState, Boolean rt,
			Integer poolId, Map<String, String> subMachines) {
		StringBuilder body = new StringBuilder("");
		body.append(GenerationNames.CurrentMachineName + " = " + PointerAndMemoryNames.NullPtr + ";\n");
		body.append(hierarchicalStateMachineClassConstructorSharedBody(subMachines, rt, poolId));
		return FunctionTemplates.functionDef(className, GenerationNames.InitStateMachine, body.toString());
	}

	public static String hiearchialStateMachineFixFunctionDefinitions(String className, String intialState,
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

	public static String simpleStateMachineFixFunctionDefinitions(String className, String initialState, Boolean subM) {
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
