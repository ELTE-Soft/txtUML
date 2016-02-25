package hu.elte.txtuml.api.model.runtime;

public interface Wrapper<W> {

	W getWrapped();

	default Class<?> getTypeOfWrapped() {
		return getWrapped().getClass();
	}

	@Override
	String toString();

}
