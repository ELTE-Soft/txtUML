package hu.elte.txtuml.export.uml2.transform.importers.controls;

import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.importers.IControlStructImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

public class NewActionImporter extends AbstractControlStructureImporter
		implements IControlStructImporter {

	public NewActionImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}

	@Override
	public void importControlStructure(Statement statement) {
		ExpressionStatement expStatement = (ExpressionStatement)statement;
		Assignment creationAssignment = (Assignment)expStatement.getExpression();
		
		String instanceName;
		
		if(creationAssignment.getLeftHandSide() instanceof ArrayAccess)
		{
			instanceName = (( (ArrayAccess)creationAssignment.getLeftHandSide() ).getArray().toString());
		}
		else
		{
			VariableDeclarationFragment instanceFragment = (VariableDeclarationFragment)( (VariableDeclarationExpression)creationAssignment.getLeftHandSide() ).fragments().get(0);
			instanceName = instanceFragment.getName().toString();
		}
		
		
		ClassInstanceCreation creationExp = (ClassInstanceCreation)creationAssignment.getRightHandSide();
		
		Type classType = this.getExpressionType(creationExp);
		
    	CreateObjectAction createAction =	(CreateObjectAction) 
				this.activity.createOwnedNode("create_" + instanceName + "_" + this.hashCode(),UMLPackage.Literals.CREATE_OBJECT_ACTION);
	
    	createAction.createResult("result_" + instanceName + "_" + this.hashCode(),classType);
    	
		/*ActivityNode lastNode =*/ this.getLastNode();
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(createAction);
		
		/*this.createControlFlowBetweenActivityNodes(lastNode,createAction);

		this.setLastNode(createAction);*/
	}
}
