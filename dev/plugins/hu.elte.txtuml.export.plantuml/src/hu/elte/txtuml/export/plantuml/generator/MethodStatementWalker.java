package hu.elte.txtuml.export.plantuml.generator;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Statement;

public class MethodStatementWalker extends ASTVisitor {
	
	public boolean accept(Statement statement)
	{
		
		return false;
	}
}
