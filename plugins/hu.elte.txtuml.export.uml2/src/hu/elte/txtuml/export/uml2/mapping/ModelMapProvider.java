package hu.elte.txtuml.export.uml2.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

/**
 * ModelMapProvider provides a mapping from txtUML model elements to EMF-UML2 model elements. 
 * @see hu.elte.txtuml.export.uml2.mapping.ModelMapCollector
 */
public class ModelMapProvider {
	private Map<String,String> map;
	private Resource resource;
	private static final String INVALID_MAPPING_FILE_CONTENT = "Mapping file content is ivalid.";
	private static final String CANNOT_LOAD_MODEL = "UML model cannot be loaded.";

	/**
	 * @param directory Directory of the saved mapping.
	 * @param filename	File name of the saved mapping (without extension).
	 * @throws ModelMapException 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ModelMapProvider(URI directory, String filename) throws ModelMapException {
		URI mappingURI = ModelMapUtils.createMappingURI(directory,filename);
		String modelPath;
		try {
			InputStream in = new ExtensibleURIConverterImpl().createInputStream(mappingURI);
			ObjectInputStream stream = new ObjectInputStream(in);
			Object modelPathObject = stream.readObject();
			Object mapObject = stream.readObject();
			if(modelPathObject == null || !(modelPathObject instanceof String)
					|| mapObject == null || !(mapObject instanceof Map<?,?>)) {
				throw new ModelMapException(INVALID_MAPPING_FILE_CONTENT);
			}
			modelPath = (String)modelPathObject;
			/* The 'if' above verifies that the map has been successfully retrieved
			 * from the file and it is really a map. However, key and value types are
			 * not verified. It seems unnecessary overhead to check those as well
			 * and to construct a new, type-safe map, therefore the warning is suppressed.
			 * The localMap variable is only needed to make the scope of the suppression
			 * minimal.
			 */
			@SuppressWarnings("unchecked")
			Map<String,String> localMap = (Map<String,String>)mapObject;
			map = localMap;
			stream.close();
		} catch(IOException | ClassNotFoundException e) {
			throw new ModelMapException(e);
		}

		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);
		URI modelURI = URI.createURI(modelPath);
		if(modelURI == null) {
			throw new ModelMapException(CANNOT_LOAD_MODEL);
		}
		resource = resourceSet.getResource(modelURI, true);
		if(resource == null) {
			throw new ModelMapException(CANNOT_LOAD_MODEL);
		}
	}
	
	/**
	 * Maps a Java class representing a txtUML model element
	 * to the corresponding EMF-UML2 model element.  
	 * @param clazz	Java class of the txtUML model element.
	 * @return The corresponding EObject of the EMF-UML2 model (or null if such cannot be found).
	 */
	public EObject get(Class<?> clazz) {
		String className = clazz.getCanonicalName();
		String uriFragment = map.get(className);
		if(uriFragment == null || resource == null) {
			return null;
		}
		return resource.getEObject(uriFragment);
	}

	/**
	 * Dumps the mapping to the standard output for testing purposes.
	 */
	public void dump() {
		System.out.println("Model URI: " + resource.getURI());
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pair = it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	    }
	}
}
