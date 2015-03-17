package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.Event;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelIdentifiedElement;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.UMLPrimitiveTypes;

import java.lang.reflect.Method;
import java.util.Stack;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

abstract class AbstractMethodImporter extends AbstractImporter {

	public static boolean isImporting() {
		return importing;
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
		if(variable==null)
		{
			variable=currentActivity.createVariable(targetInstanceName,type);
		}

		AddVariableValueAction addVarValAction = createAddVarValAction(variable,targetInstanceName+":="+valueExpression);

		ValuePin valuePin = (ValuePin) addVarValAction.createValue(addVarValAction.getName()+"_value",type,UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(valuePin,valueExpression,type);

		createControlFlowBetweenNodes(lastNode,addVarValAction);
		lastNode=addVarValAction;

	}
	protected static void setStructuralFeatureValue(ModelClass targetClass, String fieldName,ModelIdentifiedElement value, Type valueType)
	{
		String targetName=getObjectIdentifier(targetClass);
		String valueExpr=getExpression(value);
		Type targetType=ModelImporter.importType(targetClass.getClass());
		String fieldQualifiedName=targetName+"."+fieldName;
		AddStructuralFeatureValueAction addStrFeatValAction = (AddStructuralFeatureValueAction) 
				currentActivity.createOwnedNode(fieldQualifiedName+":="+valueExpr,UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION);

		Property field=getClassField(targetClass,fieldName);
		addStrFeatValAction.setStructuralFeature(field);
		addStrFeatValAction.setIsReplaceAll(true);

		ValuePin asfva_object=(ValuePin) addStrFeatValAction.createObject(addStrFeatValAction.getName()+"_input",targetType,UMLPackage.Literals.VALUE_PIN);
		addOpaqueExpressionToValuePin(asfva_object,targetName,targetType);


		ValuePin asfva_value=(ValuePin) addStrFeatValAction.createValue(addStrFeatValAction.getName()+"_value",valueType,UMLPackage.Literals.VALUE_PIN);
		addExpressionToValuePin(asfva_value,value,valueType);

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
		String objectName = getObjectIdentifier(object);
		
		return objectName.startsWith("self");
	}

	private static String getModelTypeLiteralExpression(ModelElement instance, InstanceInformation instInfo)
	{
		String expression=null;
		if(instance instanceof ModelInt)
		{
			Integer val=Integer.parseInt(instInfo.getExpression());
			if(val<0)
			{
				expression= "("+val.toString()+")";
			}
			else
			{
				expression=val.toString();
			}
		}
		else
		{
			expression=instInfo.getExpression();
		}
		return expression;
	}


	protected static void createLocalFieldsRecursively(Object classifier)
  	{
  		createFieldsRecursively(classifier, true);
  	}

	protected static String getExpression(ModelIdentifiedElement instance)
	{
		String expression=null;
		InstanceInformation instInfo=getInstanceInfo(instance);

		if(instInfo != null)
		{
			if(instInfo.isLiteral())
				expression = getModelTypeLiteralExpression(instance,instInfo);
			else if(instInfo.isCalculated() && currentActivity == null)
				expression = "("+instInfo.getExpression()+")";
			else
				expression = instInfo.getExpression();
		}
		else if(instance instanceof ModelClass)
			expression = instance.getIdentifier();
		else
			expression = "inst_"+System.identityHashCode(instance);
		return expression;
	}

	protected static String getObjectIdentifier(ModelIdentifiedElement instance)
	{
		String expression=null;
		InstanceInformation instInfo=getInstanceInfo(instance);
		
		if(instInfo != null && !instInfo.isLiteral() && !instInfo.isCalculated())
			expression = instInfo.getExpression();
		else if(instance instanceof ModelClass)
			expression = instance.getIdentifier();
		else
			expression = "inst_"+System.identityHashCode(instance);
		
		return expression;
	}

	protected static ForkNode createForkNode(String name, ActivityNode node1, ActivityNode node2)
	{
		ForkNode result=(ForkNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.FORK_NODE);

		createFlowBetweenNodes(result,node1);
		createFlowBetweenNodes(result,node2);

		return result;
	}

	protected static JoinNode createJoinNode(ActivityNode node1,ActivityNode node2)
	{
		String name="join_"+node1.getName()+"_and_"+node2.getName();
		JoinNode result=(JoinNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.JOIN_NODE);
		createControlFlowBetweenNodes(node1,result);
		createControlFlowBetweenNodes(node2,result);
		return result;
	}

	protected static MergeNode createMergeNode(ActivityNode node1,ActivityNode node2)
	{
		String name="merge_"+node1.getName()+"_and_"+node2.getName();
		MergeNode result=(MergeNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.MERGE_NODE);
		createControlFlowBetweenNodes(node1,result);
		createControlFlowBetweenNodes(node2,result);
		return result;
	}

	protected static ActivityEdge createFlowBetweenNodes(ActivityNode source, ActivityNode target)
	{
		if(source instanceof ObjectNode || target instanceof ObjectNode)
		{
			return createObjectFlowBetweenNodes(source,target);
		}
		else
		{
			return createControlFlowBetweenNodes(source,target);
		}
	}

	protected static ActivityEdge createControlFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("controlflow_from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.CONTROL_FLOW);
		edge.setSource(source);
		edge.setTarget(target);

		if(cntBlockBodiesBeingImported>0 && blockBodyFirstEdges.size()<cntBlockBodiesBeingImported)
		{
			blockBodyFirstEdges.push(edge);
		}
		if(source.equals(lastNode) && !unfinishedDecisionNodes.empty())
		{
			DecisionNode top=unfinishedDecisionNodes.peek();
			if(top.equals(lastNode))
			{
				unfinishedDecisionNodes.pop();
				addGuardToActivityEdge(edge,"else");
			}
		}
		return edge;
	}

	protected static ActivityEdge createObjectFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("objectflow_from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.OBJECT_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		if(cntBlockBodiesBeingImported>0 && blockBodyFirstEdges.size()<cntBlockBodiesBeingImported)
		{
			blockBodyFirstEdges.push(edge);
		}
		return edge;
	}

	protected static ValuePin addOpaqueExpressionToValuePin(ValuePin pin,String expression, Type type)
	{
		OpaqueExpression opaqueExpression=(OpaqueExpression) pin.createValue(pin.getName()+"_expression",type,UMLPackage.Literals.OPAQUE_EXPRESSION);
		opaqueExpression.getBodies().add(expression);
		return pin;
	}
	
	private static void addStringLiteralToValuePin(ValuePin pin, String expr)
	{
		LiteralString literal = (LiteralString)
				pin.createValue(
						pin.getName()+"_expression",
						UMLPrimitiveTypes.getString(),
						UMLPackage.Literals.LITERAL_STRING
					);
		
		literal.setValue(expr);
	}
	
	private static boolean isStringLiteral(ModelIdentifiedElement object)
	{
		if(!(object instanceof ModelString)) return false;
		else
		{
			ModelString modelString=(ModelString) object;
			InstanceInformation info=getInstanceInfo(modelString);
			if(info == null) return false;
			else
			{
				return info.isLiteral();
			}
			
		}
		
	}
	protected static void addExpressionToValuePin(ValuePin pin, ModelIdentifiedElement value)
	{
		String expression=getExpression(value);
		
		if(value instanceof ModelString)
		{
			if(isStringLiteral(value))
			{
				addStringLiteralToValuePin(pin,expression);
			}
			else
			{
				Type type=ModelImporter.importType(value.getClass());
				addOpaqueExpressionToValuePin(pin,expression, type);
			}
		}
		else
		{
			Type type=ModelImporter.importType(value.getClass());
			addOpaqueExpressionToValuePin(pin,expression, type);
		}
		
	}

	protected static void addExpressionToValuePin(ValuePin pin, ModelIdentifiedElement value, Type type)
	{
		String expression=getExpression(value);
		
		if(value instanceof ModelString)
		{
			if(isStringLiteral(value))
			{
				addStringLiteralToValuePin(pin,expression);
			}
			else
			{
				addOpaqueExpressionToValuePin(pin,expression, type);
			}
		}
		else
		{
			addOpaqueExpressionToValuePin(pin,expression, type);
		}
		
	}
	protected static AddVariableValueAction createAddVarValAction(Variable var, String name)
	{
		AddVariableValueAction addVarValAction = (AddVariableValueAction)
				currentActivity.createOwnedNode(name, UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);

		addVarValAction.setVariable(var);

		return addVarValAction;
	}

	protected static void addGuardToActivityEdge(ActivityEdge edge, String expression)
	{
		OpaqueExpression opaqueExpression=(OpaqueExpression) UMLFactory.eINSTANCE.createOpaqueExpression();
		opaqueExpression.getBodies().add(expression);
		edge.setGuard(opaqueExpression);
	}

	
	protected static boolean importing=false;
	protected static ModelElement[] currentParameters=null;
	protected static Method currentMethod=null;
	protected static ActivityNode lastNode=null;
	protected static Activity currentActivity=null;
	protected static Model currentModel=null;
	protected static int cntBlockBodiesBeingImported=0;
	protected static Stack<ActivityEdge> blockBodyFirstEdges=new Stack<>();
	protected static Stack<DecisionNode> unfinishedDecisionNodes=new Stack<>();
	protected static int cntDecisionNodes;
	protected static Event currentSignal=null;
}
