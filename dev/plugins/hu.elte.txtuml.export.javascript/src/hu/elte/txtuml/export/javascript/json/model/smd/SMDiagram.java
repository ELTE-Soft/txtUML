package hu.elte.txtuml.export.javascript.json.model.smd;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.diagrams.common.Point;
import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.export.javascript.scalers.NodeScaler;
import hu.elte.txtuml.export.javascript.scalers.PseudoStateScaler;
import hu.elte.txtuml.export.javascript.scalers.StateScaler;
import hu.elte.txtuml.export.javascript.utils.LinkUtils;
import hu.elte.txtuml.export.javascript.utils.NodeUtils;
import hu.elte.txtuml.export.diagrams.common.arrange.ArrangeException;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutTransformer;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutVisualizerManager;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
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
	 * @param der
	 *            The diagram layout
	 * @param map
	 *            The map which links the layout informations to the EMF-UML
	 *            model
	 * 
	 * @throws ArrangeException
	 *             Exception is thrown if a diagram could not be arranged
	 * 
	 * @throws UnexpectedException
	 *             Exception is thrown if a diagram contains unexpected parts
	 */
	public SMDiagram(String diagramName, DiagramExportationReport der, ModelMapProvider map)
			throws UnexpectedException, ArrangeException {
		name = diagramName;
		machineName = null;
		states = new ArrayList<State>();
		pseudoStates = new ArrayList<PseudoState>();
		transitions = new ArrayList<Transition>();

		Set<RectangleObject> nodes = der.getNodes();
		Set<LineAssociation> links = der.getLinks();

		// creating and sorting states into states and pseudoStates
		NodeScaler scaler = null;
		for (RectangleObject node : nodes) {

			EObject estate = map.getByName(node.getName());
			// getting the name of the containing region
			if (machineName == null) {
				machineName = ((Region) ((Element) estate).getOwner()).getName();
			}

			if (estate instanceof org.eclipse.uml2.uml.State) {
				org.eclipse.uml2.uml.State state = (org.eclipse.uml2.uml.State) estate;
				State s = new State(state, node.getName());
				scaler = new StateScaler(s);
				states.add(s);

			} else if (estate instanceof org.eclipse.uml2.uml.Pseudostate) {
				org.eclipse.uml2.uml.Pseudostate state = (org.eclipse.uml2.uml.Pseudostate) estate;
				PseudoState ps = new PseudoState(state, node.getName());
				scaler = new PseudoStateScaler(ps);
				pseudoStates.add(ps);
			} else {
				throw new UnexpectedException("Unexpected statemachine element type: " + estate.getClass().toString());
			}
			// setting the estimated size to the RectangleObject
			node.setPixelWidth(scaler.getWidth());
			node.setPixelHeight(scaler.getHeight());

		}
		// arranging
		LayoutVisualizerManager lvm = new LayoutVisualizerManager(nodes, links, der.getStatements(), DiagramType.State, null);
		lvm.arrange();
		LayoutTransformer lt = new LayoutTransformer();

		// scaling and transforming
		Map<String, Rectangle> ltrmap = NodeUtils.getRectMapfromROCollection(lvm.getObjects());
		Map<String, List<Point>> ltpmap = LinkUtils.getPointMapfromLACollection(lvm.getAssociations());

		lt.doTranformations(ltrmap, ltpmap);

		// setting correct dimensions
		for (State s : states) {
			s.setLayout(ltrmap.get(s.getId()));
		}

		for (PseudoState ps : pseudoStates) {
			ps.setLayout(ltrmap.get(ps.getId()));
		}

		// creating transitions
		for (LineAssociation link : der.getLinks()) {
			org.eclipse.uml2.uml.Transition t = (org.eclipse.uml2.uml.Transition) map.getByName(link.getId());
			Transition tr = new Transition(link, t);
			transitions.add(tr);
			tr.setRoute(ltpmap.get(tr.getId()));
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
