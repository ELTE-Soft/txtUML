package hu.elte.txtuml.api.model.execution.impl.sm;

import hu.elte.txtuml.api.model.execution.impl.base.BaseWrapper;
import hu.elte.txtuml.api.model.runtime.Wrapper;

public abstract class WrapperBuilder<T extends Wrapper<W>, W> extends BaseWrapper<W> {

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
