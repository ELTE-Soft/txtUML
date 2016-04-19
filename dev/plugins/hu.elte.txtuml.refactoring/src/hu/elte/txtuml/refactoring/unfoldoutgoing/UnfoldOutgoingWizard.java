package hu.elte.txtuml.refactoring.unfoldoutgoing;

import hu.elte.txtuml.refactoring.BaseRefactoringWizard;

public class UnfoldOutgoingWizard extends BaseRefactoringWizard {
	public UnfoldOutgoingWizard(UnfoldOutgoingRefactoring refactoring, String pageTitle) {
		super(refactoring, pageTitle);
	}

	@Override
	protected UnfoldOutgoingInputPage getInputPage() {
		return new UnfoldOutgoingInputPage("UnfoldOutgoingInputPage");
	}
}