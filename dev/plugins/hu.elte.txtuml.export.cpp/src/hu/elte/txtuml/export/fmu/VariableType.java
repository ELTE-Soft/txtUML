package hu.elte.txtuml.export.fmu;

public enum VariableType {

	BOOLEAN, INTEGER, STRING, REAL;

	public static VariableType fromJavaType(String typeSignature) {
		switch (typeSignature) {
		case "D":
			return REAL;
		case "I":
			return INTEGER;
		case "B":
			return BOOLEAN;
		case "String":
			return STRING;
		default:
			throw new RuntimeException("Cannot recognize type code: " + typeSignature);
		}
	}

}
