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

	private Set<String> dependecies;
	private Set<String> headerOnlyDependency;
	private Set<String> cppOnlyDependency;

	public DependencyExporter() {
		dependecies = new HashSet<String>();
		headerOnlyDependency = new HashSet<>();
		cppOnlyDependency = new HashSet<>();
	}

	public String createDependencyCppIncludeCode(String className) {
		StringBuilder includes = new StringBuilder("");
		includes.append(PrivateFunctionalTemplates.include(className));
		dependecies.forEach(type -> {
			includes.append(PrivateFunctionalTemplates.include(type));
		});
		return includes.toString();
	}

	public String createDependencyHeaderIncludeCode() {
		StringBuilder includes = new StringBuilder();
		dependecies.forEach(type -> {
			includes.append(GenerationTemplates.forwardDeclaration(type));
		});
		
		headerOnlyDependency.forEach(type -> {
			includes.append(GenerationTemplates.forwardDeclaration(type));
		});
		

		return includes.toString();

	}

	public void addDependency(String dependency) {
		if (!isSimpleDependency(dependency)) {
			dependecies.add(dependency);
		}

	}

	public void addDependencies(Collection<String> dependecies) {
		for (String dependency : dependecies) {
			addDependency(dependency);
		}
	}
	
	public void addHeaderOnlyDependency(String dependency) {
		if(!dependecies.contains(dependency)) {
			headerOnlyDependency.add(dependency);
		}
	}
	
	public void addHeaderOnlyDependencies(Collection<String> dependencies) {
		dependencies.forEach(d -> addHeaderOnlyDependency(d));
	}
	
	public void addCppOnlyDependency(String dependency) {
		if(!dependecies.contains(dependency)) {
			cppOnlyDependency.add(dependency);
		}
	}
	
	public void addCppOnlyDependencies(Collection<String> dependencies) {
		dependencies.forEach(d -> addCppOnlyDependency(d));
	}

	private boolean isSimpleDependency(String typename) {
		return standardDependencies.contains(typename);
	}

}
