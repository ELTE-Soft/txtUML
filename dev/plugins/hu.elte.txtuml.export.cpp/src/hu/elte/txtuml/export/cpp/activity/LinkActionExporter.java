package hu.elte.txtuml.export.cpp.activity;


import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.LinkTemplates.LinkFunctionType;

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
		
		String readLink = ActivityTemplates
				.readLinkTemplate(target, otherMember.getName(), otherMember.getAssociation().getName());
		String collectionType = LinkTemplates.endCollectionType(otherMember.getAssociation().getName() ,otherMember.getName());
		String readVarName = tempVariableExporter.getRealVariableName(readLinkNode.getResult());
		return ActivityTemplates.addVariableTemplate(collectionType, readVarName, readLink, GenerationTemplates.VariableType.StackStored);

	}

	public String createLinkActionCode(CreateLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);
		String secindLinkEndName = secondLinkEnd.getEnd().getName();
		
		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());
		
		LinkFunctionType linkType = LinkTemplates.LinkFunctionType.Link;
		
		if(isPortConnect(node)) {			
			Port p1 =  (Port) ((ReadStructuralFeatureAction) (firstLinkEnd.getValue().getIncomings().get(0).getSource())).getStructuralFeature();
			Port p2 =  (Port) ((ReadStructuralFeatureAction) (secondLinkEnd.getValue().getIncomings().get(0).getSource())).getStructuralFeature();
			switch(getRelation(p1, p2)) {
			case Child:
				String firstTmp = firstEndObject;
				firstEndObject = secondEndObject;
				secondEndObject = firstTmp;
				linkType = LinkTemplates.LinkFunctionType.DelegeateConnect;
				secindLinkEndName = CppExporterUtils.getUsedInterfaceName((Interface) p1.getType());
				break;
			case Parent:
				linkType = LinkTemplates.LinkFunctionType.DelegeateConnect;
				secindLinkEndName = CppExporterUtils.getUsedInterfaceName((Interface) p1.getType());
				break;
			case Sublings:
				linkType = LinkTemplates.LinkFunctionType.AssemblyConnect;
				break;
			default:
				break;
			
			}
			
		}
		return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
					firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
					secindLinkEndName, linkType);

		
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

		ActivityNode readActivity =  firstLinkEnd.getValue().getIncomings().get(0).getSource();
		if(readActivity.eClass().equals(UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION)) {
			ReadStructuralFeatureAction readPort = (ReadStructuralFeatureAction) readActivity;
			return readPort.getStructuralFeature().eClass().equals(UMLPackage.Literals.PORT);
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
