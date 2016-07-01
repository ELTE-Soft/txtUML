package hu.elte.txtuml.export.cpp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;

public class GuardExporter{
	
	private Map<Constraint,String> constratintFunctionMap;
	private ActivityExporter activityExporter;
	
	private int guardCount;
	
	public GuardExporter() {
		constratintFunctionMap = new HashMap<Constraint,String>();
		activityExporter = new ActivityExporter();
		guardCount = 0;
	}
	
	public String getGuard(Constraint guard_) {
		String source = "";
		if (guard_.eClass().equals(UMLPackage.Literals.DURATION_CONSTRAINT)) {
			// TODO
		} else if (guard_.eClass().equals(UMLPackage.Literals.TIME_CONSTRAINT)) {
			// TODO
		} else if (guard_.eClass().equals(UMLPackage.Literals.CONSTRAINT)) {
			
			if(constratintFunctionMap.containsKey(guard_)) {
				source = constratintFunctionMap.get(guard_);
			}
			else {
				source = getGuardFromValueSpecification(guard_.getSpecification());
				constratintFunctionMap.put(guard_, "guard" + guardCount);
				guardCount++;
			}
			
		}
		return source;
	}

	// TODO we need a more complex ocl parse....
	public String getGuardFromValueSpecification(ValueSpecification guard_) {
		String source = "";
		if (guard_ != null) {
			if (guard_.eClass().equals(UMLPackage.Literals.OPAQUE_EXPRESSION)) {
				OpaqueExpression expression = (OpaqueExpression) guard_;
				if(expression.getBehavior() != null && expression.getBehavior().eClass().equals(UMLPackage.Literals.ACTIVITY))
					activityExporter.init();
					source = activityExporter.createfunctionBody( (Activity) expression.getBehavior()).toString();
			} else {
				source = "UNKNOWN_GUARD_TYPE";
			}
		}
		return source;
	}
	
	public String calculateSmElseGuard(Transition elseTransition_) {
		String source = "";
		Pseudostate choice = (Pseudostate) elseTransition_.getSource();
		for (Transition transition : choice.getOutgoings()) {
			if (!transition.equals(elseTransition_)) {
				if (!source.isEmpty()) {
					source += " " + ActivityTemplates.Operators.And + " ";
				}
				source += ActivityTemplates.Operators.Not + "(" + getGuard(transition.getGuard()) + ")";
			}
		}
		return source;
	}
}
