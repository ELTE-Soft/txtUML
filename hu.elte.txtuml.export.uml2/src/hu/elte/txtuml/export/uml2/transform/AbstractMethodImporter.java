package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.Event;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;
import hu.elte.txtuml.export.uml2.transform.backend.UMLPrimitiveTypesProvider;
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider;

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
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

/**
 * Represents an importer that imports methods or instructions/actions/etc. in method bodies.
 * @author Ádám Ancsin
 *
 */
abstract class AbstractMethodImporter extends AbstractImporter {

	/**
	 * Creates an activity variable for the specified instance with the given UML2 type.
	 * @param instance The specified instance.
	 * @param type The given UML2 type of the instance.
	 * @return The created variable.
	 *
	 * @author Ádám Ancsin
	 */
	protected static Variable createVariableForInstance(Object instance, Type type)
	{
		Class<?> typeClass = instance.getClass();
		int lowerBound = MultiplicityProvider.getLowerBound(typeClass);
		int upperBound = MultiplicityProvider.getUpperBound(typeClass);
		Variable variable = currentActivity.createVariable(getObjectIdentifier(instance), type);
		variable.setLower(lowerBound);
		variable.setUpper(upperBound);
		
		return variable;
	}

	/**
	 * Sets the value of a variable in an activity. If the variable not yet exists, it creates the variable.
	 * @param target The target dummy instance.
	 * @param valueExpression The expression of the value to be assigned.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void setVariableValue(Object target, String valueExpression)
	{
		String targetInstanceName = getObjectIdentifier(target);
		
		Type type=ModelImporter.importType(target.getClass());

		Variable variable=currentActivity.getVariable(targetInstanceName, type);
		if(variable==null)
			variable=createVariableForInstance(target,type);
	
		AddVariableValueAction addVarValAction = createAddVarValAction(variable,targetInstanceName+":="+valueExpression);

		ValuePin valuePin = (ValuePin) 
				addVarValAction.createValue(addVarValAction.getName()+"_value",type,UMLPackage.Literals.VALUE_PIN);
		
		valuePin.setLower(variable.getLower());
		valuePin.setUpper(variable.getLower());
		createAndAddOpaqueExpressionToValuePin(valuePin,valueExpression,type);

		createControlFlowBetweenActivityNodes(lastNode,addVarValAction);
		lastNode=addVarValAction;
	}
	
	/**
	 * Sets the value of a structural feature. (a field of a model class)
	 * @param targetClass The dummy instance of the target model class.
	 * @param fieldName The name of the field.
	 * @param value The dummy instance of the value to be assigned.
	 * @param valueType The UML2 type of the new value.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void setStructuralFeatureValue
		(ModelClass targetClass, String fieldName, ModelElement value, Type valueType) 
	{
		String targetName = getObjectIdentifier(targetClass);
		String valueExpr = getExpression(value);
		Type targetType = ModelImporter.importType(targetClass.getClass());
		String fieldQualifiedName = targetName + "." + fieldName;
		String actionName = fieldQualifiedName + ":=" + valueExpr;
		
		AddStructuralFeatureValueAction addStrFeatValAction = (AddStructuralFeatureValueAction) currentActivity
				.createOwnedNode(actionName,
						UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION);

		Property property = getClassProperty(targetClass, fieldName);
		addStrFeatValAction.setStructuralFeature(property);
		addStrFeatValAction.setIsReplaceAll(true);

		ValuePin objectPin = (ValuePin) addStrFeatValAction.createObject(
				actionName + "_input", 
				targetType,
				UMLPackage.Literals.VALUE_PIN
			);
		createAndAddOpaqueExpressionToValuePin(objectPin, targetName, targetType);

		ValuePin valuePin = (ValuePin) addStrFeatValAction.createValue(
				actionName + "_value", valueType,
				UMLPackage.Literals.VALUE_PIN
			);
		createAndAddValueExpressionToValuePin(valuePin, value, valueType);

		createControlFlowBetweenActivityNodes(lastNode, addStrFeatValAction);
		lastNode = addStrFeatValAction;
	}

	/**
	 * Gets the expression of a dummy instance.
	 * @param instance The dummy instance
	 * @return The expression.
	 *
	 * @author Ádám Ancsin
	 */
	protected static String getExpression(Object instance)
	{
		String expression=null;
		InstanceInformation instInfo=InstanceManager.getInstanceInfo(instance);

		if(instInfo != null)
		{
			if(instInfo.isLiteral())
				expression = getLiteralExpression(instance,instInfo);
			else if(instInfo.isCalculated() && currentActivity == null)
				expression = "("+instInfo.getExpression()+")";
			else
				expression = instInfo.getExpression();
		}
		else if(instance instanceof ModelClass)
			expression = ((ModelClass) instance).getIdentifier();
		else
			expression = "inst_"+System.identityHashCode(instance);
		
		return expression;
	}

