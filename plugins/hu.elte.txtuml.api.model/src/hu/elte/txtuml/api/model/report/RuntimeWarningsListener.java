package hu.elte.txtuml.api.model.report;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public interface RuntimeWarningsListener {

	default void unlinkingNonExistingAssociation(ModelClass leftObj,
			ModelClass rightObj) {}

	default void signalArrivedToDeletedObject(ModelClass obj, Signal signal) {}
}
