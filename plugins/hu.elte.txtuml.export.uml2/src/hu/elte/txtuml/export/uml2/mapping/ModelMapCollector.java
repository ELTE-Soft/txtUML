package hu.elte.txtuml.export.uml2.mapping;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

/**
 * ModelMapCollector can be used to collect and save a mapping
 * from txtUML model elements to EMF-UML2 model elements. 
 * @see hu.elte.txtuml.export.uml2.mapping.ModelMapProvider
 */
public class ModelMapCollector {
	private Map<String,String> map;
	private String path;
	
	private static final String CANNOT_CREATE_URI = "Invalid directory or file name.";

	/**
	 * @param path	Path of the EMF-UML2 model.
	 */
	public ModelMapCollector(URI modelPath) {
		map = new HashMap<String,String>();
		path = modelPath.toString();
	}

	/**
	 * Add a new pair of elements to the map.
	 * @param clazz		The Java class of the txtUML model element.
	 * @param eObject	The EObject representing the same model element in the EMF-UML2 model.
	 */
	public void put(Class<?> clazz, EObject eObject) {
		String className = clazz.getCanonicalName();
		String uriFragment = eObject.eResource().getURIFragment(eObject);
		map.put(className, uriFragment);
	}

	/**
	 * Save the mapping to file.
	 * @param directory	Directory to put the file in.
	 * @param filename	Name of the file (without extension).
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void save(URI directory, String filename) throws ModelMapException {
		URI uri = ModelMapUtils.createMappingURI(directory, filename);
		if(uri == null) {
			throw new ModelMapException(CANNOT_CREATE_URI);
		}
		try {
			OutputStream out = new ExtensibleURIConverterImpl().createOutputStream(uri);
			ObjectOutputStream stream = new ObjectOutputStream(out);
			stream.writeObject(path);
			stream.writeObject(map);
			stream.close();
		} catch(IOException e) {
			throw new ModelMapException(e);
		}
	}	
}
