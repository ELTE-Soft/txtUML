package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelType;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;

/**
 * This class is responsible for importing ModelType operations in method bodies.
 * @author Adam Ancsin
 *
 */
public class ModelTypeOpImporter extends AbstractMethodImporter {

	/**
	 * Enumerates the different types of ModelInt operations.
	 * @author Adam Ancsin
	 *
	 */
	private enum ModelIntOperationType
	{
		ADD_LITERAL,
		SUBTRACT_LITERAL,
		MULTIPLY_LITERAL, 
		DIVIDE_LITERAL,
		REMAINDER_LITERAL,
		SIGNUM_LITERAL, 
		NEGATE_LITERAL,
		ABS_LITERAL
	}
	
	/**
	 * Enumerates the different types of ModelBool operations.
	 * @author Adam Ancsin
	 *
	 */
	private enum ModelBoolOperationType
	{
		NOT_LITERAL,
		OR_LITERAL,
		AND_LITERAL,
		XOR_LITERAL,
		EQUAL_LITERAL,
		NOTEQ_LITERAL
	}
	
	/**
	 * Imports the add ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntAddOp(ModelInt target, ModelInt val)  
	{
		return importModelInt2OpOperation(target,val,ModelIntOperationType.ADD_LITERAL);	
	}
	
	/**
	 * Imports the subtract ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntSubtractOp(ModelInt target, ModelInt val)
	{
		return importModelInt2OpOperation(target,val,ModelIntOperationType.SUBTRACT_LITERAL);	
	}
	
	/**
	 * Imports the multiply ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntMultiplyOp(ModelInt target, ModelInt val) 
	{
		return importModelInt2OpOperation(target,val,ModelIntOperationType.MULTIPLY_LITERAL);
	}
	
	/**
	 * Imports the divide ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntDivideOp(ModelInt target, ModelInt val)
	{
		return importModelInt2OpOperation(target,val,ModelIntOperationType.DIVIDE_LITERAL);	
	}
	
	/**
	 * Imports the remainder ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntRemainderOp(ModelInt target, ModelInt val)
	{
		return importModelInt2OpOperation(target,val,ModelIntOperationType.REMAINDER_LITERAL);	
	}
	
	/**
	 * Imports the negate ModelInt operation.
	 * @param target The dummy instance of the operand
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntNegateOp(ModelInt target)  
	{
		return importModelInt1OpOperation(target,ModelIntOperationType.NEGATE_LITERAL);
	}
	
	/**
	 * Imports the "abs" ModelInt operation.
	 * @param target The dummy instance of the operand
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntAbsOp(ModelInt target) 
	{
		return importModelInt1OpOperation(target,ModelIntOperationType.ABS_LITERAL);
	}
	
	/**
	 * Imports the signum ModelInt operation.
	 * @param target The dummy instance of the operand
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelInt importModelIntSignumOp(ModelInt target)
	{
		return importModelInt1OpOperation(target,ModelIntOperationType.SIGNUM_LITERAL);	
	}

	/**
	 * Imports the "not" ModelBool operation.
	 * @param target The dummy instance of the operand
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelBoolNotOp(ModelBool target)
	{
		return (ModelBool)importModelType1OpOperation(target,new ModelBool(),"not ",false);
	}
	
	/**
	 * Imports the "or" ModelBool operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelBoolOrOp(ModelBool target, ModelBool val) 
	{
		return importModelBool2OpOperation(target,val,ModelBoolOperationType.OR_LITERAL);
	}
	
	/**
	 * Imports the "xor" ModelBool operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelBoolXorOp(ModelBool target, ModelBool val)
	{
		return importModelBool2OpOperation(target,val,ModelBoolOperationType.XOR_LITERAL);
	}
	
	/**
	 * Imports the "and" ModelBool operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelBoolAndOp(ModelBool target, ModelBool val)
	{
		return importModelBool2OpOperation(target,val,ModelBoolOperationType.AND_LITERAL);
	}
	
	/**
	 * Imports the "equal" ModelBool operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelBoolEqualOp(ModelBool target, ModelBool val) 
	{
		return importModelBool2OpOperation(target,val,ModelBoolOperationType.EQUAL_LITERAL);
	}
	
	/**
	 * Imports the "not equal" ModelBool operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelBoolNotEqOp(ModelBool target, ModelBool val) 
	{
		return importModelBool2OpOperation(target,val,ModelBoolOperationType.NOTEQ_LITERAL);
	}

	/**
	 * Imports the "is equal" ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelIntIsEqualOp(ModelInt left, ModelInt right) 
	{
		return compareModelInts(left,right,"=");
	}
	
	/**
	 * Imports the "is less equal" ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelIntIsLessEqualOp(ModelInt left, ModelInt right) 
	{
		return compareModelInts(left,right,"<=");
	}
	
	/**
	 * Imports the "is less" ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelIntIsLessOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"<");
	}
	
	/**
	 * Imports the "is more equal" ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelIntIsMoreEqualOp(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,">=");
	}
	
	/**
	 * Imports the "is more" ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param val The dummy instance of the right operand.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static ModelBool importModelIntIsMoreOp(ModelInt left, ModelInt right)	
	{
		return compareModelInts(left,right,">");
	}
	
	/**
	 * Imports a generic 2-operand ModelType operation.
	 * @param target The dummy instance of the left operand.
	 * @param value The dummy instance of the right operand.
	 * @param result The dummy instance of the operation result-
	 * @param operator The operator.
	 * @param isFunction Marks that the operation is a function or not.
	 * @return The dummy instance of the operation result.
	 *
	 * @author Adam Ancsin
	 */
	private static <T> ModelType<T> importModelType2OpOperation
				(ModelType<T> target, ModelType<T> value, ModelType<T> result, String operator, boolean isFunction)
	{	
		String valueExpression=null;
		String newValExpr=getExpression(value);
		String targetExpr=getExpression(target);

		if(isFunction)
			valueExpression=targetExpr+"."+operator+"("+newValExpr+")";
		else
			valueExpression=targetExpr+operator+newValExpr;
		
		if(currentActivity!=null)
			setVariableValue(result,valueExpression);
		else	
			InstanceManager.createLocalInstancesMapEntry(result,InstanceInformation.createCalculated(valueExpression));
		
		return result;
	}
	
