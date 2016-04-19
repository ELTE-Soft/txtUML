package hu.elte.txtuml.refactoring.groupstates;

import hu.elte.txtuml.refactoring.BaseRefactoringWizard;

public class GroupStatesWizard extends BaseRefactoringWizard {
	public GroupStatesWizard(GroupStatesRefactoring refactoring, String pageTitle) {
		super(refactoring, pageTitle);
	}

	@Override
	protected GroupStatesInputPage getInputPage() {
		return new GroupStatesInputPage("GroupStatesInputPage");
	}
}