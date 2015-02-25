package txtuml.export.uml2tocpp;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 **********************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.eclipse.uml2.uml.Variable;

import txtuml.export.uml2tocpp.templates.ActivityTemplates;
import txtuml.export.uml2tocpp.templates.GenerationTemplates;

public class ActivityExport
{
	static private Map<CreateObjectAction,String> _objectMap=new HashMap<CreateObjectAction,String>();
	static private int _objCounter=1;
	
	public static String createfunctionBody(Activity activity_,Boolean rt_)
	{
		ActivityNode startNode=null;
		for(ActivityNode node:activity_.getOwnedNodes())
		{
			if(node.eClass().equals(UMLPackage.Literals.INITIAL_NODE))
			{
				startNode=node;
				break;
			}
		}
		return createActivityVariables(activity_)+createActivityPartCode(startNode,rt_);
	}
	
	private static String createActivityVariables(Activity activity_) 
	{
		String source="";
		List<Variable> variables=new LinkedList<Variable>();
		Shared.getTypedElements(variables,activity_.allOwnedElements(),UMLPackage.Literals.VARIABLE);
		for(Variable variable:variables)
		{
				String type="!!!UNKNOWNTYPE!!!";
				if(variable.getType() != null)
				{
					type=variable.getType().getName();
				}
				source+=GenerationTemplates.VariableDecl(type,variable.getName());
		}
		return source;
	}

	private static String createActivityPartCode(ActivityNode startNode_,Boolean rt_)
	{
		return createActivityPartCode(startNode_,null,new ArrayList<ActivityNode>(),rt_);
	}
	
	private static String createActivityPartCode(ActivityNode startNode_,ActivityNode stopNode_,List<ActivityNode> finishedControlNodes_,Boolean rt_) 
	{
		String source="";
		LinkedList<ActivityNode> nodeList=new LinkedList<ActivityNode>(Arrays.asList(startNode_));
		
		List<ActivityNode> finishedControlNodes=finishedControlNodes_;
		while(!nodeList.isEmpty() && (stopNode_==null || nodeList.getFirst() != stopNode_) )
		{
			ActivityNode currentNode=nodeList.removeFirst();
			if(currentNode!=null)//TODO have to see the model, to find what caused it, and change the code here
			{
				if(currentNode.eClass().equals(UMLPackage.Literals.INPUT_PIN))
				{
					currentNode=(ActivityNode)currentNode.getOwner();
				}
				//current node compile
				source+=createActivityNodeCode(currentNode,finishedControlNodes,rt_);
				
				for(ActivityNode node:getNextNodes(currentNode))
				{
					if(!finishedControlNodes.contains(node) && !nodeList.contains(node))
					{
						nodeList.add(node);
					}
				}
			}
		}
		return source;
	}

