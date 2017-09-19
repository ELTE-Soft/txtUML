package hu.elte.txtuml.export.cpp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.StartObjectBehaviorAction;
import org.eclipse.uml2.uml.TestIdentityAction;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.Class;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;

//import hu.elte.txtuml.utils.Logger;

public class ActivityExporter {

	private Map<CreateObjectAction, String> objectMap;
	private Map<CallOperationAction, OutputPin> returnOutputsToCallActions;

	private OutVariableExporter tempVariableExporter;
	private UserVariableExporter userVariableExporter;
	private ActivityNodeResolver activityExportResolver;
	private StructuredControlNodeExporter controlNodeExporter;
	private CallOperationExporter callOperationExporter;
	private LinkActionExporter linkActionExporter;
	private ObjectActionExporter objectActionExporter;
	private ReturnNodeExporter returnNodeExporter;

	private List<String> createdClassDependencies;

	public ActivityExporter() {
	}

	private void init() {

		createdClassDependencies = new LinkedList<String>();

		tempVariableExporter = new OutVariableExporter();
		userVariableExporter = new UserVariableExporter();
		returnOutputsToCallActions = new HashMap<CallOperationAction, OutputPin>();
		objectMap = new HashMap<CreateObjectAction, String>();
		activityExportResolver = new ActivityNodeResolver(objectMap, returnOutputsToCallActions, tempVariableExporter,
				userVariableExporter);
		returnNodeExporter = new ReturnNodeExporter(activityExportResolver);
		callOperationExporter = new CallOperationExporter(tempVariableExporter, returnOutputsToCallActions,
				activityExportResolver);
		linkActionExporter = new LinkActionExporter(tempVariableExporter, activityExportResolver);
		objectActionExporter = new ObjectActionExporter(tempVariableExporter, objectMap, activityExportResolver,
				createdClassDependencies);
		controlNodeExporter = new StructuredControlNodeExporter(this, activityExportResolver, userVariableExporter,
				returnNodeExporter);

	}

	public String createFunctionBody(Activity activity) {
		init();
		ActivityNode startNode = null;
		StringBuilder source = new StringBuilder("");
		for (ActivityNode node : activity.getOwnedNodes()) {
			if (node.eClass().equals(UMLPackage.Literals.INITIAL_NODE)) {
				startNode = node;
				break;
			}
		}

		source.append(controlNodeExporter.createStructuredActivityNodeVariables(activity.getVariables()));
		source.append(createActivityPartCode(startNode));
		source.append(returnNodeExporter.createReturnParamaterCode());

		String reducedSource = source.toString();
		for (VariableInfo varInfo : userVariableExporter.getElements()) {
			if (!varInfo.isUsed()) {
				reducedSource = reducedSource.replaceAll(ActivityTemplates.declareRegex(varInfo.getName()), "");
				reducedSource = reducedSource.replaceAll(ActivityTemplates.setRegex(varInfo.getName()), "");
			}
		}
		return reducedSource;
	}

	public boolean isContainsSignalAccess() {
		return callOperationExporter.isUsedSignalParameter();
	}

	public boolean isContainsTimerOperation() {
		return callOperationExporter.isInvokedTimerOperation();
	}

	public List<String> getAdditionalClassDependencies() {
		return createdClassDependencies;
	}

