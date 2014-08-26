package txtuml.importer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;







import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;
import org.eclipse.uml2.uml.CallOperationAction;

import txtuml.api.ModelClass;
import txtuml.api.Trigger;
import txtuml.utils.InstanceCreator;

public class MethodImporter extends AbstractImporter {

	
	public static txtuml.api.Signal createSignal(Class<? extends ModelClass.Transition> tr) {
    	Trigger triggerAnnot = tr.getAnnotation(Trigger.class);
    	if (triggerAnnot != null) {
        	return InstanceCreator.createInstance(triggerAnnot.value(), 3);
    	}
		return null;
    }
	
	public static boolean instructionImport() {
		return currentActivity != null;
	}
	
	static void importMethod(Model model,Activity activity,Method sourceMethod,
            Class<?> declaringClass, Object... params) 
	{
		currentModel=model;
		currentActivity = activity;
		ActivityNode initialNode=activity.createOwnedNode("initialNode",UMLPackage.Literals.INITIAL_NODE);
		ActivityNode finalNode=activity.createOwnedNode("finalNode",UMLPackage.Literals.ACTIVITY_FINAL_NODE);
		lastNode=initialNode;
		sourceMethod.setAccessible(true);
		setLocalInstanceToBeCreated(true);
		Object obj = InstanceCreator.createInstance(declaringClass,3);
		setLocalInstanceToBeCreated(false);
		
		if(obj instanceof ModelClass) 
		{
			self=(ModelClass)obj;
		}
		else if(!obj.getClass().equals(Importer.getModelClass()))
		{
		
		        Object o=obj;
		        while(o!=null && !(o instanceof ModelClass))
		        {
		        	Field field=null;
		        	for(Field f:o.getClass().getDeclaredFields())
		        	{
		        		if(f.getName().startsWith("this"));
		        		field=f;
		        		break;
		        	}
		        	field.setAccessible(true);
		        	try {
						o=field.get(o);
					} catch (IllegalArgumentException e) {
						
					} catch (IllegalAccessException e) {
						
					}
		        	field.setAccessible(false);
		        }
		        if(o==null)
		        {
		        	self=null;
		        }
		        else
		        {
		        	self=(ModelClass) o;
		        }
		        
		        
		}
		try 
		{
			sourceMethod.invoke(obj,params);
		}
		catch(Exception e) 
		{
			System.out.println(sourceMethod.getName());
			System.out.println(obj.getClass().getSimpleName());
			e.printStackTrace();
			
		}
		
		createControlFlowBetweenNodes(lastNode,finalNode);
		self=null;
		currentActivity = null;
		currentModel=null;
		
	}
	
	public static void delete(ModelClass obj) {
        if(currentActivity != null) 
        {
           	DestroyObjectAction destroyAction=	(DestroyObjectAction) 
					currentActivity.createOwnedNode("delete_"+obj.getIdentifier(),UMLPackage.Literals.DESTROY_OBJECT_ACTION);
			
			String instanceName=obj.getIdentifier();
			try
			{
			if(instanceName.equals(self.getIdentifier()))
			{
				instanceName="self";
			}
			}
			catch(NullPointerException e)
			{
			
			}
			
			Type type= currentModel.getOwnedType(obj.getClass().getSimpleName());
			
			ValuePin target = (ValuePin) destroyAction.createTarget("target", type, UMLPackage.Literals.VALUE_PIN);
			addOpaqueExpressionToValuePin(target,instanceName,type);
			
			createControlFlowBetweenNodes(lastNode,destroyAction);
			
			lastNode=destroyAction;
        }
		
	}
	
