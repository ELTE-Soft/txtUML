package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

/**
 * Generates a standard CMake file for exporting into build systems / IDEs
 * examples (on Linux-like OS, cd into cpp-gen folder):
 * 
 * - debug build via ninja-build using the standard compiler (gcc, g++)
 * 
 * cmake -G Ninja -D CMAKE_BUILD_TYPE=Debug . && ninja -v
 * 
 * - release build via make using clang
 * 
 * CC=clang CXX=clang++ cmake -G "Unix Makefiles" -D CMAKE_BUILD_TYPE=Release .
 * && make
 */
class CMakeSupport {
	private static final String CMAKE_MINIMUM_VERSION = "2.8.12";
	private static final String CMAKE_FILE_NAME = "CMakeLists.txt";

	private static final String CPP_STANDARD = "c++11";
	private static final String STRICTLY_NO_WARNINGS = "-Wall -pedantic -Wextra -Wconversion -Werror";
	private static final String STRICTLY_NO_WARNINGS_WIN = "/W3 /WX";

	private static final String DEBUG_ONLY_COMPILE_OPTIONS = "-fsanitize=address";
	private static final String DEBUG_ONLY_COMPILE_OPTIONS_WIN = "/RTC";
	// could be "-flto" or "-pg"
	private static final String RELEASE_ONLY_COMPILE_OPTIONS = "-flto";
	private static final String RELEASE_ONLY_COMPILE_OPTIONS_WIN = "/GL";

	private static final String DEBUG_ONLY_LINK_FLAGS = DEBUG_ONLY_COMPILE_OPTIONS;
	private static final String DEBUG_ONLY_LINK_FLAGS_WIN = "";
	private static final String RELEASE_ONLY_LINK_FLAGS = RELEASE_ONLY_COMPILE_OPTIONS;
	private static final String RELEASE_ONLY_LINK_FLAGS_WIN = "/LTCG";

	private String targetRootPath;
	private List<String> executableTargetNames = new ArrayList<String>();
	private List<List<String>> executableTargetSourceNames = new ArrayList<List<String>>();
	private List<String> staticLibraryTargetNames = new ArrayList<String>();
	private List<List<String>> staticLibraryTargetSourceNames = new ArrayList<List<String>>();
	private List<String> includeDirectories = new ArrayList<String>();

	CMakeSupport(String targetRootPath) {
		this.targetRootPath = targetRootPath;
	}

	static String convertToPosixPath(String path) {
		return path.replace('\\', '/');
	}

	void addExecutableTarget(String executableTargetName, List<String> executableTargetClasses, String pathPrefix) {
		assert executableTargetClasses.size() > 0;
		executableTargetNames.add(executableTargetName);
		List<String> fileNames = new ArrayList<String>();
		for (String className : executableTargetClasses) {
			fileNames.add(convertToPosixPath(pathPrefix + GenerationTemplates.sourceName(className)));
		}
		executableTargetSourceNames.add(fileNames);
	}

	void addStaticLibraryTarget(String staticLibraryTargetName, List<String> staticLibraryTargetClasses,
			String pathPrefix) {
		assert staticLibraryTargetClasses.size() > 0;
		staticLibraryTargetNames.add(staticLibraryTargetName);
		List<String> fileNames = new ArrayList<String>();
		for (String className : staticLibraryTargetClasses) {
			fileNames.add(convertToPosixPath(pathPrefix + GenerationTemplates.sourceName(className)));
		}
		staticLibraryTargetSourceNames.add(fileNames);
	}

	void addIncludeDirectory(String includeDirectory) {
		includeDirectories.add(convertToPosixPath(includeDirectory));
	}

	private static void addEnvironmentConfiguration(StringBuilder output) {
		output.append("if(${CMAKE_CXX_COMPILER_ID} STREQUAL \"GNU\")\n");
		output.append("  set(CMAKE_AR gcc-ar)\n");
		output.append("  set(CMAKE_RANLIB gcc-ranlib)\n");
		output.append("elseif(${CMAKE_CXX_COMPILER_ID} STREQUAL \"Clang\")\n");
		output.append("  set(CMAKE_AR llvm-ar)\n");
		output.append("  set(CMAKE_RANLIB llvm-ranlib)\n");
		output.append("endif()\n");
	}

	private static void addCompileOption(StringBuilder output, String compileOption, boolean optional,
			boolean debugOnly, boolean releaseOnly) {
		if (optional) {
			String optionName = compileOption.replace("-", "").replace("/", "").replace('=', '_').replace('+', '_')
					.toUpperCase().trim();
			output.append("  CHECK_CXX_COMPILER_FLAG(");
			output.append(compileOption);
			output.append(" ");
			output.append(optionName);
			output.append(")\n  if(");
			output.append(optionName);
			output.append(")\n  ");
		}
		output.append("  add_compile_options(");
		assert !debugOnly || !releaseOnly;
		if (debugOnly) {
			output.append("$<$<CONFIG:Debug>:");
		}
		if (releaseOnly) {
			output.append("$<$<CONFIG:Release>:");
		}
		output.append(compileOption);
		if (debugOnly || releaseOnly) {
			output.append(">");
		}
		output.append(")\n");
		if (optional) {
			output.append("  endif()\n");
		}
	}

