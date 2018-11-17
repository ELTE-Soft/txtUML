package hu.elte.txtuml.export.javascript.json.model.smd;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.diagrams.common.Point;
import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.export.diagrams.common.arrange.ArrangeException;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutTransformer;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutVisualizerManager;
import hu.elte.txtuml.export.diagrams.common.statemachine.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.export.javascript.scalers.NodeScaler;
import hu.elte.txtuml.export.javascript.scalers.PseudoStateScaler;
import hu.elte.txtuml.export.javascript.scalers.StateScaler;
import hu.elte.txtuml.export.javascript.utils.LinkUtils;
import hu.elte.txtuml.export.javascript.utils.NodeUtils;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

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
	
	private StateMachineDiagramElementsProvider provider;
	private StateMachineDiagramPixelDimensionProvider dimensionProvider;
	private ModelMapProvider map;
	private DiagramExportationReport der;
	private ArrayList<Pair<Set<RectangleObject>, Set<Transition>>> regions;

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
	public SMDiagram(String diagramName, DiagramExportationReport der, ModelMapProvider map,
			StateMachineDiagramPixelDimensionProvider dimensionProvider, StateMachineDiagramElementsProvider provider) throws UnexpectedException, ArrangeException {
		this.provider = provider;
		this.map = map;
		this.der = der;
		this.dimensionProvider = dimensionProvider;
		
			name = diagramName;
			machineName = null;
			states = new ArrayList<State>();
			pseudoStates = new ArrayList<PseudoState>();
			transitions = new ArrayList<Transition>();

			for(Region r : this.provider.getMainRegions()){
				machineName = r.getName();
				processRegion(r,0);
			}
		/*TMP: old workings*/
		/*TODO: explore links like states*/

		
	}
	
	private void processRegion(Region rec, Integer level){
		List<State> states = new ArrayList<>();
		List<PseudoState> pseudoStates = new ArrayList<>();
		List<Transition> transitions = new ArrayList<>();
		
		Set<RectangleObject> nodes = new HashSet<>();
		/*RectangleObject mainNode = new RectangleObject(rec.getName());
		try {
			processNodeInto(mainNode, states, pseudoStates, transitions);
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nodes.add(mainNode);*/
		/*TODO: links*/
		Set<LineAssociation> links = der.getLinks();
		/* TMP: old working restored*/
		/*for(org.eclipse.uml2.uml.State s : this.provider.getStatesForRegion(rec)){
			//System.out.println(level + s.getName() + " child of " + s.getOwner());
			if(!s.isSimple()){
				for(Region r : this.provider.getRegionsOfState(s)){
					System.out.println("Sub-region: " + s.getName());
					processRegion(r, level+1);
				}
			}else{
				RectangleObject node = new RectangleObject(s.getName());
				try {
					processNodeInto(s, node, states, pseudoStates, transitions);
				} catch (UnexpectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nodes.add(node);
			}
		}*/
		nodes = der.getNodes();
		for(RectangleObject r : nodes){
			try {
				processNodeInto(r, states, pseudoStates, transitions);
			} catch (UnexpectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//arrange
		try {
			arrangeRegion(nodes, links, states, pseudoStates, transitions);
		} catch (ArrangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TMP - must place result in member
		this.states = states;
		this.pseudoStates = pseudoStates;
		this.transitions = transitions;
	}
	
	private void arrangeRegion(Set<RectangleObject> nodes, Set<LineAssociation> links, List<State> states, List<PseudoState> pseudoStates, List<Transition> transitions) throws ArrangeException{
		LayoutVisualizerManager lvm = new LayoutVisualizerManager(nodes, links, this.der.getStatements(), DiagramType.State,
				this.dimensionProvider);
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
		/*TODO*/
		for (LineAssociation link : der.getLinks()) {
			org.eclipse.uml2.uml.Transition t = (org.eclipse.uml2.uml.Transition) map.getByName(link.getId());
			Transition tr = new Transition(link, t);
			transitions.add(tr);
			tr.setRoute(ltpmap.get(tr.getId()));
		}
	}
	
	/*TODO: recursive processing*/
	
	private void processNodeInto(RectangleObject node, List<State> states, List<PseudoState> pseudoStates, List<Transition> transitions) throws UnexpectedException{
		NodeScaler scaler = null;
		System.out.println("Processing node: " + node.getName());
		/*TMP NOTE: map is fine for all, only it uses full class name*/
		EObject estate = this.map.getByName(node.getName());
		/*TODO: replace this by provider*/
		// getting the name of the containing region
		/*if (machineName == null) {
			machineName = ((Region) ((Element) estate).getOwner()).getName();
		}*/

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
