package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

public class FMUResourceHandler {

	public void copyResources(IPath projectLocation) throws IOException, URISyntaxException {

		for (String copiedFileName : Arrays.asList("fmi2Functions.h", "fmi2FunctionTypes.h", "fmi2TypesPlatform.h",
				"FMUEnvironment.hpp")) {
			copyResource(copiedFileName, projectLocation);
		}

	}

	private void copyResource(String fileName, IPath projectLocation) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + fileName);
		
		Files.copy(Paths.get(FileLocator.toFileURL(fileURL).toURI()),
				Paths.get(projectLocation.toOSString(), Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME, fileName));
	}

}
