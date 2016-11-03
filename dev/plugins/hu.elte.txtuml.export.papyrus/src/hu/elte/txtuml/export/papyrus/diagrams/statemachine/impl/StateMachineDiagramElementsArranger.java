package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.papyrus.arrange.AbstractDiagramElementsArranger;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsArranger extends AbstractDiagramElementsArranger {

	public StateMachineDiagramElementsArranger(DiagramExportationReport report,
			StateMachineDiagramElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		this.pixelDimensionProvider = new StateMachineDiagramPixelDimensionProvider(mapper);
	}

	@Override
	public Rectangle getBoundsForElement(Element element) {
		if (element instanceof StateMachine) {
			return calculateBoundsOfStatemachine();
		} else {
			return super.getBoundsForElement(element);
		}
	}

	private Rectangle calculateBoundsOfStatemachine() {
		int leftMost = this.report.getNodes().stream().map(node -> node.getTopLeft().getX()).mapToInt(Integer::intValue)
				.min().getAsInt();
		int rightMost = this.report.getNodes().stream().map(node -> node.getBottomRight().getX())
				.mapToInt(Integer::intValue).max().getAsInt();
		int TopMost = this.report.getNodes().stream().map(node -> node.getTopLeft().getY()).mapToInt(Integer::intValue)
				.max().getAsInt();
		int BottomMost = this.report.getNodes().stream().map(node -> node.getBottomRight().getY())
				.mapToInt(Integer::intValue).min().getAsInt();
		int width = rightMost - leftMost;
		int height = TopMost - BottomMost + StateMachineDiagramPixelDimensionProvider.STATE_HEADER_HEIGHT;
		return new Rectangle(0, 0, width, height);
	}
}
