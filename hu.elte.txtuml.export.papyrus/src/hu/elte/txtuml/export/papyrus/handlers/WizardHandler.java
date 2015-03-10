package hu.elte.txtuml.export.papyrus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.WizardDialog;
import hu.elte.txtuml.export.papyrus.wizardz.UmlToPapyrusWizard;

public class WizardHandler extends AbstractHandler implements IHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		WizardDialog wizardDialog = new WizardDialog( null,
	      new UmlToPapyrusWizard());
		
		wizardDialog.open();
		return null;
	}

}
