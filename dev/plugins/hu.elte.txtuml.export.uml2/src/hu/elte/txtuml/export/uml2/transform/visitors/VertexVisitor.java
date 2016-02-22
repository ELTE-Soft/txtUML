package hu.elte.txtuml.export.uml2.transform.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.transform.exporters.VertexExporter;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

/**
 * @throws RuntimeException
 *             When applied to an <code>ASTNode</code>, methods of this visitor
 *             may throw this kind of exception.
 */
public class VertexVisitor extends ASTVisitor {

	private final VertexExporter vertexExporter;
	private final TypeDeclaration ownerDeclaration;

	public VertexVisitor(VertexExporter regionElementExporter, TypeDeclaration ownerDeclaration) {
		this.vertexExporter = regionElementExporter;
		this.ownerDeclaration = ownerDeclaration;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if (ElementTypeTeller.isVertex(typeDeclaration) && !typeDeclaration.equals(this.ownerDeclaration)) {
			vertexExporter.exportVertex(typeDeclaration);
			return false;
		}
		return true;
	}
}
