package hu.elte.txtuml.export.uml2.mapping;

import org.eclipse.emf.common.util.URI;

class ModelMapUtils {
	private static final String MAPPING_FILE_EXTENSION = "mapping";
	
	static URI createMappingURI(URI directory, String filename) {
		return directory.appendSegment(filename).appendFileExtension(MAPPING_FILE_EXTENSION);
	}

}