	/**
	 * Imports a generic 2-operand ModelInt operation.
	 * @param target The dummy instance of the left operand.
	 * @param value The dummy instance of the right operand.
	 * @param operationType The type of the operation. (e.g. add, subtract, multiply, etc.)
	 * @return The dummy instance of the result of the operation.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("incomplete-switch")
	private static ModelInt importModelInt2OpOperation(ModelInt target, ModelInt value, ModelIntOperationType operationType) 
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
	
	/**
	 * Imports a generic 2-operand ModelBool operation.
	 * @param target The dummy instance of the left operand.
	 * @param value The dummy instance of the right operand.
	 * @param operationType The type of the operation. (e.g. and, or, xor, etc.)
	 * @return The dummy instance of the result of the operation.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("incomplete-switch")
	private static ModelBool importModelBool2OpOperation(ModelBool target, ModelBool value, ModelBoolOperationType operationType) 
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
	
	/**
	 * Imports a generic 1-operand ModelType operation.
	 * @param target The dummy instance of the operand.
	 * @param result The dummy instance of the operation result-
	 * @param operator The operator.
	 * @param isFunction Marks that the operation is a function or not.
	 * @return The dummy instance of the operation result.
	 *
	 * @author Adam Ancsin
	 */
	private static <T> ModelType<T> importModelType1OpOperation
			(ModelType<T> target, ModelType<T> result, String operator, boolean isFunction)
	{
		String valueExpression=null;
		String targetExpr=getExpression(target);

		if(isFunction)
			valueExpression=targetExpr+"."+operator+"()";
		else
			valueExpression=operator+targetExpr;
		
		if(currentActivity!=null)
			setVariableValue(result,valueExpression);
		else			
			InstanceManager.createLocalInstancesMapEntry(result,InstanceInformation.createCalculated(valueExpression));

		return result;
	}
	
	/**
	 * Imports a generic 1-operand ModelInt operation.
	 * @param target The dummy instance of the operand.
	 * @param operationType The type of the operation. (e.g. add, subtract, multiply, etc.)
	 * @return The dummy instance of the result of the operation.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("incomplete-switch")
	private static ModelInt importModelInt1OpOperation(ModelInt target,ModelIntOperationType operationType)  {

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
	
	/**
	 * Imports a generic 2-operand ModelInt comparing operation.
	 * @param left The dummy instance of the left operand.
	 * @param right The dummy instance of the right operand.
	 * @param operator The comparing operator.
	 * @return The dummy instance of the result of the operation.
	 *
	 * @author Adam Ancsin
	 */
	private static ModelBool compareModelInts(ModelInt left, ModelInt right, String operator)
	{
		String leftExpr=getExpression(left);
		String rightExpr=getExpression(right);
		String expression=leftExpr+" "+operator+" "+rightExpr;
		
		ModelBool result=new ModelBool();
		InstanceManager.createLocalInstancesMapEntry(result,InstanceInformation.createCalculated(expression));
		
		return result;
	}
}
