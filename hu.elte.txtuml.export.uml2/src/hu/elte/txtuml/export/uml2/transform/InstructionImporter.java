package hu.elte.txtuml.export.uml2.transform;


import hu.elte.txtuml.api.Collection;
import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelIdentifiedElement;
import hu.elte.txtuml.api.ModelType;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.FieldValueAccessor;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

public class InstructionImporter extends AbstractInstructionImporter {

	static <T extends ModelClass> T selectOne(Collection<T> target) 
	{

		ParameterizedType genericSupClass=(ParameterizedType) target.getClass().getGenericSuperclass();
		String typeName=genericSupClass.getActualTypeArguments()[0].getTypeName();
		Type type=currentModel.getOwnedType(typeName);
		Class<?> resultClass=ElementFinder.findDeclaredClass(modelClass, typeName);

		@SuppressWarnings("unchecked")
		T result=(T) DummyInstanceCreator.createDummyInstance(resultClass);

		String resultName=result.getIdentifier();
		String startName=getObjectIdentifier(target);

		OpaqueAction selectOneAction=	(OpaqueAction)
				currentActivity.createOwnedNode("selectOne_"+startName,UMLPackage.Literals.OPAQUE_ACTION);
		String expression=startName;
		if(!hu.elte.txtuml.api.Association.One.class.isAssignableFrom(target.getClass()) &&
				!hu.elte.txtuml.api.Association.MaybeOne.class.isAssignableFrom(target.getClass())   )
		{
			expression+="->first()";
		}
		selectOneAction.getBodies().add(expression);

		createControlFlowBetweenNodes(lastNode, selectOneAction);

		OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);

