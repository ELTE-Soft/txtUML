package hu.elte.txtuml.export.cpp.activity;

import java.util.Collections;
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

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.CollectionNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.utils.Logger;


class ActivityNodeResolver {
	private ActivityExporter activityExporter;
	private Map<CreateObjectAction, String> objectMap;
	private Map<CallOperationAction, OutputPin> returnOutputsToCallActions;	
	private OutVariableExporter tempVariableExporter;
	private UserVariableExporter userVariableExporter;
	
	static class ActivityResolveResult {
		
		
		static ActivityResolveResult createCopiedResult(ActivityResolveResult result) {
			return new ActivityResolveResult(result.getReferenceResultCode(), result.getDeclaredVarCodes());
		}
		static ActivityResolveResult createSimpleResult (String reference) {
			return new ActivityResolveResult(reference, "");
		}
		
		static ActivityResolveResult createComplexResult(String reference, String extras) {
			return new ActivityResolveResult (reference, extras);
		}
		
		private ActivityResolveResult (String referenceResultCode, String declaredVarsCode) {
			this.referenceResultCode = referenceResultCode;
			this.declaredVarsCode = declaredVarsCode;
		}
		
		public String getReferenceResultCode() {
			return referenceResultCode;
		}
		public String getDeclaredVarCodes() {
			return declaredVarsCode;
		}
		private String referenceResultCode = "";
		private String declaredVarsCode = "";
	}
	
	public ActivityNodeResolver(ActivityExporter activityExporter, Map<CreateObjectAction, String> objectMap,Map<CallOperationAction, OutputPin> returnOutputsToCallActions,
			OutVariableExporter tempVariableExporter, UserVariableExporter userVariableExporter) {
		this.activityExporter =  activityExporter;
	    this.objectMap = objectMap;
		this.returnOutputsToCallActions = returnOutputsToCallActions;
		this.tempVariableExporter = tempVariableExporter;
		this.userVariableExporter = userVariableExporter;
	}
	
