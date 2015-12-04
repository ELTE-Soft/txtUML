package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodNonVoidReturn;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodParameters;
import hu.elte.txtuml.validation.problems.transition.UnknownTransitionMethod;

public class TransitionVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_TRANSITION_DECLARATIONS = new Class<?>[] { MethodDeclaration.class,
			SimpleName.class, SimpleType.class, Modifier.class, Annotation.class };

	public TransitionVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		boolean invalidName = !ElementTypeTeller.isEffect(node) && !ElementTypeTeller.isGuard(node);
		collector.setProblemStatus(invalidName, new UnknownTransitionMethod(collector.getSourceInfo(), node));
		if (invalidName) {
			return false;
		}
		if (ElementTypeTeller.isEffect(node)) {
			collector.setProblemStatus(!Utils.isVoid(node.getReturnType2()),
					new TransitionMethodNonVoidReturn(collector.getSourceInfo(), node.getReturnType2()));
		} else {
			collector.setProblemStatus(!Utils.isBoolean(node.getReturnType2()),
					new TransitionMethodNonVoidReturn(collector.getSourceInfo(), node.getReturnType2()));
		}
		collector.setProblemStatus(!node.parameters().isEmpty(),
				new TransitionMethodParameters(collector.getSourceInfo(), node));
		// TODO: validate body
		return false;
	}

}
