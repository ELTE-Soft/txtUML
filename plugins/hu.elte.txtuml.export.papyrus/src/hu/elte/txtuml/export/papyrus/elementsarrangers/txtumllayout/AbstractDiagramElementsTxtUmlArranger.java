package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.Element;

/**
 * An abstract class for arranging the elements with the txtUML arranging algorithm. 
 *
 * @author András Dobreff
 */
public abstract class  AbstractDiagramElementsTxtUmlArranger extends AbstractDiagramElementsArranger{
	
	private TxtUMLElementsRegistry txtUmlRegistry;
	
	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 * @param txtUmlRegistry - The {@link TxtUMLElementsRegistry} which specifies the layout
	 */
	public AbstractDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart, TxtUMLElementsRegistry txtUmlRegistry) {
		super(diagramEditPart);
		this.txtUmlRegistry = txtUmlRegistry;
	}

	/**
	 * Arranges the children of an EditPart with the txtUML arranging algorithm 
	 * @param parent - The children of this EditPart will be arranged
	 * @throws ArrangeException 
	 */
	protected void arrangeChildren(EditPart parent, IProgressMonitor monitor) throws ArrangeException{
		@SuppressWarnings("unchecked")
		List<EditPart> editParts = parent.getChildren();
		if(!editParts.isEmpty()){
			int cellSize = Math.max(getMaxWidth(editParts), getMaxHeight(editParts));
			int gapX = 0;
			int gapY = 0;
			
			List<ConnectionNodeEditPart> connections = getConnectionsOfEditParts(editParts);
			
			DiagramExportationReport report = txtUmlRegistry.getDescriptor().getReport(this.diagep.getDiagramView().getName());
			LayoutVisualizerManager vm = new LayoutVisualizerManager(report);
			vm.addProgressMonitor(monitor);
			vm.arrange();
			
			Collection<RectangleObject> objects = vm.getObjects();
			Collection<LineAssociation> links = vm.getAssociations();
			
			Map<GraphicalEditPart, Rectangle> objectsTransform  = pairPositionsToEditParts(objects, editParts, cellSize, cellSize);
			Map<ConnectionNodeEditPart, List<Point>> linksTransform = pairRoutesToConnectionEditParts(links, connections); 
			
			int gridDensity = objects.isEmpty() ? 0 : objects.iterator().next().getWidth();
			
			transformObjectsAndLinks(objectsTransform, linksTransform , cellSize, cellSize, gapX, gapY, gridDensity-1);
			
			modifyEditParts(objectsTransform);		
			modifyConnectionEditParts(linksTransform, objectsTransform);	
		}
	}

	
	private void modifyConnectionEditParts(
			Map<ConnectionNodeEditPart, List<Point>> linksTransform,
			Map<GraphicalEditPart, Rectangle> objectsTransform) {
		
		linksTransform.forEach(new BiConsumer<ConnectionNodeEditPart , List<Point>>() {
			@Override
			public void accept(ConnectionNodeEditPart connection, List<Point> route) {
					if(connection != null && route.size() >= 2){
						Rectangle source = objectsTransform.get(connection.getSource());
						Rectangle target = objectsTransform.get(connection.getTarget());
			        	String anchor_start = getAnchor(source.getTopLeft(), route.get(0), source.width, source.height);
			        	String anchor_end = getAnchor(target.getTopLeft(), route.get(route.size()-1), target.width, target.height);
			        	route.remove(0);
			        	route.remove(route.size()-1);
			        	connection.getSource();
			        	
			        	DiagramElementsModifier.setConnectionAnchors(connection, anchor_start, anchor_end);
			        	DiagramElementsModifier.setConnectionBendpoints(connection, route);
					}
			}
		});
	}

	private void modifyEditParts(
			Map<GraphicalEditPart, Rectangle> objectsTransform) {
		objectsTransform.forEach(new BiConsumer<GraphicalEditPart, Rectangle>() {
			@Override
			public void accept(GraphicalEditPart ep, Rectangle position) {
				DiagramElementsModifier.resizeGraphicalEditPart(ep, position.width, position.height);
				DiagramElementsModifier.moveGraphicalEditPart(ep, position.getTopLeft());
			}
		});
	}

	private void transformObjectsAndLinks(
			Map<GraphicalEditPart, Rectangle> objectsTransform,
			Map<ConnectionNodeEditPart, List<Point>> linksTransform,
			int cellSize, int cellSize2, int gapX, int gapY, int gridDensity) {
		
		LayoutTransformer trans = new LayoutTransformer(cellSize, cellSize, gapX, gapY, gridDensity);
		trans.setOrigo(OrigoConstraint.UpperLeft);
		trans.flipYAxis();
		trans.doTranformations(objectsTransform, linksTransform);
		
	}

	private Map<ConnectionNodeEditPart, List<Point>> pairRoutesToConnectionEditParts(
			Collection<LineAssociation> links,
			List<ConnectionNodeEditPart> connections) {
		
		Map<ConnectionNodeEditPart, List<Point>> linksTransform = new HashMap<ConnectionNodeEditPart, List<Point>>(); 
		for(LineAssociation la : links){
			
			Optional<? extends Element> e = txtUmlRegistry.findAssociation(la.getId());
			if(!e.isPresent()) e = txtUmlRegistry.findGeneralization(la.getFrom(), la.getTo());
			
			if(e.isPresent()){
				ConnectionNodeEditPart connection = (ConnectionNodeEditPart) getEditPartOfModelElement(connections, e.get());
				Element source = (Element) ((View)connection.getSource().getModel()).getElement();
				Element target = (Element) ((View)connection.getTarget().getModel()).getElement();
				
				Optional<Element> from = txtUmlRegistry.findElement(la.getFrom());
				Optional<Element> to = txtUmlRegistry.findElement(la.getTo());
				if(!from.isPresent() || !to.isPresent()) continue;
				
				List<Point> route = new LinkedList<Point>();

				List<hu.elte.txtuml.layout.visualizer.model.Point> layoutRoute = la.getMinimalRoute();
				
				for(int i = 0; i < layoutRoute.size(); i++){
					int index = i;
					/*
					 * if the type is a generalization  or the source-end relation is different
					 *  in the two representation, the order of the route points should be reversed
					 */
					if(la.getType() == AssociationType.generalization || (from.get() == target && to.get() == source)){
						index = layoutRoute.size()-i-1;
					}
					
					route.add(new Point(layoutRoute.get(index).getX(),layoutRoute.get(index).getY()));
				}
				linksTransform.put(connection, route);
			}
		}
		return linksTransform;
	}

	private Map<GraphicalEditPart, Rectangle> pairPositionsToEditParts(Collection<RectangleObject> objects,
			List<EditPart> editParts, int commonWidth, int commonHeight) {
		
		Map<GraphicalEditPart, Rectangle> objectsTransform = new HashMap<GraphicalEditPart, Rectangle>();
		for(RectangleObject obj : objects){
			Optional<Element> e = txtUmlRegistry.findElement(obj.getName());
			if(e.isPresent()){
				GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(editParts, e.get());
				if(ep != null){
					objectsTransform.put(ep, new Rectangle(obj.getTopLeft().getX(),
									obj.getTopLeft().getY(), commonWidth, commonHeight));
				}
			}
		}
		return objectsTransform;
	}

	private List<ConnectionNodeEditPart> getConnectionsOfEditParts(List<EditPart> editParts) {
		List<ConnectionNodeEditPart> connections = new LinkedList<ConnectionNodeEditPart>();
		for (EditPart editpart : editParts) {
			@SuppressWarnings("unchecked")
			List<ConnectionNodeEditPart> conns = ((GraphicalEditPart) editpart).getSourceConnections();
			connections.addAll(conns);
		}
		return connections;
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
	 * @param center
	 * @param edge
	 * @return
	 */
	protected String getAnchor(Point topleft, Point edge, int objectWidth, int objectHeight){
		Point relativeEdge = edge.getTranslated(topleft.getNegated());
		double anchor_x = ((float) relativeEdge.x())/((float) objectWidth);
		double anchor_y = ((float) relativeEdge.y())/((float) objectHeight);
		return new String("("+anchor_x+", "+anchor_y+")");
	}
	
	/**
	 * Gets the size of a GrapchicalEditPart
	 * @param editpart - The GraphicalEditPart
	 * @return The size
	 */
	protected Dimension getSize(GraphicalEditPart editpart){
		return 	editpart.getFigure().getPreferredSize();
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
}
