package hu.elte.txtuml.export.cpp.statemachine;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.structural.DependencyExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ConstructorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.utils.Pair;

public class SubStateMachineExporter extends StateMachineExporterBase {

	private Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>

	public SubStateMachineExporter() {
		super();
	}

	public void setRegion(Region region) {
		this.stateMachineRegion = region;
		createStateList();
	}

	public void createSubSmSource(String destination) throws FileNotFoundException, UnsupportedEncodingException {
		super.createMachine();
		String source = "";
		submachineMap = getSubMachines();
		createSubMachineSources(destination);
		
		source = createSubSmClassHeaderSource();
		CppExporterUtils.writeOutSource(destination, GenerationTemplates.headerName(ownerClassName),
				HeaderTemplates.headerGuard(source, ownerClassName));

		source = GenerationTemplates.putNamespace(createSubSmClassCppSource(), GenerationNames.Namespaces.ModelNamespace);
		StringBuilder dependencyIncludes = new StringBuilder(PrivateFunctionalTemplates.include(ownerClassName)
				+ PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName) + 
				PrivateFunctionalTemplates.include(GenerationNames.FileNames.ActionPath) + 
				GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
		
		DependencyExporter dependecyExporter = new DependencyExporter();
		dependecyExporter.addDependencies(getOwnSubmachineNames());
		dependencyIncludes.append(dependecyExporter.createDependencyCppIncludeCode(ownerClassName));
		
		CppExporterUtils.writeOutSource(destination, GenerationTemplates.sourceName(ownerClassName),
				CppExporterUtils.format(dependencyIncludes + "\n" + source));
	}

	private String createSubSmClassHeaderSource() {
		String source = "";
		StringBuilder dependency = new StringBuilder(PrivateFunctionalTemplates.include(parentClassName));
		dependency.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.StringUtilsPath));
		dependency.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.CollectionUtilsPath));
		StringBuilder privateParts = new StringBuilder(entryExitFunctionExporter.createEntryFunctionsDecl());
		privateParts.append(entryExitFunctionExporter.createExitFunctionsDecl());
		privateParts.append(GenerationTemplates
				.formatSubSmFunctions(guardExporter.declareGuardFunctions(stateMachineRegion).toString()));
		privateParts.append(transitionExporter.createTransitionFunctionDecl());
		String protectedParts = "";

		StringBuilder publicParts = new StringBuilder("");
		List<String> params = new ArrayList<String>();
		params.add(parentClassName);
		publicParts.append(ConstructorTemplates.constructorDecl(ownerClassName, params));
		publicParts.append(StateMachineTemplates.stateEnum(stateList, getInitialState(stateMachineRegion).getName()));

		if (submachineMap.isEmpty()) {
			source = HeaderTemplates.simpleSubStateMachineClassHeader(dependency.toString(), ownerClassName,
					parentClassName, publicParts.toString(), protectedParts, privateParts.toString()).toString();
		} else {
			source = HeaderTemplates
					.hierarchicalSubStateMachineClassHeader(dependency.toString(), ownerClassName, parentClassName, 
							publicParts.toString(), protectedParts, privateParts.toString())
					.toString();
		}
		return source;
	}

	private String createSubSmClassCppSource() {
		StringBuilder source = new StringBuilder("");
		source.append(createTransitionTableInitRelatedCodes());
		String initialStateName =  getInitialState(stateMachineRegion).getName();
		if (submachineMap.isEmpty()) {
			source.append(ConstructorTemplates.simpleSubStateMachineClassConstructor(ownerClassName, parentClassName,
					stateMachineMap, initialStateName));
		} else {
			source.append(ConstructorTemplates.hierarchicalSubStateMachineClassConstructor(ownerClassName,
					parentClassName, stateMachineMap, initialStateName, getEventSubMachineNameMap()));
		}
		
		StringBuilder subSmSpec = new StringBuilder(entryExitFunctionExporter.createEntryFunctionsDef());
		subSmSpec.append(entryExitFunctionExporter.createExitFunctionsDef());
		subSmSpec.append(guardExporter.defnieGuardFunctions(ownerClassName));
		subSmSpec.append(transitionExporter.createTransitionFunctionsDef());
		subSmSpec.append(StateMachineTemplates.entry(ownerClassName,
				createStateActionMap(entryExitFunctionExporter.getEntryMap())) + "\n");
		subSmSpec.append(
				StateMachineTemplates.exit(ownerClassName, createStateActionMap(entryExitFunctionExporter.getExitMap()))
						+ "\n");
		subSmSpec.append(StateMachineTemplates.finalizeFunctionDef(ownerClassName));
		subSmSpec.append(StateMachineTemplates.initializeFunctionDef(ownerClassName, getInitialTransition(stateMachineRegion).getName()));
		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));

		return source.toString();
	}
	
	private List<String> getOwnSubmachineNames() {
		List<String> ownSubMachines = new ArrayList<String>();
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			ownSubMachines.add(entry.getValue().getFirst());
		}
		return ownSubMachines;
	}

}
