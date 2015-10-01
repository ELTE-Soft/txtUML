package hu.elte.txtuml.export.uml2.transform.importers.actions;

import hu.elte.txtuml.export.uml2.transform.importers.AbstractActionCodeImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;
import hu.elte.txtuml.export.uml2.transform.visitors.ExpressionVisitor;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;

/**
 * Abstract class for action importer common functions and fields ex.: methodBodyImporter, activity and method argument getters!s
 * @author Turi Zolt�n
 * 
 */
public abstract class AbstractActionImporter extends AbstractActionCodeImporter {

	protected static int DECISION_NODE_COUNT = 0;
	
	/**
	 * making sure that the class cannot be created, so constructor is protected
	 * @param methodBodyImporter
	 * @param importedModel
	 */
	protected AbstractActionImporter(MethodBodyImporter methodBodyImporter, Model importedModel) 
	{	
		super(methodBodyImporter,importedModel);
	}

	protected Type obtainTypeOfNthArgument(MethodInvocation methodInvocation, int n) {
		Expression argExpr = (Expression) methodInvocation.arguments().get(n - 1);
		ITypeBinding type = argExpr.resolveTypeBinding();
		
		Type retType = this.importedModel.getOwnedType(type.getName());
		
		if(retType == null)
		{
			retType = this.importedModel.getOwnedType(type.getTypeArguments()[0].getName());
		}
		
		return retType;
	}
	
	protected String obtainExpressionOfNthArgument(MethodInvocation methodInvocation,int n){
		Expression expr = (Expression) methodInvocation.arguments().get(n-1);
		ExpressionVisitor expressionVisitor = new ExpressionVisitor();
		expr.accept(expressionVisitor);
		return expressionVisitor.getImportedExpression();
	}
}
