package hu.elte.txtuml.project.wizards;

import hu.elte.txtuml.project.ProjectCreator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class TxtUMLProject extends Wizard implements INewWizard {
	private TxtUMLProjectPage page;

	/**
	 * Constructor for TxtUMLProject.
	 */
	public TxtUMLProject() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new TxtUMLProjectPage();
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		String projectName = page.getProjectName();
		String modelName = page.getModelName();
		boolean success = setUptxtUMLProject(projectName, modelName);
		return success;
	}
	
	private boolean setUptxtUMLProject(String projectName,String modelName){
		try{
			System.out.println(projectName);
			System.out.println(modelName);
			IProject project = ProjectCreator.createProject(projectName);
			ProjectCreator.openProject(project);
		}catch(CoreException e){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			MessageDialog.openInformation(window.getShell(),"txtUML Project creation Error",e.getClass()+":\n"+e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}
}