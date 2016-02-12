package hu.elte.txtuml.export.cpp.templates;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.utils.Pair;

public class GenerationTemplates {

	public static final String EventHeader = GenerationNames.EventHeaderName + "." + GenerationNames.HeaderExtension;
	public static final String StateMachineBaseHeader = GenerationNames.StatemachineBaseHeaderName + "."
			+ GenerationNames.HeaderExtension;
	public static final String RuntimeHeader = RuntimeTemplates.RuntimeHeaderName;
	public static final String RuntimePath = RuntimeTemplates.RTPath;
	public static final String StandardIOinclude = GenerationNames.StandardIOinclude;
	public static final String RuntimeName = RuntimeTemplates.RuntimeIterfaceName;
	public static final String RuntimePointer = RuntimeTemplates.RuntimeIterfaceName + "*";
	public static final String RuntimeParamaterName = RuntimeTemplates.RuntimeParamter;

	public static final String InitSignal = GenerationNames.InitialEventName;

	public static StringBuilder eventBase(Options options) {
		StringBuilder eventBase = new StringBuilder("");

		if (options.isAddRuntime()) {
			eventBase.append(RuntimeTemplates.rtEventHeaderInclude()).append("\n");
		}

		eventBase.append(GenerationNames.ClassType + " " + GenerationNames.EventBaseName);
		if (options.isAddRuntime()) {
			eventBase.append(":" + RuntimeTemplates.EventIName);
		}
		eventBase.append("\n{\n" + GenerationNames.EventBaseName + "(");
		if (options.isAddRuntime()) {
			eventBase.append(RuntimeTemplates.SMParam + ",");
		}
		eventBase.append("int t_):");
		if (options.isAddRuntime()) {
			eventBase.append(RuntimeTemplates.EventIName + "("
					+ GenerationNames.formatIncomingParamName(RuntimeTemplates.SMRefName) + "),");
		}

		eventBase.append("t(t_){}\nint t;\n};\ntypedef const " + GenerationNames.EventBaseName + "& "
				+ GenerationNames.EventBaseRefName + ";\n\n");
		return eventBase;
	}

	public static StringBuilder eventClass(String className, List<Pair<String, String>> params, Options options) {
		StringBuilder source = new StringBuilder(
				GenerationNames.ClassType + " " + GenerationNames.eventClassName(className) + ":public "
						+ GenerationNames.EventBaseName + "\n{\n" + GenerationNames.eventClassName(className) + "(");
		if (options.isAddRuntime()) {
			source.append(RuntimeTemplates.SMParam + ",");
		}
		source.append("int t_");
		String paramList = PrivateFunctionalTemplates.paramList(params);
		if (paramList != "") {
			source.append("," + paramList);
		}
		source.append("):" + GenerationNames.EventBaseName + "(");
		if (options.isAddRuntime()) {
			source.append(GenerationNames.formatIncomingParamName(RuntimeTemplates.SMRefName) + ",");
		}
		source.append("t_)");
		StringBuilder body = new StringBuilder("{}\n");
		for (Pair<String, String> param : params) {
			source.append(
					"," + param.getSecond() + "(" + GenerationNames.formatIncomingParamName(param.getSecond()) + ")");
			body.append(PrivateFunctionalTemplates.cppType(param.getFirst()) + " " + param.getSecond() + ";\n");
		}
		source.append(body).append("};\n\n");
		body.setLength(0);
		return source;
	}

	// TODO works only with signal events! (Time,Change,.. not handled)
	public static String eventEnum(Set<SignalEvent> events) {
		StringBuilder EventList = new StringBuilder("enum Events {");
		EventList.append(GenerationNames.eventEnumName("InitSignal") + ",");

		if (events != null && !events.isEmpty()) {
			for (SignalEvent item : events) {
				EventList.append(GenerationNames.eventEnumName(item.getSignal().getName()) + ",");
			}
		}

		return EventList.substring(0, EventList.length() - 1) + "};\n";
	}

	public static String headerName(String className) {
		return className + "." + GenerationNames.HeaderExtension;
	}

	public static String sourceName(String className) {
		return className + "." + GenerationNames.SourceExtension;
	}

