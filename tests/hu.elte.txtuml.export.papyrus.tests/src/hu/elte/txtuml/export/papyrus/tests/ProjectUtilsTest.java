package hu.elte.txtuml.export.papyrus.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Test;

import hu.elte.txtuml.utils.eclipse.ProjectUtils;

/**
 * Unit test for {@link ProjectUtils}
 *
 * @author Andr�s Dobreff
 * 
 * <p>
 * <b>Attention:</b>
 * This test should be run as a JUnit Plug-in test
 * </p>
 */
public class ProjectUtilsTest {

	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	
	@After
	public void tearDown(){
		IProject[] projects = root.getProjects();
		for(int i = 0; i<projects.length; i++){
			try {
				projects[i].delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testCreateProject() {
		assertTrue(root.getProjects().length == 0);
		ProjectUtils.createProject("TestProject");
		assertTrue(root.getProjects().length == 1);
		assertTrue(root.getProject("TestProject").getName().equals("TestProject"));
	}

	@Test(expected=NullPointerException.class)
	public void testCreateProjectNull() {
		assertTrue(root.getProjects().length == 0);
		ProjectUtils.createProject(null);
	}
	
	@Test
	public void testOpenProject() {
		ProjectUtils.createProject("TestProject");
		IProject p = root.getProject("TestProject");
		ProjectUtils.openProject(p);
		assertTrue(p.isOpen());
	}
	
	@Test(expected=NullPointerException.class)
	public void testOpenProjectNull() {
		ProjectUtils.openProject(null);
	}
	
	@Test
	public void testDeleteProjectIProject() {
		ProjectUtils.createProject("TestProject");
		IProject p = root.getProject("TestProject");
		
		assertTrue(root.getProjects().length == 1);
		ProjectUtils.deleteProject(p);
		assertTrue(root.getProjects().length == 0);
	}

	@Test
	public void testDeleteProjectString() {
		ProjectUtils.createProject("TestProject");
		
		assertTrue(root.getProjects().length == 1);
		ProjectUtils.deleteProject("TestProject");
		assertTrue(root.getProjects().length == 0);
	}
	
	@Test
	public void testGetProject() {
		try {
			root.getProject("TestProject").create(new NullProgressMonitor());
		} catch (CoreException e) {
			fail("CoreException thrown: "+e.getLocalizedMessage());
		}
		
		IProject p = ProjectUtils.getProject("TestProject");
		assertTrue(p.getName().equals("TestProject"));
	}
	
	@Test(expected=NullPointerException.class)
	public void testGetProjectNull() {
		ProjectUtils.getProject(null);
	}

}
