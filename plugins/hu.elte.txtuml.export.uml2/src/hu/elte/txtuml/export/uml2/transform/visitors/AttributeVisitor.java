package hu.elte.txtuml.export.uml2.transform.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.transform.exporters.AttributeExporter;

public class AttributeVisitor extends ASTVisitor {

	private final AttributeExporter attributeExporter;
	private final TypeDeclaration classifierDeclaration;

	public AttributeVisitor(AttributeExporter attributeExporter, TypeDeclaration classifierDeclaration) {
		this.attributeExporter = attributeExporter;
		this.classifierDeclaration = classifierDeclaration;
	}

	@Override
	public boolean visit(FieldDeclaration fieldDeclaration) {
		if (fieldDeclaration.getParent().equals(this.classifierDeclaration)) {
			this.attributeExporter.exportClassifierAttributesFromFieldDeclaration(fieldDeclaration);
		}
		return false;
	}
}
