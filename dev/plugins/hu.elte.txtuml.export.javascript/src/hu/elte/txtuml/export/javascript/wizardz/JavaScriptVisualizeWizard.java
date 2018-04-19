package hu.elte.txtuml.export.javascript.wizardz;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.javascript.Exporter;
import hu.elte.txtuml.layout.export.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.utils.Pair;
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
			final String txtUMLProjectName = model.getSecond();
			final String projectAbsLocation = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName).getLocation()
					.toFile().getAbsolutePath();
			
			Path genFolderPath = Paths.get(projectAbsLocation, getGeneratedFolderName());
			deleteFolderRecursively(genFolderPath);

			refreshGeneratedFolder(txtUMLProjectName);
		}
	}

}
