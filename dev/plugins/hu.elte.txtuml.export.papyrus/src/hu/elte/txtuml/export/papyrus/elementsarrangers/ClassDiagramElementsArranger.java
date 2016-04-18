package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

public class ClassDiagramElementsArranger implements IDiagramElementsArranger {

	private static final int PIXELWIDTH_OF_CHARACTER = 12;
	private static final int PIXELHEIGHT_OF_PROPERTY = 25;
	private static final int MIN_CLASS_WIDTH = 100;
	private static final int MAX_CLASS_WIDTH = 200;
	private static final int MIN_CLASS_HEIGHT = 100;
	private static final int MAX_CLASS_HEIGHT = 200;

	private DiagramExportationReport report;
	private Map<Element, Rectangle> elementbounds;
	private Map<Relationship, List<Point>> connectionRoutes;
	private TxtUMLElementsMapper elementsMapper;
	private Map<Relationship, String> connectionSourceAnchors;
	private Map<Relationship, String> connectionTargetAnchors;

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

		setPixelsizes(objects);

		LayoutVisualizerManager vm = new LayoutVisualizerManager(objects, links, statements);
		vm.addProgressMonitor(monitor);
		vm.arrange();

		Set<RectangleObject> arrangedObjects = vm.getObjects();
		Set<LineAssociation> arrangedLinks = vm.getAssociations();

		this.elementbounds = createElementsMapping(arrangedObjects);
		this.connectionRoutes = createConnectionMapping(arrangedLinks);

		LayoutTransformer transformer = new LayoutTransformer(vm.getPixelGridRatioHorizontal(),
				vm.getPixelGridRatioVertical());

		transformer.doTranformations(this.elementbounds, this.connectionRoutes);

		this.connectionSourceAnchors = createSourceAnchors(arrangedLinks);
		this.connectionTargetAnchors = createTargetAnchors(arrangedLinks);
		monitor.worked(1);
	}

	private Map<Element, Rectangle> createElementsMapping(Set<RectangleObject> arrangedObjects) {
		return arrangedObjects.stream()
				.collect(Collectors.toMap(ro -> this.elementsMapper.findNode(ro.getName()),
						ro -> new Rectangle(ro.getTopLeft().getX(), ro.getTopLeft().getY(), ro.getPixelWidth(),
								ro.getPixelHeight())));
	}

	private Map<Relationship, List<Point>> createConnectionMapping(Set<LineAssociation> arrangedLinks) {
		return arrangedLinks.stream()
				.collect(Collectors.toMap(la -> this.elementsMapper.findConnection(la.getId()),
						la -> la.getMinimalRoute().stream().map(p -> new Point(p.getX(), p.getY()))
								.collect(Collectors.toList())));
	}

	private void setPixelsizes(Set<RectangleObject> objects) {
		objects.forEach(object -> {
			Element elem = this.elementsMapper.findNode(object.getName());
			if (elem != null && elem instanceof Classifier) {
				int width = ((Classifier) elem).getFeatures().stream()
						.mapToInt((attribute) -> attribute.getName().length() * PIXELWIDTH_OF_CHARACTER).max()
						.orElse(0);
				width = width < MIN_CLASS_WIDTH ? MIN_CLASS_WIDTH : width;
				width = width > MAX_CLASS_WIDTH ? MAX_CLASS_WIDTH : width;
				
				int height = ((Classifier) elem).getFeatures().size()* PIXELHEIGHT_OF_PROPERTY;
				height = height < MIN_CLASS_HEIGHT ? MIN_CLASS_HEIGHT : height;
				height = height > MAX_CLASS_HEIGHT ? MAX_CLASS_HEIGHT : height;
				
				
				object.setPixelWidth(width);
				object.setPixelHeight(height);
			}
		});

	}

	private Map<Relationship, String> createTargetAnchors(Set<LineAssociation> links) {
		Map<Relationship, String> result = new HashMap<>();
		links.forEach(l -> {
			Relationship connection = this.elementsMapper.findConnection(l.getId());
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
			Relationship connection = this.elementsMapper.findConnection(l.getId());
			Rectangle sourceNode = this.elementbounds.get(this.elementsMapper.findNode(l.getFrom()));
			Point startPoint = this.connectionRoutes.get(connection).get(0);
			result.put(connection, "(" + (startPoint.x - sourceNode.x) / (float) sourceNode.width + ","
					+ (startPoint.y - sourceNode.y) / (float) sourceNode.height + ")");
		});
		return result;
	}

	@Override
	public Rectangle getBoundsForElement(Element element) {
		return this.elementbounds.get(element);
	}

	@Override
	public List<Point> getRouteForConnection(Relationship connection) {
		return this.connectionRoutes.get(connection);
	}

	public String getSourceAnchorForConnection(Relationship assoc) {
		return this.connectionSourceAnchors.get(assoc);
	}

	public String getTargetAnchorForConnection(Relationship assoc) {
		return this.connectionTargetAnchors.get(assoc);
	}

}
