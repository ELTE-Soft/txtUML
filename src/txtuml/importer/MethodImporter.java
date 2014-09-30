package txtuml.importer;

import java.lang.reflect.Field;
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

import txtuml.api.ModelClass;
import txtuml.api.ModelIdentifiedElement;
import txtuml.api.Trigger;
import txtuml.utils.InstanceCreator;

public class MethodImporter extends AbstractMethodImporter {

	public static txtuml.api.Signal createSignal(Class<? extends ModelClass.Transition> tr) {
    	Trigger triggerAnnot = tr.getAnnotation(Trigger.class);
    	if (triggerAnnot != null) {
        	return InstanceCreator.createInstance(triggerAnnot.value(), 3);
    	}
		return null;
    }
	
	
	
	static void importMethod(Model model,Activity activity,Method sourceMethod,
            Class<?> declaringClass) 
	{
		currentModel=model;
		currentActivity = activity;
		currentMethod=sourceMethod;
		importing=true;
		
		ActivityNode initialNode=activity.createOwnedNode("initialNode",UMLPackage.Literals.INITIAL_NODE);
		ActivityNode finalNode=activity.createOwnedNode("finalNode",UMLPackage.Literals.ACTIVITY_FINAL_NODE);
		
		lastNode=initialNode;
		
		Object obj=createLocalInstance(declaringClass,3);
		
		loadCurrentParameters();
		assignSelf(obj);
		
		try 
		{
			sourceMethod.setAccessible(true);
			Object returnObj=sourceMethod.invoke(obj,currentParameters);
			if(returnObj!=null)
			{
				try
				{
					createAssignReturnValueAction(returnObj);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			sourceMethod.setAccessible(false);
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
		currentParameters=null;
		importing=false;
	}
	
	
	private static void createAssignReturnValueAction(Object returnObj) throws Exception
	{
		String retName=getObjectIdentifier((ModelIdentifiedElement) returnObj);

		Type returnType=null;
		Parameter returnParam=null;
		for(Parameter p : currentActivity.getSpecification().getOwnedParameters())
		{	
			if(p.getName().equals("return"))
			{
				returnType=p.getType();
				returnParam=p;
				break;
			}
			
		}
		ActivityNode readAction=null;
		OutputPin outputPin=null;
		if(isObjectAFieldOfSelf((ModelIdentifiedElement)returnObj))
		{
			String fieldName=getObjectIdentifier((ModelIdentifiedElement) returnObj).substring(5);
			readAction=createReadStructuralFeatureAction(self,fieldName,returnType);
			outputPin=((ReadStructuralFeatureAction) readAction).createResult(readAction.getName()+"_result",returnType);
		}
		else
		{
			String variableName=getObjectIdentifier((ModelIdentifiedElement)returnObj);
			readAction=createReadVariableAction(variableName,returnType);
			outputPin=((ReadVariableAction)readAction).createResult(readAction.getName()+"_result",returnType);
		}
		createControlFlowBetweenNodes(lastNode,readAction);
		
		ActivityParameterNode returnParamNode= (ActivityParameterNode)
					currentActivity.createOwnedNode("return_paramNode",UMLPackage.Literals.ACTIVITY_PARAMETER_NODE);
		
		returnParamNode.setParameter(returnParam);
		returnParamNode.setType(returnType);
		createObjectFlowBetweenNodes(outputPin,returnParamNode);
		lastNode=returnParamNode;
		
		
		
	}
	private static void loadCurrentParameters()
	{
		currentParameters=new Object[currentMethod.getParameterTypes().length];
		int i=0;
		for(Class<?> c: currentMethod.getParameterTypes())
		{
			currentParameters[i]=createLocalInstance(c,1);
			String argName="arg"+i;
			Type paramType=null;
			Parameter param=null;
			
			for(Parameter p : currentActivity.getSpecification().getOwnedParameters())
			{	
				if(p.getName().equals(argName))
				{
					paramType=p.getType();
					param=p;
					break;
				}
				
			}
			Variable paramVar=currentActivity.createVariable(argName, paramType);
			
			ActivityParameterNode paramNode= (ActivityParameterNode)
					currentActivity.createOwnedNode(argName+"_paramNode",UMLPackage.Literals.ACTIVITY_PARAMETER_NODE);
		
			
			paramNode.setParameter(param);
			paramNode.setType(paramType);
			
			AddVariableValueAction addVarValAction = (AddVariableValueAction)
					currentActivity.createOwnedNode("set_"+argName, UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
			
			addVarValAction.setVariable(paramVar);
			
			InputPin inputPin =  addVarValAction.createValue("value",paramType,UMLPackage.Literals.INPUT_PIN);
			
			createObjectFlowBetweenNodes(paramNode,inputPin);
			createControlFlowBetweenNodes(lastNode,addVarValAction);
			lastNode=addVarValAction;
			++i;
		}
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
