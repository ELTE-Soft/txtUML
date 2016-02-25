package hu.elte.txtuml.api.model.backend;

import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

/**
 * A class that connects the modeling API and the {@link DiagnosticsService} by
 * listening to the model execution events and reporting them to the diagnostics
 * service.
 */
public final class DiagnosticsServiceConnector implements
		ModelExecutionEventsListener {

	/**
	 * The singleton instance.
	 */
	private static DiagnosticsServiceConnector instance;

	private DiagnosticsServiceConnector() {
		// TODO remove feature
	}

	public static synchronized DiagnosticsServiceConnector startAndGetInstance() {
		if (instance == null) {
			instance = new DiagnosticsServiceConnector();
		}
		return instance;
	}

	public static synchronized void shutdown() {
		instance = null;
	}

}
