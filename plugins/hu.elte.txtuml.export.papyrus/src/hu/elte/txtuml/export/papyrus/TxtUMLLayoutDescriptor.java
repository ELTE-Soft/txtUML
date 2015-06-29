package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.layout.export.DiagramExportationReport;

/**
 * The instance holds infomration about the layout of a specific model
 * @author András Dobreff
 */
public class TxtUMLLayoutDescriptor {
	
	/**
	 * The canonical name of the txtUML model 
	 */
	public String modelName;
	
	/**
	 * The {@link DiagramExportationReport} that holds all information about the layout
	 */
	public DiagramExportationReport report;
	
	/**
	 * The Constructor
	 * @param modelName - The canonical name of the model
	 * @param report - The {@link DiagramExportationReport} that holds all information about the layout
	 */
	public TxtUMLLayoutDescriptor(String modelName, DiagramExportationReport report) {
		this.modelName = modelName;
		this.report = report;
	}
}
