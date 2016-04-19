package hu.elte.txtuml.refactoring.groupstates;

import org.eclipse.jface.action.IAction;

import hu.elte.txtuml.refactoring.BaseActionDelegate;

public class GroupStatesAction extends BaseActionDelegate {
	@Override
	public void run(IAction action) {
		if (elements != null && window != null) {
			GroupStatesRefactoring refactoring = new GroupStatesRefactoring();
			refactoring.setSelectedStates(elements);
			run(new GroupStatesWizard(refactoring, GroupStatesRefactoring.NAME), window.getShell(), GroupStatesRefactoring.NAME);
		}
	}
}