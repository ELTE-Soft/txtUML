package hu.elte.txtuml.api.model;

// TODO document
public final class OrderedAny<E> extends OrderedCollection<E, OrderedAny<E>> {

	OrderedAny() {
	}

	@Override
	final int getLowerBoundPackagePrivate() {
		return 0;
	}

	@Override
	final int getUpperBoundPackagePrivate() {
		return GeneralCollection.INFINITE_BOUND;
	}

}
