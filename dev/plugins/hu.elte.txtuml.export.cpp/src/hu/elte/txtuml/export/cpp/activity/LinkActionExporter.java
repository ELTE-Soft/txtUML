package hu.elte.txtuml.export.cpp.activity;

import java.util.Arrays;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.PortTemplates;

class LinkActionExporter {

	private OutVariableExporter tempVariableExporter;
	private ActivityNodeResolver activityExportResolver;

	public LinkActionExporter(OutVariableExporter tempVariableExporter, ActivityNodeResolver activityExportResolver) {
		this.tempVariableExporter = tempVariableExporter;
		this.activityExportResolver = activityExportResolver;
	}

	public String createReadLinkActionCode(ReadLinkAction readLinkNode) {

		Property otherMember = null;
		for (LinkEndData end : readLinkNode.getEndData()) {
			if (end.getValue() == null) {
				otherMember = end.getEnd();
			}
		}
		tempVariableExporter.exportOutputPinToMap(readLinkNode.getResult());

		String target = readLinkNode.getInputValues().size() > 0
				? activityExportResolver.getTargetFromInputPin(readLinkNode.getInputValues().get(0))
				: ActivityTemplates.SelfLiteral;

		return ActivityTemplates.defineAndAddToCollection(otherMember.getType().getName(),
				tempVariableExporter.getRealVariableName(readLinkNode.getResult()), ActivityTemplates
						.selectAllTemplate(target, otherMember.getAssociation().getName(), otherMember.getName()));
	}

	public String createLinkActionCode(CreateLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);
		
		
		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());
		
		
		if(isPortConnect(node)) {
			
			Port p1 =  (Port) ((ReadStructuralFeatureAction) ((OutputPin) firstLinkEnd.getValue().getIncomings().get(0).getSource()).getOwner()).getStructuralFeature();
			Port p2 =  (Port) ((ReadStructuralFeatureAction) ((OutputPin) firstLinkEnd.getValue().getIncomings().get(0).getSource()).getOwner()).getStructuralFeature();
			String connectOperation = "";
			String firstPort = firstEndObject;
			String secodPort = secondEndObject;
			switch(getRelation(p1, p2)) {
			case Child:
				firstPort = secondEndObject;
				secodPort = firstEndObject;
				connectOperation = PortTemplates.DelegationConnectionSetter;
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
			
			return ActivityTemplates.operationCall(connectOperation, Arrays.asList(firstPort,secodPort));
		}
		else {
			return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
					firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
					secondLinkEnd.getEnd().getName(), LinkTemplates.LinkFunctionType.Link);
		}

		
	}
	
	enum Relation {
		Parent,
		Child,
		Sublings
	}
	private Relation getRelation(Port p1, Port p2) {
		Class p1Class = p1.getClass_();
		Class p2Class = p2.getClass_();
		if(p1Class.getOwnedAttributes().stream().anyMatch(p -> p.getType().equals(p2Class))) {
			return Relation.Child;
		} else if(p2Class.getOwnedAttributes().stream().anyMatch(p -> p.getType().equals(p1Class))) {
			return Relation.Parent;
		} else {
			return Relation.Sublings;
		}
		
	}
	
	boolean isPortConnect(CreateLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);

		OutputPin outPin = (OutputPin) firstLinkEnd.getValue().getIncomings().get(0).getSource();
		if(outPin.getOwner().eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			ReadStructuralFeatureAction readPort = (ReadStructuralFeatureAction) outPin.getOwner();
			return readPort.getStructuralFeature().equals(UMLPackage.Literals.PORT);
		}
		
		
		return false;
		
		
	}

	public String createDestroyLinkActionCode(DestroyLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);

		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());

		return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
				firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
				secondLinkEnd.getEnd().getName(), LinkTemplates.LinkFunctionType.Unlink);
	}
}
