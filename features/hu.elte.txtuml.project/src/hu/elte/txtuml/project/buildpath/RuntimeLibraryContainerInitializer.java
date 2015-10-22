package hu.elte.txtuml.project.buildpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * Setups a container that automatically adds the runtime and required projects
 * to the java class path when the model is executed.
 */
public class RuntimeLibraryContainerInitializer extends
		ClasspathContainerInitializer {

	public static final Path LIBRARY_PATH = new Path(
			"hu.elte.txtuml.project.runtimeClasspathInitializer"); //$NON-NLS-1$

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		if (!LIBRARY_PATH.equals(containerPath)) {
			return;
		}

		IClasspathContainer container = new RuntimeLibraryContainer(
				containerPath);
		JavaCore.setClasspathContainer(containerPath,
				new IJavaProject[] { project },
				new IClasspathContainer[] { container }, null);
	}
}