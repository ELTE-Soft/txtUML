package hu.elte.txtuml.export.cpp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ExpansionKind;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.ObjectFlow;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.StartObjectBehaviorAction;
import org.eclipse.uml2.uml.TestIdentityAction;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;
import org.eclipse.uml2.uml.ExpansionRegion;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.ActivityTemplates.CreateObjectType;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

import hu.elte.txtuml.utils.Logger;


public class ActivityExporter {

	private Map<CreateObjectAction, String> objectMap;
	private Map<CallOperationAction, OutputPin> returnOutputsToCallActions;
	private Set<String> declaredTempVariables;
	private ActivityNode returnNode;
	private boolean containsSignalAcces;
	
	private OutVariableExporter tempVariableExporter;
	private UserVariableExporter userVariableExporter;
	private ActivityNodeResolver activityExportResolver;
	
	

	public ActivityExporter() {
		init();

	}

	public void init() {
		tempVariableExporter = new OutVariableExporter();
		userVariableExporter = new UserVariableExporter();
		activityExportResolver = new ActivityNodeResolver(objectMap,returnOutputsToCallActions,
				tempVariableExporter,userVariableExporter);
		
		declaredTempVariables = new HashSet<String>();
		objectMap = new HashMap<CreateObjectAction, String>();
		returnOutputsToCallActions = new HashMap<CallOperationAction, OutputPin>();
		containsSignalAcces = false;
		returnNode = null;

	}

	public String createfunctionBody(Activity activity_) {
		ActivityNode startNode = null;
		StringBuilder source = new StringBuilder("");
		for (ActivityNode node : activity_.getOwnedNodes()) {
			if (node.eClass().equals(UMLPackage.Literals.INITIAL_NODE)) {
				startNode = node;
				break;
			}
		}
		String reducedSource = createStructuredActivityNodeVariables(activity_.getVariables())
				+ createActivityPartCode(startNode) + createReturnParamaterCode(activity_);
		for (VariableInfo varInfo : userVariableExporter.getElements() ) {
			if (!varInfo.isUsed()) {
				reducedSource = reducedSource.replaceAll(ActivityTemplates.declareRegex(varInfo.getName()), "");
				reducedSource = reducedSource.replaceAll(ActivityTemplates.setRegex(varInfo.getName()), "");
			}
		}
		source.append(reducedSource);
		return source.toString();
	}

	public boolean isContainsSignalAcces() {
		return containsSignalAcces;
	}

	private String createReturnParamaterCode(Activity activity_) {
		if (returnNode != null) {
			return ActivityTemplates.returnTemplates(activityExportResolver.getTargetFromActivityNode(returnNode));
		} else {
			return "";
		}

	}

	private String createStructuredActivityNodeVariables(EList<Variable> variables) {
		StringBuilder source = new StringBuilder("");
		for (Variable variable : variables) {
			source.append(createVariable(variable));
		}

		return source.toString();
	}

	private String createVariable(Variable variable) {
		String type = "!!!UNKNOWNTYPE!!!";
		if (variable.getType() != null) {
			type = variable.getType().getName();
		}
		userVariableExporter.exportNewVariable(variable);

		return GenerationTemplates.variableDecl(type, userVariableExporter.getRealVariableName(variable),
				variable.getType().eClass().equals(UMLPackage.Literals.SIGNAL));
	}

	private String createActivityPartCode(ActivityNode startNode_) {
		return createActivityPartCode(startNode_, null, new ArrayList<ActivityNode>());
	}