		Variable variable=currentActivity.createVariable(resultName,type);
		AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);

		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);

		createObjectFlowBetweenNodes(outputPin,inputPin_AVVA);
		lastNode=setVarAction;

		return result;
	}

	private static <T extends ModelClass, AE extends hu.elte.txtuml.api.AssociationEnd<T> > String getAssociationEndOwner(AE target)
	{
		Method method=null;
		String ret=null;
		try {
			method=hu.elte.txtuml.api.AssociationEnd.class.getDeclaredMethod("getOwner");
			method.setAccessible(true);
			ret=getObjectIdentifier( (ModelIdentifiedElement) method.invoke(target) );
			method.setAccessible(false);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return ret;
	}
	
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
			!hu.elte.txtuml.api.Association.MaybeOne.class.isAssignableFrom(target.getClass())  
		   )
			expression+="->first()";
	
		selectOneAction.getBodies().add(expression);

		createControlFlowBetweenNodes(lastNode, selectOneAction);

		Association association=(Association) currentModel.getOwnedMember(assocClass.getSimpleName());
		Property memberEnd=ElementFinder.findAssociationMemberEnd(association,phrase);
		Type type=null;
		
		if(memberEnd!=null)
			type=memberEnd.getType();
	
		OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);

		Variable variable=currentActivity.createVariable(resultName,type);
		AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);

		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);

		createObjectFlowBetweenNodes(outputPin,inputPin_AVVA);
		lastNode=setVarAction;

		return result;
	}
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
			createControlFlowBetweenNodes(lastNode,createAction);

			//creating a variable for created instance, so we can reference it later
			Type type=ModelImporter.importType(createdInstance.getClass());
			Variable variable=currentActivity.createVariable(instanceName,type);

			//creating an Add Variable Value action
			AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+instanceName);
			//creating an input pin for Add Variable Action
			InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);

			//creating a Start Classifier Behavior Action for our newly created object
			StartClassifierBehaviorAction startClassifierBehaviorAction = (StartClassifierBehaviorAction) 
					currentActivity.createOwnedNode("startClassifierBehavior_"+instanceName,UMLPackage.Literals.START_CLASSIFIER_BEHAVIOR_ACTION);

			//creating an input pin for Start Classifier Behavior Action
			InputPin inputPin_startCBA=startClassifierBehaviorAction.createObject(startClassifierBehaviorAction.getName()+"_input",classifier);

			//creating a fork node and an object flow from Create Object Action's output pin to fork node
			ForkNode forkNode=createForkNode("fork_"+createAction.getName(),inputPin_AVVA,inputPin_startCBA);
			createObjectFlowBetweenNodes(outputPin,forkNode);

			//creating a join node for joining the two separate "threads"
			lastNode = createJoinNode(startClassifierBehaviorAction,setVarAction);
		}
	}

	private static void addParamsToCallAction
		(CallOperationAction callAction, ModelClass target, String methodName,Object[] args) throws ImportException
	{
		int i=0;
		for(Object param: args)
		{
			Type paramType=ModelImporter.importType(param.getClass());
			String paramName="arg"+i;
			if(!(param instanceof ModelIdentifiedElement))
			{
				throw new ImportException("Illegal argument (position "+(i+1)+ ") passed to method "+target+"."+methodName);
			}

			ValuePin argValuePin=(ValuePin)callAction.createArgument(paramName, paramType, UMLPackage.Literals.VALUE_PIN);
			addExpressionToValuePin(argValuePin,(ModelIdentifiedElement)param,paramType);
			++i;
		}
	}
	
	private static Object importMethodCallInOperationBody(ModelClass target, String methodName, Object... args) throws ImportException
	{
		Object returnObj=null;

		String targetName=getObjectIdentifier(target);

		CallOperationAction callAction=(CallOperationAction)
				currentActivity.createOwnedNode("call_"+targetName+"."+methodName, UMLPackage.Literals.CALL_OPERATION_ACTION);

		Type type=currentModel.getOwnedType(target.getClass().getSimpleName());

		org.eclipse.uml2.uml.Class targetClass	=	(org.eclipse.uml2.uml.Class)
				currentModel.getOwnedMember(target.getClass().getSimpleName());

		ValuePin callTarget=(ValuePin)callAction.createTarget(callAction.getName()+"_target",type,UMLPackage.Literals.VALUE_PIN);

		addOpaqueExpressionToValuePin(callTarget,targetName,type);

		callAction.setOperation(ElementFinder.findOperation(targetClass,methodName));
		addParamsToCallAction(callAction,target,methodName,args);

		createControlFlowBetweenNodes(lastNode,callAction);
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
	static Object importMethodCallInGuardBody(ModelClass target, String methodName, Object... args)
	{
		String targetExpression = getExpression(target);
		String expression=targetExpression+"."+methodName+"(";
		int argsProcessed=0;
		for(Object currArg : args)
		{
			String currArgExpr=getExpression((ModelIdentifiedElement)currArg);
			
			if(argsProcessed>0)
				expression+=",";

			expression+=currArgExpr;		
			
			++argsProcessed;
		}
		expression+=")";
		
		InstanceInformation returnValInfo=InstanceInformation.createCalculated(expression);
		
		ModelElement returnObj=null;
		try {
			Method method = ElementFinder.findMethod(target.getClass(),methodName);
			Class<?> returnType= method.getReturnType();
			returnObj=(ModelElement)DummyInstanceCreator.createDummyInstance(returnType);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		InstanceManager.createLocalInstancesMapEntry(returnObj,returnValInfo);
		
		
		return returnObj;
	}
	static Object importMethodCall(ModelClass target, String methodName, Object... args) throws ImportException
	{
		// this method is called at every method call where the target object is of any type that extends ModelClass 
		// parameters: the target object, the name of the called method and the given parameters

		Object returnObj=null;
		
		if(currentActivity!=null)
			returnObj = importMethodCallInOperationBody(target, methodName, args);
		else
			returnObj= importMethodCallInGuardBody(target, methodName, args);
	
		return returnObj;
		
	}


	static Object callExternal(ExternalClass target, String methodName, Object... args)
	{
		Method method=ElementFinder.findMethod(target.getClass(), methodName);
		Class<?> returnType=method.getReturnType();
		return DummyInstanceCreator.createDummyInstance(returnType);
		// TODO import calls into UML2 model
	}

	static Object callStaticExternal(Class<?> c, String methodName, Object... args)
	{
		Method method=ElementFinder.findMethod(c, methodName);
		Class<?> returnType=method.getReturnType();
		Object ret=DummyInstanceCreator.createDummyInstance(returnType);
		return ret;
	
		// TODO import calls into UML2 model
	}


	private static Object assignField(Object target, String fieldName, Class<?> newValueClass) 
	{
	
		Object fieldValue=FieldValueAccessor.getObjectFieldVal(target,fieldName);
		if(fieldValue != null)
		{
		
		}
		else
		{
			fieldValue=DummyInstanceCreator.createDummyInstance(newValueClass);
			FieldValueAccessor.setObjectFieldVal(target,fieldName,fieldValue);
		}
		
		return fieldValue;
	}

	static Object importModelClassFieldGet(ModelClass target, String fieldName, Class<?> fieldType)
	{
		return assignField(target,fieldName,fieldType);
	}
	
	static Object importExternalClassFieldGet(ExternalClass target, String fieldName, Class<?> fieldType)
	{	
		return assignField(target,fieldName,fieldType);
	}

	static Object importModelClassFieldSet(ModelClass target, String fieldName, Object newValue)  
	{
		try
		{
			Class<?> newValueClass = newValue.getClass();
			Object fieldObj=assignField(target,fieldName,newValueClass);
			if(currentActivity!=null)
			
			{
				Type newValType=ModelImporter.importType(newValue.getClass());
				setStructuralFeatureValue(target,fieldName,(ModelIdentifiedElement)newValue,newValType);
			}
			
			return fieldObj;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return null;
	}

	static <T> void createModelTypeLiteral(ModelType<T> inst)
	{
		@SuppressWarnings("unchecked")
		T val=(T)FieldValueAccessor.getObjectFieldVal(inst,"value");
		String expression=val.toString();
		
		InstanceInformation instInfo=InstanceInformation.createLiteral(expression);
		InstanceManager.createLocalInstancesMapEntry(inst,instInfo);
	}

	static Signal initAndGetSignalInstanceOfTransition(Transition target)
	{
		Signal signal = (Signal) FieldValueAccessor.getObjectFieldVal(target,"signal");
		
		if(signal == null)
		{
			signal=MethodImporter.createSignal(target.getClass());
			FieldValueAccessor.setObjectFieldVal(target,"signal",signal);
			
			String signalName=signal.getClass().getSimpleName();
			InstanceManager.createLocalInstancesMapEntry(signal,InstanceInformation.create(signalName));
			InstanceManager.createLocalFieldsRecursively(signal);
			
		}
		
		return signal;
	}
}
