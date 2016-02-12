package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesUI;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * A PreferencesPage for the visualization wizard.
 * The UI is the same as by {@link hu.elte.txtuml.export.papyrus.preferences.PreferencesPage PreferencesPage}
 */
public class PreferencesPage extends WizardPage{

	private PreferencesUI preferencesUI;
	
	/**
	 * The Contructor
	 */
	public PreferencesPage() {
		super("Plugin Preferences Page");
		setTitle("Preferences page");
	    setDescription("Select which diagrams and components you want to be generated automatically!");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		preferencesUI = new PreferencesUI();
		preferencesUI.init(container);
		setControl(container);
	}

	/**
	 * Sets the Preferences
	 */
	public void setPreferences(){
		if(preferencesUI != null){
			PreferencesManager.setValues(preferencesUI.getValues());
		}
	}
}
