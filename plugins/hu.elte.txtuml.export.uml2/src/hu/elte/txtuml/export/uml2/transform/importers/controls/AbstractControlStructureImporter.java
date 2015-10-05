package hu.elte.txtuml.export.uml2.transform.importers.controls;

import hu.elte.txtuml.export.uml2.transform.importers.AbstractActionCodeImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;
import hu.elte.txtuml.export.uml2.transform.visitors.BlockVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.ExpressionVisitor;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;

public abstract class AbstractControlStructureImporter extends AbstractActionCodeImporter {
	
	/**
	 * 
	 * @param methodBodyImporter
	 * @param importedModel
	 */
	public AbstractControlStructureImporter(MethodBodyImporter methodBodyImporter, Model importedModel) {
		super(methodBodyImporter,importedModel);
	}
	
	protected Type getExpressionType(Expression exp)
	{
		ITypeBinding bind = exp.resolveTypeBinding();
		
		Type retType = this.importedModel.getOwnedType(bind.getName());
		
		if(retType == null && bind.getTypeArguments().length > 0)
		{
			retType = this.importedModel.getOwnedType(bind.getTypeArguments()[0].getName());
		}
		
		return retType;
	}
	
	protected String getExpressionString(Expression exp)
	{
		ExpressionVisitor vis = new ExpressionVisitor();
		exp.accept(vis);
		
		return vis.getImportedExpression();
	}
	
	protected MethodBodyImporter importBodyFromStatement(Statement statement)
	{
		Block block = (Block) statement;
		
		MethodBodyImporter imp = new MethodBodyImporter(this.activity,this.typeImporter, this.importedModel);
		
		BlockVisitor vis = new BlockVisitor(imp);
		
		if(block != null)
		{
			block.accept(vis);
		}
		
		return imp;
	}
}
