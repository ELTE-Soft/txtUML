package hu.elte.txtuml.export.javascript.json.model.smd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * Holds information about a statemachine diagram and it's elements
 *
 */
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
	@XmlAccessMethods(getMethodName = "getSpacing")
	private double spacing;

	/**
	 * No-arg constructor required for serialization
	 */
	protected SMDiagram() {
	}

	/**
	 * Creates a SMDiagram based on the EMF-UML model's information and layout
	 * information provided
	 * 
	 * @param diagramName
	 *            The name of the diagram
	 * @param nodes
	 *            The nodes of the already arranged diagram which are containing
	 *            abstract positions and sizes
	 * @param links
	 *            The links of the already arranged diagram which are containing
	 *            abstract routes
	 * @param map
	 *            The map which links the layout informations to the EMF-UML
	 *            model
	 * @param spacing
	 *            The desired spacing
	 */
	public SMDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links, ModelMapProvider map,
			double spacing) {
		this.spacing = spacing;
		name = diagramName;
		machineName = null;
		states = new ArrayList<State>();
		pseudoStates = new ArrayList<PseudoState>();
		transitions = new ArrayList<Transition>();

		// creating and sorting states into states and pseudoStates
		for (RectangleObject node : nodes) {

			EObject estate = map.getByName(node.getName());

			if (estate instanceof org.eclipse.uml2.uml.State) {
				org.eclipse.uml2.uml.State state = (org.eclipse.uml2.uml.State) estate;

				if (machineName == null) {
					machineName = ((Region) state.getOwner()).getName();
				}
				states.add(new State(node, state));
			} else if (estate instanceof org.eclipse.uml2.uml.Pseudostate) {
				org.eclipse.uml2.uml.Pseudostate state = (org.eclipse.uml2.uml.Pseudostate) estate;

				if (machineName == null) {
					machineName = ((Region) state.getOwner()).getName();
				}
				pseudoStates.add(new PseudoState(node, state));
			}
		}

		// crating transitions
		for (LineAssociation link : links) {
			org.eclipse.uml2.uml.Transition t = (org.eclipse.uml2.uml.Transition) map.getByName(link.getId());
			transitions.add(new Transition(link, t));
		}
	}

	/**
	 * 
	 * @return the name of the diagram
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the states of the diagram
	 */
	public List<State> getStates() {
		return states;
	}

	/**
	 * 
	 * @return the pseudo states of the diagram
	 */
	public List<PseudoState> getPseudoStates() {
		return pseudoStates;
	}

	/**
	 * 
	 * @return the transitions of the diagram
	 */
	public List<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * 
	 * @return the name of the statemachine which this diagram represents
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * 
	 * @return the spacing of the diagram
	 */
	public double getSpacing() {
		return spacing;
	}

}
