package hu.elte.txtuml.export.plantuml.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class PlantUmlExportTestUtils {

	private static final String PROJECT_FILE = ".project";
	private static final String TEST_MODEL_NAME = "hu.elte.txtuml.export.plantuml.tests.models";
	private static final String TEST_MODEL_PATH = "../../../examples/tests/" + TEST_MODEL_NAME;
	private static IJavaProject project;

	public static IProject getModelsProject() throws IOException, CoreException {
		String projectPath = new File(TEST_MODEL_PATH).getCanonicalPath();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IProjectDescription description = workspace
				.loadProjectDescription(new Path(projectPath + Path.SEPARATOR + PROJECT_FILE));
		IProject genericProject = workspace.getRoot().getProject(description.getName());

		if (!genericProject.exists()) {
			genericProject.create(description, new NullProgressMonitor());
		}

		genericProject.open(new NullProgressMonitor());
		project = JavaCore.create(genericProject);
		genericProject.refreshLocal(IProject.DEPTH_INFINITE, new NullProgressMonitor());
		genericProject.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());

		return genericProject;
	}

	public static String getOutput(IFile outfile) throws CoreException, IOException {
		InputStream inp = outfile.getContents();
		ByteArrayOutputStream str = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inp.read(buffer)) != -1) {
			str.write(buffer, 0, length);
		}

		str.close();
		inp.close();
		return str.toString("UTF-8");
	}

	public static IJavaProject getJavaModelsProject() throws CoreException, IOException {
		PlantUmlExportTestUtils.getModelsProject();
		return PlantUmlExportTestUtils.project;
	}
}
