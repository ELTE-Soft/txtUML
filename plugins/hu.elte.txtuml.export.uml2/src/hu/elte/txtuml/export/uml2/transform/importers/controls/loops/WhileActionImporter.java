package hu.elte.txtuml.export.uml2.transform.importers.controls.loops;

import hu.elte.txtuml.export.uml2.transform.importers.AbstractActionCodeImporter;
import hu.elte.txtuml.export.uml2.transform.importers.IControlStructImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

public class WhileActionImporter extends AbstractLoopImporter implements
		IControlStructImporter {
	
	protected WhileStatement currentStatement;
	
	public WhileActionImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}
	
	@Override
	protected MethodBodyImporter importBodyFromStatement(Statement statement)
	{	
		String condExpr = this.getExpressionString(this.currentStatement.getExpression());
		Type condExprType = this.getExpressionType(this.currentStatement.getExpression());
		
		this.addTestsToLoopVar(this.currentNode.getTests(), condExpr, condExprType);
		
		MethodBodyImporter imp = super.importBodyFromStatement(statement);
		return imp;
	}

	@Override
	public void importControlStructure(Statement statement) {
		WhileStatement whileState = (WhileStatement)statement;
		
		this.currentStatement = whileState;
		
		String condExpr	=	this.getExpressionString(whileState.getExpression());
		String name 	= 	"LoopNode_" + this.hashCode();
		
		Type condExprType = this.getExpressionType(whileState.getExpression());
		
		this.currentNode = (LoopNode)this.activity.createOwnedNode(name, UMLPackage.Literals.LOOP_NODE);
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
		
		this.currentNode.setDecider(pin);
		
		this.setLastNode(this.currentNode);
		
		++AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.importBodyFromStatement(whileState.getBody());
		
		--AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(this.currentNode);
		
		//this.setLastNode(this.currentNode);	
	}

}
