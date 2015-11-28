package hu.elte.txtuml.project.wizards;

import hu.elte.txtuml.eclipseutils.Dialogs;
import hu.elte.txtuml.project.Activator;
import hu.elte.txtuml.project.ProjectCreator;
import hu.elte.txtuml.project.ProjectCreator.ProjectSettings;
import hu.elte.txtuml.project.TxtUMLProjectNature;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.xtext.ui.XtextProjectHelper;

public class TxtUMLProjectWizard extends Wizard implements INewWizard {

	private static final String[] PROJECT_NATURE_IDS = new String[] {
			TxtUMLProjectNature.NATURE_ID, JavaCore.NATURE_ID,
			XtextProjectHelper.NATURE_ID };
	
	private static final String BIN_DIR = "bin";
	private static final String GENERATED_SOURCE_FOLDER_DIR = "src-gen";
	private static final String SOURCE_FOLDER_DIR = "src";
	
	public static final String WIZARD_IMAGE = "icons/txtuml_model_wizard.png";
	public static final String TITLE = "New txtUML Project";
	
	private TxtUMLProjectPage page;

	/**
	 * Constructor for TxtUMLProject.
	 */
	public TxtUMLProjectWizard() {
		super();
		setWindowTitle(TITLE);
		ImageDescriptor descriptor = Activator.getImageDescriptor(WIZARD_IMAGE);
		setDefaultPageImageDescriptor(descriptor);
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		page = new TxtUMLProjectPage();
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		String projectName = page.getProjectName();
		IPath projectLocation = page.getLocationPath();
		projectLocation = projectLocation.append(projectName);
		boolean success = setUptxtUMLProject(projectLocation, projectName);
		return success;
	}

	private boolean setUptxtUMLProject(IPath projectLocation, String projectName) {
		IRunnableWithProgress op = new WorkspaceModifyOperation() {
			
			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException,
					InvocationTargetException, InterruptedException {
				
				if (monitor == null) {
					monitor = new NullProgressMonitor();
				}
				
				try {				
					IProject project = ProjectCreator.createProjectOnLocation(projectLocation, projectName);
					ProjectCreator.openProject(project);
					
					ProjectCreator.addProjectNatures(project, PROJECT_NATURE_IDS);
	
					IFolder src = ProjectCreator.createFolder(project, SOURCE_FOLDER_DIR);
					IFolder srcgen = ProjectCreator.createFolder(project, GENERATED_SOURCE_FOLDER_DIR);
					IFolder bin = ProjectCreator.createFolder(project, BIN_DIR);
	
					ProjectSettings settings = new ProjectSettings();
					settings.executionEnviromentID = "JavaSE-1.8";
					settings.output = bin;
					settings.source = src;
					settings.sourcegen = srcgen;
	
					ProjectCreator.addProjectSettings(project, settings);

					project.refreshLocal(IResource.DEPTH_INFINITE, null);
					
				} catch (OperationCanceledException e) {
					throw new InterruptedException();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, true, op);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML Project creation Error", 
					"Error occured during project creation.", e);
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}