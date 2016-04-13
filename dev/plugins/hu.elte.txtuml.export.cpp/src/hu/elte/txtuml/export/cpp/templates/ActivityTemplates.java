package hu.elte.txtuml.export.cpp.templates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import hu.elte.txtuml.utils.Pair;

public class ActivityTemplates {
	public static final String AddSimpleTypeOp = "+=";
	public static final String ReplaceSimpleTypeOp = "=";
	public static final String AddCompositTypeOp = ".push_back";
	public static final String ReplaceCompositTypeOp = ReplaceSimpleTypeOp;
	public static final String Self = "this";
	public static final String AccessOperatorForSets = GenerationNames.SimpleAccess;

	public enum OperationSide {
		Left, Right
	}

	public static String generalSetValue(String leftValueName, String rightValueName, String operator) {
		if (operator == AddCompositTypeOp) {
			return leftValueName + operator + "(" + rightValueName + ");\n";
		}
		return leftValueName + operator + rightValueName + ";\n";
	}

	public static String simpleSetValue(String leftValueName, String rightValueName) {
		return generalSetValue(leftValueName, rightValueName, ReplaceSimpleTypeOp);
	}

	public static StringBuilder signalSend(String signalName, String targetName, String targetTypeName,
			String accessOperator, List<String> params) {
		StringBuilder source = new StringBuilder(targetName + accessOperator);
		StringBuilder signal = new StringBuilder(GenerationNames.eventClassName(signalName) + "(");
		signal.append(GenerationNames.derefenrencePointer(targetName) + ",");

		signal.append(targetTypeName + "::" + GenerationNames.eventEnumName(signalName));
		String paramList = operationCallParamList(params);
		if (!paramList.isEmpty()) {
			signal.append("," + paramList);
		}
		signal.append(")");

		source.append(RuntimeTemplates.sendSignal(signal.toString()));
		source.append(");\n");

		return source;
	}

	public static String linkObjects(String firstClassName, String firstObjectName, String secondClassName,
			String secondObjectName) {
		return GenerationNames.ActionFunctionsNamespace + "::" + GenerationNames.LinkFunctionName + "<" + firstClassName
				+ "," + secondClassName + ">" + "(" + firstObjectName + "," + secondObjectName + ");\n";
	}

	public static String signalSend(String target, String signalName) {
		return target + GenerationNames.PointerAccess + GenerationNames.SendSignal + "(" + GenerationNames.EventPtr
				+ "(" + signalName + "));\n";
	}

	public static String transitionActionCall(String operationName) {
		List<String> params = new LinkedList<String>();
		params.add(GenerationNames.EventFParamName);
		return operationCall(operationName, params);
	}

	public static String operationCall(String operationName) {
		return operationCall(operationName, null);
	}

	public static String operationCall(String operationName, List<String> params) {
		StringBuilder source = new StringBuilder(operationName + "(");
		if (params != null) {
			source.append(operationCallParamList(params));
		}
		source.append(")");
		return source.toString();
	}

	public static String operationCall(String ownerName, String accessOperator, String operationName,
			List<String> params) {
		String source = operationCall(operationName, params);
		if (ownerName != null && ownerName != "") {
			source = ownerName + accessOperator + source;
		}
		return source;
	}

	public static String invokeProcedure(String operationName, List<String> parameters) {
		return Operators.getStandardOperationName(operationName) + "(" + operationCallParamList(parameters) + ");\n";
	}

	public static String stdLibOperationCall(String operationName, String left, String right) {

		return "(" + left + " " + Operators.getStandardOperationName(operationName) + " " + right + ")";

	}

	public static String stdLibOperationCall(String operationName, String operand) {
		return Operators.getStandardSigneleOperatorName(operationName) + operand;

	}

	public static String simpleFunctionCall(String functionName, List<String> parameters) {
		return functionName + "(" + operationCallParamList(parameters) + ")";
	}

	public static String operationCallOnPointerVariable(String ownerName, String operationName, List<String> params) {
		return operationCall(ownerName, GenerationNames.PointerAccess, operationName, params);
	}

	public static StringBuilder blockStatement(String statement) {
	    	
		return new StringBuilder(statement).append(";\n");
	}

	private static String operationCallParamList(List<String> params) {
		String source = "";
		for (String param : params) {
			source += param + ",";
		}
		if (!params.isEmpty()) {
			source = source.substring(0, source.length() - 1);
		}
		return source;
	}

	public static String startObject(String objectVariable) {
		return objectVariable + GenerationNames.PointerAccess + GenerationNames.StartSmMethodName + "();\n";
	}

