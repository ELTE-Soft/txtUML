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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;

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
			new Config("machine", "machine1.j.model", "machine1.j.Machine1Configuration"),
			new Config("monitoring", "monitoring.x.model", "monitoring.x.XMonitoringConfiguration"),
			new Config("producer_consumer", "producer_consumer.j.model", "producer_consumer.j.ProducerConsumerConfiguration"),
			new Config("train", "train.j.model", "train.j.TrainConfiguration"), };

	private static final String exportTestProjectPrefix = "exportTest_";
	private static final String compileTestProjectPrefix = "compileTest_";
	private static final String buildDirPrefix = "build_";

	private static String testWorkspace = "target/work/data/";
	private static boolean buildStuffPresent = false;
	private static boolean compilerGCCPresent = false;
	private static boolean compilerClangPresent = false;

	@BeforeClass
	public static void detectCPPEnvironment() {
		try {
			testWorkspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getCanonicalPath() + "/";
		} catch (IOException e1) {
		}
		System.out.println("***************** CPP Compilation Test probing environment in workspace " + testWorkspace);

		int cmakeRet = -1;
		int ninjaRet = -1;
		int gccRet = -1;
		int gccxxRet = -1;
		int clangRet = -1;
		int clangxxRet = -1;
		try {
			cmakeRet = executeCommand(testWorkspace, Arrays.asList("cmake", "--version"), null);
			ninjaRet = executeCommand(testWorkspace, Arrays.asList("ninja", "--version"), null);
		} catch (IOException | InterruptedException e) {
		}
		if (cmakeRet != 0 || ninjaRet != 0) {
			System.out.println(
					"***************** CPP Compilation Test needs cmake and ninja, skipping compilation tests!!!!!!!!!!");
			return;
		}

		try {
			gccRet = executeCommand(testWorkspace, Arrays.asList("gcc", "--version"), null);
			gccxxRet = executeCommand(testWorkspace, Arrays.asList("g++", "--version"), null);
			clangRet = executeCommand(testWorkspace, Arrays.asList("clang", "--version"), null);
			clangxxRet = executeCommand(testWorkspace, Arrays.asList("clang++", "--version"), null);
		} catch (IOException | InterruptedException e) {
		}
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
	}

	@Test
	public void exportTest() {
		for (Config config : testProjects) {
			try {
				generateCPP(config, exportTestProjectPrefix, false,true);
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
				String projectName = generateCPP(config, compileTestProjectPrefix, true,true);
				if (buildStuffPresent) {
					compileCPP(projectName, config.model);
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

	private static String generateCPP(Config config, String testPrefix, boolean addRuntime, boolean overWriteMainFile) throws Exception {
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
		project.delete(false, false, null);

		project = ResourcesPlugin.getWorkspace().getRoot().getProject(testProject);
		IFolder folder = project.getFolder("src-gen");
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
		project.refreshLocal(IProject.DEPTH_INFINITE, new NullProgressMonitor());
		int buildsTriggered = 0;
		int buildsFailed = 0;
		while (buildsTriggered < 3 && buildsFailed < 4) {
			try {
				System.out.println("Calling build round " + buildsTriggered);
				project.build(buildsTriggered == 0 ? IncrementalProjectBuilder.CLEAN_BUILD
						: (buildsTriggered == 1 ? IncrementalProjectBuilder.INCREMENTAL_BUILD
								: IncrementalProjectBuilder.AUTO_BUILD),
						new NullProgressMonitor());
				buildsTriggered++;
			} catch (Exception ex) {
				System.out.println("Whoops, build failed round " + buildsFailed);
				buildsFailed++;
			}
		}

		cppgen.uml2ToCpp(testProject, config.model, config.deployment, testProject, addRuntime,overWriteMainFile);

		project.close(null);
		project.delete(false, false, null);
		return testProject;
	}

	private static void compileCPP(String testProjectName, String modelName) throws Exception {
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

				// TODO Remove this as soon as LLVM distributions put back
				// LLVMgold.so (broken symbolic link in llvm-dev package)
				if (compileEnv.get("CC").equals("clang") && modeStr.equals("Release")) {
					continue;
				}

				System.out.println("***************** CPP Compilation Test started on " + testProjectName + " "
						+ compileEnv.get("CC").split(" ")[0] + modeStr);
				String buildDir = testWorkspace + "/" + testProjectName + "/"
						+ Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME + "/" + modelName + "/" + buildDirPrefix
						+ compileEnv.get("CC").split(" ")[0] + modeStr;
				File buildDirFile = new File(buildDir);
				boolean wasCreated = buildDirFile.mkdir();
				assertThat(wasCreated, is(true));
				int cmakeRetCode = executeCommand(buildDir,
						Arrays.asList("cmake", "-G", "Ninja", "-DCMAKE_BUILD_TYPE=" + modeStr, ".."), compileEnv);
				assertThat(cmakeRetCode, is(0));
				int ninjaRetCode = executeCommand(buildDir, Arrays.asList("ninja", "-v"), compileEnv);
				assertThat(ninjaRetCode, is(0));
				System.out.println("***************** CPP Compilation Test successful on " + testProjectName + " "
						+ compileEnv.get("CC").split(" ")[0] + modeStr);
			}
		}
	}

}
