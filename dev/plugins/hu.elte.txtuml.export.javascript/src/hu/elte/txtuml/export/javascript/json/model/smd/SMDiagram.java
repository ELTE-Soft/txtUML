package hu.elte.txtuml.export.javascript.json.model.smd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.javascript.json.model.cd.ClassAttributeLink;
import hu.elte.txtuml.export.javascript.json.model.cd.ClassLink;
import hu.elte.txtuml.export.javascript.json.model.cd.ClassNode;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class SMDiagram {
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getMachineName")
	private String machineName;
	@XmlAccessMethods(getMethodName = "getStates")
	private List<State> states;
	@XmlAccessMethods(getMethodName = "getPseudoStates")
	private List<PseudoState> pseudoStates;
	@XmlAccessMethods(getMethodName = "getTransitions")
	private List<Transition> transitions;

	protected SMDiagram() {
	}

	public SMDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links, ModelMapProvider map) {
		name = diagramName;
		machineName = null;
		states = new ArrayList<State>();
		pseudoStates = new ArrayList<PseudoState>();
		transitions = new ArrayList<Transition>();

		for (RectangleObject node : nodes) {
			EObject estate = map.getByName(node.getName());
			if (estate instanceof org.eclipse.uml2.uml.State) {
				org.eclipse.uml2.uml.State state = (org.eclipse.uml2.uml.State) estate;
				states.add(new State(node, state));
			} else if (estate instanceof org.eclipse.uml2.uml.Pseudostate) {
				org.eclipse.uml2.uml.Pseudostate state = (org.eclipse.uml2.uml.Pseudostate) estate;
				/*
				 * if (machineName == null) { machineName =
				 * state.getStateMachine().getLabel(); }
				 */
				pseudoStates.add(new PseudoState(node, state));
			}
		}

		for (LineAssociation link : links) {
			org.eclipse.uml2.uml.Transition signal = (org.eclipse.uml2.uml.Transition) map.getByName(link.getId());
			transitions.add(new Transition(link, signal));
		}
	}

	public String getName() {
		return name;
	}

	public List<State> getStates() {
		return states;
	}

	public List<PseudoState> getPseudoStates() {
		return pseudoStates;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public String getMachineName() {
		return machineName;
	}

}
