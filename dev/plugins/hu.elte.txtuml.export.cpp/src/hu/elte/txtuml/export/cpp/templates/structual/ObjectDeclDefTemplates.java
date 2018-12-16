package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.List;
import java.util.Optional;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;

public class ObjectDeclDefTemplates {
	
	public enum AllocateType {
		RawPointer,
		SharedPtr,
		Temporary
	}

	public static String variableDecl(String typeName, String variableName, String defaultValue, Optional<List<String>> templateParameters, GenerationTemplates.VariableType varType, boolean isStatic) {
		StringBuilder source = new StringBuilder("");
		String type = "";
		String actualTypeName = typeName + CppExporterUtils.createTemplateParametersCode(templateParameters);
		type = PrivateFunctionalTemplates.cppType(actualTypeName, varType);
	
		if(isStatic) {
			source.append(ModifierNames.StaticModifier + " ");
		}
		source.append(type);
		source.append(" ");
		source.append(variableName);
		if (defaultValue != "" && defaultValue != null) {
			source.append(ActivityTemplates.ReplaceSimpleTypeOp + defaultValue);
		}

		return ActivityTemplates.blockStatement(source.toString());
	}

	public static String variableDecl(String typeName, String variableName) {
		return variableDecl(typeName, variableName, "", Optional.empty(), GenerationTemplates.VariableType.RawPointerType, false);
	}

	public static String variableDecl(String typeName, String variableName, GenerationTemplates.VariableType varType) {
		return variableDecl(typeName, variableName, "", Optional.empty(), varType, false);
	}
	
	public static String propertyDecl(String typeName, String variableName, String defaultValue, Optional<List<String>> templateParameters, GenerationTemplates.VariableType varType) {
		return variableDecl(typeName,variableName, defaultValue,templateParameters, varType, false);
	}
	
	public static String propertyDecl(String typeName, String variableName, String defaultValue,  GenerationTemplates.VariableType varType) {
		return variableDecl(typeName, variableName, defaultValue,Optional.empty(), varType, false);
	}
	
	public static String propertyDecl(String typeName, String variableName, String defaultValue) {
		return variableDecl(typeName, variableName, defaultValue,Optional.empty(), GenerationTemplates.VariableType.RawPointerType, false);
	}

	public static String createObject(String typeName, String objName, boolean sharedObject) {
		return ObjectDeclDefTemplates.createObject(typeName, objName, null, null, sharedObject);
	}

	public static String createObject(String typeName, String objName, Optional<List<String>> params, boolean sharedObject) {
		return ObjectDeclDefTemplates.createObject(typeName, objName, null, params, sharedObject);
	}

	public static String createObject(String typeName, String objName, Optional<List<String>> templateParams,
			Optional<List<String>> params, boolean sharedObject) {
		String templateList = CppExporterUtils.createTemplateParametersCode(templateParams);
		if(!sharedObject) {
			return GenerationNames.sharedPtrType(typeName + templateList) + " " + 
					setAllocatedObjectToObjectVariable(typeName,templateParams, objName, params, AllocateType.RawPointer);
		} else {
			return GenerationNames.sharedPtrType(typeName + templateList) + " " + 
							setAllocatedObjectToObjectVariable(typeName,templateParams, objName, params, AllocateType.SharedPtr);
		}
	
	
	}
	
	public static String setAllocatedObjectToObjectVariable(String typeName, 
			Optional<List<String>> templateParams, String objName, Optional<List<String>> params, AllocateType allocType) {
		return objName  + " = " + allocateObject(typeName, templateParams, params, allocType) + ";\n";
	}

	public static String allocateObject(String typeName, Optional<List<String>> templateParams, 
			Optional<List<String>> params, AllocateType allocType) {
		String templateParameters = CppExporterUtils.createTemplateParametersCode(templateParams);
		String allocatedObject =  typeName + 
				templateParameters + 
				CppExporterUtils.createParametersCode(params);	
		switch(allocType) {
		case Temporary:
			return allocatedObject;
		case RawPointer:
			return PointerAndMemoryNames.MemoryAllocator + " " + allocatedObject;
		case SharedPtr:
			 return GenerationNames.sharedPtrType(typeName + templateParameters) + "(" + 
			 	PointerAndMemoryNames.MemoryAllocator + " "  + allocatedObject + ")";
		default:
			return allocatedObject;
		
		}

	}

	public static String allocateObject(String typeName, Optional<List<String>> params, AllocateType allocType) {
		return allocateObject(typeName, Optional.empty(), params, allocType);
	}

	public static String allocateObject(String typeName) {
		return allocateObject(typeName, Optional.empty(), Optional.empty(), AllocateType.RawPointer);
	}

	public static String staticPropertyDecl(String typeName, String variableName) {
		return variableDecl(typeName, variableName, "",Optional.empty(), GenerationTemplates.VariableType.StackStored, true);
	}
	
	public static String staticPropertyDef(String typeName, String ownerClassName, String propertyName, Optional<String> value) {
		String leftExpression =  typeName + " " + ownerClassName + "::" + propertyName;
		String rightPart = value.isPresent() ? " " + ActivityTemplates.ReplaceSimpleTypeOp + " " + value : "";
		return leftExpression + rightPart + ";\n";

	}

}
