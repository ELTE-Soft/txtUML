package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.export.uml2.transform.importers.actions.CreateObjectActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.actions.DeleteObjectActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.actions.LinkActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.actions.SendActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.actions.StartActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.AssignmentImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.IfActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.NewActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.loops.ForActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.loops.ForEachActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.loops.WhileActionImporter;
import hu.elte.txtuml.export.uml2.transform.visitors.BlockVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.ExpressionVisitor;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

public class MethodBodyImporter {
	private final Activity activity;
	private final TypeImporter typeImporter;
	private final Model importedModel;
	private ActivityNode lastNode;
	private SequenceNode bodyNode;
	
	public MethodBodyImporter(Activity activity, TypeImporter typeImporter, Model importedModel){
		this.activity = activity;
		this.typeImporter = typeImporter;
		this.importedModel = importedModel;
		this.lastNode = null;
		
		this.bodyNode = (SequenceNode)this.activity.createOwnedNode("Body_Node_" + activity.getName(),UMLPackage.Literals.SEQUENCE_NODE);
	}
	
	public SequenceNode getBodyNode()
	{
		return this.bodyNode;
	}

	public void importAction(MethodInvocation methodInvocation) {
		String actionName = methodInvocation.getName().getFullyQualifiedName();
		
		IActionImporter actionImporter = null; 
		
		if(actionName.equals("send")) {
			actionImporter = new SendActionImporter(this, this.importedModel);
		}
		else if(actionName.equals("create"))
		{
			actionImporter = new CreateObjectActionImporter(this, this.importedModel);
		}
		else if(actionName.equals("delete"))
		{
			actionImporter = new DeleteObjectActionImporter(this, this.importedModel);
		}
		else if(actionName.equals("link"))
		{
			actionImporter = new LinkActionImporter(this, this.importedModel);
		}
		else if(actionName.equals("unlink"))
		{
			actionImporter = new LinkActionImporter(this, this.importedModel);
		}
		else if(actionName.equals("start"))
		{
			actionImporter = new StartActionImporter(this, this.importedModel);
		}
	
		if(actionImporter != null) {
			actionImporter.importFromMethodInvocation(methodInvocation);
		}
	}
	
	public void importControlStructure(Statement statement)
	{
		IControlStructImporter controlStructureImporter = null; 
		
		if(statement.getNodeType() == ASTNode.IF_STATEMENT)
		{
			controlStructureImporter = new IfActionImporter(this, this.importedModel);
		}
		else if(statement.getNodeType() == ASTNode.WHILE_STATEMENT)
		{
			controlStructureImporter = new WhileActionImporter(this,this.importedModel);
		}
		else if(statement.getNodeType() == ASTNode.FOR_STATEMENT)
		{
			controlStructureImporter = new ForActionImporter(this,this.importedModel);
		}
		else if(statement.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT)
		{
			controlStructureImporter = new ForEachActionImporter(this,this.importedModel);
		}
		else if(statement.getNodeType() == ASTNode.EXPRESSION_STATEMENT)
		{
			ExpressionStatement exprStatement = (ExpressionStatement)statement;

			if(exprStatement.getExpression().getNodeType() == ASTNode.ASSIGNMENT)
			{
				Assignment assignment = (Assignment)exprStatement.getExpression();
				if(assignment.getRightHandSide().getNodeType() == ASTNode.CLASS_INSTANCE_CREATION)
				{
					controlStructureImporter = new NewActionImporter(this,this.importedModel);
				}
				else
				{
					controlStructureImporter = new AssignmentImporter(this,this.importedModel);
				}
			}
		}
		
		if(controlStructureImporter != null) {
			controlStructureImporter.importControlStructure(statement);
		}
	}
	
	Activity getActivity() {
		return this.activity;
	}
	
	TypeImporter getTypeImporter() {
		return this.typeImporter;
	}
	
	/**
	 * Creates a fork node (and the necessary flows) to the given two nodes.
	 * 
	 * @param name
	 *            The name of the fork node.
	 * @param node1
	 *            The first node to fork to.
	 * @param node2
	 *            The second node to fork tSo.
	 * @return The created fork node.
	 *
	 * @author Adam Ancsin
	 */
	ForkNode forkToNodes(String name, ActivityNode node1, ActivityNode node2)
	{
		ForkNode result = (ForkNode) this.activity.createOwnedNode(name,UMLPackage.Literals.FORK_NODE);

		createEdgeBetweenActivityNodes(result,node1);
		createEdgeBetweenActivityNodes(result,node2);

		return result;
	}