	public static String eventHeaderGuard(String source) {
		return headerGuard(source, GenerationNames.EventHeaderName);
	}

	public static String headerGuard(String source, String className) {
		return "#ifndef __" + className.toUpperCase() + "_" + GenerationNames.HeaderExtension.toUpperCase() + "__\n"
				+ "#define __" + className.toUpperCase() + "_" + GenerationNames.HeaderExtension.toUpperCase()
				+ "__\n\n" + source + "\n\n#endif //__" + className.toUpperCase() + "_"
				+ GenerationNames.HeaderExtension.toUpperCase() + "_";
	}

	public static String setState(String state) {
		return GenerationNames.setStateFuncName + "(" + GenerationNames.stateEnumName(state) + ");\n";
	}

	public static String hierarchicalSubStateMachineClassHeader(String dependency, String className, String parentclass,
			List<String> subMachines, String public_, String protected_, String private_) {
		List<String> parentParam = new LinkedList<String>();
		parentParam.add(parentclass);

		return hierarchicalStateMachineClassHeader(dependency, className, null, parentParam, subMachines, public_,
				protected_, (PrivateFunctionalTemplates.subStateMachineClassFixPrivateParts(parentclass) + private_),
				false);
	}

	public static String hierarchicalStateMachineClassHeader(String dependency, String className,
			List<String> subMachines, String public_, String protected_, String private_, Boolean rt) {
		return hierarchicalStateMachineClassHeader(dependency, className, null, null, subMachines, public_, protected_,
				private_, rt);
	}

	public static String hierarchicalStateMachineClassHeader(String dependency, String className, String baseClassName,
			List<String> constructorParams, List<String> subMachines, String public_, String protected_,
			String private_, Boolean rt) {
		return classHeader(PrivateFunctionalTemplates.classHeaderIncludes(rt) + dependency, className, baseClassName,
				constructorParams, PrivateFunctionalTemplates.stateMachineClassFixPublicParts(className, rt) + public_,
				protected_,
				PrivateFunctionalTemplates.hierarchicalStateMachineClassFixPrivateParts(className, subMachines)
						+ private_,
				true, rt);
	}

	public static String simpleSubStateMachineClassHeader(String dependency, String className, String parentclass,
			String public_, String protected_, String private_) {
		List<String> parentParam = new LinkedList<String>();
		parentParam.add(parentclass);

		return simpleStateMachineClassHeader(dependency, className, null, parentParam, public_, protected_,
				(PrivateFunctionalTemplates.subStateMachineClassFixPrivateParts(parentclass) + private_), false);
	}

	public static String simpleStateMachineClassHeader(String dependency, String className, String baseClassName,
			String public_, String protected_, String private_, Boolean rt) {
		return simpleStateMachineClassHeader(dependency, className, baseClassName, public_, protected_, private_, rt);
	}

	public static String simpleStateMachineClassHeader(String dependency, String className, String baseClassName,
			List<String> constructorParams, String public_, String protected_, String private_, Boolean rt) {
		return classHeader(PrivateFunctionalTemplates.classHeaderIncludes(rt) + dependency, className, baseClassName,
				constructorParams, PrivateFunctionalTemplates.stateMachineClassFixPublicParts(className, rt) + public_,
				protected_, PrivateFunctionalTemplates.simpleStateMachineClassFixPrivateParts(className) + private_,
				true, rt);
	}

	public static String classHeader(String dependency, String name, String baseClassName, String public_,
			String protected_, String private_) {
		return classHeader(dependency, name, baseClassName, null, public_, protected_, private_, false, false);
	}

	public static String classHeader(String dependency, String name, String baseClassName,
			List<String> constructorParams, String public_, String protected_, String private_, Boolean rt) {
		return classHeader(dependency, name, baseClassName, constructorParams, public_, protected_, private_, false,
				rt);
	}

