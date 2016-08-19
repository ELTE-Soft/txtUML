package hu.elte.txtuml.export.plantuml.generator;

import org.eclipse.jdt.core.dom.ASTVisitor;

import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.InteractionExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.LifelineExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.MessageSendExporter;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.core.dom.*;

public class MethodStatementWalker extends ASTVisitor {

	private PlantUmlGenerator generator;
	private ConcurrentLinkedQueue<BaseSeqdiagExporter<?>> expQueue;

	public MethodStatementWalker(PlantUmlGenerator generator) {

		expQueue = new ConcurrentLinkedQueue<BaseSeqdiagExporter<?>>();

		this.generator = generator;
	}

	@Override
	public boolean visit(TypeDeclaration statement) {
		InteractionExporter exp = new InteractionExporter(generator.getTargetFile());
		exp.visit(statement);

		expQueue.add(exp);

		return true;
	}

	@Override
	public void endVisit(TypeDeclaration statement) {
		BaseSeqdiagExporter<?> exp = expQueue.peek();

		if (exp instanceof InteractionExporter) {
			InteractionExporter cExp = (InteractionExporter) exp;

			if (cExp.endVisit(statement)) {
				expQueue.poll();
			}
		} else {
			// @TODO create exception class
		}
	}

	@Override
	public boolean visit(FieldDeclaration statement) {
		LifelineExporter exp = new LifelineExporter(generator.getTargetFile());

		exp.visit(statement);

		expQueue.add(exp);

		return true;
	}

	@Override
	public void endVisit(FieldDeclaration statement) {

		BaseSeqdiagExporter<? extends ASTNode> exp = expQueue.peek();

		if (exp instanceof LifelineExporter) {
			LifelineExporter cExp = (LifelineExporter) exp;

			if (cExp.endVisit(statement)) {
				expQueue.poll();
			}
		} else {
			// @TODO Exception case
		}
	}

	@Override
	public boolean visit(MethodInvocation statement) {
		MessageSendExporter exp = new MessageSendExporter(generator.getTargetFile());

		exp.visit(statement);

		expQueue.add(exp);
		return true;
	}

	@Override
	public void endVisit(MethodInvocation statement) {
		BaseSeqdiagExporter<? extends ASTNode> exp = expQueue.peek();

		if (exp instanceof MessageSendExporter) {
			MessageSendExporter cExp = (MessageSendExporter) exp;

			if (cExp.endVisit(statement)) {
				expQueue.poll();
			}
		} else {
			// @TODO Exception case
		}
	}
}