	/**
	 * Creates a join node (and the necessary flows) to join the two given
	 * nodes.
	 * 
	 * @param node1
	 *            The first node to join.
	 * @param node2
	 *            The second node to join.
	 * @return The created join node.
	 *
	 * @author Adam Ancsin
	 */
	JoinNode joinNodes(ActivityNode node1, ActivityNode node2) {
		String name = "join_" + node1.getName() + "_and_" + node2.getName();
		JoinNode result = (JoinNode) this.activity.createOwnedNode(name,
				UMLPackage.Literals.JOIN_NODE);
		createControlFlowBetweenActivityNodes(node1, result);
		createControlFlowBetweenActivityNodes(node2, result);
		return result;
	}

	/**
	 * Creates a merge node (and the necessary flows) to merge the two given
	 * nodes.
	 * 
	 * @param node1
	 *            The first node to merge.
	 * @param node2
	 *            The second node to merge.
	 * @return The created merge node.
	 *
	 * @author Adam Ancsin
	 */
	MergeNode createMergeNode(ActivityNode node1, ActivityNode node2) {
		String name = "merge_" + node1.getName() + "_and_" + node2.getName();
		MergeNode result = (MergeNode) this.activity.createOwnedNode(name,
				UMLPackage.Literals.MERGE_NODE);
		createControlFlowBetweenActivityNodes(node1, result);
		createControlFlowBetweenActivityNodes(node2, result);
		return result;
	}

	/**
	 * Creates an activity edge from the source activity node to the target
	 * activity node. If one of the nodes is an object node, the edge will be an
	 * object flow. Otherwise, it will be a control flow.
	 * 
	 * @param source
	 *            The source activity node.
	 * @param target
	 *            The target activity node.
	 * @return The created activity edge.
	 *
	 * @author Adam Ancsin
	 */
	ActivityEdge createEdgeBetweenActivityNodes(ActivityNode source, ActivityNode target) {
		if(source instanceof ObjectNode || target instanceof ObjectNode) {
			return createObjectFlowBetweenActivityNodes(source,target);
		} else {
			return createControlFlowBetweenActivityNodes(source,target);	
		}
	}

	/**
	 * Creates a control flow from the source activity node to the target
	 * activity node.
	 * 
	 * @param source
	 *            The source activity node.
	 * @param target
	 *            The target activity node.
	 * @return The created control flow.
	 *
	 * @author Adam Ancsin
	 */
	ActivityEdge createControlFlowBetweenActivityNodes(ActivityNode source,	ActivityNode target) {
		ActivityEdge edge = this.activity.createEdge("controlflow_from_"
				+ source.getName() + "_to_" + target.getName(),
				UMLPackage.Literals.CONTROL_FLOW);

		edge.setSource(source);
		edge.setTarget(target);

		/*
		 * if(cntBlockBodiesBeingImported>0 &&
		 * blockBodyFirstEdges.size()<cntBlockBodiesBeingImported)
		 * blockBodyFirstEdges.push(edge);
		 * 
		 * if(source.equals(lastNode) && !unfinishedDecisionNodes.empty()) {
		 * DecisionNode top=unfinishedDecisionNodes.peek();
		 * if(top.equals(lastNode)) { unfinishedDecisionNodes.pop();
		 * addGuardToActivityEdge(edge,"else"); } }
		 */
		return edge;
	}

