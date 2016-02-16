package hu.elte.txtuml.export.papyrus.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectionAnchorsCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.common.commands.ShowHideLabelsRequest;

@SuppressWarnings("restriction")
public class DiagramElementsModifier {
	/**
	 * Resizes  a GraphicalEditPart
	 * @param graphEP - The GraphicalEditPart that is to be resized
	 * @param new_width - The new width of the EditPart
	 * @param new_height - The new height of the EditPart
	 */
	public static void resizeGraphicalEditPart(GraphicalEditPart graphEP, int new_width, int new_height){
		Dimension figuredim = graphEP.getFigure().getSize();
		ChangeBoundsRequest resize_req = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
		resize_req.setSizeDelta(new Dimension(new_width-figuredim.width(), new_height-figuredim.height()));
		resize_req.setEditParts(graphEP);
		
		Command cmd = graphEP.getCommand(resize_req);
		if(cmd != null)
			cmd.execute();
	}
	
	/**
	 * Hides the labels on the connections of the given elements
	 * @param elements - The EditParts which's connection labels is to be hidden 
	 * @param excluding - The types of connection labels which are not wanted to be hidden
	 */
	public static void hideConnectionLabelsForEditParts(List<EditPart> elements, List<java.lang.Class<?>> excluding){
		for(EditPart editpart: elements){
			GraphicalEditPart ep = ((GraphicalEditPart) editpart);
			@SuppressWarnings("unchecked")
			List<ConnectionNodeEditPart> connections = ep.getSourceConnections();
			for(ConnectionNodeEditPart connection : connections){
				@SuppressWarnings("unchecked")
				List<ConnectionNodeEditPart> labels = connection.getChildren();
				for(EditPart label : labels){
					if(!isInstanceOfAny(label, excluding)){
						ShowHideLabelsRequest request = new ShowHideLabelsRequest(false, ((View) label.getModel()));
						Command com = connection.getCommand(request);
						if(com != null && com.canExecute())
							com.execute();
					}
				}
				
			}
		}
	}
	
	/**
	 * Checks if an object is instance any of the given types
	 * @param object - The objects whose parent class is checked
	 * @param types - The types that are checked
	 * @return Returns true if the object is instance any of the given types
	 */
	private static boolean isInstanceOfAny(Object object, Collection<java.lang.Class<?>> types){
		boolean result = false;
		Iterator<java.lang.Class<?>> it = types.iterator();
		while(!result && it.hasNext()){
			java.lang.Class<?> cls = it.next();
			result = cls.isInstance(object);
		}
		return result;
	}
	
	/**
	 * Sets the anchors of a connection according to the given terminals in format: (float, float) .
	 * @param connection - The connection 
	 * @param src - Source Terminal
	 * @param trg - Target Terminal
	 */
	public static void setConnectionAnchors(ConnectionNodeEditPart connection, String src, String trg){
		TransactionalEditingDomain editingDomain = connection.getEditingDomain();
		SetConnectionAnchorsCommand cmd = new SetConnectionAnchorsCommand(editingDomain, "Rearrange Anchor");
		cmd.setEdgeAdaptor(new EObjectAdapter(connection.getNotationView()));
		cmd.setNewSourceTerminal(src);
		cmd.setNewTargetTerminal(trg);
		Command proxy =  new ICommandProxy(cmd);
		proxy.execute();
	}
	
	/**
	 * Sets the BendPoints of a connection. (Starting point and Ending points are not BendPoints)
	 * @param connection - The connection
	 * @param bendpoints - The BendPoints
	 */
	public static void setConnectionBendpoints(ConnectionNodeEditPart connection, List<Point> bendpoints){
		TransactionalEditingDomain editingDomain = connection.getEditingDomain();
		SetConnectionBendpointsCommand cmd = new SetConnectionBendpointsCommand(editingDomain);
		cmd.setEdgeAdapter(new EObjectAdapter(connection.getNotationView()));
		
		Point sourceRef = connection.getConnectionFigure().getSourceAnchor().getReferencePoint();
		Point targetRef = connection.getConnectionFigure().getTargetAnchor().getReferencePoint();
		PointList pointList = new PointList();
		
		pointList.addPoint(sourceRef);
		for(Point bendpoint: bendpoints){
			pointList.addPoint(bendpoint);
		}
		pointList.addPoint(targetRef);
		
		cmd.setNewPointList(pointList, sourceRef, targetRef);
		Command proxy =  new ICommandProxy(cmd);
		proxy.execute();
	}
	
	/**
	 * Moves a GraphicalEditPart to the given location
	 * @param graphEp
	 * @param p
	 */
	public static void moveGraphicalEditPart(GraphicalEditPart graphEp, Point p){
		moveGraphicalEditPart(graphEp, p.x(), p.y());
	}
	
	/**
	 * Moves a GraphicalEditPart to the given location
	 * @param graphEP - The GraphicalEditPart
	 * @param new_X - The new x coordinate
	 * @param new_Y - The new y coordinate
	 */
	public static void moveGraphicalEditPart(GraphicalEditPart graphEP, int new_X, int new_Y){
		Rectangle figurebounds = graphEP.getFigure().getBounds();
		ChangeBoundsRequest move_req = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
		move_req.setMoveDelta(new Point(new_X-figurebounds.x(), new_Y-figurebounds.y()));
		move_req.setEditParts(graphEP);
		
		Command cmd = graphEP.getCommand(move_req);
		if(cmd != null && cmd.canExecute())
			cmd.execute();
	}
}
