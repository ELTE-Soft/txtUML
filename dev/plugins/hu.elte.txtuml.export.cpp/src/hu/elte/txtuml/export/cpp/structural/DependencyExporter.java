package hu.elte.txtuml.export.cpp.structural;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class DependencyExporter {
	private Set<String> dependecies;

	public DependencyExporter() {
		dependecies = new HashSet<String>();
	}

	public String createDependencyCppIncudesCode(String className) {
		StringBuilder includes = new StringBuilder("");
		if (!dependecies.contains(className)) {
			includes.append(GenerationTemplates.cppInclude(className));
		}
		dependecies.forEach(type -> {
			includes.append(GenerationTemplates.cppInclude(type));
		});
		return includes.toString();
	}

	public String createDependecyHeaderIncludeCode() {
		StringBuilder includes = new StringBuilder();
		dependecies.forEach(type -> {
			includes.append(GenerationTemplates.forwardDeclaration(type));
		});

		return includes.toString();

	}

	public void addDependecy(String depndency) {
		if (!Shared.isBasicType(depndency))
			this.dependecies.add(depndency);

	}

	public void addDependecies(Collection<String> dependecies) {
		for (String dependency : dependecies) {
			addDependecy(dependency);
		}
	}

}
