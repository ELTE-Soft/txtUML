package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import hu.elte.txtuml.export.fmu.CreateFMUParamaters;
import hu.elte.txtuml.export.fmu.FMUStandardCreator;


public class BuildSupport implements IRunnableWithProgress {

	private String directory;
	private List<String> environments;
	private List<String> unavailableEnvironments;
	private CreateFMUParamaters fmuParameters;
	
	private IOException buildIOFail;
	private Exception 	fmuCreateFail;

	public BuildSupport(String directory, List<String> environments, CreateFMUParamaters fmuParameters) {
		this.directory = directory;
		this.environments = environments;
		this.unavailableEnvironments = new ArrayList<String>();
		this.fmuParameters = fmuParameters;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InterruptedException {
		if(environments != null && !environments.isEmpty()) {
			monitor.beginTask("Building environments ...", environments.size());

			try {
				for (String environment : environments) {
					monitor.worked(1);
					
					if(!createBuildEnvironment(directory, environment, Optional.empty()).equals(0)){
						unavailableEnvironments.add(environment);
					}
				}
			} catch (IOException e) {
				buildIOFail = e;
			} finally {
				monitor.done();
			}
		}
		
		if(fmuParameters.needFMU) {			
			monitor.beginTask("Create FMU", 2);
			
			String sourceDirectoryPath = fmuParameters.genPath.toFile().getPath();
			String fmuBuildDirectoryPath = createBuildEnvioronmentDir(sourceDirectoryPath,"fmu");
			
			try {
				if(!createBuildEnvironment(sourceDirectoryPath, FMUStandardCreator.FMU_BUILD_ENV, Optional.of(fmuBuildDirectoryPath)).equals(0)) {
					unavailableEnvironments.add(FMUStandardCreator.FMU_BUILD_ENV);
				}
				buildWithEnvironment(fmuBuildDirectoryPath, Arrays.asList("ninja", "-v"));
				monitor.worked(1);		

				FMUStandardCreator fmuStandardCreator = new FMUStandardCreator();		
				fmuStandardCreator.createFMU(fmuParameters.fmuConfig, fmuBuildDirectoryPath, fmuParameters.xmlPath);
				
				monitor.worked(1);	
				
			} catch (Exception e) {
				fmuCreateFail = e;
			}  finally {
				monitor.done();
			}			
			
		}
	}
		
	public void handleErrors() throws Exception {
		if(buildIOFail != null) {
			throw buildIOFail;
		}
		if(fmuCreateFail != null) {
			throw fmuCreateFail;
		}
		if(unavailableEnvironments.size() > 0) {
			StringBuilder sBuilder = new StringBuilder();
			  sBuilder.append("The following build environments are not supported:\n");
			  for(String environment : unavailableEnvironments){
				  sBuilder.append(environment + "\n");
			  }
			throw new EnvironmentNotFoundException(sBuilder.toString());
		}
	}

	public Integer createBuildEnvironment(String directory, String environment, Optional<String> buildDirectory) throws IOException, InterruptedException {
		String buildDir = buildDirectory.isPresent() ? buildDirectory.get() : createBuildEnvioronmentDir(directory, environment);
		File buildDirFile = new File(buildDir);
		buildDirFile.mkdir();
		return CppExporterUtils.executeCommand(buildDir,
				Arrays.asList("cmake", "-G", environment, "-DCMAKE_BUILD_TYPE=" + "Debug", ".."), null, "createEnvLog.txt");
	}
	
	public Integer buildWithEnvironment(String buildDirectoryPath, List<String> buildCommand) throws IOException, InterruptedException {
		Integer ret = CppExporterUtils.executeCommand(buildDirectoryPath, buildCommand, null, "buildLog.txt");	
		return ret;

	}
	
	public String createBuildEnvioronmentDir(String directory, String environment) {
		return directory + File.separator + "build_" + environment;
	}
}
