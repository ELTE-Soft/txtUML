package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

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
	 * @throws UnknownStatementException 
	 * @throws CannotFindAssociationRouteException 
	 * @throws StatementTypeMatchException 
	 * @throws ConversionException 
	 * @throws ConflictException 
	 * @throws InternalException 
	 */
	protected void arrangeChildren(EditPart parent) throws InternalException, ConflictException, ConversionException, StatementTypeMatchException, CannotFindAssociationRouteException, UnknownStatementException {
		@SuppressWarnings("unchecked")
		List<EditPart> elements = parent.getChildren();
		if(!elements.isEmpty()){
			int maxWidth = getMaxWidth(elements);
			int maxHeight = getMaxHeight(elements);
			int gapX = 6;
			int gapY = 6;
			
			List<ConnectionNodeEditPart> connections = new LinkedList<ConnectionNodeEditPart>();
			for (EditPart editpart : elements) {
				@SuppressWarnings("unchecked")
				List<ConnectionNodeEditPart> conns = ((GraphicalEditPart) editpart).getSourceConnections();
				connections.addAll(conns);
			}
			
			LayoutVisualizerManager vm = new LayoutVisualizerManager(txtUmlRegistry);
			vm.arrange();
			
			Collection<RectangleObject> objects = vm.getObjects();
			Collection<LineAssociation> links = vm.getAssociations();
			
			Map<GraphicalEditPart, Rectangle> objectsTransform = new HashMap<GraphicalEditPart, Rectangle>();
			Map<ConnectionNodeEditPart, List<Point>> linksTransform = new HashMap<ConnectionNodeEditPart, List<Point>>(); 
			
			for(RectangleObject obj:objects){
				Element e = txtUmlRegistry.findElement(obj.getName());
				if(e != null){
					GraphicalEditPart ep = (GraphicalEditPart) getEditPartOfModelElement(elements, e);
					if(ep != null){
						objectsTransform.put(ep, new Rectangle(obj.getTopLeft().getX(),
										obj.getTopLeft().getY(), getSize(ep).width(), getSize(ep).height()));
					}
				}
			}
			
			int gridDensity = objects.isEmpty() ? 0 : objects.iterator().next().getWidth();
			
			for(LineAssociation la : links){
				List<Point> route = new LinkedList<Point>();
				//The point of the route have opposite order in the two representations
				List<hu.elte.txtuml.layout.visualizer.model.Point> layoutRoute = la.getMinimalRoute();
				
				for(int i = layoutRoute.size()-1; i >= 0; i--){
					route.add(new Point(layoutRoute.get(i).getX(),layoutRoute.get(i).getY()));
				}
				
				Element e = txtUmlRegistry.findAssociation(la.getId());
				if(e == null) e = txtUmlRegistry.findGeneralization(la.getId());
				if(e != null){
					ConnectionNodeEditPart connection = (ConnectionNodeEditPart) getEditPartOfModelElement(connections, e);
					linksTransform.put(connection, route);
				}
			}
			
			
			LayoutTransformer trans = new LayoutTransformer(maxWidth, maxHeight, gapX, gapY, gridDensity-1);
			trans.setOrigo(OrigoConstraint.UpperLeft);
			trans.flipYAxis();
			trans.doTranformations(objectsTransform, linksTransform);
			
			
			objectsTransform.forEach(new BiConsumer<GraphicalEditPart, Rectangle>() {
				@Override
				public void accept(GraphicalEditPart ep, Rectangle position) {
					AbstractDiagramElementsTxtUmlArranger.super
						.moveGraphicalEditPart((GraphicalEditPart) ep, position.getTopLeft());
				}
			});
		
			
			linksTransform.forEach(new BiConsumer<ConnectionNodeEditPart , List<Point>>() {
				@Override
				public void accept(ConnectionNodeEditPart connection, List<Point> route) {
						if(connection != null && route.size() >= 2){
				        	AbstractDiagramElementsTxtUmlArranger.super.setConnectionAnchors(connection, "(0.5,0.5)", "(0.5,0.5)");
				        	route.remove(0);
				        	route.remove(route.size()-1);
				        	AbstractDiagramElementsTxtUmlArranger.super.setConnectionBendpoints(connection, route);
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
