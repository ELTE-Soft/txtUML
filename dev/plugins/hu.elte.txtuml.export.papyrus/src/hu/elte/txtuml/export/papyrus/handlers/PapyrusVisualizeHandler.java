package hu.elte.txtuml.export.papyrus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import hu.elte.txtuml.export.papyrus.wizardz.PapyrusVisualizeWizard;

/**
 * Handles the call to txtUML Visualization
 */
public class PapyrusVisualizeHandler  extends AbstractHandler {

	/**
	 * Opens a TxtUMLVisuzalize wizard
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {


		WizardDialog wizardDialog = new WizardDialog( null,
	      new PapyrusVisualizeWizard());
		
		wizardDialog.open();
		return null;
	}
	
}
