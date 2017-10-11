package hu.elte.txtuml.export.cpp.statemachine;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.utils.Pair;

public class TransitionExporter {
	private ActivityExporter activityExporter;
	private GuardExporter guardExporter;

	String className;
	List<Transition> transitions;

	TransitionExporter(String className, List<Transition> transitions, GuardExporter guardExporter) {
		activityExporter = new ActivityExporter();

		this.className = className;
		this.transitions = transitions;
		this.guardExporter = guardExporter;
	}

	String createTransitionFunctionDecl() {
		StringBuilder source = new StringBuilder("");
		for (Transition transition : transitions) {
			source.append(StateMachineTemplates.transitionActionDecl(transition.getName()));
		}
		source.append("\n");
		return source.toString();
	}

	String createTransitionFunctionsDef() {
		StringBuilder source = new StringBuilder("");
		for (Transition transition : transitions) {
			ActivityExportResult activityResult = new ActivityExportResult();
			Behavior b = transition.getEffect();
			String setState = createSetState(transition);
			activityResult = activityExporter.createFunctionBody((Activity) b);

			
			source.append(StateMachineTemplates.transitionActionDef(className, transition.getName(),
					transition.getName(), activityResult.getActivitySource() + setState,
					hasChoiceTarget(transition) || activityResult.sourceHasSignalReference()));
		}
		source.append("\n");
		return source.toString();
	}

	private Boolean hasChoiceTarget(Transition transition) {
		return transition.getTarget() != null && transition.getTarget().eClass().equals(UMLPackage.Literals.PSEUDOSTATE)
				&& ((Pseudostate) transition.getTarget()).getKind().equals(PseudostateKind.CHOICE_LITERAL);
	}

	private String createSetState(Transition transition) {
		String source = "";
		Vertex targetState = transition.getTarget();

		// choice handling
		if (hasChoiceTarget(transition)) {
			List<Pair<String, String>> branches = new LinkedList<Pair<String, String>>();
			Pair<String, String> elseBranch = null;
			for (Transition trans : targetState.getOutgoings()) {

				String guard = guardExporter.getGuard(trans.getGuard()) + "(" + EventTemplates.eventParamName() + ")";
				String body = ActivityTemplates.blockStatement(ActivityTemplates.transitionActionCall(trans.getName()))
						.toString();

				if (guard.isEmpty() || guard.equals("else")) {
					elseBranch = new Pair<String, String>(guard, body);
				} else {
					branches.add(new Pair<String, String>(guard, body));
				}
			}
			if (elseBranch != null) {
				branches.add(elseBranch);
			}
			source = ActivityTemplates.elseIf(branches).toString();
		} else if (targetState.eClass().equals(UMLPackage.Literals.STATE)) {
			source = StateMachineTemplates.setState(targetState.getName());

		} else {
			source = StateMachineTemplates.setState("UNKNOWN_TRANSITION_TARGET");
		}
		return source;
	}
}
