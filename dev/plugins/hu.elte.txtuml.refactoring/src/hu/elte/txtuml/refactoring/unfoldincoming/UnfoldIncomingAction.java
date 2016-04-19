package hu.elte.txtuml.refactoring.unfoldincoming;

import org.eclipse.jface.action.IAction;

import hu.elte.txtuml.refactoring.BaseActionDelegate;

public class UnfoldIncomingAction extends BaseActionDelegate {
	@Override
	public void run(IAction action) {
		if (elements != null && window != null) {
			UnfoldIncomingRefactoring refactoring = new UnfoldIncomingRefactoring();
			refactoring.setTransition(elements.get(0));
			run(new UnfoldIncomingWizard(refactoring, UnfoldIncomingRefactoring.NAME), window.getShell(), UnfoldIncomingRefactoring.NAME);
		}
	}
}