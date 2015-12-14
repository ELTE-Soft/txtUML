package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.WrongCompositionEnds;

public class CompositionVisitor extends VisitorBase {

	public CompositionVisitor(ProblemCollector collector) {
		super(collector);
	}

	private int containerMembers = 0;
	private int partMembers = 0;

	@Override
	public boolean visit(TypeDeclaration node) {
		for (Object decl : node.bodyDeclarations()) {
			((BodyDeclaration) decl).accept(new VisitorBase(collector) {

				@Override
				public boolean visit(TypeDeclaration node) {
					if (ElementTypeTeller.isContainer(node)) {
						++containerMembers;
					} else {
						++partMembers;
					}
					return false;
				}

			});
		}
		return false;
	}

	public void endVisit(TypeDeclaration node) {
		if (containerMembers != 1 || partMembers != 1) {
			collector.setProblemStatus(true,
					new WrongCompositionEnds(collector.getSourceInfo(), node));
		}
	}

}
