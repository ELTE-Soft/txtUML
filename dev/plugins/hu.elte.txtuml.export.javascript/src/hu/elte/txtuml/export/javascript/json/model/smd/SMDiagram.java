package hu.elte.txtuml.export.javascript.json.model.smd;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.export.diagrams.common.Point;
import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.export.diagrams.common.arrange.ArrangeException;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutTransformer;
import hu.elte.txtuml.export.diagrams.common.arrange.LayoutVisualizerManager;
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
	
	private ModelMapProvider map;
	private DiagramExportationReport der;
	private StateMachineDiagramPixelDimensionProvider provider;

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
			StateMachineDiagramPixelDimensionProvider provider, IProgressMonitor monitor) throws UnexpectedException, ArrangeException {
		this.map = map;
		this.der = der;
		this.provider = provider;
		name = diagramName;
		machineName = null;
		states = new ArrayList<State>();
		pseudoStates = new ArrayList<PseudoState>();
		transitions = new ArrayList<Transition>();

		Set<RectangleObject> nodes = der.getNodes();
		Set<LineAssociation> links = der.getLinks();
		
		if(monitor.isCanceled()) return;
		processDiagram(nodes, links, monitor);
	}
	
	private void processDiagram(Set<RectangleObject> nodes, Set<LineAssociation> links, IProgressMonitor monitor) throws ArrangeException, UnexpectedException{
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.subTask("Exploring diagram...");
		// Discovering hierarchy, parent regions of links and creating and sorting states into states and pseudoStates
		Set<Pair<LineAssociation, RectangleObject>> innerLinks = new HashSet<>();
		for(LineAssociation l : der.getLinks()){
			if(subMonitor.isCanceled()) return;
			innerLinks.add(new Pair<>(new LineAssociation(l), null));
		}
		for (RectangleObject node : nodes) {
			if(subMonitor.isCanceled()) return;
			processNode(node, innerLinks);
		}
		subMonitor.worked(10);
		if(subMonitor.isCanceled()) return;
		// arranging
		LayoutVisualizerManager lvm = new LayoutVisualizerManager(nodes, links, der.getStatements(), DiagramType.State,
				provider);
		lvm.addProgressMonitor(subMonitor.newChild(80));
		lvm.arrange();
		LayoutTransformer lt = new LayoutTransformer();

		subMonitor.subTask("Applying transformations...");
		if(subMonitor.isCanceled()) return;
		// scaling and transforming
		Map<String, Rectangle> ltrmap = NodeUtils.getFlattenedAndOffsetRectMapfromROCollection(lvm.getObjects());
		Map<String, List<Point>> ltpmap = LinkUtils.getPointMapfromLACollection(lvm.getAllAssociations());

		lt.doTranformations(ltrmap, ltpmap);

		// setting correct dimensions
		for (State s : states) {
			Rectangle pos = ltrmap.get(s.getId());
			s.setLayout(pos);
		}

		for (PseudoState ps : pseudoStates) {
			Rectangle pos = ltrmap.get(ps.getId());
			ps.setLayout(pos);
		}
		subMonitor.worked(5);

		subMonitor.subTask("Creating transitions...");
		// creating transitions
		for (Pair<LineAssociation, RectangleObject> pair : innerLinks) {
			if(subMonitor.isCanceled()) return;
			LineAssociation link = pair.getFirst();
			Point offset = new Point(0,0);
			//getting the offset of the parent region
			if(pair.getSecond() != null){
				offset = ltrmap.get(pair.getSecond().getName()).getTopLeft();
				offset.setY(offset.y() + NodeUtils.HEIGHT_PADDING);
			}
			org.eclipse.uml2.uml.Transition t = (org.eclipse.uml2.uml.Transition) map.getByName(link.getId());
			Transition tr = new Transition(link, t);
			transitions.add(tr);
			ArrayList<Point> route = new ArrayList<>(ltpmap.get(tr.getId()));
			for(Point p : route){
				p.setX(p.x() + offset.x());
				p.setY(p.y() + offset.y());
			}
			tr.setRoute(route);
		}
		subMonitor.done();
	}
	
	private void processNode(RectangleObject node, Set<Pair<LineAssociation, RectangleObject>> links) throws UnexpectedException{
		NodeScaler scaler = null;
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
		
		//exploring the hierarchy recursively
		if(node.hasInner()){
			for(RectangleObject r : node.getInner().Objects){
				processNode(r, links);
			}
			for(LineAssociation l : node.getInner().Assocs){
				links.add(new Pair<>(l, node));
			}
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
