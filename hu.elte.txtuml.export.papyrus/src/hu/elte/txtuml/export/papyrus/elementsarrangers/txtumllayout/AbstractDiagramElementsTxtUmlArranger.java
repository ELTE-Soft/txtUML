package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

/**
 * An abstract class for arranging the elements with the txtUML arranging algorithm. 
 *
 * @author András Dobreff
 */
public abstract class  AbstractDiagramElementsTxtUmlArranger extends AbstractDiagramElementsArranger{
	
	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 */
	public AbstractDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	/**
	 * Arranges the children of an EditPart with the txtUML arranging algorithm 
	 * @param parent - The children of this EditPart will be arranged
	 */
	protected void arrangeChildren(EditPart EP, List<EditPart> elements) {
		if(!elements.isEmpty()){
			int maxWidth = getMaxWidth(elements);
			int maxHeight = getMaxHeight(elements);
			int gapX = 20;
			int gapY = 20;
			
			List<String> statementsList = Arrays.asList();		
					
			LayoutVisualizerManager vismanager = new LayoutVisualizerManager(elements, statementsList);
			vismanager.arrange();
			
			Map<EditPart, Rectangle> nodeMap = vismanager.getNodesAndCoordinates();
			Map<ConnectionNodeEditPart, List<Point>> connectionMap = vismanager.getConnectionsAndRoutes();
			
			LayoutTransformer layoutTransformer = new LayoutTransformer(maxWidth, maxHeight);
			layoutTransformer.setGapX(gapX);
			layoutTransformer.setGapY(gapY);
			layoutTransformer.setOrigo(OrigoConstraint.UpperLeft);
			layoutTransformer.flipYAxis();
			layoutTransformer.doTranformations(nodeMap, connectionMap);
			
			for(Entry<EditPart, Rectangle> e : nodeMap.entrySet()) {
		        EditPart ep = e.getKey();
		        if(ep != null)
		        	super.moveGraphicalEditPart((GraphicalEditPart) ep, e.getValue().getTopLeft());
		    }
			
			for(Entry<ConnectionNodeEditPart, List<Point>> e : connectionMap.entrySet()) {
		        ConnectionNodeEditPart connection = e.getKey();
		        String[] anchors;
		        if(connection != null){
		        	List<Point> bendpoints = e.getValue();
		        	anchors = defineAnchors(bendpoints);
		        	super.setConnectionAnchors(connection, anchors[0], anchors[1]);
		        	bendpoints.remove(0);
		        	bendpoints.remove(bendpoints.size()-1);
		        	super.SetConnectionBendpoints(connection, bendpoints);
		        }
		    }
		    
		}
	}

	/**
	 * Gets the XmiId of the Model elment of an EditPart
	 * @param editPart - The EditPart
	 * @return The XmiId
	 */
	protected String getXmiId(EditPart editPart){
		EObject object = ((View) editPart.getModel()).getElement();
	    return ((XMLResource) object.eResource()).getID(object);
	}
	
	/**
	 * Gets the maximum width of the given EditParts 
	 * @param editParts - The EditParts
	 * @return The maximum width
	 */
	protected int getMaxWidth(List<EditPart> editParts){
		int max = 0;
		int width = 0;
		for(EditPart editpart : editParts){
			width = getSize((GraphicalEditPart) editpart).width();
			if(width > max)
				max = width;
		}
		return max;
	}
	
	/**
	 * Gets the maximum height of the given EditParts 
	 * @param editParts - The EditParts
	 * @return The maximum height
	 */
	protected int getMaxHeight(List<EditPart> editParts){
		int max = 0;
		int height = 0;
		for(EditPart editpart : editParts){
			height = getSize((GraphicalEditPart) editpart).height();
			if(height > max)
				max = height;
		}
		return max;
	}
	
	/**
	 * Gets the size of a GrapchicalEditPart
	 * @param editpart - The GraphicalEditPart
	 * @return The size
	 */
	protected Dimension getSize(GraphicalEditPart editpart){
		return 	editpart.getFigure().getPreferredSize();
	}
	
	/**
	 * Gets the anchors from a route
	 * @param route - The route
	 * @return A String array width two values. The first
	 *  is the source anchor, the second is the target
	 */
	protected  String[] defineAnchors(List<Point> route){
		String[] result = new String[2];
		
		if(route.size() >= 2){
			double[] source = defineAnchor(route.get(1), route.get(0));
			double[] target = defineAnchor(route.get(route.size()-1), route.get(route.size()-2));
			result[0] = "("+source[0]+", "+source[1]+")";
			result[1] = "("+target[0]+", "+target[1]+")";
		}
		return result;
	}
	
	/**
	 * Gets an anchor from the given Points
	 * @param a - The first Point
	 * @param b - The second Point
	 * @return The anchor
	 */
	private double[] defineAnchor(Point a, Point b){
		Point vec = new Point(a.x-b.x, a.y-b.y);
		double length = Math.sqrt(vec.x*vec.x+vec.y*vec.y);
		double[] result = new double[2];
		result[0] = (vec.x/length+1)/2;
		result[1] = (vec.y/length+1)/2;
		return result;
	}
}