	/**
	 * Creates an object flow from the source activity node to the target
	 * activity node.
	 * 
	 * @param source
	 *            The source activity node.
	 * @param target
	 *            The target activity node.
	 * @return The created object flow.
	 *
	 * @author Adam Ancsin
	 */
	ActivityEdge createObjectFlowBetweenActivityNodes(ActivityNode source, ActivityNode target) {
		ActivityEdge edge = this.activity.createEdge("objectflow_from_"
				+ source.getName() + "_to_" + target.getName(),
				UMLPackage.Literals.OBJECT_FLOW);
		edge.setSource(source);
		edge.setTarget(target);

		/*
		 * if(cntBlockBodiesBeingImported>0 &&
		 * blockBodyFirstEdges.size()<cntBlockBodiesBeingImported)
		 * blockBodyFirstEdges.push(edge);
		 */

		return edge;
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
	 * @author Adam Ancsin
	 */
	void createAndAddValueExpressionToValuePin(ValuePin pin, Expression value, Type type)
	{
		ExpressionVisitor expressionVisitor = new ExpressionVisitor();
		value.accept(expressionVisitor);
		String expression = expressionVisitor.getImportedExpression();
		
		if(value instanceof StringLiteral) {
			createAndAddLiteralStringToValuePin(pin, expression, type);
		} else {
			createAndAddOpaqueExpressionToValuePin(pin, expression, type);
		}
	}
	
	public void createAndAddValueExpressionToValuePin(ValuePin pin, String expression, Type type) {
		createAndAddOpaqueExpressionToValuePin(pin, expression, type);
	}

	/**
	 * Creates and adds a literal string to a value pin.
	 * @param pin The value pin.
	 * @param expr The expression which represents the value of the literal string.
	 *
	 * @author Adam Ancsin
	 */
	private void createAndAddLiteralStringToValuePin(ValuePin pin, String expr, Type type)
	{
		LiteralString literal = (LiteralString)	pin.createValue(
				pin.getName()+"_expression", type, UMLPackage.Literals.LITERAL_STRING);
		
		literal.setValue(expr);
	}

	/**
	 * Creates and adds an opaque expression to a value pin.
	 * 
	 * @param pin
	 *            The value pin.
	 * @param expression
	 *            The expression the created opaque expression is based on.
	 * @param type
	 *            The UML2 type of the value represented by the expression.
	 *
	 * @author Adam Ancsin
	 */
	void createAndAddOpaqueExpressionToValuePin(ValuePin pin, String expression, Type type) {
		OpaqueExpression opaqueExpression = (OpaqueExpression) pin.createValue(
				pin.getName() + "_expression", type,
				UMLPackage.Literals.OPAQUE_EXPRESSION);
		opaqueExpression.getBodies().add(expression);
	}

	public void importOperationBody(Operation operation, MethodDeclaration methodDeclaration) {
		importMethodBodyWithParameters(methodDeclaration, operation.getOwnedParameters());
	}
	
	public void importMethodBody(MethodDeclaration methodDeclaration) {
		EList<Parameter> emptyParameterList = new BasicEList<>();
		importMethodBodyWithParameters(methodDeclaration, emptyParameterList);
	}
	
	private void importMethodBodyWithParameters(MethodDeclaration methodDeclaration, EList<Parameter> parameters) {
		ActivityNode initialNode = this.activity.createOwnedNode(
				"initialNode",UMLPackage.Literals.INITIAL_NODE);	
			
		this.lastNode = initialNode;
		
		parameters.forEach(param -> addParameterToActivity(param));
		
		Block methodBody = methodDeclaration.getBody();
		if(methodBody != null) {
			methodBody.accept(new BlockVisitor(this));
		}
		
		ActivityNode finalNode = this.activity.createOwnedNode("finalNode", UMLPackage.Literals.ACTIVITY_FINAL_NODE);
		createControlFlowBetweenActivityNodes(this.lastNode, finalNode);
	}
	
	/**
	 * Adds a parameter to the current activity. Creates an activity parameter node and a variable for the parameter. If
	 * the parameter is an input parameter, it reads the value of the parameter node and assigns it to the variable.
	 * 
	 * @param param The UML2 parameter.
	 *
	 * @author Adam Ancsin
	 */
	private void addParameterToActivity(Parameter param) {
		String paramName = param.getName();
		Type paramType = param.getType();
		Variable paramVar = this.activity.createVariable(paramName, paramType);
		
		ActivityParameterNode paramNode = this.createParameterNode(param);
		
		if(ParameterDirectionKind.IN_LITERAL.equals(param.getDirection())) {
			AddVariableValueAction addVarValAction = this.createAddVarValAction(paramVar, "set_" + paramName);
			
			InputPin inputPin = addVarValAction.createValue(
					addVarValAction.getName() + "_value", paramType, UMLPackage.Literals.INPUT_PIN);
			
			createObjectFlowBetweenActivityNodes(paramNode, inputPin);
			createControlFlowBetweenActivityNodes(this.lastNode, addVarValAction);
			this.lastNode = addVarValAction;
		}
	}
	
	/**
	 * Creates a parameter node in the current activity for a specified parameter.
	 * @param param The UML2 parameter.
	 * @return The created activity parameter node.
	 *
	 * @author Adam Ancsin
	 */
	private ActivityParameterNode createParameterNode(Parameter param) {
		String paramName = param.getName();
		Type paramType = param.getType();
		
		ActivityParameterNode paramNode= (ActivityParameterNode)
				this.activity.createOwnedNode(paramName + "_paramNode", UMLPackage.Literals.ACTIVITY_PARAMETER_NODE);
		
		paramNode.setParameter(param);
		paramNode.setType(paramType);
		
		return paramNode;
	}
	
	/**
	 * Creates an add variable value action with a given name for a variable.
	 * @param variable The variable.
	 * @param name The name of the created action.
	 * @return The created action.
	 *
	 * @author Adam Ancsin
	 */
	private AddVariableValueAction createAddVarValAction(Variable variable, String name) {
		AddVariableValueAction addVarValAction = (AddVariableValueAction)
				this.activity.createOwnedNode(name, UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		addVarValAction.setVariable(variable);
		
		return addVarValAction;
	}
	
	void setLastNode(ActivityNode lastNode) {
		this.lastNode = lastNode;
	}
	
	ActivityNode getLastNode() {
		return this.lastNode;
	}	
}
