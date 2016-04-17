package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

public class ClassDiagramElementsArranger implements IDiagramElementsArranger {

	private DiagramExportationReport report;
	private Map<Element, Rectangle> elementbounds;
	private Map<Relationship, List<Point>> connectionRoutes;
	private TxtUMLElementsMapper elementsMapper;

	public ClassDiagramElementsArranger(DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
	}

	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		monitor.beginTask("Arrange", 1);
		monitor.subTask("Arranging elements...");

		Set<RectangleObject> objects = report.getNodes();
		Set<LineAssociation> links = report.getLinks();
		List<Statement> statements = report.getStatements();

		Map<RectangleObject, Element> elementsObjectsMapping = createElementsMapping(objects);
		Map<LineAssociation, Relationship> connectionsLinksMapping = createConnectionsMapping(links);

		setPixelsizes(elementsObjectsMapping);

		LayoutVisualizerManager vm = new LayoutVisualizerManager(objects, links, statements);
		vm.addProgressMonitor(monitor);
		vm.arrange();

		Set<RectangleObject> arrangedObjects = vm.getObjects();
		Set<LineAssociation> arrangedLinks = vm.getAssociations();
		
		createElementBounds(elementsObjectsMapping, arrangedObjects);
		createConnectionRoutesAndAnchors(connectionsLinksMapping, arrangedLinks);

		LayoutTransformer transformer = new LayoutTransformer(vm.getPixelGridRatioHorizontal(),
				vm.getPixelGridRatioVertical());

		transformer.doTranformations(this.elementbounds, this.connectionRoutes);

		monitor.worked(1);
	}

	private Map<RectangleObject, Element> createElementsMapping(Set<RectangleObject> objects) {
		Map<RectangleObject, Element> map = new HashMap<>();
		objects.forEach((object) -> {
			Optional<Element> elem = this.elementsMapper.findElement(object.getName());
			elem.ifPresent(e -> map.put(object, e));
		});
		return map;
	}

	@SuppressWarnings("unchecked")
	private Map<LineAssociation, Relationship> createConnectionsMapping(Set<LineAssociation> links) {
		Map<LineAssociation, Relationship> map = new HashMap<>();
		links.forEach((link) -> {
			Optional<Relationship> connection;
			// TODO Get all connections from mapper
			if (link.getType() == AssociationType.generalization) {
				connection = (Optional<Relationship>) (Object) this.elementsMapper.findGeneralization(link.getFrom(),
						link.getTo());
			} else {
				connection = (Optional<Relationship>) (Object) this.elementsMapper.findAssociation(link.getId());
			}

			if (connection.isPresent()) {
				map.put(link, connection.get());
			}
		});
		return map;
	}

	private void createElementBounds(Map<RectangleObject, Element> map,
			Set<RectangleObject> objects) {
		Map<Element, Rectangle> result = new HashMap<>();
		
		Map<String, RectangleObject> namesToObjects = objects.stream().collect(Collectors.toMap((rect) -> rect.getName(), (rect) -> rect));
		map.forEach((o, e) -> {
			RectangleObject object = namesToObjects.get(o.getName());
			result.put(e, new Rectangle(object.getTopLeft().getX(), object.getTopLeft().getY(), object.getPixelWidth(),
					object.getPixelHeight()));
		});
		this.elementbounds = result;
	}

	private void createConnectionRoutesAndAnchors(Map<LineAssociation, Relationship> map,
			Set<LineAssociation> links) {
		Map<Relationship, List<Point>> routes = new HashMap<>();
		Map<String, LineAssociation> namesToLinks = links.stream().collect(Collectors.toMap((link) -> link.getId(), (link) -> link));
		map.forEach((l, e) -> {
			LineAssociation link = namesToLinks.get(l.getId());
			routes.put(e, link.getMinimalRoute().stream().map((point) -> new Point(point.getX(), point.getY()))
					.collect(Collectors.toList()));
		});
		this.connectionRoutes = routes;
	}

	private void setPixelsizes(Map<RectangleObject, Element> elementsObjectsMapping) {
		elementsObjectsMapping.keySet().forEach(object -> {
			object.setPixelWidth(100);
			object.setPixelHeight(100);
		});
	}

	@Override
	public Rectangle getBoundsForElement(Element element) {
		return this.elementbounds.get(element);
	}

	@Override
	public List<Point> getRouteForConnection(Relationship connection) {
		return this.connectionRoutes.get(connection);
	}

	public String getSourceAnchorForConnection(Association assoc) {
		return null;
	}

	public String getTargetAnchorForConnection(Association assoc) {
		return null;
	}

}
