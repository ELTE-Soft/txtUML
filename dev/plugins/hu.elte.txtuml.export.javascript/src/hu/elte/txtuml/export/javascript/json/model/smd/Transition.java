package hu.elte.txtuml.export.javascript.json.model.smd;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Trigger;

import hu.elte.txtuml.export.diagrams.common.Point;
import hu.elte.txtuml.export.javascript.json.MarshalablePoint;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

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
	protected List<MarshalablePoint> route;

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
	}

	/**
	 * Sets the route of the link
	 * 
	 * @param route
	 *            the desired route
	 */
	public void setRoute(List<Point> route) {
		this.route = route.stream().map(MarshalablePoint::new).collect(Collectors.toList());
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
	public List<MarshalablePoint> getRoute() {
		return route;
	}

}
