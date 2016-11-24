package hu.elte.txtuml.export.javascript.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import hu.elte.txtuml.export.javascript.wizardz.TxtUMLVisualizeWizard;

/**
 * Handles the call to txtUML Visualization
 */
public class TxtUMLVisualizeHandler extends AbstractHandler {

	/**
	 * Opens a TxtUMLVisuzalize wizard
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		WizardDialog wizardDialog = new WizardDialog(null, new TxtUMLVisualizeWizard());

		wizardDialog.open();
		return null;
	}

}
