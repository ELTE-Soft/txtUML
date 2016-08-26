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
	private String sourceDestination;
	private Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>

	private EntryExitFunctionExporter entryExitExporter;
	private SubStateMachineExporter subStateMachineExporter;

	SubStateMachineExporter(Region region,String parentClassName, String subMachineName,String sourceDestination) {
		super(region,subMachineName);
		init(parentClassName, sourceDestination);
	}

	void init(String parentClassName, String sourceDestination) {
		this.parentClassName = parentClassName;
		this.sourceDestination = sourceDestination;

		//entryExitExporter = new EntryExitFunctionExporter(new ActivityExporter());
	}
	
	void setRegion(Region region) {
		this.stateMachineRegion = region;
	}

	void createSubSmSource() throws FileNotFoundException, UnsupportedEncodingException {
		String source = "";
		submachineMap = getSubMachines();
		entryExitExporter.createEntryFunctionTypeMap();
		entryExitExporter.createExitFunctionTypeMap();

		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			subStateMachineExporter = new SubStateMachineExporter(entry.getValue().getSecond(),parentClassName,entry.getValue().getFirst(),sourceDestination);
			subStateMachineExporter.createSubSmSource();
		}

		source = createSubSmClassHeaderSource();
		Shared.writeOutSource(sourceDestination, GenerationTemplates.headerName(className),
				GenerationTemplates.headerGuard(source, className));
		source = createSubSmClassCppSource().toString();

		String dependencyIncludes = GenerationTemplates.cppInclude(className);
		dependencyIncludes = GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude)
				+ dependencyIncludes + GenerationTemplates.cppInclude(className);

		Shared.writeOutSource(sourceDestination, GenerationTemplates.sourceName(className),
				dependencyIncludes + "\n" + source);
	}

	private String createSubSmClassHeaderSource() {
		String source = "";
		StringBuilder dependency = new StringBuilder(GenerationTemplates.cppInclude(parentClassName));
		dependency.append(GenerationTemplates.cppInclude(GenerationTemplates.StandardFunctionsHeader));

		StringBuilder privateParts = entryExitExporter.createEntryFunctionsDecl();
		privateParts.append(entryExitExporter.createExitFunctionsDecl());
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
		source.append(GenerationTemplates.destructorDef(className, false));
		StringBuilder subSmSpec = entryExitExporter.createEntryFunctionsDef();
		subSmSpec.append(entryExitExporter.createExitFunctionsDef());
		subSmSpec.append(guardExporter.defnieGuardFunctions(className));
		subSmSpec.append(transitionExporter.createTransitionFunctionsDef());
		subSmSpec.append(GenerationTemplates.entry(className, createStateActionMap(entryExitExporter.getEntryMap())) + "\n");
		subSmSpec.append(GenerationTemplates.exit(className, createStateActionMap(entryExitExporter.getExitMap())) + "\n");

		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));
		return source;
	}


}
