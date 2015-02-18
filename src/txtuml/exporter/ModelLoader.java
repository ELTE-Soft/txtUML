package txtuml.exporter;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class ModelLoader {
	private String umlFileName;

	public ModelLoader(String umlFileName) {
		this.umlFileName = umlFileName;
	}

	public Model load() {
		File umlFile = new File(umlFileName);

		ResourceSet resSet = new ResourceSetImpl();
		resSet.getPackageRegistry().put(UMLPackage.eNS_URI,
				UMLPackage.eINSTANCE);

		resSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		Map<URI, URI> uriMap = resSet.getURIConverter().getURIMap();

		URI _uri = URI
				.createURI("jar:file:umlplugins/org.eclipse.uml2.uml.resources_5.0.0.v20140910-1354.jar!/");
		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), _uri
				.appendSegment("libraries").appendSegment(""));

		UMLResourcesUtil.init(resSet);

		Resource resource = resSet.getResource(
				URI.createURI(umlFile.getAbsolutePath()), true);

		return (Model) resource.getContents().get(0);
	}
}
