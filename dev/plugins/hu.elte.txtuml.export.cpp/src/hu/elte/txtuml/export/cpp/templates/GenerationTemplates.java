package hu.elte.txtuml.export.cpp.templates;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.Property;
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
	public static final String MyRuntimeName = RuntimeTemplates.UsingRuntime;
	public static final String InitStateMachineProcedureName = GenerationNames.InitStateMachine;
	public static final String StandardFunctionsHeader = GenerationNames.StandardLibaryFunctionsHeaderName;
	public static final String AssocationHeader = GenerationNames.AssocationHeaderName;
	public static final String AssociationsStructuresHreaderName = GenerationNames.AssociationsHeaderName;
	public static final String AssociationStructuresHeader = GenerationNames.AssociationsHeaderName + "."
			+ GenerationNames.HeaderExtension;
	public static final String AssociationStructuresSource = GenerationNames.AssociationsHeaderName + "."
			+ GenerationNames.SourceExtension;
	public static final String DeploymentHeader = GenerationNames.DeploymentHeaderName;
	public static final String TimerInterfaceHeader = GenerationNames.TimerInterFaceName.toLowerCase();
	public static final String TimerHeader = GenerationNames.TimerClassName.toLowerCase();

	public static final String InitSignal = GenerationNames.InitialEventName;

	public enum LinkFunctionType {
		Link, Unlink
	};

	public static StringBuilder eventBase(Options options) {
		StringBuilder eventBase = new StringBuilder("");

		eventBase.append(RuntimeTemplates.rtEventHeaderInclude()).append("\n");

		eventBase.append(GenerationNames.ClassType + " " + GenerationNames.EventBaseName);
		if (options.isAddRuntime()) {
			eventBase.append(":" + RuntimeTemplates.EventIName);
		}
		eventBase.append("\n{\n" + GenerationNames.EventBaseName + "(");
		eventBase.append("int t_):");

		eventBase.append("t(t_){}\nint t;\n};\ntypedef const " + GenerationNames.EventBaseName + "& "
				+ GenerationNames.EventBaseRefName + ";\n\n");
		return eventBase;
	}

	public static StringBuilder eventClass(String className, List<Pair<String, String>> params, String constructorBody,
			List<Property> properites, Options options) {
		StringBuilder source = new StringBuilder(
				GenerationNames.ClassType + " " + GenerationNames.eventClassName(className) + ":public "
						+ GenerationNames.EventBaseName + "\n{\n" + GenerationNames.eventClassName(className) + "(");
		String paramList = PrivateFunctionalTemplates.paramList(params);
		if (paramList != "") {
			source.append(paramList);
		}
		source.append("):" + GenerationNames.EventBaseName + "(");
		source.append(className + "_EE)");
		StringBuilder body = new StringBuilder("\n{\n" + constructorBody + "}\n");

		for (Property property : properites) {
			body.append(variableDecl(property.getType().getName(), property.getName(), false));
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

	public static String linkSourceName(String className) {
		return className + "-" + GenerationNames.LinkAddition + "." + GenerationNames.SourceExtension;
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

	public static StringBuilder hierarchicalStateMachineClassHeader(String dependency, String className,
			List<String> subMachines, String public_, String protected_, String private_, Boolean rt) {
		return hierarchicalStateMachineClassHeader(dependency, className, null, subMachines, public_, protected_,
				private_, rt);
	}

	public static StringBuilder hierarchicalSubStateMachineClassHeader(String dependency, String className,
			String parentClass, List<String> subMachines, String public_, String protected_, String private_) {
		List<String> parentParam = new LinkedList<String>();
		parentParam.add(parentClass);

		return hierarchicalStateMachineClassHeader(dependency, className, null, public_, protected_,
				GenerationTemplates.variableDecl(parentClass, GenerationNames.ParentSmMemberName, false) + (private_),
				false);
	}

	public static StringBuilder hierarchicalStateMachineClassHeader(String dependency, String className,
			String baseClassName, List<String> subMachines, String public_, String protected_, String private_,
			Boolean rt) {
		return classHeader(PrivateFunctionalTemplates.classHeaderIncludes(rt) + dependency, className, baseClassName,
				PrivateFunctionalTemplates.stateMachineClassFixPublicParts(className, rt) + public_, protected_,
				PrivateFunctionalTemplates.hierarchicalStateMachineClassFixPrivateParts(className, subMachines)
						+ private_,
				true, rt);
	}

	public static StringBuilder simpleSubStateMachineClassHeader(String dependency, String className,
			String parentClass, String public_, String protected_, String private_) {

		return simpleStateMachineClassHeader(dependency, className, null, parentClass, public_, protected_,
				GenerationTemplates.variableDecl(parentClass, GenerationNames.ParentSmMemberName, false) + (private_),
				false);
	}

	public static StringBuilder simpleStateMachineClassHeader(String dependency, String className, String baseClassName,
			String parentClass, String public_, String protected_, String private_, Boolean rt) {
		return classHeader(PrivateFunctionalTemplates.classHeaderIncludes(rt) + dependency, className, baseClassName,
				PrivateFunctionalTemplates.stateMachineClassFixPublicParts(className, rt) + public_, protected_,
				PrivateFunctionalTemplates.simpleStateMachineClassFixPrivateParts(className) + private_, true, rt);
	}

	public static StringBuilder classHeader(String dependency, String name, String baseClassName, String public_,
			String protected_, String private_) {
		return classHeader(dependency, name, baseClassName, public_, protected_, private_, false, false);
	}

	public static StringBuilder classHeader(String dependency, String name, String baseClassName, String public_,
			String protected_, String private_, Boolean rt) {
		return classHeader(dependency, name, baseClassName, public_, protected_, private_, false, rt);
	}

	public static StringBuilder classHeader(String dependency, String className, String baseClassName, String public_,
			String protected_, String private_, Boolean sm, Boolean rt) {
		StringBuilder source = new StringBuilder(dependency);
		if (!sm) {
			source.append(PrivateFunctionalTemplates.localInclude(GenerationNames.EventHeaderName) + "\n");
		}
		source.append(GenerationNames.ClassType + " " + className);
		if (baseClassName != null) {
			source.append(": public " + baseClassName);
		} else if (sm) {
			source.append(":public " + GenerationNames.StatemachineBaseName);
			if (rt) {
				source.append(",public " + RuntimeTemplates.STMIName);
			}

		}
		source.append("\n{\n");
		if (!sm && baseClassName == null) {
			source.append(GenerationNames.DummyProcessEventDef);
		}

		if (!public_.isEmpty()) {
			source.append(public_);
		}
		if (!protected_.isEmpty()) {
			source.append("\nprotected:\n" + protected_);
		}
		source.append("\nprivate:\n");
		source.append(functionDecl(GenerationNames.InitStateMachine) + "\n");
		if (!private_.isEmpty()) {
			source.append(private_);

		}
		source.append("\n};\n\n");

		return source;
	}
	
	public static String dataType(String datatTypeName, String attributes) {
		return GenerationNames.DataType + " " + datatTypeName + "\n" +
				"{\n" + attributes + "}";
	}

	public static String paramName(String paramName) {
		return GenerationNames.formatIncomingParamName(paramName);
	}

	public static String eventParamName() {
		return GenerationNames.formatIncomingParamName(GenerationNames.EventParamName);
	}

	public static String variableDecl(String typeName, String variableName, boolean isSignal) {
		String generatedType = isSignal ? GenerationNames.signalType(typeName)
				: PrivateFunctionalTemplates.cppType(typeName);
		return generatedType + " " + variableName + ";\n";
	}

	public static String manyMultiplicityDependecy() {
		return PrivateFunctionalTemplates.outerInclude(GenerationNames.Collection);
	}

	public static String variableDecl(String typeName, String variableName, Integer multiplicity) {
		if (multiplicity > 1) {
			return GenerationNames.AssocMultiplicityDataStruct + "<" + PrivateFunctionalTemplates.cppType(typeName)
					+ ">" + " " + variableName + ";\n";
		}
		return variableDecl(typeName, variableName, false);
	}

	public static String assocationDecl(String className, String roleName, Integer lower, Integer upper) {
		return GenerationNames.AssocMultiplicityDataStruct + "<" + className + ">" + " " + roleName + " "
				+ GenerationNames.AssigmentOperator + " " + GenerationNames.AssocMultiplicityDataStruct + "<"
				+ className + ">" + "(" + lower + "," + upper +  ");\n";
	}

	public static StringBuilder constructorDef(String className, String baseClassName, String body,
			List<Pair<String, String>> params, List<Pair<String, String>> baseParams, Boolean stateMachine) {
		StringBuilder source = new StringBuilder("");
		source.append(className + "::" + className + "(");
		source.append(PrivateFunctionalTemplates.paramList(params) + ")");
		if (baseClassName != null) {
			source.append(":" + baseClassName + "(" + PrivateFunctionalTemplates.paramList(baseParams) + ")");
		}

		source.append("\n{\n" + body + "\n");
		if (stateMachine) {
			source.append(GenerationNames.InitStateMachine + "();\n");
		}
		source.append("}\n");

		return source;

	}

	public static StringBuilder constructorDef(String className, Boolean stateMachine) {
		return constructorDef(className, null, "", null, null, stateMachine);
	}

	public static String destructorDef(String className, Boolean ownStates) {
		if (!ownStates) {
			return className + "::" + "~" + className + "(){}\n";
		} else {
			return className + "::" + "~" + className + "()\n{\n" + RuntimeTemplates.GetRuntimeInstance
					+ GenerationNames.PointerAccess + RuntimeTemplates.ObjectRemoverForRuntime + "("
					+ GenerationNames.Self + ");\n}\n\n";
		}

	}

	public static String transitionActionDecl(String transitionActionName) {
		List<String> params = new LinkedList<String>();
		params.add(GenerationNames.EventBaseRefName);

		return functionDecl(transitionActionName, params);
	}

	public static String transitionActionDef(String className, String transitionActionName, String body,
			boolean singalAcces) {
		List<Pair<String, String>> params = new LinkedList<Pair<String, String>>();
		if (singalAcces) {
			params.add(new Pair<String, String>(GenerationNames.EventBaseRefName, GenerationNames.EventParamName));
		} else {
			params.add(new Pair<String, String>(GenerationNames.EventBaseRefName, ""));

		}

		return functionDef(className, transitionActionName, params,
				PrivateFunctionalTemplates.debugLogMessage(className, transitionActionName) + body);
	}

	public static String functionDecl(String functionName) {
		return functionDecl(functionName, null);
	}

	public static String functionDecl(String functionName, List<String> params) {
		return functionDecl(GenerationNames.NoReturn, functionName, params);
	}

	public static String simpleFunctionDecl(String returnType, String functionName) {
		return PrivateFunctionalTemplates.cppType(returnType) + " " + functionName + "()";
	}

	// TODO modifiers
	public static String functionDecl(String returnTypeName, String functionName, List<String> params) {
		return PrivateFunctionalTemplates.cppType(returnTypeName) + " " + functionName + "("
				+ PrivateFunctionalTemplates.paramTypeList(params) + ");\n";
	}

	public static String constructorDecl(String className, List<String> params) {
		StringBuilder source = new StringBuilder("");
		source.append(className + "(");
		source.append(PrivateFunctionalTemplates.paramTypeList(params) + ");\n");

		return source.toString();
	}
	
	public static String defaultConstructorDecl(String className) {
		return constructorDecl(className,null);
	}
	
	public static String destructorDecl(String className) {
		return "~" + className + "();\n";
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

	public static String simpleFunctionDef(String returnType, String functionName, String body, String returnVariable) {
		return simpleFunctionDecl(returnType, functionName) + " {\n" + body + "\n" + "return " + returnVariable
				+ ";\n}";
	}

	public static StringBuilder templateLinkFunctionGeneralDef(LinkFunctionType linkFunction) {

		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.TemplateDecl + "<" + GenerationNames.TemplateType + " "
				+ GenerationNames.TemplateParameterName + "," + GenerationNames.TemplateType + " "
				+ GenerationNames.EndPointName + ">\n");
		source.append(GenerationNames.NoReturn + " " + getLinkFunctionName(linkFunction));
		source.append("(" + GenerationNames.TemplateType + " "
				+ PrivateFunctionalTemplates.cppType(GenerationNames.EndPointName + "::" + GenerationNames.EdgeType)
				+ " " + ") {}\n");

		return source;
	}

	public static StringBuilder linkTemplateSpecializationDecl(String className, String otherClassName,
			String otherEndPointName, String assocName, LinkFunctionType linkFunction) {
		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.TemplateDecl + "<>\n");
		source.append(GenerationNames.NoReturn + " " + className + "::" + getLinkFunctionName(linkFunction));
		source.append("<" + assocName + "," + GenerationNames.TemplateType + " " + assocName + "::" + otherEndPointName
				+ ">");
		source.append("(" + PrivateFunctionalTemplates.cppType(otherClassName) + ");\n");

		return source;
	}

	public static StringBuilder linkTemplateSpecializationDef(String className, String otherClassName, String assocName,
			String roleName, boolean isNavigable, LinkFunctionType linkFunction) {
		StringBuilder source = new StringBuilder("");
		if (isNavigable) {
			source.append(GenerationNames.TemplateDecl + "<>\n");
			source.append(GenerationNames.NoReturn + " " + className + "::" + getLinkFunctionName(linkFunction));
			source.append(
					"<" + assocName + "," + GenerationNames.TemplateType + " " + assocName + "::" + roleName + ">");
			source.append("(" + PrivateFunctionalTemplates.cppType(otherClassName) + " "
					+ GenerationNames.AssocParameterName + ")\n");
			source.append("{\n" + formatAssociationRoleName(assocName, roleName) + GenerationNames.SimpleAccess
					+ getAddOrRemoveAssoc(linkFunction) + "(" + GenerationNames.AssocParameterName + ");\n}\n");
		}

		return source;
	}

	public static String getLinkFunctionName(LinkFunctionType linkFunction) {
		if (linkFunction == LinkFunctionType.Link)
			return GenerationNames.LinkActionName;
		else if (linkFunction == LinkFunctionType.Unlink)
			return GenerationNames.UnLinkActionName;

		return "";
	}

	public static String getAddOrRemoveAssoc(LinkFunctionType linkFunction) {
		if (linkFunction == LinkFunctionType.Link)
			return GenerationNames.AddAssocToAssocationFunctionName;
		else if (linkFunction == LinkFunctionType.Unlink)
			return GenerationNames.RemoveAssocToAssocationFunctionName;

		return "";
	}

	public static String hierarchicalSubStateMachineClassConstructor(String className, String parentClassName,
			Multimap<Pair<String, String>, Pair<String, String>> machine, Map<String, String> subMachines,
			String intialState) {

		String parentParamName = GenerationNames.formatIncomingParamName(GenerationNames.ParentSmName);
		String source = className + "::" + className + "()" + " " + parentParamName + "):"
				+ GenerationNames.DefaultStateInitialization + "," + GenerationNames.CurrentMachineName + "("
				+ GenerationNames.NullPtr + ")," + GenerationNames.ParentSmMemberName + "(" + parentParamName + ")"
				+ "\n{\n";
		return source + PrivateFunctionalTemplates.hierarchicalStateMachineClassConstructorSharedBody(className,
				parentClassName, machine, subMachines, intialState, false);
	}

	public static StringBuilder hierarchicalStateMachineClassConstructor(String className, String parentClassName,
			String baseClassName, Multimap<Pair<String, String>, Pair<String, String>> machine,
			Map<String, String> subMachines, String intialState, Boolean rt) {

		StringBuilder source = new StringBuilder(simpleStateMachineClassConstructorHead(className, baseClassName)
				+ GenerationNames.CurrentMachineName + "(" + GenerationNames.NullPtr + ")" + ","
				+ GenerationNames.DefaultStateInitialization + "\n{\n");
		source.append(PrivateFunctionalTemplates.hierarchicalStateMachineClassConstructorSharedBody(className,
				parentClassName, machine, subMachines, intialState, rt));
		return source;
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */
	public static String simpleSubStateMachineClassConstructor(String className, String parentStateMachine,
			Multimap<Pair<String, String>, Pair<String, String>> machine, String intialState) {
		String parentParamName = GenerationNames.formatIncomingParamName(GenerationNames.ParentSmName);
		String source = className + "::" + className + "(" + PrivateFunctionalTemplates.cppType(parentStateMachine)
				+ " " + parentParamName + "):" + GenerationNames.DefaultStateInitialization + ","
				+ GenerationNames.ParentSmMemberName + "(" + parentParamName + ")" + "\n{\n"
				+ PrivateFunctionalTemplates.stateMachineClassConstructorSharedBody(className, parentStateMachine,
						machine, intialState, false, null)
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
		String source = simpleStateMachineClassConstructorHead(className, baseClassName)
				+ GenerationNames.DefaultStateInitialization + "\n{\n" + PrivateFunctionalTemplates
						.stateMachineClassConstructorSharedBody(className, className, machine, intialState, rt, poolId)
				+ "}\n\n";
		return source + PrivateFunctionalTemplates.simpleStateMachineClassConstructorSharedBody(className, machine,
				intialState, rt);

	}

	public static String simpleStateMachineInitialization(String className, String intialState, Boolean rt,
			Integer poolId, Multimap<Pair<String, String>, Pair<String, String>> machine) {
		StringBuilder body = new StringBuilder("");
		body.append(PrivateFunctionalTemplates.stateMachineClassConstructorSharedBody(className, className, machine,
				intialState, rt, poolId));

		return functionDef(className, GenerationNames.InitStateMachine, body.toString());
	}

	public static String hierachialStateMachineInitialization(String className, String intialState, Boolean rt,
			Integer poolId, Multimap<Pair<String, String>, Pair<String, String>> machine,
			Map<String, String> subMachines) {
		StringBuilder body = new StringBuilder("");
		body.append(GenerationNames.CurrentMachineName + " = " + GenerationNames.NullPtr + ";\n");
		body.append(PrivateFunctionalTemplates.hierarchicalStateMachineClassConstructorSharedBody(className, className,
				machine, subMachines, intialState, rt));
		return functionDef(className, GenerationNames.InitStateMachine, body.toString());
	}

	public static String simpleStateMachineFixFunctionDefnitions(String className, String initialState, Boolean subM) {
		StringBuilder source = new StringBuilder("");

		source.append(GenerationNames.simpleProcessEventDef(className));
		if (!subM) {
			source.append(RuntimeTemplates.rtFunctionDef(className));
		}

		source.append(GenerationNames.simpleSetStateDef(className) + "\n");
		source.append(PrivateFunctionalTemplates.setInitialState(className, initialState));

		return source.toString();
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
						+ "\n" + PrivateFunctionalTemplates.setInitialState(className, intialState) + "\n");

		return source.toString();
	}

	public static String simpleStateMachineClassConstructorHead(String className) {

		return className + "::" + className + "():";

	}

	public static String simpleStateMachineClassConstructorHead(String className, String baseClassName) {

		if (baseClassName != null) {
			return className + "::" + className + "(): " + baseClassName + "(),";
		} else {
			return className + "::" + className + "():";
		}
	}

	public static String guardDefinition(String guardFunctionName,String constraint,String className, boolean eventParamUsage) {
		StringBuilder source = new StringBuilder("bool " + className + "::" + guardFunctionName + "(" + GenerationNames.EventBaseRefName);
		if (eventParamUsage) {
			source.append(" " + GenerationNames.EventFParamName);
			
		}
		source.append(")\n{\n");

		return source + constraint + "}\n";
	}

	public static String guardDecleration(String guardFunctionName) {
		StringBuilder source = new StringBuilder(
				"bool " + guardFunctionName + "(" + GenerationNames.EventBaseRefName + ");\n");
		return source.toString();
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
		String cppType = PrivateFunctionalTemplates.cppType(className);
		if(PrivateFunctionalTemplates.stdType(cppType)) {
			return PrivateFunctionalTemplates.include(cppType);
		}
		return PrivateFunctionalTemplates.include(className);
	}

	public static String putNamespace(String source, String namespace) {
		return "namespace " + namespace + "\n{\n" + source + "\n}\n";
	}

	public static String formatSubSmFunctions(String source) {
		return source.replaceAll(ActivityTemplates.Self, GenerationNames.ParentSmMemberName);
	}

	public static String createObject(String typeName, String objName) {
		return createObject(typeName, objName, null, null);
	}

	public static String createObject(String typeName, String objName, List<String> params) {
		return createObject(typeName, objName, null, params);
	}

	public static String createObject(String typeName, String objName, List<String> templateParams,
			List<String> params) {
		String templateParameters = "";
		if (templateParams != null) {
			templateParameters = "<";
			for (int i = 0; i < templateParams.size() - 1; i++) {
				templateParameters = templateParameters + templateParams.get(i) + ",";
			}
			templateParameters = templateParameters + templateParams.get(templateParams.size() - 1) + ">";
		}

		return GenerationNames.pointerType(typeName) + " " + objName + templateParameters + " = "
				+ allocateObject(typeName, templateParams, params) + ";\n";

	}

	public static String allocateObject(String typeName, List<String> templateParams, List<String> params) {

		String parameters = "(";
		if (params != null && params.size() > 0) {

			for (int i = 0; i < params.size() - 1; i++) {
				parameters = parameters + params.get(i) + ",";
			}
			parameters = parameters + params.get(params.size() - 1);
		}
		parameters = parameters + ")";

		String templateParameters = "";
		if (templateParams != null) {
			templateParameters = "<";
			for (int i = 0; i < templateParams.size() - 1; i++) {
				templateParameters = templateParameters + templateParams.get(i) + ",";
			}
			templateParameters = templateParameters + templateParams.get(templateParams.size() - 1) + ">";
		}
		return GenerationNames.MemoryAllocator + " " + typeName + templateParameters + parameters;

	}

	public static String allocateObject(String typeName, List<String> params) {
		return allocateObject(typeName, null, params);
	}

	public static String allocateObject(String typeName) {
		return allocateObject(typeName, null, null);
	}

	public static String staticCreate(String typeName, String objName, String creatorMethod) {
		return GenerationNames.pointerType(typeName) + " " + objName + " = " + typeName + "::" + creatorMethod
				+ "();\n";
	}

	public static String debugOnlyCodeBlock(String code_) {
		return "#ifndef " + GenerationNames.NoDebugSymbol + "\n" + code_ + "#endif\n";
	}

	public static String formatAssociationRoleName(String associationName, String role) {
		return associationName + "_" + role;
	}

	public static String usingTemplateType(String usedName, String typeName, List<String> templateParams) {
		String templateParameters = "<";
		templateParameters = "<";
		for (int i = 0; i < templateParams.size() - 1; i++) {
			templateParameters = templateParameters + templateParams.get(i) + ",";
		}
		templateParameters = templateParameters + templateParams.get(templateParams.size() - 1) + ">";

		return "using " + usedName + " = " + typeName + templateParameters + ";\n";

	}

	public static String createAssociationStructure(String associationName, String E1, String E2, String endPoint1,
			String endPoint2) {
		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.ClassType + " " + associationName);
		source.append(" : public " + GenerationNames.AssociationClassName);
		source.append("<" + E1 + "," + E2 + ">{\n");
		source.append(createEndPointClass(E1, endPoint1));
		source.append(createEndPointClass(E2, endPoint2));
		source.append("};\n");
		return source.toString();
	}

	public static String createEndPointClass(String classType, String endPointName) {
		return GenerationNames.ClassType + " " + endPointName + "{typedef " + classType + " " + GenerationNames.EdgeType
				+ ";};\n";
	}

}
