package hu.elte.txtuml.api.model;

// TODO document
public final class UniqueAny<E> extends UniqueCollection<E, UniqueAny<E>> {

	UniqueAny() {
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
