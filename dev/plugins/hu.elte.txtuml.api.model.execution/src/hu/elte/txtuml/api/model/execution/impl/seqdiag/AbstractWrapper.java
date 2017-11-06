package hu.elte.txtuml.api.model.execution.impl.seqdiag;

public class AbstractWrapper<T> {
	T wrapped;

	public AbstractWrapper(T wrapped) {
		this.wrapped = wrapped;
	}

	public T getWrapped() {
		return wrapped;
	}
}
