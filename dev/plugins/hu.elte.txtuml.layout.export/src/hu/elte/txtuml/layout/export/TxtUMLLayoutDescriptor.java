package hu.elte.txtuml.layout.export;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hu.elte.txtuml.utils.Pair;

/**
 * The instance holds information about the layout of a specific model
 */
public class TxtUMLLayoutDescriptor {
	
	/**
	 * The canonical name of the txtUML model 
	 */
	public String modelName;
	
	/**
	 * The name of the Folder where the txtUML model and
	 * the EMF-UML2 mapping description is saved.
	 */
	public String mappingFolder;
	
	/**
	 * The name of the project where the txtUML model is
	 */
	public String projectName;
	
	/**
	 * The {@link DiagramExportationReport} that holds all information about the layout
	 */
	private Map<String, DiagramExportationReport> reports = new HashMap<>();


	
	/**
	 * The Constructor
	 * @param modelName - The canonical name of the model
	 * @param reports - The {@link DiagramExportationReport}s that hold all information about the layout
	 */
	public TxtUMLLayoutDescriptor(String modelName, List<Pair<String, DiagramExportationReport>> reports) {
		this.modelName = modelName;
		for(Pair<String, DiagramExportationReport> pair : reports){
			this.reports.put(pair.getFirst(), pair.getSecond());
		}
		
	}
	
	/**
	 * Returns all reports
	 * @return list of {@link DiagramExportationReport}
	 */
	public Collection<DiagramExportationReport> getReports(){
		return reports.values();
	}
	
	public List<Pair<String, DiagramExportationReport>> getReportsWithDiagramNames(){
		List<Pair<String, DiagramExportationReport>> result =  new LinkedList<>();
		reports.entrySet().forEach( entry -> {
			result.add(new Pair<>(entry.getKey(), entry.getValue()));
		});
		return result;
	}
	
	
	/**
	 * Returns the report that is specified under the given root element
	 * @param root - The name of the root element where the report is defined
	 * @param diagramName  - The name of the diagram
	 * @return The {@link DiagramExportationReport} that is specified under the given root element
	 */
	public DiagramExportationReport getReport(String diagramName){
		return reports.get(diagramName);
	}
}
