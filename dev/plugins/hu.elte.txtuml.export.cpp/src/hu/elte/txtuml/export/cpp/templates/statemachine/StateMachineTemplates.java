package hu.elte.txtuml.export.cpp.templates.statemachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.uml2.uml.State;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.statemachine.TransitionConditions;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HiearchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates.HeaderType;
import hu.elte.txtuml.utils.Pair;

public class StateMachineTemplates {

	public static final String InitStateMachineProcedureName = GenerationNames.InitStateMachine;
	public static final String StateMachineBaseHeader = GenerationNames.StatemachineBaseHeaderName + "."
			+ FileNames.HeaderExtension;
	public static final String InitTransitionTable = "initTransitionTable";
	public static final String AllTransitionTableInitialProcName = "initTransitionTables";
	public static final String TransitionTableInitialSourceName = "init_maps";
	public static final String ProcessInitTransitionFunctionName = "processInitTransition";

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
		return StateMachineTemplates.entryExitTemplate(GenerationNames.EntryName, className, states);
	}

	/*
	 * Map<String,String> state ,action
	 */
	public static String exit(String className, Map<String, String> states) {
		return StateMachineTemplates.entryExitTemplate(GenerationNames.ExitName, className, states);
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

	public static String entryExitTemplate(String typeName, String className, Map<String, String> states) {
		
		String parameter;
		if (states == null || states.isEmpty()) {
			parameter = EventTemplates.EventPointerType;
		} else {
			parameter = EventTemplates.EventPointerType + " " + EventTemplates.EventFParamName;
		}
		
		String source = ModifierNames.NoReturn + " " + className + "::" + typeName + "(" + parameter + ")\n{\n";
		if (states != null && !states.isEmpty()) {
			List<String> eventParameter = new LinkedList<String>();
			eventParameter.add(EventTemplates.EventFParamName);
			source += "switch(" + GenerationNames.CurrentStateName + ")\n{\n";
			for (Map.Entry<String, String> entry : states.entrySet()) {
				source += "case(" + GenerationNames.stateEnumName(entry.getKey()) + "):{" + ActivityTemplates.operationCall(entry.getValue(),eventParameter)
						+ ";break;}\n";
			}
			source += "}\n";
		}
		return source + "}\n";
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
	
	public static String transitionTableInitilizationBody(String className, Multimap<TransitionConditions, Pair<String, String>> machine) {
		StringBuilder source = new StringBuilder("");
		for (TransitionConditions key : machine.keySet()) {
			for (Pair<String, String> value : machine.get(key)) {
				source.append(className + "::" + GenerationNames.TransitionTableName + ".emplace(" + GenerationNames.EventStateTypeName
						+ "(" + EventTemplates.EventsEnumName + "::");
				source.append(GenerationNames.eventEnumName(key.getEvent()) + ","
						+ GenerationNames.stateEnumName(key.getState()) + ","
								+ key.getPort() + "),");
				String guardName = GenerationNames.DefaultGuardName;
				if (value.getFirst() != null) {
					guardName = value.getFirst();
				}
				source.append(GenerationNames.GuardActionName + "("  + GenerationNames.GuardFuncTypeName + "(&"
						+ className + "::" + guardName + ")," + GenerationNames.FunctionPtrTypeName + "(&" + className
						+ "::" + value.getSecond() + ")));\n");
			}

		}
		
		return source.toString();
	}

	public static String stateMachineClassFixPublicParts(HeaderType headerType) {
		StringBuilder source = new StringBuilder("");
		source.append(ModifierNames.StaticModifier + " " + FunctionTemplates.functionDecl(InitTransitionTable));
		source.append(GenerationNames.ProcessEventDecl + GenerationNames.SetInitialStateDecl + "\n");
		if (headerType.hasExecutionInterface()) {
			source.append("//RuntimeFunctions\n" + 
					RuntimeTemplates.processEventVirtualDecl() + RuntimeTemplates.processInitTransitionDecl() + "\n");
		}
		return source.toString();
	}

	public static String hierarchicalStateMachineClassFixPrivateParts(String className, List<String> subMachines) {
		return "//Hierarchical Machine Parts\n" + HiearchicalStateMachineNames.ActionCallerDecl + HiearchicalStateMachineNames.CurrentMachine
				+ HiearchicalStateMachineNames.CompositeStateMap + SubStateMachineTemplates.subMachineFriendDecls(subMachines)
				+ "//Simple Machine Parts\n" + StateMachineTemplates.simpleStateMachineClassFixPrivateParts(className);
	}

	public static String simpleStateMachineClassFixPrivateParts(String className) {
		return  FunctionTemplates.functionDecl(GenerationNames.InitStateMachine) + "\n" + 
				GenerationNames.SetStateDecl + GenerationNames.EntryDecl + GenerationNames.ExitDecl + 
				"\n" + "int " + GenerationNames.CurrentStateName + ";\n";
	}
	
	public static String simpleStateMachineClassFixProtectedParts(String className) {
		return  PrivateFunctionalTemplates.typedefs(className) + 
				PrivateFunctionalTemplates.transitionTableDecl(className);
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 * 
	 * Map<String,String> <event,SubmachineName>
	 */
	public static String hierarchicalStateMachineClassConstructorSharedBody(Map<String, String> subMachines, Boolean topMachine, Integer poolId) {
		StringBuilder source = new StringBuilder("");
		source.append(stateMachineInitializationSharedBody(topMachine,poolId));
		String parent = topMachine ? PointerAndMemoryNames.Self : HiearchicalStateMachineNames.ParentSmMemberName;
		for (Map.Entry<String, String> entry : subMachines.entrySet()) {
			source.append(
					HiearchicalStateMachineNames.CompositeStateMapName + ".emplace(" + GenerationNames.stateEnumName(entry.getKey())
							+ "," + HiearchicalStateMachineNames.CompositeStateMapSmType + "(" + PointerAndMemoryNames.MemoryAllocator
							+ " " + entry.getValue() + "(" + parent + ")" + "));\n");
		}
		return source.toString();
	}

	public static String setState(String state) {
		return GenerationNames.SetStateFuncName + "(" + GenerationNames.stateEnumName(state) + ");\n";
	}

	public static String simpleStateMachineInitializationDefinition(String className, String intialState, Boolean rt,
			Integer poolId) {
		return FunctionTemplates.functionDef(className, GenerationNames.InitStateMachine, stateMachineInitializationSharedBody(rt, poolId));
	}

	public static String hierachialStateMachineInitialization(String className, String intialState, Boolean rt,
			Integer poolId,
			Map<String, String> subMachines) {
		StringBuilder body = new StringBuilder("");
		body.append(HiearchicalStateMachineNames.CurrentMachineName + " = " + PointerAndMemoryNames.NullPtr + ";\n");
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
