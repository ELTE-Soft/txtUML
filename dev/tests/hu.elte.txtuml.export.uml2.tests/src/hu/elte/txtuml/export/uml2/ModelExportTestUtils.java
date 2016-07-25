package hu.elte.txtuml.export.uml2;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.uml2.uml.Model;

public class ModelExportTestUtils {
	private static final String PROJECT_FILE = ".project";
	private static final String TEST_PROJECT_NAME = "hu.elte.txtuml.export.uml2.tests.models";
	private static final String TEST_PROJECT_PATH = "../../../examples/tests/" + TEST_PROJECT_NAME;
	private static IJavaProject project;

	public static Model export(String txtUMLModelTopPackage) throws Exception {
		Model ret = TxtUMLToUML2.exportModel(project.getElementName(), txtUMLModelTopPackage,
				ExportMode.ExportActionsPedantic, "gen");
		return ret;
	}

	public static void initialize() throws CoreException, IOException, InterruptedException {
		String projectPath = new File(TEST_PROJECT_PATH).getCanonicalPath();
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(new Path(projectPath + Path.SEPARATOR + PROJECT_FILE));
		IProject genericProject = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		if (!genericProject.exists()) {
			genericProject.create(description, null);
		}
		genericProject.open(null);
		project = JavaCore.create(genericProject);
		genericProject.refreshLocal(IProject.DEPTH_INFINITE, null);
	}
}