	/**
	 * Gets the identifier of a dummy instance.
	 * @param instance The dummy instance.
	 * @return The identifier.
	 *
	 * @author Ádám Ancsin
	 */
	protected static String getObjectIdentifier(Object instance)
	{
		String identifier=null;
		InstanceInformation instInfo=InstanceManager.getInstanceInfo(instance);
		
		if(instInfo != null && !instInfo.isLiteral() && !instInfo.isCalculated())
			identifier = instInfo.getExpression();
		else if(instance instanceof ModelClass)
			identifier = ((ModelClass) instance).getIdentifier();
		else
			identifier = "inst_"+System.identityHashCode(instance);
		
		return identifier;
	}

	/**
	 * Creates a fork node (and the necessary flows) to the given two nodes.
	 * 
	 * @param name The name of the fork node.
	 * @param node1 The first node to fork to.
	 * @param node2 The second node to fork tSo.
	 * @return The created fork node.
	 *
	 * @author Ádám Ancsin
	 */
	protected static ForkNode forkToNodes(String name, ActivityNode node1, ActivityNode node2)
	{
		ForkNode result=(ForkNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.FORK_NODE);

		createEdgeBetweenActivityNodes(result,node1);
		createEdgeBetweenActivityNodes(result,node2);

		return result;
	}

	/**
	 * Creates a join node (and the necessary flows) to join the two given nodes.
	 * @param node1 The first node to join.
	 * @param node2 The second node to join.
	 * @return The created join node.
	 *
	 * @author Ádám Ancsin
	 */
	protected static JoinNode joinNodes(ActivityNode node1,ActivityNode node2)
	{
		String name="join_"+node1.getName()+"_and_"+node2.getName();
		JoinNode result=(JoinNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.JOIN_NODE);
		createControlFlowBetweenActivityNodes(node1,result);
		createControlFlowBetweenActivityNodes(node2,result);
		return result;
	}

	/**
	 * Creates a merge node (and the necessary flows) to merge the two given nodes.
	 * 
	 * @param node1 The first node to merge.
	 * @param node2 The second node to merge.
	 * @return The created merge node.
	 *
	 * @author Ádám Ancsin
	 */
	protected static MergeNode createMergeNode(ActivityNode node1,ActivityNode node2)
	{
		String name="merge_"+node1.getName()+"_and_"+node2.getName();
		MergeNode result=(MergeNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.MERGE_NODE);
		createControlFlowBetweenActivityNodes(node1,result);
		createControlFlowBetweenActivityNodes(node2,result);
		return result;
	}

	/**
	 * Creates an activity edge from the source activity node to the target activity node.
	 * If one of the nodes is an object node, the edge will be an object flow. Otherwise, it
	 * will be a control flow.
	 * 
	 * @param source The source activity node.
	 * @param target The target activity node.
	 * @return The created activity edge.
	 *
	 * @author Ádám Ancsin
	 */
	protected static ActivityEdge createEdgeBetweenActivityNodes(ActivityNode source, ActivityNode target)
	{
		if(source instanceof ObjectNode || target instanceof ObjectNode)
			return createObjectFlowBetweenActivityNodes(source,target);
		else
			return createControlFlowBetweenActivityNodes(source,target);
	}

	/**
	 * Creates a control flow from the source activity node to the target activity node.
	 * 
	 * @param source The source activity node.
	 * @param target The target activity node.
	 * @return The created control flow.
	 *
	 * @author Ádám Ancsin
	 */
	protected static ActivityEdge createControlFlowBetweenActivityNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge(
				"controlflow_from_"+source.getName()+"_to_"+target.getName(),
				UMLPackage.Literals.CONTROL_FLOW
			);
		
		edge.setSource(source);
		edge.setTarget(target);

		if(cntBlockBodiesBeingImported>0 && blockBodyFirstEdges.size()<cntBlockBodiesBeingImported)
			blockBodyFirstEdges.push(edge);
		
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

	/**
	 * Creates an object flow from the source activity node to the target activity node.
	 * 
	 * @param source The source activity node.
	 * @param target The target activity node.
	 * @return The created object flow.
	 *
	 * @author Ádám Ancsin
	 */
	protected static ActivityEdge createObjectFlowBetweenActivityNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge(
				"objectflow_from_"+source.getName()+"_to_"+target.getName(),
				UMLPackage.Literals.OBJECT_FLOW
			);
		edge.setSource(source);
		edge.setTarget(target);
		
		if(cntBlockBodiesBeingImported>0 && blockBodyFirstEdges.size()<cntBlockBodiesBeingImported)
			blockBodyFirstEdges.push(edge);
		
