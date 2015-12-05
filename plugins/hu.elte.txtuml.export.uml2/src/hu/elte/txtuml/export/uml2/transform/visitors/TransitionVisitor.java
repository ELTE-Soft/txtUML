package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.exporters.TransitionExporter;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TransitionVisitor extends ASTVisitor {

	private final TransitionExporter transitionExporter;
	private final TypeDeclaration ownerDeclaration;

	public TransitionVisitor(TransitionExporter transitionExporter,
			TypeDeclaration ownerDeclaration) {
		this.transitionExporter = transitionExporter;
		this.ownerDeclaration = ownerDeclaration;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if (ElementTypeTeller.isTransition(typeDeclaration)
				&& !typeDeclaration.equals(this.ownerDeclaration)) {
			transitionExporter.exportTransition(typeDeclaration);
			return false;
		} else if (ElementTypeTeller.isState(typeDeclaration)
				&& !typeDeclaration.equals(this.ownerDeclaration)) {
			return false;
		}
		return true;
	}
}
