package hu.elte.txtuml.refactoring.unfoldoutgoing;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import hu.elte.txtuml.refactoring.Type;
import hu.elte.txtuml.refactoring.unfoldtransitions.UnfoldTransitionsRefactoring;

public class UnfoldOutgoingRefactoring extends UnfoldTransitionsRefactoring {
	public static final String NAME = "Unfold outgoing transitions";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected String getEndPointName() {
		return "source";
	}

	@Override
	protected hu.elte.txtuml.refactoring.Type getOccurrenceAnnotationType() {
		return Type.FROM_ANNOTATION;
	}

	@Override
	protected hu.elte.txtuml.refactoring.Type getOppositeAnnotationType() {
		return Type.TO_ANNOTATION;
	}

	@Override
	protected String getNameOfNewTransition(IType transition, IType state) throws JavaModelException {
		String oppositeEnd = (String) transition.getAnnotation(Type.TO_ANNOTATION.get()).getMemberValuePairs()[0].getValue();
		return state.getElementName() + "_" + oppositeEnd.replace('.', '_');
	}
}