package hu.elte.txtuml.export.cpp.statemachine;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates.HeaderInfo;
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
		
		StringBuilder publicParts = new StringBuilder("");	
		StringBuilder protectedParts = new StringBuilder("");
		StringBuilder privateParts = new StringBuilder("");

		publicParts.append(ConstructorTemplates.constructorDecl(ownerClassName, Arrays.asList(parentClassName)));		
		
		/*if(stateList.isEmpty()) {
			source = HeaderTemplates
					.classHeader(dependency.toString(), null,
							publicParts.toString(), protectedParts.toString(), privateParts.toString(), 
							new HeaderInfo(ownerClassName,new 
									HeaderTemplates.SubMachineHeaderType(parentClassName), 
									Optional.empty()));
			
		} else {*/
			publicParts.append(StateMachineTemplates.stateEnum(stateList, getInitialStateName()));
			
			privateParts.append(entryExitFunctionExporter.createEntryFunctionsDecl());
			privateParts.append(entryExitFunctionExporter.createExitFunctionsDecl());
			privateParts.append(GenerationTemplates
					.formatSubSmFunctions(guardExporter.declareGuardFunctions(stateMachineRegion)));
			privateParts.append(transitionExporter.createTransitionFunctionDecl());
			
			if (submachineMap.isEmpty()) {
				source = HeaderTemplates
						.classHeader(dependency.toString(), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), 
								new HeaderInfo(ownerClassName, 
										new HeaderTemplates.SubMachineHeaderType(parentClassName), 
										Optional.of(new HeaderInfo.StateMachineInfo(false))));
				
			} else {
				source = HeaderTemplates
						.classHeader(dependency.toString(), null,
								publicParts.toString(), protectedParts.toString(), privateParts.toString(), 
								new HeaderInfo(ownerClassName,
										new HeaderTemplates.SubMachineHeaderType(parentClassName),
										Optional.of(new HeaderInfo.StateMachineInfo(true))));
		}

		//}
		return source;
	}

	private String createSubSmClassCppSource() {
		StringBuilder source = new StringBuilder("");
		source.append(createTransitionTableInitRelatedCodes());
		source.append(ConstructorTemplates.subStateMachineClassConstructor(ownerClassName, parentClassName, stateMachineMap, 
				 submachineMap.isEmpty() ? Optional.empty() : Optional.of(getEventSubMachineNameMap())));
		//if(!stateList.isEmpty()) {
			source.append(StateMachineTemplates.stateMachineFixFunctionDefitions(ownerClassName, getInitialStateName(), true, submachineMap.isEmpty()));
			
			StringBuilder subSmSpec = new StringBuilder(entryExitFunctionExporter.createEntryFunctionsDef());
			subSmSpec.append(entryExitFunctionExporter.createExitFunctionsDef());
			subSmSpec.append(guardExporter.defnieGuardFunctions(ownerClassName));
			subSmSpec.append(transitionExporter.createTransitionFunctionsDef());
			subSmSpec.append(StateMachineTemplates.entry(ownerClassName,
					createStateActionMap(entryExitFunctionExporter.getEntryMap())) + "\n");
			subSmSpec.append(
					StateMachineTemplates.exit(ownerClassName, createStateActionMap(entryExitFunctionExporter.getExitMap()))
							+ "\n");
			source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));

		//}



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
