package hu.elte.txtuml.export.cpp.structural;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
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
	
	EntryExitFunctionExporter(String className,List<State> stateList) {
		activityExporter = new ActivityExporter();
		this.className = className;
		this.stateList = stateList;
		
		createFuncTypeMap(FuncTypeEnum.Entry);
		createFuncTypeMap(FuncTypeEnum.Exit);
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
	
	public StringBuilder createEntryFunctionsDecl() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : entryMap.entrySet()) {
			source.append(GenerationTemplates.functionDecl(entry.getKey()));
		}
		return source;
	}

	public StringBuilder createExitFunctionsDecl() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : exitMap.entrySet()) {
			source.append(GenerationTemplates.functionDecl(entry.getKey()));
		}
		return source;
	}
	
	public StringBuilder createEntryFunctionsDef() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : entryMap.entrySet()) {
			source.append(GenerationTemplates.functionDef(className, entry.getKey(), entry.getValue().getSecond()));
		}
		return source;
	}

	public StringBuilder createExitFunctionsDef() {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : exitMap.entrySet()) {
			source.append(GenerationTemplates.functionDef(className, entry.getKey(), entry.getValue().getSecond()));
		}
		return source;
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
					activityExporter.init();
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
