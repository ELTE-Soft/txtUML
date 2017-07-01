package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.utils.Pair;

public class FunctionTemplates {

	public static String functionDecl(String functionName) {
		return FunctionTemplates.functionDecl(functionName, null);
	}

	public static String functionDecl(String functionName, List<String> params) {
		return FunctionTemplates.functionDecl(ModifierNames.NoReturn, functionName, params, "", false);
	}

	public static String simpleFunctionDecl(String returnType, String functionName) {
		return PrivateFunctionalTemplates.cppType(returnType) + " " + functionName + "()";
	}

	// TODO modifiers (static, etc) - not supported yet
	public static String functionDecl(String returnTypeName, String functionName, List<String> params, String modifier,
			boolean isPureVirtual) {
		String mainDecl = PrivateFunctionalTemplates.cppType(returnTypeName) + " " + functionName + "("
				+ PrivateFunctionalTemplates.paramTypeList(params) + ")";
		if (modifier != "") {
			return modifier + " " + mainDecl + (isPureVirtual ? "= 0" : "") + ";\n";
		} else {
			return ActivityTemplates.blockStatement(mainDecl);
		}

	}

	public static String functionDecl(String returnTypeName, String functionName, List<String> params) {
		return functionDecl(returnTypeName, functionName, params, "", false);
	}

	public static String functionDef(String className, String functionName, String body) {
		return FunctionTemplates.functionDef(className, ModifierNames.NoReturn, functionName, body);
	}

	public static String functionDef(String className, String returnTypeName, String functionName, String body) {
		return FunctionTemplates.functionDef(className, returnTypeName, functionName, null, body, false);
	}

	public static String functionDef(String className, String functionName, List<Pair<String, String>> params,
			String body) {
		return FunctionTemplates.functionDef(className, ModifierNames.NoReturn, functionName, params, body, false);
	}

	// TODO modifiers (static, etc..) - not supported yet
	public static String functionDef(String className, String returnTypeName, String functionName,
			List<Pair<String, String>> params, String body, boolean hasModifier) {
		String mainDef = PrivateFunctionalTemplates.cppType(returnTypeName) + " " + className + "::" + functionName
				+ "(" + PrivateFunctionalTemplates.paramList(params) + ")\n{\n";
		if (!hasModifier) {
			return mainDef + body + "}\n\n";

		} else {
			return mainDef + "//TODO\n" + "}\n\n";
		}
	}

	public static String simpleFunctionDef(String returnType, String functionName, String body, String returnVariable) {
		return simpleFunctionDecl(returnType, functionName) + " {\n" + body + "\n" + "return " + returnVariable
				+ ";\n}";
	}

}
