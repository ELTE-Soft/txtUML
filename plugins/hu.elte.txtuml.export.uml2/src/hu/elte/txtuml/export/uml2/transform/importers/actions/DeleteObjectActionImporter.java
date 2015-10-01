package hu.elte.txtuml.export.uml2.transform.importers.actions;

import hu.elte.txtuml.export.uml2.transform.importers.IActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;

/**
 * 
 * @author Turi Zoltán
 * 
 * class responsible for handling the delete object action
 *
 */
public class DeleteObjectActionImporter extends AbstractActionImporter implements IActionImporter {

	public DeleteObjectActionImporter(MethodBodyImporter methodBodyImporter,Model importedModel) {
		super(methodBodyImporter, importedModel);
	}

	@Override
	public void importFromMethodInvocation(MethodInvocation methodInvocation) {
	    	String instanceName = this.obtainExpressionOfNthArgument(methodInvocation, 1);
	    	
	    	DestroyObjectAction destroyAction=	(DestroyObjectAction) 
					this.activity.createOwnedNode("delete_" + instanceName,UMLPackage.Literals.DESTROY_OBJECT_ACTION);
	
			Type type = this.obtainTypeOfNthArgument(methodInvocation, 1);
	
			ValuePin target = (ValuePin) destroyAction.createTarget("target", type, UMLPackage.Literals.VALUE_PIN);
			this.createAndAddOpaqueExpressionToValuePin(target,instanceName,type);
	
			this.methodBodyImporter.getBodyNode().getExecutableNodes().add(destroyAction);
			
			/*this.createControlFlowBetweenActivityNodes(this.getLastNode(),destroyAction);
	
			this.setLastNode(destroyAction);*/
	}
}
