package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.TxtUMLElementsFinder;
import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer.OrigoConstraint;
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
	
	private TxtUMLElementsFinder finder;
	
	/**
	 * The Constructor 
	 * @param diagramEditPart - The EditPart of the diagram which elements is to arranged.
	 * @param finder - The {@link TxtUMLElementsFinder} which specifies the layout
	 */
	public AbstractDiagramElementsTxtUmlArranger(DiagramEditPart diagramEditPart, TxtUMLElementsFinder finder) {
		super(diagramEditPart);
		this.finder = finder;
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
	protected void arrangeChildren(EditPart EP, List<EditPart> elements) throws InternalException, ConflictException, ConversionException, StatementTypeMatchException, CannotFindAssociationRouteException, UnknownStatementException {
		if(!elements.isEmpty()){
			int maxWidth = getMaxWidth(elements);
			int maxHeight = getMaxHeight(elements);
			
			List<ConnectionNodeEditPart> connections = new LinkedList<ConnectionNodeEditPart>();
			for (EditPart editpart : elements) {
				@SuppressWarnings("unchecked")
				List<ConnectionNodeEditPart> conns = ((GraphicalEditPart) editpart).getSourceConnections();
				connections.addAll(conns);
			}
			
			LayoutVisualizerManager vm = new LayoutVisualizerManager(finder);
			vm.arrange();
			
			Collection<RectangleObject> objects = vm.getObjects();
			Collection<LineAssociation> links = vm.getAssociations();
			
			Map<String, Rectangle> objects2 = new HashMap<String, Rectangle>();
			Map<String, List<Point>> links2 = new HashMap<String, List<Point>>(); 
			
			for(RectangleObject obj:objects){
				objects2.put(obj.getName(), new Rectangle(obj.getTopLeft().getX(), obj.getTopLeft().getY(), 0, 0));
			}
			
			
			for(LineAssociation la : links){
				List<Point> route = new LinkedList<Point>();
				//The point of the route have opposite order in the two representations
				List<hu.elte.txtuml.layout.visualizer.model.Point> layoutRoute = la.getMinimalRoute();
						for(int i = layoutRoute.size()-1; i >= 0; i--){
							route.add(new Point(layoutRoute.get(i).getX(),layoutRoute.get(i).getY()));
						}
				links2.put(la.getId(), route);
			}
			
			
			int gridDensity = objects.iterator().next().getWidth()-1;
			
			LayoutTransformer trans = new LayoutTransformer(maxWidth, maxHeight, gridDensity);
			trans.setOrigo(OrigoConstraint.UpperLeft);
			trans.flipYAxis();
			trans.doTranformations(objects2, links2);
			
			
			objects2.forEach(new BiConsumer<String, Rectangle>() {
				@Override
				public void accept(String name, Rectangle position) {
					Element e = finder.findElement(name);
					if(e != null){
						EditPart ep = getEditPartOfModelElement(elements, e);
						if(ep != null){
							AbstractDiagramElementsTxtUmlArranger.super
								.moveGraphicalEditPart((GraphicalEditPart) ep, position.getTopLeft());
						}
					}
				}
				
			});
		
			
			links2.forEach(new BiConsumer<String , List<Point>>() {
				@Override
				public void accept(String Id, List<Point> route) {
					Element e = finder.findAssociation(Id);
					if(e == null) e = finder.findGeneralization(Id);
					if(e != null){
						ConnectionNodeEditPart connection = (ConnectionNodeEditPart) getEditPartOfModelElement(connections, e);
						String[] anchors;
						if(connection != null){
							anchors = defineAnchors(route);
				        	AbstractDiagramElementsTxtUmlArranger.super.setConnectionAnchors(connection, anchors[0], anchors[1]);
				        	route.remove(0);
				        	route.remove(route.size()-1);
				        	AbstractDiagramElementsTxtUmlArranger.super.setConnectionBendpoints(connection, route);
						}
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
