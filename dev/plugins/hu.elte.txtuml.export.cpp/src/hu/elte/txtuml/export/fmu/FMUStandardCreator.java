package hu.elte.txtuml.export.fmu;

import java.io.IOException;
import java.util.Arrays;

import hu.elte.txtuml.export.cpp.BuildSupport;

public class FMUStandardCreator {
	
	private static final String FMU_BUILD_ENV = "Ninja";
	
	
	public void buildFMUProject(String path) throws IOException, InterruptedException {
		BuildSupport.createBuildEnvironment(path, FMU_BUILD_ENV);
		BuildSupport.buildWithEnvironment(path, FMU_BUILD_ENV, Arrays.asList("ninja", "-v"));
	}
}
