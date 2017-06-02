package hu.elte.txtuml.api.model;

// TODO document
public final class Any<E> extends Collection<E, Any<E>> {

	Any() {
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
