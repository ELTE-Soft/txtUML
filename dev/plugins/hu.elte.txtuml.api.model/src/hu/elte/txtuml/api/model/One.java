package hu.elte.txtuml.api.model;

//TODO document
@Min(1)
@Max(1)
public final class One<E> extends Collection<E, One<E>> {

	One() {
	}

	@Override
	final int getLowerBoundPackagePrivate() {
		return 1;
	}

	@Override
	final int getUpperBoundPackagePrivate() {
		return 1;
	}

}
