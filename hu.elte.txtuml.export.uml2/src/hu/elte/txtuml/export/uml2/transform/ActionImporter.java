package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelIdentifiedElement;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.blocks.BlockBody;
import hu.elte.txtuml.api.blocks.Condition;
import hu.elte.txtuml.api.blocks.ParameterizedBlockBody;
import hu.elte.txtuml.utils.Pair;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;

class ActionImporter extends AbstractInstructionImporter {

	enum LinkTypes
	{
		CREATE_LINK_LITERAL,
		DESTROY_LINK_LITERAL
	};
	
	private static void addEndToLinkAction(LinkAction linkAction, Association association, 
			String phrase, String instName,ModelClass obj,int endNum)
	{
		Type endType=ModelImporter.importType(obj.getClass());

		ValuePin end_valuePin=(ValuePin) 
				linkAction.createInputValue(linkAction.getName()+"_end"+endNum+"input",endType,UMLPackage.Literals.VALUE_PIN);

		addOpaqueExpressionToValuePin(end_valuePin,instName,endType);

		LinkEndData end=linkAction.createEndData();
		Property endProp=association.getOwnedEnd(phrase,endType);
		end.setEnd(endProp);

	}

	static void importLinkAction(Class<?> leftEnd, ModelClass leftObj,
			Class<?> rightEnd, ModelClass rightObj, LinkTypes linkType)
	{
		if(currentActivity != null) 
		{
			String leftName=getObjectIdentifier(leftObj);
			String rightName=getObjectIdentifier(rightObj);
			String leftPhrase=leftEnd.getSimpleName();
			String rightPhrase=rightEnd.getSimpleName();

			String assocName=leftEnd.getDeclaringClass().getSimpleName();

			Association association=(Association)currentModel.getOwnedMember(assocName);

			String linkActionName=null;
			LinkAction linkAction=null;

			switch(linkType)
			{
			case CREATE_LINK_LITERAL:
				linkActionName="link_"+leftName+"_and_"+rightName;
				linkAction	=	(CreateLinkAction) 
						currentActivity.createOwnedNode(linkActionName, UMLPackage.Literals.CREATE_LINK_ACTION);
				break;
			case DESTROY_LINK_LITERAL:
				linkActionName="unlink_"+leftName+"_and_"+rightName;
				linkAction	=	(DestroyLinkAction) 
						currentActivity.createOwnedNode(linkActionName, UMLPackage.Literals.DESTROY_LINK_ACTION);
				break;
			}

			addEndToLinkAction(linkAction,association,leftPhrase,leftName,leftObj,1);
			addEndToLinkAction(linkAction,association,rightPhrase,rightName,rightObj,2);
			createControlFlowBetweenNodes(lastNode,linkAction);
			lastNode=linkAction;

		}
	}

	private static void addArgumentsToSendSignalAction(SendSignalAction sendSignalAction, hu.elte.txtuml.api.Signal event, Signal signalToSend)
	{
		for(Property signalAttribute:signalToSend.getAllAttributes())
		{
			String attributeName=signalAttribute.getName();
			Type attributeType=signalAttribute.getType();

			ValuePin argValuePin=(ValuePin)sendSignalAction.createArgument(attributeName,attributeType,UMLPackage.Literals.VALUE_PIN);

			try
			{
				ModelIdentifiedElement attributeInstance=(ModelIdentifiedElement) getObjectFieldVal(event,attributeName);
				
				addExpressionToValuePin(argValuePin,attributeInstance,attributeType);
				
				
			}
			catch(Exception e)
			{

			}

		}
	}
	
	
	static void importSignalSend(ModelClass receiver, hu.elte.txtuml.api.Signal event) 
	{
		if(currentActivity != null) 
		{
			Signal signalToSend=(Signal) currentModel.getOwnedType(event.getClass().getSimpleName());
			String instanceName=getObjectIdentifier(receiver);

			Type instanceType=currentModel.getOwnedType(receiver.getClass().getSimpleName());

			SendSignalAction sendSignalAction	=	(SendSignalAction) 
					currentActivity.createOwnedNode("send_"+signalToSend.getName()+"_to_"+instanceName,UMLPackage.Literals.SEND_SIGNAL_ACTION);

			sendSignalAction.setSignal(signalToSend);

			ValuePin target=(ValuePin)sendSignalAction.createTarget(sendSignalAction.getName()+"_target",instanceType,UMLPackage.Literals.VALUE_PIN);

			addOpaqueExpressionToValuePin(target,instanceName,instanceType);

			addArgumentsToSendSignalAction(sendSignalAction,event,signalToSend);
			createControlFlowBetweenNodes(lastNode,sendSignalAction);

			lastNode=sendSignalAction;

		}

	}

	private static String importCondition(Condition cond)
	{

		Activity currActivityBackup=currentActivity;
		currentActivity=null;
		ModelBool checkedCond=cond.check();
		String ret= getExpression(checkedCond);
		currentActivity=currActivityBackup;
		return ret;

	}


