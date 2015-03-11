package hu.elte.txtuml.layout.export;

import hu.elte.txtuml.layout.export.impl.DiagramExporterImpl;
import hu.elte.txtuml.layout.lang.Diagram;

public interface DiagramExporter {

	/**
	 * Creates a new DiagramExporter to export the given diagram.
	 * 
	 * @param diagClass
	 *            The diagram class to export.
	 * @return The new exporter instance.
	 */
	static DiagramExporter create(Class<? extends Diagram> diagClass) {

		return new DiagramExporterImpl(diagClass);
	}

	/**
	 * Creates a new DiagramExporter to export the given diagram.
	 * 
	 * An empty report instance might be given which will be filled with the
	 * appropriate data and finally returned. It is useful if the actual type of
	 * this empty report instance is derived from DiagramExportationReport with
	 * the methods to log an error or warning overridden.
	 * 
	 * @see DiagramExportationReport
	 * 
	 * @param diagClass
	 *            The diagram class to export.
	 * @param emptyReport
	 *            An empty report instance. If null, the method behaves exactly
	 *            as if the one-parameter create method was called.
	 * @return The new exporter instance.
	 */
	static DiagramExporter create(Class<? extends Diagram> diagClass,
			DiagramExportationReport emptyReport) {
		return new DiagramExporterImpl(diagClass, emptyReport);
	}

	/**
	 * Exports the Diagram class the instance was parameterized with.
	 * 
	 * @return A report about the exportation.
	 */
	DiagramExportationReport export();

}
