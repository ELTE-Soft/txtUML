package hu.elte.txtuml.validation.sequencediagram.visitors;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.ValidationErrors;

public class SequenceDiagramVisitor extends ASTVisitor {

	protected ProblemCollector collector;

	public SequenceDiagramVisitor(ProblemCollector collector) {
		this.collector = collector;
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		Utils.checkSequenceDiagram(collector, elem);
		return true;
	}

	@Override
	public boolean visit(FieldDeclaration elem) {
		Utils.checkFieldDeclaration(collector, elem);
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		return !elem.getName().getFullyQualifiedName().equals("initialize");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(Block node) {
		List<Statement> statements = (List<Statement>) node.statements();
		boolean isLeaf = !statements.stream().anyMatch(stm -> stm instanceof WhileStatement
				|| stm instanceof IfStatement || stm instanceof ForStatement || stm instanceof DoStatement);
		if (!isLeaf) {
			return true;
		}

		List<MethodInvocation> methodInvocations = statements.stream().map(stm -> ((Statement) stm))
				.filter(stm -> stm instanceof ExpressionStatement).map(stm -> (ExpressionStatement) stm)
				.map(ExpressionStatement::getExpression).filter(expr -> expr instanceof MethodInvocation)
				.map(expr -> (MethodInvocation) expr).collect(toList());

		boolean containsSend = methodInvocations.stream().anyMatch(this::isSendInvocation);
		if (!containsSend) {
			if (!methodInvocations.isEmpty()) {
				// check other method calls in this block instead
				return true;
			}
			collector.report(ValidationErrors.SEND_EXPECTED.create(collector.getSourceInfo(), node));
		}
		return true;
	}

	private boolean isSendInvocation(MethodInvocation expression) {
		return (expression.resolveMethodBinding().getName().equals("send")
				|| expression.resolveMethodBinding().getName().equals("fromActor"))
				&& expression.resolveMethodBinding().getDeclaringClass().getQualifiedName()
						.equals(Sequence.class.getCanonicalName());
	}

}
