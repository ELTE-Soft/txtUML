package hu.elte.txtuml.project;

import hu.elte.txtuml.project.buildpath.RuntimeLibraryContainerInitializer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentsManager;

public class ProjectCreator {

	public static class ProjectSettings {
		public String executionEnviromentID;
		public IFolder output;
		public IFolder source;
		public IFolder sourcegen;
	}

	public static IProject createProject(String name) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(name);
		project.create(new NullProgressMonitor());
		return project;
	}
	
	public static IProject createProjectOnLocation(IPath baseDirectory, String projectName)
			throws CoreException{
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
		description.setLocation(baseDirectory);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, null);
		return project;
	}

	public static void openProject(IProject project) throws CoreException {
		project.open(new NullProgressMonitor());
	}

	public static void deleteProject(IProject project) throws CoreException {
		project.delete(true, true, new NullProgressMonitor());
	}

	public static void deleteProjectbyName(String projectname)
			throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectname);
		deleteProject(project);
	}

	public static IFolder createFolder(IContainer parent, String projectName)
			throws CoreException {
		IFolder folder = parent.getFolder(new Path(projectName));
		folder.create(true, true, new NullProgressMonitor());
		return folder;
	}

	public static void addProjectNatures(IProject project, String[] natures)
			throws CoreException {
		for (String nature : natures) {
			if (!project.hasNature(nature)) {
				IProjectDescription description = project.getDescription();
				String[] prevNatures = description.getNatureIds();
				String[] newNatures = new String[prevNatures.length + 1];
				System.arraycopy(prevNatures, 0, newNatures, 0,
						prevNatures.length);
				newNatures[prevNatures.length] = nature;
				description.setNatureIds(newNatures);

				project.setDescription(description, new NullProgressMonitor());
			}
		}
	}

	public static void addProjectSettings(IProject project,
			ProjectSettings settings) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IExecutionEnvironmentsManager executionEnvironmentsManager = JavaRuntime
				.getExecutionEnvironmentsManager();
		IExecutionEnvironment[] executionEnvironments = executionEnvironmentsManager
				.getExecutionEnvironments();
		for (IExecutionEnvironment iExecutionEnvironment : executionEnvironments) {
			if (iExecutionEnvironment.getId().equals(
					settings.executionEnviromentID)) {
				entries.add(JavaCore.newContainerEntry(JavaRuntime
						.newJREContainerPath(iExecutionEnvironment)));
				break;
			}
		}

		IPackageFragmentRoot packageRoot = javaProject
				.getPackageFragmentRoot(settings.source);
		
		IPackageFragmentRoot packageRootSrcGen = javaProject
				.getPackageFragmentRoot(settings.sourcegen);

		IClasspathEntry containerEntry = JavaCore
				.newContainerEntry(RuntimeLibraryContainerInitializer.LIBRARY_PATH);
		entries.add(containerEntry);
		entries.add(JavaCore.newSourceEntry(packageRoot.getPath()));
		entries.add(JavaCore.newSourceEntry(packageRootSrcGen.getPath()));

		javaProject.setRawClasspath(
				entries.toArray(new IClasspathEntry[entries.size()]), null);
		javaProject.setOutputLocation(settings.output.getFullPath(), null);
	}

}
