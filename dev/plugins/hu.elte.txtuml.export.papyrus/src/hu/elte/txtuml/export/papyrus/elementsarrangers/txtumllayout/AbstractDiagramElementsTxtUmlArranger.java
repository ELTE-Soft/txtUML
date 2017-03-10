package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomStateEditPart;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.diagrams.common.LayoutTransformer;
import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

/**
 * An abstract class for arranging the elements with the txtUML arranging algorithm. 
 */
public abstract class  AbstractDiagramElementsTxtUmlArranger extends AbstractDiagramElementsArranger{

	protected int MINWIDTH = 20;
	protected int MINHEIGHT = 20;
	protected int MAXWIDTH = 200;
	protected int MAXHEIGHT = 200;
	
	protected TxtUMLElementsRegistry txtUmlRegistry;
	
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
	protected void arrangeChildren(GraphicalEditPart parent, IProgressMonitor monitor) throws ArrangeException{
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> editParts = parent.getChildren();
		if(!editParts.isEmpty()){
			DiagramExportationReport report = txtUmlRegistry.getDescriptor().getReport(this.diagep.getDiagramView().getName());
			Set<RectangleObject> objects = report.getNodes();
			Set<LineAssociation> links = report.getLinks();
			List<Statement> statements =  report.getStatements();
			
			Map<GraphicalEditPart, RectangleObject> editPartsObjectsMapping  = pairObjectsToEditParts(objects, editParts);
			setPixelsizes(editPartsObjectsMapping);
			
			LayoutVisualizerManager vm = new LayoutVisualizerManager(objects, links, statements);
			vm.addProgressMonitor(monitor);
			vm.arrange();
			
			objects = vm.getObjects();
			links = vm.getAssociations();
			
			
			List<ConnectionNodeEditPart> connections = getConnectionsOfEditParts(editParts);
			Map<ConnectionNodeEditPart, List<hu.elte.txtuml.export.diagrams.common.Point>> linksTransform = pairRoutesToConnectionEditParts(links, connections);
			Map<GraphicalEditPart, hu.elte.txtuml.export.diagrams.common.Rectangle> objectsTransform = createObjectRectangleMappingFromObjectsAndEditParts(objects, editParts);
			
			transformObjectsAndLinks(objectsTransform, linksTransform, 
					vm.getPixelGridRatioHorizontal(), vm.getPixelGridRatioVertical());
			
			this.modifyEditParts(objectsTransform);		
			this.modifyConnectionEditParts(linksTransform, objectsTransform);	
		}
	}

	private Map<GraphicalEditPart, hu.elte.txtuml.export.diagrams.common.Rectangle> createObjectRectangleMappingFromObjectsAndEditParts(Set<RectangleObject> objects,
			List<GraphicalEditPart> editParts) {
		Map<GraphicalEditPart, hu.elte.txtuml.export.diagrams.common.Rectangle> result = new HashMap<>();
		for(RectangleObject obj : objects){
			Optional<Element> e = txtUmlRegistry.findElement(obj.getName());
			if(e.isPresent()){
				GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(editParts, e.get());
				if(ep != null){
					hu.elte.txtuml.export.diagrams.common.Rectangle rect =  new hu.elte.txtuml.export.diagrams.common.Rectangle(obj.getPosition().getX(), obj.getPosition().getY(),
													obj.getPixelWidth(), obj.getPixelHeight());
					result.put(ep, rect);
				}
			}
		}
		return result;
	}

	private void setPixelsizes(Map<GraphicalEditPart, RectangleObject> editPartsObjectsMapping) {
		editPartsObjectsMapping.forEach((GraphicalEditPart ep, RectangleObject obj) -> {
			int w = getSize(ep).width;
			int h = getSize(ep).height;
			int nW = w > MAXWIDTH ? MAXWIDTH : w;
			int nH = h > MAXHEIGHT ? MAXHEIGHT : h;
			obj.setPixelWidth(nW);
			obj.setPixelHeight(nH);
		});
	}

	private void modifyConnectionEditParts(
			Map<ConnectionNodeEditPart, List<hu.elte.txtuml.export.diagrams.common.Point>> linksTransform,
			Map<GraphicalEditPart, hu.elte.txtuml.export.diagrams.common.Rectangle> objectTransform) {
		linksTransform.forEach((ConnectionNodeEditPart connection, List<hu.elte.txtuml.export.diagrams.common.Point> route)  ->{
					if(connection != null && route.size() >= 2){
						hu.elte.txtuml.export.diagrams.common.Rectangle source = objectTransform.get(connection.getSource());
						hu.elte.txtuml.export.diagrams.common.Rectangle target = objectTransform.get(connection.getTarget());

			        	String anchor_start = getAnchor(source.getTopLeft(), route.get(0), source.width(), source.height());
			        	String anchor_end = getAnchor(target.getTopLeft(), route.get(route.size()-1), target.width(), target.height());

			        	DiagramElementsModifier.setConnectionAnchors(connection, anchor_start, anchor_end);
			        	List<Point> gmfRoute = route.stream().map(p -> new Point(p.x(), p.y())).collect(Collectors.toList());
			        	DiagramElementsModifier.setConnectionPoints(connection, gmfRoute);
					}
		});
	}

