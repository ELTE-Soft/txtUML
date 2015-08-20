package hu.elte.txtuml.api.model.backend.log;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.report.RuntimeWarningsListener;

class RuntimeWarningsListenerImpl extends BaseListenerImpl implements RuntimeWarningsListener {

	RuntimeWarningsListenerImpl(ExecutorLog log) {
		super(log);
	}

	@Override
	public void signalArrivedToDeletedObject(ModelClass obj) {
		warn("Warning: signal arrived to deleted model object "
				+ obj.toString() + ".");
	}
	
	@Override
	public void unlinkingNonExistingAssociation(ModelClass leftObj,
			ModelClass rightObj) {
		warn("Warning: trying to unlink a non-existing association between "
				+ leftObj + " and " + rightObj + ".");
	}
	
}
