package hu.elte.txtuml.export.fmu;

public class VariableDefinition {

	public VariableDefinition(String elementName, VariableType type) {
		name = elementName;
		this.type = type;
	}
	
	String name;
	VariableType type;
	
}
