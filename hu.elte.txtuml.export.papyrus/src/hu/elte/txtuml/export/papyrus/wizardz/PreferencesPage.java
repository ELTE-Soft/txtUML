package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesUI;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class PreferencesPage extends WizardPage{

	private PreferencesUI preferencesUI;
	private PreferencesManager preferencesManager;
	
	public PreferencesPage() {
		super("Plugin Preferences Page");
		setTitle("Preferences page");
	    setDescription("Select which diagrams and components you want to be generated automatically!");
	    preferencesManager = new PreferencesManager();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		preferencesUI = new PreferencesUI();
		preferencesUI.init(container, preferencesManager);
		setControl(container);
	}

	public void setPreferences(){
		if(preferencesUI != null){
			preferencesManager.setValues(preferencesUI.getValues());
		}
	}
}
