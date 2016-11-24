package hu.elte.txtuml.export.javascript.json.model.smd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class PseudoState {
	@XmlAccessMethods(getMethodName = "getId")
	private String id;
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getKind")
	private String kind;
	@XmlAccessMethods(getMethodName = "getPosition")
	private Point position;
	@XmlAccessMethods(getMethodName = "getWidth")
	private Integer width;
	@XmlAccessMethods(getMethodName = "getHeight")
	private Integer height;

	protected PseudoState() {
	}

	public PseudoState(RectangleObject node, org.eclipse.uml2.uml.Pseudostate state) {
		id = node.getName();
		name = state.getLabel();
		kind = state.getKind().getLiteral();
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

	public String getKind() {
		return kind;
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
