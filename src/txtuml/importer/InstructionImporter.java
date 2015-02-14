package txtuml.importer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

import txtuml.api.BlockBody;
import txtuml.api.Collection;
import txtuml.api.Condition;
import txtuml.api.ExternalClass;
import txtuml.api.ModelBool;
import txtuml.api.ModelClass;
import txtuml.api.ModelIdentifiedElement;
import txtuml.api.ModelInt;
import txtuml.api.ModelString;
import txtuml.api.ModelType;
//FIXME there is no One annotation
//import txtuml.api.One;
import txtuml.api.ParameterizedBlockBody;
import txtuml.export.uml2tocpp.Util.Pair;

@SuppressWarnings("unused")
public class InstructionImporter extends AbstractMethodImporter {

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
	
	
	enum LinkTypes
	{
		CREATE_LINK_LITERAL,
		DESTROY_LINK_LITERAL
	};

	
	private static void delete(ModelClass obj) {
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
		
	}

	private static void addEndToLinkAction(LinkAction linkAction, Association association, 
												 String phrase, String instName,ModelClass obj,int endNum)
	{
		Type endType=ModelImporter.importType(obj.getClass());
		
		ValuePin end_valuePin=(ValuePin) 
				linkAction.createInputValue(linkAction.getName()+"_end"+endNum+"input",endType,UMLPackage.Literals.VALUE_PIN);
		
		addOpaqueExpressionToValuePin(end_valuePin,instName,endType);
		
		LinkEndData end=linkAction.createEndData();
		Property endProp=association.getOwnedEnd(phrase,endType);
		end.setEnd(endProp);
		
	}

	private static void importLinkAction(Class<?> leftEnd, ModelClass leftObj,
			Class<?> rightEnd, ModelClass rightObj, LinkTypes linkType)
	{
		if(currentActivity != null) 
        {
        	String leftName=getObjectIdentifier(leftObj);
        	String rightName=getObjectIdentifier(rightObj);
        	String leftPhrase=leftEnd.getSimpleName();
        	String rightPhrase=rightEnd.getSimpleName();

        	String assocName=leftEnd.getDeclaringClass().getSimpleName();
        	
        	Association association=(Association)currentModel.getOwnedMember(assocName);
        	
        	String linkActionName=null;
        	LinkAction linkAction=null;
        	
        	switch(linkType)
        	{
        		case CREATE_LINK_LITERAL:
        			linkActionName="link_"+leftName+"_and_"+rightName;
        			linkAction	=	(CreateLinkAction) 
						currentActivity.createOwnedNode(linkActionName, UMLPackage.Literals.CREATE_LINK_ACTION);
        		break;
        		case DESTROY_LINK_LITERAL:
        			linkActionName="unlink_"+leftName+"_and_"+rightName;
        			linkAction	=	(DestroyLinkAction) 
						currentActivity.createOwnedNode(linkActionName, UMLPackage.Literals.DESTROY_LINK_ACTION);
        		break;
        	}

			addEndToLinkAction(linkAction,association,leftPhrase,leftName,leftObj,1);
			addEndToLinkAction(linkAction,association,rightPhrase,rightName,rightObj,2);
			createControlFlowBetweenNodes(lastNode,linkAction);
			lastNode=linkAction;
			
        }
	}
	
	private static ModelClass getOtherAssociationEnd(Class<? extends txtuml.api.Association> assocClass, String phrase)
	{
		Field[] fields = assocClass.getDeclaredFields();
        ModelClass result;
		for(Field field : fields) {
			if(field.getName().equals(phrase)) {
				result = (ModelClass)createLocalInstance(field.getType());
                return result;
			}
		}      
		return null;
	}
	
	private static boolean isOtherEndOne(Class<? extends txtuml.api.Association> assocClass, String phrase)
	{
		Field[] fields = assocClass.getDeclaredFields();
  
		for(Field field : fields) {
			//FIXME there is no One annotation
			/*if(field.getName().equals(phrase) && field.isAnnotationPresent(One.class)) 
			{
				return true;
			}*/
			return false;
		}      
		return false;
	}
	