	void writeOutCMakeLists() throws FileNotFoundException, UnsupportedEncodingException {
		assert executableTargetNames.size() > 0;
		assert executableTargetNames.size() == executableTargetSourceNames.size();
		assert staticLibraryTargetNames.size() == staticLibraryTargetSourceNames.size();
		StringBuilder fileContent = new StringBuilder();

		// preambulum
		fileContent.append("cmake_minimum_required(VERSION " + CMAKE_MINIMUM_VERSION + ")\n");
		String projectName = targetRootPath.substring(targetRootPath.lastIndexOf(File.separator) + 1).replace('.', '_');
		fileContent.append("project(" + projectName + " CXX C)\n");
		fileContent.append("find_package(Threads)\n");
		fileContent.append("include(CheckCXXCompilerFlag)\n");

		addEnvironmentConfiguration(fileContent);

		fileContent.append("if(MSVC)\n");

		addCompileOption(fileContent, STRICTLY_NO_WARNINGS_WIN, false, false, false);
		if (DEBUG_ONLY_COMPILE_OPTIONS_WIN.length() > 0) {
			addCompileOption(fileContent, DEBUG_ONLY_COMPILE_OPTIONS_WIN, false, true, false);
		}
		if (RELEASE_ONLY_COMPILE_OPTIONS_WIN.length() > 0) {
			addCompileOption(fileContent, RELEASE_ONLY_COMPILE_OPTIONS_WIN, false, false, true);
		}

		fileContent.append("else()\n");

		addCompileOption(fileContent, "-std=" + CPP_STANDARD, false, false, false);
		addCompileOption(fileContent, STRICTLY_NO_WARNINGS, false, false, false);
		// TODO remove these later on
		addCompileOption(fileContent, "-Wno-error=unused-parameter", false, false, false);
		addCompileOption(fileContent, "-Wno-error=unused-variable", false, false, false);
		// only clang supports it
		addCompileOption(fileContent, "-Wno-error=unused-private-field", true, false, false);
		if (DEBUG_ONLY_COMPILE_OPTIONS.length() > 0) {
			addCompileOption(fileContent, DEBUG_ONLY_COMPILE_OPTIONS, false, true, false);
		}
		if (RELEASE_ONLY_COMPILE_OPTIONS.length() > 0) {
			addCompileOption(fileContent, RELEASE_ONLY_COMPILE_OPTIONS, false, false, true);
		}

		fileContent.append("endif()\n");

		fileContent.append("include_directories(.)\n");
		for (String includeDirectory : includeDirectories) {
			fileContent.append("include_directories(" + includeDirectory + ")\n");
		}

		// targets
		for (int i = 0; i < staticLibraryTargetNames.size(); i++) {
			fileContent.append("set(LIB_CONTENT_" + staticLibraryTargetNames.get(i));
			for (String fileName : staticLibraryTargetSourceNames.get(i)) {
				fileContent.append(" \"" + fileName + "\"");
			}
			fileContent.append(")\n");

			fileContent.append("add_library(").append(staticLibraryTargetNames.get(i)).append(" STATIC ")
					.append("${LIB_CONTENT_").append(staticLibraryTargetNames.get(i));
			fileContent.append("})\n");
		}

		for (int i = 0; i < executableTargetNames.size(); i++) {
			String targetName = executableTargetNames.get(i);
			
			fileContent.append("set(EXEC_CONTENT_" + executableTargetNames.get(i));
			
			boolean hasMainCpp = false;
			
			for (String fileName : executableTargetSourceNames.get(i)) {
				if (!fileName.equals("main.cpp")) {
					fileContent.append(" \"" + fileName + "\"");
				} else {
					hasMainCpp = true;
				}
			}
			fileContent.append(")\n");
			
			fileContent.append("add_executable(" + targetName).append(" ${EXEC_CONTENT_" + executableTargetNames.get(i));
			fileContent.append("}").append(hasMainCpp ? " main.cpp" : "").append(")\n");

			if (staticLibraryTargetNames.size() > 0) {
				fileContent.append("target_link_libraries(" + targetName);
				for (String libTargetName : staticLibraryTargetNames) {
					fileContent.append(" " + libTargetName);
				}
				fileContent.append(")\n");
			}

			fileContent.append("if(MSVC)\n");

			if (DEBUG_ONLY_LINK_FLAGS_WIN.length() > 0) {
				fileContent.append("  set_target_properties(" + targetName);
				fileContent.append(" PROPERTIES LINK_FLAGS_DEBUG \"" + DEBUG_ONLY_LINK_FLAGS_WIN + "\")\n");
			}
			if (RELEASE_ONLY_LINK_FLAGS_WIN.length() > 0) {
				fileContent.append("  set_target_properties(" + targetName);
				fileContent.append(" PROPERTIES LINK_FLAGS_RELEASE \"" + RELEASE_ONLY_LINK_FLAGS_WIN + "\")\n");
			}

			fileContent.append("else()\n");

			if (DEBUG_ONLY_LINK_FLAGS.length() > 0) {
				fileContent.append("  set_target_properties(" + targetName);
				fileContent.append(" PROPERTIES LINK_FLAGS_DEBUG \"" + DEBUG_ONLY_LINK_FLAGS + "\")\n");
			}
			if (RELEASE_ONLY_LINK_FLAGS.length() > 0) {
				fileContent.append("  set_target_properties(" + targetName);
				fileContent.append(" PROPERTIES LINK_FLAGS_RELEASE \"" + RELEASE_ONLY_LINK_FLAGS + "\")\n");
			}

			fileContent.append("endif()\n");

			fileContent.append("if(Threads_FOUND)\n");
			fileContent.append("  target_link_libraries(");
			fileContent.append(targetName);
			fileContent.append(" \"${CMAKE_THREAD_LIBS_INIT}\")\n");
			fileContent.append("endif()\n");
		}

		CppExporterUtils.writeOutSource(targetRootPath, CMAKE_FILE_NAME, fileContent.toString());
	}
}
