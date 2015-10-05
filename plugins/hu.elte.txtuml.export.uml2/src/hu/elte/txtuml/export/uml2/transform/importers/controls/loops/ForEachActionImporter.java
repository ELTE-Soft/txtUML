package hu.elte.txtuml.export.uml2.transform.importers.controls.loops;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ExpansionNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.importers.AbstractActionCodeImporter;
import hu.elte.txtuml.export.uml2.transform.importers.IControlStructImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.AbstractControlStructureImporter;

public class ForEachActionImporter extends AbstractControlStructureImporter implements
		IControlStructImporter {

	protected ExpansionRegion currentNode;
	protected SequenceNode loopBody;
	
	public ForEachActionImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}

	@Override
	public void importControlStructure(Statement statement) {
		EnhancedForStatement forState = (EnhancedForStatement)statement;
	
		SingleVariableDeclaration parameter = forState.getParameter();
		Expression expression = forState.getExpression();
		Statement forEachBody	=	forState.getBody(); 
		
		Type exprType = this.getExpressionType(expression);
		String expr = this.getExpressionString(expression);
		
		ExpansionNode currentNode = (ExpansionNode)this.activity.createOwnedNode("LOOP_NODE_FOREACH_" + this.hashCode(), UMLPackage.Literals.EXPANSION_NODE);
		
		ExpansionRegion currentRegion = (ExpansionRegion)this.activity.createOwnedNode("LOOP_REGION_FOREACH_" + this.hashCode(), UMLPackage.Literals.EXPANSION_REGION );
		
		currentNode.setRegionAsInput(currentRegion);
		currentNode.setRegionAsOutput(currentRegion);
		
		this.currentNode = currentRegion;
		
		Variable loopVar = this.currentNode.createVariable("LOOP_VAR_" + this.currentNode.getName(), exprType);
		AddVariableValueAction writeVar = (AddVariableValueAction)this.currentNode.createNode("INIT_LOOP_VAR_" + this.currentNode.getName(), UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		writeVar.setVariable(loopVar);
		
		ValuePin valuePin = (ValuePin) writeVar.createValue(parameter.getName().toString() + "_" + this.hashCode(),exprType,UMLPackage.Literals.VALUE_PIN);
		this.createAndAddOpaqueExpressionToValuePin(valuePin, expr, exprType);
		
		this.createEdgeBetweenActivityNodes(this.getLastNode(), writeVar);
		this.setLastNode(writeVar);
		
		++AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.importBodyFromStatement(forEachBody);
		
		--AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(this.currentNode);
		
		//this.setLastNode(this.currentNode);
	}
	
	@Override
	protected MethodBodyImporter importBodyFromStatement(Statement statement)
	{
		MethodBodyImporter imp = super.importBodyFromStatement(statement);
			
		SequenceNode seqNode = imp.getBodyNode();
		
		this.currentNode.getNodes().add(seqNode);
		
		Action lastAction = (Action)seqNode.getExecutableNodes().get(seqNode.getExecutableNodes().size() - 1);
		
		this.currentNode.getOutputs().addAll(lastAction.getOutputs());
		this.loopBody = seqNode;
		
		return imp;
	}
}
