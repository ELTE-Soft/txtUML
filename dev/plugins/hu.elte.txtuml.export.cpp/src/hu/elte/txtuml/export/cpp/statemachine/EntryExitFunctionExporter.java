package hu.elte.txtuml.export.cpp.statemachine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
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
	private String className;

	EntryExitFunctionExporter(String className, List<State> stateList) {
		activityExporter = new ActivityExporter();
		this.className = className;
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
		String source = "";
		String bevaiorCode = "";
		String compositeRelatedCode = "";
		String name = "";
		for (State item : stateList) {
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
			if (behavior != null && behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
				bevaiorCode = activityExporter.createFunctionBody((Activity) behavior).toString();
			}
			
			if(item.isComposite()) {
				switch (funcType) {
				case Entry:
					compositeRelatedCode = ActivityTemplates.simpleIf(GenerationNames.CurrentMachineName, 
							ActivityTemplates.operationCallOnPointerVariable(GenerationNames.CurrentMachineName,
									StateMachineMethodNames.InitializeFunctionName,
									Arrays.asList(EventTemplates.EventFParamName)));
					source = bevaiorCode + compositeRelatedCode;
					
					break;
				case Exit:
					compositeRelatedCode = ActivityTemplates.simpleIf(GenerationNames.CurrentMachineName,
							ActivityTemplates.operationCallOnPointerVariable(GenerationNames.CurrentMachineName,
									StateMachineMethodNames.FinalizeFunctionName,
									Arrays.asList(EventTemplates.EventFParamName)));
					source = compositeRelatedCode + bevaiorCode;
					break;
				default:
					break;
				}
				
			} else {
				source = bevaiorCode;
			}
			if (source != "") {
					name = item.getName() + "_" + unknownName;
					functionList.add(new EntryExitFunctionDescription(item.getName(), name, source,
							item.isComposite() || (behavior != null && activityExporter.isContainsSignalAccess()))); //TODO Should be static function
				
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
		List<String> eventParameter = new LinkedList<String>();
		eventParameter.add(EventTemplates.EventPointerType);
		for (EntryExitFunctionDescription description : getTheProperList(funcType)) {
			source.append(FunctionTemplates.functionDecl(description.getFunctionName(), eventParameter));
		}
		return source.toString();
	}

	private String createFunctionDef(FuncTypeEnum funcType) {
		StringBuilder source = new StringBuilder("");

		List<Pair<String, String>> hiddenParam = new LinkedList<Pair<String, String>>();
		List<Pair<String, String>> notHiddenParam = new LinkedList<Pair<String, String>>();
		hiddenParam.add(new Pair<String, String>(EventTemplates.EventPointerType, ""));
		notHiddenParam.add(new Pair<String, String>(EventTemplates.EventPointerType, EventTemplates.EventParamName));
		for (EntryExitFunctionDescription description : getTheProperList(funcType)) {
			if (description.getContainsSignalAccess()) {
				source.append(FunctionTemplates.functionDef(className, description.getFunctionName(), notHiddenParam,
						description.getFunctionBody()));
			} else {
				source.append(FunctionTemplates.functionDef(className, description.getFunctionName(), hiddenParam,
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
