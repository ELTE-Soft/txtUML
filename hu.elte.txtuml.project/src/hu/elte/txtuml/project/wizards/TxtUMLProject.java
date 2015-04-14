package hu.elte.txtuml.project.wizards;

import hu.elte.txtuml.export.utils.Dialogs;
import hu.elte.txtuml.project.ProjectCreator;
import hu.elte.txtuml.project.ProjectCreator.ProjectSettings;
import hu.elte.txtuml.project.TxtUMLProjectNature;
import hu.elte.txtuml.utils.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Constants;
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
	
	@SuppressWarnings("deprecation")
	private boolean setUptxtUMLProject(String projectName, String modelName){
		try{
			IProject project = ProjectCreator.createProject(projectName);
			ProjectCreator.openProject(project);
			
			String[] natures = new String[]{
					TxtUMLProjectNature.NATURE_ID,
					JavaCore.NATURE_ID, 
					"org.eclipse.ajdt.ui.ajnature",
					"org.eclipse.pde.PluginNature"};
			ProjectCreator.addProjectNatures(project, natures);
			
			IFolder metainf = ProjectCreator.createFolder(project, "META-INF");
			IFolder src = ProjectCreator.createFolder(project, "src");
			IFolder bin = ProjectCreator.createFolder(project, "bin");
			
			ProjectSettings settings = new ProjectSettings();
			settings.executionEnviromentID = "JavaSE-1.8";
			settings.output = bin;
			settings.source = src;
			settings.pluginDepAttributes = new ArrayList<Pair<String,String>>();
			settings.pluginDepAttributes.add(new Pair<String,String>("org.eclipse.ajdt.aspectpath", "org.eclipse.ajdt.aspectpath"));
			settings.manifest = new Manifest();
			settings.manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
			settings.manifest.getMainAttributes().putValue(Constants.BUNDLE_VERSION,  "1.0.0.qualifier");
			settings.manifest.getMainAttributes().putValue(Constants.BUNDLE_MANIFESTVERSION,  "2");
			settings.manifest.getMainAttributes().putValue(Constants.BUNDLE_REQUIREDEXECUTIONENVIRONMENT,  "JavaSE-1.8");
			settings.manifest.getMainAttributes().putValue(Constants.BUNDLE_NAME, project.getName());
			settings.manifest.getMainAttributes().putValue(Constants.BUNDLE_SYMBOLICNAME, project.getName());
			
			StringBuilder requireBundles = new StringBuilder();
			requireBundles.append(" ").append("hu.elte.txtuml.api").append(",\n");
			requireBundles.append(" ").append("hu.elte.txtuml.export.papyrus").append(",\n");
			requireBundles.append(" ").append("hu.elte.txtuml.export.uml2").append(",\n");
			requireBundles.append(" ").append("hu.elte.txtuml.layout.export").append(",\n");
			requireBundles.append(" ").append("hu.elte.txtuml.layout.visualizer").append(",\n");
			requireBundles.append(" ").append("hu.elte.txtuml.utils").append(",\n");
			requireBundles.append(" ").append("org.aspectj.runtime").append(",\n");
			requireBundles.append(" ").append("org.eclipse.uml2.uml").append(",\n");
			requireBundles.append(" ").append("org.eclipse.uml2.uml.resources");
			settings.manifest.getMainAttributes().putValue(Constants.REQUIRE_BUNDLE, requireBundles.toString());
			
			ProjectCreator.addProjectSettings(project, settings);
			ProjectCreator.addManifest(metainf, settings.manifest);
			ProjectCreator.createBuildProps(project, src, bin);
			
			ICompilationUnit modelFile = ProjectCreator.addTxtUMLModel(project, src, project.getName(), modelName);
			
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);
			
			openEditor((IFile) modelFile.getCorrespondingResource());
			
		}catch(CoreException | IOException e){
			Dialogs.errorMsgb("txtUML Project creation Error", e.getClass()+":\n"+e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}
	

	private void openEditor(final IFile file) throws PartInitException {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if(page != null) {
				try {
					IDE.openEditor(page, file);
				} catch (PartInitException e) {
					//ignore
				}
			}
	}
	
}