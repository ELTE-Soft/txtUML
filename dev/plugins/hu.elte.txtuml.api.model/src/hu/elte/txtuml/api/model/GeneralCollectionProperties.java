package hu.elte.txtuml.api.model;

interface GeneralCollectionProperties {
	boolean isOrdered();

	boolean isUnique();

	int getLowerBound();

	int getUpperBound();
}
