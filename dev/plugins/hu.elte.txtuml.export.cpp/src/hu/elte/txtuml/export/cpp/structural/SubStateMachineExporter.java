package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;

import com.google.common.collect.Multimap;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.utils.Pair;

class SubStateMachineExporter extends StateMachineExporter{

	private String subMachineName;
	private String parentClassName;
	private String sourceDestination;

	private Map<String, Pair<String, Region>> submachineMap;// <stateName,<machinename,behavior>>

	private EntryExitFunctionExporter entryExitExporter;
	private SubStateMachineExporter subStateMachineExporter;
	private GuardExporter guardExporter;
	private TransitionExporter transitionExporter;

	SubStateMachineExporter(Region region,String parentClassName, String sourceDestination) {
		super(region);
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
	
	void setSubMachineName(String name) {
		this.subMachineName = name;
	}

	void createSubSmSource() throws FileNotFoundException, UnsupportedEncodingException {
		String source = "";
		submachineMap = getSubMachines(stateMachineRegion);
		entryExitExporter.createEntryFunctionTypeMap();
		entryExitExporter.createExitFunctionTypeMap();

		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			subStateMachineExporter = new SubStateMachineExporter(entry.getValue().getSecond(),parentClassName,sourceDestination);
			subStateMachineExporter.createSubSmSource();
		}

		source = createSubSmClassHeaderSource();
		Shared.writeOutSource(sourceDestination, GenerationTemplates.headerName(subMachineName),
				GenerationTemplates.headerGuard(source, subMachineName));
		source = createSubSmClassCppSource().toString();

		String dependencyIncludes = GenerationTemplates.cppInclude(subMachineName);
		dependencyIncludes = GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude)
				+ dependencyIncludes + GenerationTemplates.cppInclude(subMachineName);

		Shared.writeOutSource(sourceDestination, GenerationTemplates.sourceName(subMachineName),
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
		publicParts.append(GenerationTemplates.constructorDecl(subMachineName, params));
		publicParts.append(
				GenerationTemplates.stateEnum(stateList, getInitialStateName()));

		if (submachineMap.isEmpty()) {
			source = GenerationTemplates.simpleSubStateMachineClassHeader(dependency.toString(), subMachineName,
					parentClassName, publicParts.toString(), protectedParts, privateParts.toString()).toString();
		} else {
			source = GenerationTemplates
					.hierarchicalSubStateMachineClassHeader(dependency.toString(), subMachineName, parentClassName,
							getSubmachines(), publicParts.toString(), protectedParts, privateParts.toString())
					.toString();
		}
		return source;
	}

	private StringBuilder createSubSmClassCppSource() {
		StringBuilder source = new StringBuilder("");
		if (submachineMap.isEmpty()) {
			source.append(GenerationTemplates.simpleSubStateMachineClassConstructor(subMachineName, parentClassName,
					stateMachineMap,getInitialStateName()));
		} else {
			source.append(GenerationTemplates.hierarchicalSubStateMachineClassConstructor(subMachineName,
					parentClassName, stateMachineMap, getEventSubmachineNameMap(), getInitialStateName()));
		}
		source.append(GenerationTemplates.destructorDef(subMachineName, false));
		StringBuilder subSmSpec = entryExitExporter.createEntryFunctionsDef(subMachineName);
		subSmSpec.append(entryExitExporter.createExitFunctionsDef(subMachineName));
		subSmSpec.append(guardExporter.defnieGuardFunctions(subMachineName));
		subSmSpec.append(transitionExporter.createTransitionFunctionsDef());
		subSmSpec.append(GenerationTemplates.entry(subMachineName, createStateActionMap(entryExitExporter.getEntryMap())) + "\n");
		subSmSpec.append(GenerationTemplates.exit(subMachineName, createStateActionMap(entryExitExporter.getExitMap())) + "\n");

		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));
		return source;
	}

	private Map<String, Pair<String, Region>> getSubMachines(Region region_) {
		Map<String, Pair<String, Region>> submachineMap = new HashMap<String, Pair<String, Region>>();
		for (State state : stateList) {
			// either got a submachine or a region, both is not permitted
			StateMachine m = state.getSubmachine();
			if (m != null) {
				submachineMap.put(state.getName(), new Pair<String, Region>(m.getName(), m.getRegions().get(0)));
			} else {
				List<Region> r = state.getRegions();
				if (!r.isEmpty()) {
					submachineMap.put(state.getName(), new Pair<String, Region>(state.getName() + "_subSM", r.get(0)));
				}
			}
		}
		return submachineMap;
	}
}
