package hu.elte.txtuml.api.backend.logs;

import hu.elte.txtuml.api.ModelClass;

/**
 * This interface contains static methods to create specified warning messages.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
@SuppressWarnings("javadoc")
public interface WarningMessages {

	static String getUnlinkingNonExistingAssociationMessage(ModelClass leftObj,
			ModelClass rightObj) {
		return "Warning: trying to unlink a non-existing association between "
				+ leftObj.toString() + " and " + rightObj.toString() + ".";
	}

	static String getSignalArrivedToDeletedObjectMessage(ModelClass obj) {
		return "Warning: signal arrived to deleted model object "
				+ obj.toString() + ".";
	}

}
