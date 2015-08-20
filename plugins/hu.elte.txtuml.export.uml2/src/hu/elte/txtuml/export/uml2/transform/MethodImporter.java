package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.model.ModelBool;
import hu.elte.txtuml.api.model.ModelElement;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ImportWarningProvider;

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
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

/**
 * This class is responsible for importing methods and their parameters, bodies and return values.
 * @author Adam Ancsin
 *
 */
class MethodImporter extends AbstractMethodImporter 
{
	/**
	 * Imports a guard method of a transition.
	 * @param model The UML2 model.
	 * @param sourceMethod The guard method to be imported.
	 * @param transitionInstance The dummy instance of the transition.
	 * @return The guard expression represented by the method's return value.
	 *
	 * @author Adam Ancsin
	 */
	static String importGuardMethod
		(Model model, Method sourceMethod,StateMachine.Transition transitionInstance)
	{
		initGuardImport(model,sourceMethod,transitionInstance);
		
		ModelBool guardReturnValue=importGuardBody(transitionInstance);
		
		String guardExpression = null;
		if(guardReturnValue!=null)			
			guardExpression=getConditionOrConstraintExpression(guardReturnValue);
	
		endGuardImport();
	
		return guardExpression;
	}
	
	/**
	 * Imports a method (member function of a model class or an entry/exit/effect action).
	 * @param model The UML2 model.
	 * @param activity The UML2 activity of the member function/action (entry/exit/effect).
	 * @param sourceMethod The method to be imported.
	 * @param classInstance The dummy instance of the owner class. (model class, state or transition)
	 *
	 * @author Adam Ancsin
	 */
	static void importMethod
		(Model model, Activity activity, Method sourceMethod, ModelElement classInstance)
	{
		initMethodImport(model,activity,sourceMethod,classInstance);
		importBody(classInstance);
		endMethodImport(classInstance);	
	}

	/**
	 * Imports the body of a guard method.
	 * @param transitionInstance The dummy instance of the transition.
	 * @return The return value of the guard method.
	 *
	 * @author Adam Ancsin
	 */
	private static ModelBool importGuardBody(StateMachine.Transition transitionInstance) 
	{
		currentMethod.setAccessible(true);
		ModelBool returnValue = null;
		try 
		{
			returnValue = (ModelBool) currentMethod.invoke(transitionInstance);
		} 
		catch (Exception e)
		{
			ImportWarningProvider.createWarning(
					"Failed to import guard of transition: " +
					transitionInstance.getClass().getCanonicalName()
				);
		} 
		currentMethod.setAccessible(false);
		
		return returnValue;
	}
	
	/**
	 * Initializes the import of a guard method.
	 * @param model The UML2 model.
	 * @param sourceMethod The guard method to be imported.
	 * @param transitionInstance The dummy instance of the transition.
	 *
	 * @author Adam Ancsin
	 */
	private static void initGuardImport(Model model, Method sourceMethod, StateMachine.Transition transitionInstance)
	{
		InstanceManager.initLocalInstancesMap();
		currentSignal = null;
		currentModel=model;
		currentMethod=sourceMethod;
		currentParameters=null;
	}
	
	/**
	 * Ends guard import.
	 *
	 * @author Adam Ancsin
	 */
	private static void endGuardImport()
	{
		InstanceManager.clearLocallInstancesMap();
		currentSignal = null;
		currentModel=null;
	}
	
	/**
	 * Initializes the import of a method (member function of a model class or an entry/exit/effect action).
	 * @param model The UML2 model.
	 * @param activity The UML2 activity of the member function/action (entry/exit/effect).
	 * @param sourceMethod The method to be imported.
	 * @param classInstance The dummy instance of the owner class. (model class, state or transition)
	 *
	 * @author Adam Ancsin
	 */
	private static void initMethodImport(Model model, Activity activity, Method sourceMethod, ModelElement classInstance)
	{
		currentModel = model;
		currentActivity = activity;
		currentMethod = sourceMethod;
		cntBlockBodiesBeingImported=0;
		blockBodyFirstEdges=new Stack<ActivityEdge>();
		cntDecisionNodes=0;
		InstanceManager.initLocalInstancesMap();
		
		ActivityNode initialNode=activity.createOwnedNode("initialNode",UMLPackage.Literals.INITIAL_NODE);	
		lastNode=initialNode;
		
		loadCurrentParameters();
	}
	
	/**
	 * Ends method import.
	 * 
	 * @param classInstance The dummy instance of the owner class. (model class, state or transition)
	 * 
	 * @author Adam Ancsin
	 */
	private static void endMethodImport(ModelElement classInstance)
	{
		if(ElementTypeTeller.isTransition(classInstance) && currentSignal != null)
			createSignalParameter();
		
		ActivityNode finalNode=currentActivity.createOwnedNode("finalNode",UMLPackage.Literals.ACTIVITY_FINAL_NODE);
		createControlFlowBetweenActivityNodes(lastNode,finalNode);
		
		InstanceManager.clearLocallInstancesMap();
		currentActivity = null;
		currentModel=null;
		currentParameters=null;
		currentSignal=null;	
	}
	
