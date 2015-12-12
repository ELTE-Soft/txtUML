package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.state.UnknownClassInState;

public class CompositeStateVisitor extends StateVisitor {

	public static final Class<?>[] ALLOWED_COMPOSITE_STATE_DECLARATIONS = new Class<?>[] { TypeDeclaration.class,
			MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class };

	public CompositeStateVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		if (!ElementTypeTeller.isVertex(node) && !ElementTypeTeller.isTransition(node)) {
			collector.setProblemStatus(new UnknownClassInState(collector.getSourceInfo(), node));
		} else {
			handleStateMachineElements(node);
		}
		return false;
	}

}
