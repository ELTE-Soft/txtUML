package hu.elte.txtuml.export.fmu;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.FolderZipper;

public class FMUStandardCreator {

	public static final String FMU_BUILD_ENV = "Ninja";
	private static final String FMU_LIBRARY_NAME = "modelfmu";
	
	static class FMUStandardNames {
		public static final String FMU_BINARIES_ROOT_FOLDER = "binaries";
		public static final String FMU_EXTENSION = "fmu";
		
		public static List<String> getArcBinaryFolders() {
			String op = (CppExporterUtils.isWindowsOS() ? "win" : "linux");
			return  Arrays.asList(op + "32", op + "64");
		}
	}
	
	private String mapToGeneratedLibraryName(String libraryName) {
		String paltfromDependedLibraryName = System.mapLibraryName(FMU_LIBRARY_NAME);
		if(CppExporterUtils.isWindowsOS()) {
			return "lib" + paltfromDependedLibraryName;
		}
		
		return paltfromDependedLibraryName;
	}
	
	private String mapToSimpleLibraryName(String libraryName) {
		String paltfromDependedLibraryName = System.mapLibraryName(libraryName);
		if(!CppExporterUtils.isWindowsOS()) {
			return paltfromDependedLibraryName.replaceAll("lib", "");
		}
		
		return paltfromDependedLibraryName;
	}


	public void createFMU(FMUConfig fmuConfig, String fmuBuildDirectoryPath, Path xmlPath) throws Exception {
		String generatedLibName = mapToGeneratedLibraryName(FMU_LIBRARY_NAME);
		String fmuLibName = mapToSimpleLibraryName(CppExporterUtils.qualifiedNameToSimpleName(fmuConfig.umlClassName));
		
		File fmuDir = new File(fmuBuildDirectoryPath + File.separator + fmuConfig.umlClassName + "." + FMUStandardNames.FMU_EXTENSION);
		
		File binaries = new File(fmuBuildDirectoryPath + File.separator + FMUStandardNames.FMU_BINARIES_ROOT_FOLDER);
		binaries.mkdir();
		
		for(String arcFolders : FMUStandardNames.getArcBinaryFolders ()) {
			File subBinaryFolder = new File(binaries.getAbsolutePath() + File.separator + arcFolders);
			subBinaryFolder.mkdir();
			Files.copy(Paths.get(fmuBuildDirectoryPath, generatedLibName), Paths.get(subBinaryFolder.getAbsolutePath(), fmuLibName),
					StandardCopyOption.REPLACE_EXISTING);
		}
		
		FolderZipper zipper = new FolderZipper (fmuDir);
		zipper.addDestinationToZip(binaries);
		zipper.addDestinationToZip(xmlPath.toAbsolutePath().toFile());
		zipper.zipFolder();

	}


}
