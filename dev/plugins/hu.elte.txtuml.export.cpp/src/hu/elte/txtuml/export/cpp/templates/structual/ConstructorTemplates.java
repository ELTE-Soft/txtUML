package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.CppExporterUtils.TypeDescriptor;
import hu.elte.txtuml.export.cpp.statemachine.TransitionConditions;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HierarchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.utils.Pair;

public class ConstructorTemplates {

	public static String initDecl(String className, List<TypeDescriptor> params) {
		StringBuilder source = new StringBuilder("");

		source.append(ModifierNames.NoReturn + " ");
		source.append(GenerationNames.initFunctionName(className));
		source.append("(" + PrivateFunctionalTemplates.paramTypeList(params) + ");\n");

		return source.toString();
	}

	public static String constructorDecl(String className, List<TypeDescriptor> params) {
		return className + "(" + PrivateFunctionalTemplates.paramTypeList(params) + ");\n";

	}

	public static String defaultConstructorDecl(String className) {
		return constructorDecl(className, null);
	}

	public static String destructorDecl(String className) {
		return "~" + className + "();\n";
	}

	public static String destructorDef(String className) {
		return className + "::" + "~" + className + "(){}\n";

	}

	public static String initDef(String className, String body, List<Pair<TypeDescriptor, String>> params,
			Boolean stateMachine) {
		StringBuilder source = new StringBuilder("");
		source.append(ModifierNames.NoReturn + " ");
		source.append(className + "::" + GenerationNames.initFunctionName(className) + "(");
		source.append(PrivateFunctionalTemplates.paramList(params) + ")" + "\n{\n");
		source.append(GenerationNames.InitializerFixFunctionNames.InitPorts + "();\n");
		if (stateMachine) {
			source.append(GenerationNames.InitializerFixFunctionNames.InitStateMachine + "();\n");
		}
		source.append(body + "\n" + "}\n");

		return source.toString();
	}

	public static String constructorDef(String className, List<String> paramNames,
			List<Pair<TypeDescriptor, String>> params) {

		return className + "::" + className + "(" + PrivateFunctionalTemplates.paramList(params) + ")" + "{"
				+ GenerationNames.initFunctionName(className) + "("
				+ PrivateFunctionalTemplates.paramNameList(paramNames) + ");}\n";

	}
	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */

	public static String subStateMachineClassConstructor(String className, String parentClassName,
			Multimap<TransitionConditions, Pair<String, String>> machine,
			Optional<Map<String, String>> optionalSubMachine) {
		if (!optionalSubMachine.isPresent()) {
			return subStateMachineSharedConstructor(className, parentClassName, machine,
					StateMachineTemplates.stateMachineInitializationSharedBody(false, null));
		} else {
			Map<String, String> subMachines = optionalSubMachine.get();
			return subStateMachineSharedConstructor(className, parentClassName, machine,
					ActivityTemplates.simpleSetValue(HierarchicalStateMachineNames.CurrentMachineName,
							PointerAndMemoryNames.NullPtr)
							+ StateMachineTemplates.hierarchicalStateMachineClassConstructorSharedBody(subMachines,
									false, null));
		}
	}

	private static String subStateMachineSharedConstructor(String className, String parentClassName,
			Multimap<TransitionConditions, Pair<String, String>> machine, String body) {
		String parentParamName = GenerationNames.formatIncomingParamName(HierarchicalStateMachineNames.ParentSmName);
		return className + "::" + className + "("
				+ PrivateFunctionalTemplates.cppType(parentClassName, GenerationTemplates.VariableType.RawPointerType)
				+ " " + parentParamName + "):" + HierarchicalStateMachineNames.ParentSmMemberName + "("
				+ parentParamName + ")" + "\n{\n" + body + "}\n\n";
	}

}
