package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;

import java.util.ArrayList;
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
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

public abstract class  AbstractDiagramElementsTxtUmlArranger extends AbstractDiagramElementsArranger{
	protected void arrangeAll(EditPart EP, List<EditPart> elements) {
		if(!elements.isEmpty()){
			int maxWidth = getMaxWidth(elements);
			int maxHeight = getMaxHeight(elements);
			int gapX = 20;
			int gapY = 20;
			
			List<String> statementsList = Arrays.asList(
					"topmost("+getXmiId(elements.get(0))+")"
				);		
					
			LayoutVisualizerManager vismanager = new LayoutVisualizerManager(elements, statementsList);
			vismanager.arrange();
			Map<EditPart, Rectangle> nodeMap = vismanager.getNodesAndCoordinates();
			@SuppressWarnings("unchecked")
			Map<ConnectionNodeEditPart, List<Point>> connectionMap = (Map<ConnectionNodeEditPart, List<Point>>)(Map<?,?>)
																				vismanager.getConnectionsAndRoutes();
			vismanager.simplifyRoutes(connectionMap);
			
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

	protected String getXmiId(EditPart editPart){
		EObject object = ((View) editPart.getModel()).getElement();
	    return ((XMLResource) object.eResource()).getID(object);
	}
	
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
	
	protected Dimension getSize(GraphicalEditPart editpart){
		return 	editpart.getFigure().getPreferredSize();
	}
	
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
	
	private double[] defineAnchor(Point a, Point b){
		Point vec = new Point(a.x-b.x, a.y-b.y);
		double length = Math.sqrt(vec.x*vec.x+vec.y*vec.y);
		double[] result = new double[2];
		result[0] = (vec.x/length+1)/2;
		result[1] = (vec.y/length+1)/2;
		return result;
	}
}
