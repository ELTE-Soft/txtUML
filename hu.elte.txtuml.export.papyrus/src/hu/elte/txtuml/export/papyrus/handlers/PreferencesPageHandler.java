package hu.elte.txtuml.export.papyrus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class PreferencesPageHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PreferenceDialog pd = PreferencesUtil.createPreferenceDialogOn(null,
								"hu.elte.txtuml.export.papyrus.preferences1", null, null);
		
		pd.open();
		return null;
	}

}
