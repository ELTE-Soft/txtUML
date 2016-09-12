package hu.elte.txtuml.export.cpp.structural;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

class DependencyExporter {
	private Set<String> dependecies;
	
	DependencyExporter() {
		dependecies = new HashSet<String>();
	}
	
	public String createDependencyIncudesCode(boolean isHeader) {
		StringBuilder includes = new StringBuilder("");
		for(String type : dependecies) {
			if(isHeader) {
				includes.append(GenerationTemplates.forwardDeclaration(type));
			}
			else {
				includes.append(GenerationTemplates.cppInclude(type));
			}
		}
		
		return includes.toString();
	}
	
	public void addDependecy(String depndency) {
		this.dependecies.add(depndency);
	}
	
	public void addDependecies(Collection<String> dependecies) {
		this.dependecies.addAll(dependecies);
	}

}
