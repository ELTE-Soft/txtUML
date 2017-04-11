package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.WRONG_COMPOSITION_ENDS;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;

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
		if (containerMembers != 1 || partMembers != 1) {
			collector.report(WRONG_COMPOSITION_ENDS.create(collector.getSourceInfo(), root));
		}
	}

}
