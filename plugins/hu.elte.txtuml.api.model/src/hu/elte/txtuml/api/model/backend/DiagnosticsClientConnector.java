package hu.elte.txtuml.api.model.backend;

import hu.elte.txtuml.api.diagnostics.DiagnosticsClient;
import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;

public final class DiagnosticsClientConnector implements ModelExecutionEventsListener {

	private static DiagnosticsClientConnector instance;

	private DiagnosticsClientConnector() {
		if (DiagnosticsClient.getInstance().addTerminationListener(DiagnosticsClientConnector::shutdown)) {
			ModelExecutor.addToShutdownQueue(DiagnosticsClient::shutdownInstance);
			ModelExecutor.Report.addModelExecutionEventsListener(this);		
		}
	}
	
	public static synchronized DiagnosticsClientConnector startAndGetInstance() {
		if (instance == null) {
			instance = new DiagnosticsClientConnector();
		}
		return instance;
	}
	
	public static synchronized void shutdown() {
		ModelExecutor.Report.removeModelExecutionEventsListener(instance);
		instance = null;
	}
	
	@Override
	public void processingSignal(Region region, Signal signal) {
		DiagnosticsClient.getInstance().sendNewModelEvent(MessageType.PROCESSING_SIGNAL,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				signal.getClass().getCanonicalName(), true);
	}

	@Override
	public void usingTransition(Region region, Transition transition) {
		DiagnosticsClient.getInstance().sendNewModelEvent(MessageType.USING_TRANSITION,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				transition.getClass().getCanonicalName(), true);
	}

	@Override
	public void enteringVertex(Region region, Vertex vertex) {
		DiagnosticsClient.getInstance().sendNewModelEvent(MessageType.ENTERING_VERTEX,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				vertex.getClass().getCanonicalName(), true);
	}

	@Override
	public void leavingVertex(Region region, Vertex vertex) {
		DiagnosticsClient.getInstance().sendNewModelEvent(MessageType.LEAVING_VERTEX,
				region.getClass().getCanonicalName(), region.getIdentifier(),
				vertex.getClass().getCanonicalName(), true);
	}

}
