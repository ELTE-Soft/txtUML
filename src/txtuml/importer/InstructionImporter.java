package txtuml.importer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

import txtuml.api.Collection;
import txtuml.api.ExternalClass;
import txtuml.api.ModelBool;
import txtuml.api.ModelClass;
import txtuml.api.ModelIdentifiedElement;
import txtuml.api.ModelInt;
import txtuml.api.ModelString;
import txtuml.api.ModelType;

class InstructionImporter extends AbstractInstructionImporter {

	/*private static void importObjectDeletion(ModelClass obj) {
    if(currentActivity != null) 
    {
       	DestroyObjectAction destroyAction=	(DestroyObjectAction) 
				currentActivity.createOwnedNode("delete_"+obj.getIdentifier(),UMLPackage.Literals.DESTROY_OBJECT_ACTION);

		String instanceName=getObjectIdentifier(obj);

		Type type= currentModel.getOwnedType(obj.getClass().getSimpleName());

		ValuePin target = (ValuePin) destroyAction.createTarget("target", type, UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(target,instanceName,type);

		createControlFlowBetweenNodes(lastNode,destroyAction);

		lastNode=destroyAction;


    }

}*/

	static <T extends ModelClass> T selectOne(Collection<T> target) 
	{

		ParameterizedType genericSupClass=(ParameterizedType) target.getClass().getGenericSuperclass();
		String typeName=genericSupClass.getActualTypeArguments()[0].getTypeName();
		Type type=currentModel.getOwnedType(typeName);
		Class<?> resultClass=null;

		for(Class<?> c: modelClass.getDeclaredClasses())
		{
			if(c.getName().equals(typeName))
			{
				resultClass=c;
				break;
			}
		}

		@SuppressWarnings("unchecked")
		T result=(T) createLocalInstance(resultClass);

		String resultName=result.getIdentifier();
		String startName=target.getIdentifier();

		OpaqueAction selectOneAction=	(OpaqueAction)
				currentActivity.createOwnedNode("selectOne_"+startName,UMLPackage.Literals.OPAQUE_ACTION);
		String expression=startName;
		if(!txtuml.api.Association.One.class.isAssignableFrom(target.getClass()) &&
				!txtuml.api.Association.MaybeOne.class.isAssignableFrom(target.getClass())   )
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

	private static <T extends ModelClass, AE extends txtuml.api.Association.AssociationEnd<T> > String getAssociationEndOwner(AE target)
	{
		Method method=null;
		String ret=null;
		try {
			method=txtuml.api.Association.AssociationEnd.class.getDeclaredMethod("getOwnerId");
			method.setAccessible(true);
			ret=getObjectIdentifier( (String) method.invoke(target) );
			method.setAccessible(false);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return ret;
	}
	static <T extends ModelClass, AE extends txtuml.api.Association.AssociationEnd<T> >
		T importAssociationEnd_SelectOne(AE target) 
	{

		ParameterizedType genericSupClass=(ParameterizedType) target.getClass().getGenericSuperclass();
		String typeName=genericSupClass.getActualTypeArguments()[0].getTypeName();

		Class<?> assocClass=target.getClass().getDeclaringClass();
		Class<?> resultClass=null;

		for(Class<?> c: modelClass.getDeclaredClasses())
		{
			if(c.getName().equals(typeName))
			{
				resultClass=c;
				break;
			}
		}

		@SuppressWarnings("unchecked")
		T result=(T) createLocalInstance(resultClass);

		String phrase=target.getClass().getSimpleName();
		String resultName=result.getIdentifier();


		String startName=getAssociationEndOwner(target);		

		OpaqueAction selectOneAction=	(OpaqueAction)
				currentActivity.createOwnedNode("selectOne_"+startName+"."+phrase,UMLPackage.Literals.OPAQUE_ACTION);
		String expression=startName+"."+phrase;
		if(!txtuml.api.Association.One.class.isAssignableFrom(target.getClass()) &&
				!txtuml.api.Association.MaybeOne.class.isAssignableFrom(target.getClass())   )
		{
			expression+="->first()";
		}
		selectOneAction.getBodies().add(expression);

		createControlFlowBetweenNodes(lastNode, selectOneAction);

		Association association=(Association) currentModel.getOwnedMember(assocClass.getSimpleName());
		Type type=null;
		for(Property p:association.getMemberEnds())
		{
			if(p.getName().equals(phrase))
			{
				type=p.getType();
				break;
			}
		}
		OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);

		Variable variable=currentActivity.createVariable(resultName,type);
		AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);

		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);

		createObjectFlowBetweenNodes(outputPin,inputPin_AVVA);
		lastNode=setVarAction;

