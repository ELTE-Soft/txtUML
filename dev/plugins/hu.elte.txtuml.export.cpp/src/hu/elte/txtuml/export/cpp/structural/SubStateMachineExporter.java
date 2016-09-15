package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.uml2.uml.Region;
import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.utils.Pair;

class SubStateMachineExporter extends StateMachineExporter{

	private String parentClassName;
	private Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>

	private SubStateMachineExporter subStateMachineExporter;
	
	SubStateMachineExporter() {}
	
	@Override
	public boolean ownStateMachine() {
		return true;
	}
	
	void setRegion(Region region) {
		this.stateMachineRegion = region;
		createStateList();
	}
	
	void setParentClass(String name) {
		this.parentClassName = name;
	}

	void createSubSmSource(String destination) throws FileNotFoundException, UnsupportedEncodingException {
		super.init();
		String source = "";
		submachineMap = getSubMachines();
		entryExitFunctionExporter.createEntryFunctionTypeMap();
		entryExitFunctionExporter.createExitFunctionTypeMap();

		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			subStateMachineExporter = new SubStateMachineExporter();
			subStateMachineExporter.setRegion(entry.getValue().getSecond());
			subStateMachineExporter.setName(entry.getValue().getFirst());
			subStateMachineExporter.setParentClass(parentClassName);			
			subStateMachineExporter.createSubSmSource(destination);
		}

		source = createSubSmClassHeaderSource();
		Shared.writeOutSource(destination, GenerationTemplates.headerName(className),
				GenerationTemplates.headerGuard(source, className));
		source = createSubSmClassCppSource().toString();

		String dependencyIncludes = GenerationTemplates.cppInclude(className);
		dependencyIncludes = GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude)
				+ dependencyIncludes + GenerationTemplates.cppInclude(className);

		Shared.writeOutSource(destination, GenerationTemplates.sourceName(className),
				dependencyIncludes + "\n" + source);
	}

	private String createSubSmClassHeaderSource() {
		String source = "";
		StringBuilder dependency = new StringBuilder(GenerationTemplates.cppInclude(parentClassName));
		dependency.append(GenerationTemplates.cppInclude(GenerationTemplates.StandardFunctionsHeader));

		StringBuilder privateParts = entryExitFunctionExporter.createEntryFunctionsDecl();
		privateParts.append(entryExitFunctionExporter.createExitFunctionsDecl());
		privateParts.append(GenerationTemplates.formatSubSmFunctions(guardExporter.declareGuardFunctions(stateMachineRegion).toString()));
		privateParts.append(transitionExporter.createTransitionFunctionDecl());
		String protectedParts = "";

		StringBuilder publicParts = new StringBuilder("");
		List<String> params = new ArrayList<String>();
		params.add(parentClassName);
		publicParts.append(GenerationTemplates.constructorDecl(className, params));
		publicParts.append(
				GenerationTemplates.stateEnum(stateList, getInitialStateName()));

		if (submachineMap.isEmpty()) {
			source = GenerationTemplates.simpleSubStateMachineClassHeader(dependency.toString(), className,
					parentClassName, publicParts.toString(), protectedParts, privateParts.toString()).toString();
		} else {
			source = GenerationTemplates
					.hierarchicalSubStateMachineClassHeader(dependency.toString(), className, parentClassName,
							getSubmachines(), publicParts.toString(), protectedParts, privateParts.toString())
					.toString();
		}
		return source;
	}

	private StringBuilder createSubSmClassCppSource() {
		StringBuilder source = new StringBuilder("");
		if (submachineMap.isEmpty()) {
			source.append(GenerationTemplates.simpleSubStateMachineClassConstructor(className, parentClassName,
					stateMachineMap,getInitialStateName()));
		} else {
			source.append(GenerationTemplates.hierarchicalSubStateMachineClassConstructor(className,
					parentClassName, stateMachineMap, getEventSubmachineNameMap(), getInitialStateName()));
		}
		StringBuilder subSmSpec = entryExitFunctionExporter.createEntryFunctionsDef();
		subSmSpec.append(entryExitFunctionExporter.createExitFunctionsDef());
		subSmSpec.append(guardExporter.defnieGuardFunctions(className));
		subSmSpec.append(transitionExporter.createTransitionFunctionsDef());
		subSmSpec.append(GenerationTemplates.entry(className, createStateActionMap(entryExitFunctionExporter.getEntryMap())) + "\n");
		subSmSpec.append(GenerationTemplates.exit(className, createStateActionMap(entryExitFunctionExporter.getExitMap())) + "\n");

		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));
		return source;
	}


}