	public static String classHeader(String dependency, String className, String baseClassName,
			List<String> constructorParams, String public_, String protected_, String private_, Boolean sm,
			Boolean rt) {
		String source = dependency;
		if (!sm) {
			source += PrivateFunctionalTemplates.localInclude(GenerationNames.EventHeaderName) + "\n";
		}

		source += GenerationNames.ClassType + " " + className;

		if (baseClassName != null) {
			source += ": public " + baseClassName;
		} else if (sm) {
			source += ":public " + GenerationNames.StatemachineBaseName;
			if (rt) {
				source += ",public " + RuntimeTemplates.STMIName;
			}
		}

		source += "\n{\npublic:\n" + className + "(" + PrivateFunctionalTemplates.paramTypeList(constructorParams)
				+ ");\n";

		if (!sm && baseClassName == null) {
			source += GenerationNames.DummyProcessEventDef;
		}

		if (!public_.isEmpty()) {
			source += public_;
		}
		if (!protected_.isEmpty()) {
			source += "\nprotected:\n" + protected_;
		}
		if (!private_.isEmpty()) {
			source += "\nprivate:\n" + private_;
		}

		return source + "\n};\n\n";
	}

	public static String paramName(String paramName) {
		return GenerationNames.formatIncomingParamName(paramName);
	}

	public static String variableDecl(String typeName, String variableName) {
		return PrivateFunctionalTemplates.cppType(typeName) + " " + variableName + ";\n";
	}

	public static String manyMultiplicityDependecy() {
		return PrivateFunctionalTemplates.outerInclude(GenerationNames.AssocMultiplicityDataStruct);
	}

	public static String variableDecl(String typeName, String variableName, Integer multiplicity) {
		if (multiplicity > 1) {
			return GenerationNames.AssocMultiplicityDataStruct + "<" + PrivateFunctionalTemplates.cppType(typeName)
					+ ">" + " " + variableName + ";\n";
		}
		return variableDecl(typeName, variableName);
	}

	// TODO too simple....
	public static String constructorDef(String className, String baseClassName, Boolean rt) {
		if (baseClassName == null) {
			return className + "::" + className + "(){}\n\n";
		} else if (baseClassName != null && rt) {
			return className + "::" + className + "(" + RuntimePointer + " " + RuntimeParamaterName + "): "
					+ baseClassName + "(" + RuntimeParamaterName + ") {}\n\n";
		} else {
			return className + "::" + className + "() :" + baseClassName + "() {}\n\n";
		}

	}

	public static String transitionActionDecl(String transitionActionName) {
		List<String> params = new LinkedList<String>();
		params.add(GenerationNames.EventBaseRefName);

		return functionDecl(transitionActionName, params);
	}

	public static String transitionActionDef(String className, String transitionActionName, String body) {
		List<Pair<String, String>> params = new LinkedList<Pair<String, String>>();
		params.add(new Pair<String, String>(GenerationNames.EventBaseRefName, GenerationNames.EventParamName));

		return functionDef(className, transitionActionName, params,
				PrivateFunctionalTemplates.debugLogMessage(className, transitionActionName) + body);
	}

	public static String functionDecl(String functionName) {
		return functionDecl(functionName, null);
	}

	public static String functionDecl(String functionName, List<String> params) {
		return functionDecl(GenerationNames.NoReturn, functionName, params);
	}

	// TODO modifiers
	public static String functionDecl(String returnTypeName, String functionName, List<String> params) {
		return PrivateFunctionalTemplates.cppType(returnTypeName) + " " + functionName + "("
				+ PrivateFunctionalTemplates.paramTypeList(params) + ");\n";
	}

	public static String functionDef(String className, String functionName, String body) {
		return functionDef(className, GenerationNames.NoReturn, functionName, body);
	}

	public static String functionDef(String className, String returnTypeName, String functionName, String body) {
		return functionDef(className, returnTypeName, functionName, null, body);
	}

	public static String functionDef(String className, String functionName, List<Pair<String, String>> params,
			String body) {
		return functionDef(className, GenerationNames.NoReturn, functionName, params, body);
	}

	// TODO modifiers
	public static String functionDef(String className, String returnTypeName, String functionName,
			List<Pair<String, String>> params, String body) {
		return PrivateFunctionalTemplates.cppType(returnTypeName) + " " + className + "::" + functionName + "("
				+ PrivateFunctionalTemplates.paramList(params) + ")\n{\n" + body + "}\n\n";
	}

