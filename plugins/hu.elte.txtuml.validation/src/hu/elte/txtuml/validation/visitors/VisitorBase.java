package hu.elte.txtuml.validation.visitors;

import hu.elte.txtuml.validation.ProblemCollector;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class VisitorBase extends ASTVisitor {

	protected ProblemCollector collector;
	
	public VisitorBase(ProblemCollector collector) {
		this.collector = collector;
	}

}
