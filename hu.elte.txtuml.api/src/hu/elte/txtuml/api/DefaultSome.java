package hu.elte.txtuml.api;

class DefaultSome<T extends ModelClass> extends DefaultMany<T> {
	
	@Override
	boolean checkLowerBound() {
		return getSize() > 0;
	}
	
}
