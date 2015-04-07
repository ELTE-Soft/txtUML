package hu.elte.txtuml.export.papyrus.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Initializes the preferences
 *
 * @author András Dobreff
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {


	private PreferencesManager preferenceManager;
	
  /**
   * The constructor
   */
  public PreferencesInitializer() {
	  preferenceManager = new PreferencesManager();
  }

  /**
   * Initializes the default preferences
   */
  @Override
  public void initializeDefaultPreferences() {
	  preferenceManager.setDefaults();
  }
}