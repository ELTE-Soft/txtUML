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
			if (elem != null) {
				// TODO: Implement algorithm
				object.setPixelWidth(100);
				object.setPixelHeight(100);
			}
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
