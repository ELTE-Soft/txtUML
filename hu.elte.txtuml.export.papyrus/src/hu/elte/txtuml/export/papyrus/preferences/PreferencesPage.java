package hu.elte.txtuml.export.papyrus.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencesPage extends PreferencePage implements IWorkbenchPreferencePage{

	private PreferencesUI preferencesUI;
	private PreferencesManager preferencesManager;
	
	@Override
	public void init(IWorkbench workbench) {
		preferencesManager = new PreferencesManager();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		preferencesUI = new PreferencesUI();
		preferencesUI.init(container, preferencesManager);
		return container;
	}
	
	
	@Override
	public boolean performOk() {
		if(preferencesUI != null){
			preferencesManager.setValues(preferencesUI.getValues());
		}
		return super.performOk();
	}
	
	@Override
	protected void performDefaults(){
		preferencesManager.resetDefaults();
		preferencesUI.refresh();
		super.performDefaults();
	}
}
