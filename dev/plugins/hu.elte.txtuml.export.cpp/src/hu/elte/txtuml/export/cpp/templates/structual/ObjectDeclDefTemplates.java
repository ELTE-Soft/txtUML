package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;

public class ObjectDeclDefTemplates {
	public enum VariableType {
		Default,
		EventPtr,
		SharedPtr
	}
	public static String variableDecl(String typeName, String variableName, String defaultValue, VariableType varType) {
		StringBuilder source = new StringBuilder("");
		String type = "";
		switch(varType) {
		case Default:
			type = PrivateFunctionalTemplates.cppType(typeName);
			break;
		case EventPtr:
			type = EventTemplates.eventPtr(typeName);
			break;
		case SharedPtr:
			type = GenerationNames.sharedPtrType(typeName);
			break;
		default:
			assert(false);
			break;
		
		}
		source.append(type);
		source.append(" ");
		source.append(variableName);
		if (defaultValue != "" && defaultValue != null) {
			source.append(ActivityTemplates.ReplaceSimpleTypeOp + defaultValue);
		}

		return ActivityTemplates.blockStatement(source.toString());
	}

	public static String variableDecl(String typeName, String variableName) {
		return variableDecl(typeName, variableName, "", VariableType.Default);
	}

	public static String variableDecl(String typeName, String variableName, VariableType varType) {
		return variableDecl(typeName, variableName, "", varType);
	}

	public static String propertyDecl(String typeName, String variableName, String defaultValue, List<String> templateParameters, VariableType varType) {
		return variableDecl(typeName + 
				CppExporterUtils.createTemplateParametersCode(templateParameters), 
				variableName, defaultValue, varType);
	}
	
	public static String propertyDecl(String typeName, String variableName, String defaultValue) {
		return variableDecl(typeName, variableName, defaultValue, VariableType.Default);
	}

	public static String createObject(String typeName, String objName, boolean sharedObject) {
		return ObjectDeclDefTemplates.createObject(typeName, objName, null, null, sharedObject);
	}

	public static String createObject(String typeName, String objName, List<String> params, boolean sharedObject) {
		return ObjectDeclDefTemplates.createObject(typeName, objName, null, params, sharedObject);
	}

	public static String createObject(String typeName, String objName, List<String> templateParams,
			List<String> params, boolean sharedObject) {
		String templateList = CppExporterUtils.createTemplateParametersCode(templateParams);
		if(!sharedObject) {
			return GenerationNames.sharedPtrType(typeName + templateList) + " " + 
					setAllocatedObjectToObjectVariable(typeName,templateParams, objName, params, false);
		} else {
			return GenerationNames.sharedPtrType(typeName + templateList) + " " + 
							setAllocatedObjectToObjectVariable(typeName,templateParams, objName, params, true);
		}
	
	
	}
	
	public static String setAllocatedObjectToObjectVariable(String typeName, 
			List<String> templateParams, String objName, List<String> params, Boolean sharedObject) {
		return objName  + " = " + allocateObject(typeName, templateParams, params, sharedObject) + ";\n";
	}

	public static String allocateObject(String typeName, List<String> templateParams, List<String> params, boolean sharedObject) {
	
		String parameters = "(";
		if (params != null && params.size() > 0) {
	
			for (int i = 0; i < params.size() - 1; i++) {
				parameters = parameters + params.get(i) + ",";
			}
			parameters = parameters + params.get(params.size() - 1);
		}
		parameters = parameters + ")";
	
		String templateParameters = "";
		if (templateParams != null) {
			templateParameters = "<";
			for (int i = 0; i < templateParams.size() - 1; i++) {
				templateParameters = templateParameters + templateParams.get(i) + ",";
			}
			templateParameters = templateParameters + templateParams.get(templateParams.size() - 1) + ">";
		}
		
		String allocatedObject = PointerAndMemoryNames.MemoryAllocator + " " + typeName + templateParameters + parameters;
		if(!sharedObject) {
			return allocatedObject;
		} else {
			return GenerationNames.sharedPtrType(typeName + templateParameters) + "(" + allocatedObject + ")";
		}
	
	}

	public static String allocateObject(String typeName, List<String> params, boolean sharedObject) {
		return allocateObject(typeName, null, params, sharedObject);
	}

	public static String allocateObject(String typeName) {
		return allocateObject(typeName, null, null, false);
	}

}
