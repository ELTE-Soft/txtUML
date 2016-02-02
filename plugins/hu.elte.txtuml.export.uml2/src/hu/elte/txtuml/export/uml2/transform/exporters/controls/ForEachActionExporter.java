package hu.elte.txtuml.export.uml2.transform.exporters.controls;

import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;

public class ForEachActionExporter extends AbstractLoopExporter {

	public ForEachActionExporter(BlockExporter blockExporter) {
		super(blockExporter);
	}

	public void exportForEachStatement(Statement statement) {
		EnhancedForStatement forState = (EnhancedForStatement) statement;

		SingleVariableDeclaration parameter = forState.getParameter();
		Expression expression = forState.getExpression();
		Statement forEachBody = forState.getBody();
		
		Type exprType = blockExporter.getTypeExporter().exportType(expression.resolveTypeBinding());
		Type paramType = blockExporter.getTypeExporter().exportType(parameter.getType());
		Variable exprVar = blockExporter.createVariable("foreach_expr_" + expression.hashCode(), exprType);
		Variable indexVar = blockExporter.createVariable(parameter.getName().getIdentifier(), paramType);

		throw new RuntimeException("For-each loop is not supported yet");
		
		// TODO: generate indexed for loop
	}

}
