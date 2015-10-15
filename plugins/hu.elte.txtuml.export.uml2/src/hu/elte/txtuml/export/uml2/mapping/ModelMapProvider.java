package hu.elte.txtuml.export.uml2.mapping;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

/**
 * ModelMapProvider provides a mapping from txtUML model elements to EMF-UML2
 * model elements.
 * 
 * @see hu.elte.txtuml.export.uml2.mapping.ModelMapCollector
 */
public class ModelMapProvider {
	public static final String MAPPING_FILE_EXTENSION = ModelMapUtils.MAPPING_FILE_EXTENSION;
	private URIFragmentMapper uriFragmentMapper;
	private Resource resource;
	private static final String CANNOT_LOAD_MODEL = "UML model cannot be loaded.";

	/**
	 * @param directory
	 *            Directory of the saved mapping.
	 * @param filename
	 *            File name of the saved mapping (without extension).
	 * @throws ModelMapException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ModelMapProvider(URI directory, String filename)
			throws ModelMapException {
		uriFragmentMapper = new URIFragmentMapper(directory, filename);

		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);
		URI modelURI = URI.createURI(uriFragmentMapper.getModelPath());
		if (modelURI == null) {
			throw new ModelMapException(CANNOT_LOAD_MODEL);
		}
		resource = resourceSet.getResource(modelURI, true);
		if (resource == null) {
			throw new ModelMapException(CANNOT_LOAD_MODEL);
		}
	}

	/**
	 * @param directory
	 *            Directory of the saved mapping.
	 * @param filename
	 *            File name of the saved mapping (without extension) the
	 * @param resource
	 *            the resource where UML2 model is to be find
	 * @throws ModelMapException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ModelMapProvider(URI directory, String filename, Resource resource)
			throws ModelMapException {
		uriFragmentMapper = new URIFragmentMapper(directory, filename);

		if (resource == null) {
			throw new ModelMapException(CANNOT_LOAD_MODEL);
		} else {
			this.resource = resource;
		}
	}

	/**
	 * Maps a Java class representing a txtUML model element to the
	 * corresponding EMF-UML2 model element.
	 * 
	 * @param clazz
	 *            Java class of the txtUML model element.
	 * @return The corresponding EObject of the EMF-UML2 model (or null if such
	 *         cannot be found).
	 */
	public EObject get(Class<?> clazz) {
		String className = clazz.getCanonicalName();
		return getByName(className);
	}

	/**
	 * Maps a Java class name representing a txtUML model element to the
	 * corresponding EMF-UML2 model element.
	 * 
	 * @param className
	 *            Java class canonical name of the txtUML model element.
	 * @return The corresponding EObject of the EMF-UML2 model (or null if such
	 *         cannot be found).
	 */
	public EObject getByName(String className) {
		String uriFragment = uriFragmentMapper.get(className);
		if (uriFragment == null || resource == null) {
			return null;
		}
		return resource.getEObject(uriFragment);
	}

	/**
	 * Dumps the mapping to the standard output for testing purposes.
	 */
	public void dump() {
		System.out.println("Model URI: " + resource.getURI());
		System.out.println(uriFragmentMapper.toString());
	}
}
