package hu.elte.txtuml.api.model.backend;

import hu.elte.txtuml.api.diagnostics.DiagnosticsService;
import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

/**
 * A class that connects the modeling API and the {@link DiagnosticsService} by
 * listening to the model execution events and reporting them to the diagnostics
 * service.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public final class DiagnosticsServiceConnector implements
		ModelExecutionEventsListener {

	/**
	 * The singleton instance.
	 */
	private static DiagnosticsServiceConnector instance;

	private DiagnosticsServiceConnector() {
		if (DiagnosticsService.getInstance().addTerminationListener(
				DiagnosticsServiceConnector::shutdown)) {
			ModelExecutor
					.addToShutdownQueue(DiagnosticsService::shutdownInstance);
			ModelExecutor.Report.addModelExecutionEventsListener(this);
		}
	}

	public static synchronized DiagnosticsServiceConnector startAndGetInstance() {
		if (instance == null) {
			instance = new DiagnosticsServiceConnector();
		}
		return instance;
	}

	public static synchronized void shutdown() {
		ModelExecutor.Report.removeModelExecutionEventsListener(instance);
		instance = null;
	}

	@Override
	public void processingSignal(Region region, Signal signal) {
		DiagnosticsService.getInstance().sendNewModelEvent(
				MessageType.PROCESSING_SIGNAL,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				signal.getClass().getCanonicalName());
	}

	@Override
	public void usingTransition(Region region, Transition transition) {
		DiagnosticsService.getInstance().sendNewModelEvent(
				MessageType.USING_TRANSITION,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				transition.getClass().getCanonicalName());
	}

	@Override
	public void enteringVertex(Region region, Vertex vertex) {
		DiagnosticsService.getInstance().sendNewModelEvent(
				MessageType.ENTERING_VERTEX,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				vertex.getClass().getCanonicalName());
	}

	@Override
	public void leavingVertex(Region region, Vertex vertex) {
		DiagnosticsService.getInstance().sendNewModelEvent(
				MessageType.LEAVING_VERTEX,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				vertex.getClass().getCanonicalName());
	}

}
