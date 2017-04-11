package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.UNKNOWN_CLASS_IN_STATE;

import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.model.ProblemCollector;

public class CompositeStateVisitor extends StateVisitor {

	public static final Class<?>[] ALLOWED_COMPOSITE_STATE_DECLARATIONS = new Class<?>[] { TypeDeclaration.class,
			MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class, Javadoc.class };

	public CompositeStateVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		if (!ElementTypeTeller.isVertex(node) && !ElementTypeTeller.isTransition(node)) {
			collector.report(UNKNOWN_CLASS_IN_STATE.create(collector.getSourceInfo(), node));
		} else {
			handleStateMachineElements(node);
		}
		return false;
	}

}
