package txtuml.importer;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import txtuml.api.ModelBool;
import txtuml.api.ModelInt;
import txtuml.api.ModelString;

public privileged aspect ModelTypeOpImporterAspect extends AbstractImporterAspect {

	after() returning(ModelInt target) : call((ModelInt).new(int)) && isActive()
	{
		ModelTypeOpImporter.createLiteral(target);
	}
	after() returning(ModelBool target) : call((ModelBool).new(boolean)) && isActive()
	{
		ModelTypeOpImporter.createLiteral(target);
	}
	after() returning(ModelString target) : call((ModelString).new(String)) && isActive()
	{
		ModelTypeOpImporter.createLiteral(target);
	}
	
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.add(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntAddOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.subtract(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntSubtractOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.multiply(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntMultiplyOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.divide(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntDivideOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.remainder(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntRemainderOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.abs())
	{
		return ModelTypeOpImporter.importModelIntAbsOp(target);
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.signum())
	{
		return ModelTypeOpImporter.importModelIntSignumOp(target);
	}
	@SuppressAjWarnings
	ModelInt around(ModelInt target): target(target) && isActive() && call(ModelInt ModelInt.negate())
	{
		return ModelTypeOpImporter.importModelIntNegateOp(target);
	}
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.and(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolAndOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.or(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolOrOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.xor(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolXorOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.eq(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolEqualOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.notEqu(ModelBool))
	{
		return ModelTypeOpImporter.importModelBoolNotEqOp(target, (ModelBool)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelBool target): target(target) && isActive() && call(ModelBool ModelBool.not())
	{
		return ModelTypeOpImporter.importModelBoolNotOp(target);
	}	
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isEqual(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsEqualOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isLessEqual(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsLessEqualOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isMoreEqual(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsMoreEqualOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isLess(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsLessOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
	@SuppressAjWarnings
	ModelBool around(ModelInt target): target(target) && isActive() && call(ModelBool ModelInt.isMore(ModelInt))
	{
		return ModelTypeOpImporter.importModelIntIsMoreOp(target, (ModelInt)(thisJoinPoint.getArgs()[0]));
	}
}
