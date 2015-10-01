package hu.elte.txtuml.export.uml2.transform.importers.controls;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.importers.IControlStructImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

public class AssignmentImporter extends AbstractControlStructureImporter
		implements IControlStructImporter {

	public AssignmentImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}

	@Override
	public void importControlStructure(Statement statement) {
		ExpressionStatement exprState = (ExpressionStatement)statement;
		Assignment assign = (Assignment)exprState.getExpression();
		
		String targetName = this.getExpressionString(assign.getLeftHandSide());
		Type assignedValueType = this.getExpressionType(assign.getRightHandSide());
		String assignedExpresion = this.getExpressionString(assign.getRightHandSide());
		
		AddVariableValueAction assignAct = (AddVariableValueAction)this.activity.createOwnedNode("ASSIGN_VAL_" + this.hashCode(), UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		Variable var = this.activity.getVariable("VARIABLE_" + targetName, assignedValueType);
		
		assignAct.setVariable(var);
		
		ValuePin valuePin = (ValuePin) assignAct.createValue(assignAct.getName() + "_value",assignedValueType,UMLPackage.Literals.VALUE_PIN);
		this.createAndAddOpaqueExpressionToValuePin(valuePin, assignedExpresion, assignedValueType);
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(assignAct);
		
		/*this.createEdgeBetweenActivityNodes(this.getLastNode(), assignAct);
		this.setLastNode(assignAct);*/
	}

}
