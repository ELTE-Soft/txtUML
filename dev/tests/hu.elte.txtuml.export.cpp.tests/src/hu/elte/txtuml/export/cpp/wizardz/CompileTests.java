package hu.elte.txtuml.export.cpp.wizardz;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.Uml2ToCppExporter;
import hu.elte.txtuml.utils.Logger;

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
		final List<String> expectedLines;
		final Boolean isDeterministic;
		final String mainFileName;

		Config(String project, String model, String deployment, List<String> expectedLines, Boolean isDeterministic, String mainFileName) {
			this.project = project;
			this.model = model;
			this.deployment = deployment;
			this.expectedLines = expectedLines;
			this.isDeterministic = isDeterministic;
			this.mainFileName = mainFileName;
		}
	}

	private static final String PATH_TO_PROJECTS = "../../../examples/demo/";

	private static final Config[] TEST_PROJECTS = {
			new Config("machine", "machine1.j.model", "machine1.j.cpp.Machine1Configuration", DemoExpectedLines.MACHINE.getLines(), true, "main.cpp"),
			new Config("monitoring", "monitoring.x.model", "monitoring.x.cpp.XMonitoringConfiguration", DemoExpectedLines.MONITORING.getLines(), false, "mainTest.cpp"),
			new Config("producer_consumer", "producer_consumer.j.model",
					"producer_consumer.j.cpp.ProducerConsumerConfiguration", DemoExpectedLines.PRODUCER_CONSUMER.getLines(), false, "main.cpp"),
			new Config("train", "train.j.model", "train.j.cpp.TrainConfiguration", DemoExpectedLines.TRAIN.getLines(), false, "mainTest.cpp"),
			new Config("pingpong", "pingpong.j.model", "pingpong.j.cpp.PingPongConfiguration", DemoExpectedLines.PINGPONG.getLines(), true, "main.cpp")};
	
	private static final String EXPORT_TEST_PROJECT_PREFIX = "exportTest_";
	private static final String COMPILE_TEST_PROJECT_PREFIX = "compileTest_";
	private static final String BUILD_DIR_PREFIX = "build_";

	private static final String RELATIVE_PATH_TO_STDLIB = "../../../dev/plugins/hu.elte.txtuml.api.stdlib";
	private static final String MAIN_OUTPUT_FILE = "mainOutput.txt";

	private static String testWorkspace = "target/work/data/";
	private static boolean buildStuffPresent = false;
	private static boolean compilerGCCPresent = false;
	private static boolean compilerClangPresent = false;

	private static IProject stdLibProject;
	
	@BeforeClass
	public static void detectCPPEnvironment() {
		try {
			testWorkspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getCanonicalPath() + "/";
		} catch (Exception e1) {
			System.out.println(e1);
		}
		System.out.println("***************** CPP Compilation Test probing environment in workspace " + testWorkspace);

		int cmakeRet = -1;
		int ninjaRet = -1;
		int gccRet = -1;
		int gccxxRet = -1;
		int clangRet = -1;
		int clangxxRet = -1;
		try {
			cmakeRet = CppExporterUtils.executeCommand(testWorkspace, Arrays.asList("cmake", "--version"), null, null);
			ninjaRet = CppExporterUtils.executeCommand(testWorkspace, Arrays.asList("ninja", "--version"), null, null);
		} catch (IOException | InterruptedException e) {
		}
		if (cmakeRet != 0 || ninjaRet != 0) {
			System.out.println(
					"***************** CPP Compilation Test needs cmake and ninja, skipping compilation tests!!!!!!!!!!");
			return;
		}

		try {
			gccRet = CppExporterUtils.executeCommand(testWorkspace, Arrays.asList("gcc", "--version"), null, null);
			gccxxRet = CppExporterUtils.executeCommand(testWorkspace, Arrays.asList("g++", "--version"), null, null);
			if(!CppExporterUtils.isWindowsOS()){
				clangRet = CppExporterUtils.executeCommand(testWorkspace, Arrays.asList("clang", "--version"), null, null);
				clangxxRet = CppExporterUtils.executeCommand(testWorkspace, Arrays.asList("clang++", "--version"), null, null);
			}
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
	


	@BeforeClass
	public static void importStdLibIntoWorkspace() {
		try {
			String canonicalPath = new File(RELATIVE_PATH_TO_STDLIB).getCanonicalPath();
			IProjectDescription desc = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(new Path(canonicalPath + "/.project"));
			desc.setLocation(new Path(canonicalPath));
			stdLibProject = ResourcesPlugin.getWorkspace().getRoot().getProject(desc.getName());
			if (!stdLibProject.exists()) {
				stdLibProject.create(desc, null);
			}
			stdLibProject.open(null);
		} catch (Exception e) {
			Logger.sys.error("Couldn't import stdlib project into workspace", e);
		}
	}
	
	@AfterClass
	public static void removeStdLibFromWorkspace() {
		try {
			stdLibProject.close(null);
			stdLibProject.delete(false, false, null);
		} catch (Exception e) {
			Logger.sys.error("Couldn't remove stdlib project from workspace", e);
		}
	}

	@Test
	public void exportTest() {
		for (Config config : TEST_PROJECTS) {
			try {
				generateCPP(config, EXPORT_TEST_PROJECT_PREFIX, false, true);
			} catch (Exception e) {
				Logger.sys.error("", e);
				assertThat(false, is(true));
			}
		}
	}

	@Test(timeout=720 * 1000)
	public void compileTest() {
		for (Config config : TEST_PROJECTS) {
			try {
				String projectName = generateCPP(config, COMPILE_TEST_PROJECT_PREFIX, true, true);
				if (buildStuffPresent) {
					compileCPP(projectName, config);
				}
			} catch (Exception e) {
				Logger.sys.error("", e);
				assertThat(false, is(true));
			}
		}
	}

	

	private static String generateCPP(Config config, String testPrefix, boolean addRuntime, boolean overWriteMainFile)
			throws Exception {
		TxtUMLToCppGovernor cppgen = new TxtUMLToCppGovernor(true);

		String canPathToProjects = new File(PATH_TO_PROJECTS).getCanonicalPath();
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

		cppgen.uml2ToCpp(testProject, config.model, config.deployment, testProject, 
				addRuntime, overWriteMainFile, null);

		project.close(null);
		project.delete(false, false, null);
		return testProject;
	}

	private static void compileCPP(String testProjectName, Config config) throws Exception {
		List<Map<String, String>> compileEnvironments = new ArrayList<Map<String, String>>();
		if (compilerGCCPresent) {
			Map<String, String> env = new TreeMap<String, String>();
			env.put("CC", "gcc");
			env.put("CXX", "g++");
			compileEnvironments.add(env);
		}
		if (compilerClangPresent && !CppExporterUtils.isWindowsOS()) {
			Map<String, String> env = new TreeMap<String, String>();
			env.put("CC", "clang");
			env.put("CXX", "clang++");
			compileEnvironments.add(env);
		}

		for (Map<String, String> compileEnv : compileEnvironments) {
			String modeStr = "Debug";
			
			System.out.println("***************** CPP Compilation Test started on " + testProjectName + " "
					+ compileEnv.get("CC").split(" ")[0] + modeStr);
			String buildDir = testWorkspace + "/" + testProjectName + "/"
					+ Uml2ToCppExporter.GENERATED_CPP_FOLDER_NAME + "/" + config.model + "/" + BUILD_DIR_PREFIX
					+ compileEnv.get("CC").split(" ")[0] + modeStr;
			File buildDirFile = new File(buildDir);
			boolean wasCreated = buildDirFile.mkdir();
			assertThat(wasCreated, is(true));
			
			// Copy main.cpp for build
			List<String> tmpDirList = new LinkedList<String>(Arrays.asList(buildDir.split("/"))); // getting actual path
			if(tmpDirList.size() > 3){
				tmpDirList.remove(tmpDirList.size() - 1); 
				List<String> destinationDirList = new LinkedList<String>(tmpDirList);
				for(int i = 0; i < 2; ++i){
					tmpDirList.remove(tmpDirList.size() - 1); // go back for src 
				}
				String destinationDir = destinationDirList.stream().collect(Collectors.joining("/")); // destination path
				tmpDirList.add("src");
				String initDir = tmpDirList.stream().collect(Collectors.joining("/")); // search init path
				File mainFile = searchFile(new File(initDir), config.mainFileName); // search main.cpp
					
				if(mainFile != null){
					Files.copy(Paths.get(mainFile.getCanonicalPath()), Paths.get(destinationDir + "/main.cpp"), StandardCopyOption.REPLACE_EXISTING);
				}
			}
			
			int cmakeRetCode = CppExporterUtils.executeCommand(buildDir,
					Arrays.asList("cmake", "-G", "Ninja", "-DCMAKE_BUILD_TYPE=" + modeStr, ".."), compileEnv, null);
			assertThat(cmakeRetCode, is(0));
			int ninjaRetCode = CppExporterUtils.executeCommand(buildDir, Arrays.asList("ninja", "-v"), compileEnv, null);
			assertThat(ninjaRetCode, is(0));
			
			String bash = CppExporterUtils.isWindowsOS() ? "cmd.exe" : "/bin/bash";
			String mainBinary = CppExporterUtils.isWindowsOS() ? "main.exe" : "./main";
			String c = CppExporterUtils.isWindowsOS() ? "/c" : "-c";
			
			int mainRetCode = CppExporterUtils.executeCommand(buildDir, Arrays.asList(bash, c, mainBinary), compileEnv, MAIN_OUTPUT_FILE);
			assertThat(mainRetCode, is(0));
				
			System.out.println("***************** CPP Compilation Test successful on " + testProjectName + " "
					+ compileEnv.get("CC").split(" ")[0] + modeStr);
			
			File outputFile = new File(buildDir + File.separator + "mainOutput.txt");
			if(modeStr.equals("Debug") && outputFile != null && outputFile.length() > 0){
				assertFiles(outputFile, config.expectedLines, config.isDeterministic);
			}
		}
	}
	
	private static void assertFiles(File outputFile, List<String> expectedLines, Boolean isDeterministic) {
		System.out.println("***************** Asserting output files started");
		
		try (Stream<String> stream = Files.lines(Paths.get(outputFile.toURI()))) {
			List<String> lines =  stream.collect(Collectors.toList());
						
			if(isDeterministic) {
				if(lines.size() != expectedLines.size()) {
					System.out.println("***************** Asserting output files failed, different sizes");
					assertThat(lines.size(), is(expectedLines.size()));
				}
				for(int i = 0; i < lines.size(); ++i) {
					if(!lines.get(i).trim().equals(expectedLines.get(i).trim())) {
						logAssertFilesFailed(i + 1, lines.get(i).trim(), expectedLines.get(i).trim());
					}
					assertThat(lines.get(i).trim(), is(expectedLines.get(i).trim()));
				}
			}
			else{
				Set<String> nonDeterministicSet = new HashSet<String>(lines);
				for(int i = 0; i < expectedLines.size(); ++i) {
					if(!nonDeterministicSet.contains(expectedLines.get(i).trim())) {
						logAssertFilesFailed(i + 1, null, expectedLines.get(i).trim());
						assertThat(true, is(false));
					}
				}
			}
			System.out.println("***************** Asserting output files successed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void logAssertFilesFailed(Integer rowIndex, String cppOutput, String expectedOutput){
		if(rowIndex != null){
			System.out.println("***************** Asserting output files failed at line " + rowIndex + ".:");
		}
		if(cppOutput != null){
			System.out.println("CPP output: " + cppOutput);
		}
		if(expectedOutput != null){
			System.out.println("Expected output: " + expectedOutput);
		}
	}
	
	// Searches file recursively
	private static File searchFile(File file, String search) {
	    if (file.isDirectory()) {
	        File[] arr = file.listFiles();
	        for (File f : arr) {
	            File found = searchFile(f, search);
	            if (found != null)
	                return found;
	        }
	    } else {
	        if (file.getName().equals(search)) {
	            return file;
	        }
	    }
	    return null;
	}
}
