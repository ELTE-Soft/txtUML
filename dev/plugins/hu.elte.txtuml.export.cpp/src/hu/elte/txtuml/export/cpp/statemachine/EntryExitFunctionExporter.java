package hu.elte.txtuml.export.cpp.statemachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.utils.Pair;

public class EntryExitFunctionExporter {

	private static String unknownEntryName = "entry";
	private static String unknownExitName = "exit";

	enum FuncTypeEnum {
		Entry, Exit
	}

	private Map<String, Pair<String, String>> entryMap;// <name,<state,funcBody>>
	private Map<String, Pair<String, String>> exitMap;// <name,<state,funcBody>>

	private ActivityExporter activityExporter;
	private List<State> stateList;
	private String className;

	EntryExitFunctionExporter(String className, List<State> stateList) {
		activityExporter = new ActivityExporter();
		this.className = className;
		this.stateList = stateList;
	}

	public Map<String, Pair<String, String>> getExitMap() {
		return exitMap;
	}

	public Map<String, Pair<String, String>> getEntryMap() {
		return entryMap;
	}

	public void createEntryFunctionTypeMap() {
		createFuncTypeMap(FuncTypeEnum.Entry);
	}

	public void createExitFunctionTypeMap() {
		createFuncTypeMap(FuncTypeEnum.Exit);
	}

	public String createEntryFunctionsDecl() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : entryMap.entrySet()) {
			source.append(FunctionTemplates.functionDecl(entry.getKey()));
		}
		return source.toString();
	}

	public String createExitFunctionsDecl() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : exitMap.entrySet()) {
			source.append(FunctionTemplates.functionDecl(entry.getKey()));
		}
		return source.toString();
	}

	public String createEntryFunctionsDef() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : entryMap.entrySet()) {
			source.append(FunctionTemplates.functionDef(className, entry.getKey(), entry.getValue().getSecond()));
		}
		return source.toString();
	}

	public String createExitFunctionsDef() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : exitMap.entrySet()) {
			source.append(FunctionTemplates.functionDef(className, entry.getKey(), entry.getValue().getSecond()));
		}
		return source.toString();
	}

	private void createFuncTypeMap(FuncTypeEnum funcType) {
		Map<String, Pair<String, String>> map = new HashMap<String, Pair<String, String>>();
		String source = "";
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

			if (behavior != null) {
				if (behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
					source = activityExporter.createfunctionBody((Activity) behavior).toString();
					name = item.getName() + "_" + unknownName;
					map.put(name, new Pair<String, String>(item.getName(), source.toString()));
				}
			}
		}

		if (funcType == FuncTypeEnum.Entry) {
			setEntryMap(map);
		} else if (funcType == FuncTypeEnum.Exit) {
			setExitMap(map);
		}

	}

	private void setExitMap(Map<String, Pair<String, String>> exitMap) {
		this.exitMap = exitMap;
	}

	private void setEntryMap(Map<String, Pair<String, String>> entryMap) {
		this.entryMap = entryMap;
	}
}
