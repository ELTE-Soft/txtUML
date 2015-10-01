package hu.elte.txtuml.export.uml2.transform.importers.controls.loops;

import hu.elte.txtuml.export.uml2.transform.importers.AbstractActionCodeImporter;
import hu.elte.txtuml.export.uml2.transform.importers.IControlStructImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

public class ForActionImporter extends AbstractLoopImporter implements
		IControlStructImporter {

	protected ForStatement currentStatement;
	
	public ForActionImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}
	
	@Override
	protected MethodBodyImporter importBodyFromStatement(Statement statement)
	{
		String condExpr = this.getExpressionString( this.currentStatement.getExpression());
		Type condExprType = this.getExpressionType( this.currentStatement.getExpression());
		/*
		 * Adding conditions
		 */
		this.addTestsToLoopVar(this.currentNode.getTests(), condExpr, condExprType);
		
		/*
		 * Importing loopBody  
		 */
		MethodBodyImporter imp = super.importBodyFromStatement(statement);
		
		/*
		 * preparing to add update to the end of the loop body
		 */
		EList<ExecutableNode> currentSeqBody = loopBody.getExecutableNodes(); 
		
		Variable loopVar = this.currentNode.getVariables().get(0);
		String updateExpr = this.getExpressionString( (Expression) this.currentStatement.updaters().get(0) );
		Type updateExprType = this.getExpressionType( (Expression) this.currentStatement.updaters().get(0) );
		
		this.updateLoopVariable(currentSeqBody, loopVar, updateExpr, updateExprType);
		
		return imp;
	}
	
	protected void initLoopVariable(String initExpr,Type initExprType)
	{
		Variable loopVar = this.currentNode.createVariable("LOOP_VAR_" + this.currentNode.getName(), initExprType);
		AddVariableValueAction writeVar = (AddVariableValueAction)this.currentNode.createNode("INIT_LOOP_VAR_" + this.currentNode.getName(), UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		writeVar.setVariable(loopVar);
		
		ValuePin valuePin = (ValuePin) writeVar.createValue(writeVar.getName()+"_value",initExprType,UMLPackage.Literals.VALUE_PIN);
		this.createAndAddOpaqueExpressionToValuePin(valuePin, initExpr, initExprType);
		
		this.createEdgeBetweenActivityNodes(this.getLastNode(), writeVar);
		this.setLastNode(writeVar);
	}
	
	protected void updateLoopVariable(EList<ExecutableNode> loopSeqBody,Variable loopVar,String updateExpr,Type updateExpressionType)
	{
		ReadVariableAction readVar = (ReadVariableAction)this.currentNode.createNode("UPDATE_LOOP_VAR_" + this.currentNode.getName(), UMLPackage.Literals.READ_VARIABLE_ACTION);
		readVar.setVariable(loopVar);
		
		readVar.createResult("READ_VAL_" + readVar.getName(), updateExpressionType);
		
		loopSeqBody.add(readVar);
		
		AddVariableValueAction writeVar = (AddVariableValueAction)this.currentNode.createNode("INIT_LOOP_VAR_" + this.currentNode.getName(), UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		this.createEdgeBetweenActivityNodes(readVar, writeVar);
		
		writeVar.setVariable(loopVar);
		
		ValuePin valuePin = (ValuePin) writeVar.createValue(writeVar.getName()+"_value",updateExpressionType,UMLPackage.Literals.VALUE_PIN);
		this.createAndAddOpaqueExpressionToValuePin(valuePin, updateExpr, updateExpressionType);
		
		loopSeqBody.add(writeVar);	
	}

	@Override
	public void importControlStructure(Statement statement) {
		ForStatement forState = (ForStatement)statement;
		
		String initExpr 	= this.getExpressionString( (Expression)forState.initializers().get(0) );
		Type   initExprType = this.getExpressionType( (Expression)forState.initializers().get(0) );
		
		String condExpr	=	this.getExpressionString(forState.getExpression());
		Type condExprType = this.getExpressionType(forState.getExpression());
		String name 	= 	"LoopNode_" + this.hashCode();
		
		this.currentNode = (LoopNode)this.activity.createOwnedNode(name, UMLPackage.Literals.LOOP_NODE);
		this.currentStatement = forState;
		
		this.initLoopVariable(initExpr, initExprType);
		
		this.createEdgeBetweenActivityNodes(this.getLastNode(),this.currentNode);
		
		OutputPin pin = this.currentNode.createResult("Decider_" + this.hashCode(), condExprType);
		
		OpaqueExpression lowerOpaqueExpression = (OpaqueExpression) pin.createLowerValue(
				pin.getName() + "_expression", condExprType,
				UMLPackage.Literals.OPAQUE_EXPRESSION);
		
		lowerOpaqueExpression.getBodies().add(condExpr);
		
		OpaqueExpression UpperOpaqueExpression = (OpaqueExpression) pin.createUpperValue(
				pin.getName() + "_expression", condExprType,
				UMLPackage.Literals.OPAQUE_EXPRESSION);
		
		UpperOpaqueExpression.getBodies().add(condExpr);
		
		pin.createNameExpression(condExpr, condExprType);
		
		this.currentNode.setDecider(pin);
		
		//this.setLastNode(this.currentNode);
		
		++AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.importBodyFromStatement(forState.getBody());
		
		--AbstractActionCodeImporter.blockBodiesBeingImported;

		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(this.currentNode);
		
		//this.setLastNode(this.currentNode);
		
	}
}
