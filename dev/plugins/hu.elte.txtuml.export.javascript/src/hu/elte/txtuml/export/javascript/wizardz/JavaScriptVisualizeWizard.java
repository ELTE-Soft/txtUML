package hu.elte.txtuml.export.javascript.wizardz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.javascript.Exporter;
import hu.elte.txtuml.layout.export.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;
import hu.elte.txtuml.utils.eclipse.wizards.UML2VisualizeWizard;
import hu.elte.txtuml.utils.eclipse.wizards.VisualizeTxtUMLPage;

/**
 * Wizard for visualization of txtUML models
 */
public class JavaScriptVisualizeWizard extends UML2VisualizeWizard {

	public JavaScriptVisualizeWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Create Javascript Diagrams from txtUML Model";
	}

	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage(true, StateMachineDiagram.class, ClassDiagram.class, CompositeDiagram.class);
		addPage(selectTxtUmlPage);
	}

	@Override
	protected void exportDiagram(TxtUMLLayoutDescriptor layoutDescriptor, IProgressMonitor monitor) throws Exception{
		Exporter ex = new Exporter(layoutDescriptor, layoutDescriptor.modelName);
		ex.export();
		monitor.done();
	}

	@Override
	protected void cleanBeforeVisualization(Set<Pair<String, String>> layouts) throws CoreException, IOException {
		for (Pair<String, String> model : layouts) {
			String txtUMLModelName = model.getFirst();
			String txtUMLProjectName = model.getSecond();
			
			String generatedFolderName = PreferencesManager
					.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
			
			String projectAbsLocation = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName).getLocation()
					.toFile().getAbsolutePath();
	
			Path notationFilePath = Paths.get(projectAbsLocation, generatedFolderName, txtUMLModelName + ".di");
			Path mappingFilePath = Paths.get(projectAbsLocation, generatedFolderName, txtUMLModelName + ".mapping");
			Path diFilePath = Paths.get(projectAbsLocation, generatedFolderName, txtUMLModelName + ".notation");
			Path umlFilePath = Paths.get(projectAbsLocation, generatedFolderName, txtUMLModelName + ".uml");
			Path profileFilePath = Paths.get(projectAbsLocation, generatedFolderName, txtUMLModelName + ".profile.uml");
			
			Path modelFolderPath = Paths.get(projectAbsLocation, generatedFolderName, "js", txtUMLModelName);
			
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
	}
	
	private void deleteFolderRecursively(Path modelFolderPath) throws IOException {
		if (modelFolderPath.toFile().exists()) {
			Files.walk(modelFolderPath).map(Path::toFile).sorted((o1, o2) -> -o1.compareTo(o2)).forEach(File::delete);
		}
	}

}
