package hu.elte.txtuml.export.cpp;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;

public class GuardExporter {
	
	ActivityExporter guardActivityExporter;
	
	public GuardExporter() {
		guardActivityExporter = new ActivityExporter();
	}
	
	public String getGuard(Constraint guard_) {
		String source = "";
		if (guard_.eClass().equals(UMLPackage.Literals.DURATION_CONSTRAINT)) {
			// TODO
		} else if (guard_.eClass().equals(UMLPackage.Literals.TIME_CONSTRAINT)) {
			// TODO
		} else if (guard_.eClass().equals(UMLPackage.Literals.CONSTRAINT)) {

			source=getGuardFromValueSpecification(guard_.getSpecification());
		}
		return source;
	}

	// TODO we need a more complex ocl parse....
	public String getGuardFromValueSpecification(ValueSpecification guard_) {
		String source = "";
		if (guard_ != null) {
			if (guard_.eClass().equals(UMLPackage.Literals.LITERAL_STRING)) {
				source = ((LiteralString) guard_).getValue();
				if (source.toLowerCase().equals("else")) {
					source = "";
				}
			} else if (guard_.eClass().equals(UMLPackage.Literals.OPAQUE_EXPRESSION)) {
				OpaqueExpression expression = (OpaqueExpression) guard_;
				if(expression.getBehavior() != null && expression.getBehavior().eClass().equals(UMLPackage.Literals.ACTIVITY))
					guardActivityExporter.reinitilaize();
					source = guardActivityExporter.createfunctionBody( (Activity) expression.getBehavior()).toString();
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
