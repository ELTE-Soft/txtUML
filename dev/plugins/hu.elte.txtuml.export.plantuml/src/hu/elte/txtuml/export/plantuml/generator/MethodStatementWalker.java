package hu.elte.txtuml.export.plantuml.generator;

import org.eclipse.jdt.core.dom.ASTVisitor;

import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.InteractionExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.LifelineExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.MessageSendExporter;

import java.util.Stack;

import org.eclipse.jdt.core.dom.*;

public class MethodStatementWalker extends ASTVisitor {

	private PlantUmlGenerator generator;
	private Stack<BaseSeqdiagExporter<?>> expQueue;

	public MethodStatementWalker(PlantUmlGenerator generator) {

		expQueue = new Stack<BaseSeqdiagExporter<?>>();

		this.generator = generator;
	}

	@Override
	public boolean visit(TypeDeclaration statement) {
		InteractionExporter exp = new InteractionExporter(generator.getTargetFile(), generator);
		exp.visit(statement);

		expQueue.push(exp);

		return true;
	}

	@Override
	public void endVisit(TypeDeclaration statement) {
		BaseSeqdiagExporter<?> exp = expQueue.peek();
		
		if (exp instanceof InteractionExporter) {
			InteractionExporter cExp = (InteractionExporter) exp;

			if (cExp.endVisit(statement)) {
				expQueue.pop();
			}
		} else {
			// @TODO create exception class
		}
	}

	@Override
	public boolean visit(FieldDeclaration statement) {
		LifelineExporter exp = new LifelineExporter(generator.getTargetFile(), generator);

		exp.visit(statement);

		expQueue.push(exp);

		return true;
	}

	@Override
	public void endVisit(FieldDeclaration statement) {

		BaseSeqdiagExporter<? extends ASTNode> exp = expQueue.peek();

		if (exp instanceof LifelineExporter) {
			LifelineExporter cExp = (LifelineExporter) exp;

			if (cExp.endVisit(statement)) {
				expQueue.pop();
			}
		} else {
			// @TODO Exception case
		}
	}

	@Override
	public boolean visit(MethodInvocation statement) {
		MessageSendExporter exp = new MessageSendExporter(generator.getTargetFile(), generator);

		exp.visit(statement);
		if(exp.validElement(statement))
		{
			expQueue.push(exp);
		}
		
		return true;
	}

	@Override
	public void endVisit(MethodInvocation statement) {
		BaseSeqdiagExporter<? extends ASTNode> exp = expQueue.peek();
		
		if (exp instanceof MessageSendExporter) {
			MessageSendExporter cExp = (MessageSendExporter) exp;

			boolean endElement = cExp.endVisit(statement);
			
			if (endElement) {
				expQueue.pop();
			}
		} else {
			// @TODO Exception case
		}
	}
	
	@Override
	public void endVisit(Block statement)
	{
		if(statement.getParent() instanceof MethodDeclaration)
		{
			MethodDeclaration method = (MethodDeclaration)statement.getParent();
			if(method.getName().toString().equals("run"))
			{
				generator.deactivateAllLifelines();
			}
		}
	}
}
