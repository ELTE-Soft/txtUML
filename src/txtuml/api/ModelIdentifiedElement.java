package txtuml.api;

public abstract class ModelIdentifiedElement implements ModelElement{
	protected ModelIdentifiedElement()
	{
		identifier="inst_"+hashCode();
	}
	public final String getIdentifier() {
		return identifier;
	}
	protected final String identifier;
}
