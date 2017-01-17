package hu.elte.txtuml.export.cpp.templates;

import java.util.List;

import hu.elte.txtuml.utils.Pair;

public class PrivateFunctionalTemplates {

	public static String signalType(String type) {
		return type + GenerationNames.EventClassTypeId;
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

	public static String typedefs(String className) {
		return "typedef std::function<" + GenerationNames.NoReturn + "(" + className + "&,"
				+ GenerationNames.EventBaseRefName + ")> " + GenerationNames.FunctionPtrTypeName + ";\n"
				+ "typedef std::function<bool(" + className + "&," + GenerationNames.EventBaseRefName + ")> "
				+ GenerationNames.GuardFuncTypeName + ";\n" + "typedef std::pair<" + GenerationNames.GuardFuncTypeName
				+ "," + GenerationNames.FunctionPtrTypeName + "> " + GenerationNames.GuardActionName + ";\n";
	}

	private static String transitionTableType(String className) {
		return "std::unordered_multimap<" + GenerationNames.EventStateTypeName + "," + className + "::"
				+ GenerationNames.GuardActionName + ">";
	}

	public static String transitionTableDecl(String className) {
		return GenerationNames.StaticModifier + " " + transitionTableType(className) + " "
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
				case GenerationNames.TimerClassName:
					cppType = GenerationNames.sharedPtrType(typeName);
					break;
				case GenerationNames.EventPtr:
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
