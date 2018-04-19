package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Display;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.TxtUMLPapyrusModelManager;
import hu.elte.txtuml.layout.export.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;
import hu.elte.txtuml.utils.eclipse.wizards.UML2VisualizeWizard;
import hu.elte.txtuml.utils.eclipse.wizards.VisualizeTxtUMLPage;

/**
 * Wizard for visualization of txtUML models
 */
public class PapyrusVisualizeWizard extends UML2VisualizeWizard {
	
	public PapyrusVisualizeWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public String getWindowTitle() {
		return "Create Papyrus model from txtUML model";
	}
	
	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage(true, StateMachineDiagram.class, ClassDiagram.class, CompositeDiagram.class);
		addPage(selectTxtUmlPage);
	}
	
	@Override
	protected void exportDiagram(TxtUMLLayoutDescriptor layoutDescriptor, IProgressMonitor monitor) throws Exception{
			Job job = new Job("Diagram Visualization") {

				@Override
				protected IStatus run(IProgressMonitor innerMonitor) {

					URI umlFileURI = URI.createFileURI(layoutDescriptor.projectName + "/" + getGeneratedFolderName() + "/" + layoutDescriptor.modelName + ".uml");
					URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
					IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFile(new org.eclipse.core.runtime.Path(UmlFileResURI.toFileString()));

					PapyrusVisualizer pv = new PapyrusVisualizer(layoutDescriptor.projectName , getGeneratedFolderName() + "/" + layoutDescriptor.modelName,
							UmlFile.getRawLocationURI().toString(), layoutDescriptor);
					pv.registerPayprusModelManager(TxtUMLPapyrusModelManager.class);

					Display.getDefault().syncExec(() -> {
						try {
							pv.run(SubMonitor.convert(monitor, 70));
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML visualization Error",
									"Error occured during the visualization process.", e);
						}
					});
					monitor.done();
					innerMonitor.done();
					return Status.OK_STATUS;
				}

			};
			job.setUser(true);
			job.schedule();
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
			
			profileFilePath.toFile().delete();
			mappingFilePath.toFile().delete();
			umlFilePath.toFile().delete();
			diFilePath.toFile().delete();
			notationFilePath.toFile().delete();
		}
	}
}
