package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.Arrays;
import java.util.Optional;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ActionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.CollectionNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class LinkTemplates {

	public enum LinkFunctionType {
		Link, Unlink,
		AssemblyConnect, 
		DelegeateConnect
	};


	
	public static String getLinkFunctionName(LinkFunctionType linkFunction) {
		
		switch (linkFunction) {
		case AssemblyConnect:
			return ActionNames.AssemblyConnect;
		case DelegeateConnect:
			return ActionNames.DelegateConnect;
		case Link:
			return ActionNames.LinkActionName;
		case Unlink:
			return ActionNames.UnLinkActionName;
		default:
			assert(false);
			return "MISSING_LINK_OPTION";
		
		}	

	}

	public static String createEndPointClass(String endPointType, String endPointName, String leftEndPointName, String rightEndPointName, 
			Integer lowMultiplicity, Integer upMultiplicity) {

		return GenerationNames.TypeDelcreationKeywords.AssociationEndDescriptor + " " + endStructDescriptor(endPointName) + ": public " + "AssocEnd" 
				+ CppExporterUtils.createTemplateParametersCode(Optional.of(
						Arrays.asList(endStructDescriptor(leftEndPointName), 
								endStructDescriptor(rightEndPointName), endPointType, lowMultiplicity.toString(), upMultiplicity.toString()))) + "{" +
				ObjectDeclDefTemplates.propertyDecl(endStructDescriptor(endPointName), endPointName, GenerationNames.PointerAndMemoryNames.Self) + "};";	

	}

	public static String formatAssociationRoleName(String associationName, String role) {
		return associationName + "_" + role;
	}

	public static String manyMultiplicityDependency() {
		return PrivateFunctionalTemplates.include(CollectionNames.Collection);
	}
	
	public static String associationDecl(String assocName, String leftDescriptor, String rigthDescriptor) {
		return "extern " + ObjectDeclDefTemplates.variableDecl(GenerationNames.AssociationNames.AssociationClassName, 
				assocName, "", Optional.of(Arrays.asList(endStructDescriptor(leftDescriptor), endStructDescriptor(rigthDescriptor))), 
				GenerationTemplates.VariableType.StackStored, false);
	}
	
	public static String associationDef(String assocName, String leftDescriptor, String rigthDescriptor) {
		return ObjectDeclDefTemplates.variableDecl(GenerationNames.AssociationNames.AssociationClassName, 
				assocName, "", Optional.of(Arrays.asList(endStructDescriptor(leftDescriptor) , endStructDescriptor(rigthDescriptor))), 
				GenerationTemplates.VariableType.StackStored, false);
	}

	private static String endStructDescriptor(String originalDescriptor) {
		return originalDescriptor + "End";
	}
}
