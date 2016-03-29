package hu.elte.txtuml.export.cpp.wizardz;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

public class CompileTests {

	private static class Config {
		final String project;
		final String model;
		final String deployment;

		Config(String project, String model, String deployment) {
			this.project = project;
			this.model = model;
			this.deployment = deployment;
		}
	}

	private static final String pathToProjects = "../../../examples/demo/";

	private static final Config[] testProjects = {
			new Config("machine", "machine1.j.model", "machine1.j.DefaultConfiguration"),
			new Config("monitoring", "monitoring.x.model", "monitoring.x.DefaultConfiguration"),
			new Config("producer_consumer", "producer_consumer.j.model", "producer_consumer.j.DefaultConfiguration"),
			new Config("train", "train.j.model", "train.j.DefaultConfiguration"), };

	private static String generateCPP(Config config, String testPrefix, boolean addRuntime) throws Exception {
		TxtUMLToCppGovernor cppgen = new TxtUMLToCppGovernor(true);

		String canPathToProjects = new File(pathToProjects).getCanonicalPath();
		IProjectDescription desc = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(new Path(canPathToProjects + "/" + config.project + "/.project"));
		desc.setLocation(new Path(new File(canPathToProjects + "/" + config.project).getCanonicalPath()));
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(desc.getName());
		if (!project.exists()) {
			project.create(desc, null);
		}
		project.open(null);

		String testProject = testPrefix + config.project;
		project.copy(new Path(testProject), true, null);
		project.close(null);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(testProject);
		project.refreshLocal(IProject.DEPTH_INFINITE, null);

		cppgen.uml2ToCpp(testProject, config.model, config.deployment, addRuntime);

		return testProject;
	}

	@Test
	public void exportTest() {
		for (Config config : testProjects) {
			try {
				generateCPP(config, "exportTest_", false);
			} catch (Exception e) {
				e.printStackTrace();
				assertThat(false, is(true));
			}
		}
	}

	@Test
	public void compileTest() {
		for (Config config : testProjects) {
			try {
				generateCPP(config, "compileTest_", true);
			} catch (Exception e) {
				e.printStackTrace();
				assertThat(false, is(true));
			}
		}
	}
}
