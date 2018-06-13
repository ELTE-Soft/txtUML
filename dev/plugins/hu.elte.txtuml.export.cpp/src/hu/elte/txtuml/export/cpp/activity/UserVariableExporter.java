package hu.elte.txtuml.export.cpp.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;

class VariableInfo {
	VariableInfo(String name, Boolean usage) {
		this.name = name;
		this.usage = usage;
	}

	public String getName() {
		return name;
	}

	public Boolean isUsed() {
		return usage;
	}

	public void setUsage(Boolean value) {
		usage = value;
	}

	String name;
	Boolean usage;
}

class UserVariableExporter {

	private Map<Variable, VariableInfo> variableTable;
	private int genTempVariableCounter;
	private int userVarCounter;

	public UserVariableExporter() {
		variableTable = new HashMap<Variable, VariableInfo>();
		genTempVariableCounter = 0;
		userVarCounter = 0;

	}

	public void exportNewVariable(Variable var) {
		if (!variableTable.containsKey(var)) {
			if (ActivityTemplates.invalidIdentifier(var.getName())) {
				variableTable.put(var,
						new VariableInfo(ActivityTemplates.generatedTempVariable(genTempVariableCounter), false));
				genTempVariableCounter++;
			} else {
				variableTable.put(var,
						new VariableInfo(ActivityTemplates.formatUserVar(var.getName(), userVarCounter), false));
				userVarCounter++;
			}
		}
	}

	public void modifyVariableInfo(Variable var) {
		if (variableTable.get(var) != null) {
			variableTable.get(var).setUsage(true);
		}
	}

	public Collection<VariableInfo> getElements() {
		return variableTable.values();
	}

	public String getRealVariableReference(Variable var) {
		if (variableTable.get(var) != null) {
			return variableTable.get(var).getName();
		} else {
			return var.getName();
		}

	}
}
