package hu.elte.txtuml.xtxtuml;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class XtxtUMLStandaloneSetup extends XtxtUMLStandaloneSetupGenerated {

	public static void doSetup() {
		new XtxtUMLStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
	
}

