package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;


public class BuildSupport implements IRunnableWithProgress {

	private String directory;
	private List<String> environments;
	private List<String> unavailableEnvironments;
	IOException buildIOFail;

	public BuildSupport(String directory, List<String> environments) {
		this.directory = directory;
		this.environments = environments;
		this.unavailableEnvironments = new ArrayList<String>();
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InterruptedException {
		monitor.beginTask("Building environments ...", environments.size());

		try {
			for (String environment : environments) {
				monitor.worked(1);
				
				if(!createBuildEnvironment(directory, environment).equals(0)){
					unavailableEnvironments.add(environment);
				}
			}
		} catch (IOException e) {
			buildIOFail = e;
		} finally {
			monitor.done();
		}
	}
		
	public void handleErrors() throws Exception {
		if(buildIOFail != null) {
			throw buildIOFail;
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

	public static Integer createBuildEnvironment(String directory, String environment) throws IOException, InterruptedException {
		String buildDir = createBuildEnvioronmentDir(directory, environment);
		File buildDirFile = new File(buildDir);
		buildDirFile.mkdir();
		return CppExporterUtils.executeCommand(buildDir,
				Arrays.asList("cmake", "-G", environment, "-DCMAKE_BUILD_TYPE=" + "Debug", ".."), null, "createEnvLog.txt");
	}
	
	public static Integer buildWithEnvironment(String directory, String environment, List<String> buildCommand) throws IOException, InterruptedException {
		String buildDir = createBuildEnvioronmentDir(directory, environment);
		Integer ret = CppExporterUtils.executeCommand(buildDir, buildCommand, null, "buildLog.txt");
		
		return ret;

	}
	
	public static String createBuildEnvioronmentDir(String directory, String environment) {
		return directory + File.separator + "build_" + environment;
	}
}
