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
	public static String createAssociationDescriptor(String assocName, String endDescriptors) {	
		return GenerationNames.TypeDelcreationKeywords.AssociationEndDescriptor + " " + assocDescriptor(assocName) + "{" + endDescriptors + "};";
		
	}
	
	public static String createEndPointClass(String endPointType, String endPointName, String leftEndPointName, String rightEndPointName, 
			Integer lowMultiplicity, Integer upMultiplicity) {
		String endPointCppType = PrivateFunctionalTemplates.cppType(endPointType, GenerationTemplates.VariableType.OriginalType);
		return GenerationNames.TypeDelcreationKeywords.AssociationEndDescriptor + " " + endStructDescriptor(endPointName) + ": public " + "AssocEnd" 
				+ CppExporterUtils.createTemplateParametersCode(Optional.of(
						Arrays.asList(endStructDescriptor(leftEndPointName), 
								endStructDescriptor(rightEndPointName), endPointCppType, lowMultiplicity.toString(), upMultiplicity.toString()))) + "{" +
				ObjectDeclDefTemplates.propertyDecl(endStructDescriptor(endPointName), endPointName, GenerationNames.PointerAndMemoryNames.Self) + "};";	

	}

	public static String manyMultiplicityDependency() {
		return PrivateFunctionalTemplates.include(CollectionNames.Collection);
	}
	
	public static String associationDecl(String assocName, String leftDescriptor, String rigthDescriptor) {
		return "extern " + ObjectDeclDefTemplates.variableDecl(GenerationNames.AssociationNames.AssociationClassName, 
				assocName, "", Optional.of(Arrays.asList(endStructDescriptorReference(assocName,leftDescriptor), endStructDescriptorReference(assocName,rigthDescriptor))), 
				GenerationTemplates.VariableType.StackStored, false);
	}
	
	public static String associationDef(String assocName, String leftDescriptor, String rigthDescriptor) {
		return ObjectDeclDefTemplates.variableDecl(GenerationNames.AssociationNames.AssociationClassName, 
				assocName, "", Optional.of(Arrays.asList(endStructDescriptorReference(assocName, leftDescriptor) , endStructDescriptorReference(assocName, rigthDescriptor))), 
				GenerationTemplates.VariableType.StackStored, false);
	}
	
	public static String assocEndPreDecl(String assocEnd) {
		return GenerationTemplates.forwardDeclaration(endStructDescriptor(assocEnd), GenerationTemplates.ClassDeclerationType.AssocDescriptor);
	}
	
	public static String endCollectionType(String assocName, String endName) {
		return endStructDescriptorReference(assocName,endName) + "::" + CollectionNames.EndCollectionTypeDef;
	}

	private static String endStructDescriptor(String originalDescriptor) {
		return originalDescriptor + "End";
	}
	
	private static String endStructDescriptorReference(String assocName, String originalDescriptor) {
		return assocDescriptor(assocName) + "::" + endStructDescriptor(originalDescriptor);
	}
	
	private static String assocDescriptor(String originalAssocName) {
		return originalAssocName + "Descriptor";
	}


}
