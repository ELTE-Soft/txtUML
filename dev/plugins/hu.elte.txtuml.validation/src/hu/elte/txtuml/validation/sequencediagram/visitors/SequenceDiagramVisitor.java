package hu.elte.txtuml.validation.sequencediagram.visitors;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

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
		Utils.checkField(collector, elem);
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		Utils.checkMethod(collector, elem);
		return elem.getName().getFullyQualifiedName().equals("run");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(Block node) {
		List<Statement> statements = (List<Statement>) node.statements();
		Stream<ExpressionStatement> expressions = statements.stream().map(stm -> ((Statement) stm))
				.filter(stm -> stm instanceof ExpressionStatement).map(stm -> (ExpressionStatement) stm);
		boolean isError = !expressions.anyMatch(expr -> checkSendInExpression(expr.getExpression()));
		if (isError) {
			collector.report(ValidationErrors.INVALID_SUPERCLASS.create(collector.getSourceInfo(), node));
		}
		return true;
	}

	private boolean checkSendInExpression(Expression expression) {
		if (expression.getNodeType() != ASTNode.METHOD_INVOCATION) {
			return false;
		}
		MethodInvocation mi = ((MethodInvocation) expression);

		return mi.resolveMethodBinding().getName().equals("send") && mi.resolveMethodBinding().getDeclaringClass()
				.getQualifiedName().equals(Sequence.class.getCanonicalName());
	}

}
