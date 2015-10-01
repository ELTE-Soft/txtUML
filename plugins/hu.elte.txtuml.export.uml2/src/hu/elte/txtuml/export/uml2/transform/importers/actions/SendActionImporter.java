package hu.elte.txtuml.export.uml2.transform.importers.actions;

import hu.elte.txtuml.export.uml2.transform.importers.IActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;
import hu.elte.txtuml.export.uml2.transform.visitors.ExpressionVisitor;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.Variable;

public class SendActionImporter extends AbstractActionImporter implements IActionImporter{
	
	public SendActionImporter(MethodBodyImporter methodBodyImporter, Model importedModel) {
		super(methodBodyImporter,importedModel);
	}

	@Override
	public void importFromMethodInvocation(MethodInvocation methodInvocation) {
		
		Signal signalToSend = this.obtainSignalToSend(methodInvocation);
		Type instanceType = this.obtainInstanceType(methodInvocation);
		String instanceExpression = this.obtainInstanceExpression(methodInvocation);

		SendObjectAction sendAction	=	(SendObjectAction) 
				this.activity.createOwnedNode(
						"send_"+signalToSend.getName()+"_to_"+instanceExpression,
						UMLPackage.Literals.SEND_OBJECT_ACTION
					);

		createAndWireRequest(sendAction, methodInvocation, signalToSend);
		createAndWireTarget(instanceType, instanceExpression, sendAction);
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(sendAction);
		
		/*createControlFlowBetweenActivityNodes(this.getLastNode(), sendAction);
		setLastNode(sendAction);*/
	}

	private void createAndWireTarget(Type instanceType,	String instanceExpression,
			SendObjectAction sendAction) {
		
		ValuePin target = (ValuePin)
					sendAction.createTarget(
							sendAction.getName()+"_target",
							instanceType,UMLPackage.Literals.VALUE_PIN
						);

		this.createAndAddOpaqueExpressionToValuePin(target, instanceExpression, instanceType);
	}
	
	private void createAndWireRequest(SendObjectAction sendAction,
			MethodInvocation methodInvocation, Signal signalToSend) {
		
		ValuePin request = (ValuePin)
				sendAction.createRequest(
						sendAction.getName()+"_request",
						signalToSend,UMLPackage.Literals.VALUE_PIN
					);
		
		Expression signalArg = (Expression) methodInvocation.arguments().get(1);
		String signalExpression = null;
		if(signalArg instanceof ClassInstanceCreation) {
	
			ClassInstanceCreation signalCreation = 
					(ClassInstanceCreation) signalArg;
			
			Variable signalVar = importSignalCreation(signalCreation, signalToSend);
			if(signalVar != null) {
				signalExpression = signalVar.getName();
			}
			
		} else {
			ExpressionVisitor visitor = new ExpressionVisitor();
			signalArg.accept(visitor);
			signalExpression = visitor.getImportedExpression();	
		}
		
		this.createAndAddValueExpressionToValuePin(request, signalExpression, signalToSend);
	}

	private Type obtainInstanceType(MethodInvocation methodInvocation) {		
		return obtainTypeOfNthArgument(methodInvocation, 1);
	}
	
	private Signal obtainSignalToSend(MethodInvocation methodInvocation) {		
		return (Signal) obtainTypeOfNthArgument(methodInvocation, 2);
	}
	
	private String obtainInstanceExpression(MethodInvocation methodInvocation) {
		Expression expr = (Expression) methodInvocation.arguments().get(0);
		ExpressionVisitor expressionVisitor = new ExpressionVisitor();
		expr.accept(expressionVisitor);
		return expressionVisitor.getImportedExpression();
	}
	
	private Variable importSignalCreation(ClassInstanceCreation signalCreation, Signal signal) {
		String variableName = "signal_" + System.identityHashCode(signalCreation);
		Variable variable = this.activity.createVariable(variableName, signal);
		return variable;
	}
}
