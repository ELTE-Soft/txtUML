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
		this.pixelDimensionProvider = new StateMachineDiagramPixelDimensionProvider();
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
		int leftMost = this.elementbounds.values().stream().map(rect -> rect.getTopLeft().x()).mapToInt(Integer::intValue).min().getAsInt();
		int rightMost = this.elementbounds.values().stream().map(node -> node.getBottomRight().x())
				.mapToInt(Integer::intValue).max().getAsInt();
		int TopMost = this.elementbounds.values().stream().map(node -> node.getTopLeft().y()).mapToInt(Integer::intValue)
				.min().getAsInt();
		int BottomMost = this.elementbounds.values().stream().map(node -> node.getBottomRight().y())
				.mapToInt(Integer::intValue).max().getAsInt();
		int width = rightMost - leftMost + 2 * StateMachineDiagramPixelDimensionProvider.DEFAULT_ELEMENT_BORDER;
		int height = BottomMost - TopMost + 2 * StateMachineDiagramPixelDimensionProvider.DEFAULT_ELEMENT_BORDER
				+ StateMachineDiagramPixelDimensionProvider.STATE_HEADER_HEIGHT;
		return new Rectangle(0, 0, width, height);
	}
}
