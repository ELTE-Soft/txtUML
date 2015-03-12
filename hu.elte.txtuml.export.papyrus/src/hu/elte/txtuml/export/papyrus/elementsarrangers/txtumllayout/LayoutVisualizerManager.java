package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.helpers.MyModel;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.NamedElement;

public class LayoutVisualizerManager {
	
	private HashSet<RectangleObject> objects;
	private HashSet<LineAssociation> associations;
	private ArrayList<Statement> statementsSet;
	private List<EditPart> editparts;
	private List<ConnectionNodeEditPart> connectionNodeEditParts;
	
	public LayoutVisualizerManager(List<EditPart> editparts, List<String> statementsList){
		objects = new HashSet<RectangleObject>();
		associations = new HashSet<LineAssociation>();
		statementsSet = new ArrayList<Statement>();
		connectionNodeEditParts = new ArrayList<ConnectionNodeEditPart>();
		this.editparts = editparts;
		
		for(EditPart editpart : editparts){
			String name = getXmiId(editpart);
			objects.add(new RectangleObject(name));
			@SuppressWarnings("unchecked")
			List<ConnectionNodeEditPart> connections = ((GraphicalEditPart) editpart).getSourceConnections();
			connectionNodeEditParts.addAll(connections);
			for (ConnectionNodeEditPart connection: connections){
				String connName = getXmiId(connection);
				String targetName = getXmiId(connection.getTarget());
				associations.add(new LineAssociation(connName, name, targetName)); //TODO add ass type
			}
		}
		
		for(String statement : statementsList){
			try {
				statementsSet.add(Statement.Parse(statement));
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void arrange(){
		LayoutVisualize v = new LayoutVisualize();
		MyModel model = new MyModel(objects, associations, statementsSet);
		
		v.load(model.Value.ToFirstPair());
		
		try {
			v.arrange(model.Value.Third);
		} catch (UnknownStatementException | ConflictException
				| ConversionException | StatementTypeMatchException
				| InternalException | CannotPositionObjectException
				| CannotFindAssociationRouteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objects = v.getObjects();
		associations = v.getAssocs();
	}
	
	public HashMap<EditPart, Rectangle> getNodesAndCoordinates(){
		HashMap<EditPart, Rectangle> result = new HashMap<EditPart, Rectangle>();
		int x,y,width,height;
		
		for(RectangleObject object : objects){
			GraphicalEditPart ep = (GraphicalEditPart) getEditPartByXmiId(editparts, object.getName());
			x = object.getPosition().getX();
			y = object.getPosition().getY();
			width = ep.getFigure().getPreferredSize().width();
			height = ep.getFigure().getPreferredSize().height();
			Rectangle p = new Rectangle(x, y, width, height);
			result.put(ep, p);
		}
		return result;
	}
	
	public HashMap<ConnectionNodeEditPart, List<Point> > getConnectionsAndRoutes(){
		HashMap<ConnectionNodeEditPart, List<Point> > result = new HashMap<ConnectionNodeEditPart, List<Point> >();
		for(LineAssociation connection : associations){	
			@SuppressWarnings("unchecked")
			ConnectionNodeEditPart ep = 
					(ConnectionNodeEditPart) getEditPartByXmiId((List<EditPart>)(List<?>) connectionNodeEditParts,connection.getId());
			
			List<Point> route = new LinkedList<Point>();
			for(hu.elte.txtuml.layout.visualizer.model.Point point: connection.getRoute()){
				route.add(new Point(point.getX(), point.getY()));
			}
			
			result.put(ep, route);
		}
		return result;
	}

	private EditPart getEditPartByXmiId(List<EditPart> editparts, String name){
		for (EditPart ep : editparts) {
			if(getXmiId(ep).equals(name))
				return ep;
		}
		return null;
	}
	
	private String getXmiId(EditPart editPart){
		EObject object = ((View) editPart.getModel()).getElement();
	    return ((XMLResource) object.eResource()).getID(object);
	}
	
	public void simplifyRoutes(Map<ConnectionNodeEditPart, List<Point>> connections){
		for(List<Point> route : connections.values()){
			simpliflyRoute(route);
		}
	}

	public void simpliflyRoute(List<Point> route) {
		if(route.size() < 2)
			return;
		for(int i = 1; i< route.size()-1; i++){
			while(i<route.size()-1 && (route.get(i-1).x == route.get(i+1).x || route.get(i-1).y == route.get(i+1).y)){
				route.remove(i);
			}
		}
	}
}
