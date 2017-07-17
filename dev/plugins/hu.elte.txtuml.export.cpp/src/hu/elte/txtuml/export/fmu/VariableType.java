package hu.elte.txtuml.export.fmu;

public enum VariableType {
	
	BOOLEAN("Boolean"), INTEGER("Integer"), STRING("String"), REAL("Real");
	
	private String name;

	private VariableType(String name) {
		this.name = name;
		
	};
	
	public String getName() {
		return name;
	}

	public static VariableType fromJavaType(String typeSignature) {
		switch (typeSignature) {
		case "D":
			return REAL;
		case "I":
			return INTEGER;
		case "Z":
			return BOOLEAN;
		case "String":
			return STRING;
		default:
			throw new RuntimeException("Cannot recognize type code: " + typeSignature);
		}
	}

}
