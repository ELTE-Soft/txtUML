package hu.elte.txtuml.export.papyrus.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

public class PreferencesInitializer extends AbstractPreferenceInitializer {

  PreferencesManager preferenceManager;
	
  public PreferencesInitializer() {
	  preferenceManager = new PreferencesManager();
  }

  @Override
  public void initializeDefaultPreferences() {
	  preferenceManager.setDefaults();
  }
}