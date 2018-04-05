package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.Arrays;
import java.util.Optional;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ActionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.CollectionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class LinkTemplates {

	public enum LinkFunctionType {
		Link, Unlink,
		AssemblyConnect, 
		DelegeateConnect
	};

	public static String linkSourceName(String className) {
		return className + "-" + GenerationNames.LinkAddition + "." + FileNames.SourceExtension;
	}

	public static String templateLinkFunctionGeneralDef(LinkFunctionType linkFunction) {

		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.TemplateDecl + "<" + GenerationNames.TemplateType + " "
				+ GenerationNames.EndPointName + ">\n");
		source.append(ModifierNames.NoReturn + " " + getMemberLinkUnlinkFunctionName(linkFunction));
		source.append("(" + GenerationNames.TemplateType + " "
				+ PrivateFunctionalTemplates.cppType(GenerationNames.EndPointName + "::" + GenerationNames.EdgeType)
				+ " " + ") {}\n");

		return source.toString();
	}

	public static String linkTemplateSpecializationDecl(String className, String otherClassName,
			String otherEndPointName, String assocName, LinkFunctionType linkFunction) {
		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.TemplateDecl + "<>\n");
		source.append(ModifierNames.NoReturn + " " + className + "::" + getMemberLinkUnlinkFunctionName(linkFunction));
		source.append("<" + GenerationNames.TemplateType + " " + assocName + "::" + otherEndPointName
				+ ">");
		source.append("(" + PrivateFunctionalTemplates.cppType(otherClassName) + ");\n");

		return source.toString();
	}
	
	private static String getAddOrRemoveAssoc(LinkFunctionType linkFunction) {
		if (linkFunction == LinkFunctionType.Link)
			return GenerationNames.AddAssocToAssocationFunctionName;
		else if (linkFunction == LinkFunctionType.Unlink)
			return GenerationNames.RemoveAssocToAssocationFunctionName;

		return "";
	}
	
	private static String getMemberLinkUnlinkFunctionName(LinkFunctionType linkFunction) {
		switch(linkFunction) {
		
		case Link:
			return GenerationNames.LinkMemberFunctionName;
		case Unlink:
			return GenerationNames.UnlinkMemberFunctionName;
		case AssemblyConnect:
		case DelegeateConnect:
		default:
			assert(false);
			return "UNSUPPORTED_MEMBER_LINK";
		
		}
		
	}

	public static String linkTemplateSpecializationDef(String className, String otherClassName, String assocName,
			String roleName, boolean isNavigable, LinkFunctionType linkFunction) {
		StringBuilder source = new StringBuilder("");
		if (isNavigable) {
			source.append(GenerationNames.TemplateDecl + "<>\n");
			source.append(ModifierNames.NoReturn + " " + className + "::" + getMemberLinkUnlinkFunctionName(linkFunction));
			source.append(
					"<" + GenerationNames.TemplateType + " " + assocName + "::" + roleName + ">");
			source.append("(" + PrivateFunctionalTemplates.cppType(otherClassName) + " "
					+ GenerationNames.AssocParameterName + ")\n");
			source.append("{\n" + formatAssociationRoleName(assocName, roleName) + PointerAndMemoryNames.SimpleAccess
					+ getAddOrRemoveAssoc(linkFunction) + "(" + GenerationNames.AssocParameterName + ");\n}\n");
		}

		return source.toString();
	}


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
				ObjectDeclDefTemplates.VariableType.StackStored, false);
	}
	
	public static String associationDef(String assocName, String leftDescriptor, String rigthDescriptor) {
		return ObjectDeclDefTemplates.variableDecl(GenerationNames.AssociationNames.AssociationClassName, 
				assocName, "", Optional.of(Arrays.asList(endStructDescriptor(leftDescriptor) , endStructDescriptor(rigthDescriptor))), 
				ObjectDeclDefTemplates.VariableType.StackStored, false);
	}

	private static String endStructDescriptor(String originalDescriptor) {
		return originalDescriptor + "End";
	}
}
