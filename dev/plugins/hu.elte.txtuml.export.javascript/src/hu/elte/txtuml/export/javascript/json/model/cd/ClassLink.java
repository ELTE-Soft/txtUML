package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.javascript.json.MarshalablePoint;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.utils.diagrams.Point;

public class ClassLink {
	@XmlAccessMethods(getMethodName = "getId")
	protected String id;
	@XmlAccessMethods(getMethodName = "getFromID")
	protected String fromID;
	@XmlAccessMethods(getMethodName = "getToID")
	protected String toID;
	@XmlAccessMethods(getMethodName = "getRoute")
	protected List<MarshalablePoint> route;
	@XmlAccessMethods(getMethodName = "getType")
	protected AssociationType type;

	/**
	 * No-arg constructor required for serialization
	 */
	protected ClassLink() {
	}

	/**
	 * Creates a ClassLink based on the layout information provided
	 * 
	 * @param assoc
	 *            the layout information
	 */
	protected ClassLink(LineAssociation assoc) {
		id = assoc.getId();
		fromID = assoc.getFrom();
		toID = assoc.getTo();
		type = assoc.getType();
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
	 * @return the links layout descriptor ID
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
	 * @return the turning points of the node's abstract route (if there's none,
	 *         then a point in the center of the link)
	 */
	public List<MarshalablePoint> getRoute() {
		return route;
	}

	/**
	 * 
	 * @return the type of the link
	 */
	public AssociationType getType() {
		return type;
	}

}