	public static void link(Class<?> assoc, String leftPhrase, ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        if(currentActivity != null) 
        {
        	String leftName=leftObj.getIdentifier();
			try
			{
				if(leftName.equals(self.getIdentifier()))
				{
					leftName="self";
				}
			}
			catch(NullPointerException e)
			{
				
			}
			
			String rightName=rightObj.getIdentifier();
			try
			{
				if(rightName.equals(self.getIdentifier()))
				{
					rightName="self";
				}
			}
			catch(NullPointerException e)
			{
				
			}
			
        	String linkActionName="link_"+leftName+"_and_"+rightName;
        	CreateLinkAction createLinkAction	=	(CreateLinkAction) 
					currentActivity.createOwnedNode(linkActionName, UMLPackage.Literals.CREATE_LINK_ACTION);

			Type end1Type=currentModel.getOwnedType(leftObj.getClass().getSimpleName());
			Type end2Type=currentModel.getOwnedType(rightObj.getClass().getSimpleName());
			
			ValuePin end1_valuePin=(ValuePin) 
					createLinkAction.createInputValue(createLinkAction.getName()+"_end1input",end1Type,UMLPackage.Literals.VALUE_PIN);
			end1_valuePin.setType(end1Type);
			
			ValuePin end2_valuePin=(ValuePin) 
					createLinkAction.createInputValue(createLinkAction.getName()+"_end2input",end2Type,UMLPackage.Literals.VALUE_PIN);
			
			end2_valuePin.setType(end2Type);
			
			
			addOpaqueExpressionToValuePin(end1_valuePin,leftName,end1Type);
			addOpaqueExpressionToValuePin(end2_valuePin,rightName,end2Type);
			
			LinkEndData end1=createLinkAction.createEndData();
			LinkEndData end2=createLinkAction.createEndData();
			
			
			Association association=(Association)currentModel.getOwnedMember(assoc.getSimpleName());
			
			Property end1prop=association.getOwnedEnd(leftPhrase,end1Type);
			Property end2prop=association.getOwnedEnd(rightPhrase,end2Type);
			
			end1.setEnd(end1prop);
			end2.setEnd(end2prop);
			createControlFlowBetweenNodes(lastNode,createLinkAction);
			lastNode=createLinkAction;
        }
       
    }
	
	public static ModelClass selectOne(ModelClass start, Class<? extends txtuml.api.Association> assocClass, String phrase) {
        Field[] fields = assocClass.getDeclaredFields();
        ModelClass result = null;
		for(Field field : fields) {
			if(field.getName().equals(phrase)) {
                setLocalInstanceToBeCreated(true);
				result = (ModelClass)InstanceCreator.createInstance(field.getType(),3);
                setLocalInstanceToBeCreated(false);
                break;
			}
		}            
        
		String startName=start.getIdentifier();
		try
		{
			if(startName.equals(self.getIdentifier()))
			{
				startName="self";
			}
		}
		catch(NullPointerException e)
		{
			
		}
		OpaqueAction selectOneAction=	(OpaqueAction)
				currentActivity.createOwnedNode("selectOne_"+startName+"."+phrase,UMLPackage.Literals.OPAQUE_ACTION);
		selectOneAction.getBodies().add(startName+"."+phrase+"->first()");
		
		createControlFlowBetweenNodes(lastNode, selectOneAction);
		
		
		Association association=(Association) currentModel.getOwnedMember(assocClass.getSimpleName());
		Type type=association.getEndType(phrase);
		OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);
	
		//creating an Add Variable Value action
		AddVariableValueAction setVarAction=	(AddVariableValueAction)
				currentActivity.createOwnedNode("setVar_"+result.getIdentifier(),UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
				
		
		Variable variable=currentActivity.createVariable(result.getIdentifier(),type);
		setVarAction.setVariable(variable);
		
		//creating an input pin for Add Variable Action
		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);
				
