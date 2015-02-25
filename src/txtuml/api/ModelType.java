package txtuml.api;

public abstract class ModelType<T> extends ModelIdentifiedElementImpl implements
		ModelElement, ModelIdentifiedElement {

	private final T value;

	protected ModelType(T val) {
		super();
		value = val;
	}

	protected ModelType() {
		this(null);
	}

	final T getValue() {
		return value;
	}

	public String toString() {
		return value.toString(); // TODO should not be used in the model
	}

	public ModelString toMString() {
		return new ModelString(value.toString());
	}
}
