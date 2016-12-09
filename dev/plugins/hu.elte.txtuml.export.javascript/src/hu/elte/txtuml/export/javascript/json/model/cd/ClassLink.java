package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

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
	protected String type;

	/**
	 * No-arg constructor required for serialization
	 */
	protected ClassLink() {
	}

	/**
	 * Creates a ClassLink based on the layout information and type provided
	 * 
	 * @param assoc
	 *            the layout information
	 * @param type
	 *            the type of the link
	 */
	public ClassLink(LineAssociation assoc, String type) {
		this(assoc);
		this.type = type;
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

		// this code assumes that at least one coordinate is provided (the
		// actual minimum is more than two) also that the links are orthogonal
		if (assoc.getTurns() == 0) {
			// in case of links without turns we need one "turning point" to
			// define the link's position
			List<Point> points = assoc.getRoute();
			int center = points.size() / 2;
			route = points.subList(center, center + 1);
		} else {
			// in case of links with turns we need the turning points
			List<Point> points = assoc.getMinimalRoute();
			route = points.subList(1, points.size() - 1);
		}
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
	public String getType() {
		return type;
	}

}
