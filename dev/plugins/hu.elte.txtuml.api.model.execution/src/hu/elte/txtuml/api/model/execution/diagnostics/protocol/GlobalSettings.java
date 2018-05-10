package hu.elte.txtuml.api.model.execution.diagnostics.protocol;

/**
 * Global protocol and timing related constants which can be used from the
 * DiagnosticsPlugin as well.
 */
public class GlobalSettings {
	/**
	 * Sets the VM property key which determines the DiagnosticsPlugin port
	 */
	public static final String TXTUML_DIAGNOSTICS_PORT_KEY = "txtUMLDiagnosticsPort";
	
	/**
	 * Parameters for service over HTTP
	 * NOT KEYS, only predefined parameters.
	 */
	public static final Integer TXTUML_DIAGNOSTICS_HTTP_PORT = 5252;
	public static final String TXTUML_DIAGNOSTICS_HTTP_PATH = "activeElements";
}
