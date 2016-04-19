package hu.elte.txtuml.refactoring.groupstates;

import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GroupStatesInputPage extends UserInputWizardPage {
	Text newNameField;

	public GroupStatesInputPage(String name) {
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
		label.setText("&Name of the composite state:");

		newNameField = createNameField(composite);
		newNameField.addModifyListener(e -> handleInputChanged());
		newNameField.setFocus();
	}

	private Text createNameField(Composite composite) {
		Text field = new Text(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		field.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return field;
	}

	private void handleInputChanged() {
		RefactoringStatus status = new RefactoringStatus();
		GroupStatesRefactoring refactoring = getGroupStatesRefactoring();
		refactoring.setCompositeStateName(newNameField.getText());

		setPageComplete(!status.hasError());
		int severity = status.getSeverity();
		String message = status.getMessageMatchingSeverity(severity);
		if (severity >= RefactoringStatus.INFO) {
			setMessage(message, severity);
		} else {
			setMessage("", NONE);
		}
	}

	private GroupStatesRefactoring getGroupStatesRefactoring() {
		return (GroupStatesRefactoring) getRefactoring();
	}
}