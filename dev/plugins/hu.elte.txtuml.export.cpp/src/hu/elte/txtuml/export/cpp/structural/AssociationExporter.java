package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;

class AssociationExporter {
	private List<Property> associationMembers;
	private boolean ownAssociations;
	
	AssociationExporter() {
		associationMembers = new ArrayList<Property>();
		ownAssociations = false;
	}
	
	AssociationExporter(EList<Property> properites) {
		associationMembers = new ArrayList<Property>();
		exportAssociations(properites);
	}

	void exportAssociations(EList<Property> properites) {
		for (Property prop : properites) {
			if (prop.getAssociation() != null) {
				associationMembers.add(prop);
				ownAssociations = true;
			}
		}
	}
	
	boolean ownAssociation() {return ownAssociations;}

	String createAssociationMemberDeclarationsCode() {
		StringBuilder source = new StringBuilder("");
		for (Property prop : associationMembers) {
			int upper = prop.getUpper();
			int lower = prop.getLower();
			String linkedClass = LinkTemplates.assocationDecl(prop.getType().getName(),
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
		return PrivateFunctionalTemplates.include(LinkTemplates.AssociationsStructuresHreaderName)
				+ assocDeclerations.toString();
	}
	
	List<String> getAssociatedPropertyTypes() {
		List<String> assocNames = new ArrayList<String>();
		for(Property assoc : associationMembers) {
			assocNames.add(assoc.getType().getName());
		}
		
		return assocNames;
	}

}
