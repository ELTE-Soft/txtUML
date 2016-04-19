package hu.elte.txtuml.refactoring.unfoldoutgoing;

import org.eclipse.jface.action.IAction;

import hu.elte.txtuml.refactoring.unfoldtransitions.UnfoldTransitionsAction;

public class UnfoldOutgoingAction extends UnfoldTransitionsAction {
	@Override
	public void run(IAction action) {
		if (elements != null && window != null) {
			UnfoldOutgoingRefactoring refactoring = new UnfoldOutgoingRefactoring();
			refactoring.setTransition(elements.get(0));
			run(new UnfoldOutgoingWizard(refactoring, UnfoldOutgoingRefactoring.NAME), window.getShell(), UnfoldOutgoingRefactoring.NAME);
		}
	}
}