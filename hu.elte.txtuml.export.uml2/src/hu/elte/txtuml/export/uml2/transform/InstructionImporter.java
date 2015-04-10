package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelType;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.StateMachine;
import hu.elte.txtuml.api.Trigger;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.FieldValueAccessor;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;
import hu.elte.txtuml.utils.InstanceCreator;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

/**
 * This class is responsible for importing instructions that are not actions (Action.* calls)
 * nor ModelType operations inside method bodies.
 * 
 * @author Ádám Ancsin
 *
 */
public class InstructionImporter extends AbstractMethodImporter
{
	/**
	 * Imports a select one instruction of an association end in a method body.
	 * @param target The dummy instance of the target association end.
	 * @return The dummy instance of the result.
	 *
	 * @author Ádám Ancsin
	 */
	static <T extends ModelClass, AE extends hu.elte.txtuml.api.AssociationEnd<T> >
		T importAssociationEnd_SelectOne(AE target) 
	{

		ParameterizedType genericSupClass=(ParameterizedType) target.getClass().getGenericSuperclass();
		String typeName=genericSupClass.getActualTypeArguments()[0].getTypeName();

		Class<?> assocClass=target.getClass().getDeclaringClass();
		Class<?> resultClass=ElementFinder.findDeclaredClass(modelClass, typeName);

		@SuppressWarnings("unchecked")
		T result=(T) DummyInstanceCreator.createDummyInstance(resultClass);

		String phrase=target.getClass().getSimpleName();
		String resultName=result.getIdentifier();

		String startName=getAssociationEndOwner(target);		

		OpaqueAction selectOneAction=	(OpaqueAction)
				currentActivity.createOwnedNode("selectOne_"+startName+"."+phrase,UMLPackage.Literals.OPAQUE_ACTION);
		String expression=startName+"."+phrase;
		
		if (!hu.elte.txtuml.api.Association.One.class.isAssignableFrom(target.getClass()) &&
			!hu.elte.txtuml.api.Association.MaybeOne.class.isAssignableFrom(target.getClass())  )
		{
			expression+="->first()";
		}
		
		selectOneAction.getBodies().add(expression);

		createControlFlowBetweenActivityNodes(lastNode, selectOneAction);

		Association association=(Association) currentModel.getOwnedMember(assocClass.getSimpleName());
		Property memberEnd=ElementFinder.findAssociationMemberEnd(association,phrase);
		Type type=null;
		
		if(memberEnd!=null)
			type=memberEnd.getType();
	
		OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);

