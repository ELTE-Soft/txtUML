package hu.elte.txtuml.export.papyrus.arrange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.layout.IDiagramElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.utils.Logger;

public abstract class AbstractDiagramElementsArranger implements IDiagramElementsArranger {

	protected IPixelDimensionProvider pixelDimensionProvider;
	protected Map<Element, Rectangle> elementbounds = new HashMap<>();
	protected Map<Element, List<Point>> connectionRoutes = new HashMap<>();
	private Map<Element, String> connectionSourceAnchors = new HashMap<>();
	private Map<Element, String> connectionTargetAnchors = new HashMap<>();
	public DiagramExportationReport report;
	public IDiagramElementsMapper elementsMapper;

	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		Set<RectangleObject> objects = report.getNodes();
		Set<LineAssociation> links = report.getLinks();
		List<Statement> statements = report.getStatements();
		DiagramType dType = convertDiagramType(report.getType());
	
		LayoutVisualizerManager vm = new LayoutVisualizerManager(objects, links, statements, dType,
				this.pixelDimensionProvider);
		vm.addProgressMonitor(monitor);
		vm.arrange();
	
		Set<RectangleObject> arrangedObjects = vm.getObjects();
		Set<LineAssociation> arrangedLinks = vm.getAssociations();
	
		this.elementbounds = createElementsMapping(arrangedObjects);
		Set<LineAssociation> allLinks = flattenAllLinks(arrangedLinks, arrangedObjects);
		this.connectionRoutes = createConnectionMapping(allLinks);
	
		LayoutTransformer transformer = new LayoutTransformer(vm.getPixelGridRatioHorizontal(),
				vm.getPixelGridRatioVertical());
	
		transformer.doTranformations(this.elementbounds, this.connectionRoutes);
	
		this.connectionSourceAnchors = createSourceAnchors(allLinks);
		this.connectionTargetAnchors = createTargetAnchors(allLinks);
	}

	private Set<LineAssociation> flattenAllLinks(Set<LineAssociation> arrangedLinks,
			Set<RectangleObject> arrangedObjects) {
		Set<LineAssociation> links = new HashSet<LineAssociation>();

		for (LineAssociation link : arrangedLinks) {
			links.add(link);
		}

		for (RectangleObject box : arrangedObjects) {
			if (box.hasInner()) {
				links.addAll(flattenAllLinks(box.getInner().Assocs, box.getInner().Objects));
			}
		}

		return links;
	}


	private Map<Element, Rectangle> createElementsMapping(Set<RectangleObject> arrangedObjects) {
		List<RectangleObject> flattenedObjects = flattenArrangedObjectsRecursively(arrangedObjects);
	
		return flattenedObjects.stream()
				.collect(Collectors.toMap(ro -> this.elementsMapper.findNode(ro.getName()),
						ro -> new Rectangle(ro.getTopLeft().getX(), ro.getTopLeft().getY(), ro.getPixelWidth(),
								ro.getPixelHeight())));
	}

	private List<RectangleObject> flattenArrangedObjectsRecursively(Set<RectangleObject> arrangedObjects) {
		List<RectangleObject> arrayList = new ArrayList<>();
		arrangedObjects.forEach(object -> {
			if (object.hasInner()) {
				arrayList.addAll(flattenArrangedObjectsRecursively(object.getInner().Objects));			
			}
			
			arrayList.add(object);
		});
		return arrayList;
	}

	private Map<Element, List<Point>> createConnectionMapping(Set<LineAssociation> arrangedLinks) {
		Map<Element, List<Point>> result = arrangedLinks.stream()
				.collect(Collectors.toMap(la -> this.elementsMapper.findConnection(la.getId()),
						la -> la.getMinimalRoute().stream().map(p -> new Point(p.getX(), p.getY()))
								.collect(Collectors.toList())));
		return result;
	}

	private Map<Element, String> createTargetAnchors(Set<LineAssociation> links) {
		Map<Element, String> result = new HashMap<>();
		links.forEach(l -> {
			Element connection = this.elementsMapper.findConnection(l.getId());
			Rectangle targetNode = this.elementbounds.get(this.elementsMapper.findNode(l.getTo()));
			List<Point> pointlist = this.connectionRoutes.get(connection);
			Point endPoint = pointlist.get(pointlist.size() - 1);
			String anchor = "(" + (endPoint.x - targetNode.x) / (float) targetNode.width + ","
					+ (endPoint.y - targetNode.y) / (float) targetNode.height + ")";
			result.put(connection, anchor);
		});
		return result;
	}

	private Map<Element, String> createSourceAnchors(Set<LineAssociation> links) {
		Map<Element, String> result = new HashMap<>();
		links.forEach(l -> {
			Element connection = this.elementsMapper.findConnection(l.getId());
			Rectangle sourceNode = this.elementbounds.get(this.elementsMapper.findNode(l.getFrom()));
			Point startPoint = this.connectionRoutes.get(connection).get(0);
			result.put(connection, "(" + (startPoint.x - sourceNode.x) / (float) sourceNode.width + ","
					+ (startPoint.y - sourceNode.y) / (float) sourceNode.height + ")");
		});
		return result;
	}

	@Override
	public Rectangle getBoundsForElement(Element element) {
		Rectangle rect = this.elementbounds.get(element);
		if (rect == null) {
			rect = new Rectangle(0, 0, 100, 100);
			Logger.sys.error("Could not find bounds for an element");
		}
		return rect;
	}

	@Override
	public List<Point> getRouteForConnection(Element connection) {
		return this.connectionRoutes.get(connection);
	}

	@Override
	public String getSourceAnchorForConnection(Element assoc) {
		return this.connectionSourceAnchors.get(assoc);
	}

	@Override
	public String getTargetAnchorForConnection(Element assoc) {
		return this.connectionTargetAnchors.get(assoc);
	}

	protected static DiagramType convertDiagramType(hu.elte.txtuml.layout.export.DiagramType type) {
		switch (type) {
		case Class:
			return DiagramType.Class;
		case StateMachine:
			return DiagramType.State;
		default:
			return DiagramType.unknown;
		}
	}	
}
