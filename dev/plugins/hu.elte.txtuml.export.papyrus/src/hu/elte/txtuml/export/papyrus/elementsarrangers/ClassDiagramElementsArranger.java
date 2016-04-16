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
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutVisualizerManager;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

public class ClassDiagramElementsArranger implements IDiagramElementsArranger {

	private DiagramExportationReport report;
	private Map<Element, Rectangle> elementbounds;
	private Map<Relationship, List<Point>> connectionbounds;
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

		Map<RectangleObject, Element> elementsObjectsMapping = createMapping(objects);

		setPixelsizes(elementsObjectsMapping);

		LayoutVisualizerManager vm = new LayoutVisualizerManager(objects, links, statements);
		vm.addProgressMonitor(monitor);
		vm.arrange();

		objects = vm.getObjects();
		links = vm.getAssociations();

		this.elementbounds = createElementBounds(elementsObjectsMapping, objects);

		monitor.worked(1);
	}

	private Map<Element, Rectangle> createElementBounds(Map<RectangleObject, Element> map,
			Set<RectangleObject> objects) {
		Map<Element, Rectangle> result = new HashMap<>();
		Map<String, RectangleObject> NameROMap = objects.stream()
				.collect(Collectors.toMap((rect) -> rect.getName(), (rect) -> rect));
		map.forEach((o, e) -> {
			RectangleObject object = NameROMap.get(o.getName());
			result.put(e, new Rectangle(object.getTopLeft().getX(), object.getTopLeft().getY(), object.getPixelWidth(),
					object.getPixelHeight()));
		});
		return result;
	}

	private Map<RectangleObject, Element> createMapping(Set<RectangleObject> objects) {
		Map<RectangleObject, Element> map = new HashMap<>();
		objects.forEach((object) -> {
			Optional<Element> elem = this.elementsMapper.findElement(object.getName());
			elem.ifPresent(e -> map.put(object, e));
		});
		return map;
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

}
