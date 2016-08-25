package hu.elte.txtuml.export.cpp.structural;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class GuardExporter extends ActivityExporter{
	
	private static String GuardName = "guard";
	
	private Map<Constraint,String> constratintFunctionMap;	
	private int guardCount;
	
	public GuardExporter() {
		constratintFunctionMap = new HashMap<Constraint,String>();
		guardCount = 0;
	}
	
	public Map<Constraint, String> getGuards() {
		return constratintFunctionMap;
	}
	
	public void exportConstraintToMap(Constraint guard) {
		constratintFunctionMap.put(guard, GuardName + guardCount);
		guardCount++;
	}
	
	public String getGuard(Constraint guard) {
		String source = "";
		if (guard.eClass().equals(UMLPackage.Literals.DURATION_CONSTRAINT)) {
			// TODO
		} else if (guard.eClass().equals(UMLPackage.Literals.TIME_CONSTRAINT)) {
			// TODO
		} else if (guard.eClass().equals(UMLPackage.Literals.CONSTRAINT)) {
			
			source = constratintFunctionMap.get(guard);
			
		}
		return source;
	}
	
	public String declareGuardFunctions(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region_.getTransitions()) {
			Constraint constraint = item.getGuard();
			if (constraint != null) {
				// TODO else..
				exportConstraintToMap(constraint);
				source.append(GenerationTemplates.guardDecleration(getGuard(constraint)));
			}
		}
		source.append("\n");
		return source.toString();
	}
	
	public StringBuilder defnieGuardFunctions(String className) {
		StringBuilder source = new StringBuilder("");
		for (Entry<Constraint, String> guardEntry : getGuards().entrySet()) {
			init();
			String body =getGuardFromValueSpecification(guardEntry.getKey().getSpecification());
			source.append(GenerationTemplates.guardDefinition(guardEntry.getValue(), body, className,
					isContainsSignalAcces()));
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
					init();
					source = createfunctionBody( (Activity) expression.getBehavior()).toString();
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