	public static String deleteObject(String objectVariable) {
		return GenerationNames.DeleteObject + " " + objectVariable + ";\n";
	}

	public static String simpleCondControlStruct(String control, String cond, String body) {
		return control + "(" + cond + ")\n{\n" + body + "}\n";
	}

	public static String simpleIf(String cond, String body) {
		return simpleCondControlStruct("if", cond, body);
	}

	public static StringBuilder elseIf(List<Pair<String, String>> branches) {
		StringBuilder source = new StringBuilder("");
		int i = 1;
		for (Pair<String, String> part : branches) {
			if (i == 1) {
				source.append(simpleIf(part.getFirst(), part.getSecond()));
			} else if (i == branches.size() && (part.getFirst().isEmpty() || part.getFirst().equals("else"))) {
				source.append("else\n{\n" + part.getSecond() + "}\n");
			} else {
				source.append(simpleCondControlStruct("else if", part.getFirst(), part.getSecond()));
			}
			++i;
		}
		return source;
	}

	public static String whileCycle(String cond, String body) {
		return simpleCondControlStruct("while", cond, body);
	}

	public static String foreachCycle(String conatinedType, String paramName, String collection, String body) {
		return "for (" + PrivateFunctionalTemplates.cppType(conatinedType) + " " + paramName + " :" + collection
				+ ")\n{\n" + body + "\n}";
	}

	public static String transitionActionParameter(String paramName) {
		return GenerationNames.RealEventName + "." + paramName;
	}

	public static String createObject(String typenName, String objName, List<String> parameters) {

		return GenerationNames.pointerType(typenName) + " " + objName + "= " + GenerationNames.MemoryAllocator + " "
				+ typenName + "(" + operationCallParamList(parameters) + ");\n";
	}

	public static String createObject(String typenName, String objName) {

		return createObject(typenName, objName, new ArrayList<String>());
	}

	public static String selectAnyTemplate(String otherEnd) {
		return otherEnd + GenerationNames.SimpleAccess + GenerationNames.SelectAnyFunctionName + "()";

	}

	public static String selectAllTemplate(String otherEnd) {
		return GenerationNames.Self + GenerationNames.PointerAccess +
			otherEnd + GenerationNames.SimpleAccess + GenerationNames.SelectAllFunctionName + "()";
	}

	public static String collectionTemplate(String collectedType) {
		return GenerationNames.Collection + "<" + PrivateFunctionalTemplates.cppType(collectedType) + ">";

	}

	public static String returnTemplates(String variable) {
		return "return " + variable + ";\n";
	}

	public static String getOperationFromType(boolean isMultivalued, boolean isReplace) {
		String source = "";
		if (isMultivalued)// if the type is a collection
		{
			if (isReplace) {
				source = ReplaceCompositTypeOp;
			} else {
				source = AddCompositTypeOp;
			}
		} else // simple class or basic type
		{
			source = ReplaceSimpleTypeOp;
		}
		return source;
	}

	// Everything is pointer
	public static String accesOperatoForType(String typeName) {
		return GenerationNames.PointerAccess;
	}

	public static String addVariableTemplate(String type, String left, String right) {
		return PrivateFunctionalTemplates.cppType(type) + " " + left + " = " + right + ";\n";
	}

	public static String defineAndAddToCollection(String collectedType, String collectionName, String valueName) {
		return collectionTemplate(collectedType) + " " + collectionName + " " + ReplaceSimpleTypeOp + " " + valueName
				+ ";\n";
	}

	public static String signalType(String type) {
		return type + GenerationNames.EventClassTypeId;
	}

	public static class Operators {

		public static final String Remove = "remove";
		public static final String First = "front";
		public static final String Last = "back";

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

		public static final String Log = "printLine";

		public static String Fork(String cond, String e1, String e2) {
			return cond + " ? " + e1 + " : " + e2;
		}

		public static String getStandardOperationName(String operation) {
			String name = "unknown_operator";
			switch (operation) {
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
			case "log":
				name = Log;
				break;
			}

			return name;
		}

		public static String getStandardSigneleOperatorName(String operation) {
			String name = "";
			switch (operation) {
			case "inc":
				name = Increment;
				break;
			case "dec":
				name = Decrement;
				break;
			case "not":
				name = Not;
				break;
			default:
				name = "";
			}

			return name;
		}

		public static boolean isStdLibFunction(String name) {
			if (name.equals("delayedInc") || name.equals("delayedDec")) {
				return true;
			} else {
				return false;
			}

		}
	}

}
