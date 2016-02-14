package hu.elte.txtuml.export.cpp.templates;

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

	public static String generalSetValue(String leftValueName, String rightValueName, String operator) {
		if (operator == AddCompositTypeOp) {
			return leftValueName + operator + "(" + rightValueName + ");\n";
		}
		return leftValueName + operator + rightValueName + ";\n";
	}

	public static String signalSend(String signalName, String targetName, String targetTypeName, String accessOperator,
			List<String> params, Boolean rt) {
		String source = targetName + accessOperator;
		String signal = GenerationNames.eventClassName(signalName) + "(";
		if (rt) {
			signal += GenerationNames.derefenrencePointer(targetName) + ",";
		}
		signal += targetTypeName + "::" + GenerationNames.eventEnumName(signalName);
		String paramList = operationCallParamList(params);
		if (!paramList.isEmpty()) {
			signal += "," + paramList;
		}
		signal += ")";

		if (rt) {
			source += RuntimeTemplates.sendSignal(signal);
		} else {
			source += "" + GenerationNames.ProcessEventFName + "(" + signal;
		}

		return source + ");\n";
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
		source += ");\n";
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

	public static String simpleWhile(String cond, String body) {
		return simpleCondControlStruct("while", cond, body);
	}

	public static String transitionActionParameter(String paramName) {
		return GenerationNames.RealEventName + "." + paramName;
	}

	public static String createObject(String typenName, String objName, Boolean rt, Boolean isSm) {
		String source;
		if (rt && isSm) {
			source = GenerationNames.pointerType(typenName) + " " + objName + "= " + GenerationNames.MemoryAllocator
					+ " " + typenName + "(" + RuntimeTemplates.RuntimeVarName + ");\n";
			source += objName + GenerationNames.PointerAccess + "startSM();\n";
			// source+=RuntimeTemplates.CreateObject(objName_);
		} else {
			source = GenerationNames.pointerType(typenName) + " " + objName + "= " + GenerationNames.MemoryAllocator
					+ " " + typenName + "();\n";
		}
		return source;
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

	public static class Operators {
		public static final String Not = "!";
		public static final String And = "&&";
		public static final String Or = "||";
		public static final String NotEqual = "!=";
		public static final String Equal = "==";
		public static final String Remove = "remove";
		public static final String First = "front";
		public static final String Last = "back";
	}
}
