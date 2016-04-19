package hu.elte.txtuml.refactoring.unfoldincoming;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import hu.elte.txtuml.refactoring.Type;
import hu.elte.txtuml.refactoring.unfoldtransitions.UnfoldTransitionsRefactoring;

public class UnfoldIncomingRefactoring extends UnfoldTransitionsRefactoring {
	public static final String NAME = "Unfold incoming transitions";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected String getEndPointName() {
		return "target";
	}

	@Override
	protected hu.elte.txtuml.refactoring.Type getOccurrenceAnnotationType() {
		return Type.TO_ANNOTATION;
	}

	@Override
	protected hu.elte.txtuml.refactoring.Type getOppositeAnnotationType() {
		return Type.FROM_ANNOTATION;
	}

	@Override
	protected String getNameOfNewTransition(IType transition, IType state) throws JavaModelException {
		String oppositeEnd = (String) transition.getAnnotation(Type.TO_ANNOTATION.get()).getMemberValuePairs()[0].getValue();
		return oppositeEnd.replace('.', '_') + "_" + state.getElementName();
	}	
}