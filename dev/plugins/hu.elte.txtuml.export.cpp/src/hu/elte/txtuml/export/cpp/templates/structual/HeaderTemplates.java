package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ClassUtilsNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;

public class HeaderTemplates {

	public enum HeaderType {
		StateMachineOwnerClass(true, true), NotStateMachineOwnerClass(false, false), SubStateMachine(true, false);

		private Boolean stateMachineOwner;
		private Boolean runtimeInterface;

		HeaderType(Boolean stateMachineOwner, Boolean runtimeInterface) {
			this.stateMachineOwner = stateMachineOwner;
			this.runtimeInterface = runtimeInterface;
		}

		public Boolean hasStateMachine() {
			return stateMachineOwner;
		}

		public Boolean hasExecutionInterface() {
			return runtimeInterface;
		}

	}

	public static String headerGuard(String source, String className) {
		return "#ifndef __" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase() + "__\n"
				+ "#define __" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase() + "__\n\n"
				+ source + "\n\n#endif //__" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase()
				+ "_";
	}

	public static String hierarchicalStateMachineClassHeader(String dependency, String className, String baseClassName,
			List<String> subMachines, String publicPart, String protectedPart, String privatePart, HeaderType herderType) {
		return HeaderTemplates.classHeader(dependency, className,
				baseClassName, StateMachineTemplates.stateMachineClassFixPublicParts(herderType) + publicPart,
				protectedPart, StateMachineTemplates.hierarchicalStateMachineClassFixPrivateParts(className, subMachines) + privatePart, herderType);
	}

	public static String simpleSubStateMachineClassHeader(String dependency, String className, String parentClass,
			String publicPart, String protectedPart, String privatePart) {

		return HeaderTemplates.simpleStateMachineClassHeader(dependency, className, null, parentClass, publicPart,
				protectedPart,
				VariableTemplates.variableDecl(parentClass, GenerationNames.ParentSmMemberName, null, false)
						+ (privatePart),
				HeaderType.SubStateMachine);
	}

	public static String simpleStateMachineClassHeader(String dependency, String className, String baseClassName,
			String parentClass, String publicPart, String protectedPart, String privatePart, HeaderType headerType) {
		return HeaderTemplates.classHeader(dependency,
				className, baseClassName,
				StateMachineTemplates.stateMachineClassFixPublicParts(headerType) + publicPart, protectedPart,
				StateMachineTemplates.simpleStateMachineClassFixPrivateParts(className) + privatePart, headerType);
	}

	public static String classHeader(String dependency, String className, String baseClassName, String publicPart,
			String protectedPart, String privatePart, HeaderType headerType) {
		StringBuilder source = new StringBuilder(dependency + PrivateFunctionalTemplates.classHeaderIncludes(headerType));
		StringBuilder classDecleration = new StringBuilder("");
		classDecleration.append(GenerationNames.ClassType + " " + className);
		if (baseClassName != null) {
			classDecleration.append(": public " + baseClassName);
		} else {
			String generalModelObjectBase = "";
			switch (headerType) {
			case NotStateMachineOwnerClass:
				generalModelObjectBase = ClassUtilsNames.NotStateMachineOwnerBaseName;
				break;
			case StateMachineOwnerClass:
				generalModelObjectBase = ClassUtilsNames.StateMachineOwnerBaseName;
				break;
			case SubStateMachine:
				generalModelObjectBase = ClassUtilsNames.SubStateMachineBase;
				break;
			default:
				break;

			}
			classDecleration.append(":public " + generalModelObjectBase);

		}
		classDecleration.append("\n{\n");

		if (!publicPart.isEmpty()) {
			classDecleration.append(publicPart);
		}

		if (!protectedPart.isEmpty() || headerType.hasStateMachine()) {
			classDecleration.append("\nprotected:\n" + protectedPart);
			classDecleration.append(StateMachineTemplates.simpleStateMachineClassFixProtectedParts(className));
		}
		classDecleration.append("\nprivate:\n");
		if (!privatePart.isEmpty()) {
			classDecleration.append(privatePart);

		}
		classDecleration.append("\n};\n\n");
		source.append(GenerationTemplates.putNamespace(classDecleration.toString(),
				GenerationNames.Namespaces.ModelNamespace));
		return source.toString();
	}

	public static String hierarchicalSubStateMachineClassHeader(String dependency, String className, String parentClass,
			String publicPart, String protectedPart, String privatePart) {
		List<String> parentParam = new LinkedList<String>();
		parentParam.add(parentClass);

		return HeaderTemplates.hierarchicalStateMachineClassHeader(dependency, className, Collections.emptyList(),
				publicPart, protectedPart, 
				VariableTemplates.variableDecl(parentClass, GenerationNames.ParentSmMemberName, null, false)+ privatePart, 
				HeaderType.SubStateMachine);
	}

	public static String hierarchicalStateMachineClassHeader(String dependency, String className,
			List<String> subMachines, String publicPart, String protectedPart, String privatePart, HeaderType herderType) {
		return hierarchicalStateMachineClassHeader(dependency, className, null, subMachines, publicPart, protectedPart,
				privatePart, herderType);
	}

}
