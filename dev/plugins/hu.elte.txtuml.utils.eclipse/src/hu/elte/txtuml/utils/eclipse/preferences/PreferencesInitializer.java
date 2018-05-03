package hu.elte.txtuml.utils.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Initializes the preferences
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {

  /**
   * Initializes the default preferences
   */
  @Override
  public void initializeDefaultPreferences() {
	  PreferencesManager.setDefaults();
  }
}