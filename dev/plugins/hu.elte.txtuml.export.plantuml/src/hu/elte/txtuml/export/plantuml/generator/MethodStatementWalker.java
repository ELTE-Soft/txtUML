package hu.elte.txtuml.export.plantuml.generator;

import org.eclipse.jdt.core.dom.ASTVisitor;

import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.InteractionExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.LifelineExporter;
import hu.elte.txtuml.export.plantuml.seqdiag.MessageSendExporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.*;

public class MethodStatementWalker extends ASTVisitor {

	private PlantUmlGenerator generator;
	private Stack<BaseSeqdiagExporter<?>> expQueue;
	private List<ASTNode> errors;

	public MethodStatementWalker(PlantUmlGenerator generator) {

		expQueue = new Stack<BaseSeqdiagExporter<?>>();

		this.generator = generator;

		errors = new ArrayList<ASTNode>();
	}

	@Override
	public boolean visit(TypeDeclaration statement) {
		InteractionExporter exp = new InteractionExporter(generator.getTargetFile(), generator);
		exp.visit(statement);

		if (exp.validElement(statement)) {
			expQueue.push(exp);
		}

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
			errors.add(statement);
		}
	}

	@Override
	public boolean visit(FieldDeclaration statement) {
		LifelineExporter exp = new LifelineExporter(generator.getTargetFile(), generator);

		exp.visit(statement);

		if (exp.validElement(statement)) {
			expQueue.push(exp);
		}

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
			errors.add(statement);
		}
	}

	@Override
	public boolean visit(MethodInvocation statement) {
		MessageSendExporter exp = new MessageSendExporter(generator.getTargetFile(), generator);

		exp.visit(statement);

		if (exp.validElement(statement)) {
			expQueue.push(exp);
		}

		return true;
	}

	@Override
	public void endVisit(MethodInvocation statement) {
		BaseSeqdiagExporter<? extends ASTNode> exp = expQueue.peek();
		String invoc = statement.resolveMethodBinding().getDeclaringClass().getQualifiedName();

		if (invoc.equals("hu.elte.txtuml.api.model.Action")) {
			return;
		}

		if (exp instanceof MessageSendExporter) {
			MessageSendExporter cExp = (MessageSendExporter) exp;

			boolean endElement = cExp.endVisit(statement);

			if (endElement) {
				expQueue.pop();
			}
		} else {
			errors.add(statement);
		}
	}

	@Override
	public void endVisit(Block statement) {
		if (statement.getParent() instanceof MethodDeclaration) {
			MethodDeclaration method = (MethodDeclaration) statement.getParent();
			if (method.getName().toString().equals("run")) {
				generator.deactivateAllLifelines();
			}
		}
	}

	public List<String> getErrors() {
		ArrayList<String> errList = new ArrayList<String>();
		for (ASTNode error : errors) {
			errList.add("Error! Couldn't parse the following statement: " + error.toString() + "\n");
		}

		if (!expQueue.isEmpty()) {
			errList.add("Warning! Some expressions where left unparsed/unclosed \n");
		}

		return errList;
	}
}
