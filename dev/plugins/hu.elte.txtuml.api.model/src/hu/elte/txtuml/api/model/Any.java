package hu.elte.txtuml.api.model;

// TODO document
public final class Any<E> extends Collection<E, Any<E>> {

	Any() {
	}

	@Override
	final int getLowerBound() {
		return 0;
	}

	@Override
	final int getUpperBound() {
		return -1;
	}

}
