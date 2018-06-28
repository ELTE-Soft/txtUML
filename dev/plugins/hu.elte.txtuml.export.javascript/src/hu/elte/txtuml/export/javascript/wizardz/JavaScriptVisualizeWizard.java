package hu.elte.txtuml.export.javascript.wizardz;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.javascript.Exporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.uml2.ExportMode;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
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
public class JavaScriptVisualizeWizard extends TxtUMLVisualizeWizard {

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
		selectTxtUmlPage = new VisualizeTxtUMLPage(false, true, StateMachineDiagram.class, ClassDiagram.class, CompositeDiagram.class);
		addPage(selectTxtUmlPage);
	}

	@Override
	protected boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs, List<IType> txtUMLLayout) {
		for (Pair<String, String> model : layoutConfigs.keySet()) {
			String txtUMLModelName = model.getFirst();
			String txtUMLProjectName = model.getSecond();

			String generatedFolderName = PreferencesManager
					.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

			Map<String, String> layouts = new HashMap<String, String>();
			layoutConfigs.get(model).forEach(
					layout -> layouts.put(layout.getFullyQualifiedName(), layout.getJavaProject().getElementName()));

			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProjectName, txtUMLModelName,
					txtUMLLayout.stream().map(IType::getFullyQualifiedName).collect(Collectors.toList()));
			if (!saveSucceeded)
				return false;

			try {
				this.checkNoLayoutDescriptionsSelected();
				IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
				progressService.runInUI(progressService, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {
						monitor.beginTask("Visualization", 100);

						TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, generatedFolderName,
								txtUMLModelName, layouts);
						try {
							exporter.cleanBeforeVisualization();
						} catch (CoreException | IOException e) {
							Dialogs.errorMsgb("txtUML export Error - cleaning resources",
									"Error occured when cleaning resources.", e);
							throw new InterruptedException();
						}
						monitor.subTask("Exporting txtUML Model to UML2 model...");
						try {
							TxtUMLToUML2.exportModel(txtUMLProjectName, txtUMLModelName,
									txtUMLProjectName + "/" + generatedFolderName, ExportMode.ErrorHandlingNoActions,
									"gen");
							monitor.worked(10);
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML export Error", "Error occured during the UML2 exportation.", e);
							monitor.done();
							throw new InterruptedException();
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
								warningMessages.append(
										String.join(System.lineSeparator() + System.lineSeparator() + "- ", warnings));
								warningMessages.append(
										System.lineSeparator() + System.lineSeparator() + "Do you want to continue?");

								if (!Dialogs.WarningConfirm("Warnings about layout description",
										warningMessages.toString())) {
									throw new InterruptedException();
								}
							}

							monitor.worked(5);
						} catch (Exception e) {
							if (e instanceof InterruptedException) {
								throw (InterruptedException) e;
							} else {
								Dialogs.errorMsgb("txtUML layout export Error",
										"Error occured during the diagram layout interpretation.", e);
								monitor.done();
								throw new InterruptedException();
							}
						}

						monitor.subTask("Exporting diagrams for JavaScript visualization...");
						try {
							Exporter ex = new Exporter(layoutDescriptor, txtUMLModelName);
							ex.export();
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML visualization Error",
									"Error occured during the visualization process.", e);
							monitor.done();
							throw new InterruptedException();
						}
					}
				}, ResourcesPlugin.getWorkspace().getRoot());
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.sys.error(e.getMessage());
				return false;
			}
		}
		return true;
	}

}
