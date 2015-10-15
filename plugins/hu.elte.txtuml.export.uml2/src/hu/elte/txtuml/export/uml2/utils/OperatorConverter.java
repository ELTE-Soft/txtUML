package hu.elte.txtuml.export.uml2.utils;

public class OperatorConverter {

	private OperatorConverter() {
	}

	public static String convert(String operator) {
		if (operator.equals("&&")) {
			return "and";
		} else if (operator.equals("||")) {
			return "or";
		} else if (operator.equals("==")) {
			return "=";
		} else {
			return operator;
		}
	}
}
