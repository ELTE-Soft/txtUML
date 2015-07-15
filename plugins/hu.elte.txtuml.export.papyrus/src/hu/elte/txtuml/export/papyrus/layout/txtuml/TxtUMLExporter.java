package hu.elte.txtuml.export.papyrus.layout.txtuml;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.ProjectUtils;
import hu.elte.txtuml.export.uml2.UML2;
import hu.elte.txtuml.export.utils.ClassLoaderProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.Diagram;

import java.net.URLClassLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * This class helps preparing the {@link PapyrusVisualizer} from a txtUML model
 * @author András Dobreff
 */
public class TxtUMLExporter {
	
	private String sourceProject;
	private String targetProject;
	private String umlFolder;
	private String txtUMLModelName;
	private ClassLoader parent;
	private String txtUMLLayout;
	
	/**
	 * The Constructor
	 * @param sourceProject - The txtUMLProject
	 * @param targetProject - The Papyrus project that will be generated
	 * @param umlFolder - The folder where the source UML file is to be find
	 * @param txtUMLModelName - The fully qualified name of the txtUML model
	 * @param txtUMLLayout - The fully qualified name of the txtUML Diagram
	 * @param parent - the parent ClassLoader
	 */
	public TxtUMLExporter(String sourceProject, String targetProject, String umlFolder,
			String txtUMLModelName, String txtUMLLayout, ClassLoader parent) {
		
		this.sourceProject = sourceProject;
		this.targetProject = targetProject;
		this.umlFolder = umlFolder;
		this.txtUMLModelName = txtUMLModelName;
		this.txtUMLLayout = txtUMLLayout;
		this.parent = parent;
	}
	
	/**
	 * Calls the txtUML to UML2 exportation method
	 * @throws Exception
	 */
	public void exportTxtUMLModelToUML2() throws Exception{
		try(URLClassLoader loader = ClassLoaderProvider
				.getClassLoaderForProject(this.sourceProject, parent)){
			Class<?> txtUMLModelClass = loader.loadClass(this.txtUMLModelName);
			String uri = URI.createPlatformResourceURI(
					this.sourceProject + "/" + this.umlFolder, false).toString();
			UML2.exportModel(txtUMLModelClass, uri);
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * Creates a description structure from a txtUML diagram definition
	 * @return
	 * @throws Exception
	 */
	public TxtUMLLayoutDescriptor exportTxtUMLLayout() throws Exception{
		try (URLClassLoader loader = ClassLoaderProvider
				.getClassLoaderForProject(this.sourceProject, parent)){
			Class<?> txtUMLLayoutClass = loader.loadClass(this.txtUMLLayout);  
	        @SuppressWarnings("unchecked")
			DiagramExporter exporter= DiagramExporter.create((Class<? extends Diagram>) txtUMLLayoutClass); 
	        DiagramExportationReport report = exporter.export();
	        	
	        if(!report.isSuccessful()){
	        	StringBuilder errorMessages = new StringBuilder("Errors occured during layout exportation:\n");
	        	for(Object error : report.getErrors()){
	        		errorMessages.append(error).append(errorMessages);
	        		errorMessages.append(System.lineSeparator());
	        	}
	        	errorMessages.append(System.lineSeparator()+"The exportation was't successfull.");
	        	throw new LayoutExportException(errorMessages.toString());
	        }
	        
	        return new TxtUMLLayoutDescriptor(this.txtUMLModelName, report);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Creates a Visualizer with the correct settings
	 * @param layoutDesriptor
	 * @return The Visualizer
	 */
	public PapyrusVisualizer createVisualizer(TxtUMLLayoutDescriptor layoutDesriptor) {
		URI umlFileURI = URI.createFileURI(sourceProject + "/" + this.umlFolder
				+ "/" + this.txtUMLModelName + ".uml");
		URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
		IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(UmlFileResURI.toFileString()));

		URI diFileURI = URI.createFileURI(targetProject + "/"
				+ this.txtUMLModelName + ".di");
		URI diFileResURI = CommonPlugin.resolve(diFileURI);
		IFile diFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(diFileResURI.toFileString()));

		IEditorInput input = new FileEditorInput(diFile);

		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findEditor(input);
		if (editor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().closeEditor(editor, false);
			
		}

		ProjectUtils.deleteProject(targetProject);
		PapyrusVisualizer pv = new PapyrusVisualizer(targetProject, this.txtUMLModelName,
				UmlFile.getRawLocationURI().toString(), layoutDesriptor);
		return pv;
	}
}
