package hu.elte.txtuml.export.cpp.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.IDependencyCollector;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.activity.OperatorTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;

public class GuardExporter extends ActivityExporter {

	private static String GuardName = "guard";

	private Map<Constraint, String> constratintFunctionMap;
	private int guardCount;

	public GuardExporter(Optional<IDependencyCollector> exportUser) {
		super(exportUser);
		constratintFunctionMap = new HashMap<Constraint, String>();
		guardCount = 0;
	}

	public Map<Constraint, String> getGuards() {
		return constratintFunctionMap;
	}

	public void exportConstraintToMap(Constraint guard) {
		if (!constratintFunctionMap.containsKey(guard)) {
			constratintFunctionMap.put(guard, GuardName + guardCount);
			guardCount++;
		}

	}

	public String getGuard(Constraint guard) {
		String source = "";
		if (guard.eClass().equals(UMLPackage.Literals.DURATION_CONSTRAINT)) {
			// TODO when this feature will be enable
		} else if (guard.eClass().equals(UMLPackage.Literals.TIME_CONSTRAINT)) {
			// TODO when this feature will be enable
		} else if (guard.eClass().equals(UMLPackage.Literals.CONSTRAINT)) {
			source = constratintFunctionMap.get(guard);

		}
		return source;
	}

	public String declareGuardFunctions(Region region) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region.getTransitions()) {
			Constraint constraint = item.getGuard();
			if (constraint != null) {
				exportConstraintToMap(constraint);
				source.append(StateMachineTemplates.guardDeclaration(getGuard(constraint)));
			}
		}
		source.append("\n");
		return source.toString();
	}

	public String defnieGuardFunctions(String className) {
		StringBuilder source = new StringBuilder("");
		for (Entry<Constraint, String> guardEntry : getGuards().entrySet()) {
			
			ValueSpecification guard = guardEntry.getKey().getSpecification();
			ActivityExportResult activityResult = new ActivityExportResult();
			if (guard != null) {
				if (guard.eClass().equals(UMLPackage.Literals.OPAQUE_EXPRESSION)) {
					OpaqueExpression expression = (OpaqueExpression) guard;
					activityResult = createFunctionBody(expression.getBehavior());
					source.append(StateMachineTemplates.guardDefinition(guardEntry.getValue(), activityResult.getActivitySource(), className,
							activityResult.sourceHasSignalReference()));
					
				} else {
					source.append(StateMachineTemplates.guardDefinition(guardEntry.getValue(), "UNKNOWN_GUARD_TYPE", className, false));
				}
			}			
			
			

		}

		return source.toString();
	}

	public String calculateSmElseGuard(Transition elseTransition) {
		StringBuilder source = new StringBuilder("");
		Pseudostate choice = (Pseudostate) elseTransition.getSource();
		for (Transition transition : choice.getOutgoings()) {
			if (!transition.equals(elseTransition)) {
				if (!source.toString().isEmpty()) {
					source.append(OperatorTemplates.And);
					source.append(" ");
				}
				source.append(OperatorTemplates.Not);
				source.append("(");
				source.append(getGuard(transition.getGuard()));
				source.append(")");
			}
		}
		return source.toString();
	}
}
