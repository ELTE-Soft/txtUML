package hu.elte.txtuml.export.fmu;

import java.util.List;

public class VariableDefinition {

	public VariableDefinition(String elementName, VariableType type) {
		name = elementName;
		this.type = type;
	}
	
	String name;
	VariableType type;
	
	static public String generateDeclareBuffers(List<VariableDefinition> vars) {
		StringBuilder sb = new StringBuilder();
		for (VariableType varType : VariableType.values()) {
			if (vars.stream().anyMatch(var -> var.type == varType)) {
				String typeName = varType.getName();
				sb.append("fmi2").append(typeName).append(" temp").append(typeName).append("[1];\n");
			}
		}

		return sb.toString();
	}
	
}
