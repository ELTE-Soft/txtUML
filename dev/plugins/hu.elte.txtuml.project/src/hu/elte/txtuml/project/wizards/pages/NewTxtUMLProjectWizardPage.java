package hu.elte.txtuml.project.wizards.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import hu.elte.txtuml.project.Messages;
import hu.elte.txtuml.project.wizards.NewTxtUMLProjectCreationWizard;

public class NewTxtUMLProjectWizardPage extends WizardNewProjectCreationPage {
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewTxtUMLProjectWizardPage() {
		super(NewTxtUMLProjectCreationWizard.TITLE);
		setTitle("Creating txtUML Project");
		setDescription("Give the name of the new txtUML Project!");
	}

	@Override
	protected boolean validatePage() {
		if (!super.validatePage())
			return false;
		IStatus status = JavaConventions.validatePackageName(getProjectName(), JavaCore.VERSION_1_5,
				JavaCore.VERSION_1_5);
		if (!status.isOK()) {
			if(status.matches(IStatus.WARNING)){
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