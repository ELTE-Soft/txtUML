package txtuml.api;

public class ModelType<T> extends ModelIdentifiedElement {
	protected ModelType(T val) {
		super();
		value = val;
	
	}
	protected ModelType() {
		this(null);
	}
	T getValue() {
		return value;
	}
	public String toString() {
		return value.toString(); // TODO should not be used in the model
	}

	public ModelString toMString() {
		return new ModelString(value.toString());
	}
	
	private final T value;

}

