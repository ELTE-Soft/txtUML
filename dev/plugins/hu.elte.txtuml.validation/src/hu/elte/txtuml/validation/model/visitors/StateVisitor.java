package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.STATE_METHOD_PARAMETERS;
import static hu.elte.txtuml.validation.model.ModelErrors.UNKNOWN_STATE_METHOD;

import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;

import hu.elte.txtuml.validation.model.ProblemCollector;

public class StateVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_STATE_DECLARATIONS = new Class<?>[] { MethodDeclaration.class,
			SimpleName.class, SimpleType.class, Modifier.class, Javadoc.class };

	public static final Class<?>[] ALLOWED_INITIAL_STATE_DECLARATIONS = new Class<?>[] { SimpleName.class,
			SimpleType.class, Modifier.class, Javadoc.class };

	public StateVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (!node.getName().toString().equals("entry") && !node.getName().toString().equals("exit")) { //$NON-NLS-1$ //$NON-NLS-2$

			collector.report(UNKNOWN_STATE_METHOD.create(collector.getSourceInfo(), node));
			return false;
		}
		if (!node.parameters().isEmpty()) {
			collector.report(STATE_METHOD_PARAMETERS.create(collector.getSourceInfo(), node));
		}
		// TODO: validate body
		return false;
	}

}
