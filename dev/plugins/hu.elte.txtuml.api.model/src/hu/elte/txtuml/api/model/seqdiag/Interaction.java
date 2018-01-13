package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * This class represents an interaction UML element which is a whole sequence
 * diagram or a part of it.
 */
@FunctionalInterface
@SequenceDiagramRelated
public interface Interaction extends Runnable {

	/**
	 * Contains sequence diagram code (see {@link Sequence}).
	 */
	void run();

}
