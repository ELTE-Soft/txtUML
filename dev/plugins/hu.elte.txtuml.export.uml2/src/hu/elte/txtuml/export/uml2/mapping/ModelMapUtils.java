package hu.elte.txtuml.export.uml2.mapping;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import hu.elte.txtuml.utils.eclipse.ProjectUtils;

public class ModelMapUtils {
	public static final String MAPPING_FILE_EXTENSION = "mapping";
	public static final String MAPPING_FILE_EXTENSION_TOKEN = "."
			+ MAPPING_FILE_EXTENSION;

	public static URI createMappingURI(URI directory, String filename) {
		return directory.appendSegment(filename).appendFileExtension(
				MAPPING_FILE_EXTENSION);
	}

	public static Map<String, ModelMapProvider> collectModelMapProviders(
			String projectName, String mappingFolderName, Resource resource)
			throws ModelMapException {
		Map<String, ModelMapProvider> modelMapProviders = new TreeMap<>();

		IProject project = ProjectUtils.getProject(projectName);

		if (project.isOpen()
				&& (projectName.equals("") || project.getName().equals(
						projectName))) {
			IFolder mappingFolder = project.getFolder(mappingFolderName);
			if (mappingFolder.exists()) {
				File mappingDirectory = new File(mappingFolder.getLocation()
						.toOSString());
				URI uri = URI.createFileURI(mappingFolder.getLocation()
						.toOSString());

				if (mappingDirectory.exists() && mappingDirectory.isDirectory()
						&& mappingDirectory.canRead()) {
					for (String mappingFilename : mappingDirectory
							.list(new FilenameFilter() {
								@Override
								public boolean accept(File dir, String name) {
									return name
											.endsWith(MAPPING_FILE_EXTENSION_TOKEN);
								}
							})) {

						ModelMapProvider modelMapProvider = null;
						mappingFilename = mappingFilename
								.substring(
										0,
										mappingFilename
												.lastIndexOf(MAPPING_FILE_EXTENSION_TOKEN));
						if (resource != null) {
							modelMapProvider = new ModelMapProvider(uri,
									mappingFilename, resource);
						} else {
							modelMapProvider = new ModelMapProvider(uri,
									mappingFilename);
						}

						if (modelMapProvider != null) {
							modelMapProviders.put(mappingFilename,
									modelMapProvider);
						}
					}
				}
			}
		}
		return modelMapProviders;
	}
}
