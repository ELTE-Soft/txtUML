package hu.elte.txtuml.api.model;

//TODO document
@Min(1)
public final class OneToAny<E> extends Collection<E, OneToAny<E>> {

	OneToAny() {
	}

	@Override
	final int getLowerBoundPackagePrivate() {
		return 1;
	}

	@Override
	final int getUpperBoundPackagePrivate() {
		return GeneralCollection.INFINITE_BOUND;
	}

}
