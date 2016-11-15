package hu.elte.txtuml.export.javascript.json.model.smd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

public class Transition {
	@XmlAccessMethods(getMethodName="getFromID")
	private String fromID;
	@XmlAccessMethods(getMethodName="getToID")
	private String toID;
	@XmlAccessMethods(getMethodName="getName")
	private String name;
	@XmlAccessMethods(getMethodName="getRoute")
	protected List<Point> route;
	@XmlAccessMethods(getMethodName="getAnchors")
	protected List<Point> anchors;
	
	protected Transition() {}
	
	public Transition(LineAssociation link, org.eclipse.uml2.uml.Transition signal) {
		fromID = link.getFrom();
		toID = link.getTo();
		name = signal.getLabel();
		if (link.getTurns() == 0){
			List<Point> points = link.getRoute();
			int center = points.size() / 2;
			route = new ArrayList<Point>();
			route.add(points.get(center));
		}else{
			List<Point> points = link.getMinimalRoute();
			route = points.subList(1, points.size() - 1);
			//route = points;
		}
		anchors = new ArrayList<Point>();
		List<Point> points = link.getMinimalRoute();
		anchors.add(points.get(0));
		anchors.add(points.get(points.size() - 1));
	}

	public String getFromID() {
		return fromID;
	}

	public String getToID() {
		return toID;
	}

	public String getName() {
		return name;
	}
	public List<Point> getRoute(){
		return route;
	}
	
	public List<Point> getAnchors(){
		return anchors;
	}

}