	private void modifyEditParts(Map<GraphicalEditPart, hu.elte.txtuml.export.diagrams.common.Rectangle> editPartsObjectsMapping) {
		editPartsObjectsMapping.forEach((GraphicalEditPart ep, hu.elte.txtuml.export.diagrams.common.Rectangle rect) -> {
			if(ep instanceof CustomStateEditPart) {
				DiagramElementsModifier.fixStateLabelHeight((CustomStateEditPart)ep);
			}
			DiagramElementsModifier.resizeGraphicalEditPart(ep, rect.width(), rect.height());
			DiagramElementsModifier.moveGraphicalEditPart(ep, new Point(20+rect.getTopLeft().x(), 20+rect.getTopLeft().y()));
		});
	}

	private void transformObjectsAndLinks(
			Map<GraphicalEditPart, hu.elte.txtuml.export.diagrams.common.Rectangle> objectsTransform,
			Map<ConnectionNodeEditPart, List<hu.elte.txtuml.export.diagrams.common.Point>> linksTransform,
			int pixelGridRatioHorizontal, int pixelGridRatioVertical) {
		
		LayoutTransformer trans = new LayoutTransformer(pixelGridRatioHorizontal, 
				pixelGridRatioVertical);
		trans.doTranformations(objectsTransform, linksTransform);
	}

	private Map<ConnectionNodeEditPart, List<hu.elte.txtuml.export.diagrams.common.Point>> pairRoutesToConnectionEditParts(
			Collection<LineAssociation> links,
			List<ConnectionNodeEditPart> connections) {
		
		Map<ConnectionNodeEditPart, List<hu.elte.txtuml.export.diagrams.common.Point>> linksTransform = new HashMap<>(); 
		for(LineAssociation la : links){
			
			Optional<? extends Element> e = findConnection(la);
			
			if(e.isPresent()){
				ConnectionNodeEditPart connection = (ConnectionNodeEditPart) getEditPartOfModelElement(connections, e.get());
				if(connection == null) continue;
				Element source = (Element) ((View)connection.getSource().getModel()).getElement();
				Element target = (Element) ((View)connection.getTarget().getModel()).getElement();
				
				Optional<Element> from = txtUmlRegistry.findElement(la.getFrom());
				Optional<Element> to = txtUmlRegistry.findElement(la.getTo());
				if(!from.isPresent() || !to.isPresent()) continue;
				
				List<hu.elte.txtuml.export.diagrams.common.Point> route = new LinkedList<>();

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
					
					route.add(new hu.elte.txtuml.export.diagrams.common.Point(layoutRoute.get(index).getX(),layoutRoute.get(index).getY()));
				}
				linksTransform.put(connection, route);
			}
		}
		return linksTransform;
	}

	/**
	 * Returns the required element associated with the given lineassociation 
	 * @param la - the lineAssociation in the registry
	 * @return
	 */
	protected Optional<? extends Element> findConnection(LineAssociation la) {
		Optional<? extends Element> e = txtUmlRegistry.findAssociation(la.getId());
		if(!e.isPresent()) e = txtUmlRegistry.findGeneralization(la.getFrom(), la.getTo());
		return e;
	}

	private Map<GraphicalEditPart, RectangleObject> pairObjectsToEditParts(Collection<RectangleObject> objects,
			List<GraphicalEditPart> editParts) {
		
		Map<GraphicalEditPart, RectangleObject> objectsTransform = new HashMap<>();
		for(RectangleObject obj : objects){
			Optional<Element> e = txtUmlRegistry.findElement(obj.getName());
			if(e.isPresent()){
				GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(editParts, e.get());
				if(ep != null){
					objectsTransform.put(ep, obj);
				}
			}
		}
		return objectsTransform;
	}

	private List<ConnectionNodeEditPart> getConnectionsOfEditParts(List<GraphicalEditPart> editParts) {
		List<ConnectionNodeEditPart> connections = new LinkedList<ConnectionNodeEditPart>();
		for (GraphicalEditPart editpart : editParts) {
			@SuppressWarnings("unchecked")
			List<ConnectionNodeEditPart> conns = editpart.getSourceConnections();
			connections.addAll(conns);
		}
		return connections;
	}

	/**
	 * @param center
	 * @param edge
	 * @return
	 */
	protected String getAnchor(hu.elte.txtuml.export.diagrams.common.Point topleft, hu.elte.txtuml.export.diagrams.common.Point edge, int objectWidth, int objectHeight){
		hu.elte.txtuml.export.diagrams.common.Point relativeEdge = new hu.elte.txtuml.export.diagrams.common.Point(edge.x() - topleft.x(), edge.y() -  topleft.y());
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
