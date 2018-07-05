package hu.elte.txtuml.export.fmu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
		if (optionalBuildDirectory.isPresent()) {
			String buildDirectory = optionalBuildDirectory.get();
			String libName = System.mapLibraryName(FMU_LIBRARY_NAME);
			File fmuDir = new File(buildDirectory + File.separator + fmuName + ".zip");
			//fmuDir.mkdir();
			
			
			zip(fmuDir, Arrays.asList(Paths.get(buildDirectory, libName).toFile()));
			/*
			Files.copy(xmlPath, Paths.get(fmuDir.getPath(), "modelDescription.xml"),
					StandardCopyOption.REPLACE_EXISTING);*/

		}
	}

	private static void zip(File zip, List<File> files) throws IOException {
		ZipOutputStream zos = null;
		try {
			for (File file : files) {

				String name = file.getName();
				zos = new ZipOutputStream(new FileOutputStream(zip));

				ZipEntry entry = new ZipEntry(name);
				zos.putNextEntry(entry);

				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					byte[] byteBuffer = new byte[1024];
					int bytesRead = -1;
					while ((bytesRead = fis.read(byteBuffer)) != -1) {
						zos.write(byteBuffer, 0, bytesRead);
					}
					zos.flush();
				} finally {
					try {
						fis.close();
					} catch (Exception e) {
					}
				}
				zos.closeEntry();

				zos.flush();
			}
		} finally {
			try {
				zos.close();
			} catch (Exception e) {
			}
		}
	}
}
