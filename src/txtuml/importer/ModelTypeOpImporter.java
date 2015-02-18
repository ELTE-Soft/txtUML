package txtuml.importer;

import txtuml.api.ModelBool;
import txtuml.api.ModelInt;
import txtuml.api.ModelString;
import txtuml.api.ModelType;


public class ModelTypeOpImporter extends InstructionImporter {

	private enum ModelIntOperations{
		ADD_LITERAL,
		SUBTRACT_LITERAL,
		MULTIPLY_LITERAL, 
		DIVIDE_LITERAL,
		REMAINDER_LITERAL,
		SIGNUM_LITERAL, 
		NEGATE_LITERAL,
		ABS_LITERAL
	};
	
	private enum ModelBoolOperations{
		NOT_LITERAL,
		OR_LITERAL,
		AND_LITERAL,
		XOR_LITERAL,
		EQUAL_LITERAL,
		NOTEQ_LITERAL
	};
	
	@SuppressWarnings("unused")
	private static <T> void createLiteral(ModelType<T> inst)
	{
		@SuppressWarnings("unchecked")
		T val=(T)getObjectFieldVal(inst,"value");
		String expression=val.toString();
		
		boolean literal=true;
		boolean calculated=false;
		ModelTypeInformation instInfo;
		
		if(val instanceof Integer)
		{
			instInfo=new ModelTypeInformation(expression,literal,calculated,(Integer)val);
		}
		else
		{
			instInfo=new ModelTypeInformation(expression,literal,calculated);
		}
		
		modelTypeInstancesInfo.put(inst,instInfo);
	}

	private static <T> void createCalculatedModelTypeInstInfo(ModelType<T> inst, String expression)
	{
		ModelTypeInformation instInfo=new ModelTypeInformation(expression,false,true);
		modelTypeInstancesInfo.put(inst,instInfo);
	}
	@SuppressWarnings("unchecked")
	private static <T> ModelType<T> importModelType2OpOperation
				(ModelType<T> target, ModelType<T> value, ModelType<T> result, String operator, boolean isFunction)
	{
		
		String valueExpression=null;
		String newValExpr=getExpression(value);
		String targetExpr=getExpression(target);

		if(isFunction)
		{
			valueExpression=targetExpr+"."+operator+"("+newValExpr+")";
		}
		else
		{
			valueExpression=targetExpr+operator+newValExpr;
		}
		
		if(currentActivity!=null)
		{
			setVariableValue(result,valueExpression);
		}
		else
		{
			if(result instanceof ModelInt)
			{
				result=(ModelType<T>) new ModelInt();
			}
			else if(result instanceof ModelBool)
			{
				result=(ModelType<T>) new ModelBool();
			}
			else if(result instanceof ModelString)
			{
				result=(ModelType<T>) new ModelString();
			}
			createCalculatedModelTypeInstInfo(result,valueExpression);
		}
		
		return result;
	}
	@SuppressWarnings("incomplete-switch")
	private static ModelInt importModelInt2OpOperation(ModelInt target, ModelInt value, ModelIntOperations operationType) 
	{
		
		String operator=" ";
		boolean isFunction=false;
		
		switch(operationType)
		{
			case ADD_LITERAL:
				operator=" + ";
			break;
			
			case SUBTRACT_LITERAL:
				operator=" - ";
			break;
			
			case MULTIPLY_LITERAL:
				operator=" * ";
			break;
			
			case DIVIDE_LITERAL:
				operator="div";
				isFunction=true;
			break;
			
			case REMAINDER_LITERAL:
				operator="mod";
				isFunction=true;
			break;		
			
			
		}
		
		ModelInt result=new ModelInt();
		
		return (ModelInt) importModelType2OpOperation(target,value,result,operator,isFunction);
	}
	@SuppressWarnings("incomplete-switch")
	private static ModelBool importModelBool2OpOperation(ModelBool target, ModelBool value, ModelBoolOperations operationType) 
	{
		
		String operator=" ";
		
		boolean isFunction=false;
		switch(operationType)
		{
		
			case OR_LITERAL:
				operator=" or ";
			break;
			
			case XOR_LITERAL:
				operator=" xor ";
			break;
			
			case AND_LITERAL:
				operator=" and ";
			break;
			
			case EQUAL_LITERAL:
				operator=" = ";
			break;
			
			case NOTEQ_LITERAL:
				operator=" <> ";
			break;
			
			
		}
		
		ModelBool result=new ModelBool();
		
		return (ModelBool) importModelType2OpOperation(target,value,result,operator,isFunction);
	}
	@SuppressWarnings("unused")
	private static ModelInt importModelIntAddOp(ModelInt target, ModelInt val)  {

		return importModelInt2OpOperation(target,val,ModelIntOperations.ADD_LITERAL);
		
	}
	
