package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	 * True if the user wants to generate StateMachines automatically
	 */
	public boolean generateSMDs;
	
	/**
	 * The {@link DiagramExportationReport} that holds all information about the layout
	 */
	private Map<String, DiagramExportationReport> reports = new HashMap<>();
	
	/**
	 * The Constructor
	 * @param modelName - The canonical name of the model
	 * @param reports - The {@link DiagramExportationReport}s that hold all information about the layout
	 */
	public TxtUMLLayoutDescriptor(String modelName, List<DiagramExportationReport> reports) {
		this.modelName = modelName;
		for(DiagramExportationReport report : reports){
			this.reports.put("", report);
		}
		
	}
	
	/**
	 * Returns the reports
	 * @return list of {@link DiagramExportationReport}
	 */
	public List<DiagramExportationReport> getReports(){
		return new LinkedList<DiagramExportationReport>(reports.values());
	}
}
