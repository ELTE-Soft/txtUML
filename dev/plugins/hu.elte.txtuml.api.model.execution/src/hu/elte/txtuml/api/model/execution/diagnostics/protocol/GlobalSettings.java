package hu.elte.txtuml.api.model.execution.diagnostics.protocol;

/**
 * Global protocol and timing related constants which can be used from the
 * DiagnosticsPlugin as well.
 */
public class GlobalSettings {
	/**
	 * Sets the VM property key which determines the DiagnosticsPlugin ports
	 */
	public static final String TXTUML_DIAGNOSTICS_SOCKET_PORT_KEY = "txtUMLDiagnosticsSocketPort";
	public static final String TXTUML_DIAGNOSTICS_HTTP_PORT_KEY = "txtUMLDiagnosticsHttpPort";
	
	/**
	 * Parameters for service over HTTP
	 * NOT KEYS, only predefined parameters.
	 */
	public static final String TXTUML_DIAGNOSTICS_HTTP_PATH = "registry";
	public static final String TXTUML_DIAGNOSTICS_DELAY_PATH = "delay";
}
