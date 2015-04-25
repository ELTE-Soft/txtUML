package hu.elte.txtuml.export.uml2.transform.aspects;

import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.InstructionImporter;

import java.lang.Class;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;


/**
 * This aspect contains advices (and some pointcuts) for importing instructions that are not actions (Action.* calls)
 * nor ModelType operations.
 * 
 * @author Adam Ancsin
 */
public privileged aspect InstructionImporterAspect extends AbstractImporterAspect {

	/**
	 * This pointcut indicates that dummy instance creation is in progress. 
	 *
	 * @author Adam Ancsin
	 */
	private pointcut creatingDummyInstance() : if(DummyInstanceCreator.isCreating());
	
	/**
	 * This pointcut indicates that "assoc" is being called.
	 * @author Adam Ancsin
	 */
	private pointcut callingAssocMethod(): if(thisJoinPoint.getSignature().getName().equals("assoc"));
	
	/**
	 * This advice imports ModelInt literal creation.
	 * Runs after the one-parameter constructor (if called during model import from a txtUML method body)
	 * of ModelInt returns with the created instance.
	 * @param created The created instance. 
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	after() returning(ModelInt created) : call((ModelInt).new(int)) && isActive() && !creatingDummyInstance()
	{
		InstructionImporter.importModelTypeLiteralCreation(created);
	}
	
	/**
	 * This advice imports ModelBool literal creation.
	 * Runs after the one-parameter constructor (if called during model import from a txtUML method body)
	 * of ModelBool returns with the created instance.
	 * @param created The created instance. 
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	after() returning(ModelBool created) : call((ModelBool).new(boolean)) && isActive() && !creatingDummyInstance()
	{
		InstructionImporter.importModelTypeLiteralCreation(created);
	}
	
	/**
	 * This advice imports ModelString literal creation.
	 * Runs after the one-parameter constructor (if called during model import from a txtUML method body)
	 * of ModelString returns with the created instance.
	 * @param created The created instance. 
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	after() returning(ModelString created) : call((ModelString).new(String)) && isActive() && !creatingDummyInstance()
	{
		InstructionImporter.importModelTypeLiteralCreation(created);
	}
	
	/**
	 * This advice imports a ModelClass instance creation in a txtUML method body.
	 * Runs after the constructor of a subclass of ModelClass is executed (called from a txtUML method body)
	 * during model import.
	 * @param created The created instance.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	after(ModelClass created): execution((ModelClass+).new(..)) && isActive() && target(created)
	{
		InstructionImporter.importInstanceCreation(created);
	}
	
	/**
	 * This advice provides a proper return value (a dummy instance) for a getSignal call of a
	 * transition during model import.
	 * 
	 * @param target The target transition of the call.
	 * @param signalClass The class of the signal.
	 * @return The dummy instance of the trigger signal.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("unchecked")
	@SuppressAjWarnings
	Signal around(Transition target):
		call(Signal getSignal(..)) && 
		target(target) &&
		importing()
	{
		Class<? extends Signal> signalClass = (Class<? extends Signal>) thisJoinPoint.getArgs()[0];
		return InstructionImporter.initAndGetSignalInstanceOfTransition(target, signalClass);
	}
		
	/**
	 * This advice imports a ModelClass member function call in a txtUML method body.
	 * Runs if the method is called in a txtUML method body during model import.
	 * @param target The target ModelClass (dummy) instance.
	 * @return The dummy instance of the return value of the method.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around(ModelClass target): target(target) && call(* *(..))  && isActive() && !callingAssocMethod()
	{
		return InstructionImporter.importMethodCall(target, thisJoinPoint.getSignature().getName(),thisJoinPoint.getArgs());
	}
	
	/**
	 * This advice an "assoc" call on the specified ModelClass target instance in a txtUML method body
	 * during model import.
	 * @param target The target instance of the call
	 * @return The result of the call (an AssociationEnd).
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around(ModelClass target): target(target) && isActive() && call(* assoc(..))
	{
		Class<?> otherEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		return InstructionImporter.importAssocCall(target,otherEnd);
	}
	
	/**
	 * This advice imports an external method call (member function of an ExternalClass) in a txtUML method body.
	 * Runs if the method is called in a txtUML method body during model import.
	 * @param target The target ExternalClass (dummy) instance.
	 * @return The dummy instance of the return value of the called method.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around(ExternalClass target) : target(target) && call(* (ExternalClass+).*(..)) && isActive() {
		return InstructionImporter.importExternalMethodCall(
				target, 
				thisJoinPoint.getSignature().getName(), 
				thisJoinPoint.getArgs()
			);
	}
	
	/**
	 * This advice imports a static external method call (static member function of an ExternalClass) in a txtUML method body.
	 * Runs if the method is called in a txtUML method body during model import.
	 *
	 * @return The dummy instance of the return value of the called method.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around() : call(static * (ExternalClass+).*(..)) && isActive() {
		return InstructionImporter.importExternalStaticMethodCall(
				thisJoinPoint.getSignature().getDeclaringType(),
				thisJoinPoint.getSignature().getName(),
				thisJoinPoint.getArgs()
			);
	}

	/**
	 * This advice imports a field set of a ModelClass instance.
	 * Runs if a field of a ModelClass instance is set in a txtUML method body during model import.
	 * 
	 * @param target The ModelClass (dummy) instance
	 * @param newValue The new value to be assigned to the field.
	 * @return The dummy instance of the field.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around(ModelClass target, Object newValue) : target(target) &&
														set(* *) && 
														args(newValue) && 
														isActive() &&
														!withincode((ModelClass+).new(..))
	{
		return InstructionImporter.importModelClassFieldSet(target,thisJoinPoint.getSignature().getName(),newValue);
	}

	/**
	 * This advice imports a field get of a ModelClass instance.
	 * Runs if a field of a ModelClass instance is accessed in a txtUML method body during model import.
	 * 
	 * @param target The ModelClass (dummy) instance
	 * @return The dummy instance of the field.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around(ModelClass target) : target(target) && get(* *) && isActive() {
		Signature signature=thisJoinPoint.getSignature();
		try
		{
			return InstructionImporter.importModelClassFieldGet(target,signature.getName(),signature.getDeclaringType().getDeclaredField(signature.getName()).getType());
		} 
		catch (NoSuchFieldException e) 
		{

		}
		return null;
	}
	
	/**
	 * This advice imports a field get of an ExternaClass instance.
	 * Runs if a field of an ExternalClass instance is accessed in a txtUML method body during model import.
	 * 
	 * @param target The ExternalClass (dummy) instance
	 * @return The dummy instance of the field.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around(ExternalClass target) : target(target) && get(* *) && isActive() {
		Signature signature=thisJoinPoint.getSignature();
		try
		{
			return InstructionImporter.importExternalClassFieldGet(target,signature.getName(),signature.getDeclaringType().getDeclaredField(signature.getName()).getType());
		} 
		catch (NoSuchFieldException e)
		{
		}
		return null;
	}
}
