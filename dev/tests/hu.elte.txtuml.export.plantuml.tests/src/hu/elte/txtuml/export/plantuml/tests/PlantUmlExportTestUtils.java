package hu.elte.txtuml.export.plantuml.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;

public class PlantUmlExportTestUtils {

	public static IProject getSelfProject() throws IOException, CoreException {
		String projectPath = new File("").getCanonicalPath();
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(new Path(projectPath + Path.SEPARATOR + ".project"));
		IProject genericProject = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		if (!genericProject.exists()) {
			genericProject.create(description, null);
		}
		genericProject.open(null);
		JavaCore.create(genericProject);
		genericProject.refreshLocal(IProject.DEPTH_INFINITE, null);

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
}
