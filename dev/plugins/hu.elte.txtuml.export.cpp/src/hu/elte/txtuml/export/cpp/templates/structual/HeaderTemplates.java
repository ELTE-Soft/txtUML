package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;

public class HeaderTemplates {

	public static String headerGuard(String source, String className) {
		return "#ifndef __" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase() + "__\n"
				+ "#define __" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase()
				+ "__\n\n" + source + "\n\n#endif //__" + className.toUpperCase() + "_"
				+ FileNames.HeaderExtension.toUpperCase() + "_";
	}

	public static String hierarchicalStateMachineClassHeader(String dependency, String className,
			String baseClassName, List<String> subMachines, String publicPart, String protectedPart, String privatePart,
			Boolean rt) {
		return HeaderTemplates.classHeader(PrivateFunctionalTemplates.classHeaderIncludes(rt) + dependency, className,
				baseClassName, StateMachineTemplates.stateMachineClassFixPublicParts(className, rt) + publicPart,
				protectedPart,
				StateMachineTemplates.hierarchicalStateMachineClassFixPrivateParts(className, subMachines)
						+ privatePart,
				true, rt);
	}

	public static String simpleSubStateMachineClassHeader(String dependency, String className,
			String parentClass, String publicPart, String protectedPart, String privatePart) {

		return HeaderTemplates.simpleStateMachineClassHeader(dependency, className, null, parentClass, publicPart,
				protectedPart,
				VariableTemplates.variableDecl(parentClass, GenerationNames.ParentSmMemberName, null, false)
						+ (privatePart),
				false);
	}

	public static String simpleStateMachineClassHeader(String dependency, String className, String baseClassName,
			String parentClass, String publicPart, String protectedPart, String privatePart, Boolean rt) {
		return HeaderTemplates.classHeader(PrivateFunctionalTemplates.classHeaderIncludes(rt) + dependency, className,
				baseClassName, StateMachineTemplates.stateMachineClassFixPublicParts(className, rt) + publicPart,
				 protectedPart, StateMachineTemplates.stateMachineClassFixPrivateParts(className) + privatePart,
				true, rt);
	}

	public static String classHeader(String dependency, String name, String baseClassName, String publicPart,
			String protectedPart, String privatePart) {
		return HeaderTemplates.classHeader(dependency, name, baseClassName, publicPart, protectedPart, privatePart,
				false, false);
	}

	public static String classHeader(String dependency, String name, String baseClassName, String publicPart,
			String protectedPart, String privatePart, Boolean rt) {
		return HeaderTemplates.classHeader(dependency, name, baseClassName, publicPart, protectedPart, privatePart,
				false, rt);
	}

	public static String classHeader(String dependency, String className, String baseClassName,
			String publicPart, String protectedPart, String privatePart, Boolean sm, Boolean rt) {
		StringBuilder source = new StringBuilder(dependency);
		StringBuilder classDecleration = new StringBuilder("");
		classDecleration.append(GenerationNames.ClassType + " " + className);
		if (baseClassName != null) {
			classDecleration.append(": public " + baseClassName);
		} else if (sm) {
			classDecleration.append(":public " + GenerationNames.StatemachineBaseName);
			if (rt) {
				classDecleration.append(",public " + RuntimeTemplates.STMIName);
			}

		}
		classDecleration.append("\n{\n");

		if (!publicPart.isEmpty()) {
			classDecleration.append(publicPart);
		}
		
		
		if (!protectedPart.isEmpty() || sm) {
			classDecleration.append("\nprotected:\n" + protectedPart);
			classDecleration.append(StateMachineTemplates.simpleStateMachineClassFixProtectedParts(className));
		}
		classDecleration.append("\nprivate:\n");
		if (!privatePart.isEmpty()) {
			classDecleration.append(privatePart);

		}
		classDecleration.append("\n};\n\n");
		source.append(GenerationTemplates.putNamespace(classDecleration.toString(), GenerationNames.Namespaces.ModelNamespace));
		return source.toString();
	}

	public static String hierarchicalSubStateMachineClassHeader(String dependency, String className,
			String parentClass, String publicPart, String protectedPart, String privatePart) {
		List<String> parentParam = new LinkedList<String>();
		parentParam.add(parentClass);

		return HeaderTemplates.hierarchicalStateMachineClassHeader(dependency, className, Collections.emptyList(), publicPart,
				protectedPart,
				VariableTemplates.variableDecl(parentClass, GenerationNames.ParentSmMemberName, null, false)
						+ (privatePart),
				false);
	}

	public static String hierarchicalStateMachineClassHeader(String dependency, String className,
			List<String> subMachines, String publicPart, String protectedPart, String privatePart, Boolean rt) {
		return hierarchicalStateMachineClassHeader(dependency, className, null, subMachines, publicPart, protectedPart,
				privatePart, rt);
	}

}
