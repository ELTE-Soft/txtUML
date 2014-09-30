package txtuml.importer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

import txtuml.api.ModelClass;
import txtuml.api.ModelIdentifiedElement;
import txtuml.utils.InstanceCreator;

public abstract class AbstractMethodImporter extends AbstractImporter {
	

	public static boolean isImporting() {
		return importing;
	}
	
	protected static <T> T createLocalInstance(Class<T> typeClass, int depth, Object... givenParameters)
	{
		setLocalInstanceToBeCreated(true);
		T createdObject = InstanceCreator.createInstance(typeClass,depth);
		setLocalInstanceToBeCreated(false);
		return createdObject;
	}
	
	protected static ReadVariableAction createReadVariableAction(String variableName,Type variableType)
	{
		ReadVariableAction readVariableAction	=	(ReadVariableAction)
				currentActivity.createOwnedNode("get_"+variableName,UMLPackage.Literals.READ_VARIABLE_ACTION);
	
		readVariableAction.setVariable(currentActivity.getVariable(variableName,variableType));
		
		
		return readVariableAction;
	
	}
	
	protected static ReadStructuralFeatureAction createReadStructuralFeatureAction(ModelClass targetClass, String fieldName, Type valueType)
	{
		String targetName=getObjectIdentifier(targetClass);
		Type targetType=ModelImporter.importType(targetClass.getClass());
		String fieldQualifiedName=targetName+"."+fieldName;
		ReadStructuralFeatureAction readStrFeatAction = (ReadStructuralFeatureAction) 
				currentActivity.createOwnedNode("get_"+fieldQualifiedName,UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION);
		
		Property field=getClassField(targetClass,fieldName);
		readStrFeatAction.setStructuralFeature(field);
		
		ValuePin rsfa_object=(ValuePin) readStrFeatAction.createObject(readStrFeatAction.getName()+"_input",targetType,UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(rsfa_object,targetName,targetType);
		
		return readStrFeatAction;
	}
	
	protected static void setVariableValue(ModelIdentifiedElement target, String valueExpression)
	{
		String targetInstanceName=getObjectIdentifier(target);
		Type type=ModelImporter.importType(target.getClass());
		Variable variable=currentActivity.getVariable(targetInstanceName, type);
		
		AddVariableValueAction addVarValAction = (AddVariableValueAction)
				currentActivity.createOwnedNode(targetInstanceName+":="+valueExpression, UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		addVarValAction.setVariable(variable);
		
		ValuePin valuePin = (ValuePin) addVarValAction.createValue(addVarValAction.getName()+"_value",type,UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(valuePin,valueExpression,type);
		
		createControlFlowBetweenNodes(lastNode,addVarValAction);
		lastNode=addVarValAction;
		
	}
	protected static void setStructuralFeatureValue(ModelClass targetClass, String fieldName,String valueExpression, Type valueType)
	{
		String targetName=getObjectIdentifier(targetClass);
		Type targetType=ModelImporter.importType(targetClass.getClass());
		String fieldQualifiedName=targetName+"."+fieldName;
		AddStructuralFeatureValueAction addStrFeatValAction = (AddStructuralFeatureValueAction) 
				currentActivity.createOwnedNode(fieldQualifiedName+":="+valueExpression,UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION);
		
		Property field=getClassField(targetClass,fieldName);
		addStrFeatValAction.setStructuralFeature(field);
		addStrFeatValAction.setIsReplaceAll(true);
		
		ValuePin asfva_object=(ValuePin) addStrFeatValAction.createObject(addStrFeatValAction.getName()+"_input",targetType,UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(asfva_object,targetName,targetType);
		

		ValuePin asfva_value=(ValuePin) addStrFeatValAction.createValue(addStrFeatValAction.getName()+"_value",valueType,UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(asfva_value,valueExpression,valueType);
		
		createControlFlowBetweenNodes(lastNode,addStrFeatValAction);
		lastNode=addStrFeatValAction;
	}
	
	protected static Property getClassField(ModelClass target, String fieldName)
	{
		org.eclipse.uml2.uml.Class uml2Class = (org.eclipse.uml2.uml.Class) 
				currentModel.getOwnedMember(target.getClass().getSimpleName());
		
		for(Property field:uml2Class.getAllAttributes())
		{
			if(field.getName().equals(fieldName))
			{
				return field;
			}
		}
		return null;
	}
	
	protected static boolean isObjectAFieldOfSelf(ModelIdentifiedElement object)
	{
		if(self!=null)
		{
			for(Field f:self.getClass().getDeclaredFields())
			{
				try {
					f.setAccessible(true);
					Object fieldObj = f.get(self);
					f.setAccessible(false);
					if(fieldObj instanceof ModelIdentifiedElement)
					{
						if( ( (ModelIdentifiedElement) fieldObj ).getIdentifier().equals( object.getIdentifier() ) )
						{
							return true;
						}
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		return false;
	}
	
	protected static String getObjectIdentifier(ModelIdentifiedElement object)
	{

		
		try
		{
			if(object.getIdentifier()==self.getIdentifier())
			{
				return "self";
			}
			for(Field f:self.getClass().getDeclaredFields())
			{
				try {
					f.setAccessible(true);
					Object fieldObj = f.get(self);
					f.setAccessible(false);
					if(fieldObj instanceof ModelIdentifiedElement)
					{
						if( ( (ModelIdentifiedElement) fieldObj ).getIdentifier().equals( object.getIdentifier() ) )
						{
							return "self."+f.getName();
						}
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		}
		catch(NullPointerException e)
		{
			
		}
		
		try
		{
			int i=0;
			for(Object param: currentParameters)
			{
				try
				{
					ModelIdentifiedElement p=(ModelIdentifiedElement) param;
					try
					{
						if(p.getIdentifier().equals(object.getIdentifier()))
						{
							return "arg"+i;
						}
					}
					catch(NullPointerException e)
					{
						
					}
				}
				catch(ClassCastException e)
				{
					e.printStackTrace();
				}
				++i;
			}
		}
		catch(NullPointerException e)
		{
			
		}
		
		
		
		try
		{
			return object.getIdentifier();
		}
		catch(NullPointerException e)
		{
			
		}
		return null;
		
	}
	
	protected static void createFlowBetweenNodes(ActivityNode source, ActivityNode target)
	{
		if(source instanceof ObjectNode || target instanceof ObjectNode)
		{
			createObjectFlowBetweenNodes(source,target);
		}
		else
		{
			createControlFlowBetweenNodes(source,target);
		}
	}
	
	protected static ForkNode createForkNode(String name, ActivityNode node1, ActivityNode node2)
	{
		ForkNode result=(ForkNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.FORK_NODE);
		
		createFlowBetweenNodes(result,node1);
		createFlowBetweenNodes(result,node2);

		return result;
	 }
	
	 protected static MergeNode createMergeNode(String name,ActivityNode node1,ActivityNode node2)
	 {
		 MergeNode result=(MergeNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.MERGE_NODE);
		 createControlFlowBetweenNodes(node1,result);
		 createControlFlowBetweenNodes(node2,result);
		 return result;
	 }

	protected static ActivityEdge createControlFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("controlflow_from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.CONTROL_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}
	
	protected static ActivityEdge createObjectFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("objectflow_from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.OBJECT_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}
	
	protected static ValuePin addOpaqueExpressionToValuePin(ValuePin pin,String expression, Type type)
    {
    	OpaqueExpression opaqueExpression=(OpaqueExpression) pin.createValue(pin.getName()+"_expression",type,UMLPackage.Literals.OPAQUE_EXPRESSION);
		opaqueExpression.getBodies().add(expression);
		return pin;
    }
	

	protected static Operation getOperation(org.eclipse.uml2.uml.Class ownerClass,String name)
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
	
	protected static boolean importing=false;
	protected static Object[] currentParameters=null;
	protected static Method currentMethod=null;
	protected static ActivityNode lastNode=null;
	protected static Activity currentActivity=null;
	protected static ModelClass self=null;
	protected static Model currentModel=null;
}
