package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ClassUtilsNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.EntryExitNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HierarchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.SubStateMachineTemplates;

public class HeaderTemplates {

	public static interface HeaderType {
		String getSpecificBaseClass();

		String getSpecificPrivatePart();

		List<String> getSpecificIncludes();

		Boolean hasStateMachine();

		Boolean hasHierarchicalStateMachine();

		Boolean hasExecutionInterface();

	}

	public static class RawClassHeaderType implements HeaderType {

		@Override
		public String getSpecificPrivatePart() {
			return "";
		}

		@Override
		public String getSpecificBaseClass() {
			return null;
		}

		@Override
		public List<String> getSpecificIncludes() {
			return Collections.emptyList();
		}


		@Override
		public Boolean hasExecutionInterface() {
			return false;
		}

		@Override
		public Boolean hasStateMachine() {
			return false;
		}

		@Override
		public Boolean hasHierarchicalStateMachine() {
			return false;
		}

	}

	public static class SimpleClassHeaderType implements HeaderType {

		@Override
		public String getSpecificPrivatePart() {
			return "";
		}

		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.NotStateMachineOwnerBaseName;
		}

		@Override
		public List<String> getSpecificIncludes() {
			return Arrays.asList(RuntimeTemplates.RTPath + ClassUtilsNames.NotStateMachineOwnerBaseName);
		}

		@Override
		public Boolean hasExecutionInterface() {
			return false;
		}

		@Override
		public Boolean hasStateMachine() {
			return false;
		}

