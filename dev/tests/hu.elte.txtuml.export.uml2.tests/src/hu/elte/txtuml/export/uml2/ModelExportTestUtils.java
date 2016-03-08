package hu.elte.txtuml.export.uml2;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import hu.elte.txtuml.export.uml2.UML2.ExportMode;
import hu.elte.txtuml.utils.eclipse.PackageUtils;

public class ModelExportTestUtils {
	private static final String PROJECT_FILE = ".project";
	private static final String TEST_PROJECT_NAME = "hu.elte.txtuml.export.uml2.tests.models_";
	private static final String TEST_PROJECT_PATH = "../../../examples/tests/" + TEST_PROJECT_NAME;
	private static final String GEN_DIR = TEST_PROJECT_NAME + "/gen";
	private static final String MODEL_PATH_PREFIX = "platform:/resource/" + GEN_DIR + "/";
	private static final String MODEL_EXTENSION = ".uml";
	private static IJavaProject project;

	public static Model export(String txtUMLModelTopPackage) throws Exception {
		String uri = URI.createPlatformResourceURI(GEN_DIR, false).toString();
		UML2.exportModel(txtUMLModelTopPackage, PackageUtils.findPackageFragments(project, txtUMLModelTopPackage),
				project, uri, ExportMode.ExportActionCode);
		Model ret = loadModel(txtUMLModelTopPackage);
		Thread.sleep(1000);
		return ret;
	}

	public static void initialize() throws CoreException, IOException, InterruptedException {
		String projectPath = new File(TEST_PROJECT_PATH).getCanonicalPath();
		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(projectPath + Path.SEPARATOR + PROJECT_FILE));
		IProject genericProject = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		if (!genericProject.exists()) {
			genericProject.create(description, null);
		}
		genericProject.open(null);
		project = JavaCore.create(genericProject);
		genericProject.refreshLocal(IProject.DEPTH_INFINITE, null);
	}

	private static Model loadModel(String txtUMLModelName) {
		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);
		URI uri = URI.createURI(MODEL_PATH_PREFIX + txtUMLModelName + MODEL_EXTENSION);
		Resource resource = resourceSet.getResource(uri, true);
		return (Model) resource.getContents().get(0);
	}
}