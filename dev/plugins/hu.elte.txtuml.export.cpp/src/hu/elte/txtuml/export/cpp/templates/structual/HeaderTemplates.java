package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;
import java.util.Optional;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ClassUtilsNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HiearchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.UMLStdLibNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.SubStateMachineTemplates;

public class HeaderTemplates {
	
	public static interface HeaderType {
		String getSpecificBaseClass();
		String getSpecificPrivatePart();
		String getSpecificInlude();
		Boolean hasExecutionInterface();
		
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
		public String getSpecificInlude() {
			return PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + RuntimeTemplates.SMIHeaderName);
		}

		@Override
		public Boolean hasExecutionInterface() {
			return false;
		}
		
	}
	
	public static class StateMachineClassHeaderType implements HeaderType {
		private List<String> subMachines;
		
		public StateMachineClassHeaderType(List<String> subMachines) {
			this.subMachines = subMachines;
		}
		@Override
		public String getSpecificPrivatePart() {
			return SubStateMachineTemplates.subMachineFriendDecls(subMachines);
		}
		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.StateMachineOwnerBaseName;
		}
		@Override
		public String getSpecificInlude() {
			return PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + RuntimeTemplates.SMIHeaderName);
		}
		@Override
		public Boolean hasExecutionInterface() {
			return true;
		}
	}
	
	public static class SubMachineHeaderType implements HeaderType {
		private String parentClassName;
		
		public SubMachineHeaderType(String parentClassName) {
			this.parentClassName = parentClassName;
		}

		@Override
		public String getSpecificPrivatePart() {
			return VariableTemplates.variableDecl(parentClassName,
					HiearchicalStateMachineNames.ParentSmMemberName, null, false);
		}

		@Override
		public String getSpecificBaseClass() {
			return ClassUtilsNames.SubStateMachineBase;
		}

		@Override
		public String getSpecificInlude() {
			return PrivateFunctionalTemplates.include(RuntimeTemplates.RTPath + RuntimeTemplates.SMIHeaderName);
		}

		@Override
		public Boolean hasExecutionInterface() {
			return false;
		}
	}
	
	public static class HeaderInfo {
		
		/*public static HeaderInfo createSimpleMachineOwnerHeaderInfo(String ownerClassName) {
			return new HeaderInfo(ownerClassName, Optional.empty(), Optional.empty(), HeaderType.ModelClassHeader, Optional.of(new StateMachineInfo(false)));
		}
		
		public static HeaderInfo createHiearhicalMachineOwnerHeaderInfo(String ownerClassName, List<String> subMachines) {
			return new HeaderInfo(ownerClassName, Optional.empty(), Optional.of(subMachines), HeaderType.ModelClassHeader, Optional.of(new StateMachineInfo(true)));
		}
		
		public static HeaderInfo createSimpleClassHeaderInfo(String ownerClassName) {
			return new HeaderInfo(ownerClassName, Optional.empty(), Optional.empty(), HeaderType.ModelClassHeader, Optional.empty());
		}
		
		public static HeaderInfo createSimpleSubMachineHeaderInfo(String ownerClassName, String parentClassName) {
			return new HeaderInfo(ownerClassName, Optional.of(parentClassName), Optional.empty(), HeaderType.CompositeStateHeader, Optional.of(new StateMachineInfo(false)));

		}
		
		public static HeaderInfo crateHierhicalSubMachineHeaderInfo(String ownerClassName, String parentClassName) {
			return new HeaderInfo(ownerClassName, Optional.of(parentClassName), Optional.empty(), HeaderType.CompositeStateHeader, Optional.of(new StateMachineInfo(true)));

		}*/
		
		public static class StateMachineInfo {

			public StateMachineInfo(Boolean hiearhical) {
				this.hiearhical = hiearhical;
			}

			Boolean isHierhicalStateMachine() {
				return hiearhical;
			}
			
			private Boolean hiearhical;
		}
		private String ownerClassName;
		private Optional<StateMachineInfo> stateMachineInfo;
		private HeaderType headerType;

		public HeaderInfo(String ownerClassName, HeaderType headerType, Optional<StateMachineInfo> stateMachineInfo) {
			this.ownerClassName = ownerClassName;
			this.headerType = headerType;
			this.stateMachineInfo = stateMachineInfo;
		}

		public String getRleatedBaseClass() {
			return headerType.getSpecificBaseClass();
		}

		public String getRelatedBaseClassInclude() {
			// TODO Separate includes
			return headerType.getSpecificInlude();
		}
		
		public String getOwnerClassName() {
			return ownerClassName;
		}
		

		public Boolean hasExecutionInterface() {
			return headerType.hasExecutionInterface();
		}
		
		public String getFixPublicParts() {
			StringBuilder fixPublicParts = new StringBuilder("");
			if (stateMachineInfo.isPresent()) {
				fixPublicParts.append(ModifierNames.StaticModifier + " "
						+ FunctionTemplates.functionDecl(StateMachineTemplates.InitTransitionTable));
				fixPublicParts.append(GenerationNames.ProcessEventDecl + GenerationNames.SetInitialStateDecl + "\n");

			}
			if (hasExecutionInterface()) {
				fixPublicParts.append("//RuntimeFunctions\n" + RuntimeTemplates.processEventVirtualDecl()
						+ RuntimeTemplates.processInitTransitionDecl() + "\n");
			}
			
			return fixPublicParts.toString();
		}
		
		public String getFixPrtectedParts() {
			StringBuilder fixProtectedParts = new StringBuilder("");
			if(stateMachineInfo.isPresent()) {
				fixProtectedParts.append(PrivateFunctionalTemplates.typedefs(ownerClassName));
				fixProtectedParts.append(PrivateFunctionalTemplates.transitionTableDecl(ownerClassName));
			}
			
			return fixProtectedParts.toString();
		}
		
		public String getFixPrivateParts() {
			StringBuilder fixPrivateParts = new StringBuilder("");
			if(stateMachineInfo.isPresent()) {
				StateMachineInfo machineInfo = stateMachineInfo.get();
				fixPrivateParts.append("//Simple Machine Parts\n" + FunctionTemplates.functionDecl(GenerationNames.InitStateMachine) + "\n" + 
						GenerationNames.SetStateDecl + GenerationNames.EntryDecl + GenerationNames.ExitDecl + 
						"\n" + "int " + GenerationNames.CurrentStateName + ";\n");
				
				if(machineInfo.isHierhicalStateMachine()) {
					fixPrivateParts.append("//Hierarchical Machine Parts\n" 
							+ HiearchicalStateMachineNames.ActionCallerDecl 
							+ HiearchicalStateMachineNames.CurrentMachine
							+ HiearchicalStateMachineNames.CompositeStateMap); 	
				
					
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

	public static String classHeader(String dependency, String baseClassName, String publicPart,
			String protectedPart, String privatePart, HeaderInfo headerInfo) {
		StringBuilder source = new StringBuilder(dependency + headerInfo.getRelatedBaseClassInclude());
		StringBuilder classDecleration = new StringBuilder("");
		classDecleration.append(GenerationNames.ClassType + " " + headerInfo.getOwnerClassName());
		String objectBase = "";
		if (baseClassName != null && !baseClassName.equals(UMLStdLibNames.ModelClassName)) {
			objectBase = baseClassName;

		} else {
			objectBase = headerInfo.getRleatedBaseClass();

		}

		if (!objectBase.isEmpty()) {
			classDecleration.append(":public " + objectBase);
		}
		classDecleration.append("\n{\n");
		
		classDecleration.append("\npublic:\n" + headerInfo.getFixPublicParts() + publicPart);
		classDecleration.append("\nprotected:\n" + headerInfo.getFixPrtectedParts() + protectedPart);		
		classDecleration.append("\nprivate:\n" + headerInfo.getFixPrivateParts() + privatePart);

		classDecleration.append("\n};\n\n");
		source.append(GenerationTemplates.putNamespace(classDecleration.toString(),
				GenerationNames.Namespaces.ModelNamespace));
		return source.toString();
	}



}
