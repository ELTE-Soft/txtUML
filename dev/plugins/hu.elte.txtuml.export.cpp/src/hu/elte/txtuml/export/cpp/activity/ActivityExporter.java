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
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.StartObjectBehaviorAction;
import org.eclipse.uml2.uml.TestIdentityAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;

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
	
	private ActivityExportResult activityExportResult;
	
	private ICppCompilationUnit exportUser;
	
	public ActivityExporter(ICppCompilationUnit exportUser) {
		this.exportUser = exportUser;
	}

	public ActivityExportResult createFunctionBody(Behavior behavior) {
		if(behavior != null && behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
			return createFunctionBody((Activity) behavior);
		} else {
			return ActivityExportResult.emptyResult();
		}
	}
	
	String createActivityNodeCode(ActivityNode node) {

		StringBuilder source = new StringBuilder("");

		if (node.eClass().equals(UMLPackage.Literals.SEQUENCE_NODE)) {
			source.append(controlNodeExporter.createSequenceNodeCode((SequenceNode) node));
		} else if (node.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			AddStructuralFeatureValueAction asfva = (AddStructuralFeatureValueAction) node;
			source.append(ActivityTemplates.generalSetValue(activityExportResolver.getTargetFromASFVA(asfva),
					activityExportResolver.getTargetFromInputPin(asfva.getValue(), false), ActivityTemplates
							.getOperationFromType(asfva.getStructuralFeature().isMultivalued(), asfva.isReplaceAll())));
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
			CallOperationAction callAction = (CallOperationAction) node;
			if (callAction.getOperation().getName().equals(ActivityTemplates.GetSignalFunctionName)) {
				activityExportResult.setSignalReferenceContainment();
			}
			source.append(callOperationExporter.createCallOperationActionCode(callAction));

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
	
	private ActivityExportResult createFunctionBody(Activity activity){	
		assert(activity != null);
		activityExportResult = new ActivityExportResult();	
		init();
		ActivityNode startNode = null;
		for (ActivityNode node : activity.getOwnedNodes()) {
			if (node.eClass().equals(UMLPackage.Literals.INITIAL_NODE)) {
				startNode = node;
				break;
			}
		}

		activityExportResult.appendToSource(controlNodeExporter.createStructuredActivityNodeVariables(activity.getVariables()));
		activityExportResult.appendToSource(createActivityPartCode(startNode));
		activityExportResult.appendToSource(returnNodeExporter.createReturnParamaterCode());

		for (VariableInfo varInfo : userVariableExporter.getElements()) {
			if (!varInfo.isUsed()) {
				activityExportResult.reduceSource(ActivityTemplates.declareRegex(varInfo.getName()));
				activityExportResult.reduceSource(ActivityTemplates.setRegex(varInfo.getName()));
			}
		}
		return activityExportResult;
	}

	
	private void init() {

		tempVariableExporter = new OutVariableExporter();
		userVariableExporter = new UserVariableExporter();
		returnOutputsToCallActions = new HashMap<CallOperationAction, OutputPin>();
		objectMap = new HashMap<CreateObjectAction, String>();
		activityExportResolver = new ActivityNodeResolver(objectMap, returnOutputsToCallActions, tempVariableExporter,
				userVariableExporter);
		returnNodeExporter = new ReturnNodeExporter(activityExportResolver);
		callOperationExporter = new CallOperationExporter(tempVariableExporter, returnOutputsToCallActions,
				activityExportResolver, exportUser);
		linkActionExporter = new LinkActionExporter(tempVariableExporter, activityExportResolver);
		objectActionExporter = new ObjectActionExporter(tempVariableExporter, objectMap, activityExportResolver,
				exportUser);
		controlNodeExporter = new StructuredControlNodeExporter(this, activityExportResolver, userVariableExporter,
				returnNodeExporter);

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

}