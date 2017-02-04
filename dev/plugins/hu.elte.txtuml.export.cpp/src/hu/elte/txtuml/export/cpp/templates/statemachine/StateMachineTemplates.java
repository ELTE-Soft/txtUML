package hu.elte.txtuml.export.cpp.templates.statemachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.uml2.uml.State;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.statemachine.TransitionConditions;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;
import hu.elte.txtuml.utils.Pair;

public class StateMachineTemplates {

	public static final String InitStateMachineProcedureName = GenerationNames.InitStateMachine;
	public static final String StateMachineBaseHeader = GenerationNames.StatemachineBaseHeaderName + "."
			+ FileNames.HeaderExtension;
	public static final String InitTransitionTable = "initTransitionTable";
	public static final String AllTransitionTableInitialProcName = "initTransitionTables";
	public static final String TransitionTableInitialSourceName = "init_maps";
	public static final String MachineNamespace = "StateMachine";
	public static final String ProcessInitTransitionFunctionName = "processInitTransition";

	public static String transitionActionDecl(String transitionActionName) {
		List<String> params = new LinkedList<String>();
		params.add(EventTemplates.EventBaseRefName);

		return FunctionTemplates.functionDecl(transitionActionName, params);
	}

	public static String transitionActionDef(String className, String transitionActionName, String body,
			boolean singalAcces) {
		List<Pair<String, String>> params = new LinkedList<Pair<String, String>>();
		if (singalAcces) {
			params.add(new Pair<String, String>(EventTemplates.EventBaseRefName, EventTemplates.EventParamName));
		} else {
			params.add(new Pair<String, String>(EventTemplates.EventBaseRefName, ""));

		}

		return FunctionTemplates.functionDef(className, transitionActionName, params,
				PrivateFunctionalTemplates.debugLogMessage(className, transitionActionName) + body);
	}

	public static String guardDeclaration(String guardFunctionName) {
		StringBuilder source = new StringBuilder(
				"bool " + guardFunctionName + "(" + EventTemplates.EventBaseRefName + ");\n");
		return source.toString();
	}

	public static String guardDefinition(String guardFunctionName, String constraint, String className,
			boolean eventParamUsage) {
		StringBuilder source = new StringBuilder(
				"bool " + className + "::" + guardFunctionName + "(" + EventTemplates.EventBaseRefName);
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
			parameter = EventTemplates.EventBaseRefName;
		} else {
			parameter = EventTemplates.EventBaseRefName + " " + EventTemplates.EventFParamName;
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

	public static String stateMachineInitializationSharedBody(String className, String intialState, Boolean simpleMachine,
			Integer poolId) {
		StringBuilder source = new StringBuilder("");

		if (poolId != null) {
			source.append(GenerationNames.PoolIdSetter + "(" + poolId + ");\n");
		}
		if (simpleMachine) {
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
								+ PortTemplates.ponrtEnumName(key.getPort()) + "),");
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

	public static String stateMachineClassFixPublicParts(String className, Boolean ownStateMachine) {
		StringBuilder source = new StringBuilder("");
		source.append(ModifierNames.StaticModifier + " " + FunctionTemplates.functionDecl(InitTransitionTable));
		source.append(GenerationNames.ProcessEventDecl + GenerationNames.SetInitialStateDecl + "\n");
		if (ownStateMachine) {
			source.append("//RuntimeFunctions\n" + RuntimeTemplates.HeaderFuncs + "\n");
		}
		return source.toString();
	}

	public static String hierarchicalStateMachineClassFixPrivateParts(String className, List<String> subMachines) {
		return "//Hierarchical Machine Parts\n" + GenerationNames.ActionCallerDecl + GenerationNames.CurrentMachine
				+ GenerationNames.CompositeStateMap + SubStateMachineTemplates.subMachineFriendDecls(subMachines)
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
	public static String hierarchicalStateMachineClassConstructorSharedBody(String className,
			String parentStateMchine,
			Map<String, String> subMachines, String intialState, Boolean rt) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, String> entry : subMachines.entrySet()) {
			source.append(
					GenerationNames.CompositeStateMapName + ".emplace(" + GenerationNames.stateEnumName(entry.getKey())
							+ "," + GenerationNames.CompositeStateMapSmType + "(" + PointerAndMemoryNames.MemoryAllocator
							+ " " + entry.getValue() + "(" + PointerAndMemoryNames.Self + ")" + "));\n");
		}
		return source.toString();
	}

	public static String simpleStateMachineClassConstructorSharedBody(String className,
			Multimap<TransitionConditions, Pair<String, String>> machine, String intialState, Boolean simpleMachine) {
		String source = "";
		if (simpleMachine) {
			source += RuntimeTemplates.rtFunctionDef(className);
		}

		return source + GenerationNames.simpleProcessEventDef(className) + "\n"
				+ GenerationNames.simpleSetStateDef(className) + "\n" + setInitialState(className, intialState) + "\n";
	}

	public static String setState(String state) {
		return GenerationNames.SetStateFuncName + "(" + GenerationNames.stateEnumName(state) + ");\n";
	}

	public static String simpleStateMachineInitializationDefinition(String className, String intialState, Boolean rt,
			Integer poolId) {
		StringBuilder body = new StringBuilder("");
		body.append(stateMachineInitializationSharedBody(className, intialState, rt, poolId));

		return FunctionTemplates.functionDef(className, GenerationNames.InitStateMachine, body.toString());
	}

	public static String hierachialStateMachineInitialization(String className, String intialState, Boolean rt,
			Integer poolId,
			Map<String, String> subMachines) {
		StringBuilder body = new StringBuilder("");
		body.append(GenerationNames.CurrentMachineName + " = " + PointerAndMemoryNames.NullPtr + ";\n");
		body.append(hierarchicalStateMachineClassConstructorSharedBody(className, className, subMachines,
				intialState, rt));
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
