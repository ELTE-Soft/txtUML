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

import hu.elte.txtuml.export.cpp.BuildSupport;
import hu.elte.txtuml.export.cpp.CppExporterUtils;

public class FMUStandardCreator {

	private static final String FMU_BUILD_ENV = "Ninja";
	private static final String FMU_LIBRARY_NAME = "liblibmodelfmu";
	
	
	static class FMUStandardNames {
		public static final String FMU_BINARIES_ROOT_FOLDER = "binaries";
		public static final String FMU_EXTENSION = "fmu";
		
		public static String getSpecificBinariesFolder() {
			String arc = System.getProperty("os.arch");
			switch(arc) {
			case "amd64":
				arc = "64"; 
				break;
			case "x86":
				arc = "32";
				break;
			default:
				arc = "32";
				break;			
			}
			return (CppExporterUtils.isWindowsOS() ? "win" : "linux") + arc;
		}
	}

	private Optional<String> buildFMUProject(String path) throws IOException, InterruptedException {
		BuildSupport.createBuildEnvironment(path, FMU_BUILD_ENV);
		BuildSupport.buildWithEnvironment(path, FMU_BUILD_ENV, Arrays.asList("ninja", "-v"));

		return Optional.of(BuildSupport.createBuildEnvioronmentDir(path, FMU_BUILD_ENV));
	}

	public void createFMU(FMUConfig fmuConfig, Path genPath, Path xmlPath) throws Exception {
		Optional<String> optionalBuildDirectory = buildFMUProject(genPath.toFile().getPath());
		if (optionalBuildDirectory.isPresent()) {
			String buildDirectory = optionalBuildDirectory.get();
			String generatedLibName = System.mapLibraryName(FMU_LIBRARY_NAME);
			String fmuLibName = System.mapLibraryName(fmuConfig.umlClassName);
			File fmuDir = new File(buildDirectory + File.separator + fmuConfig.umlClassName + "." + FMUStandardNames.FMU_EXTENSION);

			File binaries = new File(buildDirectory + File.separator + FMUStandardNames.FMU_BINARIES_ROOT_FOLDER);
			binaries.mkdir();

			File subBinaryFolder = new File(binaries.getAbsolutePath() + File.separator + FMUStandardNames.getSpecificBinariesFolder());
			subBinaryFolder.mkdir();

			Files.copy(Paths.get(buildDirectory, generatedLibName), Paths.get(subBinaryFolder.getAbsolutePath(), fmuLibName),
					StandardCopyOption.REPLACE_EXISTING);

			
			zipFolder(Arrays.asList(binaries.getAbsolutePath(), xmlPath.toAbsolutePath().toFile().getAbsolutePath()) , 
					fmuDir.getAbsolutePath());

		}
	}

	static public void zipFolder(List<String> srcFolders, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);
		for(String srcFolder : srcFolders) {
			addFileToZip("", srcFolder, zip);
		}		
		zip.flush();
		zip.close();
	}

	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path.equals("") ? folder.getName() : path + File.separator + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
			
			//in.close();
		}
	}

	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}
}
