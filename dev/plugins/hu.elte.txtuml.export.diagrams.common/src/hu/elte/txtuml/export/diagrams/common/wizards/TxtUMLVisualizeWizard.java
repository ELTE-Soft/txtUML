package hu.elte.txtuml.export.diagrams.common.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.wizard.Wizard;

import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
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
		/*Check if any diagrams were selected*/
		if (!this.checkNoLayoutDescriptionsSelected()) {
			return false;
		}
		
		List<IType> txtUMLLayout = selectTxtUmlPage.getTxtUmlLayouts();
		Map<Pair<String, String>, List<IType>> layoutConfigs = null;
		/*Check for invalid layouts*/
		try {
			layoutConfigs = checkLayouts(txtUMLLayout);
		} catch (IllegalArgumentException ex) {
			Dialogs.MessageBox("Invalid layouts", ex.getMessage());
			return false;
		} catch (JavaModelException e) {
			Logger.user.error(e.getMessage());
			return false;
		}
		
		setTxtUMLLayoutPreferences(layoutConfigs);

		/*TODO: continue wizard refactoring with exportDiagrams, jobs, workers, better monitoring and parallelization*/
		return exportDiagrams(layoutConfigs, txtUMLLayout);
	}

	/**
	 * Abstract method for diagram export.
	 */
	protected abstract boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs,
			List<IType> txtUMLLayout);

	private boolean checkNoLayoutDescriptionsSelected(){
		if (selectTxtUmlPage.getTxtUmlLayouts().isEmpty()) {
			boolean answer = Dialogs.WarningConfirm("No Layout descriptions",
					"No diagrams will be generated using the current setup,"
							+ " because no diagram descriptions are added." + System.lineSeparator()
							+ "In order to have diagrams visualized, select a description from the wizard."
							+ System.lineSeparator() + System.lineSeparator()
							+ "Do you want to continue without diagram descriptions?");
			return answer;
		}
		return true;
	}
	
	private Map<Pair<String, String>, List<IType>> checkLayouts(List<IType> txtUMLLayout) throws JavaModelException {
		Map<Pair<String, String>, List<IType>> layoutConfigs = new HashMap<>();
		List<String> invalidLayouts = new ArrayList<>();
		for (IType layout : txtUMLLayout) {
			Optional<Pair<String, String>> maybeModel = Optional.empty();
			maybeModel = Stream.of(layout.getTypes()).map(innerClass -> WizardUtils.getModelByAnnotations(innerClass))
					.filter(Optional::isPresent).map(Optional::get).findFirst();
			if (!maybeModel.isPresent())
				maybeModel = WizardUtils.getModelByFields(layout);
			
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
			throw new IllegalArgumentException("The following diagram descriptions have no txtUML model attached"
					+ ", hence no diagram is generated for them:" + System.lineSeparator() + invalidLayouts.stream()
							.map(s -> " - ".concat(s)).collect(Collectors.joining(System.lineSeparator())));
		}
		return layoutConfigs;
	}
	
	private void setTxtUMLLayoutPreferences(Map<Pair<String, String>, List<IType>> layoutConfigs){
		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT, layoutConfigs.values().stream()
				.flatMap(c -> c.stream()).map(layout -> layout.getFullyQualifiedName()).collect(Collectors.toList()));

		PreferencesManager.setValue(PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT_PROJECTS,
				layoutConfigs.values().stream().flatMap(c -> c.stream())
						.map(layout -> layout.getJavaProject().getElementName()).collect(Collectors.toList()));
	}

}
