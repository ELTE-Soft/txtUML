package hu.elte.txtuml.refactoring.unfoldincoming;

import hu.elte.txtuml.refactoring.BaseRefactoringWizard;

public class UnfoldIncomingWizard extends BaseRefactoringWizard {
	public UnfoldIncomingWizard(UnfoldIncomingRefactoring refactoring, String pageTitle) {
		super(refactoring, pageTitle);
	}

	@Override
	protected UnfoldIncomingInputPage getInputPage() {
		return new UnfoldIncomingInputPage("UnfoldIncomingInputPage");
	}
}