		return result;
	}
	static void importInstanceCreation(ModelClass created)
	{

		if(currentActivity != null && !localInstanceToBeCreated)
		{
			String instanceName=created.getIdentifier();
			//creating Create Object Action
			CreateObjectAction createAction=(CreateObjectAction)
					currentActivity.createOwnedNode("create_"+instanceName,UMLPackage.Literals.CREATE_OBJECT_ACTION);
			Classifier classifier=(Classifier) currentModel.getOwnedMember(created.getClass().getSimpleName());
			createAction.setClassifier(classifier);

			//creating output pin for Create Object Action
			OutputPin outputPin=createAction.createResult(createAction.getName()+"_output",classifier);

			//creating a control flow from the previous node to the newly created Create Object Action
			createControlFlowBetweenNodes(lastNode,createAction);

			//creating a variable for created instance, so we can reference it later
			Type type=ModelImporter.importType(created.getClass());
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
	
	private static Object assocCall(ModelClass target,Object... args)
	{
		Object returnVal=null;
		Method assocMethod=findMethod(ModelClass.class,"assoc");

		
		try {
			returnVal=assocMethod.invoke(target,args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return returnVal;
	}
	
	private static Object importMethodCallInOperationBody(ModelClass target, String methodName, Object... args) throws ImportException
	{
		Object returnObj=null;
		
		if(methodName.equals("assoc"))
		{
			returnObj=assocCall(target,args);
		}
		else
		{
			String targetName=getObjectIdentifier(target);

			CallOperationAction callAction=(CallOperationAction)
					currentActivity.createOwnedNode("call_"+targetName+"."+methodName, UMLPackage.Literals.CALL_OPERATION_ACTION);

			Type type=currentModel.getOwnedType(target.getClass().getSimpleName());

			org.eclipse.uml2.uml.Class targetClass	=	(org.eclipse.uml2.uml.Class)
					currentModel.getOwnedMember(target.getClass().getSimpleName());

			ValuePin callTarget=(ValuePin)callAction.createTarget(callAction.getName()+"_target",type,UMLPackage.Literals.VALUE_PIN);

			addOpaqueExpressionToValuePin(callTarget,targetName,type);

			callAction.setOperation(AbstractImporter.findOperation(targetClass,methodName));
			addParamsToCallAction(callAction,target,methodName,args);

			createControlFlowBetweenNodes(lastNode,callAction);
			lastNode=callAction;

			try {
				Method method = findMethod(target.getClass(),methodName);
				Class<?> returnType=method.getReturnType();
				returnObj=createLocalInstance(returnType);
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}

		}
		
		return returnObj;
	}
	static ModelBool importMethodCallInGuardBody(ModelClass target, String methodName, Object... args)
	{
		ModelBool returnVal=new ModelBool();
		String expression=methodName+"(";
		int argsProcessed=0;
		for(Object currArg : args)
		{
			String currArgExpr=getExpression((ModelIdentifiedElement)currArg);
			if(argsProcessed>0)
			{
				expression+=",";
			}
			
			expression+=currArgExpr;		
			
			++argsProcessed;
		}
		expression+=")";
		
		boolean literal=false;
		boolean calculated=true;
			
		ModelTypeInformation returnValInfo=new ModelTypeInformation(expression,literal,calculated);
		
		modelTypeInstancesInfo.put(returnVal,returnValInfo);
		
		return returnVal;
	}
	static Object importMethodCall(ModelClass target, String methodName, Object... args) throws ImportException
	{
		// this method is called at every method call where the target object is of any type that extends ModelClass 
		// parameters: the target object, the name of the called method and the given parameters

		Object returnObj=null;
		if(currentActivity!=null)
		{
			returnObj = importMethodCallInOperationBody(target, methodName, args);
		}
		else
		{
			returnObj= importMethodCallInGuardBody(target, methodName, args);
		}
		return returnObj;
		
	}



	static Object callExternal(ExternalClass target, String methodName, Object... args)
	{
		return null;
		// TODO not implemented; should return an instance of the actual return type of the called method
		// it can be get through its Method class
		// the imported model will get this returned object as the result of the method call
	}

	static Object callStaticExternal(Class<?> c, String methodName, Object... args)
	{
		return null;
		// TODO not implemented; should return an instance of the actual return type of the called method
		// it can be get through its Method class
		// the imported model will get this returned object as the result of the method call

		// c will actually always be Class<? extends ExternalClass>
		// so this method informs the importer about a static method call on an ExternalClass class
	}





	static Object initField(ModelClass target, String fieldName, Object newValue) 
			throws IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, NoSuchFieldException, 
			SecurityException, NoSuchMethodException
	{
		Field field=target.getClass().getDeclaredField(fieldName);

		field.setAccessible(true);
		Object fieldObj=field.get(target);

		if(newValue instanceof ModelIdentifiedElement)
		{


			Method method=ModelType.class.getDeclaredMethod("getValue");
			method.setAccessible(true);

			if(newValue instanceof ModelInt)
			{
				int val=(int) method.invoke(newValue);
				fieldObj=new ModelInt(val);	
			}
			else if(newValue instanceof ModelBool)
			{
				boolean val=(boolean) method.invoke(newValue);
				fieldObj=new ModelBool(val);	
			}
			else if(newValue instanceof ModelString)
			{

				String val=(String) method.invoke(newValue);
				fieldObj=new ModelString(val);
			}
			else if(newValue instanceof ModelClass)
			{
				fieldObj=createLocalInstance(newValue.getClass());
			}

			method.setAccessible(false);
		}
		field.set(target, fieldObj);
		field.setAccessible(false);

		return fieldObj;
	}
	
	static Object fieldGet(ModelClass target, String fieldName, Class<?> fieldType)
	{
		Object val=null;
		if(fieldType==ModelInt.class)
		{
			val=new ModelInt();
		}
		else if(fieldType==ModelBool.class)
		{
			val=new ModelBool();
		}
		else if(fieldType==ModelString.class)
		{
			val=new ModelString();
		}
		else if(isModelClass(fieldType))
		{
			val=createLocalInstance(fieldType);
		}
		try
		{
			return initField(target,fieldName,val);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return val;

	}

	static Object fieldSet(ModelClass target, String fieldName, Object newValue)  
	{
		try{

			Object fieldObj=initField(target,fieldName,newValue);
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




}
