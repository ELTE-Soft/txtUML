package hu.elte.txtuml.project.wizards.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import hu.elte.txtuml.project.Messages;

public class NewTxtUMLProjectWizardPage extends WizardNewProjectCreationPage {

	public static final String TITLE = "txtUML Project";
	public static final String DESCRIPTION = "Create a new txtUML Project.";

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewTxtUMLProjectWizardPage() {
		super(TITLE);
		setTitle(TITLE);
		setDescription(DESCRIPTION);
	}

	@Override
	protected boolean validatePage() {
		if (!super.validatePage())
			return false;
		IStatus status = JavaConventions.validatePackageName(getProjectName(), JavaCore.VERSION_1_5,
				JavaCore.VERSION_1_5);
		if (!status.isOK()) {
			if (status.matches(IStatus.WARNING)) {
				setMessage(status.getMessage(), IStatus.WARNING);
				return true;
			}
			setErrorMessage(Messages.WizardNewtxtUMLProjectCreationPage_ErrorMessageProjectName + status.getMessage());
			return false;
		}
		setErrorMessage(null);
		setMessage(null);
		return true;
	}

}