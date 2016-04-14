package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * An abstract class for arranging the elements with the txtUML arranging algorithm. 
 */
public abstract class  AbstractDiagramElementsTxtUmlArranger extends AbstractDiagramElementsArranger{
	
	private DiagramExportationReport report;
	private TxtUMLElementsMapper mapper;
	
	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 * @param txtUmlRegistry - The {@link TxtUMLElementsRegistry} which specifies the layout
	 */
	public AbstractDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart, DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		super(diagramEditPart);
		this.report = report;
		this.mapper = mapper;
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
			int MAXPIXELWIDTH = 200;
			int MAXPIXELHEIGHT = 200;
			
			Set<RectangleObject> objects = report.getNodes();
			Set<LineAssociation> links = report.getLinks();
			List<Statement> statements =  report.getStatements();

			statements.add(new Statement(StatementType.corridorsize, "0.5"));
			
			Map<GraphicalEditPart, RectangleObject> editPartsObjectsMapping  = pairObjectsToEditParts(objects, editParts);
			setPixelsizes(editPartsObjectsMapping, MAXPIXELWIDTH, MAXPIXELHEIGHT);
			
			LayoutVisualizerManager vm = new LayoutVisualizerManager(objects, links, statements);
			vm.addProgressMonitor(monitor);
			vm.arrange();
			
			objects = vm.getObjects();
			links = vm.getAssociations();
			
			
			List<ConnectionNodeEditPart> connections = getConnectionsOfEditParts(editParts);
			Map<ConnectionNodeEditPart, List<Point>> linksTransform = pairRoutesToConnectionEditParts(links, connections);
			Map<GraphicalEditPart, Rectangle> objectsTransform = createObjectRectangleMappingFromObjectsAndEditParts(objects, editParts);
			
			transformObjectsAndLinks(objectsTransform, linksTransform, 
					vm.getPixelGridRatioHorizontal(), vm.getPixelGridRatioVertical());
			
			modifyEditParts(objectsTransform);		
			modifyConnectionEditParts(linksTransform, objectsTransform);	
		}
	}

	private Map<GraphicalEditPart, Rectangle> createObjectRectangleMappingFromObjectsAndEditParts(Set<RectangleObject> objects,
			List<EditPart> editParts) {
		Map<GraphicalEditPart, Rectangle> result = new HashMap<>();
		for(RectangleObject obj : objects){
			Optional<Element> e = this.mapper.findElement(obj.getName());
			if(e.isPresent()){
				GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(editParts, e.get());
				if(ep != null){
					Rectangle rect =  new Rectangle(obj.getPosition().getX(), obj.getPosition().getY(),
													obj.getPixelWidth(), obj.getPixelHeight());
					result.put(ep, rect);
				}
			}
		}
		return result;
	}

	private void setPixelsizes(Map<GraphicalEditPart, RectangleObject> editPartsObjectsMapping, int maxW, int maxH) {
		editPartsObjectsMapping.forEach((GraphicalEditPart ep, RectangleObject obj) -> {
			int w = getSize(ep).width;
			int h = getSize(ep).height;
			obj.setPixelWidth(w > maxW ? maxW : w);
			obj.setPixelHeight(h > maxH ? maxH : h);
		});
	}

	private void modifyConnectionEditParts(
			Map<ConnectionNodeEditPart, List<Point>> linksTransform,
			Map<GraphicalEditPart, Rectangle> objectTransform) {
		
		linksTransform.forEach((ConnectionNodeEditPart connection, List<Point> route)  ->{
					if(connection != null && route.size() >= 2){
						Rectangle source = objectTransform.get(connection.getSource());
						Rectangle target = objectTransform.get(connection.getTarget());
						
			        	String anchor_start = getAnchor(source.getTopLeft(), route.get(0), source.width, source.height);
			        	String anchor_end = getAnchor(target.getTopLeft(), route.get(route.size()-1), target.width, target.height);
			        	route.remove(0);
			        	route.remove(route.size()-1);
			        	connection.getSource();
			        	
			        	DiagramElementsModifier.setConnectionAnchors(connection, anchor_start, anchor_end);
			        	DiagramElementsModifier.setConnectionBendpoints(connection, route);
					}
		});
	}
	
	private void modifyEditParts(
			Map<GraphicalEditPart, Rectangle> editPartsObjectsMapping) {
		editPartsObjectsMapping.forEach((GraphicalEditPart ep, Rectangle rect) -> {
				DiagramElementsModifier.resizeGraphicalEditPart(ep, rect.width, rect.height);
				DiagramElementsModifier.moveGraphicalEditPart(ep, rect.getTopLeft());
			});
	}

	private void transformObjectsAndLinks(
			Map<GraphicalEditPart, Rectangle> objectsTransform,
			Map<ConnectionNodeEditPart, List<Point>> linksTransform,
			int pixelGridRatioHorizontal, int pixelGridRatioVertical) {
		
		LayoutTransformer trans = new LayoutTransformer(pixelGridRatioHorizontal, 
				pixelGridRatioVertical);
		trans.setOrigo(OrigoConstraint.UpperLeft);
		trans.flipYAxis();
		trans.doTranformations(objectsTransform, linksTransform);
	}

	private Map<ConnectionNodeEditPart, List<Point>> pairRoutesToConnectionEditParts(
			Collection<LineAssociation> links,
			List<ConnectionNodeEditPart> connections) {
		
		Map<ConnectionNodeEditPart, List<Point>> linksTransform = new HashMap<ConnectionNodeEditPart, List<Point>>(); 
		for(LineAssociation la : links){
			
			Optional<? extends Element> e = this.mapper.findAssociation(la.getId());
			if(!e.isPresent()) e = this.mapper.findGeneralization(la.getFrom(), la.getTo());
			
			if(e.isPresent()){
				ConnectionNodeEditPart connection = (ConnectionNodeEditPart) getEditPartOfModelElement(connections, e.get());
				Element source = (Element) ((View)connection.getSource().getModel()).getElement();
				Element target = (Element) ((View)connection.getTarget().getModel()).getElement();
				
				Optional<Element> from = this.mapper.findElement(la.getFrom());
				Optional<Element> to = this.mapper.findElement(la.getTo());
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

	private Map<GraphicalEditPart, RectangleObject> pairObjectsToEditParts(Collection<RectangleObject> objects,
			List<EditPart> editParts) {
		
		Map<GraphicalEditPart, RectangleObject> objectsTransform = new HashMap<>();
		for(RectangleObject obj : objects){
			Optional<Element> e = this.mapper.findElement(obj.getName());
			if(e.isPresent()){
				GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(editParts, e.get());
				if(ep != null){
					objectsTransform.put(ep, obj);
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