		return edge;
	}

	/**
	 * Creates and adds an opaque expression to a value pin.
	 * @param pin The value pin.
	 * @param expression The expression the created opaque expression is based on.
	 * @param type The UML2 type of the value represented by the expression.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void createAndAddOpaqueExpressionToValuePin(ValuePin pin,String expression, Type type)
	{
		OpaqueExpression opaqueExpression = (OpaqueExpression)
				pin.createValue(pin.getName()+"_expression",type,UMLPackage.Literals.OPAQUE_EXPRESSION);
		opaqueExpression.getBodies().add(expression);
	}
	
	/**
	 * Creates and adds a value expression (opaque expression or literal string) to a value pin. 
	 * If the value is a ModelString literal, the expression will be a literal string, 
	 * otherwise, it will be an opaque expression.
	 * 
	 * @param pin The value pin.
	 * @param value The value the expression is based on.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void createAndAddValueExpressionToValuePin(ValuePin pin, ModelElement value)
	{
		String expression=getExpression(value);
		
		if(value instanceof ModelString)
		{
			if(isStringLiteral(value))
				createAndAddLiteralStringToValuePin(pin,expression);
			else
			{
				Type type=ModelImporter.importType(value.getClass());
				createAndAddOpaqueExpressionToValuePin(pin,expression, type);
			}
		}
		else
		{
			Type type=ModelImporter.importType(value.getClass());
			createAndAddOpaqueExpressionToValuePin(pin,expression, type);
		}
		
	}

	/**
	 * Creates and adds a value expression (opaque expression or literal string) to a value pin. 
	 * If the value is a ModelString literal, the expression will be a literal string, 
	 * otherwise, it will be an opaque expression.
	 * 
	 * @param pin The value pin.
	 * @param value The value the expression is based on.
	 * @param type The UML2 type of the value.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void createAndAddValueExpressionToValuePin(ValuePin pin, ModelElement value, Type type)
	{
		String expression=getExpression(value);
		
		if(value instanceof ModelString)
		{
			if(isStringLiteral(value))
				createAndAddLiteralStringToValuePin(pin,expression);
			else
				createAndAddOpaqueExpressionToValuePin(pin,expression, type);
		}
		else
			createAndAddOpaqueExpressionToValuePin(pin,expression, type);
	}
	
	/**
	 * Creates an add variable value action with a given name for a variable.
	 * @param var The variable.
	 * @param name The name of the created action.
	 * @return The created action.
	 *
	 * @author Ádám Ancsin
	 */
	protected static AddVariableValueAction createAddVarValAction(Variable var, String name)
	{
		AddVariableValueAction addVarValAction = (AddVariableValueAction)
				currentActivity.createOwnedNode(name, UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);

		addVarValAction.setVariable(var);

		return addVarValAction;
	}

	/**
	 * Adds a guard to an activity edge.
	 * 
	 * @param edge The activity edge.
	 * @param expression The guard expression.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void addGuardToActivityEdge(ActivityEdge edge, String expression)
	{
		OpaqueExpression opaqueExpression=(OpaqueExpression) UMLFactory.eINSTANCE.createOpaqueExpression();
		opaqueExpression.getBodies().add(expression);
		edge.setGuard(opaqueExpression);
	}
	
	/**
	 * Creates and adds a literal string to a value pin.
	 * @param pin The value pin.
	 * @param expr The expression which represents the value of the literal string.
	 *
	 * @author Ádám Ancsin
	 */
	private static void createAndAddLiteralStringToValuePin(ValuePin pin, String expr)
	{
		LiteralString literal = (LiteralString)	pin.createValue(
				pin.getName()+"_expression",
				UMLPrimitiveTypesProvider.getString(),
				UMLPackage.Literals.LITERAL_STRING
			);
		
		literal.setValue(expr);
	}
	
	/**
	 * Decides the given object is a string literal or not.
	 * 
	 * @param object The given object.
	 * @return The "decision".
	 *
	 * @author Ádám Ancsin
	 */
	private static boolean isStringLiteral(ModelElement object)
	{
		if(!(object instanceof ModelString)) 
			return false;
		else
		{
			ModelString modelString=(ModelString) object;
			InstanceInformation info=InstanceManager.getInstanceInfo(modelString);
			
			if(info == null) 
				return false;
			else
				return info.isLiteral();
			
		}
	}

	/**
	 * Gets the UML2 property with a given field name of a given model class.
	 * @param target The dummy instance of the model class.
	 * @param fieldName The name of the field.
	 * @return The obtained UML2 property.
	 *
	 * @author Ádám Ancsin
	 */
	private static Property getClassProperty(ModelClass target, String fieldName)
	{
		org.eclipse.uml2.uml.Class uml2Class = (org.eclipse.uml2.uml.Class) 
				currentModel.getOwnedMember(target.getClass().getSimpleName());

		if(uml2Class == null)
			return null;
		
		for(Property field:uml2Class.getAllAttributes())
		{
			if(field.getName().equals(fieldName))
				return field;
		}
		
		return null;
	}

	/**
	 * Gets the value expression of a given literal.
	 * @param instance The dummy instance of the element.
	 * @param instInfo The instance information of the element.
	 * @return The obtained expression.
	 *
	 * @author Ádám Ancsin
	 */
	private static String getLiteralExpression(Object instance, InstanceInformation instInfo)
	{
		String expression=null;
		if(instance instanceof ModelInt)
		{
			Integer val=Integer.parseInt(instInfo.getExpression());
			if(val<0)
				expression= "("+val.toString()+")";
			else
				expression=val.toString();
		}
		else
			expression=instInfo.getExpression();
		
		return expression;
	}

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
