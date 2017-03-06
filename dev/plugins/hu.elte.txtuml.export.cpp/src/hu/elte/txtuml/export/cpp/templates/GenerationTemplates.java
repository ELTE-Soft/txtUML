package hu.elte.txtuml.export.cpp.templates;

import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TimerNames;

public class GenerationTemplates {

	public static final String StandardIOinclude = GenerationNames.StandardIOInclude;
	public static final String DeploymentHeader = GenerationNames.DeploymentHeaderName;
	public static final String TimerInterfaceHeader = TimerNames.TimerInterFaceName.toLowerCase();
	public static final String TimerHeader = TimerNames.TimerClassName.toLowerCase();

	public static String headerName(String className) {
		return className + "." + FileNames.HeaderExtension;
	}

	public static String sourceName(String className) {
		return className + "." + FileNames.SourceExtension;
	}

	public static String dataType(String datatTypeName, String attributes) {
		return GenerationNames.DataType + " " + datatTypeName + "\n" + "{\n" + attributes + "}";
	}

	public static String paramName(String paramName) {
		
		return GenerationNames.formatIncomingParamName(paramName);
	}

	public static String forwardDeclaration(String className) {
		
		return GenerationNames.ClassType + " " + className + ";\n";
	}

	public static String putNamespace(String source, String namespace) {
		return "namespace " + namespace + "\n{\n" + source + "\n}\n";
	}

	public static String formatSubSmFunctions(String source) {
		return source.replaceAll(PointerAndMemoryNames.Self, GenerationNames.ParentSmMemberName);
	}

	public static String createObject(String typeName, String objName, boolean sharedObject) {
		return createObject(typeName, objName, null, null, sharedObject);
	}

	public static String createObject(String typeName, String objName, List<String> params, boolean sharedObject) {
		return createObject(typeName, objName, null, params, sharedObject);
	}

	public static String createObject(String typeName, String objName, List<String> templateParams,
			List<String> params, boolean sharedObject) {
		String templateParameters = "";
		if (templateParams != null) {
			templateParameters = "<";
			for (int i = 0; i < templateParams.size() - 1; i++) {
				templateParameters = templateParameters + templateParams.get(i) + ",";
			}
			templateParameters = templateParameters + templateParams.get(templateParams.size() - 1) + ">";
		}
		if(!sharedObject) {
			return GenerationNames.pointerType(typeName + templateParameters) + " " + objName  + " = "
					+ allocateObject(typeName + templateParameters, templateParams, params, false) + ";\n";
		} else {
			return GenerationNames.sharedPtrType(typeName + templateParameters) + " " + objName  + " = "
					+ allocateObject(typeName + templateParameters, templateParams, params, true) + ";\n";
		}


	}

	public static String allocateObject(String typeName, List<String> templateParams, List<String> params, boolean sharedObject) {

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
		
		String allocatedObject = PointerAndMemoryNames.MemoryAllocator + " " + typeName + templateParameters + parameters;
		if(!sharedObject) {
			return allocatedObject;
		} else {
			return GenerationNames.PointerAndMemoryNames.SmartPtr + "<" + typeName + ">" + "(" + allocatedObject + ")";
		}

	}

	public static String allocateObject(String typeName, List<String> params, boolean sharedObject) {
		return allocateObject(typeName, null, params, sharedObject);
	}

	public static String allocateObject(String typeName) {
		return allocateObject(typeName, null, null, false);
	}

	public static String staticCreate(String typeName, String returnType, String objName, String creatorMethod) {
		return returnType + " " + objName + " = " + staticMethodInvoke(typeName,creatorMethod) + ";\n";
	}
	
	public static String staticMethodInvoke(String className, String method) {
		return className + "::" + method + "()";
	}

	public static String debugOnlyCodeBlock(String code_) {
		return "#ifndef " + GenerationNames.NoDebugSymbol + "\n" + code_ + "#endif\n";
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

}
