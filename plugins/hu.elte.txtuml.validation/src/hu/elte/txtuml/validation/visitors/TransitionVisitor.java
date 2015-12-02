package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodNonVoidReturn;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodParameters;
import hu.elte.txtuml.validation.problems.transition.UnknownTransitionMethod;

public class TransitionVisitor extends VisitorBase {

	public TransitionVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		boolean invalidName = !node.getName().toString().equals("effect");
		collector.setProblemStatus(invalidName, new UnknownTransitionMethod(collector.getSourceInfo(), node));
		if (invalidName) {
			return false;
		}
		collector.setProblemStatus(!Utils.isVoid(node.getReturnType2()),
				new TransitionMethodNonVoidReturn(collector.getSourceInfo(), node.getReturnType2()));
		collector.setProblemStatus(!node.parameters().isEmpty(),
				new TransitionMethodParameters(collector.getSourceInfo(), node));
		// TODO: validate body
		return false;
	}

}
