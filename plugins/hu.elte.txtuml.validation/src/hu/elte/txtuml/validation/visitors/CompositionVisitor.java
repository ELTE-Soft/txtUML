package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.association.WrongCompositionEnds;

public class CompositionVisitor extends VisitorBase {

	private TypeDeclaration root;

	public CompositionVisitor(TypeDeclaration root, ProblemCollector collector) {
		super(collector);
		this.root = root;
	}

	private int containerMembers = 0;
	private int partMembers = 0;

	@Override
	public boolean visit(TypeDeclaration node) {
		if (ElementTypeTeller.isContainer(node)) {
			++containerMembers;
		} else {
			++partMembers;
		}
		return false;
	}

	public void check() {
		boolean invalid = containerMembers != 1 || partMembers != 1;
		collector.setProblemStatus(invalid, new WrongCompositionEnds(collector.getSourceInfo(), root));
	}

}
