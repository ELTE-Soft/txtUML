package hu.elte.txtuml.api.model.execution.impl.util;

import hu.elte.txtuml.api.model.impl.Wrapper;

public abstract class WrapperBuilder<T extends Wrapper<W>, W> extends WrapperImpl<W> {

	protected T result;

	public WrapperBuilder(W wrapped) {
		super(wrapped);
	}

	public final T get() {
		if (result == null) {
			build();
		}
		return result;
	}

	protected abstract void build();

}
