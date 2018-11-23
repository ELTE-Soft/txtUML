package hu.elte.txtuml.export.diagrams.common.wizards;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IType;

import hu.elte.txtuml.export.diagrams.common.TxtUMLExporter;
import hu.elte.txtuml.export.diagrams.common.arrange.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.diagrams.common.layout.LayoutUtils;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.SaveUtils;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;

/**
 * Wizard for visualization of txtUML models
 */
public abstract class UML2VisualizeWizard extends TxtUMLVisualizeWizard {
	
	/**
	 * Calls the {@link hu.elte.txtuml.export.uml2.UML2 txtUML UML2 Export} and
	 * then starts the visualization.
	 */
	@Override
	public boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs, List<IType> txtUMLLayout) {
		for (Pair<String, String> model : layoutConfigs.keySet()) {
			String txtUMLModelName = model.getFirst();
			String txtUMLProjectName = model.getSecond();

			String generatedFolderName = PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

			Map<String, String> layouts = new HashMap<String, String>();
			layoutConfigs.get(model).forEach(
					layout -> layouts.put(layout.getFullyQualifiedName(), layout.getJavaProject().getElementName()));

			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProjectName, txtUMLModelName,
					txtUMLLayout.stream().map(IType::getFullyQualifiedName).collect(Collectors.toList()));
			if (!saveSucceeded) {
				return false;
			}

			Job job = new Job("Diagram Visualization: " + txtUMLProjectName) {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Visualization", 100);

						TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, generatedFolderName,
								txtUMLModelName, layouts);

						clean(exporter);
						exportModel(exporter, monitor);

						TxtUMLLayoutDescriptor layoutDescriptor = generateLayoutDescription(exporter, monitor);
						layoutDescriptor.mappingFolder = generatedFolderName;
						layoutDescriptor.projectName = txtUMLProjectName;

						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;

						visualization(generatedFolderName, SubMonitor.convert(monitor, 85), layoutDescriptor);

						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;

						return Status.OK_STATUS;
					} catch (Exception e) {
						return Status.CANCEL_STATUS;
					}
				}
			};

			job.setUser(true);
			job.schedule();
		}
		return true;
	}

	/**
	 * Cleans the output folder before visualization and handles the possible
	 * errors
	 * 
	 * @param exporter
	 * @throws InterruptedException
	 */
	private void clean(TxtUMLExporter exporter) throws InterruptedException {
		try {
			exporter.cleanBeforeVisualization();
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML export Error - cleaning resources", "Error occured when cleaning resources.", e);
			throw new InterruptedException();
		}
	}

	/**
	 * Exports the txtUML model to EMF UML model and handles the possible errors
	 * 
	 * @param exporter
	 * @param monitor
	 */
	private void exportModel(TxtUMLExporter exporter, IProgressMonitor monitor) {
		monitor.subTask("Exporting txtUML Model to UML2 model...");
		LayoutUtils.getDisplay().syncExec(() -> {
			try {
				exporter.exportModel();
			} catch (Exception e) {
				Dialogs.errorMsgb("txtUML export Error", "Error occured during the UML2 exportation.", e);
				monitor.done();
				throw new RuntimeException();
			}
		});
		monitor.worked(10);
	}

	/**
	 * Generates the layout description specified in txtUML and handles the
	 * possible errors
	 * 
	 * @param exporter
	 * @param monitor
	 * @return
	 * @throws InterruptedException
	 */
	private TxtUMLLayoutDescriptor generateLayoutDescription(TxtUMLExporter exporter, IProgressMonitor monitor)
			throws Exception {

		monitor.subTask("Generating txtUML layout description...");
		TxtUMLLayoutDescriptor layoutDescriptor = null;
		try {
			layoutDescriptor = exporter.exportTxtUMLLayout();

			List<String> warnings = new LinkedList<String>();
			for (DiagramExportationReport report : layoutDescriptor.getReports()) {
				warnings.addAll(report.getWarnings());
			}

			displayWarnings(warnings);

			monitor.worked(5);
			return layoutDescriptor;
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML layout export Error", "Error occured during the diagram layout interpretation.",
					e);
			monitor.done();
			throw e;
		}

	}

	private void displayWarnings(List<String> warnings) throws InterruptedException {
		if (warnings.size() != 0) {
			StringBuilder warningMessages = new StringBuilder(
					"Warnings:" + System.lineSeparator() + System.lineSeparator() + "- ");
			warningMessages.append(String.join(System.lineSeparator() + System.lineSeparator() + "- ", warnings));
			warningMessages.append(System.lineSeparator() + System.lineSeparator() + "Do you want to continue?");

			if (!Dialogs.WarningConfirm("Warnings about layout description", warningMessages.toString())) {
				throw new InterruptedException();
			}
		}

	}

	private void visualization(String generatedFolderName, IProgressMonitor monitor,
			TxtUMLLayoutDescriptor layoutDescriptor) throws InterruptedException {
		try {
			visualize(layoutDescriptor, generatedFolderName, monitor);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML visualization Error", "Error occured during the visualization process.", e);
			monitor.done();
			throw new InterruptedException();
		}
	}

	protected abstract void visualize(TxtUMLLayoutDescriptor layoutDescriptor, String genFolder,
			IProgressMonitor monitor) throws Exception;
}
