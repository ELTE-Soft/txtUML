package hu.elte.txtuml.export;

import hu.elte.txtuml.eclipseutils.ClassLoaderProvider;
import hu.elte.txtuml.export.uml2.UML2;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.Diagram;

import java.net.URLClassLoader;

import org.eclipse.emf.common.util.URI;

/**
 * Utilities Class to convert txtUML representations
 *
 * @author András Dobreff
 */
public class ExportUtils {
	
	private static final ClassLoader exportParent = UML2.class.getClassLoader();
	private static final ClassLoader layoutparent = DiagramExporter.class.getClassLoader();
	
	/**
	 * Exports the txtUML model to a org.eclipse.uml2.uml.Model representation
	 * @param sourceProject - Name of the source project, where the txtUML model can be find
	 * @param modelName - Fully qualified name of the txtUML model
	 * @param folder - folder where the result model should be saved
	 * @throws Exception - any exception that can be thrown during the exportation
	 */
	public static void exportTxtUMLModelToUML2(String sourceProject, String modelName,
										String folder) throws Exception{
		
		try(URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(sourceProject, exportParent)){
			Class<?> txtUMLModelClass = loader.loadClass(modelName);
			String uri = URI.createPlatformResourceURI(folder, false).toString();
			UML2.exportModel(txtUMLModelClass, uri);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * Creates a {@see DiagramExportationReport} from the specified diagram layout definition
	 * 
	 * @param sourceProject - Name of the project, where the txtUML diagram description is to be find
	 * @param txtUMLLayout - Fully qualified name of the txtUML layout description  
	 * @return A report about the diagram exportation which contains the needed statements, boxes and links
	 * @throws Exception - any exception that can be thrown during the exportation
	 */
	public static DiagramExportationReport exportTxtUMLLayout(String sourceProject,
			String txtUMLLayout) throws Exception{
		try (URLClassLoader loader = ClassLoaderProvider
				.getClassLoaderForProject(sourceProject, layoutparent)){
			Class<?> txtUMLLayoutClass = loader.loadClass(txtUMLLayout);  
	        @SuppressWarnings("unchecked")
			DiagramExporter exporter= DiagramExporter.create((Class<? extends Diagram>) txtUMLLayoutClass); 
	        DiagramExportationReport report = exporter.export();
	        return report;
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	
}