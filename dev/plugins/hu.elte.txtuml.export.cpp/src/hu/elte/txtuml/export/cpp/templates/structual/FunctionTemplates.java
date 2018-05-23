package hu.elte.txtuml.export.cpp.templates.structual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.utils.Pair;

public class FunctionTemplates {
	
	public static String externalDirName = "external";
	public static String externalFileNameTail = "_body_impl.hpp";

	public static String functionDecl(String functionName) {
		return FunctionTemplates.functionDecl(functionName, null);
	}

	public static String functionDecl(String functionName, List<String> params) {
		return FunctionTemplates.functionDecl(ModifierNames.NoReturn, functionName, params, "", false);
	}

	public static String simpleFunctionDecl(String returnType, String functionName) {
		return PrivateFunctionalTemplates.cppType(returnType) + " " + functionName + "()";
	}

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
		return FunctionTemplates.functionDef(className, returnTypeName, functionName, null, body);
	}

	public static String functionDef(String className, String functionName, List<Pair<String, String>> params,
			String body) {
		return FunctionTemplates.functionDef(className, ModifierNames.NoReturn, functionName, params, body);
	}

	public static String functionDef(String className, String returnTypeName, String functionName,
			List<Pair<String, String>> params, String body) {
		String mainDef = PrivateFunctionalTemplates.cppType(returnTypeName) + " " + className + "::" + functionName
				+ "(" + PrivateFunctionalTemplates.paramList(params) + ")\n{\n";
		return mainDef + body + "}\n\n";

	}

	public static String abstractFunctionDef(String className, String returnTypeName, String functionName,
			List<Pair<String, String>> params, Boolean testing, String destination) {
		StringBuilder body = new StringBuilder("");
		if(!testing) {
			body.append(GenerationNames.Macros.IncludeMacro + 
					GenerationTemplates.generatedExternalInclude(externalDirName, className, functionName, externalFileNameTail));
			
			String directoryPath = destination + File.separator + externalDirName;
			
			String fileName = className + "_" + functionName + externalFileNameTail;
			String fileContent = GenerationNames.Comments.ToDoMessage + 
	        		GenerationNames.Macros.ErrorMacro + GenerationTemplates.generatedErrorMessage(functionName);
			
			try {
				CppExporterUtils.writeOutSource(directoryPath, fileName, fileContent);
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return functionDef(className, returnTypeName, functionName, params, body.toString());
	}

	public static String simpleFunctionDef(String returnType, String functionName, String body, String returnVariable) {
		return simpleFunctionDecl(returnType, functionName) + " {\n" + body + "\n" + "return " + returnVariable
				+ ";\n}";
	}

}
