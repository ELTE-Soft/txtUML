package hu.elte.txtuml.export.uml2.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

public class URIFragmentMapper {

	private Map<String, String> map;
	private static final String INVALID_MAPPING_FILE_CONTENT = "Mapping file content is invalid.";
	private String modelPath;

	public URIFragmentMapper(URI directory, String filename) throws ModelMapException {
		URI mappingURI = ModelMapUtils.createMappingURI(directory, filename);

		try {
			InputStream in = new ExtensibleURIConverterImpl().createInputStream(mappingURI);
			ObjectInputStream stream = new ObjectInputStream(in);
			Object modelPathObject = stream.readObject();
			Object mapObject = stream.readObject();
			if (modelPathObject == null || !(modelPathObject instanceof String) || mapObject == null
					|| !(mapObject instanceof Map<?, ?>)) {
				throw new ModelMapException(INVALID_MAPPING_FILE_CONTENT);
			}
			modelPath = (String) modelPathObject;
			/*
			 * The 'if' above verifies that the map has been successfully
			 * retrieved from the file and it is really a map. However, key and
			 * value types are not verified. It seems unnecessary overhead to
			 * check those as well and to construct a new, type-safe map,
			 * therefore the warning is suppressed. The localMap variable is
			 * only needed to make the scope of the suppression minimal.
			 */
			@SuppressWarnings("unchecked")
			Map<String, String> localMap = (Map<String, String>) mapObject;
			map = localMap;
			stream.close();
		} catch (IOException | ClassNotFoundException e) {
			throw new ModelMapException(e);
		}
	}

	public String getModelPath() {
		return modelPath;
	}

	public String get(String className) {
		return map.get(className);
	}

	@Override
	public String toString() {
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		List<String> elements = new ArrayList<>();

		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			elements.add(pair.getKey() + " = " + pair.getValue());
		}
		return "[ " + String.join(",", elements) + " ]";
	}
}
