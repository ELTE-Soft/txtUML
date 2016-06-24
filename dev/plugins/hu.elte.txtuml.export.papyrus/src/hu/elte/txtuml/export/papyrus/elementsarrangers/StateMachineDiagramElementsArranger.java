package hu.elte.txtuml.export.papyrus.elementsarrangers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Transition;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.TxtUmlPixelDimensionProvider;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

public class StateMachineDiagramElementsArranger implements IDiagramElementsArranger {

	private DiagramExportationReport report;
	private TxtUMLElementsMapper elementsMapper;
	private Map<Element, Rectangle> elementbounds = new HashMap<>();
	private Map<Transition, List<Point>> connectionRoutes = new HashMap<>();
	private TxtUmlPixelDimensionProvider pixelDimensionProvider;
	private Map<Transition, String> connectionSourceAnchors;
	private Map<Transition, String> connectionTargetAnchors;

	public StateMachineDiagramElementsArranger(DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		// TODO: Separate pixelproviders for different diagram types
		this.pixelDimensionProvider = new TxtUmlPixelDimensionProvider(mapper, this.report);
	}

	@Override
	public void arrange(IProgressMonitor monitor) throws ArrangeException {
		// TODO Auto-generated method stub
		this.connectionSourceAnchors = createSourceAnchors(new HashSet<LineAssociation>());
		this.connectionTargetAnchors = createTargetAnchors(new HashSet<LineAssociation>());
	}

	private Map<Transition, String> createTargetAnchors(Set<LineAssociation> links) {
		Map<Transition, String> result = new HashMap<>();
		links.forEach(l -> {
			Transition connection = (Transition) this.elementsMapper.findConnection(l.getId(), this.report);
			Rectangle targetNode = this.elementbounds.get(this.elementsMapper.findNode(l.getTo(), this.report));
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
			Transition connection = (Transition) this.elementsMapper.findConnection(l.getId(), this.report);
			Rectangle sourceNode = this.elementbounds.get(this.elementsMapper.findNode(l.getFrom(), this.report));
			Point startPoint = this.connectionRoutes.get(connection).get(0);
			result.put(connection, "(" + (startPoint.x - sourceNode.x) / (float) sourceNode.width + ","
					+ (startPoint.y - sourceNode.y) / (float) sourceNode.height + ")");
		});
		return result;
	}

	@Override
	public Rectangle getBoundsForElement(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Point> getRouteForConnection(Transition connection) {
		return this.connectionRoutes.get(connection);
	}

	public String getSourceAnchorForConnection(Transition transition) {
		return this.connectionSourceAnchors.get(transition);
	}

	public String getTargetAnchorForConnection(Transition transition) {
		return this.connectionTargetAnchors.get(transition);
	}

}
