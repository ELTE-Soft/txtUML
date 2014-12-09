package txtuml.api;

abstract class ModelIdentifiedElementImpl implements ModelIdentifiedElement {
	
	protected ModelIdentifiedElementImpl() {
		this.identifier = "inst_" + System.identityHashCode(this); // guarantees to give a different identifier for every object
	}
	
	@Override
	public final String getIdentifier() {
		return identifier;
	}
	
	private String identifier;
}
