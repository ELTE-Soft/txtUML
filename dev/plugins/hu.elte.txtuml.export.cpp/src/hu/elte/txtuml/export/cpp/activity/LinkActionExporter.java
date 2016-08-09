package hu.elte.txtuml.export.cpp.activity;

import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadLinkAction;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

class LinkAnctionExporter {
	
	private OutVariableExporter tempVariableExporter;
	private ActivityNodeResolver activityExportResolver;
	
	
	public LinkAnctionExporter(OutVariableExporter tempVariableExporter,ActivityNodeResolver activityExportResolver) {
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
		
		String target = readLinkNode.getInputValues().size() > 0 ? 
				activityExportResolver.getTargetFromInputPin(readLinkNode.getInputValues().get(0)) : ActivityTemplates.Self;

		return ActivityTemplates.defineAndAddToCollection(otherMember.getType().getName(),
				tempVariableExporter.getRealVariableName(readLinkNode.getResult()),
				ActivityTemplates.selectAllTemplate(target,otherMember.getAssociation().getName(), otherMember.getName()));
	}

	public String createLinkActionCode(CreateLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);

		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());

		return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
				firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
				secondLinkEnd.getEnd().getName(), GenerationTemplates.LinkFunctionType.Link);
	}

	public Object createDestroyLinkActionCode(DestroyLinkAction node) {
		LinkEndData firstLinkEnd = node.getEndData().get(0);
		LinkEndData secondLinkEnd = node.getEndData().get(1);

		String firstEndObject = activityExportResolver.getTargetFromInputPin(firstLinkEnd.getValue());
		String secondEndObject = activityExportResolver.getTargetFromInputPin(secondLinkEnd.getValue());

		return ActivityTemplates.linkObjects(firstEndObject, secondEndObject,
				firstLinkEnd.getEnd().getAssociation().getName(), firstLinkEnd.getEnd().getName(),
				secondLinkEnd.getEnd().getName(), GenerationTemplates.LinkFunctionType.Unlink);
	}
}
