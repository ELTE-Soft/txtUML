package txtuml.importer;



import java.lang.reflect.Method;
import java.util.LinkedList;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import txtuml.api.ExternalClass;
import txtuml.api.ModelClass;
import txtuml.api.Association.AssociationEnd;
import txtuml.api.ModelClass.Transition;
import txtuml.api.BlockBody;
import txtuml.api.Condition;
import txtuml.api.ModelInt;
import txtuml.api.Action;
import txtuml.api.ModelElement;
import txtuml.api.ParameterizedBlockBody;
import txtuml.api.Signal;
import txtuml.importer.InstructionImporter.LinkTypes;



public privileged aspect ImporterAspect extends AbstractImporterAspect {
	

	void around():call(void Action.For(ModelInt, ModelInt, ParameterizedBlockBody<ModelInt>) ) && isActive()
	{
		ModelInt from=(ModelInt)(thisJoinPoint.getArgs()[0]);
		ModelInt to=(ModelInt)(thisJoinPoint.getArgs()[1]);
		@SuppressWarnings("unchecked")
		ParameterizedBlockBody<ModelInt> body=(ParameterizedBlockBody<ModelInt>)(thisJoinPoint.getArgs()[2]);
		InstructionImporter.importForStatement(from, to,body);
	}
	
	void around():call(void Action.While(Condition,BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody body=(BlockBody)(thisJoinPoint.getArgs()[1]);
		InstructionImporter.importWhileStatement(cond,body);
	}
	
	void around():call(void Action.If(Condition,BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody thenBody=(BlockBody)(thisJoinPoint.getArgs()[1]);
		InstructionImporter.importIfStatement(cond,thenBody);
	}
	
	void around():call(void Action.If(Condition,BlockBody , BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody thenBody=(BlockBody)(thisJoinPoint.getArgs()[1]);
		BlockBody elseBody=(BlockBody)(thisJoinPoint.getArgs()[2]);
		InstructionImporter.importIfStatement(cond,thenBody,elseBody);
	}
	
	@SuppressWarnings("rawtypes")
	Object around(AssociationEnd target):target(target) && call(ModelClass selectOne()) && isActive()
	{
		return InstructionImporter.importAssociationEnd_SelectOne(target);
	}

	@SuppressAjWarnings
	Signal around(Transition target): call(Signal getSignal()) && isActive() && target(target)
	{
		return MethodImporter.createSignal(target.getClass());
	}

	@SuppressAjWarnings
	after(ModelClass target): execution((ModelClass+).new(..)) && isActive() && target(target)
	{
		InstructionImporter.importInstanceCreation(target);
	}
	
	@SuppressAjWarnings
	void around(ModelClass target): call(void startThread()) && importing()  && target(target)
	{
		//do nothing when importing and startThread is called on a ModelClass
	}

	
	void around(): call(void Action.link(..)) && isActive()
	{
		Class<?> leftEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		ModelClass leftObj=(ModelClass)(thisJoinPoint.getArgs()[1]);
		Class<?> rightEnd=(Class<?>)(thisJoinPoint.getArgs()[2]);
		ModelClass rightObj=(ModelClass)(thisJoinPoint.getArgs()[3]);
		InstructionImporter.importLinkAction(leftEnd,leftObj,rightEnd,rightObj,LinkTypes.CREATE_LINK_LITERAL);	
	}
    		
	void around(): call(void Action.unlink(..)) && isActive()
	{
		Class<?> leftEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		ModelClass leftObj=(ModelClass)(thisJoinPoint.getArgs()[1]);
		Class<?> rightEnd=(Class<?>)(thisJoinPoint.getArgs()[2]);
		ModelClass rightObj=(ModelClass)(thisJoinPoint.getArgs()[3]);
		InstructionImporter.importLinkAction(leftEnd,leftObj,rightEnd,rightObj,LinkTypes.DESTROY_LINK_LITERAL);	
	}
    	

	@SuppressAjWarnings
	void around(): call(void Action.send(ModelClass, Signal)) && isActive()
	{
	
		ModelClass receiverObj=(ModelClass)(thisJoinPoint.getArgs()[0]);
		Signal event=(Signal)(thisJoinPoint.getArgs()[1]);
		
		InstructionImporter.importSignalSend(receiverObj, event);	
			
	}
	
	@SuppressAjWarnings
	void around(): call(void Action.delete(ModelClass)) && isActive()
	{
	
		ModelClass obj=(ModelClass)(thisJoinPoint.getArgs()[0]);
		
		InstructionImporter.importObjectDeletion(obj);	
			
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
	Object around() : call(* (!ModelElement+).*(..)) && !call(* (java.lang..*).*(..)) && isActive() {
		System.err.println("Error: unpermitted method call: " + thisJoinPoint.getSignature().getDeclaringType().getName() + "." + thisJoinPoint.getSignature().getName());
		return proceed();
	}
	
	@SuppressAjWarnings
	Object around(ModelClass target, Object newValue) : target(target) && set(* *) && args(newValue) && isActive() && !withincode((ModelClass+).new(..))
	{
		return InstructionImporter.fieldSet(target,thisJoinPoint.getSignature().getName(),newValue);
	}
	@SuppressAjWarnings
	Object around(ModelClass target) : target(target) && get(* *) && isActive() {
		Signature signature=thisJoinPoint.getSignature();
		try {
			return InstructionImporter.fieldGet(target,signature.getName(),signature.getDeclaringType().getDeclaredField(signature.getName()).getType());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * This advice hides all the synthetic methods from the result of Class.getDeclaredMethods() calls.
	 * It is needed to hide the private methods generated by AspectJ.
	 */
	@SuppressAjWarnings
	Method[] around(Object c) : target(c) && call(Method[] Class.getDeclaredMethods()) && withinProject() {
		LinkedList<Method> methods = new LinkedList<>();
		for(Method m : proceed(c)) {
			if (!m.isSynthetic()) {
				methods.add(m);
			}
		}
		return methods.toArray(new Method[0]);
	}
	
	@SuppressAjWarnings
	void around(): 
		(
			call(void Action.log(String)) || 
			call(void Action.logError(String)) ||
			call(void Action.runtimeLog(String)) ||
			call(void Action.runtimeFormattedLog(String, Object...))
		)
		&& isActive()
	{
		//do nothing
	}
	
    	
	
}