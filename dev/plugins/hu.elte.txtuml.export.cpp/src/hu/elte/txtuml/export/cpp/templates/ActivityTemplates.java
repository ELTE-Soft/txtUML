package hu.elte.txtuml.export.cpp.templates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates.LinkFunctionType;
import hu.elte.txtuml.utils.Pair;

public class ActivityTemplates {
	public static final String AddSimpleTypeOp = "+=";
	public static final String ReplaceSimpleTypeOp = "=";
	public static final String AddCompositTypeOp = ".push_back";
	public static final String ReplaceCompositTypeOp = ReplaceSimpleTypeOp;
	public static final String Self = "this";
	public static final String AccessOperatorForSets = GenerationNames.SimpleAccess;
	public static final String SignalSmartPointerType = GenerationNames.EventPtr;
	public static final String ProcessorDirectivesSign = "#";
	public static final String CreateStereoType = "Create";
	public static final String GetSignalFunctionName = "getSignal";
	public static final String TempVar = "temp";
	public static final String NullPtrLiteral = GenerationNames.NullPtr;

	public enum OperationSide {
		Left, Right
	}

	public enum CreateObjectType {
		Signal, Class
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

	public static String linkObjects(String firstObjectName, String secondObjectName, String associationName,
			String endPoint1, String endPoint2, LinkFunctionType linkType) {
		return GenerationNames.ActionFunctionsNamespace + "::" + GenerationTemplates.getLinkFunctionName(linkType) + "<"
				+ associationName + ",typename " + associationName + "::" + endPoint1 + ",typename " + associationName
				+ "::" + endPoint2 + ">" + "(" + firstObjectName + "," + secondObjectName + ");\n";
	}

	public static String signalSend(String target, String signalName) {
		return target + GenerationNames.PointerAccess + GenerationNames.SendSignal + "(" + signalName + ");\n";
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

	public static String isEqualTesting(String firstArgument, String secondArgument) {
		return firstArgument + " " + Operators.Equal + " " + secondArgument;
	}

	public static String stdLibCall(String functionName, List<String> parameters) {
		if(Operators.isInfixBinaryOperator(functionName)) {
			assert(parameters.size() == 2);
			return "(" + parameters.get(0) + Operators.resolveBinaryOperation(functionName) + 
					parameters.get(1) + ")";
		}
		
		return Operators.getStandardLibaryFunctionName(functionName) + "(" + operationCallParamList(parameters) + ")";
	}

	public static String operationCallOnPointerVariable(String ownerName, String operationName, List<String> params) {
		return operationCall(ownerName, GenerationNames.PointerAccess, operationName, params);
	}

	public static String blockStatement(String statement) {

		return statement + ";\n";
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

	public static String foreachCycle(String conatinedType, String paramName, String collection, String body,
			String inits) {
		return inits + "for (" + PrivateFunctionalTemplates.cppType(conatinedType) + " " + paramName + " :" + collection
				+ ")\n{\n" + body + "\n}\n";
	}

	public static String transitionActionParameter(String paramName) {
		return GenerationNames.RealEventName + "." + paramName;
	}

	public static String createObject(String typeName, String objName, CreateObjectType objectType,
			List<String> parameters) {

		if (objectType.equals(CreateObjectType.Signal)) {
			return GenerationNames.signalType(typeName) + " " + objName + ";\n";
		} else {
			return GenerationNames.pointerType(typeName) + " " + objName + " " + ReplaceSimpleTypeOp + " "
					+ GenerationNames.NullPtr + ";\n";
		}

	}

	public static String constructorCall(String ownerName, String typeName, CreateObjectType objectType,
			List<String> parameters) {
		if (objectType.equals(CreateObjectType.Signal)) {
			return ownerName + ReplaceSimpleTypeOp + GenerationNames.signalType(typeName) + "("
					+ GenerationNames.MemoryAllocator + " " + PrivateFunctionalTemplates.signalType(typeName) + "("
					+ operationCallParamList(parameters) + "))";
		} else {
			return ownerName + ReplaceSimpleTypeOp + GenerationNames.MemoryAllocator + " " + typeName + "("
					+ operationCallParamList(parameters) + ")";
		}
	}

	public static String createObject(String typenName, String objName, CreateObjectType objectType) {

		return createObject(typenName, objName, objectType, new ArrayList<String>());
	}

	public static String selectAnyTemplate(String otherEnd) {
		return otherEnd + GenerationNames.SimpleAccess + GenerationNames.SelectAnyFunctionName + "()";

	}

	public static String selectAllTemplate(String target, String otherEnd, String associationName) {
		return target + GenerationNames.PointerAccess
				+ GenerationTemplates.formatAssociationRoleName(otherEnd, associationName)
				+ GenerationNames.SimpleAccess + GenerationNames.SelectAllFunctionName + "()";
	}

	public static String collectionTemplate(String collectedType) {
		return GenerationNames.Collection + "<" + PrivateFunctionalTemplates.cppType(collectedType) + ">";

	}

	public static String getRealSignal(String signalType, String signalVariableName) {
		StringBuilder source = new StringBuilder("");
		source.append(GenerationNames.signalType(signalType) + " ");
		source.append(signalVariableName + " = ");
		source.append(GenerationNames.signalType(signalType) + "(");
		source.append(GenerationNames.MemoryAllocator + " " + PrivateFunctionalTemplates.signalType(signalType));
		source.append("(" + GenerationNames.StaticCast + "<const " + PrivateFunctionalTemplates.signalType(signalType)
				+ "&>");
		source.append("(" + GenerationNames.EventFParamName + ")));\n");
		return source.toString();
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

	public static String generatedTempVariable(int count) {
		return "gen" + count;
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

		public static final String Log = "log";
		public static final String Select = "select";
		public static final String Concat = "concat";
		public static final String ToString = "toString";
		public static final String Count = "count";
		public static final String Round = "round";
		public static final String Sinus = "sin";
		public static final String Cosinus = "cos";
		public static final String TimerStart = GenerationNames.StartTimerFunctionName;
		
		
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

		public static String getStandardLibaryFunctionName(String function) {
			switch (function) {
			case ToString:
				return GenerationNames.ConversionNamspace + "::" + "to_string";
			case Log:
				return GenerationNames.ActionFunctionsNamespace + "::" + Log;
			case TimerStart:
				return GenerationNames.TimerInterFaceName + "::" + TimerStart;
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
	}

	public static boolean invalidIdentifier(String name) {

		return name.startsWith(ProcessorDirectivesSign) || name.equals("return");
	}

	public static String setRegex(String variableName) {
		return "[ ]*" + variableName + "[ ]*=[^;]*;\n";
	}

	public static String declareRegex(String variableName) {
		return "[ a-zA-z0-9<>:*]*" + variableName + "[ ]*;\n";
	}

	public static String formatUserVar(String varName, int userVarCounter) {
		return varName + "_us" + userVarCounter;
	}

}
