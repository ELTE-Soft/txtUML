package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.utils.LayoutUtils;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2.ExportMode;
import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.eclipse.NotFoundException;

/**
 * This class helps preparing the {@link PapyrusVisualizer} from a txtUML model
 */
public class TxtUMLExporter {

	private String projectName;
	private String outputFolder;
	private String txtUMLModelName;
	private List<String> txtUMLLayout;

	/**
	 * The Constructor
	 * 
	 * @param projectName
	 *            - The txtUMLProject
	 * @param outputFolder
	 *            - The folder in the project where UML data and diagrams are
	 *            put
	 * @param txtUMLModelName
	 *            - The fully qualified name of the txtUML model
	 * @param txtUMLLayout
	 *            - The fully qualified name of the txtUML Diagram
	 * @param parent
	 *            - the parent ClassLoader
	 */
	public TxtUMLExporter(String projectName, String outputFolder, String txtUMLModelName, List<String> txtUMLLayout) {

		this.projectName = projectName;
		this.outputFolder = outputFolder;
		this.txtUMLModelName = txtUMLModelName;
		this.txtUMLLayout = txtUMLLayout;
	}

	/**
	 * Creates a description structure from a txtUML diagram definition
	 * 
	 * @return
	 * @throws Exception
	 */
	public TxtUMLLayoutDescriptor exportTxtUMLLayout() throws Exception {
		List<DiagramExportationReport> reports = new ArrayList<>();

		for (String layout : txtUMLLayout) {
			try {
				DiagramExportationReport report = LayoutUtils.exportTxtUMLLayout(projectName, layout);
				if (!report.isSuccessful()) {
					StringBuilder errorMessages = new StringBuilder(
							"Errors occured during layout exportation:" + System.lineSeparator());
					for (Object error : report.getErrors()) {
						errorMessages.append("- " + error + System.lineSeparator());
					}
					errorMessages.append("The exportation was't successfull.");
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
	 * 
	 * @param layoutDescriptor
	 * @return The Visualizer
	 */
	public PapyrusVisualizer createVisualizer(TxtUMLLayoutDescriptor layoutDescriptor) {
		URI umlFileURI = URI.createFileURI(projectName + "/" + this.outputFolder + "/" + this.txtUMLModelName + ".uml");
		URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
		IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(UmlFileResURI.toFileString()));

		PapyrusVisualizer pv = new PapyrusVisualizer(projectName, this.outputFolder + "/" + this.txtUMLModelName,
				UmlFile.getRawLocationURI().toString(), layoutDescriptor);
		return pv;
	}

	/**
	 * Exports the txtUML model to an EMF model to the same folder where the
	 * papyrus Model will be
	 * 
	 * @throws JavaModelException
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public void exportModel() throws JavaModelException, NotFoundException, IOException {
		TxtUMLToUML2.exportModel(projectName, txtUMLModelName, projectName + "/" + outputFolder,
				ExportMode.ExportDefinitions);
	}

	/**
	 * Closes the editor and deletes the files that are have the same name as
	 * the generated ones will have
	 * 
	 * @throws CoreException
	 *             if this method fails. Reasons include:
	 *             <ul>
	 *             <li>This resource could not be deleted for some reason.</li>
	 *             <li>This resource or one of its descendents is out of sync
	 *             with the local file system and force is false.</li>
	 *             <li>Resource changes are disallowed during certain types of
	 *             resource change event notification. See IResourceChangeEvent
	 *             for more details.</li>
	 *             </ul>
	 */
	public void cleanBeforeVisualization() throws CoreException, InvocationTargetException, InterruptedException {
		String location = projectName + "/" + this.outputFolder + "/" + this.txtUMLModelName;
		URI diFileURI = URI.createFileURI(location + ".di");
		URI umlFileURI = URI.createFileURI(location + ".uml");
		URI profileFileURI = URI.createFileURI(location + ".profile.uml");
		URI mappingFileURI = URI.createFileURI(location + ".mapping");
		URI notationFileURI = URI.createFileURI(location + ".notation");

		IFile diFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(CommonPlugin.resolve(diFileURI).toFileString()));
		IFile umlFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(CommonPlugin.resolve(umlFileURI).toFileString()));
		IFile profileFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(CommonPlugin.resolve(profileFileURI).toFileString()));
		IFile mappingFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(CommonPlugin.resolve(mappingFileURI).toFileString()));
		IFile notationFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(CommonPlugin.resolve(notationFileURI).toFileString()));

		IEditorInput input = new FileEditorInput(diFile);

		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(input);
		if (editor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);

		}

		diFile.delete(true, new NullProgressMonitor());
		umlFile.delete(true, new NullProgressMonitor());
		profileFile.delete(true, new NullProgressMonitor());
		mappingFile.delete(true, new NullProgressMonitor());
		notationFile.delete(true, new NullProgressMonitor());
	}
}
