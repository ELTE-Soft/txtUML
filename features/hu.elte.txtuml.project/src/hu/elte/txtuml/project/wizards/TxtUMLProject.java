package hu.elte.txtuml.project.wizards;

import hu.elte.txtuml.eclipseutils.Dialogs;
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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Constants;

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
		String modelName = page.getModelName();
		boolean success = setUptxtUMLProject(projectName, modelName);
		return success;
	}

	@SuppressWarnings("deprecation")
	private boolean setUptxtUMLProject(String projectName, String modelName) {
		try {
			IProject project = ProjectCreator.createProject(projectName);
			ProjectCreator.openProject(project);
			
			String[] natures = new String[]{
					TxtUMLProjectNature.NATURE_ID,
					JavaCore.NATURE_ID, 
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
			requireBundles.append("hu.elte.txtuml.api.model,\n");
			requireBundles.append(" hu.elte.txtuml.api.layout,\n");
			requireBundles.append(" hu.elte.txtuml.api.stdlib,\n");
			requireBundles.append(" hu.elte.txtuml.api.diagnostics,\n");
			requireBundles.append(" hu.elte.txtuml.export.papyrus,\n");
			requireBundles.append(" hu.elte.txtuml.export.uml2,\n");
			requireBundles.append(" hu.elte.txtuml.layout.export,\n");
			requireBundles.append(" hu.elte.txtuml.layout.visualizer,\n");
			requireBundles.append(" hu.elte.txtuml.utils,\n");
			requireBundles.append(" org.eclipse.uml2.uml,\n");
			requireBundles.append(" org.eclipse.uml2.uml.resources");
			settings.manifest.getMainAttributes().putValue(
					Constants.REQUIRE_BUNDLE, requireBundles.toString());

			ProjectCreator.addProjectSettings(project, settings);
			ProjectCreator.addManifest(metainf, settings.manifest);
			ProjectCreator.createBuildProps(project, src, bin);

			ICompilationUnit modelFile = addTxtUMLModel(project, src,
					modelName, modelName);

			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			openEditor((IFile) modelFile.getCorrespondingResource());

		} catch (CoreException | IOException e) {
			Dialogs.errorMsgb("txtUML Project creation Error", e.getClass()
					+ ":\n" + e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	private void openEditor(final IFile file) throws PartInitException {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			try {
				IDE.openEditor(page, file);
			} catch (PartInitException e) {
				// ignore
			}
		}
	}

	private static ICompilationUnit addTxtUMLModel(IProject project,
			IFolder sourceFolder, String packageName, String modelName)
			throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragment pack = javaProject
				.getPackageFragmentRoot(sourceFolder).createPackageFragment(
						packageName.toLowerCase(), false, null);

		StringBuffer buffer = new StringBuffer();
		buffer.append("package " + pack.getElementName().toLowerCase() + ";\n");
		buffer.append("\n");
		buffer.append("import hu.elte.txtuml.api.model.*;\n");
		buffer.append("import hu.elte.txtuml.api.layout.*;\n");
		buffer.append("\n");
		buffer.append("// This is your model.\n");
		buffer.append("class " + modelName + "Model extends Model {\n\n}\n");
		buffer.append("\n");
		buffer.append("// This is a diagram description to your model.\n");
		buffer.append("class " + modelName + "Diagram extends Diagram {\n\n}\n");
		buffer.append("\n");
		buffer.append("// This class executes your model.\n");
		buffer.append("class " + modelName + "Tester {\n");
		buffer.append("\tvoid test() {\n");
		buffer.append("\t\t// Write here the model execution logic.\n\t}\n");
		buffer.append("}\n");
		buffer.append("\n");
		buffer.append("public class " + modelName + " {\n");
		buffer.append("\tpublic static void main(String[] args) {\n");
		buffer.append("\t\tnew " + modelName + "Tester().test();\n");
		buffer.append("\t}\n");
		buffer.append("}\n");

		ICompilationUnit cu = pack.createCompilationUnit(modelName + ".java",
				buffer.toString(), false, null);
		return cu;
	}

}