	@SuppressWarnings("unused")
	private static ModelInt importModelIntSubtractOp(ModelInt target, ModelInt val) {

		return importModelInt2OpOperation(target,val,ModelIntOperations.SUBTRACT_LITERAL);
		
	}
	
	@SuppressWarnings("unused")
	private static ModelInt importModelIntMultiplyOp(ModelInt target, ModelInt val)  {

		return importModelInt2OpOperation(target,val,ModelIntOperations.MULTIPLY_LITERAL);
	}
	
	@SuppressWarnings("unused")
	private static ModelInt importModelIntDivideOp(ModelInt target, ModelInt val){

		return importModelInt2OpOperation(target,val,ModelIntOperations.DIVIDE_LITERAL);
		
	}
	
	@SuppressWarnings("unused")
	private static ModelInt importModelIntRemainderOp(ModelInt target, ModelInt val) {

		return importModelInt2OpOperation(target,val,ModelIntOperations.REMAINDER_LITERAL);
		
	}
	
	@SuppressWarnings("unchecked")
	private static <T> ModelType<T> importModelType1OpOperation
			(ModelType<T> target, ModelType<T> result, String operator, boolean isFunction)
	{
		String valueExpression=null;
		String targetExpr=getExpression(target);
	

		if(isFunction)
		{
			valueExpression=targetExpr+"."+operator+"()";
		}
		else
		{
			valueExpression=operator+targetExpr;
		}
		
		if(currentActivity!=null)
		{
			setVariableValue(result,valueExpression);
		}
		else
		{
			if(result instanceof ModelInt)
			{
				result=(ModelType<T>) new ModelInt();
			}
			else if(result instanceof ModelBool)
			{
				result=(ModelType<T>) new ModelBool();
			}
			else if(result instanceof ModelString)
			{
				result=(ModelType<T>) new ModelBool();
			}
			createCalculatedModelTypeInstInfo(result,valueExpression);
		}
		
		return result;
	}
	
	@SuppressWarnings("incomplete-switch")
	private static ModelInt importModelInt1OpOperation(ModelInt target,ModelIntOperations operationType)  {

		boolean isFunction=false;
		String operator="";
		
		switch(operationType)
		{
			case SIGNUM_LITERAL:
				operator="signum";
				isFunction=true;
			break;
			
			case NEGATE_LITERAL:
				operator="-";
			break;		
			
			case ABS_LITERAL:
				operator="abs";
				isFunction=true;
			break;
				
		}
		
		ModelInt result=new ModelInt();
		
		return (ModelInt) importModelType1OpOperation(target,result,operator,isFunction);
		
	}
	
	@SuppressWarnings("unused")
	private static ModelInt importModelIntNegateOp(ModelInt target)  {

		return importModelInt1OpOperation(target,ModelIntOperations.NEGATE_LITERAL);
		
	}
	@SuppressWarnings("unused")
	private static ModelInt importModelIntAbsOp(ModelInt target) {
		return importModelInt1OpOperation(target,ModelIntOperations.ABS_LITERAL);
	}
	
	@SuppressWarnings("unused")
	private static ModelInt importModelIntSignumOp(ModelInt target) {

		return importModelInt1OpOperation(target,ModelIntOperations.SIGNUM_LITERAL);
		
	}

	@SuppressWarnings("unused")
	private static ModelBool importModelBoolNotOp(ModelBool target) {
		return (ModelBool)importModelType1OpOperation(target,new ModelBool(),"not ",false);
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelBoolOrOp(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.OR_LITERAL);
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelBoolXorOp(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.XOR_LITERAL);
	}
	
	
	@SuppressWarnings("unused")
	private  static ModelBool importModelBoolAndOp(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.AND_LITERAL);
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelBoolEqualOp(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.EQUAL_LITERAL);
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelBoolNotEqOp(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.NOTEQ_LITERAL);
	}

	@SuppressWarnings("unused")
	private static ModelBool importModelIntIsEqualOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"=");
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelIntIsLessEqualOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"<=");
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelIntIsLessOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"<");
	}
	

	@SuppressWarnings("unused")
	private static ModelBool importModelIntIsMoreEqualOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,">=");
	}
	
	@SuppressWarnings("unused")
	private static ModelBool importModelIntIsMoreOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,">");
	}
	
	
	private static ModelBool compareModelInts(ModelInt left, ModelInt right, String operator)
	{
		String leftExpr=getExpression(left);
		String rightExpr=getExpression(right);
		String expression=leftExpr+" "+operator+" "+rightExpr;
		
		ModelBool result=new ModelBool();
		createCalculatedModelTypeInstInfo(result,expression);
		
		return result;
	}

	
}
