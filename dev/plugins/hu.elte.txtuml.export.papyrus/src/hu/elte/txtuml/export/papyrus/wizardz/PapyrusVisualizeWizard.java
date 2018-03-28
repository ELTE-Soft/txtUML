package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Display;

import org.eclipse.emf.common.util.URI;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.core.resources.ResourcesPlugin;

import java.nio.file.Path;
import java.nio.file.Paths;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.TxtUMLPapyrusModelManager;
import hu.elte.txtuml.export.uml2.ExportMode;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.TxtUMLExporter;
import hu.elte.txtuml.layout.export.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.SaveUtils;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;
import hu.elte.txtuml.utils.eclipse.wizards.TxtUMLVisualizeWizard;
import hu.elte.txtuml.utils.eclipse.wizards.VisualizeTxtUMLPage;

/**
 * Wizard for visualization of txtUML models
 */
public class PapyrusVisualizeWizard extends TxtUMLVisualizeWizard {
	
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
	protected boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs, List<IType> txtUMLLayout) {
		for (Pair<String, String> model : layoutConfigs.keySet()) {
			//1
			String txtUMLModelName = model.getFirst();
			String txtUMLProjectName = model.getSecond();
			//2
			String generatedFolderName = PreferencesManager
					.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
			//3
			Map<String, String> layouts = new HashMap<String, String>();
			layoutConfigs.get(model).forEach(
					layout -> layouts.put(layout.getFullyQualifiedName(), layout.getJavaProject().getElementName()));

			List<String> fullyQualifiedNames = txtUMLLayout.stream().map(IType::getFullyQualifiedName)
					.collect(Collectors.toList());
			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProjectName, txtUMLModelName,
					fullyQualifiedNames);
			if (!saveSucceeded)
				return false;

			try {
				this.checkNoLayoutDescriptionsSelected();

				Job job = new Job("Diagram Visualization") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("Visualization", 100);

						TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName,
								txtUMLModelName, layouts);
					
						monitor.subTask("Exporting txtUML Model to UML2 model...");
						try {
							TxtUMLToUML2.exportModel(txtUMLProjectName, txtUMLModelName,
									txtUMLProjectName + "/" + generatedFolderName,
									ExportMode.ErrorHandlingNoActions, "gen");
							monitor.worked(10);
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML export Error", "Error occured during the UML2 exportation.",
									e);
							return Status.CANCEL_STATUS;
						}

						monitor.subTask("Generating txtUML layout description...");
						TxtUMLLayoutDescriptor layoutDescriptor = null;
						try {
							layoutDescriptor = exporter.exportTxtUMLLayout();

							List<String> warnings = new LinkedList<String>();
							for (DiagramExportationReport report : layoutDescriptor.getReports()) {
								warnings.addAll(report.getWarnings());
							}

							layoutDescriptor.mappingFolder = generatedFolderName;
							layoutDescriptor.projectName = txtUMLProjectName;

							if (warnings.size() != 0) {
								StringBuilder warningMessages = new StringBuilder(
										"Warnings:" + System.lineSeparator() + System.lineSeparator() + "- ");
								warningMessages.append(String
										.join(System.lineSeparator() + System.lineSeparator() + "- ", warnings));
								warningMessages.append(System.lineSeparator() + System.lineSeparator()
								+ "Do you want to continue?");

								if (!Dialogs.WarningConfirm("Warnings about layout description",
										warningMessages.toString())) {
									throw new InterruptedException();
								}
							}

							monitor.worked(5);
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML layout export Error",
									"Error occured during the diagram layout interpretation.", e);
							return Status.CANCEL_STATUS;
						}

						URI umlFileURI = URI.createFileURI(txtUMLProjectName + "/" + generatedFolderName + "/" + txtUMLModelName + ".uml");
						URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
						IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
								.getFile(new org.eclipse.core.runtime.Path(UmlFileResURI.toFileString()));

						PapyrusVisualizer pv = new PapyrusVisualizer(txtUMLProjectName, generatedFolderName + "/" + txtUMLModelName,
								UmlFile.getRawLocationURI().toString(), layoutDescriptor);
						pv.registerPayprusModelManager(TxtUMLPapyrusModelManager.class);

						Display.getDefault().syncExec(() -> {
							try {
								pv.run(SubMonitor.convert(monitor, 85));
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
			} catch (InterruptedException e) {
				Logger.sys.error(e.getMessage());
				return false;
			}
		}
		return true;
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
