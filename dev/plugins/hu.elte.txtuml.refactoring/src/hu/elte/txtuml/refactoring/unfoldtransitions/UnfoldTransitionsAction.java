package hu.elte.txtuml.refactoring.unfoldtransitions;

import org.eclipse.jdt.core.JavaModelException;

import hu.elte.txtuml.refactoring.BaseActionDelegate;

public abstract class UnfoldTransitionsAction extends BaseActionDelegate {
	@Override
	protected boolean areRegular() throws JavaModelException {
		return elements.size() == 1 && super.areRegular();
	}
}