package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ClassUtilsNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.EntryExitNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HierarchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.SubStateMachineTemplates;

public class HeaderTemplates {
	
	public static interface HeaderType {
		String getSpecificBaseClass();
		String getSpecificPrivatePart();
		String getSpecificInclude();
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
		public String getSpecificInclude() {
			return "";
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
		public String getSpecificInclude() {
			return PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + ClassUtilsNames.NotStateMachineOwnerBaseName);
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
			if(optionalSubMachines.isPresent()) {
				return SubStateMachineTemplates.subMachineFriendDecls(optionalSubMachines.get());
 
			}
			return "";
		}
		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.StateMachineOwnerBaseName;
		}
		@Override
		public String getSpecificInclude() {
			StringBuilder includes = new StringBuilder("");
			if(hasHierarchicalStateMachine()) {
				includes.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + ClassUtilsNames.SubStateMachineBase));
			}
			includes.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + ClassUtilsNames.StateMachineOwnerBaseName));
			
			return includes.toString();
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
			return VariableTemplates.variableDecl(parentClassName,
					HierarchicalStateMachineNames.ParentSmMemberName, null, false);
		}

		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.SubStateMachineBase;
		}

		@Override
		public String getSpecificInclude() {
			return PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + ClassUtilsNames.SubStateMachineBase);
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

		public String getRelatedBaseClassInclude() {
			return headerType.getSpecificInclude();
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
			if(headerType.hasStateMachine()) {
				fixProtectedParts.append(PrivateFunctionalTemplates.typedefs(ownerClassName));
				fixProtectedParts.append(PrivateFunctionalTemplates.transitionTableDecl(ownerClassName));
			}
			
			return fixProtectedParts.toString();
		}

		public String getFixPrivateParts() {
			StringBuilder fixPrivateParts = new StringBuilder("");
			if(headerType.hasStateMachine()) {
				fixPrivateParts.append("//Simple Machine Parts\n" + FunctionTemplates.functionDecl(GenerationNames.InitStateMachine) + "\n" + 
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

	public static String classHeader(String dependency, List<String> baseClassNames, String publicPart,
			String protectedPart, String privatePart, HeaderInfo headerInfo) {
		StringBuilder source = new StringBuilder(dependency + headerInfo.getRelatedBaseClassInclude());
		StringBuilder classDecleration = new StringBuilder("");
		classDecleration.append(GenerationNames.ClassType + " " + headerInfo.getOwnerClassName());

		List<String> objectBase = new LinkedList<>();
		if (baseClassNames != null) {
			objectBase.addAll(baseClassNames);
		}

		String relatedBaseClass = headerInfo.getRelatedBaseClass();
		if (relatedBaseClass != null && !relatedBaseClass.isEmpty()) {
			objectBase.add(relatedBaseClass);
		}

		classDecleration.append(PrivateFunctionalTemplates.baseClassList(objectBase));

		classDecleration.append("\n{\n");
		
		classDecleration.append("\npublic:\n" + headerInfo.getFixPublicParts() + publicPart);
		classDecleration.append("\nprotected:\n" + headerInfo.getFixProtectedParts() + protectedPart);		
		classDecleration.append("\nprivate:\n" + headerInfo.getFixPrivateParts() + privatePart);

		classDecleration.append("\n};\n\n");
		source.append(GenerationTemplates.putNamespace(classDecleration.toString(),
				GenerationNames.Namespaces.ModelNamespace));
		return source.toString();
	}


}
