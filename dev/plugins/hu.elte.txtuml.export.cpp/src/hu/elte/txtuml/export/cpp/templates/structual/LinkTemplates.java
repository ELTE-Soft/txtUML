package hu.elte.txtuml.export.cpp.templates.structual;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ActionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.CollectionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TypeDelcreationKeywords;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class LinkTemplates {

	public static final String AssocationHeader = GenerationNames.AssocationHeaderName;
	public static final String AssociationsStructuresHreaderName = GenerationNames.AssociationsHeaderName;
	public static final String AssociationStructuresHeader = GenerationNames.AssociationsHeaderName + "."
			+ FileNames.HeaderExtension;
	public static final String AssociationStructuresSource = GenerationNames.AssociationsHeaderName + "."
			+ FileNames.SourceExtension;

	public enum LinkFunctionType {
		Link, Unlink,
		AssemblyConnect, 
		DelegeateConnect
	};

	public static String linkSourceName(String className) {
		return className + "-" + GenerationNames.LinkAddition + "." + FileNames.SourceExtension;
	}

	public static String assocationEndPointDecl(String className, String roleName, Integer lower, Integer upper) {
		return GenerationNames.AssocMultiplicityDataStruct + "<" + className + ">" + " " + roleName + " "
				+ GenerationNames.AssigmentOperator + " " + GenerationNames.AssocMultiplicityDataStruct + "<"
				+ className + ">" + "(" + lower + "," + upper + ");\n";
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

	public static String createAssociationStructure(String associationName, String E1, String E2, String endPoint1,
			String endPoint2) {
		StringBuilder source = new StringBuilder("");
		source.append(TypeDelcreationKeywords.AssociationStructure + " " + associationName);
		source.append("{\n");
		source.append(createEndPointClass(E1, endPoint1));
		source.append(createEndPointClass(E2, endPoint2));
		source.append("};\n");
		return source.toString();
	}

	public static String createEndPointClass(String classType, String endPointName) {

		return GenerationNames.TypeDelcreationKeywords.AssociationStructure + " " + endPointName + "{typedef " + PrivateFunctionalTemplates.mapUMLClassToCppClass(classType) + " " + GenerationNames.EdgeType
				+ ";};\n";
	}

	public static String formatAssociationRoleName(String associationName, String role) {
		return associationName + "_" + role;
	}

	public static String manyMultiplicityDependency() {
		return PrivateFunctionalTemplates.include(CollectionNames.Collection);
	}

}
