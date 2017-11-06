package hu.elte.txtuml.export.cpp.templates;

import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.BasicTypeNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ClassUtilsNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TimerNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.UMLStdLibNames;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.utils.Pair;

public class PrivateFunctionalTemplates {

	public static String signalType(String type) {
		return type + GenerationNames.EventClassTypeId;
	}

	public static String include(String className) {
		return "#include \"" + mapUMLClassToCppClass(className) + "." + FileNames.HeaderExtension + "\"\n";
	}

	public static String typedefs(String className) {
		return "typedef std::function<" + ModifierNames.NoReturn + "(" + className + "&,"
				+ EventTemplates.EventPointerType + ")> " + GenerationNames.FunctionPtrTypeName + ";\n"
				+ "typedef std::function<bool(" + className + "&," + EventTemplates.EventPointerType + ")> "
				+ GenerationNames.GuardFuncTypeName + ";\n" + "typedef std::pair<" + GenerationNames.GuardFuncTypeName
				+ "," + GenerationNames.FunctionPtrTypeName + "> " + GenerationNames.GuardActionName + ";\n";
	}

	private static String transitionTableType(String className) {
		return "std::unordered_multimap<" + GenerationNames.EventStateTypeName + "," + className + "::"
				+ GenerationNames.GuardActionName + ">";
	}

	public static String transitionTableDecl(String className) {
		return ModifierNames.StaticModifier + " " + transitionTableType(className) + " "
				+ GenerationNames.TransitionTableName + ";\n";

	}

	public static String transitionTableDef(String className) {
		return transitionTableType(className) + " " + className + "::" + GenerationNames.TransitionTableName + ";\n";
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

	public static String baseClassList(List<String> baseClasses) {
		if (baseClasses == null || baseClasses.size() == 0) {
			return "";
		}
		StringBuilder source = new StringBuilder(" :");
		for (String baseClass : baseClasses) {
			source.append(" " + GenerationNames.ModifierNames.PublicModifier + " " + baseClass + ",");
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

	public static String paramNameList(List<String> params) {
		if (params == null || params.size() == 0)
			return "";

		StringBuilder source = new StringBuilder("");
		for (String param : params) {
			source.append(GenerationNames.formatIncomingParamName(param) + ",");
		}
		return source.substring(0, source.length() - 1);
	}
	
	public static String mapUMLClassToCppClass(String className) {
		switch(className) {
			case UMLStdLibNames.ModelClassName:
				return ClassUtilsNames.BaseClassName;				
			default:
				return className;
		}
	}
	
	public static String cppType(String typeName) {
		String cppType = typeName;
		if (typeName != EventTemplates.EventPointerType && typeName != ModifierNames.NoReturn) {
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
					cppType = BasicTypeNames.StringTypeName;
					break;
				case TimerNames.TimerClassName:
					cppType = TimerNames.TimerPtrName;
					break;
				case PointerAndMemoryNames.EventPtr:
				case RuntimeTemplates.UsingRuntimePtr:
				case RuntimeTemplates.RuntimePtrType:
					cppType = typeName;
					break;
				default:
					cppType = GenerationNames.pointerType(mapUMLClassToCppClass(typeName));
					break;
				}
			} else {
				cppType = "void";
			}
		}
		return cppType;
	}

	public static String debugLogMessage(String className, String functionName) {
		return GenerationTemplates
				.debugOnlyCodeBlock("\tstd::cout << \"" + className + "::" + functionName + "\" << std::endl;\n");
	}

}
