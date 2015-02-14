package txtuml.api;

abstract class ModelIdentifiedElementImpl implements ModelIdentifiedElement {
	private final String identifier;

	protected ModelIdentifiedElementImpl() {
		this.identifier = "inst_" + System.identityHashCode(this); // FIXME
																	// guarantee
																	// to give
																	// different
																	// ids for
																	// every
																	// object
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}
}
