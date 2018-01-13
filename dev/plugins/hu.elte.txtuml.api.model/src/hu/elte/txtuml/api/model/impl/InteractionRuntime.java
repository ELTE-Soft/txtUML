package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.NotSeqDiagExecutorThreadError;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.Interaction;

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

	void message(ModelClass sender, Signal signal, ModelClass target);

	void messageFromActor(Signal signal, ModelClass target);

	default void startFragment(CombinedFragmentType type, String fragmentName) {
		// TODO fix or remove (old syntax, doesn't work (never did))
	}

	default void endFragment() {
		// TODO fix or remove (old syntax, doesn't work (never did))
	}
	
	void par(Interaction[] operands);
}
