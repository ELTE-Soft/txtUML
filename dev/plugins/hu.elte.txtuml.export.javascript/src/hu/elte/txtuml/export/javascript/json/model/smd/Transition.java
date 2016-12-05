package hu.elte.txtuml.export.javascript.json.model.smd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Trigger;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

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

	protected Transition() {
	}

	public Transition(LineAssociation link, org.eclipse.uml2.uml.Transition transition) {
		id = link.getId();
		fromID = link.getFrom();
		toID = link.getTo();
		trigger = null;
		List<Trigger> triggers = transition.getTriggers();
		if (triggers.size() > 0) {
			trigger = triggers.get(0).getEvent().getLabel();
		}
		anchors = new ArrayList<Point>();

		if (link.getTurns() == 0) {
			List<Point> points = link.getRoute();
			int center = points.size() / 2;
			route = points.subList(center, center + 1);
		} else {
			List<Point> points = link.getMinimalRoute();
			route = points.subList(1, points.size() - 1);
		}

		List<Point> points = link.getMinimalRoute();
		anchors.add(points.get(0));
		anchors.add(points.get(points.size() - 1));
	}

	public String getId() {
		return id;
	}

	public String getFromID() {
		return fromID;
	}

	public String getToID() {
		return toID;
	}

	public String getTrigger() {
		return trigger;
	}

	public List<Point> getRoute() {
		return route;
	}

	public List<Point> getAnchors() {
		return anchors;
	}

}
