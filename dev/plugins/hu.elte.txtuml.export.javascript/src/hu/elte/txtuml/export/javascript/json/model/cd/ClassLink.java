package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.javascript.utils.LinkUtils;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

public class ClassLink {
	@XmlAccessMethods(getMethodName = "getId")
	protected String id;
	@XmlAccessMethods(getMethodName = "getFromID")
	protected String fromID;
	@XmlAccessMethods(getMethodName = "getToID")
	protected String toID;
	@XmlAccessMethods(getMethodName = "getRoute")
	protected List<Point> route;
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
		route = LinkUtils.getTurningPoints(assoc);
		type = assoc.getType();
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
	public List<Point> getRoute() {
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
