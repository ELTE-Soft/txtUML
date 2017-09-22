package hu.elte.txtuml.export.cpp.activity;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralReal;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.ValueSpecificationAction;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.utils.Logger;

class ActivityNodeResolver {
	
	private Map<CreateObjectAction, String> objectMap;
	private Map<CallOperationAction, OutputPin> returnOutputsToCallActions;	
	private OutVariableExporter tempVariableExporter;
	private UserVariableExporter userVariableExporter;
	
	public ActivityNodeResolver(Map<CreateObjectAction, String> objectMap,Map<CallOperationAction, OutputPin> returnOutputsToCallActions,
			OutVariableExporter tempVariableExporter, UserVariableExporter userVariableExporter) {
	    this.objectMap = objectMap;
		this.returnOutputsToCallActions = returnOutputsToCallActions;
		this.tempVariableExporter = tempVariableExporter;
		this.userVariableExporter = userVariableExporter;
	}
	
	public String getTargetFromActivityNode(ActivityNode node) {
		if(node == null) {
			Logger.sys.error("This should not happen..");
		}

		String source = "UNHANDLED_ACTIVITYNODE";
		if (node.eClass().equals(UMLPackage.Literals.FORK_NODE) || node.eClass().equals(UMLPackage.Literals.JOIN_NODE)
				|| node.eClass().equals(UMLPackage.Literals.DECISION_NODE)) {
			source = getTargetFromActivityNode(node.getIncomings().get(0).getSource());
		} else if (node.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			source = getTargetFromInputPin(((AddStructuralFeatureValueAction) node).getObject());
		} else if (node.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			source = getTargetFromRSFA((ReadStructuralFeatureAction) node);
		} else if (node.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
			EClass ec = node.getActivity().getOwner().eClass();
			String paramName = ((ActivityParameterNode) node).getParameter().getName();
			if (ec.equals(UMLPackage.Literals.TRANSITION)) {
				source = ActivityTemplates.transitionActionParameter(paramName);
			} else // the parameter is a function parameter
			{
				source = GenerationTemplates.paramName(paramName);
			}

		} else if (node.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION)) {
			source = objectMap.get(node);
		} else if (node.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION)) {
			source = ActivityTemplates.SelfLiteral;

		} else if (node.eClass().equals(UMLPackage.Literals.READ_LINK_ACTION)) {
			source = getTargetFromActivityNode(((ReadLinkAction) node).getResult());

		} else if (node.eClass().equals(UMLPackage.Literals.OUTPUT_PIN)) {
			OutputPin outPin = (OutputPin) node;
			source = tempVariableExporter.isOutExported(outPin) ? 
					tempVariableExporter.getRealVariableName(outPin):
					 getTargetFromActivityNode((ActivityNode) node.getOwner());

		} else if (node.eClass().equals(UMLPackage.Literals.VALUE_SPECIFICATION_ACTION)) {
			source = getValueFromValueSpecification(((ValueSpecificationAction) node).getValue());
		} else if (node.eClass().equals(UMLPackage.Literals.READ_VARIABLE_ACTION)) {

			ReadVariableAction rA = (ReadVariableAction) node;
			userVariableExporter.modifyVariableInfo(rA.getVariable());
			source = userVariableExporter.getRealVariableName(rA.getVariable());
		} else if (node.eClass().equals(UMLPackage.Literals.SEQUENCE_NODE)) {
			SequenceNode seqNode = (SequenceNode) node;
			int lastIndex = seqNode.getNodes().size() - 1;
			source = getTargetFromActivityNode(seqNode.getNodes().get(lastIndex));

		} else if (node.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
			CallOperationAction callAction = (CallOperationAction) node;
			source = tempVariableExporter.getRealVariableName(returnOutputsToCallActions.get(callAction));
		} else {
			Logger.sys.error("Unhandled activity node: " + node.getName());
		}
		return source;
	}
	
	public String getTargetFromASFVA(AddStructuralFeatureValueAction node) {
		String source = node.getStructuralFeature().getName();
		String object = getTargetFromInputPin(node.getObject());
		if (!object.isEmpty()) {
			source = object + ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node.getObject())) + source;
		}
		return source;
	}
	
	private String getTargetFromRSFA(ReadStructuralFeatureAction node) {
		String source = node.getStructuralFeature().getName();
		String object = getTargetFromInputPin(node.getObject());
		if (!object.isEmpty()) {
			source = object + ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node.getObject())) + source;
		}
		return source;
	}
	
	public String getTargetFromInputPin(InputPin node) {
		return getTargetFromInputPin(node, true);
	}
	
	public String getTargetFromInputPin(InputPin node, Boolean recursive) {
		String source = "UNKNOWN_TYPE_FROM_VALUEPIN";
		if (node.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {

			if (node.getIncomings().size() > 0) {
				source = getTargetFromActivityNode(node.getIncomings().get(0).getSource());
			}

		} else if (node.eClass().equals(UMLPackage.Literals.VALUE_PIN)) {

			ValueSpecification valueSpec = ((ValuePin) node).getValue();
			if (valueSpec != null) {
				source = getValueFromValueSpecification(valueSpec);
			} else if (node.getIncomings().size() > 0) {
				source = getTargetFromActivityNode(node.getIncomings().get(0).getSource());
			}

		}
		return source;
	}
	
	public String getTypeFromInputPin(InputPin inputPin) {
		Type type = inputPin.getType();
		String targetTypeName = "UNKNOWN_TARGET_TYPE_NAME";
		if (type != null) {
			targetTypeName = type.getName();
		} else if (inputPin.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {
			targetTypeName = getTypeFromSpecialAcivityNode(inputPin.getIncomings().get(0).getSource());
		} else if (inputPin.eClass().equals(UMLPackage.Literals.VALUE_PIN)) {
			// ValueSpecification valueSpec = ((ValuePin) inputPin_).getValue();
		}
		return targetTypeName;
	}
	
	private String getTypeFromSpecialAcivityNode(ActivityNode node) {
		String targetTypeName;
		// because the output pins not count as parent i have to if-else again
		// ...
		if (node.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION)) {
			targetTypeName = getParentClass(node.getActivity()).getName();
		}
		if (node.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			targetTypeName = getTypeFromInputPin(((AddStructuralFeatureValueAction) node).getObject());
		} else if (node.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			targetTypeName = getTypeFromInputPin(((ReadStructuralFeatureAction) node).getObject());
		} else if (node.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
			targetTypeName = ((ActivityParameterNode) node).getType().getName();
		} else {
			targetTypeName = "UNKNOWN_TARGET_TYPER_NAME";
			// TODO unknown for me, need the model
		}

		return targetTypeName;
	}
	
	private <ItemType extends Element> Class getParentClass(ItemType element) {
		Element parent = element.getOwner();
		while (!parent.eClass().equals(UMLPackage.Literals.CLASS)) {
			parent = parent.getOwner();
		}
		return (Class) parent;
	}
	
	private String getValueFromValueSpecification(ValueSpecification valueSpec) {
		String source = "";
		if (valueSpec.eClass().equals(UMLPackage.Literals.LITERAL_INTEGER)) {
			source = ((Integer) ((LiteralInteger) valueSpec).getValue()).toString();
		} else if(valueSpec.eClass().equals(UMLPackage.Literals.LITERAL_REAL)) {
			source = ((Double)  ((LiteralReal) valueSpec).getValue()).toString();
		}
		else if (valueSpec.eClass().equals(UMLPackage.Literals.LITERAL_BOOLEAN)) {
			source = ((Boolean) ((LiteralBoolean) valueSpec).isValue()).toString();
		} else if (valueSpec.eClass().equals(UMLPackage.Literals.LITERAL_STRING)) {
			source = "\"" + ((LiteralString) valueSpec).getValue() + "\"";
		
		} else if(valueSpec.eClass().equals(UMLPackage.Literals.LITERAL_NULL)) {
			source = ActivityTemplates.NullPtrLiteral;
		}
		else {
			source = "UNHANDLED_VALUEPIN_VALUETYPE";
		}
		return source;
	}
}
