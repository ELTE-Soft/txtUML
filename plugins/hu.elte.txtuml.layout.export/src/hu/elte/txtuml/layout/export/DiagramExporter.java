package hu.elte.txtuml.layout.export;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.layout.export.impl.DiagramExporterImpl;

/**
 * Public interface to export diagram layout descriptions from their Java
 * format.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface DiagramExporter {

	/**
	 * Creates a new <code>DiagramExporter</code> to export the given diagram.
	 * 
	 * @param javaProject
	 *            the name of the Eclipse project that contains the model
	 * @param diagClass
	 *            the diagram class to export
	 * @return the new exporter instance
	 */
	static DiagramExporter create(String sourceProjectName, Class<? extends Diagram> diagClass) {
		return new DiagramExporterImpl(sourceProjectName, diagClass);
	}

	/**
	 * Creates a new <code>DiagramExporter</code> to export the given diagram.
	 * 
	 * An empty report instance might be given which will be filled with the
	 * appropriate data and finally returned. It is useful if the actual type of
	 * this empty report instance is derived from
	 * <code>DiagramExportationReport</code> with the methods to log an error or
	 * warning overridden.
	 * 
	 * @see DiagramExportationReport
	 * 
	 * @param javaProject
	 *            the name of the Eclipse project that contains the model
	 * @param diagClass
	 *            the diagram class to export
	 * @param emptyReport
	 *            an empty report instance. If <code>null</code>, the method
	 *            behaves exactly as if the one-parameter create method was
	 *            called
	 * @return the new exporter instance
	 */
	static DiagramExporter create(String sourceProjectName, Class<? extends Diagram> diagClass,
			DiagramExportationReport emptyReport) {
		return new DiagramExporterImpl(sourceProjectName, diagClass, emptyReport);
	}

	/**
	 * Exports the diagram the instance was parameterized with.
	 * 
	 * @return a report about the exportation
	 */
	DiagramExportationReport export();

}
