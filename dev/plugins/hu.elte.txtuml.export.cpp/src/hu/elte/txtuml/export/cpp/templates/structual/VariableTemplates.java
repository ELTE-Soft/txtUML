package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;

public class VariableTemplates {

	public static String variableDecl(String typeName, String variableName, String defaultValue, boolean isSignal) {
		StringBuilder source = new StringBuilder("");
		source.append(isSignal ? EventTemplates.eventPtr(typeName) : 
			PrivateFunctionalTemplates.cppType(typeName));
		source.append(" ");
		source.append(variableName);
		if (defaultValue != "" && defaultValue != null) {
			source.append(ActivityTemplates.ReplaceSimpleTypeOp + defaultValue);
		}

		return ActivityTemplates.blockStatement(source.toString());
	}

	public static String variableDecl(String typeName, String variableName) {
		return variableDecl(typeName, variableName, "", false);
	}

	public static String variableDecl(String typeName, String variableName, boolean isSignal) {
		return variableDecl(typeName, variableName, "", isSignal);
	}

	public static String propertyDecl(String typeName, String variableName, String defaultValue, List<String> templateParameters) {
		return variableDecl(typeName + 
				CppExporterUtils.createTemplateParametersCode(templateParameters), 
				variableName, defaultValue, false);
	}
	
	public static String propertyDecl(String typeName, String variableName, String defaultValue) {
		return variableDecl(typeName, variableName, defaultValue, false);
	}

}
