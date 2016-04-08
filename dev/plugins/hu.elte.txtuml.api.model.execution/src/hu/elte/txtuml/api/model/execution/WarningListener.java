package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;

/**
 * Listener for runtime warnings of the model execution.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface WarningListener {

	default <L extends ModelClass, R extends ModelClass> void unlinkingNonExistingAssociation(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj) {
	}

	default void signalArrivedToDeletedObject(ModelClass obj, Signal signal) {
	}

	default void lostSignalAtObject(Signal signal, ModelClass obj) {
	}

	default void lostSignalAtPort(Signal signal, Port<?, ?> portInstance) {
	}
}
