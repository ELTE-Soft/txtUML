package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.TxtUMLPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.plantuml.PlantUmlExporter;
import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramExportException;
import hu.elte.txtuml.export.uml2.ExportMode;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.SaveUtils;
import hu.elte.txtuml.utils.eclipse.WizardUtils;

/**
 * Wizard for visualization of txtUML models
 */
public class TxtUMLVisualizeWizard extends Wizard {

	private VisualizeTxtUMLPage selectTxtUmlPage;

	/**
	 * The Constructor
	 */
	public TxtUMLVisualizeWizard() {
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
		List<IType> txtUMLLayout = selectTxtUmlPage.getTxtUmlLayouts();
		Map<Pair<String, String>, List<IType>> layoutConfigs = new HashMap<>();
		List<String> invalidLayouts = new ArrayList<>();
		for (IType layout : txtUMLLayout) {
			Optional<Pair<String, String>> maybeModel = Optional.empty();
			try {
				maybeModel = Stream.of(layout.getTypes())
						.map(innerClass -> WizardUtils.getModelByAnnotations(innerClass)).filter(Optional::isPresent)
						.map(Optional::get).findFirst();
			} catch (JavaModelException e) {
				Logger.sys.error(e.getMessage());
				return false;
			}

			if (maybeModel.isPresent()) {
				Pair<String, String> model = maybeModel.get();
				if (!layoutConfigs.containsKey(model)) {
					layoutConfigs.put(model, new ArrayList<>(Arrays.asList(layout)));
				} else {
					layoutConfigs.get(model).add(layout);
				}
			} else {
				invalidLayouts.add(layout.getElementName());
			}
		}

		if (!invalidLayouts.isEmpty()) {
			Dialogs.MessageBox("Invalid layouts", "The following diagram descriptions have no txtUML model attached"
					+ ", hence no diagram is generated for them:" + System.lineSeparator() + invalidLayouts.stream()
							.map(s -> " - ".concat(s)).collect(Collectors.joining(System.lineSeparator())));
		}

		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT, layoutConfigs.values().stream()
				.flatMap(c -> c.stream()).map(layout -> layout.getFullyQualifiedName()).collect(Collectors.toList()));

		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT_PROJECTS,
				layoutConfigs.values().stream().flatMap(c -> c.stream())
						.map(layout -> layout.getJavaProject().getElementName()).collect(Collectors.toList()));

		for (Pair<String, String> model : layoutConfigs.keySet()) {
			String txtUMLModelName = model.getFirst();
			String txtUMLProjectName = model.getSecond();

			String generatedFolderName = PreferencesManager
					.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

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

				IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
				PlantUmlExporter exp = new PlantUmlExporter(txtUMLProjectName, generatedFolderName,
						fullyQualifiedNames);

				if (exp.hasSequenceDiagram()) {
					progressService.runInUI(progressService, new IRunnableWithProgress() {

						@Override
						public void run(IProgressMonitor monitor) throws InterruptedException {
							monitor.beginTask("Sequence Diagram Export", 100);
							try {
								exp.generatePlantUmlOutput(monitor);
							} catch (CoreException | SequenceDiagramExportException e) {
								Dialogs.errorMsgb("txtUML export Error",
										"Error occured during the PlantUml exportation.", e);
								monitor.done();
								throw new InterruptedException();
							}
						}

					}, ResourcesPlugin.getWorkspace().getRoot());

					if (exp.noDiagramLayout()) {
						return true;
					}
				}

				if (!exp.noDiagramLayout() || !exp.hasSequenceDiagram()) {
					Job job = new Job("Diagram Visualization") {

						@Override
						protected IStatus run(IProgressMonitor monitor) {
							monitor.beginTask("Visualization", 100);

							TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, generatedFolderName,
									txtUMLModelName, layouts);
							Display.getDefault().syncExec(() -> {
								try {
									exporter.cleanBeforeVisualization();
								} catch (CoreException | IOException e) {
									Dialogs.errorMsgb("txtUML export Error - cleaning resources",
											"Error occured when cleaning resources.", e);
								}
							});
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

							PapyrusVisualizer pv = exporter.createVisualizer(layoutDescriptor);
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
				}
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.sys.error(e.getMessage());
				return false;
			}
		}
		return true;
	}

	private void checkNoLayoutDescriptionsSelected() throws InterruptedException {
		if (selectTxtUmlPage.getTxtUmlLayouts().isEmpty()) {
			boolean answer = Dialogs.WarningConfirm("No Layout descriptions",
					"No diagrams will be generated using the current setup,"
							+ " because no diagram descriptions are added." + System.lineSeparator()
							+ "In order to have diagrams visualized, select a description from the wizard."
							+ System.lineSeparator() + System.lineSeparator()
							+ "Do you want to continue without diagram descriptions?");
			if (!answer)
				throw new InterruptedException();
		}
	}
}
