package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.diagrams.common.arrange.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.diagrams.common.wizards.UML2VisualizeWizard;
import hu.elte.txtuml.export.diagrams.common.wizards.VisualizeTxtUMLPage;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;

/**
 * Wizard for visualization of txtUML models
 */
public class PapyrusVisualizeWizard extends UML2VisualizeWizard {

	@Override
	protected void visualize(TxtUMLLayoutDescriptor layoutDescriptor, String genFolder, IProgressMonitor monitor)
			throws Exception {
		PapyrusVisualizer pv = createVisualizer(layoutDescriptor, genFolder);
		pv.run(monitor);
	}

	/**
	 * Creates a Visualizer with the correct settings
	 * 
	 * @param layoutDescriptor
	 * @return The Visualizer
	 */
	public PapyrusVisualizer createVisualizer(TxtUMLLayoutDescriptor descriptor, String outputFolder) {
		String projectName = descriptor.projectName;
		String modelName = descriptor.modelName;
		URI umlFileURI = URI
				.createFileURI(projectName + File.separator + outputFolder + File.separator + modelName + ".uml");
		URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
		IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(UmlFileResURI.toFileString()));

		PapyrusVisualizer pv = new PapyrusVisualizer(projectName, outputFolder + File.separator + modelName,
				UmlFile.getRawLocationURI().toString(), descriptor);
		return pv;
	}
	
	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage(false, true, StateMachineDiagram.class, ClassDiagram.class, CompositeDiagram.class);
		addPage(selectTxtUmlPage);
	}
	
	@Override
	public String getWindowTitle() {
		return "Visualize Papyrus diagrams from txtUML models";
	}
}
