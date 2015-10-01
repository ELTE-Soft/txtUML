package hu.elte.txtuml.export.uml2.transform.importers.controls;

import hu.elte.txtuml.export.uml2.transform.importers.AbstractActionCodeImporter;
import hu.elte.txtuml.export.uml2.transform.importers.IControlStructImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import java.util.Stack;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

public class IfActionImporter extends AbstractControlStructureImporter implements
		IControlStructImporter {

	protected ConditionalNode currentNode;
	protected Stack<Clause> clausesInProgress;
	
	public IfActionImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
		
		this.clausesInProgress = new Stack<Clause>();
	}
	
	@Override
	protected MethodBodyImporter importBodyFromStatement(Statement statement)
	{
		/*
		 * Freshly imported data contained in the node;
		 */
		MethodBodyImporter imp = super.importBodyFromStatement(statement);
		
		Clause currentClause = this.clausesInProgress.pop();
		
		EList<ExecutableNode> bodies = currentClause.getBodies();
		
		SequenceNode seqNode = imp.getBodyNode();
		seqNode.setName("IF_SEQ_" + this.currentNode.getName() );
		
		//Adding body to Clause
		bodies.add(seqNode);
		
		Action lastAction = (Action)seqNode.getExecutableNodes().get(seqNode.getExecutableNodes().size() - 1);
		
		currentClause.getBodyOutputs().addAll(lastAction.getOutputs());
		
		return imp;
	}

	@Override
	public void importControlStructure(Statement statement) {
		IfStatement ifs = (IfStatement) statement;
		String condExpression = this.getExpressionString(ifs.getExpression());
		
		Type condExprType = this.getExpressionType(ifs.getExpression());
		
		String name = "Conditional_" + this.hashCode();
		
		this.currentNode = (ConditionalNode)this.activity.createOwnedNode(name, UMLPackage.Literals.CONDITIONAL_NODE);
		
		//this.createEdgeBetweenActivityNodes(this.getLastNode(),this.currentNode);

		//this.setLastNode(this.currentNode);
		
		OutputPin decider = this.currentNode.createResult(condExpression, condExprType);
		
		OpaqueExpression lowerOpaqueExpression = (OpaqueExpression) decider.createLowerValue(
				decider.getName() + "_expression", condExprType,
				UMLPackage.Literals.OPAQUE_EXPRESSION);
		
		lowerOpaqueExpression.getBodies().add(condExpression);
		
		OpaqueExpression UpperOpaqueExpression = (OpaqueExpression) decider.createUpperValue(
				decider.getName() + "_expression", condExprType,
				UMLPackage.Literals.OPAQUE_EXPRESSION);
		
		UpperOpaqueExpression.getBodies().add(condExpression);
		
		Clause clause = this.currentNode.createClause();
		clause.setDecider(decider);
		this.clausesInProgress.push(clause);
		
		++AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.importBodyFromStatement(ifs.getThenStatement());
		
		--AbstractActionCodeImporter.blockBodiesBeingImported;
		
		//this.setLastNode(this.currentNode);
		
		clause = this.currentNode.createClause();
		this.clausesInProgress.push(clause);

		++AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.importBodyFromStatement(ifs.getElseStatement());
		
		--AbstractActionCodeImporter.blockBodiesBeingImported;
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(this.currentNode);
		
		//this.setLastNode(this.currentNode);
		
	}

}
