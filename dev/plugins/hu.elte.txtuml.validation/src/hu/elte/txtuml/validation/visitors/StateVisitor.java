package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.state.StateMethodParameters;
import hu.elte.txtuml.validation.problems.state.UnknownStateMethod;

public class StateVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_STATE_DECLARATIONS = new Class<?>[] { MethodDeclaration.class,
			SimpleName.class, SimpleType.class, Modifier.class };

	public static final Class<?>[] ALLOWED_INITIAL_STATE_DECLARATIONS = new Class<?>[] { SimpleName.class,
			SimpleType.class, Modifier.class };

	public StateVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (!node.getName().toString().equals("entry") && !node.getName().toString().equals("exit")) { //$NON-NLS-1$ //$NON-NLS-2$

			collector.report(new UnknownStateMethod(collector.getSourceInfo(), node));
			return false;
		}
		if (!node.parameters().isEmpty()) {
			collector.report(new StateMethodParameters(collector.getSourceInfo(), node));
		}
		// TODO: validate body
		return false;
	}

}
