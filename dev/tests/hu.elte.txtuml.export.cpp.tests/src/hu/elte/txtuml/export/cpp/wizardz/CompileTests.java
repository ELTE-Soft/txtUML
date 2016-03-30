package hu.elte.txtuml.export.cpp.wizardz;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * These tests can also test the compilation of generated CPP sources if the
 * following are installed:
 * 
 * - cmake, ninja for building sources (sudo apt-get install cmake ninja-build)
 * 
 * - GCC compiler (sudo apt-get install gcc g++)
 * 
 * - Clang compiler (sudo apt-get install clang llvm)
 */

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

	private static final String testWorkspace = "target/work/data/";
	private static final String exportTestProjectPrefix = "exportTest_";
	private static final String compileTestProjectPrefix = "compileTest_";
	private static final String buildDirPrefix = "build_";

	private static boolean buildStuffPresent = false;
	private static boolean compilerGCCPresent = false;
	private static boolean compilerClangPresent = false;

	@BeforeClass
	public static void detectCPPEnvironment() {
		System.out.println("***************** CPP Compilation Test probing environment");
		try {
			int cmakeRet = executeCommand(testWorkspace, Arrays.asList("cmake", "--version"), null);
			int ninjaRet = executeCommand(testWorkspace, Arrays.asList("ninja", "--version"), null);
			if (cmakeRet != 0 || ninjaRet != 0) {
				System.out.println(
						"***************** CPP Compilation Test needs cmake and ninja, skipping compilation tests!!!!!!!!!!");
				return;
			}
			int gccRet = executeCommand(testWorkspace, Arrays.asList("gcc", "--version"), null);
			int gccxxRet = executeCommand(testWorkspace, Arrays.asList("g++", "--version"), null);
			int clangRet = executeCommand(testWorkspace, Arrays.asList("clang", "--version"), null);
			int clangxxRet = executeCommand(testWorkspace, Arrays.asList("clang++", "--version"), null);
			if (gccRet == 0 && gccxxRet == 0) {
				System.out.println("***************** CPP Compilation Test found GCC");
				compilerGCCPresent = true;
			}
			if (clangRet == 0 && clangxxRet == 0) {
				System.out.println("***************** CPP Compilation Test found Clang");
				compilerClangPresent = true;
			}
			if (!compilerGCCPresent && !compilerClangPresent) {
				System.out.println(
						"***************** CPP Compilation Test needs a C++ compiler, skipping compilation tests!!!!!!!!!!");
				return;
			}
			buildStuffPresent = true;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			assertThat(false, is(true));
		}
	}

	@Test
	public void exportTest() {
		for (Config config : testProjects) {
			try {
				generateCPP(config, exportTestProjectPrefix, false);
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
				String projectName = generateCPP(config, compileTestProjectPrefix, true);
				if (buildStuffPresent) {
					compileCPP(projectName);
				}
			} catch (Exception e) {
				e.printStackTrace();
				assertThat(false, is(true));
			}
		}
	}

	private static int executeCommand(String directory, List<String> strings, Map<String, String> environment)
			throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(strings);
		if (environment != null) {
			processBuilder.environment().putAll(environment);
		}
		processBuilder.inheritIO();
		processBuilder.directory(new File(directory));
		Process process = processBuilder.start();
		return process.waitFor();
	}

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

	private static void compileCPP(String testProjectName) throws Exception {
		List<Map<String, String>> compileEnvironments = new ArrayList<Map<String, String>>();
		if (compilerGCCPresent) {
			Map<String, String> env = new TreeMap<String, String>();
			env.put("CC", "gcc");
			env.put("CXX", "g++");
			compileEnvironments.add(env);
		}
		if (compilerClangPresent) {
			Map<String, String> env = new TreeMap<String, String>();
			env.put("CC", "clang");
			env.put("CXX", "clang++");
			compileEnvironments.add(env);
		}

		for (Map<String, String> compileEnv : compileEnvironments) {
			for (String modeStr : new String[] { "Debug", "Release" }) {
				System.out.println("***************** CPP Compilation Test started on " + testProjectName + " "
						+ compileEnv.get("CC") + modeStr);
				String buildDir = testWorkspace + "/" + testProjectName + "/" + buildDirPrefix + compileEnv.get("CC")
						+ modeStr;
				File buildDirFile = new File(buildDir);
				boolean wasCreated = buildDirFile.mkdir();
				assertThat(wasCreated, is(true));
				int cmakeRetCode = executeCommand(buildDir,
						Arrays.asList("cmake", "-G", "Ninja", "-DCMAKE_BUILD_TYPE=" + modeStr, ".."), compileEnv);
				if (cmakeRetCode != 0) {
					throw new Exception("***************** CMake command failed with exit code: " + cmakeRetCode);
				}
				int ninjaRetCode = executeCommand(buildDir, Arrays.asList("ninja", "-v"), compileEnv);
				if (ninjaRetCode != 0) {
					throw new Exception("***************** Ninja build failed with exit code: " + ninjaRetCode);
				}
				System.out.println("***************** CPP Compilation Test successful on " + testProjectName + " "
						+ compileEnv.get("CC") + modeStr);
			}
		}
	}

}
