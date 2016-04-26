package hu.elte.txtuml.export.cpp.templates;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.utils.Pair;

class PrivateFunctionalTemplates {

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 * 
	 * Map<String,String> <event,SubmachineName>
	 */
	public static StringBuilder hierarchicalStateMachineClassConstructorSharedBody(String className, String parentStateMchine,
			 Multimap<Pair<String, String>, Pair<String, String>> machine,
			Map<String, String> subMachines, String intialState, Boolean rt) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, String> entry : subMachines.entrySet()) {
			source.append(
					GenerationNames.CompositeStateMapName + ".emplace(" + GenerationNames.stateEnumName(entry.getKey())
							+ "," + GenerationNames.CompositeStateMapSmType + "(" + GenerationNames.MemoryAllocator
							+ " " + entry.getValue() + "()" + "));\n");
		}

		source.append("\n"
				+ stateMachineClassConstructorSharedBody(className,parentStateMchine, machine, intialState, rt, null)
				+ "\n\n");
		return source;
	}

	public static String simpleStateMachineClassConstructorSharedBody(String className,
			Multimap<Pair<String, String>, Pair<String, String>> machine, String intialState, Boolean simpleMachine) {
		String source = "";
		if(simpleMachine) {
			source += RuntimeTemplates.rtFunctionDef(className);
		}
		
		

		return source + GenerationNames.simpleProcessEventDef(className) + "\n"
				+ GenerationNames.simpleSetStateDef(className) + "\n"
				+ PrivateFunctionalTemplates.setInitialState(className, intialState) + "\n";
	}
	
	public static String signalType(String type) {
		return type + GenerationNames.EventClassTypeId;
	}
	

	public static StringBuilder stateMachineClassConstructorSharedBody(String className, String parentStateMachine,
			Multimap<Pair<String, String>, Pair<String, String>> machine, String intialState, Boolean simpleMachine,
			Integer poolId) {
		StringBuilder source = new StringBuilder("");
		for (Pair<String, String> key : machine.keySet()) {
			for (Pair<String, String> value : machine.get(key)) {
				source.append(
						GenerationNames.TransitionTableName + ".emplace(" +  GenerationNames.EventStateTypeName + "(" + GenerationNames.EventsEnumName + "::");
				source.append(GenerationNames.eventEnumName(key.getFirst()) + ","
						+ GenerationNames.stateEnumName(key.getSecond()) + "),");
				String guardName = GenerationNames.DefaultGuardName;
				if (value.getFirst() != null) {
					guardName = value.getFirst();
				}
				source.append(GenerationNames.GuardActionName + "(" + GenerationNames.GuardFuncTypeName + "(&"
						+ className + "::" + guardName + ")," + GenerationNames.FunctionPtrTypeName + "(&" + className
						+ "::" + value.getSecond() + ")));\n");
			}

		}

		if (poolId != null) {
			source.append(GenerationNames.PoolIdSetter + "(" + poolId + ");\n");
		}
		if(simpleMachine) {
			source.append(RuntimeTemplates.initStateMachineForRuntime());
		}
		

		source.append(GenerationNames.SetInitialStateName + "();\n");

		return source;
	}

	public static String classHeaderIncludes(Boolean rt) {
		String source = include(GenerationNames.StatemachineBaseHeaderName);

		if (rt) {
			source += "\n" + include(RuntimeTemplates.RTPath + RuntimeTemplates.SMIHeaderName);
		}
		return source + "\n";
	}

	public static String include(String className) {
		if (className.contains("std::")) {
			return outerInclude(className);
		} else {
			return localInclude(className);
		}
	}

	public static String localInclude(String className) {
		return "#include \"" + className + "." + GenerationNames.HeaderExtension + "\"\n";
	}

	public static String outerInclude(String className) {
		if (stdType(className)) {
			className = className.substring(5);
		}
		return "#include <" + className + ">\n";
	}

	public static String stateMachineClassFixPublicParts(String className, Boolean rt) {
		String source = GenerationNames.ProcessEventDecl + GenerationNames.SetInitialStateDecl + "\n";
		if (rt) {
			source += "//RuntimeFunctions\n" + RuntimeTemplates.HeaderFuncs + "\n";
		}
		return source;
	}

	public static String simpleStateMachineClassFixPrivateParts(String className) {
		return GenerationNames.SetStateDecl + GenerationNames.NoReturn + " " + GenerationNames.EntryName + "();\n"
				+ GenerationNames.NoReturn + " " + GenerationNames.ExitName + "();\n\n" + "int "
				+ GenerationNames.CurrentStateName + ";\n" + typedefs(className) + GenerationNames.TransitionTable;
	}

	public static String hierarchicalStateMachineClassFixPrivateParts(String className, List<String> subMachines) {
		return "//Hierarchical Machine Parts\n" + GenerationNames.ActionCallerDecl + GenerationNames.CurrentMachine
				+ GenerationNames.CompositeStateMap + subMachineFriendDecls(subMachines) + "//Simple Machine Parts\n"
				+ simpleStateMachineClassFixPrivateParts(className);
	}

	private static StringBuilder subMachineFriendDecls(List<String> subMachines) {
		StringBuilder source = new StringBuilder("");
		for (String subMachine : subMachines) {
			source.append(GenerationNames.friendClassDecl(subMachine));
		}
		return source;
	}

	public static String typedefs(String className) {
		return "typedef std::function<" + GenerationNames.NoReturn + "(" + className + "&,"
				+ GenerationNames.EventBaseRefName + ")> " + GenerationNames.FunctionPtrTypeName + ";\n"
				+ "typedef std::function<bool(" + className + "&," + GenerationNames.EventBaseRefName + ")> "
				+ GenerationNames.GuardFuncTypeName + ";\n" + "typedef std::pair<" + GenerationNames.GuardFuncTypeName
				+ "," + GenerationNames.FunctionPtrTypeName + "> " + GenerationNames.GuardActionName + ";\n";
	}

	public static String pointerBaseType(String typeName) {
		return typeName.substring(0, typeName.indexOf("*"));
	}

	public static String paramList(List<Pair<String, String>> params) {
		if (params == null || params.size() == 0)
			return "";
		StringBuilder source = new StringBuilder("");
		for (Pair<String, String> item : params) {
			source.append(PrivateFunctionalTemplates.cppType(item.getFirst()) + " "
					+ GenerationNames.formatIncomingParamName(item.getSecond()) + ",");
		}
		return source.substring(0, source.length() - 1);
	}

	public static String paramTypeList(List<String> params) {
		if (params == null || params.size() == 0)
			return "";
		StringBuilder source = new StringBuilder("");
		for (String item : params) {
			source.append(cppType(item) + ",");
		}
		return source.substring(0, source.length() - 1);
	}

	public static String cppType(String typeName) {
		String cppType = typeName;
		if (typeName != GenerationNames.EventBaseRefName && typeName != GenerationNames.NoReturn) {
			if (typeName != null) {
				switch (typeName) {
				case "Integer":
					cppType = "int";
					break;
				case "Real":
					cppType = "double";
					break;
				case "Boolean":
					cppType = "bool";
					break;
				case "String":
					cppType = GenerationNames.cppString;
					break;
				case GenerationNames.EventPtr : 
					cppType = GenerationNames.EventPtr;
					break;
				default:
					cppType = GenerationNames.pointerType(typeName);
					break;
				}
			} else {
				cppType = "void";
			}
		}
		return cppType;
	}

	public static String entryExitTemplate(String typeName, String className, Map<String, String> states) {
		String source = GenerationNames.NoReturn + " " + className + "::" + typeName + "()\n{\n";
		if (states != null && !states.isEmpty()) {
			source += "switch(" + GenerationNames.CurrentStateName + ")\n{\n";
			for (Map.Entry<String, String> entry : states.entrySet()) {
				source += "case(" + GenerationNames.stateEnumName(entry.getKey()) + "):{" + entry.getValue()
						+ "();break;}\n";
			}
			source += "}\n";
		}
		return source + "}\n";
	}

	public static String setInitialState(String className, String initialState) {

		return GenerationNames.NoReturn + " " + className + "::" + GenerationNames.SetInitialStateName + "(){"
				+ GenerationNames.setStateFuncName + "(" + GenerationNames.stateEnumName(initialState) + ");}\n";
	}

	public static String subStateMachineClassFixPrivateParts(String parentClass) {
		return GenerationNames.pointerType(parentClass) + " " + GenerationNames.ParentSmMemberName + ";\n";
	}

	public static boolean stdType(String cppType) {
		if (cppType.contains("std::")) {
			return true;
		}
		return false;
	}

	public static String debugLogMessage(String className, String functionName) {
		return GenerationTemplates
				.debugOnlyCodeBlock("\tstd::cout << \"" + className + "::" + functionName + "\" << std::endl;\n");
	}

}
