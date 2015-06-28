package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class TxtUMLLayoutDescriptor {
	public String modelName;
	public DiagramExportationReport report;
	
	public TxtUMLLayoutDescriptor(String modelName, DiagramExportationReport report) {
		this.modelName = modelName;
		this.report = report;
	}
}
