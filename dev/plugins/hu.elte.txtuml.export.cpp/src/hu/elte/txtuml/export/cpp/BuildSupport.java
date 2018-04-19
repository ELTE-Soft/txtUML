package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import hu.elte.txtuml.utils.eclipse.Dialogs;

public class BuildSupport implements IRunnableWithProgress {

	private String directory;
	private List<String> environments;

	public BuildSupport(String directory, List<String> environments) {
		this.directory = directory;
		this.environments = environments;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InterruptedException {
		monitor.beginTask("Building environments ...", environments.size());

		try {
			for (String environment : environments) {
				monitor.worked(1);
				
				if(!build(directory, environment).equals(0)){
					throw new IOException("Throw test exception"); // This is not throwed
				}
			}
		} catch (IOException e) {
			Dialogs.errorMsgb("txtUML export environment build error", e.getClass() + ":" + System.lineSeparator() + e.getMessage(), e);
		} finally {
			monitor.done();
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
