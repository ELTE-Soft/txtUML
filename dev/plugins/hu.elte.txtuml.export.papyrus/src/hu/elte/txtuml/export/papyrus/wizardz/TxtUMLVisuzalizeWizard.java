package hu.elte.txtuml.export.papyrus.wizardz;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
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
		return "Create Papyrus Model from txtUML Model";
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
		String txtUMLModelName = selectTxtUmlPage.getTxtUmlModelClass();
		List<String> txtUMLLayout = selectTxtUmlPage.getTxtUmlLayout();
		String txtUMLProjectName = selectTxtUmlPage.getTxtUmlProject();

		String generatedFolderName = PreferencesManager
				.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT, txtUMLProjectName);
		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL, txtUMLModelName);
		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT, txtUMLLayout);

		try {
			
			this.checkEmptyLayoutDecsriptions();
			
			IProgressService progressService = PlatformUI.getWorkbench()
					.getProgressService();
			
			//This code runs on the UI thread and the following should also because of the error/warning dialogs 
			progressService.run(false, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Visualization", 100);

					TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, generatedFolderName,
							txtUMLModelName, txtUMLLayout);
					
					clean(exporter);
					exportModel(exporter, monitor);
					TxtUMLLayoutDescriptor layoutDescriptor = generateLayoutDescription(exporter, monitor);
					layoutDescriptor.mappingFolder = generatedFolderName;
					layoutDescriptor.projectName = txtUMLProjectName;

					visualize(exporter, layoutDescriptor, monitor);
				}
			});

			return true;
		} catch (InvocationTargetException | InterruptedException e) {
			return false;
		}
	}

	/**
	 * Cleans the output folder before visualization and handles the possible errors
	 * @param exporter
	 * @throws InterruptedException
	 */
	private void clean(TxtUMLExporter exporter) throws InterruptedException {
		try {
			exporter.cleanBeforeVisualization();
		} catch (CoreException | InvocationTargetException e) {
			Dialogs.errorMsgb("txtUML export Error - cleaning resources",
					"Error occured when cleaning resources.", e);
			throw new InterruptedException();
		}
	}

	/**
	 * Exports the txtUML model to EMF UML model and handles the possible errors
	 * @param exporter
	 * @param monitor
	 * @throws InterruptedException
	 */
	private void exportModel(TxtUMLExporter exporter, IProgressMonitor monitor) throws InterruptedException {
		monitor.subTask("Exporting txtUML Model to UML2 model...");
		try {
			exporter.exportModel();
			monitor.worked(10);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML export Error", "Error occured during the UML2 exportation.", e);
			monitor.done();
			throw new InterruptedException();
		}
	}

	/**
	 * Generates the layout description specified in txtUML and handles the possible errors 
	 * @param exporter
	 * @param monitor
	 * @return
	 * @throws InterruptedException
	 */
	private TxtUMLLayoutDescriptor generateLayoutDescription(TxtUMLExporter exporter, IProgressMonitor monitor) throws InterruptedException {

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
			if (e instanceof InterruptedException) {
				throw (InterruptedException) e;
			} else {
				Dialogs.errorMsgb("txtUML layout export Error",
						"Error occured during the diagram layout interpretation.", e);
				monitor.done();
				throw new InterruptedException();
			}
		}

	}

	/**
	 * Displays a list of warnings
	 * @param warnings
	 * @throws InterruptedException
	 */
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
			PlatformUI.getWorkbench().getProgressService().run(true, true, pv);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML visualization Error",
					"Error occured during the visualization process.", e);
			monitor.done();
			throw new InterruptedException();
		}
	}
	
	private void checkEmptyLayoutDecsriptions() throws InterruptedException {
		if(selectTxtUmlPage.getTxtUmlLayout().isEmpty()){
			boolean answer = Dialogs.WarningConfirm("No Layout descriptions",
					"No diagrams will be generated using the current setup,"
							+ " because no diagram descriptions are added."
							+ System.lineSeparator() +
							"Use the 'Add txtUML diagram descriptions' button to avoid this message."
							+ System.lineSeparator() + System.lineSeparator()
							+ "Do you want to continue without diagram descriptions?"
					);
			if(!answer) throw new InterruptedException();
		}
	}
}
