package hu.elte.txtuml.export.cpp.templates;

import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TimerNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TypeDelcreationKeywords;

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
		return TypeDelcreationKeywords.DataType + " " + datatTypeName + "\n" + "{\n" + attributes + "}";
	}
	

	public static String paramName(String paramName) {
		
		return GenerationNames.formatIncomingParamName(paramName);
	}

	public static String forwardDeclaration(String className) {
		
		return TypeDelcreationKeywords.ClassType + " " + className + ";\n";
	}

	public static String putNamespace(String source, String namespace) {
		return "namespace " + namespace + "\n{\n" + source + "\n}\n";
	}

	public static String formatSubSmFunctions(String source) {
		return source.replaceAll(PointerAndMemoryNames.Self, GenerationNames.ParentSmMemberName);
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
