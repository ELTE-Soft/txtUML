package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

public class ClassLink {
	@XmlAccessMethods(getMethodName="getId")
	protected String id;
	@XmlAccessMethods(getMethodName="getFromID")
	protected String fromID;
	@XmlAccessMethods(getMethodName="getToID")
	protected String toID;
	@XmlAccessMethods(getMethodName="getRoute")
	protected List<Point> route;
	@XmlAccessMethods(getMethodName="getType")
	protected String type;


	protected ClassLink() {}
	
	public ClassLink(LineAssociation assoc, String type){
		this(assoc);
		this.type = type; 
	}
	
	protected ClassLink(LineAssociation assoc){
		id = assoc.getId();
		fromID = assoc.getFrom();
		toID = assoc.getTo();
		if (assoc.getTurns() == 0){
			List<Point> points = assoc.getRoute();
			int center = points.size() / 2;
			route = new ArrayList<Point>();
			route.add(points.get(0));
			route.add(points.get(center));
			route.add(points.get(points.size() - 1));
		}else{
			List<Point> points = assoc.getMinimalRoute();
			//route = points.subList(1, points.size() - 1);
			route = points;
		}
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
	
	public List<Point> getRoute(){
		return route;
	}
	
	public String getType() {
		return type;
	}

}
