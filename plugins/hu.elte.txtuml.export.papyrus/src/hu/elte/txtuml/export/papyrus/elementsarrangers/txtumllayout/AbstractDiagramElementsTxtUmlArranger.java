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
		List<EditPart> elements = parent.getChildren();
		if(!elements.isEmpty()){
			int cellSize = Math.max(getMaxWidth(elements), getMaxHeight(elements));
			int gapX = 0;
			int gapY = 0;
			
			List<ConnectionNodeEditPart> connections = new LinkedList<ConnectionNodeEditPart>();
			for (EditPart editpart : elements) {
				@SuppressWarnings("unchecked")
				List<ConnectionNodeEditPart> conns = ((GraphicalEditPart) editpart).getSourceConnections();
				connections.addAll(conns);
			}

			DiagramExportationReport report = txtUmlRegistry.getDescriptor().getReport(this.diagep.getDiagramView().getName());
			LayoutVisualizerManager vm = new LayoutVisualizerManager(report);
			vm.addProgressMonitor(monitor);
			vm.arrange();
			
			Collection<RectangleObject> objects = vm.getObjects();
			Collection<LineAssociation> links = vm.getAssociations();
			
			Map<GraphicalEditPart, Rectangle> objectsTransform = new HashMap<GraphicalEditPart, Rectangle>();
			Map<ConnectionNodeEditPart, List<Point>> linksTransform = new HashMap<ConnectionNodeEditPart, List<Point>>(); 
			
			for(RectangleObject obj:objects){
				Optional<Element> e = txtUmlRegistry.findElement(obj.getName());
				if(e.isPresent()){
					GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(elements, e.get());
					if(ep != null){
						objectsTransform.put(ep, new Rectangle(obj.getTopLeft().getX(),
										obj.getTopLeft().getY(), cellSize, cellSize));
					}
				}
			}
			
			int gridDensity = objects.isEmpty() ? 0 : objects.iterator().next().getWidth();
			
			for(LineAssociation la : links){
				
				Optional<? extends Element> e = txtUmlRegistry.findAssociation(la.getId());
				if(!e.isPresent()) e = txtUmlRegistry.findGeneralization(la.getFrom(), la.getTo());
				
				if(e.isPresent()){
					ConnectionNodeEditPart connection = (ConnectionNodeEditPart) getEditPartOfModelElement(connections, e.get());
					Element from = txtUmlRegistry.findElement(la.getFrom()).get();
					Element to = txtUmlRegistry.findElement(la.getTo()).get();
					Element source = (Element) ((View)connection.getSource().getModel()).getElement();
					Element target = (Element) ((View)connection.getTarget().getModel()).getElement();
					List<Point> route = new LinkedList<Point>();

					List<hu.elte.txtuml.layout.visualizer.model.Point> layoutRoute = la.getMinimalRoute();
					
					for(int i = 0; i < layoutRoute.size(); i++){
						int index = i;
						if(la.getType() == AssociationType.generalization || (from == target && to == source)){
							index = layoutRoute.size()-i-1;
						}
						
						route.add(new Point(layoutRoute.get(index).getX(),layoutRoute.get(index).getY()));
					}
					linksTransform.put(connection, route);
				}
			}
			
			LayoutTransformer trans = new LayoutTransformer(cellSize, cellSize, gapX, gapY, gridDensity-1);
			trans.setOrigo(OrigoConstraint.UpperLeft);
			trans.flipYAxis();
			trans.doTranformations(objectsTransform, linksTransform);
			
			
			objectsTransform.forEach(new BiConsumer<GraphicalEditPart, Rectangle>() {
				@Override
				public void accept(GraphicalEditPart ep, Rectangle position) {
					DiagramElementsModifier.resizeGraphicalEditPart(ep, cellSize, cellSize);
					DiagramElementsModifier.moveGraphicalEditPart(ep, position.getTopLeft());
				}
			});
		
			
			linksTransform.forEach(new BiConsumer<ConnectionNodeEditPart , List<Point>>() {
				@Override
				public void accept(ConnectionNodeEditPart connection, List<Point> route) {
						if(connection != null && route.size() >= 2){
				        	String anchor_start = getAnchor(objectsTransform.get(connection.getSource()).getTopLeft(), route.get(0), cellSize);
				        	String anchor_end = getAnchor(objectsTransform.get(connection.getTarget()).getTopLeft(), route.get(route.size()-1), cellSize);
				        	route.remove(0);
				        	route.remove(route.size()-1);
				        	connection.getSource();
				        	
				        	DiagramElementsModifier.setConnectionAnchors(connection, anchor_start, anchor_end);
				        	DiagramElementsModifier.setConnectionBendpoints(connection, route);
						}
				}
			});
		}
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
	protected String getAnchor(Point topleft, Point edge, int objectSize){
		Point relativeEdge = edge.getTranslated(topleft.getNegated());
		double anchor_x = ((float) relativeEdge.x())/((float) objectSize);
		double anchor_y = ((float) relativeEdge.y())/((float) objectSize);
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
