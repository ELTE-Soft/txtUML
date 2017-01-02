package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

public class FMUResourceHandler {

	public void copyResources(Path path) throws IOException, URISyntaxException {
		Files.move(path.resolve("CMakeLists.txt"), path.resolve("CMakeLists_base.txt"),
				StandardCopyOption.REPLACE_EXISTING);
		for (String copiedFileName : Arrays.asList("CMakeLists.txt", "fmu/fmi2Functions.h", "fmu/fmi2FunctionTypes.h",
				"fmu/fmi2TypesPlatform.h", "fmu/fmudebug.cpp")) {
			copyResource(copiedFileName, path);
		}

	}

	private void copyResource(String fileName, Path basePath) throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(Uml2ToCppExporter.PROJECT_NAME);
		URL fileURL = bundle.getEntry("fmuResources" + IPath.SEPARATOR + fileName);

		Files.createDirectories(basePath.resolve(Paths.get(fileName)).getParent());
		Files.copy(Paths.get(FileLocator.toFileURL(fileURL).toURI()), basePath.resolve(fileName),
				StandardCopyOption.REPLACE_EXISTING);
	}

}
