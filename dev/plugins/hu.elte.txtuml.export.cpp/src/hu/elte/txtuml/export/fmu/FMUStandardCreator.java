package hu.elte.txtuml.export.fmu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import hu.elte.txtuml.export.cpp.CppExporterUtils;

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
	
	static private String mapToGeneratedLibraryName(String libraryName) {
		String paltfromDependedLibraryName = System.mapLibraryName(FMU_LIBRARY_NAME);
		if(CppExporterUtils.isWindowsOS()) {
			return "lib" + paltfromDependedLibraryName;
		}
		
		return paltfromDependedLibraryName;
	}


	public void createFMU(FMUConfig fmuConfig, String fmuBuildDirectoryPath, Path xmlPath) throws Exception {
		String generatedLibName = mapToGeneratedLibraryName(FMU_LIBRARY_NAME);
		String fmuLibName = System.mapLibraryName(CppExporterUtils.qualifiedNameToSimpleName(fmuConfig.umlClassName));
		
		File fmuDir = new File(fmuBuildDirectoryPath + File.separator + fmuConfig.umlClassName + "." + FMUStandardNames.FMU_EXTENSION);
		
		File binaries = new File(fmuBuildDirectoryPath + File.separator + FMUStandardNames.FMU_BINARIES_ROOT_FOLDER);
		binaries.mkdir();
		
		for(String arcFolders : FMUStandardNames.getArcBinaryFolders ()) {
			File subBinaryFolder = new File(binaries.getAbsolutePath() + File.separator + arcFolders);
			subBinaryFolder.mkdir();
			Files.copy(Paths.get(fmuBuildDirectoryPath, generatedLibName), Paths.get(subBinaryFolder.getAbsolutePath(), fmuLibName),
					StandardCopyOption.REPLACE_EXISTING);
		}
		
		zipFolder(Arrays.asList(binaries.getAbsolutePath(), xmlPath.toAbsolutePath().toFile().getAbsolutePath()) , 
				fmuDir.getAbsolutePath());

	}

	public void zipFolder(List<String> srcFolders, String destZipFile) throws Exception  {
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
	

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

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
			
			in.close();
		}
	}

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
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
