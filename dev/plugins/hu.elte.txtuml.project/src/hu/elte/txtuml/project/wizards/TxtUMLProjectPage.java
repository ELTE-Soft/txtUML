package hu.elte.txtuml.project.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import hu.elte.txtuml.project.Messages;

public class TxtUMLProjectPage extends WizardNewProjectCreationPage {
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public TxtUMLProjectPage() {
		super(TxtUMLProjectWizard.TITLE);
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