package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.AssociationEnd;
import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.export.uml2.utils.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;

public privileged aspect InstructionImporterAspect extends AbstractImporterAspect {

	private pointcut creatingDummyInstance() : if(DummyInstanceCreator.isCreating());
	@SuppressAjWarnings
	after() returning(ModelInt target) : call((ModelInt).new(int)) && isActive() && !creatingDummyInstance()
	{
		InstructionImporter.createModelTypeLiteral(target);
	}
	@SuppressAjWarnings
	after() returning(ModelBool target) : call((ModelBool).new(boolean)) && isActive() && !creatingDummyInstance()
	{
		InstructionImporter.createModelTypeLiteral(target);
	}
	@SuppressAjWarnings
	after() returning(ModelString target) : call((ModelString).new(String)) && isActive() && !creatingDummyInstance()
	{
		InstructionImporter.createModelTypeLiteral(target);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	Object around(AssociationEnd target):target(target) && call(ModelClass selectOne()) && isActive()
	{
		return InstructionImporter.importAssociationEnd_SelectOne(target);
	}

	@SuppressAjWarnings
	after(ModelClass target): execution((ModelClass+).new(..)) && isActive() && target(target)
	{
		InstructionImporter.importInstanceCreation(target);
	}
		
	@SuppressAjWarnings
	Signal around(Transition target):call(Signal getSignal()) && target(target) &&  importing()
	{
		return InstructionImporter.initAndGetSignalInstanceOfTransition(target);
	}
		
	@SuppressAjWarnings
	Object around(ModelClass target): target(target) && call(* *(..))  && isActive() {
		try
		{
			return InstructionImporter.importMethodCall(target, thisJoinPoint.getSignature().getName(),thisJoinPoint.getArgs());
		}
		catch(ImportException exc)
		{
			//exc.printStackTrace();
			return null;
		}
	}
	
	@SuppressAjWarnings
	Object around(ExternalClass target) : target(target) && call(* (ExternalClass+).*(..)) && isActive() {
		return InstructionImporter.callExternal(target, thisJoinPoint.getSignature().getName(), thisJoinPoint.getArgs());
	}
	@SuppressAjWarnings
	Object around() : call(static * (ExternalClass+).*(..)) && isActive() {
		return InstructionImporter.callStaticExternal(thisJoinPoint.getSignature().getDeclaringType(), thisJoinPoint.getSignature().getName(), thisJoinPoint.getArgs());
	}

	
	@SuppressAjWarnings
	Object around(ModelClass target, Object newValue) : target(target) && set(* *) && args(newValue) && isActive() && !withincode((ModelClass+).new(..))
	{
		return InstructionImporter.importModelClassFieldSet(target,thisJoinPoint.getSignature().getName(),newValue);
	}

	@SuppressAjWarnings
	Object around(ModelClass target) : target(target) && get(* *) && isActive() {
		Signature signature=thisJoinPoint.getSignature();
		try {
			return InstructionImporter.importModelClassFieldGet(target,signature.getName(),signature.getDeclaringType().getDeclaredField(signature.getName()).getType());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	
	@SuppressAjWarnings
	Object around(ExternalClass target) : target(target) && get(* *) && isActive() {
		Signature signature=thisJoinPoint.getSignature();
		try {
			return InstructionImporter.importExternalClassFieldGet(target,signature.getName(),signature.getDeclaringType().getDeclaredField(signature.getName()).getType());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	
}
