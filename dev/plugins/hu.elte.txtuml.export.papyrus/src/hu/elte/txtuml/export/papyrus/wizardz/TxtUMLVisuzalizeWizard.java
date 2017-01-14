package hu.elte.txtuml.export.papyrus.wizardz;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.papyrus.utils.LayoutUtils;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.eclipse.Dialogs;

/**
 * Wizard for visualization of txtUML models
 */
public class TxtUMLVisuzalizeWizard extends Wizard {

	private VisualizeTxtUMLPage selectTxtUmlPage;

	/**
	 * The Constructor
	 */
	public TxtUMLVisuzalizeWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle() {
		return "Create Papyrus model from txtUML model";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage();
		addPage(selectTxtUmlPage);
	}

	/**
	 * Calls the {@link hu.elte.txtuml.export.uml2.UML2 txtUML UML2 Export} and
	 * then starts the visualization.
	 */
	@Override
	public boolean performFinish() {
		PreferencesManager.setDefaults();
		String txtUMLModelName = selectTxtUmlPage.getTxtUmlModelClass();
		Map<String, String> txtUMLLayout = selectTxtUmlPage.getTxtUmlLayout();
		String txtUMLProjectName = selectTxtUmlPage.getTxtUmlProject();

		String generatedFolderName = PreferencesManager
				.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT, txtUMLProjectName);
		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL, txtUMLModelName);
		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT, txtUMLLayout.keySet());

		if(!this.checkEmptyVisualizationRequested()){
			return false;
		}
		
		Job job = new Job("Diagram Visualization") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {							
				try {
					monitor.beginTask("Visualization", 100);

					TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, generatedFolderName,
							txtUMLModelName, txtUMLLayout);

					clean(exporter);
					exportModel(exporter, monitor);
					TxtUMLLayoutDescriptor layoutDescriptor = generateLayoutDescription(exporter, monitor);
					layoutDescriptor.mappingFolder = generatedFolderName;
					layoutDescriptor.projectName = txtUMLProjectName;
					
					if(monitor.isCanceled()) return Status.CANCEL_STATUS;
					
					visualize(exporter, layoutDescriptor, monitor);
					if(monitor.isCanceled()){
						return Status.CANCEL_STATUS;
					}else{
						return Status.OK_STATUS;
					}
				} catch (Exception e) {
					return Status.CANCEL_STATUS;
				}
			}
		};
		job.setUser(true);
		job.schedule();
		return true;
	}

	/**
	 * Cleans the output folder before visualization and handles the possible errors
	 * @param exporter
	 * @throws InterruptedException
	 */
	private void clean(TxtUMLExporter exporter) throws InterruptedException {
		try {
			exporter.cleanBeforeVisualization();
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML export Error - cleaning resources",
					"Error occured when cleaning resources.", e);
			throw new InterruptedException();
		}
	}

	/**
	 * Exports the txtUML model to EMF UML model and handles the possible errors
	 * @param exporter
	 * @param monitor
	 */
	private void exportModel(TxtUMLExporter exporter, IProgressMonitor monitor) {
		monitor.subTask("Exporting txtUML Model to UML2 model...");
			LayoutUtils.getDisplay().syncExec(() ->{
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
	 * Generates the layout description specified in txtUML and handles the possible errors 
	 * @param exporter
	 * @param monitor
	 * @return
	 * @throws InterruptedException
	 */
	private TxtUMLLayoutDescriptor generateLayoutDescription(TxtUMLExporter exporter, IProgressMonitor monitor) throws Exception {

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
			Dialogs.errorMsgb("txtUML layout export Error",
					"Error occured during the diagram layout interpretation.", e);
			monitor.done();
			throw e;
		}

	}

	private void displayWarnings(List<String> warnings) throws InterruptedException {
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
		
	}

	protected void visualize(TxtUMLExporter exporter, TxtUMLLayoutDescriptor layoutDescriptor,
			IProgressMonitor monitor) throws InterruptedException {
		try {
			PapyrusVisualizer pv = exporter.createVisualizer(layoutDescriptor);
			pv.run(new SubProgressMonitor(monitor, 85));
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML visualization Error",
					"Error occured during the visualization process.", e);
			monitor.done();
			throw new InterruptedException();
		}
	}
	
	private boolean checkEmptyVisualizationRequested() {
		if(selectTxtUmlPage.getTxtUmlLayout().isEmpty()){
			boolean answer = Dialogs.WarningConfirm("No Layout descriptions",
					"No diagrams will be generated using the current setup,"
							+ " because no diagram descriptions are added." + System.lineSeparator()
							+ "Use the 'Add txtUML diagram descriptions' button to avoid this message."
							+ System.lineSeparator() + System.lineSeparator()
							+ "Do you want to continue without diagram descriptions?");
			return answer;
		}
		return true;
	}
}
