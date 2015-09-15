package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.util.LinkedList;
import java.util.List;

import hu.elte.txtuml.export.ExportUtils;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

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
 * @author Andr�s Dobreff
 */
public class TxtUMLExporter {
	
	private String projectName;
	private String outputFolder;
	private String txtUMLModelName;
	private List<String> txtUMLLayout;
	
	/**
	 * The Constructor
	 * @param projectName - The txtUMLProject
	 * @param outputFolder - The folder in the project where UML data and diagrams are put
	 * @param txtUMLModelName - The fully qualified name of the txtUML model
	 * @param txtUMLLayout - The fully qualified name of the txtUML Diagram
	 * @param parent - the parent ClassLoader
	 */
	public TxtUMLExporter(String projectName, String outputFolder,
			String txtUMLModelName, List<String> txtUMLLayout) {
		
		this.projectName = projectName;
		this.outputFolder = outputFolder;
		this.txtUMLModelName = txtUMLModelName;
		this.txtUMLLayout = txtUMLLayout;
	}
	
	/**
	 * Creates a description structure from a txtUML diagram definition
	 * @return
	 * @throws Exception
	 */
	public TxtUMLLayoutDescriptor exportTxtUMLLayout() throws Exception{
		List<DiagramExportationReport> reports = new LinkedList<DiagramExportationReport>();
		
		for(String layout : txtUMLLayout){
			try {
				DiagramExportationReport report = ExportUtils.exportTxtUMLLayout(projectName, layout);
		        if(!report.isSuccessful()){
		        	StringBuilder errorMessages = new StringBuilder("Errors occured during layout exportation:\n");
		        	for(Object error : report.getErrors()){
		        		errorMessages.append(error).append(errorMessages);
		        		errorMessages.append(System.lineSeparator());
		        	}
		        	errorMessages.append(System.lineSeparator()+"The exportation was't successfull.");
		        	throw new LayoutExportException(errorMessages.toString());
		        }
		        
		        reports.add(report);
			} catch (Exception e) {
				throw e;
			}
		}
		return new TxtUMLLayoutDescriptor(txtUMLModelName, reports); 
	}

	/**
	 * Creates a Visualizer with the correct settings
	 * @param layoutDescriptor
	 * @return The Visualizer
	 */
	public PapyrusVisualizer createVisualizer(TxtUMLLayoutDescriptor layoutDescriptor) {
		URI umlFileURI = URI.createFileURI(projectName + "/" + this.outputFolder
				+ "/" + this.txtUMLModelName + ".uml");
		URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
		IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(UmlFileResURI.toFileString()));

		URI diFileURI = URI.createFileURI(projectName + "/" + this.outputFolder
				+ "/" + this.txtUMLModelName + ".di");
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

		PapyrusVisualizer pv = new PapyrusVisualizer(projectName,
				this.outputFolder + "/" + this.txtUMLModelName,
				UmlFile.getRawLocationURI().toString(), layoutDescriptor);
		return pv;
	}
}
