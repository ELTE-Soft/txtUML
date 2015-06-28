package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.TxtUMLElementsFinder;
import hu.elte.txtuml.export.papyrus.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.Element;

/**
 * Enables the communications to txtUML arranging algorithm.
 *
 * @author András Dobreff
 */
public class LayoutVisualizerManager {

	private Set<RectangleObject> objects;
	private Set<LineAssociation> associations;
	private List<Statement> statementsSet;

	private List<EditPart> editparts;
	private List<ConnectionNodeEditPart> connectionNodeEditParts;

	private TxtUMLElementsFinder finder;
	private TxtUMLLayoutDescriptor layoutdescriptor;

	/**
	 * The Constructor
	 * 
	 * @param editparts - The EditParts that are to be arranged
	 * @param finder - The {@link TxtUMLElementsFinder} which helps the instance to match the txtUML notation and the {@link EditPart}s
	 */
	public LayoutVisualizerManager(List<EditPart> editparts,
			TxtUMLElementsFinder finder) {
		this.finder = finder;
		this.layoutdescriptor = finder.getDescriptor();
		this.objects = this.layoutdescriptor.report.getNodes();
		this.associations = this.layoutdescriptor.report.getLinks();
		this.statementsSet = this.layoutdescriptor.report.getStatements();
		this.connectionNodeEditParts = new ArrayList<ConnectionNodeEditPart>();
		this.editparts = editparts;

		for (EditPart editpart : this.editparts) {
			@SuppressWarnings("unchecked")
			List<ConnectionNodeEditPart> connections = ((GraphicalEditPart) editpart)
					.getSourceConnections();
			connectionNodeEditParts.addAll(connections);
		}
	}

	/**
	 * Arranging command
	 */
	public void arrange() {
		LayoutVisualize v = new LayoutVisualize();
		v.load(objects, associations);

		try {
			v.arrange(new ArrayList<Statement>(statementsSet));
		} catch (UnknownStatementException | ConflictException
				| ConversionException | StatementTypeMatchException
				| InternalException | CannotPositionObjectException
				| CannotFindAssociationRouteException e) {
			e.printStackTrace();
		}
		objects = v.getObjects();
		associations = v.getAssocs();
	}

	/**
	 * Returns the Nodes and their locations
	 * 
	 * @return Returns the Nodes and their locations
	 */
	public HashMap<EditPart, Rectangle> getNodesAndCoordinates() {
		HashMap<EditPart, Rectangle> result = new HashMap<EditPart, Rectangle>();
		int x, y, width, height;

		for (RectangleObject object : objects) {
			Element elem = finder.findElement(object.getName());
			GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(this.editparts, elem);
			x = object.getPosition().getX();
			y = object.getPosition().getY();
			width = ep.getFigure().getPreferredSize().width();
			height = ep.getFigure().getPreferredSize().height();
			Rectangle p = new Rectangle(x, y, width, height);
			result.put(ep, p);
		}
		return result;
	}

	/**
	 * Returns the Nodes and their routes
	 * 
	 * @return Returns the Nodes and their routes
	 */
	public HashMap<ConnectionNodeEditPart, List<Point>> getConnectionsAndRoutes() {
		HashMap<ConnectionNodeEditPart, List<Point>> result = new HashMap<ConnectionNodeEditPart, List<Point>>();
		for (LineAssociation connection : associations) {
			Element element;
			if (connection.getType() == AssociationType.generalization) {
				element = finder.findGeneralization(connection.getId());
			} else {
				element = finder.findAssociation(connection.getId());
			}
			
			ConnectionNodeEditPart ep = (ConnectionNodeEditPart) getEditPartOfModelElement(this.connectionNodeEditParts, element); 
			
			if(ep != null)
				result.put(ep, defineRoute(connection));
		}
		return result;
	}

	/* TODO Maybe this function could have been also used somewhere else - Future refactor needed */
	private EditPart getEditPartOfModelElement(Collection<? extends EditPart> editParts, Element element) {
		if(element == null) return null;
		for (EditPart ep : editParts) {
			Element actual = (Element) ((View) ep.getModel()).getElement();
			if(actual.equals(element))
				return ep;
		}
		return null;
	}
	
	private List<Point> defineRoute(LineAssociation link){
		List<Point> route = new LinkedList<Point>();
		ArrayList<hu.elte.txtuml.layout.visualizer.model.Point> connRoute = link.getMinimalRoute();
		//The point of the route have opposite order in the two representations
		for (int i = connRoute.size()-1; i >= 0; i--) {  
			route.add(new Point(connRoute.get(i).getX(), connRoute.get(i).getY()));
		}
		return route;
	}
}
