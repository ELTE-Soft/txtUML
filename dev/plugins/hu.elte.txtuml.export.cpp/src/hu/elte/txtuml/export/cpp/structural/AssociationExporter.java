package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

class AssociationExporter {
	private List<Property> associationMembers;
	private boolean ownAssociations;
	
	AssociationExporter() {
		associationMembers = new ArrayList<Property>();
		ownAssociations = false;
	}
	
	AssociationExporter(EList<Property> properites) {
		associationMembers = new ArrayList<Property>();
		exportAssocations(properites);
	}

	void exportAssocations(EList<Property> properites) {
		for (Property prop : properites) {
			if (prop.getAssociation() != null) {
				associationMembers.add(prop);
				ownAssociations = true;
			}
		}
	}
	
	boolean ownAssociation() {return ownAssociations;}

	String createAssociationMemeberDeclerationsCode() {
		StringBuilder source = new StringBuilder("");
		for (Property prop : associationMembers) {
			int upper = prop.getUpper();
			int lower = prop.getLower();
			String linkedClass = GenerationTemplates.assocationDecl(prop.getType().getName(),
					GenerationTemplates.formatAssociationRoleName(prop.getAssociation().getName(), prop.getName()),
					lower, upper);
			if (prop.isNavigable())
				source.append(linkedClass);
		}
		return source.toString();
	}

	String createLinkFunctionDeclerations(String className) {
		StringBuilder assocDeclerations = new StringBuilder("");
		for (Property member : associationMembers) {
			if (member.isNavigable()) {
				assocDeclerations.append(GenerationTemplates.linkTemplateSpecializationDecl(className,
						member.getType().getName(), member.getName(), member.getAssociation().getName(),
						GenerationTemplates.LinkFunctionType.Link));
				assocDeclerations.append(GenerationTemplates.linkTemplateSpecializationDecl(className,
						member.getType().getName(), member.getName(), member.getAssociation().getName(),
						GenerationTemplates.LinkFunctionType.Unlink));
			}

		}
		return GenerationTemplates.cppInclude(GenerationTemplates.AssociationsStructuresHreaderName)
				+ assocDeclerations.toString();
	}
	
	List<String> getAssociatedPropertyNames() {
		List<String> assocNames = new ArrayList<String>();
		for(Property assoc : associationMembers) {
			assocNames.add(assoc.getName());
		}
		
		return assocNames;
	}

}
