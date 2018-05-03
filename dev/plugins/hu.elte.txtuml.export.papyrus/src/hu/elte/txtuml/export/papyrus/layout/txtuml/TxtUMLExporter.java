package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.utils.LayoutUtils;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.Pair;

/**
 * This class helps preparing the {@link PapyrusVisualizer} from a txtUML model
 */
public class TxtUMLExporter {

	private String projectName;
	private String outputFolder;
	private String txtUMLModelName;
	private Map<String, String> txtUMLLayout; // <layout name, project name>

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
	 *            - The fully qualified names of the txtUML Diagrams and the
	 *            project names
	 * @param parent
	 *            - the parent ClassLoader
	 */
	public TxtUMLExporter(String projectName, String outputFolder, String txtUMLModelName,
			Map<String, String> txtUMLLayout) {

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
		List<Pair<String, DiagramExportationReport>> reports = new ArrayList<>();

		for (Map.Entry<String, String> layout : txtUMLLayout.entrySet()) {
			try {
				DiagramExportationReport report = LayoutUtils.exportTxtUMLLayout(projectName, layout.getKey(),
						layout.getValue());
				if (!report.isSuccessful()) {
					StringBuilder errorMessages = new StringBuilder(
							"Errors occured during layout exportation:" + System.lineSeparator());
					for (Object error : report.getErrors()) {
						errorMessages.append("- " + error + System.lineSeparator());
					}
					errorMessages.append("The exportation was't successfull.");
					throw new LayoutExportException(errorMessages.toString());
				}

				reports.add(new Pair<>(layout.getKey(), report));
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
		IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new org.eclipse.core.runtime.Path(UmlFileResURI.toFileString()));

		PapyrusVisualizer pv = new PapyrusVisualizer(projectName, this.outputFolder + "/" + this.txtUMLModelName,
				UmlFile.getRawLocationURI().toString(), layoutDescriptor);
		return pv;
	}

	/**
	 * Closes the editor and deletes the files that have the same name as the
	 * generated ones will have
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
	public void cleanBeforeVisualization() throws CoreException, IOException {

		// TODO: Has to be separated so that Papyrus and JointJS resources can
		// be deleted separately
		String projectAbsLocation = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getLocation()
				.toFile().getAbsolutePath();

		Path modelFolderPath = Paths.get(projectAbsLocation, this.outputFolder, "js", this.txtUMLModelName);
		Path umlFilePath = Paths.get(projectAbsLocation, this.outputFolder, this.txtUMLModelName + ".uml");
		Path profileFilePath = Paths.get(projectAbsLocation, this.outputFolder, this.txtUMLModelName + ".profile.uml");
		Path mappingFilePath = Paths.get(projectAbsLocation, this.outputFolder, this.txtUMLModelName + ".mapping");
		Path diFilePath = Paths.get(projectAbsLocation, this.outputFolder, this.txtUMLModelName + ".notation");
		Path notationFilePath = Paths.get(projectAbsLocation, this.outputFolder, this.txtUMLModelName + ".di");

		Path htmlFilePath = Paths.get(modelFolderPath.toString(), "visualize.html");
		URI htmlFileURI = URI.createFileURI(htmlFilePath.toString());

		IFile htmlFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new org.eclipse.core.runtime.Path(CommonPlugin.resolve(htmlFileURI).toFileString()));

		IEditorInput input = new FileEditorInput(htmlFile);

		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(input);
		if (editor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
		}

		deleteFolderRecursively(modelFolderPath);

		profileFilePath.toFile().delete();
		mappingFilePath.toFile().delete();
		umlFilePath.toFile().delete();
		diFilePath.toFile().delete();
		notationFilePath.toFile().delete();
	}

	private void deleteFolderRecursively(Path modelFolderPath) throws IOException {
		if (modelFolderPath.toFile().exists()) {
			Files.walk(modelFolderPath).map(Path::toFile).sorted((o1, o2) -> -o1.compareTo(o2)).forEach(File::delete);
		}
	}
}
