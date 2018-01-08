package hu.elte.txtuml.api.model.execution.impl.util;

import hu.elte.txtuml.api.model.impl.Wrapper;

/**
 * Base implementation for {@link Wrapper}.
 */
public class WrapperImpl<W> implements Wrapper<W> {

	private final W wrapped;

	public WrapperImpl(W wrapped) {
		this.wrapped = wrapped;
	}

	public W getWrapped() {
		return wrapped;
	}

	@Override
	public String toString() {
		return wrapped.toString();
	}

}
