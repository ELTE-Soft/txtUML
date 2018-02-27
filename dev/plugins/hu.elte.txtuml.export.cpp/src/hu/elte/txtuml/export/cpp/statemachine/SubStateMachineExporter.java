package hu.elte.txtuml.export.cpp.statemachine;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.IDependencyCollector;
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

public class SubStateMachineExporter extends StateMachineExporterBase implements ICppCompilationUnit, IDependencyCollector {

	private String subStateMachineName;
	private DependencyExporter dependecyExporter;
	private String subMachineCodeDest;
	
	public SubStateMachineExporter(String subStateMachineName, Region region, ICppCompilationUnit owner, String subMachineCodeDest) {
		super(region,owner);
		this.subStateMachineName = subStateMachineName;
		this.stateMachineRegion = region;
		this.subMachineCodeDest = subMachineCodeDest;
		
		dependecyExporter = new DependencyExporter();
		
		init(this);

	}
	
	private List<String> getOwnSubmachineNames() {
		List<String> ownSubMachines = new ArrayList<String>();
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			ownSubMachines.add(entry.getValue().getFirst());
		}
		return ownSubMachines;
	}

	@Override
	public String getUnitName() {
		return subStateMachineName;
	}

	@Override
	public void addDependency(String type) {
		dependecyExporter.addDependency(type);
		
	}
	
	@Override
	public void addCppOnlyDependency(String type) {
		dependecyExporter.addCppOnlyDependency(type);
		
	}


	@Override
	protected ICppCompilationUnit getActualCompilationUnit() {
		return this;
	}

	@Override
	public String getUnitNamespace() {
		 return GenerationNames.Namespaces.ModelNamespace;
	}
	
	@Override
	public void createAddtionoalSources() throws FileNotFoundException, UnsupportedEncodingException {
		createSubMachineSources(getDesniation());
	}

	@Override
	public String createUnitCppCode() {
		StringBuilder source = new StringBuilder("");
		source.append(createTransitionTableInitRelatedCodes());
		source.append(ConstructorTemplates.subStateMachineClassConstructor(getUnitName(), ownerClassUnit.getUnitName(), stateMachineMap, 
								 submachineMap.isEmpty() ? Optional.empty() : Optional.of(getStateToSubMachineNameMap())));
		source.append(StateMachineTemplates.stateMachineFixFunctionDefitions(getUnitName(), getInitialState(), true, submachineMap.isEmpty()));

		
		StringBuilder subSmSpec = new StringBuilder(entryExitFunctionExporter.createEntryFunctionsDef());
		subSmSpec.append(entryExitFunctionExporter.createExitFunctionsDef());
		subSmSpec.append(guardExporter.defnieGuardFunctions(getUnitName()));
		subSmSpec.append(transitionExporter.createTransitionFunctionsDef());
		subSmSpec.append(StateMachineTemplates.entry(getUnitName(),
				createStateActionMap(entryExitFunctionExporter.getEntryMap())) + "\n");
		subSmSpec.append(
				StateMachineTemplates.exit(getUnitName(), createStateActionMap(entryExitFunctionExporter.getExitMap()))
						+ "\n");
		subSmSpec.append(StateMachineTemplates.finalizeFunctionDef(getUnitName()));
		subSmSpec.append(StateMachineTemplates.initializeFunctionDef(getUnitName(), getInitialTransition()));
		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));

		return source.toString();
	}

	@Override
	public String createUnitHeaderCode() {
		String source = "";
		
		StringBuilder publicParts = new StringBuilder("");	
		StringBuilder protectedParts = new StringBuilder("");
		StringBuilder privateParts = new StringBuilder("");

		publicParts.append(ConstructorTemplates.constructorDecl(getUnitName(), Arrays.asList(ownerClassUnit.getUnitName())));					
		publicParts.append(StateMachineTemplates.stateEnum(stateList, getInitialState()));
			
		privateParts.append(entryExitFunctionExporter.createEntryFunctionsDecl());
		privateParts.append(entryExitFunctionExporter.createExitFunctionsDecl());
		privateParts.append(GenerationTemplates
					.formatSubSmFunctions(guardExporter.declareGuardFunctions(stateMachineRegion)));
		privateParts.append(transitionExporter.createTransitionFunctionDecl());
			
		source = HeaderTemplates
					.classHeader(null, null,
							publicParts.toString(), protectedParts.toString(), privateParts.toString(), 
							new HeaderInfo(getUnitName(), 
									new HeaderTemplates.SubMachineHeaderType(ownerClassUnit.getUnitName(), !submachineMap.isEmpty())));
				
			
		return source;
	}

	@Override
	public String getUnitDependencies(UnitType type) {
		
		StringBuilder dependencyIncludes = new StringBuilder("");
		dependecyExporter.addDependencies(getOwnSubmachineNames());
		
		if(type == UnitType.Header) {
			dependencyIncludes.append(PrivateFunctionalTemplates.include(ownerClassUnit.getUnitName()));
			dependencyIncludes.append(PrivateFunctionalTemplates.include(EventTemplates.EventHeaderName));

			dependencyIncludes.append(dependecyExporter.createDependencyHeaderIncludeCode(GenerationNames.Namespaces.ModelNamespace));


		} else {
			dependencyIncludes.append(dependecyExporter.createDependencyCppIncludeCode(getUnitName()));
			dependencyIncludes.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.ActionPath));
			dependencyIncludes.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
			dependencyIncludes.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.StringUtilsPath));
			dependencyIncludes.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.CollectionUtilsPath));
		}
				
		
		return dependencyIncludes.toString();
	}

	@Override
	public String getDesniation() {
		return subMachineCodeDest;
	}





}
