package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;

class AssociationExporter {
	private List<Property> associationMembers;
	
	AssociationExporter(EList<Property> properites) {
		associationMembers = new ArrayList<Property>();
		exportAssociations(properites);
	}

	private void exportAssociations(EList<Property> properites) {
		for (Property prop : properites) {
			if (prop.getAssociation() != null) {
				associationMembers.add(prop);
			}
		}
	}
	
	boolean ownAssociation() {return !associationMembers.isEmpty();}

	String createAssociationMemberDeclarationsCode() {
		StringBuilder source = new StringBuilder("");
		for (Property prop : associationMembers) {
			int upper = prop.getUpper();
			int lower = prop.getLower();
			String linkedClass = LinkTemplates.assocationEndPointDecl(prop.getType().getName(),
					LinkTemplates.formatAssociationRoleName(prop.getAssociation().getName(), prop.getName()),
					lower, upper);
			if (prop.isNavigable())
				source.append(linkedClass);
		}
		return source.toString();
	}

	String createLinkFunctionDeclarations(String className) {
		StringBuilder assocDeclerations = new StringBuilder("");
		for (Property member : associationMembers) {
			if (member.isNavigable()) {
				assocDeclerations.append(LinkTemplates.linkTemplateSpecializationDecl(className,
						member.getType().getName(), member.getName(), member.getAssociation().getName(),
						LinkTemplates.LinkFunctionType.Link));
				assocDeclerations.append(LinkTemplates.linkTemplateSpecializationDecl(className,
						member.getType().getName(), member.getName(), member.getAssociation().getName(),
						LinkTemplates.LinkFunctionType.Unlink));
			}

		}
		return  assocDeclerations.toString();
	}
	
	List<String> getAssociatedPropertyTypes() {
		List<String> assocNames = new ArrayList<String>();
		for(Property assoc : associationMembers) {
			assocNames.add(assoc.getType().getName());
		}
		
		return assocNames;
	}

}
