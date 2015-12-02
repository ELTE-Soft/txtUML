package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.StateMethodNonVoidReturn;
import hu.elte.txtuml.validation.problems.StateMethodParameters;
import hu.elte.txtuml.validation.problems.UnknownStateMethod;

public class StateVisitor extends VisitorBase {

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
