package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import hu.elte.txtuml.export.papyrus.arrange.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.arrange.ArrangeException;
import hu.elte.txtuml.export.papyrus.arrange.LayoutTransformer;
import hu.elte.txtuml.export.papyrus.arrange.LayoutVisualizerManager;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.utils.Logger;

public class ClassDiagramElementsArranger extends AbstractDiagramElementsArranger {

	private DiagramExportationReport report;
	private ClassDiagramElementsMapper elementsMapper;
	private Map<Element, Rectangle> elementbounds = new HashMap<>();
	private Map<Relationship, List<Point>> connectionRoutes = new HashMap<>();
	private Map<Relationship, String> connectionSourceAnchors = new HashMap<>();
	private Map<Relationship, String> connectionTargetAnchors = new HashMap<>();

	public ClassDiagramElementsArranger(DiagramExportationReport report, ClassDiagramElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		this.pixelDimensionProvider = new ClassDiagramPixelDimensionProvider(mapper);
	}
	
	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		monitor.beginTask("Arrange", 1);
		monitor.subTask("Arranging elements...");

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
		this.connectionRoutes = createConnectionMapping(arrangedLinks, arrangedObjects);

		LayoutTransformer transformer = new LayoutTransformer(vm.getPixelGridRatioHorizontal(),
				vm.getPixelGridRatioVertical());

		transformer.doTranformations(this.elementbounds, this.connectionRoutes);

		this.connectionSourceAnchors = createSourceAnchors(arrangedLinks);
		this.connectionTargetAnchors = createTargetAnchors(arrangedLinks);
		monitor.worked(1);
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
			if(object.hasInner()){
				arrayList.addAll(flattenArrangedObjectsRecursively(object.getInner().Objects));
			}else{
				arrayList.add(object);
			}
		});
		return arrayList;
	}

	private Map<Relationship, List<Point>> createConnectionMapping(Set<LineAssociation> arrangedLinks, Set<RectangleObject> arrangedObjects) {
		Map<Relationship, List<Point>> result = arrangedLinks.stream()
				.collect(Collectors.toMap(la -> (Relationship) this.elementsMapper.findConnection(la.getId()),
						la -> la.getMinimalRoute().stream().map(p -> new Point(p.getX(), p.getY()))
								.collect(Collectors.toList())));
		
		result.putAll(addArrangedLinksRecursivelyFromObjects(arrangedObjects));
		
		return result;
	}

	private Map<Relationship, List<Point>> addArrangedLinksRecursivelyFromObjects(Set<RectangleObject> arrangedObjects) {
		Map<Relationship, List<Point>> result = new HashMap<Relationship, List<Point>>();
		arrangedObjects.forEach(object -> {
			if(object.hasInner()){
				result.putAll(object.getInner().Assocs.stream().collect(Collectors.toMap(la -> (Relationship) this.elementsMapper.findConnection(la.getId()),
						la -> la.getMinimalRoute().stream().map(p -> new Point(p.getX(), p.getY()))
								.collect(Collectors.toList()))));
				result.putAll(addArrangedLinksRecursivelyFromObjects(object.getInner().Objects));
			}
		});
		return result;
	}

	private Map<Relationship, String> createTargetAnchors(Set<LineAssociation> links) {
		Map<Relationship, String> result = new HashMap<>();
		links.forEach(l -> {
			Relationship connection = (Relationship) this.elementsMapper.findConnection(l.getId());
			Rectangle targetNode = this.elementbounds.get(this.elementsMapper.findNode(l.getTo()));
			List<Point> pointlist = this.connectionRoutes.get(connection);
			Point endPoint = pointlist.get(pointlist.size() - 1);
			String anchor = "(" + (endPoint.x - targetNode.x) / (float) targetNode.width + ","
					+ (endPoint.y - targetNode.y) / (float) targetNode.height + ")";
			result.put(connection, anchor);
		});
		return result;
	}

	private Map<Relationship, String> createSourceAnchors(Set<LineAssociation> links) {
		Map<Relationship, String> result = new HashMap<>();
		links.forEach(l -> {
			Relationship connection = (Relationship) this.elementsMapper.findConnection(l.getId());
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
			rect = new Rectangle(0, 0, 100, 100); // TODO: Add a proper
													// implementation
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

}
