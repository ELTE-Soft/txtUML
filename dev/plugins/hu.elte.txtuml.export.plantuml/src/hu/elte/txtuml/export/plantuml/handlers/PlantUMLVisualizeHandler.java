package hu.elte.txtuml.export.plantuml.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import hu.elte.txtuml.export.plantuml.wizards.PlantUMLVisualizeWizard;

/**
 * Handles the call to txtUML Visualization
 */
public class PlantUMLVisualizeHandler  extends AbstractHandler {

	/**
	 * Opens a TxtUMLVisuzalize wizard
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {


		WizardDialog wizardDialog = new WizardDialog( null,
	      new PlantUMLVisualizeWizard());
		
		wizardDialog.open();
		return null;
	}
	
}
