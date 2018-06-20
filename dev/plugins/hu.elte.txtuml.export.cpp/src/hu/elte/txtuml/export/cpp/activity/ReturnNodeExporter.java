package hu.elte.txtuml.export.cpp.activity;

import java.util.List;

import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.ObjectFlow;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;

public class ReturnNodeExporter {

	private ActivityNode returnNode;
	private ActivityNodeResolver activityExportResolver;
	private boolean containsReturnNode;
	private final boolean singleReturn;

	ReturnNodeExporter(ActivityNodeResolver activityExportResolver, boolean singleReturn) {

		this.activityExportResolver = activityExportResolver;
		containsReturnNode = false;
		this.singleReturn = singleReturn;
	}

	public void searchReturnNode(List<ActivityEdge> edges) {
		if (!containsReturnNode) {

			for (ActivityEdge aEdge : edges) {
				if (aEdge.eClass().equals(UMLPackage.Literals.OBJECT_FLOW)) {
					ObjectFlow objectFlow = (ObjectFlow) aEdge;
					if (objectFlow.getTarget().eClass().equals(UMLPackage.Literals.ACTIVITY_PARAMETER_NODE)) {
						ActivityParameterNode parameterNode = (ActivityParameterNode) objectFlow.getTarget();
						if (parameterNode.getParameter().getDirection().equals(ParameterDirectionKind.RETURN_LITERAL)) {
							returnNode = objectFlow.getSource();
							containsReturnNode = true;
						}
					}
				}
			}
		}
	}

	public String createReturnParamaterCode() {
		if (containsReturnNode) {
			return ActivityTemplates.returnTemplates(activityExportResolver.getTargetFromActivityNode(returnNode, false), singleReturn);
		} else {
			return "";
		}

	}
}
