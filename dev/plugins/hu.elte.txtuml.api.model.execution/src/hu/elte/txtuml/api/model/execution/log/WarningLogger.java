package hu.elte.txtuml.api.model.execution.log;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.api.model.Signal;

/**
 * Writes into the {@link hu.elte.txtuml.utils.Logger#executor} log about every
 * warning event of a model executor.
 */
public class WarningLogger extends LoggerBase implements WarningListener {

	public WarningLogger(String nameOfExecutor) {
		super(nameOfExecutor);
	}

	@Override
	public void signalArrivedToDeletedObject(ModelClass obj, Signal signal) {
		warn("Signal arrived to deleted model object " + obj.toString() + ". The signal was: " + signal);
	}

	@Override
	public <L extends ModelClass, R extends ModelClass> void unlinkingNonExistingAssociation(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj) {
		warn("Trying to unlink a non-existing association between " + leftObj + " (at " + leftEnd.getName() + ") and "
				+ rightObj + " (at " + rightEnd.getName() + ").");
	}

	@Override
	public void lostSignalAtObject(Signal signal, ModelClass obj) {
		warn("A signal arrived to model object " + obj
				+ " but it was not processed by the object's state machine. The signal was: " + signal);
	}

	@Override
	public void lostSignalAtPort(Signal signal, Port<?, ?> portInstance) {
		warn("A signal arrived to port instance " + portInstance + " but it could not be send forward. The signal was: "
				+ signal);
	}

}
