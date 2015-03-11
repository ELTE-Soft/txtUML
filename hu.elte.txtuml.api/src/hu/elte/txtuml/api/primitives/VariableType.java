package hu.elte.txtuml.api.primitives;

import hu.elte.txtuml.api.ModelType;

public abstract class VariableType<T, MT extends ModelType<T>> {

	private MT value;

	protected VariableType(MT value) {
		this.value = value;
	}

	public MT get() {
		return value;
	}

	public void set(MT value) {
		this.value = value;
	}

	public abstract void set(T rawValue);

	public String toString() {
		return value.toString();
	}
}
