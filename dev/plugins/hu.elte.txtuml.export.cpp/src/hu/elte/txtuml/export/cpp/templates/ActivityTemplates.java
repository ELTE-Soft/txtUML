package hu.elte.txtuml.export.cpp.templates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates.OperationSide;
import hu.elte.txtuml.utils.Pair;

public class ActivityTemplates {
	public static final String AddSimpleTypeOp = "+=";
	public static final String ReplaceSimpleTypeOp = "=";
	public static final String AddCompositTypeOp = ".push_back";
	public static final String ReplaceCompositTypeOp = ReplaceSimpleTypeOp;
	public static final String Self = "this";
	public static final String AccessOperatorForSets = GenerationNames.SimpleAccess;
	
	public enum OperationSide {Left,Right}
	
	public static String generalSetValue(String leftValueName, String rightValueName, String operator) {
		if (operator == AddCompositTypeOp) {
			return leftValueName + operator + "(" + rightValueName + ");\n";
		}
		return leftValueName + operator + rightValueName + ";\n";
	}

	public static StringBuilder signalSend(String signalName, String targetName, String targetTypeName, String accessOperator,
			List<String> params) {
		StringBuilder source = new StringBuilder(targetName + accessOperator);
		StringBuilder signal = new StringBuilder(GenerationNames.eventClassName(signalName) + "(");
		signal.append(GenerationNames.derefenrencePointer(targetName) + ",");

		signal.append(targetTypeName + "::" + GenerationNames.eventEnumName(signalName));
		String paramList = operationCallParamList(params);
		if (!paramList.isEmpty()) {
			signal.append("," + paramList);
		}
		signal.append( ")");
		
		source.append(RuntimeTemplates.sendSignal(signal.toString()));
		source.append(");\n");
	
		return source;
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
		String source = operationName + "(";
		if (params != null) {
			source += operationCallParamList(params);
		}
		source += ")";
		return source;
	}

	public static String operationCall(String ownerName, String accessOperator, String operationName,
			List<String> params) {
		String source = operationCall(operationName, params);
		if (ownerName != null && ownerName != "") {
			source = ownerName + accessOperator + source;
		}
		return source;
	}
	
	public static String stdLibOperationCall(String operationName, String left, String right) {
	    Operators.Init();
	    if(Operators.operationsOnPrimitiveTypesMap.get(operationName) != null ) {
		return "(" + left + " " + Operators.operationsOnPrimitiveTypesMap.get(operationName) + " " + right + ")";
	    }
	    else {
		return "";
	    }
	     
	}
	
	public static String stdLibOperationCall(String operationName, String operand, OperationSide side) {
	    if(side == OperationSide.Right) {
		return operand + operationName + ";";
	    }
	    else {
		return operationName + operand + ";";
	    }
	}
	
	public static String operationCallOnPointerVariable(String ownerName, String operationName, List<String> params) {
	    return operationCall(ownerName,GenerationNames.PointerAccess,operationName,params);
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

	public static String whileCycle(String cond, String body, String update) {
		return simpleCondControlStruct("while", cond, body);
	}

	public static String transitionActionParameter(String paramName) {
		return GenerationNames.RealEventName + "." + paramName;
	}

	public static String createObject(String typenName, String objName) {
		
		return GenerationNames.pointerType(typenName) + " " + objName + "= " + GenerationNames.MemoryAllocator
			+ " " + typenName + "();\n";
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
	
	public static String addVariableTemplate(String type,String left, String right) {
	    return PrivateFunctionalTemplates.cppType(type) + " " + left + " = " + right + ";\n";
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
		
		public static String Fork(String cond, String e1, String e2) {
		    return cond + " ? " + e1 + " : " + e2;
		}
		
		
		public static Map<String,String> operationsOnPrimitiveTypesMap;
		public static Map<String, Pair<String,OperationSide> > oneOperandOperationsOnPrimitiveTypesMap;
		public static void Init() {
		    operationsOnPrimitiveTypesMap = new HashMap<String,String>();
		    oneOperandOperationsOnPrimitiveTypesMap = new HashMap<String,Pair<String,OperationSide> >();
		    operationsOnPrimitiveTypesMap.put("add", Add);
		    operationsOnPrimitiveTypesMap.put("sub", Sub);
		    operationsOnPrimitiveTypesMap.put("mul", Mul);
		    operationsOnPrimitiveTypesMap.put("div", Div);
		    operationsOnPrimitiveTypesMap.put("mod", Mod);
		    operationsOnPrimitiveTypesMap.put("eq", Equal);
		    operationsOnPrimitiveTypesMap.put("neq", NotEqual);
		    operationsOnPrimitiveTypesMap.put("lt", LessThen);
		    operationsOnPrimitiveTypesMap.put("gt", GreatThen);
		    operationsOnPrimitiveTypesMap.put("leq", LessOrEqThen);
		    operationsOnPrimitiveTypesMap.put("geq", GreatOrEqThen);
		    
		    oneOperandOperationsOnPrimitiveTypesMap.put("inc", new Pair<String, OperationSide> (Increment, OperationSide.Right));
		    oneOperandOperationsOnPrimitiveTypesMap.put("dec", new Pair<String, OperationSide> (Decrement, OperationSide.Right));
		    oneOperandOperationsOnPrimitiveTypesMap.put("delayedInc", new Pair<String, OperationSide> (Increment, OperationSide.Left));
		    oneOperandOperationsOnPrimitiveTypesMap.put("delayedDec", new Pair<String, OperationSide> (Decrement, OperationSide.Left));
		    oneOperandOperationsOnPrimitiveTypesMap.put("neg", new Pair<String, OperationSide> (Not, OperationSide.Right));
		    
		    operationsOnPrimitiveTypesMap.put("and", And);
		    operationsOnPrimitiveTypesMap.put("or", Or);
		    oneOperandOperationsOnPrimitiveTypesMap.put("not", new Pair<String, OperationSide> (Increment, OperationSide.Right));
		    
		    operationsOnPrimitiveTypesMap.put("concat", Add);
		    
		    oneOperandOperationsOnPrimitiveTypesMap.put("id", new Pair<String, OperationSide> ("", OperationSide.Right));
		      
		}
	}
	
	
	
}
