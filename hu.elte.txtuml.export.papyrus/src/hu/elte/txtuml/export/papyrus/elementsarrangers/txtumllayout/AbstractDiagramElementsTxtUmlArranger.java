package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectionAnchorsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.common.commands.ShowHideLabelsRequest;
import org.eclipse.uml2.uml.NamedElement;

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
			Map<EditPart, Point> nodeMap = vismanager.getNodesAndCoordinates();
			
			LayoutTransformer layoutTransformer = new LayoutTransformer(maxWidth, maxHeight);
			layoutTransformer.setGapX(gapX);
			layoutTransformer.setGapY(gapY);
			layoutTransformer.setOrigo(OrigoConstraint.UpperLeft);
			layoutTransformer.flipYAxis();
			layoutTransformer.doTranformations(nodeMap);
			
			for(Entry<EditPart, Point> e : nodeMap.entrySet()) {
		        EditPart ep = e.getKey();
		        Point gridPosition = e.getValue();
		        Point pixelPosition = new Point(gridPosition.x()*(maxWidth+gapX), (gridPosition.y()*(maxHeight+gapY)));
		        if(ep != null)
		        	super.moveGraphicalEditPart((GraphicalEditPart) ep, pixelPosition);
		    }
			
			Map<ConnectionNodeEditPart, ArrayList<Point>> ConnectionMap = vismanager.getConnectionsAndRoutes();
			
			for(Entry<ConnectionNodeEditPart, ArrayList<Point> > e : ConnectionMap.entrySet()) {
		        EditPart ep = e.getKey();
		        ArrayList<Point> gridPosition = e.getValue();
		        //TODO do sg;
		        System.out.print(true);
		    }
			
			
			for(EditPart editpart: elements){
				GraphicalEditPart ep = ((GraphicalEditPart) editpart);
				@SuppressWarnings("unchecked")
				List<ConnectionNodeEditPart> connections = ep.getSourceConnections();
				for(ConnectionNodeEditPart connection : connections){
					super.setConnectionAnchors(connection, "(1.0, 0.5)", "(1.0, 0.5)");
					super.SetConnectionBendpoints(connection, Arrays.asList(new Point(140, 170), new Point(140, 50)));
				}
			}
		}
	}

	private String getXmiId(EditPart editPart){
		EObject object = ((View) editPart.getModel()).getElement();
	    return ((XMLResource) object.eResource()).getID(object);
	}
	
	private int getMaxWidth(List<EditPart> editParts){
		int max = 0;
		int width = 0;
		for(EditPart editpart : editParts){
			width = getSize((GraphicalEditPart) editpart).width();
			if(width > max)
				max = width;
		}
		return max;
	}
	
	private int getMaxHeight(List<EditPart> editParts){
		int max = 0;
		int height = 0;
		for(EditPart editpart : editParts){
			height = getSize((GraphicalEditPart) editpart).height();
			if(height > max)
				max = height;
		}
		return max;
	}
	
	private Dimension getSize(GraphicalEditPart editpart){
		return 	editpart.getFigure().getPreferredSize();
	}
}
