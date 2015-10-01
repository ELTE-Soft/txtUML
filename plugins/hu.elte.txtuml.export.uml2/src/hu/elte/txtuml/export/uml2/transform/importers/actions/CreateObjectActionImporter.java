package hu.elte.txtuml.export.uml2.transform.importers.actions;

import hu.elte.txtuml.export.uml2.transform.importers.IActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

public class CreateObjectActionImporter extends AbstractActionImporter implements
		IActionImporter {

	public CreateObjectActionImporter(MethodBodyImporter methodBodyImporter,
			Model importedModel) {
		super(methodBodyImporter, importedModel);
	}

	@Override
	public void importFromMethodInvocation(MethodInvocation methodInvocation) {
		String instanceName = this.obtainExpressionOfNthArgument(methodInvocation, 1);
		
    	CreateObjectAction createAction =	(CreateObjectAction) 
				this.activity.createOwnedNode("create_" + instanceName + "_" + this.hashCode(),UMLPackage.Literals.CREATE_OBJECT_ACTION);
 
		Type type = this.obtainTypeOfNthArgument(methodInvocation, 1);
		
		createAction.createResult("result_" + instanceName + "_" + this.hashCode(),type);
		
		//ActivityNode lastNode = this.getLastNode();
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(createAction);
		
		/*this.createControlFlowBetweenActivityNodes(lastNode,createAction);

		this.setLastNode(createAction);*/
	}

}
