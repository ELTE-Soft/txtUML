package txtuml.importer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.Variable;

import txtuml.api.ModelBool;
import txtuml.api.ModelClass;
import txtuml.api.ModelIdentifiedElement;
import txtuml.api.Trigger;
import txtuml.utils.InstanceCreator;

public class MethodImporter extends AbstractMethodImporter {

	public static txtuml.api.Signal createSignal(Class<? extends ModelClass.Transition> tr) {
    	Trigger triggerAnnot = tr.getAnnotation(Trigger.class);
    	if (triggerAnnot != null) {
        	return InstanceCreator.createInstance(triggerAnnot.value());
    	}
		return null;
    }
	
	
	static ModelBool importGuardMethod(Model model, Method sourceMethod, Class<?> declaringClass)
	{
		currentModel=model;
		currentMethod=sourceMethod;
		currentParameters=null;
		importing=true;
	

		Object classInstance=createLocalInstance(declaringClass);
		assignSelf(classInstance);
		
		sourceMethod.setAccessible(true);
		ModelBool returnValue = null;
		try {
			returnValue = (ModelBool) sourceMethod.invoke(classInstance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		sourceMethod.setAccessible(false);
		
		self=null;
		currentModel=null;
		importing=false;
		
		return returnValue;
	}

	static void importMethod(Model model, Activity activity, Method sourceMethod, Class<?> declaringClass) 

	{
		currentModel=model;
		currentActivity = activity;
		currentMethod=sourceMethod;
		importing=true;
		
		ActivityNode initialNode=activity.createOwnedNode("initialNode",UMLPackage.Literals.INITIAL_NODE);	
		lastNode=initialNode;
		
		Object classInstance=createLocalInstance(declaringClass);
		assignSelf(classInstance);

		loadCurrentParameters();
		
		importBody(classInstance);
	
		ActivityNode finalNode=activity.createOwnedNode("finalNode",UMLPackage.Literals.ACTIVITY_FINAL_NODE);
		createControlFlowBetweenNodes(lastNode,finalNode);
		
		self=null;
		currentActivity = null;
		currentModel=null;
		currentParameters=null;
		importing=false;
	}
	
	private static void importBody(Object classInstance)
	{
		try 
		{
			currentMethod.setAccessible(true);
			Object returnValue=currentMethod.invoke(classInstance,currentParameters);
			if(returnValue!=null)
			{
				try
				{
					createAssignReturnValueAction(returnValue);
				}
				catch(Exception e)
				{
					//e.printStackTrace();
				}
			}
			currentMethod.setAccessible(false);
		}
		catch(Exception e) 
		{
			//e.printStackTrace();
		}
	}
	
	
	private static void createAssignReturnValueAction(Object returnObj) throws Exception
	{
		String retName=getObjectIdentifier((ModelIdentifiedElement) returnObj);

		Parameter returnParam=findParameter("return");
		
		if(returnParam!=null)
		{
			Type returnType=returnParam.getType();
			ActivityNode readAction=null;
			OutputPin outputPin=null;
			
			if(isObjectAFieldOfSelf((ModelIdentifiedElement)returnObj))
			{
				String fieldName=retName.substring(5);
				readAction=createReadStructuralFeatureAction(self,fieldName,returnType);
				outputPin=((ReadStructuralFeatureAction) readAction).createResult(readAction.getName()+"_result",returnType);
			}
			else
			{
				String variableName=retName;
				readAction=createReadVariableAction(variableName,returnType);
				outputPin=((ReadVariableAction)readAction).createResult(readAction.getName()+"_result",returnType);
			}
			
			createControlFlowBetweenNodes(lastNode,readAction);
			
			ActivityParameterNode returnParamNode = createParameterNode(returnParam,"return",returnType);
			createObjectFlowBetweenNodes(outputPin,returnParamNode);
			lastNode=returnParamNode;
			
		}
		
		
		
	}
	
	private static ActivityParameterNode createParameterNode(Parameter param,String paramName,Type paramType)
	{
		ActivityParameterNode paramNode= (ActivityParameterNode)
				currentActivity.createOwnedNode(paramName+"_paramNode",UMLPackage.Literals.ACTIVITY_PARAMETER_NODE);
		
		paramNode.setParameter(param);
		paramNode.setType(paramType);
		
		return paramNode;
	}
	
	private static void loadCurrentParameters()
	{
		currentParameters=new Object[currentMethod.getParameterTypes().length];
		int i=0;
		for(Class<?> c: currentMethod.getParameterTypes())
		{
			currentParameters[i]=createLocalInstance(c);
			String argName="arg"+i;
			
			Parameter param=findParameter(argName);
			if(param!=null)
			{
				Type paramType=param.getType();
				addParameterToActivity(param,argName,paramType);
			}
			++i;
		}
	}
	
	private static Parameter findParameter(String paramName)
	{
		for(Parameter p : currentActivity.getSpecification().getOwnedParameters())
		{	
			if(p.getName().equals(paramName))
			{
				return p;
			}
			
		}
		return null;
	}
	
	private static void addParameterToActivity(Parameter param,String paramName,Type paramType)
	{
		Variable paramVar=currentActivity.createVariable(paramName, paramType);
		
		ActivityParameterNode paramNode = createParameterNode(param,paramName,paramType);
		
		AddVariableValueAction addVarValAction = createAddVarValAction(paramVar,"set_"+paramName);
		
		InputPin inputPin =  addVarValAction.createValue(addVarValAction.getName()+"_value",paramType,UMLPackage.Literals.INPUT_PIN);
		
		createObjectFlowBetweenNodes(paramNode,inputPin);
		createControlFlowBetweenNodes(lastNode,addVarValAction);
		lastNode=addVarValAction;
	}
	
	private static void assignSelf(Object obj)
	{
		if(obj instanceof ModelClass) 
		{
			self=(ModelClass)obj;
		}
		else if(!obj.getClass().equals(ModelImporter.getModelClass()))
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
		else
		{
			self=null;
		}
	}
	

}
