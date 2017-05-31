package hu.elte.txtuml.api.model;

// TODO document
public final class UniqueAny<E> extends UniqueCollection<E, UniqueAny<E>> {

	UniqueAny() {
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
