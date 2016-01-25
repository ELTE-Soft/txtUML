package hu.elte.txtuml.export.uml2;

import hu.elte.txtuml.export.uml2.UML2;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.uml2.uml.Model;

public class ModelExportTestUtils {
	private static final String TEST_PROJECT_NAME = "hu.elte.txtuml.export.uml2.tests.models";
	private static final String GEN_DIR = TEST_PROJECT_NAME + "/gen";
	private static final String MODEL_EXTENSION = ".uml";
	private static IJavaProject project;
	private static String bundlePath;
	
	public static Model export(String txtUMLModelName) throws Exception {
		IType model  = project.findType(txtUMLModelName, (IProgressMonitor) null);
		File file = new File(bundlePath + model.getPath().toOSString());
		String uri = URI.createPlatformResourceURI(GEN_DIR, false).toString();
		UML2.exportModel(txtUMLModelName, file, project, uri);
		Model ret = loadModel(txtUMLModelName);
		Thread.sleep(1000);
		return ret;
	}

	public static void initialize() throws CoreException, IOException, InterruptedException {
		
		IProject genericProject = ResourcesPlugin.getWorkspace().getRoot().getProject(TEST_PROJECT_NAME);
		bundlePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		genericProject.open(null);
		project = JavaCore.create(genericProject);
		Thread.sleep(1000);
	}
	
	private static Model loadModel(String txtUMLModelName) {
			String path = TEST_PROJECT_NAME + "/gen/" + txtUMLModelName + MODEL_EXTENSION;
	
			URI uri = URI.createPlatformResourceURI(path, false);
			ResourceSet resSet = new ResourceSetImpl();
		    Resource resource = resSet.getResource(uri, true);
		    Model model = null;
		    if(resource.getContents().size() != 0) 
		    	model = (Model) resource.getContents().get(0);
		    return model;
	}
}
