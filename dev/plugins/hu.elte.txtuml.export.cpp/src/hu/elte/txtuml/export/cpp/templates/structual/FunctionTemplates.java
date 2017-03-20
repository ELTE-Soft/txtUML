package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.utils.Pair;

public class FunctionTemplates {

	public static String functionDecl(String functionName) {
		return FunctionTemplates.functionDecl(functionName, null);
	}

	public static String functionDecl(String functionName, List<String> params) {
		return FunctionTemplates.functionDecl(GenerationNames.NoReturn, functionName, params);
	}

	public static String simpleFunctionDecl(String returnType, String functionName) {
		return PrivateFunctionalTemplates.cppType(returnType) + " " + functionName + "()";
	}

	// TODO modifiers (static, etc) - not supported yet
	public static String functionDecl(String returnTypeName, String functionName, List<String> params) {
		return PrivateFunctionalTemplates.cppType(returnTypeName) + " " + functionName + "("
				+ PrivateFunctionalTemplates.paramTypeList(params) + ");\n";
	}

	public static String functionDef(String className, String functionName, String body) {
		return FunctionTemplates.functionDef(className, GenerationNames.NoReturn, functionName, body);
	}

	public static String functionDef(String className, String returnTypeName, String functionName, String body) {
		return FunctionTemplates.functionDef(className, returnTypeName, functionName, null, body);
	}

	public static String functionDef(String className, String functionName, List<Pair<String, String>> params,
			String body) {
		return FunctionTemplates.functionDef(className, GenerationNames.NoReturn, functionName, params, body);
	}

	// TODO modifiers (static, etc..) - not supported yet
	public static String functionDef(String className, String returnTypeName, String functionName,
			List<Pair<String, String>> params, String body) {
		return PrivateFunctionalTemplates.cppType(returnTypeName) + " " + className + "::" + functionName + "("
				+ PrivateFunctionalTemplates.paramList(params) + ")\n{\n" + body + "}\n\n";
	}

	public static String simpleFunctionDef(String returnType, String functionName, String body, String returnVariable) {
		return simpleFunctionDecl(returnType, functionName) + " {\n" + body + "\n" + "return " + returnVariable
				+ ";\n}";
	}

}