	/**
	 * Creates an activity parameter and parameter node for the trigger signal of an effect method. Should only be used when
	 * imported method is an effect method of a transition.
	 * 
	 * @author Adam Ancsin
	 */
	private static void createSignalParameter()
	{
		Class<?> signalClass=currentSignal.getClass();
		String signalName=signalClass.getSimpleName();
		Type signalType=ModelImporter.importType(signalClass);
		Parameter signalParam=currentActivity.createOwnedParameter(signalName,signalType);
		createParameterNode(signalParam,signalName,signalType);
	}
	
	/**
	 * Imports the body of a method (member function of a model class or an entry/exit/effect action).
	 * @param classInstance The dummy instance of the owner class. (model class, state or transition)
	 *
	 * @author Adam Ancsin
	 */
	private static void importBody(ModelElement classInstance)
	{
		String methodQualifiedName = classInstance.getClass().getCanonicalName()+currentMethod.getName();
		try 
		{
			currentMethod.setAccessible(true);
			Object returnValue=currentMethod.invoke(classInstance,(Object[])currentParameters);
			if(returnValue!=null)
			{
				try
				{
					assignReturnValue(returnValue);
				}
				catch(Exception e)
				{
					ImportWarningProvider.createWarning("Failed to import method: "+methodQualifiedName);
				}
			}
			currentMethod.setAccessible(false);
		}
		catch(Exception e) 
		{
			ImportWarningProvider.createWarning("Failed to import method: "+methodQualifiedName);
		}
	}
	
	/**
	 * Assigns the return value to the return parameter of the activity.
	 * @param returnObj The return value object.
	 * @throws Exception
	 *
	 * @author Adam Ancsin
	 */
	private static void assignReturnValue(Object returnObj) throws Exception
	{
		Parameter returnParam=ElementFinder.findParameterInActivity("return",currentActivity);
		
		if(returnParam!=null)
		{
			Type returnType=returnParam.getType();
			String expression = getExpression (returnObj);
				
			OpaqueAction returnValProviderAction = (OpaqueAction)
					currentActivity.createOwnedNode(expression,UMLPackage.Literals.OPAQUE_ACTION);

			returnValProviderAction.getBodies().add(expression);

			OutputPin outputPin = returnValProviderAction
					.createOutputValue(returnValProviderAction.getName()+"_result",returnType);
			
			createControlFlowBetweenActivityNodes(lastNode,returnValProviderAction);
			
			ActivityParameterNode returnParamNode = createParameterNode(returnParam,"return",returnType);
			
			createObjectFlowBetweenActivityNodes(outputPin,returnParamNode);
			lastNode=returnParamNode;
		}
	}
	
	/**
	 * Creates a parameter node in the current activity for a specified parameter.
	 * @param param The UML2 parameter.
	 * @param paramName The name of the parameter.
	 * @param paramType The UML2 type of the parameter.
	 * @return The created activity parameter node.
	 *
	 * @author Adam Ancsin
	 */
	private static ActivityParameterNode createParameterNode(Parameter param,String paramName,Type paramType)
	{
		ActivityParameterNode paramNode= (ActivityParameterNode)
				currentActivity.createOwnedNode(paramName+"_paramNode",UMLPackage.Literals.ACTIVITY_PARAMETER_NODE);
		
		paramNode.setParameter(param);
		paramNode.setType(paramType);
		
		return paramNode;
	}
	
	/**
	 * Loads the parameters of the imported method to currentParameters.
	 * 
	 * @author Adam Ancsin
	 */
	private static void loadCurrentParameters()
	{
		currentParameters=new ModelElement[currentMethod.getParameterTypes().length];
		int i=0;
		for(Class<?> c: currentMethod.getParameterTypes())
		{
			currentParameters[i]=(ModelElement) DummyInstanceCreator.createDummyInstance(c);
		
			String argName="arg"+i;
			InstanceManager.createLocalInstancesMapEntry(currentParameters[i],InstanceInformation.create(argName));
			Parameter param=ElementFinder.findParameterInActivity(argName,currentActivity);
			if(param!=null)
			{
				Type paramType=param.getType();
				addParameterToActivity(param,argName,paramType);
			}
			++i;
		}
	}
	
	/**
	 * Adds a parameter to the current activity. Creates an activity parameter node and a variable for the parameter, reads
	 * the value of the parameter node and assigns it to the variable.
	 * 
	 * @param param The UML2 parameter.
	 * @param paramName The name of the parameter.
	 * @param paramType The UML2 type of the parameter.
	 *
	 * @author Adam Ancsin
	 */
	private static void addParameterToActivity(Parameter param,String paramName,Type paramType)
	{
		Variable paramVar = currentActivity.createVariable(paramName, paramType);
		
		ActivityParameterNode paramNode = createParameterNode(param,paramName,paramType);
		
		AddVariableValueAction addVarValAction = createAddVarValAction(paramVar,"set_"+paramName);
		
		InputPin inputPin =  addVarValAction.createValue(addVarValAction.getName()+"_value",paramType,UMLPackage.Literals.INPUT_PIN);
		
		createObjectFlowBetweenActivityNodes(paramNode,inputPin);
		createControlFlowBetweenActivityNodes(lastNode,addVarValAction);
		lastNode=addVarValAction;
	}
}
