package hu.elte.txtuml.export.papyrus.elementsarrangers;

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
import org.eclipse.uml2.uml.Transition;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformer;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.StateMachineDiagramPixelDimensionProvider;
import hu.elte.txtuml.export.papyrus.layout.txtuml.StateMachineDiagramElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.utils.Logger;

public class StateMachineDiagramElementsArranger extends AbstractDiagramElementsArranger {

	private StateMachineDiagramElementsMapper elementsMapper;
	private Map<Element, Rectangle> elementbounds = new HashMap<>();
	private Map<Transition, List<Point>> connectionRoutes = new HashMap<>();
	private Map<Transition, String> connectionSourceAnchors;
	private Map<Transition, String> connectionTargetAnchors;
	private DiagramExportationReport report;

	public StateMachineDiagramElementsArranger(DiagramExportationReport report,
			StateMachineDiagramElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		this.pixelDimensionProvider = new StateMachineDiagramPixelDimensionProvider(mapper);
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
		this.connectionRoutes = createConnectionMapping(arrangedLinks);

		LayoutTransformer transformer = new LayoutTransformer(vm.getPixelGridRatioHorizontal(),
				vm.getPixelGridRatioVertical());

		transformer.doTranformations(this.elementbounds, this.connectionRoutes);

		this.connectionSourceAnchors = createSourceAnchors(new HashSet<LineAssociation>());
		this.connectionTargetAnchors = createTargetAnchors(new HashSet<LineAssociation>());
		monitor.worked(1);
	}

	private Map<Element, Rectangle> createElementsMapping(Set<RectangleObject> arrangedObjects) {
		return arrangedObjects.stream()
				.collect(Collectors.toMap(ro -> this.elementsMapper.findNode(ro.getName()),
						ro -> new Rectangle(ro.getTopLeft().getX(), ro.getTopLeft().getY(), ro.getPixelWidth(),
								ro.getPixelHeight())));
	}

	private Map<Transition, List<Point>> createConnectionMapping(Set<LineAssociation> arrangedLinks) {
		return arrangedLinks.stream()
				.collect(Collectors.toMap(la -> (Transition) this.elementsMapper.findConnection(la.getId()),
						la -> la.getMinimalRoute().stream().map(p -> new Point(p.getX(), p.getY()))
								.collect(Collectors.toList())));
	}

	private Map<Transition, String> createTargetAnchors(Set<LineAssociation> links) {
		Map<Transition, String> result = new HashMap<>();
		links.forEach(l -> {
			Transition connection = (Transition) this.elementsMapper.findConnection(l.getId());
			Rectangle targetNode = this.elementbounds.get(this.elementsMapper.findNode(l.getTo()));
			List<Point> pointlist = this.connectionRoutes.get(connection);
			Point endPoint = pointlist.get(pointlist.size() - 1);
			String anchor = "(" + (endPoint.x - targetNode.x) / (float) targetNode.width + ","
					+ (endPoint.y - targetNode.y) / (float) targetNode.height + ")";
			result.put(connection, anchor);
		});
		return result;
	}

	private Map<Transition, String> createSourceAnchors(Set<LineAssociation> links) {
		Map<Transition, String> result = new HashMap<>();
		links.forEach(l -> {
			Transition connection = (Transition) this.elementsMapper.findConnection(l.getId());
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
			rect = new Rectangle(0, 0, 40, 40); // TODO: Add a proper
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
	public String getSourceAnchorForConnection(Element transition) {
		return this.connectionSourceAnchors.get(transition);
	}

	@Override
	public String getTargetAnchorForConnection(Element transition) {
		return this.connectionTargetAnchors.get(transition);
	}

}
