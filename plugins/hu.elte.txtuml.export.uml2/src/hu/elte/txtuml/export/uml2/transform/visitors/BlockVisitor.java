package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.WhileStatement;


public class BlockVisitor extends ASTVisitor {
	private MethodBodyImporter methodBodyImporter;
	
	public BlockVisitor(MethodBodyImporter methodBodyImporter) {
		this.methodBodyImporter = methodBodyImporter;
	}
	
	@Override
	public boolean visit(MethodInvocation methodInvocation) {
		
		if(SharedUtils.isActionCall(methodInvocation)) {
			this.methodBodyImporter.importAction(methodInvocation);
		}
		return false;
	}
	
	@Override
	public boolean visit(IfStatement statement) {
		
		this.methodBodyImporter.importControlStructure(statement);
		return false;
	}
	
	@Override
	public boolean visit(WhileStatement statement)
	{
		this.methodBodyImporter.importControlStructure(statement);
		return false;
	}
	
	@Override
	public boolean visit(EnhancedForStatement statement)
	{
		this.methodBodyImporter.importControlStructure(statement);
		return false;
	}
	
	@Override
	public boolean visit(ForStatement statement){

		this.methodBodyImporter.importControlStructure(statement);
		return false;
	}
}
