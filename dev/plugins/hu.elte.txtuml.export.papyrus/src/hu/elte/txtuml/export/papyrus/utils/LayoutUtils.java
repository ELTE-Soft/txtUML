package hu.elte.txtuml.export.papyrus.utils;

import java.net.URLClassLoader;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;

public class LayoutUtils {

	private static final ClassLoader layoutParent = DiagramExporter.class.getClassLoader();

	/**
	 * Creates a {@see DiagramExportationReport} from the specified diagram
	 * layout definition.
	 * 
	 * @param sourceProject
	 *            name of the project, where the txtUML diagram description is
	 *            to be find
	 * @param txtUMLLayout
	 *            fully qualified name of the txtUML layout description
	 * @return a report about the diagram exportation which contains the needed
	 *         statements, boxes and links
	 * @throws Exception
	 *             any exception that can be thrown during the exportation
	 */
	public static DiagramExportationReport exportTxtUMLLayout(String sourceProject, String txtUMLLayout)
			throws Exception {
		try (URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(sourceProject, layoutParent)) {
			Class<?> txtUMLLayoutClass = loader.loadClass(txtUMLLayout);
			@SuppressWarnings("unchecked")
			DiagramExporter exporter = DiagramExporter.create(sourceProject,
					(Class<? extends Diagram>) txtUMLLayoutClass);
			DiagramExportationReport report = exporter.export();
			return report;
		}
	}
}
