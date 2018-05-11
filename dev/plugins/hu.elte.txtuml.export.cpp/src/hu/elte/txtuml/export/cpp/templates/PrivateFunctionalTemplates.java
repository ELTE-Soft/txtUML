package hu.elte.txtuml.export.cpp.templates;

import java.util.List;

import hu.elte.txtuml.export.cpp.CppExporterUtils.TypeDescriptor;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.BasicTypeNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ClassUtilsNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.InterfaceNames;
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
		return "#include \"" + getClassPath(mapUMLTypeToCppClass(className)) + "\"\n";
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

	public static String paramList(List<Pair<TypeDescriptor, String>> params) {
		if (params == null || params.size() == 0)
			return "";
		StringBuilder source = new StringBuilder("");
		for (Pair<TypeDescriptor, String> item : params) {
			source.append(PrivateFunctionalTemplates.cppType(item.getFirst().getTypeName(),
					GenerationTemplates.VariableType.getUMLMultpliedElementType(item.getFirst().getLowMul(),
							item.getFirst().getUpMul()))
					+ " " + GenerationNames.formatIncomingParamName(item.getSecond()) + ",");
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

	public static String paramTypeList(List<TypeDescriptor> params) {
		if (params == null || params.size() == 0)
			return "";
		StringBuilder source = new StringBuilder("");
		for (TypeDescriptor item : params) {
			source.append(cppType(item.getTypeName(),
					GenerationTemplates.VariableType.getUMLMultpliedElementType(item.getLowMul(), item.getUpMul()))
					+ ",");
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

	public static String mapUMLTypeToCppClass(String className) {
		switch (className) {
		case "Integer":
			return "int";
		case "Real":
			return "double";
		case "Boolean":
			return "bool";
		case "String":
			return BasicTypeNames.StringTypeName;
		case TimerNames.TimerClassName:
			return TimerNames.TimerPtrName;
		case UMLStdLibNames.ModelClassName:
			return ClassUtilsNames.BaseClassName;
		case UMLStdLibNames.EmptyInfName:
			return InterfaceNames.EmptyInfName;
		default:
			return className;
		}
	}

	private static String getClassPath(String className) {
		switch (className) {
		case InterfaceNames.EmptyInfName:
			return FileNames.InterfaceUtilsPath + "." + FileNames.HeaderExtension;
		default:
			return className + "." + FileNames.HeaderExtension;

		}
	}

	public static String cppType(String typeName, GenerationTemplates.VariableType varType) {
		String cppType = typeName;
		if (typeName != ModifierNames.NoReturn) {
			if (typeName != null) {
				switch (typeName) {
				case PointerAndMemoryNames.EventPtr:
				case RuntimeTemplates.UsingRuntimePtr:
				case RuntimeTemplates.RuntimePtrType:
					cppType = typeName;
					break;
				default:
					String mappedType = mapUMLTypeToCppClass(typeName);
					switch (varType) {
					case RawPointerType:
						cppType = GenerationNames.pointerType(mappedType);
						break;
					case EventPtr:
						cppType = EventTemplates.eventPtr(mappedType);
						break;
					case SharedPtr:
						cppType = GenerationNames.sharedPtrType(mappedType);
						break;
					case UMLVariableType:
						cppType = GenerationNames.umlVarType(mappedType, varType.getLowMul(), varType.getUpMul());
						break;
					case StackStored:
					case OriginalType:
						cppType = mappedType;
						break;

					default:
						break;
					}
				}
			} else {
				cppType = "void";
			}
		}
		return cppType;
	}

	public static String debugLogMessage(String className, String functionName) {
		return GenerationTemplates
				.debugOnlyCodeBlock("\tstd::cout << \"" + className + "::" + functionName + "\\n" + '"' + ';' + "\n");
	}

}
