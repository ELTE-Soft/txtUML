package hu.elte.txtuml.api.model;

// TODO document
public final class OrderedAny<E> extends OrderedCollection<E, OrderedAny<E>> {

	OrderedAny() {
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
