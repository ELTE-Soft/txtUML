package hu.elte.txtuml.export.cpp.templates.structual;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;

public class VariableTemplates {

	public static String variableDecl(String typeName, String variableName, String defaultValue, boolean isSignal) {
		StringBuilder source = new StringBuilder("");
		source.append(isSignal ? GenerationNames.signalPointerType(typeName) : PrivateFunctionalTemplates.cppType(typeName));
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

	public static String propertyDecl(String typeName, String variableName, String defaultValue) {
		return variableDecl(typeName, variableName, defaultValue, false);
	}

}