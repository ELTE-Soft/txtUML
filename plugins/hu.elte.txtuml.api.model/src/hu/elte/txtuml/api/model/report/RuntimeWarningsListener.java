package hu.elte.txtuml.api.model.report;

import hu.elte.txtuml.api.model.ModelClass;

/**
 * A listener interface that listens to runtime warnings of the model execution.
 * 
 * @author Gabor Ferenc Kovacs
 */
public interface RuntimeWarningsListener {

	default void unlinkingNonExistingAssociation(ModelClass leftObj,
			ModelClass rightObj) {
	}

	default void signalArrivedToDeletedObject(ModelClass obj) {
	}
}