	private static <T extends ModelClass> T selectOne(Collection<T> target) 
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
	private static <T extends ModelClass, AE extends txtuml.api.Association.AssociationEnd<T> >
	T selectOne_AE(AE target) 
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
		Type type=association.getEndType(phrase);
		OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);
			
		Variable variable=currentActivity.createVariable(resultName,type);
		AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);
		
		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);
				
		createObjectFlowBetweenNodes(outputPin,inputPin_AVVA);
		lastNode=setVarAction;
		
        return result;
	}
	private static void createInstance(ModelClass created)
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
    		
    		//creating a merge node for merging the two separate "threads"
    		String mergeNodeName="merge_"+startClassifierBehaviorAction.getName()+"_and_"+setVarAction.getName();
    		lastNode = createMergeNode(mergeNodeName,startClassifierBehaviorAction,setVarAction);
    		

        }
	        
	}
	
	 
	private static void log(String message) { // user log
			if(currentActivity != null) {
	          //TODO: not implemented
			}

		}

	 private static void logError(String message) { // user log
		if(currentActivity != null) {
           
			//TODO: not implemented
		}
	}

	private static boolean runtimeLog(String message) {
		// log generated by the api package 
		// TODO not implemented
		return false;
	}
	
	private static boolean runtimeErrorLog(String message) {
		// error log generated by the api package 
		// TODO not implemented
		return false;
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
			String paramExpression=getExpression((ModelIdentifiedElement)param);
			
			ValuePin argValuePin=(ValuePin)callAction.createArgument(paramName, paramType, UMLPackage.Literals.VALUE_PIN);
			addOpaqueExpressionToValuePin(argValuePin,paramExpression,paramType);
			++i;
		}
	}
	private static Object call(ModelClass target, String methodName, Object... args) throws ImportException
	 {
	    // this method is called at every method call where the target object is of any type that extends ModelClass 
	    // parameters: the target object, the name of the called method and the given parameters
	      
		if(methodName.equals("assoc"))
		{
			for(Method m:target.getClass().getMethods())
			{
				if(m.getName().equals("assoc"))
				{
					try {
						return m.invoke(target,args);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
		}
		String targetName=getObjectIdentifier(target);
		
		CallOperationAction callAction=(CallOperationAction)
				currentActivity.createOwnedNode("call_"+targetName+"."+methodName, UMLPackage.Literals.CALL_OPERATION_ACTION);
		
		Type type=currentModel.getOwnedType(target.getClass().getSimpleName());
		
		org.eclipse.uml2.uml.Class targetClass	=	(org.eclipse.uml2.uml.Class)
					currentModel.getOwnedMember(target.getClass().getSimpleName());
		
		ValuePin callTarget=(ValuePin)callAction.createTarget(callAction.getName()+"_target",type,UMLPackage.Literals.VALUE_PIN);
		
		addOpaqueExpressionToValuePin(callTarget,targetName,type);
		
		callAction.setOperation(findOperation(targetClass,methodName));
		addParamsToCallAction(callAction,target,methodName,args);
	
		createControlFlowBetweenNodes(lastNode,callAction);
		lastNode=callAction;
		
		
		Object returnObj=null;
		try {
			Method method = findMethod(target.getClass(),methodName);
			Class<?> returnType=method.getReturnType();
			returnObj=createLocalInstance(returnType);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		
		return returnObj;
	 }
	 
	 private static void send(ModelClass receiver, txtuml.api.Signal event) 
	 {
		if(currentActivity != null) 
		{
			Signal signalToSend=(Signal) currentModel.getOwnedType(event.getClass().getSimpleName());
			String instanceName=getObjectIdentifier(receiver);
			
			Type instanceType=currentModel.getOwnedType(receiver.getClass().getSimpleName());
			
			SendSignalAction sendSignalAction	=	(SendSignalAction) 
								currentActivity.createOwnedNode("send_"+signalToSend.getName()+"_to_"+instanceName,UMLPackage.Literals.SEND_SIGNAL_ACTION);
			
			sendSignalAction.setSignal(signalToSend);
			
			ValuePin target=(ValuePin)sendSignalAction.createTarget(sendSignalAction.getName()+"_target",instanceType,UMLPackage.Literals.VALUE_PIN);
			
			addOpaqueExpressionToValuePin(target,instanceName,instanceType);
			
			createControlFlowBetweenNodes(lastNode,sendSignalAction);
			
			lastNode=sendSignalAction;
			
		}
			
	}
		 
	public static Object callExternal(ExternalClass target, String methodName, Object... args)
	{
		return null;
	        // TODO not implemented; should return an instance of the actual return type of the called method
	        // it can be get through its Method class
	        // the imported model will get this returned object as the result of the method call
	}

	public static Object callStaticExternal(Class<?> c, String methodName, Object... args)
	{
		return null;
	        // TODO not implemented; should return an instance of the actual return type of the called method
	        // it can be get through its Method class
	        // the imported model will get this returned object as the result of the method call
	        
	        // c will actually always be Class<? extends ExternalClass>
	        // so this method informs the importer about a static method call on an ExternalClass class
	}
	
	

	
	
	private static Object initField(ModelClass target, String fieldName, Object newValue) 
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
				fieldObj=new ModelInt(val,false);	
			}
			else if(newValue instanceof ModelBool)
			{
				boolean val=(boolean) method.invoke(newValue);
				fieldObj=new ModelBool(val,false);	
			}
			else if(newValue instanceof ModelString)
			{

				String val=(String) method.invoke(newValue);
				fieldObj=new ModelString(val,false);
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
	private static Object fieldGet(ModelClass target, String fieldName, Class<?> fieldType)
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
	
	private static Object fieldSet(ModelClass target, String fieldName, Object newValue)  
	{
		try{
			
			Object fieldObj=initField(target,fieldName,newValue);
			if(currentActivity!=null)
			{

				String newValueExpression=getExpression((ModelIdentifiedElement)newValue);			
				Type newValType=ModelImporter.importType(newValue.getClass());
				setStructuralFeatureValue(target,fieldName,newValueExpression,newValType);
				
			}
		
			return fieldObj;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return null;
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
			currentActivity.createVariable(result.getIdentifier(), ModelImporter.importType(target.getClass()));
			setVariableValue(result,valueExpression);
		}
		else
		{
			if(result instanceof ModelInt)
			{
				result=(ModelType<T>) new ModelInt(0,false,valueExpression);
			}
			else if(result instanceof ModelBool)
			{
				result=(ModelType<T>) new ModelBool(false,false,valueExpression);
			}
			else if(result instanceof ModelString)
			{
				result=(ModelType<T>) new ModelString("",false,valueExpression);
			}
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
		
		ModelInt result=new ModelInt(0,false);
		
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
	private static ModelInt add(ModelInt target, ModelInt val)  {

		return importModelInt2OpOperation(target,val,ModelIntOperations.ADD_LITERAL);
		
	}
	
	private static ModelInt subtract(ModelInt target, ModelInt val) {

		return importModelInt2OpOperation(target,val,ModelIntOperations.SUBTRACT_LITERAL);
		
	}
	
	private static ModelInt multiply(ModelInt target, ModelInt val)  {

		return importModelInt2OpOperation(target,val,ModelIntOperations.MULTIPLY_LITERAL);
	}
	
	private static ModelInt divide(ModelInt target, ModelInt val){

		return importModelInt2OpOperation(target,val,ModelIntOperations.DIVIDE_LITERAL);
		
	}
	
	private static ModelInt remainder(ModelInt target, ModelInt val) {

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
			currentActivity.createVariable(result.getIdentifier(), ModelImporter.importType(target.getClass()));
			setVariableValue(result,valueExpression);
		}
		else
		{
			if(result instanceof ModelInt)
			{
				result=(ModelType<T>) new ModelInt(0,false,valueExpression);
			}
			else if(result instanceof ModelBool)
			{
				result=(ModelType<T>) new ModelBool(false,false,valueExpression);
			}
			else if(result instanceof ModelString)
			{
				result=(ModelType<T>) new ModelString("",false,valueExpression);
			}
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
	
	private static ModelInt negate(ModelInt target)  {

		return importModelInt1OpOperation(target,ModelIntOperations.NEGATE_LITERAL);
		
	}
	private static ModelInt abs(ModelInt target) {
		return importModelInt1OpOperation(target,ModelIntOperations.ABS_LITERAL);
	}
	
	private static ModelInt signum(ModelInt target) {

		return importModelInt1OpOperation(target,ModelIntOperations.SIGNUM_LITERAL);
		
	}

	private static ModelBool not(ModelBool target) {
		return (ModelBool)importModelType1OpOperation(target,new ModelBool(),"not ",false);
	}
	
	private static ModelBool or(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.OR_LITERAL);
	}
	
	private static ModelBool xor(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.XOR_LITERAL);
	}
	
	
	private  static ModelBool and(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.AND_LITERAL);
	}
	
	private static ModelBool equal(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.EQUAL_LITERAL);
	}
	
	private static ModelBool noteq(ModelBool target, ModelBool val)  {

		return importModelBool2OpOperation(target,val,ModelBoolOperations.NOTEQ_LITERAL);
	}

	private static ModelBool isEqual(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"=");
	}
	
	private static ModelBool isLessEqual(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"<=");
	}
	
	private static ModelBool isLess(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,"<");
	}
	
	
	private static ModelBool isMoreEqual(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,">=");
	}
	
	private static ModelBool isMore(ModelInt left, ModelInt right)
	{
		return compareModelInts(left,right,">");
	}
	
	
	private static ModelBool compareModelInts(ModelInt left, ModelInt right, String operator)
	{
		String leftExpr=getExpression(left);
		String rightExpr=getExpression(right);
		String expression=leftExpr+" "+operator+" "+rightExpr;
		return new ModelBool(false,false,expression);
	}

	private static String importCondition(Condition cond)
	{
		
		Activity currActivityBackup=currentActivity;
		currentActivity=null;
		ModelBool checkedCond=cond.check();
		String ret= getExpression(checkedCond);
		currentActivity=currActivityBackup;
		return ret;
		
	}
	
	
	private static Pair<ActivityNode,ActivityEdge> createImportBlockBodyRetVal()
	{
		ActivityEdge firstEdge;
		if(blockBodyFirstEdges.size()==cntBlockBodiesBeingImported)
		{
			firstEdge=blockBodyFirstEdges.pop();
		}
		else
		{
			firstEdge=null;
		}
		Pair<ActivityNode,ActivityEdge> ret=new Pair<>(lastNode,firstEdge);
		return ret;
	}
	private static Pair<ActivityNode,ActivityEdge> importBlockBody(BlockBody body)
	{
	
		++cntBlockBodiesBeingImported;
		
		body.run();		
		Pair<ActivityNode,ActivityEdge>  ret=createImportBlockBodyRetVal();
		
		--cntBlockBodiesBeingImported;
		return ret;
	}
	
	private static <T> Pair<ActivityNode,ActivityEdge> importParameterizedBlockBody(ParameterizedBlockBody<T> body, T param)
	{
	
		++cntBlockBodiesBeingImported;
		
		body.run(param);		
		Pair<ActivityNode,ActivityEdge>  ret=createImportBlockBodyRetVal();
		
		--cntBlockBodiesBeingImported;
		return ret;
	}
	
	private static void importWhileStatement(Condition cond, BlockBody body)
	{
		String condExpr=importCondition(cond);
		DecisionNode decisionNode=createNextDecisionNode();
		createFlowBetweenNodes(lastNode,decisionNode);
		
		lastNode=decisionNode;
		
		OpaqueAction elseDummyNode=createNextDummyNode();
		ActivityEdge elseFirstEdge=createControlFlowBetweenNodes(decisionNode,elseDummyNode);
		addGuardToEdge(elseFirstEdge, "else");
		
		Pair<ActivityNode,ActivityEdge> importThenBodyResult=importBlockBody(body);
		ActivityEdge thenFirstEdge=importThenBodyResult.getValue();
		ActivityNode thenLastNode=importThenBodyResult.getKey();
		addGuardToEdge(thenFirstEdge, condExpr);
		
		createFlowBetweenNodes(thenLastNode,decisionNode);
		lastNode=elseDummyNode;
		
		
	}

	private static void importIfStatement(Condition cond, BlockBody thenBody)
	{
		String condExpr=importCondition(cond);
		
		Pair<ActivityNode,ActivityEdge> importBlockBodyResult=importBlockBody(thenBody);
		ActivityEdge thenFirstEdge=importBlockBodyResult.getValue();
		ActivityNode thenLastNode=importBlockBodyResult.getKey();
		addGuardToEdge(thenFirstEdge, condExpr);
		
		lastNode=thenLastNode;
		
	}
	
	private static void importIfStatement(Condition cond, BlockBody thenBody,BlockBody elseBody)
	{
		String condExpr=importCondition(cond);
		DecisionNode decisionNode=createNextDecisionNode();
		createFlowBetweenNodes(lastNode,decisionNode);
		
		lastNode=decisionNode;
		
		
		Pair<ActivityNode,ActivityEdge> importThenBodyResult=importBlockBody(thenBody);
		ActivityEdge thenFirstEdge=importThenBodyResult.getValue();
		ActivityNode thenLastNode=importThenBodyResult.getKey();
		addGuardToEdge(thenFirstEdge, condExpr);
		
		lastNode=decisionNode;
		
		Pair<ActivityNode,ActivityEdge> importElseBodyResult=importBlockBody(elseBody);
		ActivityEdge elseFirstEdge=importElseBodyResult.getValue();
		ActivityNode elseLastNode=importElseBodyResult.getKey();
		addGuardToEdge(elseFirstEdge, "else");
		
		OpaqueAction afterDummyNode=createNextDummyNode();
		createFlowBetweenNodes(thenLastNode,afterDummyNode);
		createFlowBetweenNodes(elseLastNode,afterDummyNode);
		lastNode=afterDummyNode;
		
	}
	
	private static void addGuardToEdge(ActivityEdge edge, String expression)
	{
		OpaqueExpression opaqueExpression=(OpaqueExpression) UMLFactory.eINSTANCE.createOpaqueExpression();
		opaqueExpression.getBodies().add(expression);
		edge.setGuard(opaqueExpression);
	}

	private static void importForStatement(ModelInt from, ModelInt to, ParameterizedBlockBody<ModelInt> body) 
	{
		String fromExpression=getExpression(from);
		String toExpression=getExpression(to);
		ModelInt loopVar=new ModelInt();
		String loopVarId=getObjectIdentifier(loopVar);
		String condExpr=loopVarId+"<="+toExpression;
		
		setVariableValue(loopVar, fromExpression);
		
		DecisionNode decisionNode=createNextDecisionNode();
		createFlowBetweenNodes(lastNode,decisionNode);
		
		lastNode=decisionNode;
		
		OpaqueAction elseDummyNode=createNextDummyNode();
		ActivityEdge elseFirstEdge=createControlFlowBetweenNodes(decisionNode,elseDummyNode);
		addGuardToEdge(elseFirstEdge, "else");
		
		Pair<ActivityNode,ActivityEdge> importThenBodyResult=importParameterizedBlockBody(body,loopVar);
		ActivityEdge thenFirstEdge=importThenBodyResult.getValue();
		ActivityNode thenLastNode=importThenBodyResult.getKey();
		addGuardToEdge(thenFirstEdge, condExpr);
		
		createFlowBetweenNodes(thenLastNode,decisionNode);
		lastNode=elseDummyNode;
		
	}
	
	private static OpaqueAction createNextDummyNode()
	{
		++cntDummyNodes;
		String name="dummy"+cntDummyNodes;
		OpaqueAction dummyNode=(OpaqueAction) currentActivity.createOwnedNode(name,UMLPackage.Literals.OPAQUE_ACTION);
		return dummyNode;
	}
	
	private static DecisionNode createNextDecisionNode()
	{
		++cntDecisionNodes;
		String name="decision"+cntDecisionNodes;
		DecisionNode decisionNode=(DecisionNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.DECISION_NODE);
		return decisionNode;
	}
	
}
