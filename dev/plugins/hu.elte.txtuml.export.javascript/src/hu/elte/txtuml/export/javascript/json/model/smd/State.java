package hu.elte.txtuml.export.javascript.json.model.smd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class State {
	@XmlAccessMethods(getMethodName="getId")
	private String id;
	@XmlAccessMethods(getMethodName="getName")
	private String name;
	@XmlAccessMethods(getMethodName="getPosition")
	private Point position;
	@XmlAccessMethods(getMethodName="getWidth")
	private Integer width;
	@XmlAccessMethods(getMethodName="getHeight")
	private Integer height;
	
	protected State(){}
	
	public State(RectangleObject node, org.eclipse.uml2.uml.State state) {
		id = node.getName();
		name = state.getLabel();
		position = node.getPosition();
		width = node.getWidth();
		height = node.getHeight();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Point getPosition() {
		return position;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

}
