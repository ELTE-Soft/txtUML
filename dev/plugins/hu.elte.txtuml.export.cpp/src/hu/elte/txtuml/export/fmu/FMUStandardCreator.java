package hu.elte.txtuml.export.fmu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;

import org.eclipse.core.runtime.FileLocator;

import hu.elte.txtuml.export.cpp.BuildSupport;

public class FMUStandardCreator {
	
	private static final String FMU_BUILD_ENV = "Ninja";
	private static final String FMU_LIBRARY_NAME = "liblibmodelfmu";
	
	
	private Optional<String> buildFMUProject(String path) throws IOException, InterruptedException {
		BuildSupport.createBuildEnvironment(path, FMU_BUILD_ENV);
		BuildSupport.buildWithEnvironment(path, FMU_BUILD_ENV, Arrays.asList("ninja", "-v"));
		
		return Optional.of(BuildSupport.createBuildEnvioronmentDir(path, FMU_BUILD_ENV));
	}
	
	public void createFMU(String fmuName, Path genPath, Path xmlPath) throws IOException, InterruptedException {
		Optional<String> optionalBuildDirectory = buildFMUProject(genPath.toFile().getPath());
		if(optionalBuildDirectory.isPresent()) {
			String buildDirectory = optionalBuildDirectory.get();
			String libName = System.mapLibraryName(FMU_LIBRARY_NAME);
			File fmuDir = new File(buildDirectory + File.separator + fmuName);
			fmuDir.mkdir();
			Files.copy(Paths.get(buildDirectory, libName), Paths.get(fmuDir.getPath(), libName), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(xmlPath, Paths.get(fmuDir.getPath(), "modelDescription.xml"), StandardCopyOption.REPLACE_EXISTING);

		}
		
		
		
	}
}
