package hu.elte.txtuml.export.cpp.templates.activity;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ActionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TimerNames;

public class OperatorTemplates {


	public static final String Add = "+";
	public static final String Sub = "-";
	public static final String Mul = "*";
	public static final String Div = "/";
	public static final String Mod = "%";
	public static final String Increment = "++";
	public static final String Decrement = "--";
	public static final String Neg = "-";
	public static final String Equal = "==";
	public static final String NotEqual = "!=";
	public static final String LessThen = "<";
	public static final String GreatThen = ">";
	public static final String LessOrEqThen = "<=";
	public static final String GreatOrEqThen = ">=";

	public static final String Not = "!";
	public static final String And = "&&";
	public static final String Or = "||";

	public static final String Select = "select";
	public static final String Concat = "concat";
	public static final String ToString = "toString";
	public static final String Count = "count";
	public static final String Round = "round";
	public static final String Sinus = "sin";
	public static final String Cosinus = "cos";
	public static final String TimerStart = TimerNames.StartTimerFunctionName;

	public static final String NotBinaryOperator = "not_binary_operator";

	public static String Fork(String cond, String e1, String e2) {
		return cond + " ? " + e1 + " : " + e2;
	}

	public static boolean isInfixBinaryOperator(String functionName) {
		return resolveBinaryOperation(functionName) != NotBinaryOperator;
	}

	public static String resolveBinaryOperation(String operationNem) {
		String name = NotBinaryOperator;
		switch (operationNem) {
		case "add":
			name = Add;
			break;
		case "concat":
			name = Add;
			break;
		case "sub":
			name = Sub;
			break;
		case "mul":
			name = Mul;
			break;
		case "div":
			name = Div;
			break;
		case "mod":
			name = Mod;
			break;
		case "eq":
			name = Equal;
			break;
		case "neq":
			name = NotEqual;
			break;
		case "lt":
			name = LessThen;
			break;
		case "gt":
			name = GreatThen;
			break;
		case "leq":
			name = LessOrEqThen;
			break;
		case "geq":
			name = GreatOrEqThen;
			break;
		case "and":
			name = And;
			break;
		case "or":
			name = Or;
			break;
		}

		return name;
	}

	public static String getStandardLibraryFunctionName(String function) {
		switch (function) {
		case "toString":
			return GenerationNames.Namespaces.StringUtilsNamespace + "::" + ToString;
		case "count":
			return GenerationNames.Namespaces.CollectionUtilsNamespace + "::" + "count"; 
		case "select":
			return GenerationNames.Namespaces.CollectionUtilsNamespace + "::" + "select"; 
		case "log":
			return ActionNames.Log;
		case "start":
			return TimerNames.TimerInterFaceName + "::" + TimerStart;
		case "inc":
			return Increment;
		case "dec":
			return Decrement;
		case "not":
			return Not;
		case "neg":
			return Neg;

		default:
			return function;
		}
	}

	public static boolean isTimerStart(String function) {
		return function == TimerStart;
	}

}
