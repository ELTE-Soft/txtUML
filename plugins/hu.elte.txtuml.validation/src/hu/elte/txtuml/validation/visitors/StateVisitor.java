package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.state.StateMethodNonVoidReturn;
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
		boolean invalidName = !node.getName().toString().equals("entry") && !node.getName().toString().equals("exit");
		collector.setProblemStatus(invalidName, new UnknownStateMethod(collector.getSourceInfo(), node));
		if (invalidName) {
			return false;
		}
		collector.setProblemStatus(!Utils.isVoid(node.getReturnType2()),
				new StateMethodNonVoidReturn(collector.getSourceInfo(), node.getReturnType2()));
		collector.setProblemStatus(!node.parameters().isEmpty(),
				new StateMethodParameters(collector.getSourceInfo(), node));
		// TODO: validate body
		return false;
	}

}