	public static String hierarchicalSubStateMachineClassConstructor(String className, String parentClassName,
			Multimap<Pair<String, String>, Pair<String, String>> machine, Map<String, String> subMachines,
			String intialState) {

		String parentParamName = GenerationNames.formatIncomingParamName(GenerationNames.ParentSmName);
		String source = className + "::" + className + "(" + GenerationNames.pointerType(parentClassName) + " "
				+ parentParamName + "):" + GenerationNames.DefaultStateInitialization + ","
				+ GenerationNames.CurrentMachineName + "(" + GenerationNames.NullPtr + "),"
				+ GenerationNames.ParentSmMemberName + "(" + parentParamName + ")" + "\n{\n";
		return source + PrivateFunctionalTemplates.hierarchicalStateMachineClassConstructorSharedBody(className,
				parentParamName, machine, subMachines, intialState, false);
	}

	public static StringBuilder hierarchicalStateMachineClassConstructor(String className, String baseClassName,
			Multimap<Pair<String, String>, Pair<String, String>> machine, Map<String, String> subMachines,
			String intialState, Boolean rt) {

		StringBuilder source = new StringBuilder(simpleStateMachineClassConstructorHead(className, baseClassName, rt)
				+ GenerationNames.CurrentMachineName + "(" + GenerationNames.NullPtr + ")" + ","
				+ GenerationNames.DefaultStateInitialization + "\n{\n");
		source.append(PrivateFunctionalTemplates.hierarchicalStateMachineClassConstructorSharedBody(className, "this",
				machine, subMachines, intialState, rt));
		return source;
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */
	public static String simpleSubStateMachineClassConstructor(String className, String parentClass,
			Multimap<Pair<String, String>, Pair<String, String>> machine, String intialState) {
		String parentParam = GenerationNames.formatIncomingParamName(GenerationNames.ParentSmName);
		String source = className + "::" + className + "(" + GenerationNames.pointerType(parentClass) + " "
				+ parentParam + "):" + GenerationNames.DefaultStateInitialization + ","
				+ GenerationNames.ParentSmMemberName + "(" + parentParam + ")" + "\n{\n"
				+ PrivateFunctionalTemplates.stateMachineClassConstructorSharedBody(className, parentClass, machine,
						intialState, false, null)
				+ "}\n\n";
		return source + PrivateFunctionalTemplates.simpleStateMachineClassConstructorSharedBody(className, machine,
				intialState, false);
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */
	public static String simpleStateMachineClassConstructor(String className, String baseClassName,
			Multimap<Pair<String, String>, Pair<String, String>> machine, String intialState, Boolean rt,
			Integer poolId) {
		String source = simpleStateMachineClassConstructorHead(className, baseClassName, rt)
				+ GenerationNames.DefaultStateInitialization + "\n{\n" + PrivateFunctionalTemplates
						.stateMachineClassConstructorSharedBody(className, machine, intialState, rt, poolId)
				+ "}\n\n";
		return source + PrivateFunctionalTemplates.simpleStateMachineClassConstructorSharedBody(className, machine,
				intialState, rt);

	}

	public static String simpleStateMachineClassConstructorHead(String className, Boolean rt) {
		if (rt) {
			return className + "::" + className + "(" + RuntimePointer + " " + RuntimeTemplates.RuntimeParamter + "):";

		} else {
			return className + "::" + className + "():";

		}
	}

	public static String simpleStateMachineClassConstructorHead(String className, String baseClassName, Boolean rt) {
		if (rt) {
			if (baseClassName != null) {
				return className + "::" + className + "(" + RuntimePointer + " " + RuntimeTemplates.RuntimeParamter
						+ "): " + baseClassName + "(" + RuntimeTemplates.RuntimeParamter + "),";
			} else {
				return className + "::" + className + "(" + RuntimePointer + " " + RuntimeTemplates.RuntimeParamter
						+ "):";
			}

		} else {
			if (baseClassName != null) {
				return className + "::" + className + "(): " + baseClassName + "(" + RuntimeTemplates.RuntimeParamter
						+ "),";
			} else {
				return className + "::" + className + "():";
			}
		}
	}

	public static String guardFunction(String guardFunctionName, String constraint, String eventName) {
		String source = "bool " + guardFunctionName + "(" + GenerationNames.EventBaseRefName;
		if (eventName != null && !eventName.isEmpty() && constraint.contains(eventName)) {
			source += " " + GenerationNames.EventFParamName + ")\n{" + getRealEvent(eventName);
			constraint = eventParamUsage(eventName, constraint);
		} else {
			source += "){";
		}

		return source + "return " + constraint + ";}\n";
	}

	/*
	 * Map<String,String> state ,actionName
	 */
	public static String entry(String className, Map<String, String> states) {
		return PrivateFunctionalTemplates.entryExitTemplate(GenerationNames.EntryName, className, states);
	}

	/*
	 * Map<String,String> state ,action
	 */
	public static String exit(String className, Map<String, String> states) {
		return PrivateFunctionalTemplates.entryExitTemplate(GenerationNames.ExitName, className, states);
	}

	public static String stateEnum(Iterable<State> states, String initialState) {
		StringBuilder StateList = new StringBuilder("enum States {");

		StateList.append(GenerationNames.stateEnumName(initialState) + ",");
		for (State item : states) {
			StateList.append(GenerationNames.stateEnumName(item.getName()) + ",");
		}
		return StateList.substring(0, StateList.length() - 1) + "};\n";
	}

	public static String forwardDeclaration(String className) {
		String source = "";
		String cppType = PrivateFunctionalTemplates.cppType(className);
		if (PrivateFunctionalTemplates.stdType(cppType)) {
			source = PrivateFunctionalTemplates.include(cppType);
		} else {
			source = GenerationNames.ClassType + " " + className + ";\n";
		}
		return source;
	}

	public static String getRealEvent(String eventName) {
		return "const " + eventName + GenerationNames.EventClassTypeId + "& " + GenerationNames.RealEventName
				+ "=static_cast<const " + eventName + GenerationNames.EventClassTypeId + "&>("
				+ GenerationNames.EventFParamName + ");\n";
	}

	public static String cppInclude(String className) {
		return PrivateFunctionalTemplates.include(className);
	}

	public static String formatSubSmFunctions(String source) {
		return source.replaceAll(ActivityTemplates.Self, GenerationNames.ParentSmMemberName);
	}

	public static String eventParamUsage(String eventName, String body) {
		return body.replaceAll((eventName + "\\" + GenerationNames.SimpleAccess),
				(GenerationNames.RealEventName + GenerationNames.SimpleAccess));
	}

	public static String createObject(String typeName, String objName) {
		String source;
		source = GenerationNames.pointerType(typeName) + " " + objName + "= " + GenerationNames.MemoryAllocator + " "
				+ typeName + "();\n";
		return source;

	}

	public static String allocateObject(String typeName, List<String> params) {
		String parameters = "(";
		for (int i = 0; i < params.size() - 1; i++) {
			parameters = parameters + params.get(i) + ",";
		}
		parameters = parameters + params.get(params.size() - 1) + ")";

		return GenerationNames.MemoryAllocator + " " + typeName + parameters;
	}

	public static String allocateObject(String typeName) {
		return GenerationNames.MemoryAllocator + " " + typeName + "()";
	}

	public static String getDefaultReturn(String returnType) {

		if (returnType == null) {
			return "\n";
		} else {
			return "return " + getDefaultReturnValue(returnType) + ";\n";
		}

	}

	public static String getDefaultReturnValue(String returnType) {

		switch (PrivateFunctionalTemplates.cppType(returnType)) {
		case "int":
			return "0";
		case "double":
			return "0";
		case "bool":
			return "true";
		case GenerationNames.cppString:
			return "\"\"";
		default:
			return "0";

		}

	}

	public static String emptyBody() {
		return "{}";
	}

	public static String debugOnlyCodeBlock(String code_) {
		return "#ifndef " + GenerationNames.NoDebugSymbol + "\n" + code_ + "#endif\n";
	}
}
