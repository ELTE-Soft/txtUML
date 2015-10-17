package hu.elte.txtuml.project.wizards;

import hu.elte.txtuml.eclipseutils.Dialogs;
import hu.elte.txtuml.project.Activator;
import hu.elte.txtuml.project.ProjectCreator;
import hu.elte.txtuml.project.ProjectCreator.ProjectSettings;
import hu.elte.txtuml.project.TxtUMLProjectNature;
import hu.elte.txtuml.utils.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import org.osgi.framework.Constants;

public class TxtUMLProjectWizard extends Wizard implements INewWizard {

	public static final String WizardImage = "icons/txtuml_model_wizard.png";
	public static final String TITLE = "New txtUML Project";
	private TxtUMLProjectPage page;

	/**
	 * Constructor for TxtUMLProject.
	 */
	public TxtUMLProjectWizard() {
		super();
		setWindowTitle(TITLE);
		ImageDescriptor descriptor = Activator.getImageDescriptor(WizardImage);
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
		boolean success = setUptxtUMLProject(projectName);
		return success;
	}

	@SuppressWarnings("deprecation")
	private boolean setUptxtUMLProject(String projectName) {
		IRunnableWithProgress op = new WorkspaceModifyOperation() {
			
			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException,
					InvocationTargetException, InterruptedException {
				
				if (monitor == null) {
					monitor = new NullProgressMonitor();
				}
				
				try {				
					IProject project = ProjectCreator.createProject(projectName);
					ProjectCreator.openProject(project);
					
					String[] natures = new String[]{
							TxtUMLProjectNature.NATURE_ID,
							JavaCore.NATURE_ID, 
							XtextProjectHelper.NATURE_ID,
							"org.eclipse.pde.PluginNature"
							};
					ProjectCreator.addProjectNatures(project, natures);
	
					IFolder metainf = ProjectCreator.createFolder(project, "META-INF");
					IFolder src = ProjectCreator.createFolder(project, "src");
					IFolder srcgen = ProjectCreator.createFolder(project, "src-gen");
					IFolder bin = ProjectCreator.createFolder(project, "bin");
	
					ProjectSettings settings = new ProjectSettings();
					settings.executionEnviromentID = "JavaSE-1.8";
					settings.output = bin;
					settings.source = src;
					settings.sourcegen = srcgen;
					settings.pluginDepAttributes = new ArrayList<Pair<String,String>>();
					settings.manifest = new Manifest();
					settings.manifest.getMainAttributes().put(
							Attributes.Name.MANIFEST_VERSION, "1.0");
					settings.manifest.getMainAttributes().putValue(
							Constants.BUNDLE_VERSION, "1.0.0.qualifier");
					settings.manifest.getMainAttributes().putValue(
							Constants.BUNDLE_MANIFESTVERSION, "2");
					settings.manifest.getMainAttributes()
							.putValue(Constants.BUNDLE_REQUIREDEXECUTIONENVIRONMENT,
									"JavaSE-1.8");
					settings.manifest.getMainAttributes().putValue(
							Constants.BUNDLE_NAME, project.getName());
					settings.manifest.getMainAttributes().putValue(
							Constants.BUNDLE_SYMBOLICNAME, project.getName());
	
					StringBuilder requireBundles = new StringBuilder();
					requireBundles.append("org.eclipse.uml2.uml,\n");
					requireBundles.append(" org.eclipse.uml2.uml.resources,\n");
					requireBundles.append(" org.eclipse.xtext.xbase.lib,\n");
					requireBundles.append(" hu.elte.txtuml.api.model,\n");
					requireBundles.append(" hu.elte.txtuml.api.layout,\n");
					requireBundles.append(" hu.elte.txtuml.api.stdlib,\n");
					requireBundles.append(" hu.elte.txtuml.api.diagnostics,\n");
					requireBundles.append(" hu.elte.txtuml.export.papyrus,\n");
					requireBundles.append(" hu.elte.txtuml.export.uml2,\n");
					requireBundles.append(" hu.elte.txtuml.layout.export,\n");
					requireBundles.append(" hu.elte.txtuml.layout.visualizer,\n");
					requireBundles.append(" hu.elte.txtuml.utils,\n");
					requireBundles.append(" hu.elte.txtuml.xtxtuml.lib,\n");
					requireBundles.append(" hu.elte.txtuml.xtxtuml,\n");
					requireBundles.append(" hu.elte.txtuml.xtxtuml.ui");
					settings.manifest.getMainAttributes().putValue(
							Constants.REQUIRE_BUNDLE, requireBundles.toString());

					ProjectCreator.addProjectSettings(project, settings);

					ProjectCreator.addManifest(metainf, settings.manifest);
					ProjectCreator.createBuildProps(project, src,srcgen, bin);
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (OperationCanceledException e) {
					throw new InterruptedException();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				}finally{
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, true, op);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML Project creation Error", e.getClass()
					+ ":\n" + e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}