package hu.elte.txtuml.refactoring.unfoldincoming;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class UnfoldIncomingInputPage extends UserInputWizardPage {
	public UnfoldIncomingInputPage(String name) {
		super(name);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Replaces a transition, going to a composite state, by a set of transitions, going to all substates (one transition for each substate) leaving from the same source.");
	}
}