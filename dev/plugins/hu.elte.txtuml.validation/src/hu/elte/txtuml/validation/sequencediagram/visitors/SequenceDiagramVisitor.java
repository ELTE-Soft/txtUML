package hu.elte.txtuml.validation.sequencediagram.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.ValidationErrors;

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
		boolean isActionCall;
		try {
			isActionCall = elem.resolveMethodBinding().getDeclaringClass().getQualifiedName()
					.equals(Action.class.getCanonicalName());
		} catch (NullPointerException ex) {
			isActionCall = false;
		}
		if (isActionCall) {
			collector.report(ValidationErrors.INVALID_ACTION_CALL.create(collector.getSourceInfo(), elem));
		}
		return false;
	};

	@Override
	public boolean visit(Block node) {
		Utils.checkSendExists(collector, node, this);
		return true;
	}

}