		Variable variable=currentActivity.createVariable(resultName,type);
		AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);

		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);

		createObjectFlowBetweenActivityNodes(outputPin,inputPin_AVVA);
		lastNode=setVarAction;

		if(result!=null)
		{
			InstanceManager.createLocalInstancesMapEntry(result, InstanceInformation.create(result.getIdentifier()));
			InstanceManager.createLocalFieldsRecursively(result);
		}
		return result;
	}
	
	/**
	 * Imports the creation of a model class instance in a method body.
	 * @param createdInstance The created dummy instance.
	 *
	 * @author Ádám Ancsin
	 */
	static void importInstanceCreation(ModelClass createdInstance)
	{
		if(currentActivity != null && !DummyInstanceCreator.isCreating())
		{
			String instanceName=createdInstance.getIdentifier();
			InstanceManager.createLocalInstancesMapEntry(createdInstance, InstanceInformation.create(instanceName));
			InstanceManager.createLocalFieldsRecursively(createdInstance);
			
			//creating Create Object Action
			CreateObjectAction createAction=(CreateObjectAction)
					currentActivity.createOwnedNode("create_"+instanceName,UMLPackage.Literals.CREATE_OBJECT_ACTION);
			Classifier classifier=(Classifier) currentModel.getOwnedMember(createdInstance.getClass().getSimpleName());
			createAction.setClassifier(classifier);

			//creating output pin for Create Object Action
			OutputPin outputPin=createAction.createResult(createAction.getName()+"_output",classifier);

			//creating a control flow from the previous node to the newly created Create Object Action
			createControlFlowBetweenActivityNodes(lastNode,createAction);

			//creating a variable for created instance, so it can be referenced later
			Type type=ModelImporter.importType(createdInstance.getClass());
			Variable variable=currentActivity.createVariable(instanceName,type);

			//creating an Add Variable Value action
			AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+instanceName);
			//creating an input pin for Add Variable Action
			InputPin inputPin=setVarAction.createValue(setVarAction.getName()+"_input",type);

			createObjectFlowBetweenActivityNodes(outputPin,inputPin);
			lastNode = setVarAction;
		}
	}

	
	/**
	 * Imports a method call in a method body.
	 * @param target The target dummy instance.
	 * @param methodName The name of the method.
	 * @param args The dummy instances of the current arguments.
	 * @return The dummy instance of the return value.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	static Object importMethodCall(ModelClass target, String methodName, Object... args) throws ImportException
	{
		// this method is called at every method call where the target object is of any type that extends ModelClass 
		// parameters: the target object, the name of the called method and the given parameters

		Object returnObj=null;
		
		if(currentActivity!=null)
			returnObj = importMethodCallInActivity(target, methodName, args);
		else
			returnObj= importMethodCallInGuardBody(target, methodName, args);
	
		return returnObj;
		
	}

	/**
	 * Imports a method call of an external class in a method body.
	 * @param target The target dummy instance of the external class.
	 * @param methodName The name of the method.
	 * @param args The dummy instances of the current arguments. 
	 * @return The dummy instance of the return value.
	 *
	 * @author Ádám Ancsin
	 */
	static Object importExternalMethodCall(ExternalClass target, String methodName, Object... args)
	{
		Method method=ElementFinder.findMethod(target.getClass(), methodName);
		Class<?> returnType=method.getReturnType();
		return DummyInstanceCreator.createDummyInstance(returnType);
		// TODO import calls into UML2 model
	}

	/**
	 * Imports a static method call of an external class in a method body.
	 * @param target The target external class.
	 * @param methodName The name of the method.
	 * @param args The dummy instances of the current arguments. 
	 * @return The dummy instance of the return value.
	 *
	 * @author Ádám Ancsin
	 */
	static Object importExternalStaticMethodCall(Class<?> targetClass, String methodName, Object... args)
	{
		Method method=ElementFinder.findMethod(targetClass, methodName);
		Class<?> returnType=method.getReturnType();
		Object ret=DummyInstanceCreator.createDummyInstance(returnType);
		return ret;
	
		// TODO import calls into UML2 model
	}

	/**
	 * Imports a field get of a model class instance.
	 * @param target The dummy instance of the target model class.
	 * @param fieldName The name of the field.
	 * @param fieldType The type of the field.
	 * @return The dummy instance of the field.
	 *
	 * @author Ádám Ancsin
	 */
	static Object importModelClassFieldGet(ModelClass target, String fieldName, Class<?> fieldType)
	{
		return initAndGetField(target,fieldName,fieldType);
	}
	
	/**
	 * Imports a field get of an external class instance.
	 * @param target The dummy instance of the target external class.
	 * @param fieldName The name of the field.
	 * @param fieldType The type of the field.
	 * @return The dummy instance of the field.
	 *
	 * @author Ádám Ancsin
	 */
	static Object importExternalClassFieldGet(ExternalClass target, String fieldName, Class<?> fieldType)
	{	
		return initAndGetField(target,fieldName,fieldType);
	}

	/**
	 * Imports a field set of a model class instance.
	 * @param target The dummy instance of the target model class.
	 * @param fieldName The name of the field.
	 * @param newValue The dummy instance of the new value.
	 * @return The dummy instance of the field.
	 *
	 * @author Ádám Ancsin
	 */
	static Object importModelClassFieldSet(ModelClass target, String fieldName, Object newValue)  
	{
		try
		{
			Class<?> newValueClass = newValue.getClass();
			Object fieldObj=initAndGetField(target,fieldName,newValueClass);
			
			if(currentActivity!=null)			
			{
				Type newValType=ModelImporter.importType(newValue.getClass());
				setStructuralFeatureValue(target,fieldName,(ModelElement)newValue,newValType);
			}
			
			return fieldObj;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return null;
	}

	/**
	 * Imports the creation of a ModelType literal.
	 * @param inst The dummy instance representing the literal.
	 *
	 * @author Ádám Ancsin
	 */
	static <T> void importModelTypeLiteralCreation(ModelType<T> inst)
	{
		@SuppressWarnings("unchecked")
		T val=(T)FieldValueAccessor.getObjectFieldVal(inst,"value");
		String expression=val.toString();
		
		InstanceInformation instInfo=InstanceInformation.createLiteral(expression);
		InstanceManager.createLocalInstancesMapEntry(inst,instInfo);
	}

	/**
	 * Initializes (creates a dummy instance and assigns it) the trigger signal of the given transition, if not
	 * yet initialized. Gets the dummy instance of the trigger signal.
	 * @param target The dummy instance of the target transition.
	 * @return The dummy instance of the trigger signal of the transition.
	 *
	 * @author Ádám Ancsin
	 */
	static Signal initAndGetSignalInstanceOfTransition(Transition target)
	{
		Signal signal = (Signal) FieldValueAccessor.getObjectFieldVal(target,"signal");
		
		if(signal == null)
		{
			signal=createSignal(target.getClass());
			
			if(signal != null)
			{
				FieldValueAccessor.setObjectFieldVal(target,"signal",signal);
				
				String signalName=signal.getClass().getSimpleName();
				InstanceManager.createLocalInstancesMapEntry(signal,InstanceInformation.create(signalName));
				InstanceManager.createLocalFieldsRecursively(signal);
			}
		}
		return signal;
	}
	
	/**
	 * Creates a dummy instance of the trigger signal (if there's any) of the given transition.
	 * @param transitionClass The class of the transition.
	 * @return The created dummy instance. (null if there's no trigger)
	 *
	 * @author Ádám Ancsin
	 */
	private static hu.elte.txtuml.api.Signal createSignal(Class<? extends StateMachine.Transition> transitionClass) {
		Trigger triggerAnnotation = transitionClass.getAnnotation(Trigger.class);
		if (triggerAnnotation != null) {
			return InstanceCreator.createInstance(triggerAnnotation.value());
		}
		return null;
	}

	/**
	 * Adds the parameters of the method call to a CallOperationAction.
	 * @param callAction The call operation action.
	 * @param target The dummy instance of the target model class.
	 * @param methodName The name of the called method.
	 * @param args The dummy instances of the current arguments.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void addParamsToCallAction
		(CallOperationAction callAction, ModelClass target, String methodName,Object[] args) throws ImportException
	{
		int i=0;
		for(Object param: args)
		{
			Type paramType=ModelImporter.importType(param.getClass());
			String paramName="arg"+i;
			if(!(param instanceof ModelElement))
				throw new ImportException("Illegal argument (position "+(i+1)+ ") passed to method "+target+"."+methodName);

			ValuePin argValuePin=(ValuePin)callAction.createArgument(paramName, paramType, UMLPackage.Literals.VALUE_PIN);
			createAndAddValueExpressionToValuePin(argValuePin,(ModelElement)param,paramType);
			++i;
		}
	}

	/**
	 * Imports a method call in a method body with an activity. (member functions of model classes or entry/exit/effect actions)
	 * @param target The dummy instance of the target model class.
	 * @param methodName The name of the called method.
	 * @param args The dummy instances of the current arguments.
	 * @return The dummy instance of the return value.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static Object importMethodCallInActivity
		(ModelClass target, String methodName, Object... args) throws ImportException
	{
		Object returnObj=null;

		String targetName=getObjectIdentifier(target);

		CallOperationAction callAction=(CallOperationAction)
				currentActivity.createOwnedNode("call_"+targetName+"."+methodName, UMLPackage.Literals.CALL_OPERATION_ACTION);

		Type type=currentModel.getOwnedType(target.getClass().getSimpleName());

		org.eclipse.uml2.uml.Class targetClass	=	(org.eclipse.uml2.uml.Class)
				currentModel.getOwnedMember(target.getClass().getSimpleName());

		ValuePin callTarget=(ValuePin)callAction.createTarget(callAction.getName()+"_target",type,UMLPackage.Literals.VALUE_PIN);

		createAndAddOpaqueExpressionToValuePin(callTarget,targetName,type);

		callAction.setOperation(ElementFinder.findOperation(targetClass,methodName));
		addParamsToCallAction(callAction,target,methodName,args);

		createControlFlowBetweenActivityNodes(lastNode,callAction);
		lastNode=callAction;

		try {
			Method method = ElementFinder.findMethod(target.getClass(),methodName);
			Class<?> returnType=method.getReturnType();
			returnObj=DummyInstanceCreator.createDummyInstance(returnType);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}

		return returnObj;
	}

	/**
	 * Creates a method call expression. (e.g. "self.doThis(arg0,arg1)")
	 * @param target The dummy instance of the target model class.
	 * @param methodName The name of the called method.
	 * @param args The dummy instances of the current arguments.
	 * @return The created expression.
	 *
	 * @author Ádám Ancsin
	 */
	private static String createMethodCallExpression(ModelClass target, String methodName, Object... args)
	{
		String targetExpression = getExpression(target);
		StringBuilder expression=new StringBuilder(targetExpression);

		expression.append(".");
		expression.append(methodName);
		expression.append("(");

		int argsProcessed=0;
		for(Object currArg : args)
		{
			String currArgExpr=getExpression((ModelElement)currArg);

			if(argsProcessed>0)
				expression.append(",");

			expression.append(currArgExpr);		

			++argsProcessed;
		}
		expression.append(")");

		return expression.toString();
	}

	/**
	 * Imports a method call in a guard body.
	 * @param target The dummy instance of the target model class.
	 * @param methodName The name of the called method.
	 * @param args The dummy instances of the current arguments.
	 * @return The dummy instance of the return value.
	 *
	 * @author Ádám Ancsin
	 */
	private static Object importMethodCallInGuardBody(ModelClass target, String methodName, Object... args)
	{
		String expression = createMethodCallExpression(target,methodName,args);

		InstanceInformation returnValInfo=InstanceInformation.createCalculated(expression);

		ModelElement returnObj=null;
		try 
		{
			Method method = ElementFinder.findMethod(target.getClass(),methodName);
			Class<?> returnType= method.getReturnType();
			returnObj=(ModelElement)DummyInstanceCreator.createDummyInstance(returnType);
		} 
		catch (SecurityException e1) 
		{
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}

		InstanceManager.createLocalInstancesMapEntry(returnObj,returnValInfo);

		return returnObj;
	}
	
	/**
	 * Gets the identifier of the owner of the given association end.
	 * @param target The target association end.
	 * @return The identifier of the owner.
	 *
	 * @author Ádám Ancsin
	 */
	private static <T extends ModelClass, AE extends hu.elte.txtuml.api.AssociationEnd<T> >
		String getAssociationEndOwner(AE target)
	{
		Method method=null;
		String ret=null;
		try {
			method=hu.elte.txtuml.api.AssociationEnd.class.getDeclaredMethod("getOwner");
			method.setAccessible(true);
			ret=getObjectIdentifier( (ModelElement) method.invoke(target) );
			method.setAccessible(false);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * If not yet initialized (value is null), initializes the field with the given field name of the
	 * given target object with a dummy instance of the specified value type.
	 * @param target The dummy instance of the target.
	 * @param fieldName The name of the field.
	 * @param valueType The value type of the field.
	 * @return The dummy instance of the field.
	 *
	 * @author Ádám Ancsin
	 */
	private static Object initAndGetField(Object target, String fieldName, Class<?> valueType) 
	{
		Object fieldValue=FieldValueAccessor.getObjectFieldVal(target,fieldName);
		
		if(fieldValue == null)
		{
			fieldValue=DummyInstanceCreator.createDummyInstance(valueType);
			FieldValueAccessor.setObjectFieldVal(target,fieldName,fieldValue);
		}
		
		return fieldValue;
	}

}
