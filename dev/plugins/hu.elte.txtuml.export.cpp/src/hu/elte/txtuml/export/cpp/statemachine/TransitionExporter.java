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
		for (Transition item : transitions) {
			source.append(StateMachineTemplates.transitionActionDecl(item.getName()));
		}
		source.append("\n");
		return source.toString();
	}

	String createTransitionFunctionsDef() {
		StringBuilder source = new StringBuilder("");
		for (Transition item : transitions) {
			String body = "";
			boolean signalAcces = false;
			Behavior b = item.getEffect();
			Pair<String, Boolean> setState = createSetState(item);
			if (b != null && b.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
				body = activityExporter.createfunctionBody((Activity) b);
				signalAcces = activityExporter.isContainsSignalAcces() || setState.getSecond();

			}

			source.append(StateMachineTemplates.transitionActionDef(className, item.getName(),
					body + setState.getFirst() + "\n", signalAcces));
		}
		source.append("\n");
		return source.toString();
	}

	private Pair<String, Boolean> createSetState(Transition transition) {
		String source = "";
		boolean containsChoice = false;
		Vertex targetState = transition.getTarget();

		// choice handling
		if (targetState.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)
				&& ((Pseudostate) targetState).getKind().equals(PseudostateKind.CHOICE_LITERAL)) {
			List<Pair<String, String>> branches = new LinkedList<Pair<String, String>>();
			Pair<String, String> elseBranch = null;
			containsChoice = true;
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
		return new Pair<String, Boolean>(source, containsChoice);
	}
}
