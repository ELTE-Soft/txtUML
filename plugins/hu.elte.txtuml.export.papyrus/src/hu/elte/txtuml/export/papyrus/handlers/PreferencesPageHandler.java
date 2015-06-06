package hu.elte.txtuml.export.papyrus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Handles the the call to preferences page.
 *
 * @author András Dobreff
 */
public class PreferencesPageHandler extends AbstractHandler implements IHandler {

	/**
	 * Opens the perferences page 
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PreferenceDialog pd = PreferencesUtil.createPreferenceDialogOn(null,
								"hu.elte.txtuml.export.papyrus.preferences1", null, null);
		
		pd.open();
		return null;
	}

}
