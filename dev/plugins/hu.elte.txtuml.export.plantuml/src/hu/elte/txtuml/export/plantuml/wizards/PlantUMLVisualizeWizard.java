package hu.elte.txtuml.export.plantuml.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.export.plantuml.PlantUmlExporter;
import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramExportException;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.SaveUtils;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;
import hu.elte.txtuml.utils.eclipse.wizards.TxtUMLVisualizeWizard;
import hu.elte.txtuml.utils.eclipse.wizards.VisualizeTxtUMLPage;

public class PlantUMLVisualizeWizard extends TxtUMLVisualizeWizard {

	public PlantUMLVisualizeWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Create PlantUML diagram from txtUML sequence diagram";
	}

	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage(true, false, SequenceDiagram.class, Interaction.class);
		addPage(selectTxtUmlPage);
	}

	@Override
	protected boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs, List<IType> txtUMLLayout) {
		for (Pair<String, String> model : layoutConfigs.keySet()) {
			String txtUMLModelName = model.getFirst();
			String txtUMLProjectName = model.getSecond();

			String generatedFolderName = PreferencesManager
					.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

			List<String> diagramNames = new ArrayList<>();
			layoutConfigs.get(model).forEach(layout -> diagramNames.add(layout.getFullyQualifiedName()));

			List<String> fullyQualifiedNames = txtUMLLayout.stream().map(IType::getFullyQualifiedName)
					.collect(Collectors.toList());
			boolean saveSucceeded = SaveUtils.saveAffectedFiles(getShell(), txtUMLProjectName, txtUMLModelName,
					fullyQualifiedNames);
			if (!saveSucceeded)
				return false;

			try {
				checkNoLayoutDescriptionsSelected();

				IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
				PlantUmlExporter exp = new PlantUmlExporter(txtUMLProjectName, generatedFolderName, diagramNames);

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
				}
			} catch (InterruptedException | InvocationTargetException e) {
				Logger.sys.error(e.getMessage());
				return false;
			}
		}
		return true;
	}

}
