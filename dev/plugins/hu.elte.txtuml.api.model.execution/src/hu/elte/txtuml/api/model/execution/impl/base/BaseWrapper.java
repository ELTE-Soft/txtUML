package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.impl.Wrapper;

/**
 * Base implementation for {@link Wrapper}.
 */
public class BaseWrapper<W> implements Wrapper<W> {

	private final W wrapped;

	public BaseWrapper(W wrapped) {
		this.wrapped = wrapped;
	}

	public W getWrapped() {
		return wrapped;
	};

	@Override
	public String toString() {
		return wrapped.toString();
	}

}
