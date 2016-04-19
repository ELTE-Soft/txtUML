package hu.elte.txtuml.refactoring.unfoldoutgoing;

import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class UnfoldOutgoingInputPage extends UserInputWizardPage {
	public UnfoldOutgoingInputPage(String name) {
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
		label.setText("Replaces a transition, leaving from a composite state, by a set of transitions, leaving from all substates (one transition for each substate) going to the same target.");
	}
}