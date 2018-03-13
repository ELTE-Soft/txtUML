package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class BuildSupport {
	public static void build(String directory, List<String> environments) throws Exception {
		for(String environment : environments){
			build(directory, environment);
		}
	}
	
	private static void build(String directory, String environment) throws Exception {
		String buildDir = directory + File.separator + "build_" + environment;
		File buildDirFile = new File(buildDir);
		boolean wasCreated = buildDirFile.mkdir();
		if(wasCreated){
			/*int cmakeRetCode = */
			CppExporterUtils.executeCommand(buildDir,
					Arrays.asList("cmake", "-G", environment, "-DCMAKE_BUILD_TYPE=" + "Debug", ".."), null, null);
		}
	}
}
