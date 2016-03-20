package hu.elte.txtuml.api.model.report;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.Signal;

/**
 * Listener for runtime warnings of the model execution.
 */
public interface RuntimeWarningsListener {

	default void unlinkingNonExistingAssociation(ModelClass leftObj, ModelClass rightObj) {
	}

	default void signalArrivedToDeletedObject(ModelClass obj, Signal signal) {
	}

	default void lostSignalAtObject(Region region, Signal signal) {
	}

	default void lostSignalAtPort(Port<?, ?> portInstance, Signal signal) {
	}
}
