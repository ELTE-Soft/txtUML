package hu.elte.txtuml.export.uml2.transform.aspects;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelInt;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import hu.elte.txtuml.export.uml2.transform.ModelTypeOpImporter;

/**
 * This aspect contains advices for importing ModelType operations.
 * @author Adam Ancsin
 *
 */
public privileged aspect ModelTypeOpImporterAspect extends AbstractImporterAspect {

	
	/**
	 * This advice imports the "add" ModelInt operation.
	 * Runs if the "add" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.add(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntAddOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "subtract" ModelInt operation.
	 * Runs if the "subtract" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.subtract(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntSubtractOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "multiply" ModelInt operation.
	 * Runs if the "multiply" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.multiply(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntMultiplyOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "divide" ModelInt operation.
	 * Runs if the "divide" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.divide(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntDivideOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "remainder" ModelInt operation.
	 * Runs if the "remainder" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.remainder(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntRemainderOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "abs" ModelInt operation.
	 * Runs if the "abs" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.abs())
	{
		return ModelTypeOpImporter.importModelIntAbsOp(target);
	}
	
	/**
	 * This advice imports the "signum" ModelInt operation.
	 * Runs if the "signum" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.signum())
	{
		return ModelTypeOpImporter.importModelIntSignumOp(target);
	}
	
	/**
	 * This advice imports the "negate" ModelInt operation.
	 * Runs if the "negate" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.negate())
	{
		return ModelTypeOpImporter.importModelIntNegateOp(target);
	}
	
	/**
	 * This advice imports the "and" ModelBool operation.
	 * Runs if the "and" method is called on a ModelBool instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.and(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolAndOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "or" ModelBool operation.
	 * Runs if the "or" method is called on a ModelBool instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.or(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolOrOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "xor" ModelBool operation.
	 * Runs if the "xor" method is called on a ModelBool instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.xor(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolXorOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "equal" ModelBool operation.
	 * Runs if the "equ" method is called on a ModelBool instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.equ(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolEqualOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "not equal" ModelBool operation.
	 * Runs if the "notEqu" method is called on a ModelBool instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.notEqu(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolNotEqOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "not" ModelBool operation.
	 * Runs if the "not" method is called on a ModelBool instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.not())
	{
		return ModelTypeOpImporter.importModelBoolNotOp(target);
	}	
	
	/**
	 * This advice imports the "is equal" ModelInt operation.
	 * Runs if the "isEqual" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isEqual(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsEqualOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "is less equal" ModelInt operation.
	 * Runs if the "isLessEqual" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isLessEqual(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsLessEqualOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "is more equal" ModelInt operation.
	 * Runs if the "isMoreEqual" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isMoreEqual(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsMoreEqualOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "is less" ModelInt operation.
	 * Runs if the "isLess" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isLess(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsLessOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	
	/**
	 * This advice imports the "is more" ModelInt operation.
	 * Runs if the "isMore" method is called on a ModelInt instance (the target) in a txtUML method body
	 * during model import.
	 * @param target The dummy instance of the target of the call (the left operand).

	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isMore(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsMoreOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
}
