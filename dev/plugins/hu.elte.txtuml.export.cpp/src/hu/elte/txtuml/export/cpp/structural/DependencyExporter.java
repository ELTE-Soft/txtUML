package hu.elte.txtuml.export.cpp.structural;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class DependencyExporter {

	private Set<String> dependecies;

	public DependencyExporter() {
		if(!simpleDependenciesRegistered) {
			registerSimpleDependencies();
		}
		dependecies = new HashSet<String>();
	}

	public String createDependencyCppIncludeCode(String className) {
		StringBuilder includes = new StringBuilder("");
		if (!dependecies.contains(className)) {
			includes.append(PrivateFunctionalTemplates.include(className));
		}
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

		return includes.toString();

	}

	public void addDependecy(String depndency) {
		if (!isSimpleDependency(depndency)) {
			dependecies.add(depndency);
		}
		
	}

	public void addDependencies(Collection<String> dependecies) {
		for (String dependency : dependecies) {
			addDependecy(dependency);
		}
	}
	
	private boolean isSimpleDependency(String typename) {
		return simpleDependencyNames.contains(typename);
	}
	
	private static Set<String> simpleDependencyNames;
	private boolean simpleDependenciesRegistered = false;
	private void registerSimpleDependencies() {
		simpleDependencyNames = new HashSet<String>();
		simpleDependencyNames.add("Integer");
		simpleDependencyNames.add("Real");
		simpleDependencyNames.add("Boolean");
		simpleDependencyNames.add("String");
		
		simpleDependenciesRegistered = true;
	}

}
