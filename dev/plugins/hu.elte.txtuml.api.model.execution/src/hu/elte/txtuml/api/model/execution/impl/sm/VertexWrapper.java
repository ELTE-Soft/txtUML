package hu.elte.txtuml.api.model.execution.impl.sm;

import hu.elte.txtuml.api.model.StateMachine.Choice;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.runtime.Wrapper;

public interface VertexWrapper extends Wrapper<Vertex> {

	VertexWrapper getContainer();

	TransitionWrapper[] getOutgoings();

	default boolean isChoice() {
		return getWrapped() instanceof Choice;
	}

	boolean isComposite();

	default void performEntry() {
		getWrapped().entry();
	}

	default void performExit() {
		getWrapped().exit();
	}

	VertexWrapper getInitialOfSubSM();

	void setSubSM(VertexWrapper initial);

	// create methods

	static VertexWrapper create(Vertex wrapped, VertexWrapper container, TransitionWrapper[] outgoings) {
		return new VertexWrapper() {

			@Override
			public Vertex getWrapped() {
				return wrapped;
			}

			@Override
			public VertexWrapper getContainer() {
				return container;
			}

			@Override
			public TransitionWrapper[] getOutgoings() {
				return outgoings;
			}

			@Override
			public boolean isComposite() {
				return false;
			}

			@Override
			public VertexWrapper getInitialOfSubSM() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void setSubSM(VertexWrapper initial) {
				throw new UnsupportedOperationException();
			}

		};
	}

	static VertexWrapper createComposite(Vertex wrapped, VertexWrapper container, TransitionWrapper[] outgoings) {
		return new VertexWrapper() {
			private VertexWrapper initialOfSubSM = null;

			@Override
			public Vertex getWrapped() {
				return wrapped;
			}

			@Override
			public VertexWrapper getContainer() {
				return container;
			}

			@Override
			public TransitionWrapper[] getOutgoings() {
				return outgoings;
			}

			@Override
			public boolean isComposite() {
				return true;
			}

			@Override
			public boolean isChoice() {
				return false;
			}

			@Override
			public VertexWrapper getInitialOfSubSM() {
				return initialOfSubSM;
			}

			@Override
			public void setSubSM(VertexWrapper initial) {
				if (initialOfSubSM == null) {
					initialOfSubSM = initial;
				}
			}

		};
	}

}
