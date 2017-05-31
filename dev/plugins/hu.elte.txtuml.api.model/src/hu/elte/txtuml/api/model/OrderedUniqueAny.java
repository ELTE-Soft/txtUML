package hu.elte.txtuml.api.model;

// TODO document
public final class OrderedUniqueAny<E> extends OrderedUniqueCollection<E, OrderedUniqueAny<E>> {

	OrderedUniqueAny() {
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
