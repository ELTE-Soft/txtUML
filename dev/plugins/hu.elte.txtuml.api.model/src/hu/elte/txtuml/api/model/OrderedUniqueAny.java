package hu.elte.txtuml.api.model;

// TODO document
public final class OrderedUniqueAny<E> extends OrderedUniqueCollection<E, OrderedUniqueAny<E>> {

	OrderedUniqueAny() {
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
