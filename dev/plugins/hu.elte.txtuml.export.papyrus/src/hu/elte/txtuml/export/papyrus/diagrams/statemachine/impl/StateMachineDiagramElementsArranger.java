package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import hu.elte.txtuml.export.papyrus.arrange.AbstractDiagramElementsArranger;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsArranger extends AbstractDiagramElementsArranger {

	public StateMachineDiagramElementsArranger(DiagramExportationReport report,
			StateMachineDiagramElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		this.pixelDimensionProvider = new StateMachineDiagramPixelDimensionProvider(mapper);
	}

}
