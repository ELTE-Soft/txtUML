package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import hu.elte.txtuml.export.diagrams.common.layout.IDiagramElementsMapper;
import hu.elte.txtuml.export.papyrus.arrange.AbstractDiagramElementsArranger;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class ClassDiagramElementsArranger extends AbstractDiagramElementsArranger {

	public ClassDiagramElementsArranger(DiagramExportationReport report, IDiagramElementsMapper mapper) {
		this.report = report;
		this.elementsMapper = mapper;
		this.pixelDimensionProvider = new ClassDiagramPixelDimensionProvider(mapper);
	}

}
