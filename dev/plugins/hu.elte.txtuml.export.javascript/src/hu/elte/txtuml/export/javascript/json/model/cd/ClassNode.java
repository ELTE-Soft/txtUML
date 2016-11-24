package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class ClassNode {

	@XmlAccessMethods(getMethodName = "getPosition")
	private Point position;
	@XmlAccessMethods(getMethodName = "getWidth")
	private Integer width;
	@XmlAccessMethods(getMethodName = "getHeight")
	private Integer height;
	@XmlAccessMethods(getMethodName = "getId")
	private String id;
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getType")
	private String type;
	@XmlAccessMethods(getMethodName = "getAttributes")
	private List<Attribute> attributes;
	@XmlAccessMethods(getMethodName = "getOperations")
	private List<MemberOperation> operations;

	protected ClassNode() {
	}

	public ClassNode(RectangleObject layout, Classifier clazz) {
		position = layout.getPosition();
		width = layout.getWidth();
		height = layout.getHeight();
		id = layout.getName();
		name = clazz.getName();
		attributes = new ArrayList<Attribute>();
		for (Property attr : clazz.getAttributes()) {
			if (attr.getAssociation() == null) {
				attributes.add(new Attribute(attr));
			}
		}
		;
		operations = new ArrayList<MemberOperation>();
		for (Operation op : clazz.getOperations()) {
			operations.add(new MemberOperation(op));
		}
		;
		if (clazz.isAbstract()) {
			type = "abstract";
		} else if (clazz instanceof Interface) {
			type = "interface";
		} else {
			type = "class";
		}

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

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public List<MemberOperation> getOperations() {
		return operations;
	}

	public String getType() {
		return type;
	}

}
