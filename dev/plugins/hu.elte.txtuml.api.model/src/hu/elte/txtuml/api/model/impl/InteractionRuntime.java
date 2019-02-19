package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Proxy;
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
	 * Called by {@link Sequence#send(Lifeline, Signal, Lifeline)}. Read
	 * semantics there.
	 */
	public <T extends ModelClass, U extends ModelClass> void message(Lifeline<T> sender, Signal signal,
			Lifeline<U> target);

	/**
	 * Called by {@link Sequence#fromActor(Signal, Lifeline)}. Read semantics
	 * there.
	 */
	<T extends ModelClass, U extends ModelClass> void messageFromActor(Signal signal, Lifeline<U> target);

	/**
	 * Called by {@link Sequence#par(Interaction...)}. Read semantics there.
	 */
	void par(Interaction[] operands);

	/**
	 * Called by {@link Sequence#assertState(Lifeline, Class)}. Read semantics
	 * there.
	 */
	<T extends ModelClass> void assertState(Lifeline<T> instance, Class<?> state);

	<T extends ModelClass> Proxy<T> createProxy(Class<T> modelClass);
}
