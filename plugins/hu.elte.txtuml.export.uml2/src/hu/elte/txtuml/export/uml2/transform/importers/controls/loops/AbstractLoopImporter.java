package hu.elte.txtuml.export.uml2.transform.importers.controls.loops;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;
import hu.elte.txtuml.export.uml2.transform.importers.controls.AbstractControlStructureImporter;

public abstract class AbstractLoopImporter extends
		AbstractControlStructureImporter {

	protected LoopNode currentNode;
	protected SequenceNode loopBody;
	
	
	public AbstractLoopImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}
	
	protected void addTestsToLoopVar(EList<ExecutableNode> tests,String condExpr,Type condExprType)
	{
		/*
		 * Temporary solution, need a node capable of evaluating expression values!
		 */
		Variable loopVar = this.currentNode.createVariable("LOOP_VAR_" + this.currentNode.getName(), condExprType);
		AddVariableValueAction writeVar = (AddVariableValueAction)this.currentNode.createNode("INIT_LOOP_VAR_" + this.currentNode.getName(), UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		writeVar.setVariable(loopVar);
		
		ValuePin valuePin = (ValuePin) writeVar.createValue(writeVar.getName()+"_value",condExprType,UMLPackage.Literals.VALUE_PIN);
		this.createAndAddOpaqueExpressionToValuePin(valuePin, condExpr, condExprType);
		
		tests.add(writeVar);
	}
	
	@Override
	protected MethodBodyImporter importBodyFromStatement(Statement statement)
	{
		MethodBodyImporter imp = super.importBodyFromStatement(statement);
		
		EList<ExecutableNode> bodies = this.currentNode.getBodyParts();
		
		SequenceNode seqNode = imp.getBodyNode();
		seqNode.setName("For_control_Body_" + this.currentNode.getName());
		EList<ExecutableNode> currentSeqBody = seqNode.getExecutableNodes();

		//Adding body to Clause
		bodies.add(seqNode);
		
		Action lastAction = (Action)seqNode.getExecutableNodes().get(seqNode.getExecutableNodes().size() - 1);
		
		System.out.println(lastAction);
		
		this.currentNode.getBodyOutputs().addAll(lastAction.getOutputs());
		this.loopBody = seqNode;
		
		return imp;
	}
}