		@Override
		public Boolean hasHierarchicalStateMachine() {
			return false;
		}

	}

	public static class StateMachineClassHeaderType implements HeaderType {
		private Optional<List<String>> optionalSubMachines;

		public StateMachineClassHeaderType(Optional<List<String>> optionalSubMachines) {
			this.optionalSubMachines = optionalSubMachines;
		}

		@Override
		public String getSpecificPrivatePart() {
			if (optionalSubMachines.isPresent()) {
				return SubStateMachineTemplates.subMachineFriendDecls(optionalSubMachines.get());

			}
			return "";
		}

		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.StateMachineOwnerBaseName;
		}

		@Override
		public List<String> getSpecificIncludes() {
			List<String> includes = new ArrayList<>();
			if (hasHierarchicalStateMachine()) {
				includes.add(RuntimeTemplates.RTPath + ClassUtilsNames.SubStateMachineBase);
			}
			includes.add(RuntimeTemplates.RTPath + ClassUtilsNames.StateMachineOwnerBaseName);

			return includes;
		}

		@Override
		public Boolean hasExecutionInterface() {
			return true;
		}

		@Override
		public Boolean hasStateMachine() {
			return true;
		}

		@Override
		public Boolean hasHierarchicalStateMachine() {
			return optionalSubMachines.isPresent();
		}
	}

	public static class SubMachineHeaderType implements HeaderType {
		private String parentClassName;
		private Boolean hierarchical;

		public SubMachineHeaderType(String parentClassName, Boolean hierarchical) {
			this.parentClassName = parentClassName;
			this.hierarchical = hierarchical;
		}

		@Override
		public String getSpecificPrivatePart() {
			return VariableTemplates.variableDecl(parentClassName, HierarchicalStateMachineNames.ParentSmMemberName,
					null, false);
		}

		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.SubStateMachineBase;
		}

		@Override
		public List<String> getSpecificIncludes() {
			return Arrays.asList(RuntimeTemplates.RTPath + ClassUtilsNames.SubStateMachineBase);
		}

		@Override
		public Boolean hasExecutionInterface() {
			return false;
		}

		@Override
		public Boolean hasStateMachine() {
			return true;
		}

		@Override
		public Boolean hasHierarchicalStateMachine() {
			return hierarchical;
		}
	}

	public static class HeaderInfo {

		public static class StateMachineInfo {
			public StateMachineInfo(Boolean hierarchical) {
				this.hierarchical = hierarchical;
			}


			Boolean isHierarhicalStateMachine() {
				return hierarchical;
			}

			private Boolean hierarchical;
		}

		private String ownerClassName;
		private HeaderType headerType;

		public HeaderInfo(String ownerClassName, HeaderType headerType) {
			this.ownerClassName = ownerClassName;
			this.headerType = headerType;
		}

		public String getRelatedBaseClass() {
			return headerType.getSpecificBaseClass();
		}

		public List<String> getRelatedBaseClassInclude() {
			return headerType.getSpecificIncludes();
		}

		public String getOwnerClassName() {
			return ownerClassName;
		}

		public String getFixPublicParts() {
			StringBuilder fixPublicParts = new StringBuilder("");
			if (headerType.hasStateMachine()) {
				fixPublicParts.append(ModifierNames.StaticModifier + " "
						+ FunctionTemplates.functionDecl(StateMachineTemplates.InitTransitionTable));
				fixPublicParts.append(GenerationNames.ProcessEventDecl + GenerationNames.SetInitialStateDecl + "\n");
				fixPublicParts.append(StateMachineTemplates.initializeFunctionDecl());
				fixPublicParts.append(StateMachineTemplates.finalizeFunctionDecl());

			}
			if (headerType.hasExecutionInterface()) {
				fixPublicParts.append("//RuntimeFunctions\n" + RuntimeTemplates.processEventVirtualDecl()
						+ RuntimeTemplates.processInitTransitionDecl() + "\n");
			}

			return fixPublicParts.toString();
		}

		public String getFixProtectedParts() {
			StringBuilder fixProtectedParts = new StringBuilder("");
			if (headerType.hasStateMachine()) {
				fixProtectedParts.append(PrivateFunctionalTemplates.typedefs(ownerClassName));
				fixProtectedParts.append(PrivateFunctionalTemplates.transitionTableDecl(ownerClassName));
			}

			return fixProtectedParts.toString();
		}

		public String getFixPrivateParts() {
			StringBuilder fixPrivateParts = new StringBuilder("");
			if(headerType.hasStateMachine()) {
				fixPrivateParts.append("//Simple Machine Parts\n" + FunctionTemplates.functionDecl(GenerationNames.InitializerFixFunctionNames.InitStateMachine) + "\n" + 
						GenerationNames.SetStateDecl + EntryExitNames.EntryDecl + EntryExitNames.ExitDecl + 
						"\n" + "int " + GenerationNames.CurrentStateName + ";\n");
				
				if(headerType.hasHierarchicalStateMachine()) {
					fixPrivateParts.append("//Hierarchical Machine Parts\n" 
							+ HierarchicalStateMachineNames.ActionCallerDecl 
							+ HierarchicalStateMachineNames.CurrentMachine
							+ HierarchicalStateMachineNames.CompositeStateMap); 	
				
					

				}

			}
			fixPrivateParts.append(headerType.getSpecificPrivatePart());
			return fixPrivateParts.toString();
		}
	}


	public static String headerGuard(String source, String className) {
		return "#ifndef __" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase() + "__\n"
				+ "#define __" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase() + "__\n\n"
				+ source + "\n\n#endif //__" + className.toUpperCase() + "_" + FileNames.HeaderExtension.toUpperCase()
				+ "_";
	}

	public static String classHeader(List<String> baseClassNames, List<String> pureInfBaseNames, String publicPart,
			String protectedPart, String privatePart, HeaderInfo headerInfo) {

		StringBuilder classDeclaration = new StringBuilder("");
		classDeclaration.append(GenerationNames.TypeDeclarationKeywords.ClassType + " " + headerInfo.getOwnerClassName());

		List<String> objectBase = new LinkedList<>();
		if (pureInfBaseNames != null) {
			objectBase.addAll(pureInfBaseNames);
		}
		if (baseClassNames != null && !baseClassNames.isEmpty()) {
			objectBase.addAll(baseClassNames);
		} else {
			String relatedBaseClass = headerInfo.getRelatedBaseClass();
			if (relatedBaseClass != null && !relatedBaseClass.isEmpty()) {
				objectBase.add(relatedBaseClass);
			}
		}

		classDeclaration.append(PrivateFunctionalTemplates.baseClassList(objectBase));

		classDeclaration.append("\n{\n");

		classDeclaration.append("\npublic:\n" + headerInfo.getFixPublicParts() + publicPart);
		classDeclaration.append("\nprotected:\n" + headerInfo.getFixProtectedParts() + protectedPart);
		classDeclaration.append("\nprivate:\n" + headerInfo.getFixPrivateParts() + privatePart);

		classDeclaration.append("\n};\n\n");
		return classDeclaration.toString();
	}

}
