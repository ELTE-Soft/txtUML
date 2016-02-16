package hu.elte.txtuml.export.papyrus.wizardz;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.export.ExportUtils;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.TxtUMLPapyrusModelManager;
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
		boolean generateSMsAutomatically = selectTxtUmlPage.getGenerateSMDs();
		String generatedFolderName = PreferencesManager
				.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
		
		PreferencesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT,
				txtUMLProjectName);
		PreferencesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL,
				txtUMLModelName);
		PreferencesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT,
				txtUMLLayout);
		PreferencesManager.setValue(
				PreferencesManager.GENERATE_STATEMACHINES_AUTOMATICALLY,
				generateSMsAutomatically);
		
		try {
			IProgressService progressService = PlatformUI.getWorkbench()
					.getProgressService();

			progressService.runInUI(progressService,
					new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor)
								throws InterruptedException {
							monitor.beginTask("Visualization", 100);

							
							TxtUMLExporter exporter = new TxtUMLExporter(
									txtUMLProjectName, generatedFolderName,
									txtUMLModelName, txtUMLLayout);
							try {
								exporter.cleanBeforeVisualization();
							} catch (CoreException e) {
								Dialogs.errorMsgb("txtUML export Error - cleaning resources", 
										"Error occured when cleaning resources.", e);
								throw new InterruptedException();
							}
							monitor.subTask("Exporting txtUML Model to UML2 model...");
							try {
								ExportUtils.exportTxtUMLModelToUML2(
										txtUMLProjectName, txtUMLModelName,
										txtUMLProjectName + "/" + generatedFolderName);
								monitor.worked(10);
							} catch (Exception e) {
								Dialogs.errorMsgb("txtUML export Error", 
										"Error occured during the UML2 exportation.", e);
								monitor.done();
								throw new InterruptedException();
							}

							monitor.subTask("Generating txtUML layout description...");
							TxtUMLLayoutDescriptor layoutDescriptor = null;
							try {
								layoutDescriptor = exporter.exportTxtUMLLayout();

								List<String> warnings = new LinkedList<String>();
								for (DiagramExportationReport report : layoutDescriptor
										.getReports()) {
									warnings.addAll(report.getWarnings());
								}

								layoutDescriptor.generateSMDs = generateSMsAutomatically;
								layoutDescriptor.mappingFolder = generatedFolderName;
								layoutDescriptor.projectName = txtUMLProjectName; 
								
								if (warnings.size() != 0) {
									StringBuilder warningMessages = new StringBuilder(
											"Warnings:"
													+ System.lineSeparator()+ System.lineSeparator()+"- ");
									warningMessages.append(String.join(
											System.lineSeparator()+System.lineSeparator()+"- ", warnings));
									warningMessages.append(System
											.lineSeparator()
											+ System.lineSeparator()
											+ "Do you want to continue?");

									if (!Dialogs
											.WarningConfirm(
													"Warnings about layout description",
													warningMessages.toString())) {
										throw new InterruptedException();
									}
								}

								monitor.worked(5);
							} catch (Exception e) {
								if (e instanceof InterruptedException) {
									throw (InterruptedException) e;
								} else {
									Dialogs.errorMsgb(
											"txtUML layout export Error",
											"Error occured during the diagram layout interpretation.", e);
									monitor.done();
									throw new InterruptedException();
								}
							}

							try {
								PapyrusVisualizer pv = exporter
										.createVisualizer(layoutDescriptor);
								pv.registerPayprusModelManager(TxtUMLPapyrusModelManager.class);
								pv.run(new SubProgressMonitor(monitor, 85));
							} catch (Exception e) {
								Dialogs.errorMsgb(
										"txtUML visualization Error",
										"Error occured during the visualization process.", e);
								monitor.done();
								throw new InterruptedException();
							}
						}
					}, ResourcesPlugin.getWorkspace().getRoot());
			return true;
		} catch (InvocationTargetException | InterruptedException e) {
			return false;
		}
	}

}