	private String createActivityPartCode(ActivityNode startNode) {
		return createActivityPartCode(startNode, null, new ArrayList<ActivityNode>());
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

	private LinkedList<ActivityNode> getNextNodes(ActivityNode node) {
		ActivityNode currentNode = node;
		LinkedList<ActivityNode> nextNodes = new LinkedList<ActivityNode>();
		if (node.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
			currentNode = (ActivityNode) node.getOwner();
			nextNodes.add(currentNode);
		}

		// direct output edges
		List<ActivityEdge> edges = currentNode.getOutgoings();
		// output edges from output pin
		List<OutputPin> outputPins = new LinkedList<OutputPin>();
		CppExporterUtils.getTypedElements(outputPins, UMLPackage.Literals.OUTPUT_PIN, node.getOwnedElements());
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

	String createActivityNodeCode(ActivityNode node) {

		StringBuilder source = new StringBuilder("");

		if (node.eClass().equals(UMLPackage.Literals.SEQUENCE_NODE)) {
			source.append(controlNodeExporter.createSequenceNodeCode((SequenceNode) node));
		} else if (node.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			AddStructuralFeatureValueAction asfva = (AddStructuralFeatureValueAction) node;
			source.append(createAddStructuralFeatureActionCode(asfva));
		} else if (node.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION)) {
			source.append(objectActionExporter.createCreateObjectActionCode((CreateObjectAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.CREATE_LINK_ACTION)) {
			source.append(linkActionExporter.createLinkActionCode((CreateLinkAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.DESTROY_LINK_ACTION)) {
			source.append(linkActionExporter.createDestroyLinkActionCode((DestroyLinkAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.READ_LINK_ACTION)) {
			source.append(linkActionExporter.createReadLinkActionCode((ReadLinkAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.SEND_OBJECT_ACTION)) {
			source.append(objectActionExporter.createSendSignalActionCode((SendObjectAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.START_CLASSIFIER_BEHAVIOR_ACTION)) {
			source.append(objectActionExporter.createStartObjectActionCode((StartClassifierBehaviorAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.START_OBJECT_BEHAVIOR_ACTION)) {
			source.append(objectActionExporter.createStartObjectActionCode((StartObjectBehaviorAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
			source.append(callOperationExporter.createCallOperationActionCode((CallOperationAction) node));

		} else if (node.eClass().equals(UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION)) {
			AddVariableValueAction avva = (AddVariableValueAction) node;
			source.append(ActivityTemplates.generalSetValue(
					userVariableExporter.getRealVariableName(avva.getVariable()),
					activityExportResolver.getTargetFromInputPin(avva.getValue()),
					ActivityTemplates.getOperationFromType(avva.getVariable().isMultivalued(), avva.isReplaceAll())));

		} else if (node.eClass().equals(UMLPackage.Literals.LOOP_NODE)) {
			source.append(controlNodeExporter.createLoopNodeCode((LoopNode) node));
		} else if (node.eClass().equals(UMLPackage.Literals.EXPANSION_REGION)) {
			source.append(controlNodeExporter.createExpansionRegionCode((ExpansionRegion) node));
		} else if (node.eClass().equals(UMLPackage.Literals.CONDITIONAL_NODE)) {
			source.append(controlNodeExporter.createConditionalCode(((ConditionalNode) node)));
		} else if (node.eClass().equals(UMLPackage.Literals.VALUE_SPECIFICATION_ACTION)) {
		} else if (node.eClass().equals(UMLPackage.Literals.DESTROY_OBJECT_ACTION)) {
			source.append(objectActionExporter.createDestroyObjectActionCode((DestroyObjectAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.TEST_IDENTITY_ACTION)) {
			source.append(callOperationExporter.createTestIdentityActionCode((TestIdentityAction) node));
		}

		return source.toString();
	}
	
	private String createAddStructuralFeatureActionCode(AddStructuralFeatureValueAction asfva) {
		String source = "";
		String target = activityExportResolver.getTargetFromASFVA(asfva);
		String value = activityExportResolver.getTargetFromInputPin(asfva.getValue(), false);
		if(isPortConnection(asfva)) {
			Port targetPort = (Port) asfva.getStructuralFeature();
			Port valuePort =  (Port) 
					((ReadStructuralFeatureAction) 
							((OutputPin) asfva.getValue().getIncomings().get(0).getSource()).getOwner()).getStructuralFeature();
			String connectOperation = "";
			switch(getRelation(targetPort, valuePort)) {
			case Child:
				connectOperation = PortTemplates.InnerConnectionSetter;
				break;
			case Parent:
				connectOperation = PortTemplates.DelegationConnectionSetter;
				break;
			case Sublings:
				connectOperation = PortTemplates.AssemblyConnectionSetter;
				break;
			default:
				break;
			
			}
			source = ActivityTemplates.blockStatement(ActivityTemplates.operationCallOnPointerVariable(target, connectOperation, Arrays.asList(value)));
			
		}
		else {
			source = ActivityTemplates.generalSetValue(activityExportResolver.getTargetFromASFVA(asfva),
					activityExportResolver.getTargetFromInputPin(asfva.getValue(), false), ActivityTemplates
							.getOperationFromType(asfva.getStructuralFeature().isMultivalued(), asfva.isReplaceAll()));
		}

		return source;
	}
	
	private Boolean isPortConnection(AddStructuralFeatureValueAction asfva) {
		return asfva.getStructuralFeature().eClass().equals(UMLPackage.Literals.PORT);
	}
	
	enum Relation {
		Parent,
		Child,
		Sublings
	}
	
	/*private List<Association> filterClassAssocations(Class cls) {
		Model m = cls.getModel();
		List<Association> associationList = new ArrayList<Association>();
		CppExporterUtils.getTypedElements(associationList, UMLPackage.Literals.ASSOCIATION, m.allOwnedElements());
		return associationList.stream().filter(
				a -> a.getMemberEnds().stream().anyMatch(e -> e.getType().equals(cls))).collect(Collectors.toList());
	}*/
	
	private Relation getRelation(Port p1, Port p2) {
		Class p1Class = p1.getClass_();
		Class p2Class = p2.getClass_();
		//Model m = p1Class.getModel();
		if(p1Class.getOwnedAttributes().stream().anyMatch(p -> p.getType().equals(p2Class))) {
			return Relation.Child;
		} else if(p2Class.getOwnedAttributes().stream().anyMatch(p -> p.getType().equals(p1Class))) {
			return Relation.Parent;
		} else {
			return Relation.Sublings;
		}
		/*List<Association> p1ClassAssociations = filterClassAssocations(p1Class);
		List<Association> p2ClassAssociations = filterClassAssocations(p2Class);*/
		
	}
	

}