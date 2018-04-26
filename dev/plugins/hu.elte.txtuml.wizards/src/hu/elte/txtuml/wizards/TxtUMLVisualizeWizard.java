package hu.elte.txtuml.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.EditorUtils;
import hu.elte.txtuml.utils.eclipse.WizardUtils;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;

public abstract class TxtUMLVisualizeWizard extends Wizard {

	protected VisualizeTxtUMLPage selectTxtUmlPage;

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
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public abstract void addPages();
	
	/**
	 * Collects the selected diagrams and calls the export method.
	 */
	@Override
	public boolean performFinish() {
		try {
			this.checkNoLayoutDescriptionsSelected();
		} catch (InterruptedException e1) {
			return false;
		}
		List<IType> txtUMLLayout = selectTxtUmlPage.getTxtUmlLayouts();
		Map<Pair<String, String>, List<IType>> layoutConfigs = new HashMap<>();
		List<String> invalidLayouts = new ArrayList<>();
		for (IType layout : txtUMLLayout) {
			Optional<Pair<String, String>> maybeModel = Optional.empty();
			try {
				maybeModel = Stream.of(layout.getTypes())
						.map(innerClass -> WizardUtils.getModelByAnnotations(innerClass)).filter(Optional::isPresent)
						.map(Optional::get).findFirst();
				if (!maybeModel.isPresent())
					maybeModel = WizardUtils.getModelByFields(layout);
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

		try {
			cleanBeforeVisualization(layoutConfigs.keySet());
		} catch (CoreException | IOException e) {
			Dialogs.errorMsgb("txtUML export Error - cleaning resources",
					"Error occured while cleaning resources.", e);
		}
		
		for(Map.Entry<Pair<String, String>, List<IType>> config : layoutConfigs.entrySet()){
			if(!saveOpenEditors(config)){
				Dialogs.errorMsgb("txtUML export Error - saving open editors",
						"Error occured while saving open editors.",new Exception());
				return false;
			}
		}
		
		return exportDiagrams(layoutConfigs);
	}

	
	
	/**
	 * Abstract method for diagram export.
	 */
	protected abstract boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs);
	
	/**
	 * Abstract method for cleanup.
	 * 
	 * Performs cleanup according to the visualization method used.
	 * 
	 * @param layouts
	 * 			the modelName, projectName pairs to be cleaned before export
	 * 
	 * @throws CoreException
	 *             if this method fails. Reasons include:
	 *             <ul>
	 *             <li>This resource could not be deleted for some reason.</li>
	 *             <li>This resource or one of its descendants is out of sync
	 *             with the local file system and force is false.</li>
	 *             <li>Resource changes are disallowed during certain types of
	 *             resource change event notification. See IResourceChangeEvent
	 *             for more details.</li>
	 *             </ul>
	 * 
	 */
	protected abstract void cleanBeforeVisualization(Set<Pair<String, String>> layouts) throws CoreException, IOException ;
	
	/**
	 * Saves dirty editors for all files included in the layout config
	 * 
	 * @param config
	 * 			the layout config to save
	 */
	protected boolean saveOpenEditors(Map.Entry<Pair<String, String>, List<IType>> config){
			String txtUMLModelName = config.getKey().getFirst();
			String txtUMLProjectName = config.getKey().getSecond();
			List<String> fullyQualifiedNames = config.getValue().stream().map(IType::getFullyQualifiedName)
					.collect(Collectors.toList());

			return EditorUtils.saveAffectedFiles(getShell(), txtUMLProjectName, txtUMLModelName, fullyQualifiedNames);
	}

	protected void checkNoLayoutDescriptionsSelected() throws InterruptedException {
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
	
	protected static void deleteFolderRecursively(Path modelFolderPath) throws IOException {
		if (modelFolderPath.toFile().exists()) {
			Files.walk(modelFolderPath).map(Path::toFile).sorted((o1, o2) -> -o1.compareTo(o2)).forEach(File::delete);
		}
	}
	
}
