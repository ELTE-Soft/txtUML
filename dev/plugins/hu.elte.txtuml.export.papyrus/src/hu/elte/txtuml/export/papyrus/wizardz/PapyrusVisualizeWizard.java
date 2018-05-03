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
import hu.elte.txtuml.utils.eclipse.EditorUtils;
import hu.elte.txtuml.wizards.UML2VisualizeWizard;
import hu.elte.txtuml.wizards.VisualizeTxtUMLPage;

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
		selectTxtUmlPage = new VisualizeTxtUMLPage(false, true, StateMachineDiagram.class, ClassDiagram.class,
				CompositeDiagram.class);
		addPage(selectTxtUmlPage);
	}
	
	@Override
	protected void exportDiagram(TxtUMLLayoutDescriptor layoutDescriptor, IProgressMonitor monitor) throws Exception{	
		monitor.beginTask("Visualizing "+ layoutDescriptor.projectName + "/" + layoutDescriptor.modelName +".",100);

		Job job = new Job("Diagram Visualization") {

			@Override
			protected IStatus run(IProgressMonitor innerMonitor) {
				innerMonitor.beginTask("visualizing "+ layoutDescriptor.projectName + "/" + layoutDescriptor.modelName, 100);

				URI umlFileURI = URI.createFileURI(layoutDescriptor.projectName + "/" + getGeneratedFolderName() + "/" + layoutDescriptor.modelName + ".uml");
				URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
				IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new org.eclipse.core.runtime.Path(UmlFileResURI.toFileString()));

				PapyrusVisualizer pv = new PapyrusVisualizer(layoutDescriptor.projectName , getGeneratedFolderName() + "/" + layoutDescriptor.modelName,
						UmlFile.getRawLocationURI().toString(), layoutDescriptor);
				pv.registerPayprusModelManager(TxtUMLPapyrusModelManager.class);
				
				innerMonitor.worked(5);

				Display.getDefault().syncExec(() -> {
					try {
						pv.run(SubMonitor.convert(innerMonitor, 95));
						innerMonitor.done();
					} catch (Exception e) {
						Dialogs.errorMsgb("txtUML visualization Error",
								"Error occured during the visualization process.", e);
					}
				});
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
		monitor.done();
	}
	
	@Override
	protected void cleanBeforeVisualization(Set<Pair<String, String>> layouts) throws CoreException, IOException {
		for (Pair<String, String> model : layouts) {
			final String txtUMLModelName = model.getFirst();
			final String txtUMLProjectName = model.getSecond();
			
			final String projectAbsLocation = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName).getLocation()
					.toFile().getAbsolutePath();
	
			Path notationFilePath = Paths.get(projectAbsLocation, getGeneratedFolderName(), txtUMLModelName + ".di");
			Path mappingFilePath = Paths.get(projectAbsLocation, getGeneratedFolderName(), txtUMLModelName + ".mapping");
			Path diFilePath = Paths.get(projectAbsLocation, getGeneratedFolderName(), txtUMLModelName + ".notation");
			Path umlFilePath = Paths.get(projectAbsLocation, getGeneratedFolderName(), txtUMLModelName + ".uml");
			Path profileFilePath = Paths.get(projectAbsLocation, getGeneratedFolderName(), txtUMLModelName + ".profile.uml");
			
			EditorUtils.closeEditorByPath(notationFilePath);
			
			profileFilePath.toFile().delete();
			mappingFilePath.toFile().delete();
			umlFilePath.toFile().delete();
			diFilePath.toFile().delete();
			notationFilePath.toFile().delete();
			
			refreshGeneratedFolder(txtUMLProjectName);
		}
	}
}
