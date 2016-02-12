package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.report.RuntimeWarningsListener;

final class RuntimeWarningsListenerImpl extends BaseListenerImpl implements RuntimeWarningsListener {

	RuntimeWarningsListenerImpl(ExecutorLog owner) {
		super(owner);
	}

	@Override
	public void signalArrivedToDeletedObject(ModelClass obj, Signal signal) {
		warn("Warning: signal arrived to deleted model object "
				+ obj.toString() + ". The signal was: " + signal);
	}
	
	@Override
	public void unlinkingNonExistingAssociation(ModelClass leftObj,
			ModelClass rightObj) {
		warn("Warning: trying to unlink a non-existing association between "
				+ leftObj + " and " + rightObj + ".");
	}
	
}