		//creating an object flow from fork to Add Variable Action, so we can set the value of the newly created variable to the created instance
		createObjectFlowBetweenNodes(outputPin,inputPin_AVVA);
		lastNode=setVarAction;
        return result;
	}
	 public static void createInstance(ModelClass created)
	 {
        if(currentActivity != null) {
            if(!localInstanceToBeCreated) {
            	
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
        	
        		//creating a fork node and an object flow from Create Object Action's output pin to fork node
        		ForkNode forkNode=(ForkNode) currentActivity.createOwnedNode("fork_"+createAction.getName(),UMLPackage.Literals.FORK_NODE);
        		createObjectFlowBetweenNodes(outputPin,forkNode);
        		
        		//creating a variable for created instance, so we can reference it later
        		Type type=currentModel.getOwnedType(created.getClass().getSimpleName());
        		
        		Variable variable=currentActivity.createVariable(instanceName,type);
        		
        		//creating an Add Variable Value action
        		AddVariableValueAction setVarAction=	(AddVariableValueAction)
        									currentActivity.createOwnedNode("setVar_"+instanceName,UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
        		
        		setVarAction.setVariable(variable);
        		//creating an input pin for Add Variable Action
        		InputPin inputPin_AVVA=setVarAction.createValue(setVarAction.getName()+"_input",type);
        		
        		//creating an object flow from fork to Add Variable Action, so we can set the value of the newly created variable to the created instance
        		createObjectFlowBetweenNodes(forkNode,inputPin_AVVA);
        		
        		//creating a Start Classifier Behavior Action for our newly created object
        		StartClassifierBehaviorAction startClassifierBehaviorAction = (StartClassifierBehaviorAction) 
        								currentActivity.createOwnedNode("startClassifierBehavior_"+instanceName,UMLPackage.Literals.START_CLASSIFIER_BEHAVIOR_ACTION);
        		
        		//creating an input pin for Start Classifier Behavior Action
        		InputPin inputPin_startCBA=startClassifierBehaviorAction.createObject(startClassifierBehaviorAction.getName()+"_input",classifier);
        		
        		//creating an object flow from fork node to Start Classifier Behavior Action
        		createObjectFlowBetweenNodes(forkNode,inputPin_startCBA);
        		
        		//creating a merge node, so we will have a single thread
        		String mergeNodeName="merge_"+startClassifierBehaviorAction.getName()+"_and_"+setVarAction.getName();
        		MergeNode mergeNode=(MergeNode) currentActivity.createOwnedNode(mergeNodeName,UMLPackage.Literals.MERGE_NODE);
        		
        		//creating a control flow from Start Classifier Behavior Action to merge node
        		createControlFlowBetweenNodes(startClassifierBehaviorAction,mergeNode);
        		
        		//creating a control flow from Add Variable Value Action to merge node
        		createControlFlowBetweenNodes(setVarAction,mergeNode);
        		
        		//merge node is the last node in line
        		lastNode= mergeNode;
			}
            
        }
	        
	}
	 
	 public static void send(ModelClass receiver, txtuml.api.Signal event) 
	 {
		if(currentActivity != null) 
		{
			Signal signalToSend=(Signal) currentModel.getOwnedType(event.getClass().getSimpleName());
			String instanceName=receiver.getIdentifier();
			try
			{
				if(instanceName.equals(self.getIdentifier()))
				{
					instanceName="self";
				}
			}
			catch(NullPointerException e)
			{
				
			}
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
	 
	private static ActivityEdge createControlFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.CONTROL_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}
	
	private static ActivityEdge createObjectFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.OBJECT_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}
	
	private static ValuePin addOpaqueExpressionToValuePin(ValuePin pin,String expression, Type type)
    {
    	OpaqueExpression opaqueExpression=(OpaqueExpression) pin.createValue("value",type,UMLPackage.Literals.OPAQUE_EXPRESSION);
		opaqueExpression.getBodies().add(expression);
		return pin;
    }
	
	public static Object call(ModelClass target, String methodName, Object... args)
	{
	    	// this method is called at every method call where the target object is of any type that extends ModelClass 
	    	// parameters: the target object, the name of the called method and the given parameters
	      
		String targetName=target.getIdentifier();
		try
		{
			if(targetName.equals(self.getIdentifier()))
			{
				targetName="self";
			}
		}
		catch(NullPointerException e)
		{
			
		}
		
		CallOperationAction callAction=(CallOperationAction)
				currentActivity.createOwnedNode("call_"+targetName+"."+methodName, UMLPackage.Literals.CALL_OPERATION_ACTION);
		
		Type type=currentModel.getOwnedType(target.getClass().getSimpleName());
		
		org.eclipse.uml2.uml.Class targetClass	=	(org.eclipse.uml2.uml.Class)
					currentModel.getOwnedMember(target.getClass().getSimpleName());
		
		ValuePin callTarget=(ValuePin)callAction.createTarget(callAction.getName()+"_target",type,UMLPackage.Literals.VALUE_PIN);
		
		addOpaqueExpressionToValuePin(callTarget,targetName,type);
		
		callAction.setOperation(getOperation(targetClass,methodName));
		createControlFlowBetweenNodes(lastNode,callAction);
		lastNode=callAction;
		
		return null;
	        // TODO should return an instance of the actual return type of the called method
	        // it can be get through its Method class
	        // the imported model will get this returned object as the result of the method call
	        // (if the called method is a void, a null can be returned) 
	}
	
	static Operation getOperation(org.eclipse.uml2.uml.Class ownerClass,String name)
	{
		for(Operation op:ownerClass.getOperations())
		{
			if(op.getName().equals(name))
			{
				return op;
			}
		}
		return null;
	}
		
	public static Object callExternal(Class<?> c, String methodName, Object... args)
	{
			// this method is called before any STATIC call where the target class does NOT implement the ModelElement interface 
	    	// parameters: the target class, the name of the called method and the given parameters
		return null;
	        // TODO not implemented; should return an instance of the actual return type of the called method
	        // it can be get through its Method class
	        // the imported model will get this returned object as the result of the method call
	}
	    
	public static void fieldGet(ModelClass target, String fieldName)
	{
	    	// TODO not implemented
	    	// this method is called BEFORE any field get on a ModelClass object
	    	// if this method changes the value of the actual field, the model will get that value as the result of this field get
	}

	public static void fieldSet(ModelClass target, String fieldName, Object newValue) 
	{
	    	// TODO not implemented
	    	// this method is called BEFORE any field set on a ModelClass object
	}
	    
	public static void unLink(Class<?> assoc, String leftPhrase, ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        if(currentActivity != null) 
        {
        	String leftName=leftObj.getIdentifier();
			try
			{
				if(leftName.equals(self.getIdentifier()))
				{
					leftName="self";
				}
			}
			catch(NullPointerException e)
			{
				
			}
			
			String rightName=rightObj.getIdentifier();
			try
			{
				if(rightName.equals(self.getIdentifier()))
				{
					rightName="self";
				}
			}
			catch(NullPointerException e)
			{
				
			}
			
        	String unlinkActionName="unlink_"+leftName+"_and_"+rightName;
        	DestroyLinkAction destroyLinkAction	=	(DestroyLinkAction) 
					currentActivity.createOwnedNode(unlinkActionName, UMLPackage.Literals.DESTROY_LINK_ACTION);

			Type end1Type=currentModel.getOwnedType(leftObj.getClass().getSimpleName());
			Type end2Type=currentModel.getOwnedType(rightObj.getClass().getSimpleName());
			
			ValuePin end1_valuePin=(ValuePin) 
					destroyLinkAction.createInputValue(destroyLinkAction.getName()+"_end1input",end1Type,UMLPackage.Literals.VALUE_PIN);
			end1_valuePin.setType(end1Type);
			
			ValuePin end2_valuePin=(ValuePin) 
					destroyLinkAction.createInputValue(destroyLinkAction.getName()+"_end2input",end2Type,UMLPackage.Literals.VALUE_PIN);
			
			end2_valuePin.setType(end2Type);
			
			
			addOpaqueExpressionToValuePin(end1_valuePin,leftName,end1Type);
			addOpaqueExpressionToValuePin(end2_valuePin,rightName,end2Type);
			
			LinkEndData end1=destroyLinkAction.createEndData();
			LinkEndData end2=destroyLinkAction.createEndData();
			
			
			Association association=(Association)currentModel.getOwnedMember(assoc.getSimpleName());
			
			Property end1prop=association.getOwnedEnd(leftPhrase,end1Type);
			Property end2prop=association.getOwnedEnd(rightPhrase,end2Type);
			
			end1.setEnd(end1prop);
			end2.setEnd(end2prop);
			createControlFlowBetweenNodes(lastNode,destroyLinkAction);
			lastNode=destroyLinkAction;
        }
       
    }	    
    public static void log(String message) { // user log
		if(currentActivity != null) {
          //TODO: not implemented
		}

	}

	public static void logError(String message) { // user log
		if(currentActivity != null) {
           
			//TODO: not implemented
		}
	}

	public static boolean runtimeLog(String message) {
		// log generated by the api package 
		// TODO not implemented
		return false;
	}
	
	public static boolean runtimeErrorLog(String message) {
		// error log generated by the api package 
		// TODO not implemented
		return false;
	}	
	    
	private static ActivityNode lastNode=null;
	private static Activity currentActivity=null;
	private static Model currentModel=null;
	private static ModelClass self=null;
}
