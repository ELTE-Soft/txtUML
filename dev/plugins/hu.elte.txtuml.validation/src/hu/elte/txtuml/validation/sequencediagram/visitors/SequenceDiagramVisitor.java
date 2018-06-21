package hu.elte.txtuml.validation.sequencediagram.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.validation.common.ProblemCollector;

/**
 * Visitor for sequence diagram description validation.
 */
public class SequenceDiagramVisitor extends ASTVisitor {

	protected ProblemCollector collector;

	public SequenceDiagramVisitor(ProblemCollector collector) {
		this.collector = collector;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		Utils.acceptSequenceDiagrams(node, this);
		return false;
	}

	@Override
	public boolean visit(FieldDeclaration elem) {
		Utils.checkFieldDeclaration(collector, elem);
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		return elem.getName().getFullyQualifiedName().equals("run");
	}

	@Override
	public boolean visit(MethodInvocation elem) {
		Utils.checkInvalidActionCall(elem, collector);
		return false;
	}

}