	public ActivityResolveResult getTargetFromActivityNode(ActivityNode node, boolean conditionalExpression) {
		if(node == null) {
			Logger.sys.error("This should not happen..");
		}

		if (node.eClass().equals(UMLPackage.Literals.FORK_NODE) || node.eClass().equals(UMLPackage.Literals.JOIN_NODE)
				|| node.eClass().equals(UMLPackage.Literals.DECISION_NODE)) {
			return ActivityResolveResult.createCopiedResult(getTargetFromActivityNode(node.getIncomings().get(0).getSource(), conditionalExpression));
		} else if (node.eClass().equals(UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION)) {
			return ActivityResolveResult.createCopiedResult( getTargetFromInputPin(((AddStructuralFeatureValueAction) node).getObject()));
		} else if (node.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			return ActivityResolveResult.createSimpleResult(getTargetFromRSFA((ReadStructuralFeatureAction) node));
		} else if (node.eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
			EClass ec = node.getActivity().getOwner().eClass();
			String paramName = ((ActivityParameterNode) node).getParameter().getName();
			if (ec.equals(UMLPackage.Literals.TRANSITION)) {
				return ActivityResolveResult.createSimpleResult(ActivityTemplates.transitionActionParameter(paramName));
			} else // the parameter is a function parameter
			{
				return ActivityResolveResult.createSimpleResult(GenerationTemplates.paramName(paramName));
			}

		} else if (node.eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION)) {
			return ActivityResolveResult.createSimpleResult(objectMap.get(node));
		} else if (node.eClass().equals(UMLPackage.Literals.READ_SELF_ACTION)) {
			return ActivityResolveResult.createSimpleResult(ActivityTemplates.SelfLiteral);

		} else if (node.eClass().equals(UMLPackage.Literals.READ_LINK_ACTION)) {
			return ActivityResolveResult.createCopiedResult(getTargetFromActivityNode(((ReadLinkAction) node).getResult(), conditionalExpression));

		} else if (node.eClass().equals(UMLPackage.Literals.OUTPUT_PIN)) {
			OutputPin outPin = (OutputPin) node;
			/*if(node.getOwner().eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
				return ActivityResolveResult.createCopiedResult( getTargetFromActivityNode((ActivityNode) node.getOwner(), conditionalExpression));
			}*/
			ActivityResolveResult res = tempVariableExporter.isOutExported(outPin) ? 
					 ActivityResolveResult.createSimpleResult(tempVariableExporter.getRealVariableName(outPin)) : 
					 ActivityResolveResult.createCopiedResult( getTargetFromActivityNode((ActivityNode) node.getOwner(), conditionalExpression));
					 
			return res;

		} else if (node.eClass().equals(UMLPackage.Literals.VALUE_SPECIFICATION_ACTION)) {
			return ActivityResolveResult.createSimpleResult(getValueFromValueSpecification(((ValueSpecificationAction) node).getValue()));
		} else if (node.eClass().equals(UMLPackage.Literals.READ_VARIABLE_ACTION)) {

			ReadVariableAction rA = (ReadVariableAction) node;
			userVariableExporter.modifyVariableInfo(rA.getVariable());
			ActivityResolveResult source =ActivityResolveResult.createSimpleResult(userVariableExporter.getRealVariableReference(rA.getVariable()));
			if(rA.getVariable().getUpper() == 1 && conditionalExpression) {
				source = ActivityResolveResult.createSimpleResult(ActivityTemplates.operationCall(source.getReferenceResultCode(), PointerAndMemoryNames.SimpleAccess, 
							CollectionNames.SelectAnyFunctionName, Collections.emptyList()));
			}
			
			return source;
		} else if (node.eClass().equals(UMLPackage.Literals.SEQUENCE_NODE)) {
			SequenceNode seqNode = (SequenceNode) node;
			int lastIndex = seqNode.getNodes().size() - 1;
			return getTargetFromActivityNode(seqNode.getNodes().get(lastIndex), conditionalExpression);

		} else if (node.eClass().equals(UMLPackage.Literals.CALL_OPERATION_ACTION)) {
			CallOperationAction callAction = (CallOperationAction) node;
			String declares = "";
			//if(!returnOutputsToCallActions.containsKey(callAction)) {
				declares = activityExporter.createActivityNodeCode(callAction);
			//}
			return  ActivityResolveResult.createComplexResult(tempVariableExporter.getRealVariableName(returnOutputsToCallActions.get(callAction)), declares);
		} else {
			Logger.sys.error("Unhandled activity node: " + node.getName());		
			return ActivityResolveResult.createSimpleResult("UNHANDLED_REFERENCE");
		}
		
	}
	
	public String getTargetFromASFVA(AddStructuralFeatureValueAction node) {
		String source = node.getStructuralFeature().getName();
		String object = getTargetFromInputPin(node.getObject()).getReferenceResultCode();
		if (!object.isEmpty()) {
			source = object + ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node.getObject())) + source;
		}
		return source;
	}
	
	private String getTargetFromRSFA(ReadStructuralFeatureAction node) {
		String source = node.getStructuralFeature().getName();
		String object = getTargetFromInputPin(node.getObject()).getReferenceResultCode();
		if (!object.isEmpty()) {
			source = object + ActivityTemplates.accesOperatoForType(getTypeFromInputPin(node.getObject())) + source;
		}
		return source;
	}
	
	public ActivityResolveResult getTargetFromInputPin(InputPin node) {
		return getTargetFromInputPin(node, true);
	}
	
	public ActivityResolveResult getTargetFromInputPin(InputPin node, Boolean recursive) {
		ActivityResolveResult source = ActivityResolveResult.createSimpleResult("UNHANDLED");
		if (node.eClass().equals(UMLPackage.Literals.INPUT_PIN)) {

			if (node.getIncomings().size() > 0) {				 
				source = getTargetFromActivityNode(node.getIncomings().get(0).getSource(), false);
			}

		} else if (node.eClass().equals(UMLPackage.Literals.VALUE_PIN)) {

			ValueSpecification valueSpec = ((ValuePin) node).getValue();
			if (valueSpec != null) {
				source = ActivityResolveResult.createSimpleResult(getValueFromValueSpecification(valueSpec));
			} else if (node.getIncomings().size() > 0) {
				source = getTargetFromActivityNode(node.getIncomings().get(0).getSource(), false);
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
			source =  ((LiteralString) valueSpec).getValue();			
			source = GenerationNames.BasicTypeNames.StringTypeName + "(" + "\"" + CppExporterUtils.escapeQuates(source) + "\"" + ")";
		
		} else if(valueSpec.eClass().equals(UMLPackage.Literals.LITERAL_NULL)) {
			source = ActivityTemplates.NullPtrLiteral;
		}
		else {
			source = "UNHANDLED_VALUEPIN_VALUETYPE";
			
		}
		return source;
	}
}
