package hu.elte.txtuml.utils.eclipse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * Responsible for basic functionalities of an Eclipse Project
 */
public class ProjectUtils {

	/**
	 * Creates project or returns the existing.
	 * 
	 * @param name
	 *            - The Name of the Project
	 * @return Existing or created project
	 */
	public static IProject createProject(String name) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(name);
		if (!project.exists()) {
			try {
				project.create(null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return project;
	}

	/**
	 * Opens a project
	 * 
	 * @param project
	 *            - The Project to be opened
	 */
	public static void openProject(IProject project) {
		try {
			project.open(new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes a project
	 * 
	 * @param project
	 *            - The Project to be deleted
	 */
	public static void deleteProject(IProject project) {
		if (project.exists()) {
			try {
				project.delete(true, true, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Deletes a Project by Name
	 * 
	 * @param projectname
	 *            - The Name of the Project to be deleted
	 */
	public static void deleteProject(String projectname) {
		IProject project = getProject(projectname);
		deleteProject(project);
	}

	/**
	 * @param projectName
	 *            - Name of the project
	 * @return a project resource handle
	 * @see IWorkspaceRoot#getProject(String)
	 */
	public static IProject getProject(String projectName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getProject(projectName);
	}

	/**
	 * Identifies the given project and returns an {@link IJavaProject} instance.
	 * 
	 * @param projectName - the source project
	 * @return An {@link IJavaProject} instance if the given project is a java project.
	 * @throws NotFoundException If the project cannot be found, or not a java project.
	 */
	public static IJavaProject findJavaProject(String projectName) throws NotFoundException {
		IProject project;
		try {
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		} catch (IllegalArgumentException e) {
			project = null;
		}
		if (project == null || !project.exists()) {
			throw new NotFoundException("Cannot find project '" + projectName + "'");
		}
		return JavaCore.create(project);
	}

	/**
	 * Searches all projects of the workspace and returns a filtered list containing only Java projects
	 * @return A filtered list containing only Java projects
	 */
	public static List<IJavaProject> getAllJavaProjectsOfWorkspace() {
		return Stream.of(ResourcesPlugin.getWorkspace().getRoot().getProjects()).map(pr -> {
			try {
				return ProjectUtils.findJavaProject(pr.getName());
			} catch (NotFoundException e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
}
