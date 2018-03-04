package hu.elte.txtuml.export.cpp.structural;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.UMLStdLibNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class DependencyExporter {
	private static Set<String> standardDependencies = new HashSet<>(Arrays.asList(UMLStdLibNames.ModelClassName,
			UMLStdLibNames.UMLInteger, UMLStdLibNames.UMLBoolean, UMLStdLibNames.UMLReal, UMLStdLibNames.UMLString));

	private Set<String> dependencies;
	private Set<String> headerOnlyDependency;
	private Set<String> cppOnlyDependency;
	private Set<String> headerOnlyIncludeDependency;

	public DependencyExporter() {
		dependencies = new HashSet<String>();
		headerOnlyDependency = new HashSet<>();
		cppOnlyDependency = new HashSet<>();
		headerOnlyIncludeDependency = new HashSet<>();
	}

	public String createDependencyCppIncludeCode(String className) {
		StringBuilder includes = new StringBuilder("");
		includes.append(PrivateFunctionalTemplates.include(className));
		dependencies.forEach(type -> {
			includes.append(PrivateFunctionalTemplates.include(type));
		});

		cppOnlyDependency.forEach(type -> {
			includes.append(PrivateFunctionalTemplates.include(type));
		});

		return includes.toString();
	}

	public String createDependencyHeaderIncludeCode(String preDeclNamespace) {
		StringBuilder preDeclerations = new StringBuilder();
		StringBuilder includes = new StringBuilder();

		dependencies.forEach(type -> {
			preDeclerations.append(GenerationTemplates.forwardDeclaration(type));
		});

		headerOnlyDependency.forEach(type -> {
			preDeclerations.append(GenerationTemplates.forwardDeclaration(type));
		});

		headerOnlyIncludeDependency.forEach(type -> {
			includes.append(PrivateFunctionalTemplates.include(type));
		});

		return includes.toString() + GenerationTemplates.putNamespace(preDeclerations.toString(), preDeclNamespace);

	}

	public void addDependency(String dependency) {
		if (!isSimpleDependency(dependency)) {
			dependencies.add(dependency);
		}

	}

	public void addDependencies(Collection<String> dependecies) {
		dependencies.forEach(this::addDependency);
	}

	public void addHeaderOnlyDependency(String dependency) {
		if (!dependencies.contains(dependency)) {
			headerOnlyDependency.add(dependency);
		}
	}

	public void addHeaderOnlyDependencies(Collection<String> dependencies) {
		dependencies.forEach(this::addHeaderOnlyDependency);
	}

	public void addHeaderOnlyIncludeDependency(String dependency) {
		headerOnlyIncludeDependency.add(dependency);
	}

	public void addHeaderOnlyIncludeDependencies(Collection<String> dependencies) {
		dependencies.forEach(this::addHeaderOnlyIncludeDependency);
	}

	public void addCppOnlyDependency(String dependency) {
		if (!dependencies.contains(dependency)) {
			cppOnlyDependency.add(dependency);
		}
	}

	public void addCppOnlyDependencies(Collection<String> dependencies) {
		dependencies.forEach(this::addCppOnlyDependency);
	}

	private boolean isSimpleDependency(String typename) {
		return standardDependencies.contains(typename);
	}

}
