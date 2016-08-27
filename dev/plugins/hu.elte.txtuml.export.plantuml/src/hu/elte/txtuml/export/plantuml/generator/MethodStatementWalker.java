package hu.elte.txtuml.export.plantuml.generator;

import org.eclipse.jdt.core.dom.ASTVisitor;

import hu.elte.txtuml.export.plantuml.seqdiag.BaseSeqdiagExporter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class MethodStatementWalker extends ASTVisitor {

	private PlantUmlGenerator generator;
	private List<ASTNode> errors;

	public MethodStatementWalker(PlantUmlGenerator generator) {
		super();
		this.generator = generator;
		errors = new ArrayList<ASTNode>();
	}

	@Override
	public boolean preVisit2(ASTNode node) {

		BaseSeqdiagExporter<?> exp = BaseSeqdiagExporter.createExporter(node, generator);

		if (exp != null) {
			return exp.visit(node);
		}

		return true;
	};

	@Override
	public void postVisit(ASTNode node) {
		BaseSeqdiagExporter<?> expt = BaseSeqdiagExporter.createExporter(node, generator);

		if (generator.hasPreprocessedStatement() && expt != null && !expt.skippedStatement(node)) {
			BaseSeqdiagExporter<?> exp = generator.postProcessingStatement();

			if (expt.getClass().isInstance(exp)) {
				exp.endVisit(node);
			} else if (!expt.getClass().isInstance(exp) && !expt.skippedStatement(node)) {
				errors.add(node);
			}
		}
	}

	public List<String> getErrors() {
		ArrayList<String> errList = new ArrayList<String>();
		for (ASTNode error : errors) {
			errList.add("Error! Couldn't parse the following statement: " + error.toString() + "\n");
		}

		return errList;
	}
}
