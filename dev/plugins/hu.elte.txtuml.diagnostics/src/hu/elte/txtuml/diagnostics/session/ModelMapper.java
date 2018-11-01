package hu.elte.txtuml.diagnostics.session;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;

/**
 * Keeps track of model mappings
 */
public class ModelMapper {
	private static final String MAPPING_FILE_EXTENSION = ModelMapProvider.MAPPING_FILE_EXTENSION;
	private static final String MAPPING_FILE_EXTENSION_TOKEN = "." + MAPPING_FILE_EXTENSION;
	private static final String MAPPING_DIRECTORY_PATH =
			PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
	
	private NavigableMap<String, ModelMapProvider> mappingFileNameToMapProvider = new TreeMap<String, ModelMapProvider>();

	ModelMapper(String projectName) {
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (project.isOpen() && (projectName.equals("") || project.getName().equals(projectName))) {
				// If projectName=="" than no name could be read from the environment
				// projectName cannot be null
				IFolder mappingFolder = project.getFolder(MAPPING_DIRECTORY_PATH);
				if (mappingFolder.exists()) {
					File mappingDirectory = new File(mappingFolder.getLocation().toOSString());
					URI uri = URI.createFileURI(mappingFolder.getLocation().toOSString());
					if (mappingDirectory.exists() && mappingDirectory.isDirectory() && mappingDirectory.canRead()) {
						for (String mappingFilename : mappingDirectory.list(new FilenameFilter() {
								@Override
								public boolean accept(File dir, String name) {
									return name.endsWith(MAPPING_FILE_EXTENSION_TOKEN);
								}}
								)) {
							ModelMapProvider modelMapProvider = null;
							mappingFilename = mappingFilename.substring(0, mappingFilename.lastIndexOf(MAPPING_FILE_EXTENSION_TOKEN));
							try {
								modelMapProvider = new ModelMapProvider(uri, mappingFilename);
							} catch (ModelMapException ex) {
								Logger.sys.error("Symbol mapping error", ex);
								assert false;
							}
							if (modelMapProvider != null) {
								mappingFileNameToMapProvider.put(mappingFilename.intern(), modelMapProvider);
							}
						}
					}
				}
			}
		}
	}

	void dispose() {
		mappingFileNameToMapProvider.clear();
		mappingFileNameToMapProvider = null;
	}
	
	public EObject getEObjectForModelAndElement(String modelClassName, String elementClassName) {
		ModelMapProvider modelMapProvider = null;
		Map.Entry<String, ModelMapProvider> entry = mappingFileNameToMapProvider.floorEntry(modelClassName);
		// We do depend heavily on the fact that the mapping file name begins
		// with the class name and so one file contains a full inclusive
		// naming space under itself
		if (entry != null && modelClassName.startsWith(entry.getKey())) {
			modelMapProvider = entry.getValue();
		}
		if (modelMapProvider != null) {
			EObject eobject = modelMapProvider.getByName(elementClassName);
			if (eobject != null) {
				return eobject;
			} else {
				Logger.sys.warn("Mapping failure for element " + elementClassName + " in model " + modelClassName);
			}
		} else {
			Logger.sys.warn("Mapping failure for model " + modelClassName);
		}
		return null;
	}
}
