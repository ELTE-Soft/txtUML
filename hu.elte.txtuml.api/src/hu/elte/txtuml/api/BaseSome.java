package hu.elte.txtuml.api;

class BaseSome<T extends ModelClass> extends BaseMany<T> {

	@Override
	boolean checkLowerBound() {
		return getSize() > 0;
	}

}
