package hu.elte.txtuml.export.uml2.transform.visitors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Operation;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.DoWhileActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.ForActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.ForEachActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.IfActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.WhileActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

public class BlockVisitor extends ASTVisitor {

	private final BlockExporter<? extends ActivityNode> blockExporter;

	public BlockVisitor(BlockExporter<? extends ActivityNode> blockExporter) {
		this.blockExporter = blockExporter;
	}

	@Override
	public boolean visit(Block node) {
		BlockExporter.exportBlock(blockExporter, node, "block");
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		new ForEachActionExporter(blockExporter).exportForEachStatement(node);
		return false;
	}

	@Override
	public boolean visit(EmptyStatement node) {
		blockExporter.createSequenceNode(";");
		return false;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		blockExporter.getExpressionExporter().export(node.getExpression());
		return false;
	}

	@Override
	public boolean visit(ForStatement node) {
		new ForActionExporter(blockExporter).exportForStatement(node);
		return false;
	}

	@Override
	public boolean visit(IfStatement node) {
		new IfActionExporter(blockExporter).exportIfStatement(node);
		return false;
	}

	@Override
	public boolean visit(LabeledStatement node) {
		return true;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		blockExporter.getExpressionExporter().exportReturnStatement(node.getExpression());
		return false;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		IMethodBinding ctorBinding = node.resolveConstructorBinding();
		List<?> arguments = node.arguments();
		createConstructorCall(ctorBinding, arguments);
		return false;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		List<?> arguments = node.arguments();
		IMethodBinding ctorBinding = node.resolveConstructorBinding();
		createConstructorCall(ctorBinding, arguments);
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		blockExporter.getExpressionExporter().exportVariableDeclaration(node.getType().resolveBinding(), node);
		return false;
	}

	private void createConstructorCall(IMethodBinding ctorBinding, List<?> arguments) {
		List<Expr> args = new LinkedList<>();
		ExpressionExporter<? extends ActivityNode> expressionExporter = blockExporter.getExpressionExporter();
		arguments.forEach(a -> args.add(expressionExporter.export((Expression) a)));
		Operation calledCtor = blockExporter.getTypeExporter().exportMethodAsOperation(ctorBinding, args);
		expressionExporter.createCallOperationAction(calledCtor, null, args);
	}

	@Override
	public boolean visit(WhileStatement node) {
		new WhileActionExporter(blockExporter).exportWhileStatement(node);
		return false;
	}

	@Override
	public boolean visit(DoStatement node) {
		new DoWhileActionExporter(blockExporter).exportDoWhileStatement(node);
		return false;
	}

}
