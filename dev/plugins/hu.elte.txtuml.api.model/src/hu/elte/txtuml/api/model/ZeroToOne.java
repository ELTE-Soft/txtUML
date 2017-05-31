package hu.elte.txtuml.api.model;

//TODO document
@Min(0)
@Max(1)
public final class ZeroToOne<E> extends Collection<E, ZeroToOne<E>> {

	ZeroToOne() {
	}

	@Override
	final int getLowerBound() {
		return 0;
	}

	@Override
	final int getUpperBound() {
		return 1;
	}

}
