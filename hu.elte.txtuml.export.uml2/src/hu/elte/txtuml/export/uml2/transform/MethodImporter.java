package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelIdentifiedElement;
import hu.elte.txtuml.api.StateMachine;
import hu.elte.txtuml.api.Trigger;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstancesMap;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.utils.InstanceCreator;

import java.lang.reflect.Method;
import java.util.Stack;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

public class MethodImporter extends AbstractMethodImporter {

	public static hu.elte.txtuml.api.Signal createSignal(Class<? extends StateMachine.Transition> tr) {
    	Trigger triggerAnnot = tr.getAnnotation(Trigger.class);
    	if (triggerAnnot != null) {
        	return InstanceCreator.createInstance(triggerAnnot.value());
    	}
		return null;
    }
	
	private static ModelBool importGuardBody(Object classInstance)
	{
		currentMethod.setAccessible(true);
		ModelBool returnValue = null;
		try {
			returnValue = (ModelBool) currentMethod.invoke(classInstance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		currentMethod.setAccessible(false);
		
		return returnValue;
	}
	
	private static void endGuardImport()
	{
		currentModel=null;
		importing=false;
	}
	static ModelBool importGuardMethod(Model model, Method sourceMethod,StateMachine.Transition transitionInstance)
	{
		initGuardImport(model,sourceMethod,transitionInstance);
		
		ModelBool returnValue=importGuardBody(transitionInstance);
		
		endGuardImport();
	
		return returnValue;
	}

	private static void initGuardImport(Model model, Method sourceMethod, StateMachine.Transition transitionInstance)
	{
		localInstances=InstancesMap.create();
		currentModel=model;
		currentMethod=sourceMethod;
		currentParameters=null;
		importing=true;
	
		currentSignal=InstructionImporter.initAndGetSignalInstanceOfTransition(transitionInstance);	

	}
	
	private static void initMethodImport(Model model, Activity activity, Method sourceMethod, ModelElement classInstance)
	{
		currentModel=model;
		currentActivity = activity;
		currentMethod=sourceMethod;
		cntBlockBodiesBeingImported=0;
		blockBodyFirstEdges=new Stack<ActivityEdge>();
		cntDecisionNodes=0;
		importing=true;
		localInstances=InstancesMap.create();
		
		ActivityNode initialNode=activity.createOwnedNode("initialNode",UMLPackage.Literals.INITIAL_NODE);	
		lastNode=initialNode;
		
		loadCurrentParameters();
		
		if(ElementTypeTeller.isTransition(classInstance))
		{
			currentSignal=InstructionImporter.
					initAndGetSignalInstanceOfTransition(
							(StateMachine.Transition)classInstance
							);		
			
			if(currentSignal!=null)
			{
				addSignalParameter();
			}
		}
	}
	
	private static void addSignalParameter()
	{
		Class<?> signalClass=currentSignal.getClass();
		String signalName=signalClass.getSimpleName();
		Type signalType=ModelImporter.importType(signalClass);
		Parameter signalParam=currentActivity.createOwnedParameter(signalName,signalType);
		createParameterNode(signalParam,signalName,signalType);
	}
	
	private static void endMethodImport()
	{
		ActivityNode finalNode=currentActivity.createOwnedNode("finalNode",UMLPackage.Literals.ACTIVITY_FINAL_NODE);
		createControlFlowBetweenNodes(lastNode,finalNode);
		
		currentActivity = null;
		currentModel=null;
		currentParameters=null;
		currentSignal=null;
		importing=false;
		
	}
	
	static void importMethod(Model model, Activity activity, Method sourceMethod, ModelElement classInstance) 
	{
		initMethodImport(model,activity,sourceMethod,classInstance);
		importBody(classInstance);
		endMethodImport();	
	}
	
	private static void importBody(Object classInstance)
	{
		try 
		{
			currentMethod.setAccessible(true);
			Object returnValue=currentMethod.invoke(classInstance,(Object[])currentParameters);
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

		Parameter returnParam=ElementFinder.findParameterInActivity("return",currentActivity);
		
		if(returnParam!=null)
		{
			Type returnType=returnParam.getType();
			ActivityNode returnValProviderAction=null;
			OutputPin outputPin=null;
			
			if(isObjectAFieldOfSelf((ModelIdentifiedElement)returnObj))
			{
				String fieldName=retName.substring(5);
				returnValProviderAction=
						createReadStructuralFeatureAction(selfInstance,fieldName,returnType);
				outputPin=
						((ReadStructuralFeatureAction) returnValProviderAction).
						createResult(returnValProviderAction.getName()+"_result",returnType);
			}
			else if(isInstanceCalculated((ModelElement) returnObj) || isInstanceLiteral((ModelElement) returnObj))
			{
				
				String expression = getExpression ((ModelIdentifiedElement) returnObj);
				
				returnValProviderAction=
						currentActivity.createOwnedNode(expression,UMLPackage.Literals.OPAQUE_ACTION);
			
				
				
				((OpaqueAction)returnValProviderAction)
					.getBodies().add(expression);

				outputPin=
						((OpaqueAction)returnValProviderAction)
						.createOutputValue(returnValProviderAction.getName()+"_result",returnType);
			}
			else
			{
				String variableName=retName;
				returnValProviderAction=createReadVariableAction(variableName,returnType);
				outputPin=((ReadVariableAction)returnValProviderAction).createResult(returnValProviderAction.getName()+"_result",returnType);
			}
			
			createControlFlowBetweenNodes(lastNode,returnValProviderAction);
			
			ActivityParameterNode returnParamNode = createParameterNode(returnParam,"return",returnType);
			createObjectFlowBetweenNodes(outputPin,returnParamNode);
			lastNode=returnParamNode;
			
		}
		
		
		
	}
	
	private static boolean isInstanceLiteral(ModelElement instance) {
		
		InstanceInformation instInfo=getInstanceInfo(instance);
		return instInfo!=null && instInfo.isLiteral();
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
		currentParameters=new ModelElement[currentMethod.getParameterTypes().length];
		int i=0;
		for(Class<?> c: currentMethod.getParameterTypes())
		{
			currentParameters[i]=(ModelElement) DummyInstanceCreator.createDummyInstance(c);
		
			String argName="arg"+i;
			localInstances.put(currentParameters[i],InstanceInformation.create(argName));
			Parameter param=ElementFinder.findParameterInActivity(argName,currentActivity);
			if(param!=null)
			{
				Type paramType=param.getType();
				addParameterToActivity(param,argName,paramType);
			}
			++i;
		}
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
	

}