	private String createActivityPartCode(ActivityNode startNode_, ActivityNode stopNode_,
			List<ActivityNode> finishedControlNodes_) {
		StringBuilder source = new StringBuilder("");
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
				source.append(createActivityNodeCode(currentNode));

				for (ActivityNode node : getNextNodes(currentNode)) {
					if (!finishedControlNodes.contains(node) && !nodeList.contains(node)) {
						nodeList.add(node);
					}
				}
			}
		}
		return source.toString();
	}

	private StringBuilder createActivityNodeCode(ActivityNode node) {

		StringBuilder source = new StringBuilder("");

		if (node.eClass().equals(UMLPackage.Literals.SEQUENCE_NODE)) {
			SequenceNode seqNode = (SequenceNode) node;
			if (returnNode == null) {
				for (ActivityEdge aEdge : seqNode.getContainedEdges()) {
					if (aEdge.eClass().equals(UMLPackage.Literals.OBJECT_FLOW)) {
						ObjectFlow objectFlow = (ObjectFlow) aEdge;
						if (objectFlow.getTarget().eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
							ActivityParameterNode parameterNode = (ActivityParameterNode) objectFlow.getTarget();
							if (parameterNode.getParameter().getDirection()
									.equals(ParameterDirectionKind.RETURN_LITERAL)) {
								returnNode = objectFlow.getSource();
							}
						}
					}
				}
			}

			source.append(createStructuredActivityNodeVariables(seqNode.getVariables()));
			for (ActivityNode aNode : seqNode.getNodes()) {
				source.append(createActivityNodeCode(aNode));

			}

		} else if (node.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			AddStructuralFeatureValueAction asfva = (AddStructuralFeatureValueAction) node;
			source.append(ActivityTemplates.generalSetValue(activityExportResolver.getTargetFromASFVA(asfva),
					activityExportResolver.getTargetFromInputPin(asfva.getValue(), false), ActivityTemplates
							.getOperationFromType(asfva.getStructuralFeature().isMultivalued(), asfva.isReplaceAll())));
		} else if (node.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION)) {
			source.append(createCreateObjectActionCode((CreateObjectAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.CREATE_LINK_ACTION)) {
			source.append(createLinkActionCode((CreateLinkAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.DESTROY_LINK_ACTION)) {
			source.append(createDestroyLinkActionCode((DestroyLinkAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.READ_LINK_ACTION)) {
			source.append(createReadLinkActionCode((ReadLinkAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.SEND_OBJECT_ACTION)) {
			source.append(createSendSignalActionCode((SendObjectAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.START_CLASSIFIER_BEHAVIOR_ACTION)) {
			source.append(createStartObjectActionCode((StartClassifierBehaviorAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.START_OBJECT_BEHAVIOR_ACTION)) {
			source.append(createStartObjectActionCode((StartObjectBehaviorAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
			source.append(createCallOperationActionCode((CallOperationAction) node));

		} else if (node.eClass().equals(UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION)) {
			AddVariableValueAction avva = (AddVariableValueAction) node;
			source.append(ActivityTemplates.generalSetValue(userVariableExporter.getRealVariableName(avva.getVariable()),
					activityExportResolver.getTargetFromInputPin(avva.getValue()),
					ActivityTemplates.getOperationFromType(avva.getVariable().isMultivalued(), avva.isReplaceAll())));

		} else if (node.eClass().equals(UMLPackage.Literals.LOOP_NODE)) {
			source.append(createCycleCode((LoopNode) node));
		} else if (node.eClass().equals(UMLPackage.Literals.EXPANSION_REGION)) {
			source.append(createExpansionRegaionCode((ExpansionRegion) node));
		} else if (node.eClass().equals(UMLPackage.Literals.CONDITIONAL_NODE)) {
			source.append(createConditionalCode(((ConditionalNode) node)));
		} else if (node.eClass().equals(UMLPackage.Literals.VALUE_SPECIFICATION_ACTION)) {

		} else if (node.eClass().equals(UMLPackage.Literals.DESTROY_OBJECT_ACTION)) {
			source.append(createDestroyObjectActionCode((DestroyObjectAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.TEST_IDENTITY_ACTION)) {
			source.append(createTestIdentityActionCode((TestIdentityAction) node));
		}

		return source;
	}

	private String createExpansionRegaionCode(ExpansionRegion node) {
		String source = "UNKNOWN_EXPANSION_REAGION";

		if (node.getMode().equals(ExpansionKind.ITERATIVE_LITERAL)) {

			Variable iterativeVar = node.getVariables().get(0);
			EList<ActivityNode> nodes = node.getNodes();
			StringBuilder body = createActivityNodeCode(nodes.get(nodes.size() - 1));
			StringBuilder inits = new StringBuilder("");
			for (int i = 0; i < nodes.size() - 1; i++) {
				inits.append(createActivityNodeCode(nodes.get(i)));
			}

			String collection = activityExportResolver.getTargetFromActivityNode(
					node.getInputElements().get(0).getIncomings().get(0).getSource());
			source = ActivityTemplates.foreachCycle(iterativeVar.getType().getName(), iterativeVar.getName(),
					collection, body.toString(), inits.toString());
		}

		return source;

	}

	private String createReadLinkActionCode(ReadLinkAction readLinkNode) {

		Property otherMember = null;
		for (LinkEndData end : readLinkNode.getEndData()) {
			if (end.getValue() == null) {
				otherMember = end.getEnd();
			}
		}
		tempVariableExporter.exportOutputPinToMap(readLinkNode.getResult());
		
		String target = readLinkNode.getInputValues().size() > 0 ? 
				activityExportResolver.getTargetFromInputPin(readLinkNode.getInputValues().get(0)) : ActivityTemplates.Self;

		return ActivityTemplates.defineAndAddToCollection(otherMember.getType().getName(),
				tempVariableExporter.getRealVariableName(readLinkNode.getResult()),
				ActivityTemplates.selectAllTemplate(target,otherMember.getAssociation().getName(), otherMember.getName()));
	}

	private String createLinkActionCode(CreateLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);

		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());

		return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
				firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
				secondLinkEnd.getEnd().getName(), GenerationTemplates.LinkFunctionType.Link);
	}

	private Object createDestroyLinkActionCode(DestroyLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);

		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());

		return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
				firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
				secondLinkEnd.getEnd().getName(), GenerationTemplates.LinkFunctionType.Unlink);
	}

	private String createDestroyObjectActionCode(DestroyObjectAction node_) {
		return ActivityTemplates.deleteObject(activityExportResolver.getTargetFromInputPin(node_.getTarget()));
	}

	private String createStartObjectActionCode(StartClassifierBehaviorAction node_) {
		return ActivityTemplates.startObject(activityExportResolver.getTargetFromInputPin(node_.getObject()));
	}

	private String createStartObjectActionCode(StartObjectBehaviorAction node_) {
		return ActivityTemplates.startObject(activityExportResolver.getTargetFromInputPin(node_.getObject()));
	}

	private String createCreateObjectActionCode(CreateObjectAction node_) {
		String type = node_.getClassifier().getName();

		ActivityTemplates.CreateObjectType objectType;
		if (node_.getClassifier().eClass().equals(UMLPackage.Literals.SIGNAL)) {
			objectType = ActivityTemplates.CreateObjectType.Signal;
		} else {
			objectType = CreateObjectType.Class;
		}

		tempVariableExporter.exportOutputPinToMap(node_.getResult());
		String name = tempVariableExporter.getRealVariableName(node_.getResult());
		objectMap.put(node_, name);

		return ActivityTemplates.createObject(type, name, objectType);
	}

	private StringBuilder createCycleCode(LoopNode loopNode) {
		StringBuilder source = new StringBuilder("");
		source.append(createStructuredActivityNodeVariables(loopNode.getVariables()));

		for (ExecutableNode initNode : loopNode.getSetupParts()) {
			source.append(createActivityNodeCode(initNode));
		}

		StringBuilder condition = new StringBuilder("");
		for (ExecutableNode condNode : loopNode.getTests()) {
			condition.append(createActivityNodeCode(condNode));
		}
		source.append(condition);

		StringBuilder body = new StringBuilder("");
		for (ExecutableNode bodyNode : loopNode.getBodyParts()) {
			body.append(createActivityNodeCode(bodyNode));
		}

		StringBuilder recalulcateCondition = new StringBuilder("");
		for (ExecutableNode condNode : loopNode.getTests()) {
			recalulcateCondition.append(createActivityNodeCode(condNode));
		}

		source.append(ActivityTemplates.whileCycle(activityExportResolver.getTargetFromActivityNode(loopNode.getDecider()),
				body.toString() + "\n" + recalulcateCondition.toString()));

		return source;
	}

	private StringBuilder createConditionalCode(ConditionalNode conditionalNode) {
		StringBuilder source = new StringBuilder("");
		StringBuilder tests = new StringBuilder("");
		StringBuilder bodies = new StringBuilder("");

		for (Clause clause : conditionalNode.getClauses()) {
			for (ExecutableNode test : clause.getTests()) {
				tests.append(createActivityNodeCode(test));
			}

			String cond = activityExportResolver.getTargetFromActivityNode(clause.getDecider());
			StringBuilder body = new StringBuilder("");
			for (ExecutableNode node : clause.getBodies()) {
				body.append(createActivityNodeCode(node));
			}

			bodies.append(ActivityTemplates.simpleIf(cond, body.toString()));

		}

		source.append(tests);
		source.append(bodies);
		return source;
	}

	private String createTestIdentityActionCode(TestIdentityAction node) {
		InputPin firstArgument = node.getInputs().get(0);
		InputPin secondArgument = node.getInputs().get(1);
		tempVariableExporter.exportOutputPinToMap(node.getResult());

		String val = ActivityTemplates.isEqualTesting(activityExportResolver.getTargetFromInputPin(firstArgument),
				activityExportResolver.getTargetFromInputPin(secondArgument));

		return addValueToTemporalVariable(node.getResult().getType().getName(), 
				tempVariableExporter.getRealVariableName(node.getResult()),
				val);
	}



	private String createSendSignalActionCode(SendObjectAction sendObjectAction) {

		String target = activityExportResolver.getTargetFromInputPin(sendObjectAction.getTarget());
		String singal = activityExportResolver.getTargetFromInputPin(sendObjectAction.getRequest());

		return ActivityTemplates.signalSend(target, singal);

	}

	private StringBuilder createCallOperationActionCode(CallOperationAction node_) {
		StringBuilder source = new StringBuilder("");
		tempVariableExporter.exportAllOutputPinToMap(node_.getOutputs());
		OutputPin returnPin = searchReturnPin(node_.getResults(), node_.getOperation().outputParameters());
		if (returnPin != null)
			returnOutputsToCallActions.put(node_, returnPin);

		if (Shared.isConstructor(node_.getOperation())) {

			InputPin target = node_.getTarget() == null ? node_.getArguments().get(0) : node_.getTarget();
			EList<InputPin> arguments = node_.getArguments();
			arguments.remove(target);
			return source.append(ActivityTemplates.blockStatement(createConstructorCallAction(target, arguments)));
		}

		if (node_.getOperation().getName().equals(ActivityTemplates.GetSignalFunctionName)) {
			containsSignalAcces = true;
			return ActivityTemplates.getRealSignal(returnPin.getType().getName(), 
					tempVariableExporter.getRealVariableName(returnPin));
		}

		if (isStdLibOperation(node_)) {

			String val = "";

			if (ActivityTemplates.Operators.isStdLibFunction(node_.getOperation().getName())) {

				EList<OutputPin> outParamaterPins = node_.getResults();
				outParamaterPins.remove(returnPin);
				source.append(declareAllOutTempParameter(outParamaterPins));
				List<String> parameterVariables = new ArrayList<String>(getParamNames(node_.getArguments()));
				addOutParametrsToList(parameterVariables, outParamaterPins);

				val = ActivityTemplates.simpleFunctionCall(node_.getOperation().getName(), parameterVariables);

			} else if (node_.getArguments().size() == 2) {

				val = ActivityTemplates.stdLibOperationCall(node_.getOperation().getName(),
						activityExportResolver.getTargetFromInputPin((node_.getArguments()).get(0)),
						activityExportResolver.getTargetFromInputPin((node_.getArguments()).get(1)));

			} else if (node_.getArguments().size() == 1) {
				val = ActivityTemplates.stdLibOperationCall(node_.getOperation().getName(),
						activityExportResolver.getTargetFromInputPin((node_.getArguments()).get(0)));

			}

			if (node_.getOperation().getType() != null) {
				if (!node_.getOperation().isTemplate()) {
					source.append(addValueToTemporalVariable(node_.getOperation().getType().getName(),
							tempVariableExporter.getRealVariableName(returnPin), val));

				} else {
					if (node_.getOutgoings().size() > 0) {
						source.append(addValueToTemporalVariable(
								((InputPin) node_.getOutgoings().get(0).getTarget()).getType().getName(),
								tempVariableExporter.getRealVariableName(returnPin), val));
					} else if (returnPin.getOutgoings().size() > 0) {
						source.append(addValueToTemporalVariable(
								((InputPin) returnPin.getOutgoings().get(0).getTarget()).getType().getName(),
								tempVariableExporter.getRealVariableName(returnPin), val));

					}
				}

			} else {
				source.append(ActivityTemplates.invokeProcedure(node_.getOperation().getName(),
						getParamNames(node_.getArguments())));
			}

		} else {

			String val = ActivityTemplates.operationCall(activityExportResolver.getTargetFromInputPin(node_.getTarget(), false),
					ActivityTemplates.accesOperatoForType(activityExportResolver.getTypeFromInputPin(node_.getTarget())),
					node_.getOperation().getName(), getParamNames(node_.getArguments()));
			if (returnPin != null) {
				source.append(addValueToTemporalVariable(node_.getOperation().getType().getName(),
						tempVariableExporter.getRealVariableName(returnPin), val));
			} else {

				source.append(ActivityTemplates.blockStatement(val));
			}

		}
		return source;

	}

	private void addOutParametrsToList(List<String> parameterVariables, EList<OutputPin> outParamaterPins) {
		for (OutputPin outPin : outParamaterPins) {
			parameterVariables.add(tempVariableExporter.getRealVariableName(outPin));
		}
	}

	private StringBuilder declareAllOutTempParameter(EList<OutputPin> outParamaterPins) {
		StringBuilder declerations = new StringBuilder("");
		for (OutputPin outPin : outParamaterPins) {
			declerations.append(
					GenerationTemplates.variableDecl(outPin.getType().getName(), tempVariableExporter.getRealVariableName(outPin), false));
		}

		return declerations;
	}

	private OutputPin searchReturnPin(EList<OutputPin> results, EList<Parameter> outputParameters) {
		for (int i = 0; i < Math.min(results.size(), outputParameters.size()); i++) {

			if (outputParameters.get(i).getDirection().equals(ParameterDirectionKind.RETURN_LITERAL)) {
				return results.get(i);
			}
		}
		return null;
	}



	private String createConstructorCallAction(InputPin target, EList<InputPin> arguments) {

		return ActivityTemplates.constructorCall(activityExportResolver.getTargetFromInputPin(target, false),
				target.getType().getName(), target.getType().eClass().equals(UMLPackage.Literals.SIGNAL)
						? ActivityTemplates.CreateObjectType.Signal : ActivityTemplates.CreateObjectType.Class,
				getParamNames(arguments));
	}

	private boolean isStdLibOperation(CallOperationAction node_) {
		return node_.getTarget() == null;
	}

	private List<String> getParamNames(List<InputPin> arguments_) {
		List<String> params = new ArrayList<String>();
		for (InputPin param : arguments_) {
			params.add(activityExportResolver.getTargetFromInputPin(param));
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
	
	private String addValueToTemporalVariable(String type, String var, String value) {
		if (declaredTempVariables.contains(var)) {
			return ActivityTemplates.simpleSetValue(var, value);
		} else {
			declaredTempVariables.add(var);
			return ActivityTemplates.addVariableTemplate(type, var, value);

		}
}

}