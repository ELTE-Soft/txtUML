package hu.elte.txtuml.api;

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

	protected final T getValue() {
		return value;
	}

	public String toString() {
		return value.toString();
	}

}
