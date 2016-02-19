package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

class CMakeSupport {
	// TODO gcc and clang is ok, but msvc needs different flags
	private static final String CPP_STANDARD = "c++11";
	private static final String EXTRA_WARNINGS = "-Wall -pedantic -Wextra -Wconversion";
	// TODO remove the unused/parameter option when correct code is generated
	private static final String TREAT_WARNINGS_AS_ERRORS = "-Werror -Wno-error=unused-parameter)";
	private static final String DEBUG_ONLY_COMPILE_OPTIONS = "-fsanitize=address";
	private static final String RELEASE_ONLY_COMPILE_OPTIONS = "-flto"; // "-pg"
	private static final String DEBUG_ONLY_LINK_FLAGS = DEBUG_ONLY_COMPILE_OPTIONS;
	private static final String RELEASE_ONLY_LINK_FLAGS = RELEASE_ONLY_COMPILE_OPTIONS;
	private static final String CMAKE_MINIMUM_VERSION = "2.8";
	private static final String CMAKE_FILE_NAME = "CMakeLists.txt";

	private String targetRootPath;
	private List<String> executableTargetNames = new ArrayList<String>();
	private List<List<String>> executableTargetSourceNames = new ArrayList<List<String>>();
	private List<String> staticLibraryTargetNames = new ArrayList<String>();
	private List<List<String>> staticLibraryTargetSourceNames = new ArrayList<List<String>>();
	private List<String> includeDirectories = new ArrayList<String>();

	CMakeSupport(String targetRootPath) {
		this.targetRootPath = targetRootPath;
	}

	void addExecutableTarget(String executableTargetName, List<String> executableTargetClasses, String pathPrefix) {
		assert executableTargetClasses.size() > 0;
		executableTargetNames.add(executableTargetName);
		List<String> fileNames = new ArrayList<String>();
		for (String className : executableTargetClasses) {
			fileNames.add(pathPrefix + GenerationTemplates.sourceName(className));
		}
		executableTargetSourceNames.add(fileNames);
	}

	void addStaticLibraryTarget(String staticLibraryTargetName, List<String> staticLibraryTargetClasses,
			String pathPrefix) {
		assert staticLibraryTargetClasses.size() > 0;
		staticLibraryTargetNames.add(staticLibraryTargetName);
		List<String> fileNames = new ArrayList<String>();
		for (String className : staticLibraryTargetClasses) {
			fileNames.add(pathPrefix + GenerationTemplates.sourceName(className));
		}
		staticLibraryTargetSourceNames.add(fileNames);
	}

	void addIncludeDirectory(String includeDirectory) {
		includeDirectories.add(includeDirectory);
	}

	void writeOutCMakeLists() throws FileNotFoundException, UnsupportedEncodingException {
		assert executableTargetNames.size() > 0;
		assert executableTargetNames.size() == executableTargetSourceNames.size();
		assert staticLibraryTargetNames.size() == staticLibraryTargetSourceNames.size();
		StringBuilder fileContent = new StringBuilder();

		// preambulum
		fileContent.append("cmake_minimum_required(VERSION " + CMAKE_MINIMUM_VERSION + ")\n");
		String projectName = targetRootPath.substring(targetRootPath.lastIndexOf(File.separator) + 1).replace('.', '_');
		fileContent.append("project(" + projectName + " CXX)\n");

		// compile options
		fileContent.append("add_compile_options(-std=" + CPP_STANDARD + ")\n");
		fileContent.append("add_compile_options(" + EXTRA_WARNINGS + ")\n");
		fileContent.append("add_compile_options(" + TREAT_WARNINGS_AS_ERRORS + ")\n");
		if (DEBUG_ONLY_COMPILE_OPTIONS.length() > 0) {
			fileContent.append("add_compile_options($<$<CONFIG:Debug>:" + DEBUG_ONLY_COMPILE_OPTIONS + ">)\n");
		}
		if (RELEASE_ONLY_COMPILE_OPTIONS.length() > 0) {
			fileContent.append("add_compile_options($<$<CONFIG:Release>:" + RELEASE_ONLY_COMPILE_OPTIONS + ">)\n");
		}
		for (String includeDirectory : includeDirectories) {
			fileContent.append("include_directories(" + includeDirectory + ")\n");
		}

		// targets
		for (int i = 0; i < staticLibraryTargetNames.size(); i++) {
			fileContent.append("add_library(" + staticLibraryTargetNames.get(i) + " STATIC");
			for (String fileName : staticLibraryTargetSourceNames.get(i)) {
				fileContent.append(" " + fileName);
			}
			fileContent.append(")\n");
		}

		for (int i = 0; i < executableTargetNames.size(); i++) {
			String targetName = executableTargetNames.get(i);
			fileContent.append("add_executable(" + targetName);
			for (String fileName : executableTargetSourceNames.get(i)) {
				fileContent.append(" " + fileName);
			}
			fileContent.append(")\n");

			if (staticLibraryTargetNames.size() > 0) {
				fileContent.append("target_link_libraries(" + targetName);
				for (String libTargetName : staticLibraryTargetNames) {
					fileContent.append(" " + libTargetName);
				}
				fileContent.append(")\n");
			}

			if (DEBUG_ONLY_LINK_FLAGS.length() > 0) {
				fileContent.append("set_target_properties(" + targetName);
				fileContent.append(" PROPERTIES LINK_FLAGS_DEBUG \"" + DEBUG_ONLY_LINK_FLAGS + "\")\n");
			}
			if (RELEASE_ONLY_LINK_FLAGS.length() > 0) {
				fileContent.append("set_target_properties(" + targetName);
				fileContent.append(" PROPERTIES LINK_FLAGS_RELEASE \"" + RELEASE_ONLY_LINK_FLAGS + "\")\n");
			}
		}

		Shared.writeOutSource(targetRootPath, CMAKE_FILE_NAME, fileContent.toString());
	}
}
