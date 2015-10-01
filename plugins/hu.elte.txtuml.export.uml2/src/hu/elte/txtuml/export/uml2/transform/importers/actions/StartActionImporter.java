package hu.elte.txtuml.export.uml2.transform.importers.actions;

import hu.elte.txtuml.export.uml2.transform.importers.IActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;

/**
 * 
 * @author Turi Zoltán
 *
 *	class responsible for importing Action.Start!
 */
public class StartActionImporter extends AbstractActionImporter implements IActionImporter {
	
	public StartActionImporter(MethodBodyImporter methodBodyImporter, Model importedModel) {
		super(methodBodyImporter,importedModel);
	}
	
	private String obtainInstanceExpression(MethodInvocation methodInvocation) {
		return this.obtainExpressionOfNthArgument(methodInvocation, 1);
	}
	
	private Type obtainInstanceType(MethodInvocation methodInvocation) {		
		return obtainTypeOfNthArgument(methodInvocation, 1);
	}
	
	@Override
	public void importFromMethodInvocation(MethodInvocation methodInvocation) {
		Type TypeOfInstance = this.obtainInstanceType(methodInvocation);
		
		String instanceExpression = this.obtainInstanceExpression(methodInvocation); 
			
		StartClassifierBehaviorAction startClassifierBehaviorAction = (StartClassifierBehaviorAction) 
				this.activity.createOwnedNode(
						"startClassifierBehavior_"+instanceExpression,
						UMLPackage.Literals.START_CLASSIFIER_BEHAVIOR_ACTION
					);

		
		ValuePin valuePin = (ValuePin) 
				startClassifierBehaviorAction.createObject(
						startClassifierBehaviorAction.getName()+"_input",
						TypeOfInstance,UMLPackage.Literals.VALUE_PIN
					);

		this.createAndAddOpaqueExpressionToValuePin(valuePin, instanceExpression, TypeOfInstance);
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(startClassifierBehaviorAction);
		
		/*this.createControlFlowBetweenActivityNodes(this.getLastNode(), startClassifierBehaviorAction);
		this.setLastNode(startClassifierBehaviorAction);*/
	}
}
