package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.ForActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.ForEachActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.IfActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.controls.WhileActionExporter;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * TODO BlockVisitor
 * <p>
 * {@link org.eclipse.jdt.core.dom.AssertStatement},
 * {@link org.eclipse.jdt.core.dom.BreakStatement},
 * {@link org.eclipse.jdt.core.dom.ContinueStatement},
 * {@link org.eclipse.jdt.core.dom.DoStatement},
 * {@link org.eclipse.jdt.core.dom.EmptyStatement},
 * {@link org.eclipse.jdt.core.dom.SwitchCase},
 * {@link org.eclipse.jdt.core.dom.SwitchStatement},
 * {@link org.eclipse.jdt.core.dom.SynchronizedStatement},
 * {@link org.eclipse.jdt.core.dom.ThrowStatement},
 * {@link org.eclipse.jdt.core.dom.TryStatement},
 * {@link org.eclipse.jdt.core.dom.TypeDeclarationStatement},
 */
public class BlockVisitor extends ASTVisitor {

	private final BlockExporter blockExporter;

	public BlockVisitor(BlockExporter blockExporter) {
		this.blockExporter = blockExporter;
	}

	@Override
	public boolean visit(Block node) {
		BlockExporter.exportBlock(blockExporter, node, "block");
		return false;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		// TODO ConstructorInvocation
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		new ForEachActionExporter(blockExporter).exportForEachStatement(node);
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
		blockExporter.getExpressionExporter().exportReturnStatement(
				node.getExpression());
		return false;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// TODO SuperConstructorInvocation
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		blockExporter.getExpressionExporter().exportVariableDeclaration(
				node.getType(), node);
		return false;
	}

	@Override
	public boolean visit(WhileStatement node) {
		new WhileActionExporter(blockExporter).exportWhileStatement(node);
		return false;
	}

}
