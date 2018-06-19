package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

/**
 * Wraps an interaction instance, providing additional runtime information and
 * management capabilities.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
@SequenceDiagramRelated
public interface InteractionRuntime extends Wrapper<Interaction>, ImplRelated {

	static InteractionRuntime current() throws NotSeqDiagExecutorThreadError {
		return SeqDiagThread.current().getCurrentInteraction();
	}

	/**
	 * Called by {@link Sequence#send(ModelClass, Signal, ModelClass)}. Read
	 * semantics there.
	 */
	void message(ModelClass sender, Signal signal, ModelClass target);

	/**
	 * Called by {@link Sequence#fromActor(Signal, ModelClass)}. Read semantics
	 * there.
	 */
	void messageFromActor(Signal signal, ModelClass target);

	/**
	 * Called by {@link Sequence#par(Interaction...)}. Read semantics there.
	 */
	void par(Interaction[] operands);

	/**
	 * Called by {@link Sequence#assertState(ModelClass, Class)}. Read semantics
	 * there.
	 */
	void assertState(ModelClass instance, Class<?> state);
}