	private static String createActivityNodeCode(ActivityNode node_, List<ActivityNode> finishedControlNodes_,Boolean rt_) 
	{
		String source="";
		finishedControlNodes_.add(node_);
		
		if(node_.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION))
		{
			AddStructuralFeatureValueAction asfva=(AddStructuralFeatureValueAction)node_;
			source=ActivityTemplates.GeneralSetValue(getTargetFromASFVA(asfva),
					  													 getTargetFromInputPin(asfva.getValue(),false),
					  													 ActivityTemplates.getOperationFromType(asfva.getStructuralFeature().isMultivalued(),
					  															 			  asfva.isReplaceAll()));
		}
		else if(node_.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION))
		{
			source=createObjectActionCode((CreateObjectAction)node_,rt_);
		}
		else if(node_.eClass().equals(UMLPackage.Literals.SEND_SIGNAL_ACTION))
		{
			source=createSendSignalActionCode((org.eclipse.uml2.uml.SendSignalAction)node_,rt_);
		}
		else if(node_.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION))
		{
			source=createCallOperationActionCode((org.eclipse.uml2.uml.CallOperationAction)node_);
		}
		else if(node_.eClass().equals(UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION))
		{
			AddVariableValueAction avva=(AddVariableValueAction)node_;
			source=ActivityTemplates.GeneralSetValue(avva.getVariable().getName(),
																		 getTargetFromInputPin(avva.getValue()),
																		 ActivityTemplates.getOperationFromType(avva.getVariable().isMultivalued(),
																				 			  avva.isReplaceAll()));
		}
		else if(node_.eClass().equals(UMLPackage.Literals.DECISION_NODE))
		{
			source=createDecisionNodeCode((DecisionNode)node_,finishedControlNodes_,rt_);
		}
		return source;
	}
	
	private static String createObjectActionCode(CreateObjectAction node_,Boolean rt_)
	{
		String type=node_.getClassifier().getName();
		String name="co_of_"+type+"_"+_objCounter++;//#Create#Object_#of_type_GlobalNumberOfObjectCreation
		_objectMap.put(node_,name);
		return ActivityTemplates.CreateObject(type,name,rt_);
	}

	//decide if it's a while or if-else
	private static String createDecisionNodeCode(DecisionNode node_,List<ActivityNode> finishedControlNodes_,Boolean rt_) 
	{
		String source="";
		if(isLoop(node_)) // while
		{
			//find the loop condition
			List<ActivityNode> finishedNodes=new ArrayList<ActivityNode>(Arrays.asList(node_));
			List<ActivityEdge> branches=node_.getOutgoings();
			String loopCondition="";
			ActivityEdge toFinal=null;
			for(ActivityEdge edge:branches)
			{
				if(isLeadsToFinal(edge.getTarget(),node_))
				{
					String guard=getGuard(edge);
					toFinal=edge;
					loopCondition=ActivityTemplates.Operators.Not+"("+(guard)+")";
					if(guard.isEmpty() || guard.equals("else"))
					{
						int i=0;
						for(ActivityEdge edgeInner:branches)
						{
							if(!edgeInner.equals(toFinal))
							{
								if(i==0)
								{
									if(branches.size() > 2)
									{
										guard="("+(getGuard(edgeInner))+")";
									}
									else
									{
										guard=getGuard(edgeInner);
									}
								}
								else
								{
									guard+=" "+ActivityTemplates.Operators.And+" "+"("+(getGuard(edgeInner))+")";
								}
							}
						}
						loopCondition=guard;
					}
					break;
				}
			}
			String loopBody="";
			
			//removing toFinal edge
			branches=new LinkedList<ActivityEdge>();
			for(ActivityEdge edge:node_.getOutgoings())
			{
				if(!edge.equals(toFinal))
				{
					branches.add(edge);
				}
			}
			
			if(branches.size() > 1)
			{
				loopBody=createIfCode(node_,node_,branches,finishedControlNodes_,rt_);
			}
			else
			{
				loopBody=createActivityPartCode(branches.get(0).getTarget(),node_,finishedNodes,rt_);
			}
			finishedControlNodes_.addAll(finishedNodes);
			source=ActivityTemplates.While(loopCondition, loopBody);
		}
		else // if-else
		{
			source=createIfCode(node_,getIfEnd(node_),node_.getOutgoings(),finishedControlNodes_,rt_);
		}
		return source;
	}

	private static String createIfCode(DecisionNode node_,ActivityNode ifEndNode_,List<ActivityEdge> branches_,List<ActivityNode> finishedControlNodes_,Boolean rt_)
	{
		String source="";
		List<ActivityNode> finishedNodes=new ArrayList<ActivityNode>(Arrays.asList(node_));//TODO not sure it is needed
		List<Util.Pair<String,String>> condsAndBodies=new LinkedList<Util.Pair<String,String>>();
		Util.Pair<String,String> elseBranch=null;
		for(ActivityEdge edge:branches_)
		{
			Util.Pair<String,String> tmp=new Util.Pair<String,String>(getGuard(edge),createActivityPartCode(edge.getTarget(),ifEndNode_,finishedNodes,rt_));
			if(tmp.getKey().isEmpty() || tmp.getKey().equals("else"))
			{
				elseBranch=tmp;
			}
			else
			{
				condsAndBodies.add(tmp);
			}
		}
		if(elseBranch != null && !elseBranch.getValue().isEmpty())
		{
			condsAndBodies.add(elseBranch);
		}
		source=ActivityTemplates.ElseIf(condsAndBodies);

		finishedControlNodes_.addAll(finishedNodes);
		return source;
	}
	
	private static String getTargetFromInputPin(InputPin node_)
	{
		return getTargetFromInputPin(node_,true);
	}

	private static String getTargetFromInputPin(InputPin node_,Boolean recursive_)
	{
		String source="UNKNOWN_TYPE_FROM_VALUEPIN";
		if(node_.eClass().equals(UMLPackage.Literals.INPUT_PIN))
		{
			source=getTargetFromActivityNode(node_.getIncomings().get(0).getSource());//TODO null check, if null terminate
		}
		else //node_.eClass().equals(UMLPackage.Literals.VALUE_PIN)
		{
			ValueSpecification valueSpec=((ValuePin)node_).getValue();
			source=getValueFromValueSpecification(valueSpec);
		}
		return source;
	}
	
	private static String getValueFromValueSpecification(ValueSpecification valueSpec_)
	{
		String source="";
		if(valueSpec_.eClass().equals(UMLPackage.Literals.OPAQUE_EXPRESSION))
		{
			source=Shared.parseOCL(((OpaqueExpression)valueSpec_).getBodies().get(0));//except only one, if more then one can't choose
		}
		else if(valueSpec_.eClass().equals(UMLPackage.Literals.LITERAL_INTEGER))
		{
			source=((Integer)((LiteralInteger)valueSpec_).getValue()).toString();
		}
		else if	(valueSpec_.eClass().equals(UMLPackage.Literals.LITERAL_BOOLEAN))
		{
			source=((Boolean)((LiteralBoolean)valueSpec_).isValue()).toString();
		}
		else if	(valueSpec_.eClass().equals(UMLPackage.Literals.LITERAL_STRING))
		{
			source=((LiteralString)valueSpec_).getValue();
		}
		else
		{
			source="UNHANDLED_VALUEPIN_VALUETYPE";
		}
		return source;
	}

	private static String getTargetFromActivityNode(ActivityNode node_)
	{
		String source="UNHANDLED_ACTIVITYNODE";
		if(node_.eClass().equals(UMLPackage.Literals.FORK_NODE) ||
				node_.eClass().equals(UMLPackage.Literals.JOIN_NODE) ||
				node_.eClass().equals(UMLPackage.Literals.DECISION_NODE) )
		{
			source=getTargetFromActivityNode(node_.getIncomings().get(0).getSource());
		}
		else if(node_.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION))
		{
			source=getTargetFromInputPin(((AddStructuralFeatureValueAction)node_).getObject());
		}
		else if(node_.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION))
		{
			source=getTargetFromRSFA((ReadStructuralFeatureAction)node_);
		}
		else if(node_.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE))
		{
			EClass ec=node_.getActivity().getOwner().eClass();
			String paramName=((ActivityParameterNode)node_).getParameter().getName();
			if(ec.equals(UMLPackage.Literals.TRANSITION))
			{
				source=ActivityTemplates.TransitionActionParameter(paramName);
			}
			else //the parameter is a function parameter
			{
				source=GenerationTemplates.ParamName(paramName);
			}
		}
		else if(node_.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION))
		{
			source=_objectMap.get((CreateObjectAction)node_);
		}
		else if(node_.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION))
		{
			source=ActivityTemplates.Self;
		}
		else if(node_.eClass().equals(UMLPackage.Literals.OPAQUE_ACTION))
		{
			OpaqueAction oa=(OpaqueAction)node_;
			source=Shared.parseOCL(oa.getBodies().get(0));
			for(InputPin variable:oa.getInputs())
			{
				String oclParam="\\("+variable.getName()+"\\)";
				String cppParam="\\("+GenerationTemplates.ParamName(getTargetFromInputPin(variable))+"\\)";
				source=source.replaceAll(oclParam,cppParam);
			}
		}
		else if(node_.eClass().equals(UMLPackage.Literals.OUTPUT_PIN))
		{
			source=getTargetFromActivityNode((ActivityNode)node_.getOwner());
		}
		else if(node_.eClass().equals(UMLPackage.Literals.VALUE_SPECIFICATION_ACTION))
		{
			source=getValueFromValueSpecification(((ValueSpecificationAction)node_).getValue());
		}
		else
		{
			System.out.println(node_.eClass().getName());//TODO just for development debug
		}
		return source;
	}

	private static String getTargetFromASFVA(AddStructuralFeatureValueAction node_)
	{
		String source=node_.getStructuralFeature().getName();
		String object=getTargetFromInputPin(node_.getObject());
		if(!object.isEmpty())
		{
			source=object+ActivityTemplates.AccesOperatoForType(getTypeFromInputPin(node_.getObject()))+source;
		}
		return source;
	}

	private static String getTargetFromRSFA(ReadStructuralFeatureAction node_)
	{
		String source=node_.getStructuralFeature().getName();
		String object=getTargetFromInputPin(node_.getObject());
		if(!object.isEmpty())
		{
			source=object+ActivityTemplates.AccesOperatoForType(getTypeFromInputPin(node_.getObject()))+source;
		}
		return source;
	}

	private static String createSendSignalActionCode(org.eclipse.uml2.uml.SendSignalAction node_,Boolean rt_) 
	{
		return  ActivityTemplates.SignalSend(node_.getSignal().getName(),
				getTargetFromInputPin(node_.getTarget(),false),
				getTypeFromInputPin(node_.getTarget()),
				ActivityTemplates.AccesOperatoForType(getTypeFromInputPin(node_.getTarget())),
				getParamNames(node_.getArguments()),rt_);
	}
	
	private static String getTypeFromInputPin(InputPin inputPin_)
	{
		Type type=inputPin_.getType();
		String targetTypeName="null";
		if(type != null)
		{
			targetTypeName=type.getName();
		}
		else if(inputPin_.eClass().equals(UMLPackage.Literals.INPUT_PIN))
		{
			targetTypeName=getTypeFromSpecialAcivityNode(inputPin_.getIncomings().get(0).getSource());
		}
		else //inputPin_.eClass().equals(UMLPackage.Literals.VALUE_PIN)
		{
			ValueSpecification valueSpec=((ValuePin)inputPin_).getValue();
			if(valueSpec.eClass().equals(UMLPackage.Literals.OPAQUE_EXPRESSION))
			{
				targetTypeName=getTypeFromOCL(((OpaqueExpression)valueSpec).getBodies().get(0),
												getParentClass(((ActivityNode)inputPin_.getOwner()).getActivity()));//except only one, if more then one can't choose
			}
			else
			{
				targetTypeName="UNHANDLED_VALUE_TYPE";
			}
		}
		return targetTypeName;
	}
	
	private static String getTypeFromOCL(String ocl_,Class self_) {
		Class currentClass=self_;
		String[] parts=ocl_.split("\\.");
		for(String str:parts)
		{
			if(!str.equals("self"))
			{
				if(str.contains("->"))
				{
					str=str.substring(0,str.indexOf("->"));

				}
				for(Property prop:Shared.getProperties(currentClass))
				{
					if(prop.getName().equals(str))
					{
						currentClass=(Class)prop.getType();
					}
				}
			}
		}
		return currentClass.getName();
	}

	private static String getTypeFromSpecialAcivityNode(ActivityNode node_)
	{
		String targetTypeName="null";
		//because the output pins not count as parent i have to if-else again ... 
		if(node_.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION))
		{
			targetTypeName=getParentClass(node_.getActivity()).getName();
		}
		if(node_.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION))
		{
			targetTypeName=getTypeFromInputPin(((AddStructuralFeatureValueAction)node_).getObject());
		}
		else if(node_.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION))
		{
			targetTypeName=getTypeFromInputPin(((ReadStructuralFeatureAction)node_).getObject());
		}
		else if(node_.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE))
		{
			targetTypeName=((ActivityParameterNode)node_).getType().getName();
		}
		else if(node_.eClass().equals(UMLPackage.Literals.FORK_NODE) || 
				node_.eClass().equals(UMLPackage.Literals.DECISION_NODE) ||
				node_.eClass().equals(UMLPackage.Literals.JOIN_NODE) )
		{
			targetTypeName=getTypeFromSpecialAcivityNode(node_.getIncomings().get(0).getSource());
		}
		else if(node_.eClass().equals(UMLPackage.Literals.OPAQUE_ACTION))
		{
			targetTypeName=getTypeFromOCL(((OpaqueAction)node_).getBodies().get(0),getParentClass(node_.getActivity()));
		}
		else
		{
			targetTypeName="UNKNOWN_TARGET_TYPER_NAME";
			//TODO unknown for me, need the model
		}
		
		return targetTypeName;
	}
	
	
	private static <ItemType extends Element>
	Class getParentClass(ItemType element_)
	{
		Element parent=element_.getOwner();
		while(!parent.eClass().equals(UMLPackage.Literals.CLASS))
		{
			parent=parent.getOwner();
		}
		return (Class)parent;
	}

	private static String createCallOperationActionCode(org.eclipse.uml2.uml.CallOperationAction node_)
	{
		return ActivityTemplates.OperationCall(getTargetFromInputPin(node_.getTarget(),false),
																		ActivityTemplates.AccesOperatoForType(getTypeFromInputPin(node_.getTarget())),
																		node_.getOperation().getName(),
																		getParamNames(node_.getArguments()));
	}
	
	private static List<String> getParamNames(List<InputPin> arguments_)
	{
		List<String> params=new ArrayList<String>();
		for(InputPin param:arguments_)
		{
			params.add(getTargetFromInputPin(param));
		}
		return params;
	}
	
	private static ActivityNode getIfEnd(DecisionNode node_)
	{
		ActivityNode ret=null;
		LinkedList<ActivityNode> nodes=getNextNodes(node_);
		int branchNumber=nodes.size();
		LinkedList<MergeNode> ifEnds=new LinkedList<MergeNode>();
		while(!nodes.isEmpty())
		{
			ActivityNode currentNode=nodes.removeFirst();
			if(currentNode.eClass().equals(UMLPackage.Literals.DECISION_NODE))
			{
				if(isLoop((DecisionNode)currentNode))
				{
					for(ActivityNode loopNodes:getNextNodes(currentNode))
					{
						if(isLeadsToFinal(loopNodes,(DecisionNode)currentNode))
						{
							nodes.addAll(getNextNodes(loopNodes));
							break;
						}
					}
				}
				else//if
				{
					ActivityNode ifEnd=getIfEnd((DecisionNode)currentNode);
					ifEnds.add((MergeNode)ifEnd);
					nodes.addAll(getNextNodes( ifEnd) );
				}
			}
			else if(currentNode.eClass().equals(UMLPackage.Literals.MERGE_NODE))
			{
				ifEnds.add((MergeNode)currentNode);
			}
			else
			{
				nodes.addAll(getNextNodes( currentNode) );
			}
		}
		
		for(MergeNode end:ifEnds)
		{
			if(Collections.frequency(ifEnds,end) == branchNumber)
			{
				ret=end;
				break;
			}
		}
		return ret;
	}

	private static boolean isLoop(DecisionNode loopStarter_)
	{
		LinkedList<ActivityNode> nodes=getNextNodes(loopStarter_);
		List<Boolean> toFinal=new LinkedList<Boolean>();
		for(ActivityNode node:nodes)
		{
			toFinal.add(isLeadsToFinal(node,loopStarter_));
		}
		if(1 == Collections.frequency(toFinal,true))
		{
			return true;
		}
		return false;
	}
	
	private static boolean isLeadsToFinal(ActivityNode start_,DecisionNode loopStart_)
	{
		List<DecisionNode> loopStarters=new LinkedList<DecisionNode>();
		loopStarters.add(loopStart_);
		return isLeadsToFinal(start_,loopStarters);
	}
	
	private static boolean isLeadsToFinal(ActivityNode start_,List<DecisionNode> loopStarts_)
	{
		if(start_.eClass().equals(UMLPackage.Literals.ACTIVITY_FINAL_NODE))
		{
			return true;
		}
		LinkedList<ActivityNode> nodes=getNextNodes(start_);
		while(!nodes.isEmpty())
		{
			for(ActivityNode node:nodes)
			{
				if(node.eClass().equals(UMLPackage.Literals.DECISION_NODE))
				{
					DecisionNode dec=(DecisionNode)node;
					if(loopStarts_.contains(dec))
					{
						return false;
					}
					loopStarts_.add(dec);
					
					if(isLeadsToFinal(dec, loopStarts_))
					{
						return true;
					}
				}
				if(node.eClass().equals(UMLPackage.Literals.ACTIVITY_FINAL_NODE))
				{
					return true;
				}
			}
			nodes.removeAll(loopStarts_);
			if(!nodes.isEmpty())
			{
				nodes.addAll(getNextNodes(nodes.removeFirst()));
			}
		}
		return false;
	}

	private static LinkedList<ActivityNode> getNextNodes(ActivityNode node_) 
	{
		ActivityNode currentNode=node_;
		LinkedList<ActivityNode> nextNodes=new LinkedList<ActivityNode>();
		if(node_.eClass().equals(UMLPackage.Literals.INPUT_PIN))
		{
			currentNode=(ActivityNode)node_.getOwner();
			nextNodes.add(currentNode);
		}
		
		//direct output edges
		List<ActivityEdge> edges=currentNode.getOutgoings();
		//output edges from output pin
		List<OutputPin> outputPins=new LinkedList<OutputPin>();
		Shared.getTypedElements(outputPins,currentNode.getOwnedElements(),UMLPackage.Literals.OUTPUT_PIN);
		for(OutputPin pin:outputPins)
		{
			edges.addAll(pin.getOutgoings());
		}
			
		//add next nodes to the list
		for(ActivityEdge currentEgde:edges)
		{
			ActivityNode tmp=currentEgde.getTarget();
			if(tmp.eClass().equals(UMLPackage.Literals.INPUT_PIN))
			{
				tmp=(ActivityNode)tmp.getOwner();
			}
			nextNodes.add(tmp);	
		}
		return nextNodes;
	}
	
	private static String getGuard(ActivityEdge edge_) //TODO works with literalString or opaqueExpression only
	{
		return Shared.getGuardFromValueSpecification(edge_.getGuard());
	}
	
}