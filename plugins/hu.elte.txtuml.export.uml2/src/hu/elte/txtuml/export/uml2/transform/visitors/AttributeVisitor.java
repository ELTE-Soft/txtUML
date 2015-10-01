package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.importers.AttributeImporter;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AttributeVisitor extends ASTVisitor {
	private AttributeImporter attributeImporter;
	private TypeDeclaration classifierDeclaration;
	
	public AttributeVisitor(AttributeImporter attributeImporter, TypeDeclaration classifierDeclaration) {
		super();
		this.attributeImporter = attributeImporter;
		this.classifierDeclaration = classifierDeclaration;
	}
	
	@Override
	public boolean visit(FieldDeclaration fieldDeclaration) {
		if(fieldDeclaration.getParent().equals(this.classifierDeclaration)) {
			this.attributeImporter.importClassifierAttributesFromFieldDeclaration(fieldDeclaration);
		}
		return false;
	}
}
