package hu.elte.txtuml.export.cpp.statemachine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.CppExporterUtils.TypeDescriptor;
import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.IDependencyCollector;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.HierarchicalStateMachineNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.StateMachineMethodNames;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;

class EntryExitFunctionDescription {

	public EntryExitFunctionDescription(String stateName, String functionName, String functionBody,
			boolean containsEventParamReference) {
		this.stateName = stateName;
		this.functionName = functionName;
		this.functionBody = functionBody;
		this.containsEventParamReference = containsEventParamReference;
	}

	public String getStateName() {
		return stateName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public String getFunctionBody() {
		return functionBody;
	}

	public boolean getContainsSignalAccess() {
		return containsEventParamReference;
	}

	private String stateName;
	private String functionName;
	private String functionBody;
	private boolean containsEventParamReference;

}

public class EntryExitFunctionExporter {

	private static String unknownEntryName = "entry";
	private static String unknownExitName = "exit";

	enum FuncTypeEnum {
		Entry, Exit
	}

	private List<EntryExitFunctionDescription> entryList;
	private List<EntryExitFunctionDescription> exitList;

	private ActivityExporter activityExporter;
	private List<State> stateList;
	private ICppCompilationUnit owner;

	EntryExitFunctionExporter(ICppCompilationUnit owner, IDependencyCollector ownerDependencyCollector, List<State> stateList) {
		this.owner = owner;
		activityExporter = new ActivityExporter(Optional.of(ownerDependencyCollector));
		this.stateList = stateList;
	}

	public List<EntryExitFunctionDescription> getExitMap() {
		return exitList;
	}

	public List<EntryExitFunctionDescription> getEntryMap() {
		return entryList;
	}

	public void createEntryFunctionTypeMap() {
		createFuncTypeMap(FuncTypeEnum.Entry);
	}

	public void createExitFunctionTypeMap() {
		createFuncTypeMap(FuncTypeEnum.Exit);
	}

	public String createEntryFunctionsDecl() {
		return createFunctionDecl(FuncTypeEnum.Entry);
	}

	public String createExitFunctionsDecl() {
		return createFunctionDecl(FuncTypeEnum.Exit);
	}

	public String createEntryFunctionsDef() {
		return createFunctionDef(FuncTypeEnum.Entry);
	}

	public String createExitFunctionsDef() {
		return createFunctionDef(FuncTypeEnum.Exit);
	}

	private void createFuncTypeMap(FuncTypeEnum funcType) {
		List<EntryExitFunctionDescription> functionList = new LinkedList<EntryExitFunctionDescription>();
		for (State item : stateList) {
			String source = "";
			String name = "";
			Behavior behavior = null;
			String unknownName = null;
			switch (funcType) {
			case Entry: {
				behavior = item.getEntry();
				unknownName = unknownEntryName;
				break;
			}
			case Exit: {
				behavior = item.getExit();
				unknownName = unknownExitName;
				break;
			}
			}
			ActivityExportResult activityResult = new ActivityExportResult();
			activityResult = activityExporter.createFunctionBody(behavior);

			if (item.isComposite()) {
				String compositeRelatedCode = "";
				switch (funcType) {
				case Entry:
					compositeRelatedCode = ActivityTemplates.simpleIf(HierarchicalStateMachineNames.CurrentMachineName,
							ActivityTemplates.operationCallOnPointerVariable(HierarchicalStateMachineNames.CurrentMachineName,
									StateMachineMethodNames.InitializeFunctionName,
									Arrays.asList(EventTemplates.EventFParamName)));
					source = activityResult.getActivitySource() + compositeRelatedCode;

					break;
				case Exit:
					compositeRelatedCode = ActivityTemplates.simpleIf(HierarchicalStateMachineNames.CurrentMachineName,
							ActivityTemplates.operationCallOnPointerVariable(HierarchicalStateMachineNames.CurrentMachineName,
									StateMachineMethodNames.FinalizeFunctionName,
									Arrays.asList(EventTemplates.EventFParamName)));
					source = compositeRelatedCode + activityResult.getActivitySource();
					break;
				default:
					break;
				}

			} else {
				source = activityResult.getActivitySource();
			}
			if (source != "") {
				name = item.getName() + "_" + unknownName;
				functionList.add(new EntryExitFunctionDescription(item.getName(), name, source,
						item.isComposite() || activityResult.sourceHasSignalReference()));

			}
		}

		if (funcType == FuncTypeEnum.Entry) {
			entryList = functionList;
		} else if (funcType == FuncTypeEnum.Exit) {
			exitList = functionList;
		}

	}

	private String createFunctionDecl(FuncTypeEnum funcType) {
		StringBuilder source = new StringBuilder("");
		List<TypeDescriptor> eventParameter = new LinkedList<>();
		eventParameter.add(new TypeDescriptor(EventTemplates.EventPointerType));
		for (EntryExitFunctionDescription description : getTheProperList(funcType)) {
			source.append(FunctionTemplates.functionDecl(description.getFunctionName(), eventParameter));
		}
		return source.toString();
	}

	private String createFunctionDef(FuncTypeEnum funcType) {
		StringBuilder source = new StringBuilder("");

		List<Pair<TypeDescriptor, String>> hiddenParam = new LinkedList<>();
		List<Pair<TypeDescriptor, String>> notHiddenParam = new LinkedList<>();
		hiddenParam.add(new Pair<TypeDescriptor, String>(new TypeDescriptor(EventTemplates.EventPointerType), ""));
		notHiddenParam.add(new Pair<TypeDescriptor, String>(new TypeDescriptor(EventTemplates.EventPointerType), EventTemplates.EventParamName));
		for (EntryExitFunctionDescription description : getTheProperList(funcType)) {
			if (description.getContainsSignalAccess()) {
				source.append(FunctionTemplates.functionDef(owner.getUnitName(), description.getFunctionName(), notHiddenParam,
						description.getFunctionBody()));
			} else {
				source.append(FunctionTemplates.functionDef(owner.getUnitName(), description.getFunctionName(), hiddenParam,
						description.getFunctionBody()));

			}
		}
		return source.toString();
	}

	private List<EntryExitFunctionDescription> getTheProperList(FuncTypeEnum funcType) {
		List<EntryExitFunctionDescription> functionList;
		switch (funcType) {
		case Entry:
			functionList = entryList;
			break;
		case Exit:
			functionList = exitList;
			break;
		default:
			functionList = null;
			Logger.sys.error("The FunctionTypeEnum should be Entry or Exit");
			break;
		}

		return functionList;
	}
}
