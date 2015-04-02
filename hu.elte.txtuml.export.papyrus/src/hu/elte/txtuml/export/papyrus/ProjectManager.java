package hu.elte.txtuml.export.papyrus;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ProjectManager {

	/**
	 * Creates project or returns the existing.
	 * @param name
	 * @return Existing or created project
	 */
	
	public IProject createProject(String name){
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(name);
        if (!project.exists()){
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
	 * @param project
	 */
	public void openProject(IProject project){
		try {
			project.open(new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProject(IProject project){
		 if (project.exists()){
			try{
				project.delete(true, true, new NullProgressMonitor());
			}catch(CoreException e){
				e.printStackTrace();
			}
		 }
	}

	public void deleteProjectbyName(String projectname){
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectname);
		deleteProject(project);
	}
}
