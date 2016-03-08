package hu.elte.txtuml.export.cpp;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class ActivityExporter {
	Map<CreateObjectAction, String> _objectMap = new HashMap<CreateObjectAction, String>();
	int _objCounter = 1;
	
	public ActivityExporter() {
	    ActivityTemplates.Operators.Init();
	}

	public String createfunctionBody(Activity activity_) {
		ActivityNode startNode = null;
		for (ActivityNode node : activity_.getOwnedNodes()) {
			if (node.eClass().equals(UMLPackage.Literals.INITIAL_NODE)) {
				startNode = node;
				break;
			}
		}
		return createActivityVariables(activity_) + createActivityPartCode(startNode);
	}

	private String createActivityVariables(Activity activity_) {
		String source = "";
		List<Variable> variables = new LinkedList<Variable>();
		Shared.getTypedElements(variables, activity_.allOwnedElements(), UMLPackage.Literals.VARIABLE);
		for (Variable variable : variables) {
			String type = "!!!UNKNOWNTYPE!!!";
			if (variable.getType() != null) {
				type = variable.getType().getName();
			}
			source += GenerationTemplates.variableDecl(type, variable.getName());
		}
		return source;
	}

	private String createActivityPartCode(ActivityNode startNode_) {
		return createActivityPartCode(startNode_, null, new ArrayList<ActivityNode>());
	}

	private String createActivityPartCode(ActivityNode startNode_, ActivityNode stopNode_,
			List<ActivityNode> finishedControlNodes_) {
		String source = "";
		LinkedList<ActivityNode> nodeList = new LinkedList<ActivityNode>(Arrays.asList(startNode_));

		List<ActivityNode> finishedControlNodes = finishedControlNodes_;
		while (!nodeList.isEmpty() && (stopNode_ == null || nodeList.getFirst() != stopNode_)) {
			ActivityNode currentNode = nodeList.removeFirst();
			if (currentNode != null)// TODO have to see the model, to find what
									// caused it, and change the code here
			{
				if (currentNode.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
					currentNode = (ActivityNode) currentNode.getOwner();
				}
				// current node compile
				source += createActivityNodeCode(currentNode);

				for (ActivityNode node : getNextNodes(currentNode)) {
					if (!finishedControlNodes.contains(node) && !nodeList.contains(node)) {
						nodeList.add(node);
					}
				}
			}
		}
		return source;
	}

	private StringBuilder createActivityNodeCode(ActivityNode node_) {
		StringBuilder source = new StringBuilder("");
		
		if (node_.eClass().equals(UMLPackage.Literals.SEQUENCE_NODE)) {
		    SequenceNode seqNode = (SequenceNode) node_;
		    for (ActivityNode aNode : seqNode.getNodes()) {
		       source.append(createActivityNodeCode(aNode));
		    }
		}
		else if (node_.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			AddStructuralFeatureValueAction asfva = (AddStructuralFeatureValueAction) node_;
			source.append(ActivityTemplates.generalSetValue(getTargetFromASFVA(asfva),
					getTargetFromInputPin(asfva.getValue(), false), 
					ActivityTemplates.getOperationFromType(
						asfva.getStructuralFeature().isMultivalued(), asfva.isReplaceAll())));
		} else if (node_.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION)) {
			source.append(createObjectActionCode((CreateObjectAction) node_));
		} else if (node_.eClass().equals(UMLPackage.Literals.SEND_SIGNAL_ACTION)) {
			source.append(createSendSignalActionCode((org.eclipse.uml2.uml.SendSignalAction) node_).toString());
		} else if (node_.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
		    	
			source.append(createCallOperationActionCode((org.eclipse.uml2.uml.CallOperationAction) node_));
		} else if (node_.eClass().equals(UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION)) {
			AddVariableValueAction avva = (AddVariableValueAction) node_;
			source.append(ActivityTemplates.generalSetValue(avva.getVariable().getName(),
					getTargetFromInputPin(avva.getValue()),
					ActivityTemplates.getOperationFromType(avva.getVariable().isMultivalued(), avva.isReplaceAll())));
		}
		else if(node_.eClass().equals(UMLPackage.Literals.LOOP_NODE)) {
		    source.append(createCycleCode((LoopNode) node_));
		}
		return source;
	}

	private String createObjectActionCode(CreateObjectAction node_) {
		String type = node_.getClassifier().getName();
		boolean isSm = false;
		for (Element element : node_.getClassifier().allOwnedElements()) {
			if (element.eClass().equals(UMLPackage.Literals.STATE_MACHINE)) {
				isSm = true;
				break;
			}
		}

		String name = "co_of_" + type + "_" + _objCounter++;// #Create#Object_#of_type_GlobalNumberOfObjectCreation
		_objectMap.put(node_, name);
		return ActivityTemplates.createObject(type, name, isSm);
	}
	
	private StringBuilder createCycleCode(LoopNode loopNode) {
	    StringBuilder source = new StringBuilder("");
	    
	    if(loopNode.getSetupParts() != null ) {
		    for(ExecutableNode initNode : loopNode.getSetupParts()) {
			source.append(createActivityNodeCode(initNode));
		    }
	    }
	    
	    StringBuilder body = new StringBuilder("");
	    for(ExecutableNode bodyNode : loopNode.getBodyParts()) {
		body.append(createActivityNodeCode(bodyNode));
	    }
	    
	    StringBuilder cond = new StringBuilder("");
	    for(ExecutableNode condNode : loopNode.getTests()) {
		cond.append(createActivityNodeCode(condNode));
	    }
	    
	    source.append(ActivityTemplates.whileCycle(cond.toString(), body.toString(), ""));

	    
	    return source;
	}

	private String getTargetFromInputPin(InputPin node_) {
		return getTargetFromInputPin(node_, true);
	}

	private String getTargetFromInputPin(InputPin node_, Boolean recursive_) {
		String source = "UNKNOWN_TYPE_FROM_VALUEPIN";
		if (node_.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
			source = getTargetFromActivityNode(node_.getIncomings().get(0).getSource());// TODO
																						// null
																						// check,
																						// if
																						// null
																						// terminate
		} else // node_.eClass().equals(UMLPackage.Literals.VALUE_PIN)
		{
			ValueSpecification valueSpec = ((ValuePin) node_).getValue();
			source = getValueFromValueSpecification(valueSpec);
		}
		return source;
	}

	private String getValueFromValueSpecification(ValueSpecification valueSpec_) {
		String source = "";
		if (valueSpec_.eClass().equals(UMLPackage.Literals.LITERAL_INTEGER)) {
			source = ((Integer) ((LiteralInteger) valueSpec_).getValue()).toString();
		} else if (valueSpec_.eClass().equals(UMLPackage.Literals.LITERAL_BOOLEAN)) {
			source = ((Boolean) ((LiteralBoolean) valueSpec_).isValue()).toString();
		} else if (valueSpec_.eClass().equals(UMLPackage.Literals.LITERAL_STRING)) {
			source = "\"" + ((LiteralString) valueSpec_).getValue() + "\"";
		} else {
			source = "UNHANDLED_VALUEPIN_VALUETYPE";
		}
		return source;
	}

	private String getTargetFromActivityNode(ActivityNode node_) {
		String source = "UNHANDLED_ACTIVITYNODE";
		if (node_.eClass().equals(UMLPackage.Literals.FORK_NODE) || node_.eClass().equals(UMLPackage.Literals.JOIN_NODE)
				|| node_.eClass().equals(UMLPackage.Literals.DECISION_NODE)) {
			source = getTargetFromActivityNode(node_.getIncomings().get(0).getSource());
		} else if (node_.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			source = getTargetFromInputPin(((AddStructuralFeatureValueAction) node_).getObject());
		} else if (node_.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			source = getTargetFromRSFA((ReadStructuralFeatureAction) node_);
		} else if (node_.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
			EClass ec = node_.getActivity().getOwner().eClass();
			String paramName = ((ActivityParameterNode) node_).getParameter().getName();
			if (ec.equals(UMLPackage.Literals.TRANSITION)) {
				source = ActivityTemplates.transitionActionParameter(paramName);
			} else // the parameter is a function parameter
			{
				source = GenerationTemplates.paramName(paramName);
			}
		} else if (node_.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION)) {
			source = _objectMap.get(node_);
		} else if (node_.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION)) {
			source = ActivityTemplates.Self;
		} else if (node_.eClass().equals(UMLPackage.Literals.OPAQUE_ACTION)) {
			OpaqueAction oa = (OpaqueAction) node_;
			source = Shared.parseOCL(oa.getBodies().get(0));
			for (InputPin variable : oa.getInputs()) {
				String oclParam = "\\(" + variable.getName() + "\\)";
				String cppParam = "\\(" + GenerationTemplates.paramName(getTargetFromInputPin(variable)) + "\\)";
				source = source.replaceAll(oclParam, cppParam);
			}
		} else if (node_.eClass().equals(UMLPackage.Literals.OUTPUT_PIN)) {
		    	
			source = getTargetFromActivityNode((ActivityNode) node_.getOwner());
		} else if (node_.eClass().equals(UMLPackage.Literals.VALUE_SPECIFICATION_ACTION)) {
			source = getValueFromValueSpecification(((ValueSpecificationAction) node_).getValue());
		} else if(node_.eClass().equals(UMLPackage.Literals.READ_VARIABLE_ACTION)) {
		    ReadVariableAction rA = (ReadVariableAction) node_;
		    source = rA.getVariable().getName();
		}
		else if(node_.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
		    source = (createCallOperationActionCode((org.eclipse.uml2.uml.CallOperationAction) node_));
		}
		else {
			System.out.println(node_.eClass().getName());
			// TODO just for
															// development debug
		}
		return source;
	}

	private String getTargetFromASFVA(AddStructuralFeatureValueAction node_) {
		String source = node_.getStructuralFeature().getName();
		String object = getTargetFromInputPin(node_.getObject());
		if (!object.isEmpty()) {
			source = object + ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node_.getObject())) + source;
		}
		return source;
	}

	private String getTargetFromRSFA(ReadStructuralFeatureAction node_) {
		String source = node_.getStructuralFeature().getName();
		String object = getTargetFromInputPin(node_.getObject());
		if (!object.isEmpty()) {
			source = object + ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node_.getObject())) + source;
		}
		return source;
	}

	private StringBuilder createSendSignalActionCode(org.eclipse.uml2.uml.SendSignalAction node_) {
		return ActivityTemplates.signalSend(node_.getSignal().getName(),
				getTargetFromInputPin(node_.getTarget(), false), getTypeFromInputPin(node_.getTarget()),
				ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node_.getTarget())),
				getParamNames(node_.getArguments()));
	}

	private String getTypeFromInputPin(InputPin inputPin_) {
		Type type = inputPin_.getType();
		String targetTypeName = "null";
		if (type != null) {
			targetTypeName = type.getName();
		} else if (inputPin_.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
			targetTypeName = getTypeFromSpecialAcivityNode(inputPin_.getIncomings().get(0).getSource());
		} else // inputPin_.eClass().equals(UMLPackage.Literals.VALUE_PIN)
		{
			//ValueSpecification valueSpec = ((ValuePin) inputPin_).getValue();
		}
		return targetTypeName;
	}

	private String getTypeFromSpecialAcivityNode(ActivityNode node_) {
		String targetTypeName = "null";
		// because the output pins not count as parent i have to if-else again
		// ...
		if (node_.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION)) {
			targetTypeName = getParentClass(node_.getActivity()).getName();
		}
		if (node_.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			targetTypeName = getTypeFromInputPin(((AddStructuralFeatureValueAction) node_).getObject());
		} else if (node_.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			targetTypeName = getTypeFromInputPin(((ReadStructuralFeatureAction) node_).getObject());
		} else if (node_.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
			targetTypeName = ((ActivityParameterNode) node_).getType().getName();
		} else if (node_.eClass().equals(UMLPackage.Literals.FORK_NODE)
				|| node_.eClass().equals(UMLPackage.Literals.DECISION_NODE)
				|| node_.eClass().equals(UMLPackage.Literals.JOIN_NODE)) {
			targetTypeName = getTypeFromSpecialAcivityNode(node_.getIncomings().get(0).getSource());
		} else {
			targetTypeName = "UNKNOWN_TARGET_TYPER_NAME";
			// TODO unknown for me, need the model
		}

		return targetTypeName;
	}

	private <ItemType extends Element> Class getParentClass(ItemType element_) {
		Element parent = element_.getOwner();
		while (!parent.eClass().equals(UMLPackage.Literals.CLASS)) {
			parent = parent.getOwner();
		}
		return (Class) parent;
	}

	private String createCallOperationActionCode(org.eclipse.uml2.uml.CallOperationAction node_) {
	    	if (node_.getTarget() == null) {
	    	    if(node_.getArguments().size() == 2) {
	    		return ActivityTemplates.stdLibOperationCall(node_.getOperation().getName(),
		    		    getTargetFromInputPin((node_.getArguments()).get(0)),getTargetFromInputPin((node_.getArguments()).get(1)));
	    	    }
	    	    return "";
	    	    
	    	}
		return ActivityTemplates.operationCall(getTargetFromInputPin(node_.getTarget(), false),
				ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node_.getTarget())),
				node_.getOperation().getName(), getParamNames(node_.getArguments()));
	}

	private List<String> getParamNames(List<InputPin> arguments_) {
		List<String> params = new ArrayList<String>();
		for (InputPin param : arguments_) {
			params.add(getTargetFromInputPin(param));
		}
		return params;
	}

	private LinkedList<ActivityNode> getNextNodes(ActivityNode node_) {
		ActivityNode currentNode = node_;
		LinkedList<ActivityNode> nextNodes = new LinkedList<ActivityNode>();
		if (node_.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
			currentNode = (ActivityNode) node_.getOwner();
			nextNodes.add(currentNode);
		}

		// direct output edges
		List<ActivityEdge> edges = currentNode.getOutgoings();
		// output edges from output pin
		List<OutputPin> outputPins = new LinkedList<OutputPin>();
		Shared.getTypedElements(outputPins, currentNode.getOwnedElements(), UMLPackage.Literals.OUTPUT_PIN);
		for (OutputPin pin : outputPins) {
			edges.addAll(pin.getOutgoings());
		}

		// add next nodes to the list
		for (ActivityEdge currentEgde : edges) {
			ActivityNode tmp = currentEgde.getTarget();
			if (tmp.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
				tmp = (ActivityNode) tmp.getOwner();
			}
			nextNodes.add(tmp);
		}
		return nextNodes;
	}

}