package hu.elte.txtuml.export.javascript.json;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.diagrams.common.Point;

public class MarshalablePoint {
	private Point point;

	protected MarshalablePoint() {
	}
	
	public MarshalablePoint(Point p){
		this.point = p;
	}
	

	@XmlAttribute(name = "x")
    public int getX(){
    	return point.x();
    }
	@XmlAttribute(name = "y")
    public int getY(){
    	return point.y();
    }

}
