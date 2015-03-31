package hu.elte.txtuml.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ProjectCreator {
	public static IProject createProject(String name) throws CoreException{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(name);
		project.create(new NullProgressMonitor());
        return project;
	}
	
	/**
	 * Opens a project 
	 * @param project
	 * @throws CoreException 
	 */
	public static void openProject(IProject project) throws CoreException{
		project.open(new NullProgressMonitor());
	}
	
	public void deleteProject(IProject project) throws CoreException{
		project.delete(true, true, new NullProgressMonitor());
	}

	public void deleteProjectbyName(String projectname) throws CoreException{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectname);
		deleteProject(project);
	}
}
