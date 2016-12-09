package hu.elte.txtuml.export.javascript.json.model.smd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Trigger;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

/**
 * 
 * Holds information about a transition of a statemachine
 *
 */
public class Transition {
	@XmlAccessMethods(getMethodName = "getId")
	protected String id;
	@XmlAccessMethods(getMethodName = "getFromID")
	private String fromID;
	@XmlAccessMethods(getMethodName = "getToID")
	private String toID;
	@XmlAccessMethods(getMethodName = "getTrigger")
	private String trigger;
	@XmlAccessMethods(getMethodName = "getRoute")
	protected List<Point> route;
	@XmlAccessMethods(getMethodName = "getAnchors")
	protected List<Point> anchors;

	/**
	 * No-arg constructor required for serialization
	 */
	protected Transition() {
	}

	/**
	 * Creates a Transition based on the EMF-UML model-element and layout
	 * information provided
	 * 
	 * @param link
	 *            the layout information
	 * @param transition
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public Transition(LineAssociation link, org.eclipse.uml2.uml.Transition transition) {
		id = link.getId();
		fromID = link.getFrom();
		toID = link.getTo();

		trigger = null;
		List<Trigger> triggers = transition.getTriggers();
		if (triggers.size() > 0) {
			trigger = triggers.get(0).getEvent().getLabel();
		}

		// this code assumes that at least one coordinate is provided (the
		// actual minimum is more than two) also that the links are orthogonal
		if (link.getTurns() == 0) {
			// in case of links without turns we need one "turning point" to
			// define the link's position
			List<Point> points = link.getRoute();
			int center = points.size() / 2;
			route = points.subList(center, center + 1);
		} else {
			// in case of links with turns we need the turning points
			List<Point> points = link.getMinimalRoute();
			route = points.subList(1, points.size() - 1);
		}

		// also export the connection points of the routes to help position the
		// non scaling pseudo nodes
		anchors = new ArrayList<Point>();
		List<Point> points = link.getMinimalRoute();
		anchors.add(points.get(0));
		anchors.add(points.get(points.size() - 1));
	}

	/**
	 * 
	 * @return the ID of the layout descriptor which belongs to the transition
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the node's layout descriptor ID which is on the from end of the
	 *         link
	 */
	public String getFromID() {
		return fromID;
	}

	/**
	 * 
	 * @return the node's layout descriptor ID which is on the to end of the
	 *         link
	 */
	public String getToID() {
		return toID;
	}

	/**
	 * 
	 * @return the trigger's name which belongs to the transition (more
	 *         precisely the triggering signal's)
	 */
	public String getTrigger() {
		return trigger;
	}

	/**
	 * 
	 * @return the turning points of the node's abstract route (if there's none,
	 *         then a point in the center of the link)
	 */
	public List<Point> getRoute() {
		return route;
	}

	/**
	 * 
	 * @return the abstract route's connection points to the nodes
	 */
	public List<Point> getAnchors() {
		return anchors;
	}

}