	private static Pair<ActivityNode,ActivityEdge> createImportBlockBodyRetVal()
	{
		ActivityEdge firstEdge;
		if(blockBodyFirstEdges.size()==cntBlockBodiesBeingImported)
		{
			firstEdge=blockBodyFirstEdges.pop();
		}
		else
		{
			firstEdge=null;
		}
		Pair<ActivityNode,ActivityEdge> ret=new Pair<>(lastNode,firstEdge);
		return ret;
	}
	private static Pair<ActivityNode,ActivityEdge> importBlockBody(BlockBody body)
	{

		++cntBlockBodiesBeingImported;

		body.run();		
		Pair<ActivityNode,ActivityEdge>  ret=createImportBlockBodyRetVal();

		--cntBlockBodiesBeingImported;
		return ret;
	}

	private static <T> Pair<ActivityNode,ActivityEdge> importParameterizedBlockBody(ParameterizedBlockBody<T> body, T param)
	{

		++cntBlockBodiesBeingImported;

		body.run(param);		
		Pair<ActivityNode,ActivityEdge>  ret=createImportBlockBodyRetVal();

		--cntBlockBodiesBeingImported;
		return ret;
	}

	static void importWhileStatement(Condition cond, BlockBody body)
	{
		String condExpr=importCondition(cond);
		DecisionNode decisionNode=createNextDecisionNode();
		createFlowBetweenNodes(lastNode,decisionNode);

		lastNode=decisionNode;

		Pair<ActivityNode,ActivityEdge> importThenBodyResult=importBlockBody(body);
		ActivityEdge thenFirstEdge=importThenBodyResult.getValue();
		ActivityNode thenLastNode=importThenBodyResult.getKey();
		addGuardToActivityEdge(thenFirstEdge, condExpr);

		createFlowBetweenNodes(thenLastNode,decisionNode);
		unfinishedDecisionNodes.push(decisionNode);
		lastNode=decisionNode;

	}

	static void importIfStatement(Condition cond, BlockBody thenBody)
	{
		String condExpr=importCondition(cond);

		Pair<ActivityNode,ActivityEdge> importBlockBodyResult=importBlockBody(thenBody);
		ActivityEdge thenFirstEdge=importBlockBodyResult.getValue();
		ActivityNode thenLastNode=importBlockBodyResult.getKey();
		if(thenFirstEdge!=null)
		{
			addGuardToActivityEdge(thenFirstEdge, condExpr);
		}
		

		lastNode=thenLastNode;

	}

	static void importIfStatement(Condition cond, BlockBody thenBody,BlockBody elseBody)
	{
		String condExpr=importCondition(cond);
		DecisionNode decisionNode=createNextDecisionNode();
		createFlowBetweenNodes(lastNode,decisionNode);

		lastNode=decisionNode;


		Pair<ActivityNode,ActivityEdge> importThenBodyResult=importBlockBody(thenBody);
		ActivityEdge thenFirstEdge=importThenBodyResult.getValue();
		ActivityNode thenLastNode=importThenBodyResult.getKey();
		addGuardToActivityEdge(thenFirstEdge, condExpr);

		lastNode=decisionNode;

		Pair<ActivityNode,ActivityEdge> importElseBodyResult=importBlockBody(elseBody);
		ActivityEdge elseFirstEdge=importElseBodyResult.getValue();
		ActivityNode elseLastNode=importElseBodyResult.getKey();
		addGuardToActivityEdge(elseFirstEdge, "else");

		lastNode=createMergeNode(thenLastNode,elseLastNode);

	}

	static void importForStatement(ModelInt from, ModelInt to, ParameterizedBlockBody<ModelInt> body) 
	{
		String fromExpression=getExpression(from);
		String toExpression=getExpression(to);
		ModelInt loopVar=new ModelInt();
		String loopVarId=getObjectIdentifier(loopVar);
		String condExpr=loopVarId+"<="+toExpression;

		setVariableValue(loopVar, fromExpression);

		DecisionNode decisionNode=createNextDecisionNode();
		createFlowBetweenNodes(lastNode,decisionNode);

		lastNode=decisionNode;

		Pair<ActivityNode,ActivityEdge> importThenBodyResult=importParameterizedBlockBody(body,loopVar);
		ActivityEdge thenFirstEdge=importThenBodyResult.getValue();
		
		if(thenFirstEdge!=null)
		{
			addGuardToActivityEdge(thenFirstEdge, condExpr);
		}
		
		setVariableValue(loopVar, loopVarId+" + 1"); // inc loopVar by 1
		createFlowBetweenNodes(lastNode,decisionNode);
		
		unfinishedDecisionNodes.push(decisionNode);
		lastNode=decisionNode;

	}

	private static DecisionNode createNextDecisionNode()
	{
		++cntDecisionNodes;
		String name="decision"+cntDecisionNodes;
		DecisionNode decisionNode=(DecisionNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.DECISION_NODE);
		return decisionNode;
	}
}
