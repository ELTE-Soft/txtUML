package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import hu.elte.txtuml.utils.eclipse.Dialogs;

public class BuildSupport implements IRunnableWithProgress {

	private String directory;
	private List<String> environments;
	private List<String> unavailableEnvironments;

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
				
				if(!build(directory, environment).equals(0)){
					unavailableEnvironments.add(environment);
				}
			}
		} catch (IOException e) {
			Dialogs.errorMsgb("txtUML export environment build error", e.getClass() + ":" 
					+ System.lineSeparator() + e.getMessage(), e);
		} finally {
			monitor.done();
		}
	}
	
	public void handleErrors() throws Exception {
		if(unavailableEnvironments.size() > 0) {
			StringBuilder sBuilder = new StringBuilder();
			  sBuilder.append("The following environments are not builded:\n");
			  for(String environment : unavailableEnvironments){
				  sBuilder.append(environment + "\n");
			  }
			throw new EnvironmentNotFoundException(sBuilder.toString());
		}
	}

	private static Integer build(String directory, String environment) throws IOException, InterruptedException {
		String buildDir = directory + File.separator + "build_" + environment;
		File buildDirFile = new File(buildDir);
		buildDirFile.mkdir();
		return CppExporterUtils.executeCommand(buildDir,
				Arrays.asList("cmake", "-G", environment, "-DCMAKE_BUILD_TYPE=" + "Debug", ".."), null, "buildLog.txt");
	}
}
