package hu.elte.txtuml.export.cpp.statemachine;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ConstructorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.utils.Pair;

public class SubStateMachineExporter extends StateMachineExporterBase {

	private String parentClassName;
	private Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>

	private SubStateMachineExporter subStateMachineExporter;

	public SubStateMachineExporter() {
		super();
	}

	public void setRegion(Region region) {
		this.stateMachineRegion = region;
		createStateList();
	}

	public void setParentClass(String name) {
		this.parentClassName = name;
	}

	public void createSubSmSource(String destination) throws FileNotFoundException, UnsupportedEncodingException {
		super.createMachine();
		String source = "";
		submachineMap = getSubMachines();

		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			subStateMachineExporter = new SubStateMachineExporter();
			subStateMachineExporter.setRegion(entry.getValue().getSecond());
			subStateMachineExporter.setName(entry.getValue().getFirst());
			subStateMachineExporter.setParentClass(parentClassName);
			subStateMachineExporter.createSubSmSource(destination);
		}

		source = createSubSmClassHeaderSource();
		CppExporterUtils.writeOutSource(destination, GenerationTemplates.headerName(ownerClassName),
				HeaderTemplates.headerGuard(source, ownerClassName));

		source = GenerationTemplates.putNamespace(createSubSmClassCppSource().toString(), GenerationNames.Namespaces.ModelNamespace);
		String dependencyIncludes = PrivateFunctionalTemplates.include(ownerClassName)
				+ PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName) + PrivateFunctionalTemplates.include(GenerationNames.FileNames.ActionPath);
		dependencyIncludes = GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude)
				+ dependencyIncludes + PrivateFunctionalTemplates.include(ownerClassName);
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
		publicParts.append(StateMachineTemplates.stateEnum(stateList, getInitialStateName()));

		if (submachineMap.isEmpty()) {
			source = HeaderTemplates.simpleSubStateMachineClassHeader(dependency.toString(), ownerClassName,
					parentClassName, publicParts.toString(), protectedParts, privateParts.toString()).toString();
		} else {
			source = HeaderTemplates
					.hierarchicalSubStateMachineClassHeader(dependency.toString(), ownerClassName, parentClassName,
							getSubMachineNameList(), publicParts.toString(), protectedParts, privateParts.toString())
					.toString();
		}
		return source;
	}

	private String createSubSmClassCppSource() {
		StringBuilder source = new StringBuilder("");
		source.append(createTransitionTableInitRelatedCodes());
		if (submachineMap.isEmpty()) {
			source.append(ConstructorTemplates.simpleSubStateMachineClassConstructor(ownerClassName, parentClassName,
					stateMachineMap, getInitialStateName()));
		} else {
			source.append(ConstructorTemplates.hierarchicalSubStateMachineClassConstructor(ownerClassName,
					parentClassName, stateMachineMap, getEventSubMachineNameMap(), null));
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

		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));

		return source.toString();
	}

}
