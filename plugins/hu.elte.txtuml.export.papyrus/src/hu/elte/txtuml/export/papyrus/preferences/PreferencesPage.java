package hu.elte.txtuml.export.papyrus.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Preferences page for txtUML Papyrus visualization
 * The UI is the same as by {@link hu.elte.txtuml.export.papyrus.wizardz.PreferencesPage PreferencesPage}
 */
public class PreferencesPage extends PreferencePage implements IWorkbenchPreferencePage{

	private PreferencesUI preferencesUI;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		preferencesUI = new PreferencesUI();
		preferencesUI.init(container);
		return container;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if(preferencesUI != null){
			PreferencesManager.setValues(preferencesUI.getValues());
		}
		return super.performOk();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults(){
		PreferencesManager.resetDefaults();
		preferencesUI.refresh();
		super.performDefaults();
	}
}
