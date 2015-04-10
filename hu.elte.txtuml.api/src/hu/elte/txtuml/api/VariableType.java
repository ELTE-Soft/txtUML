package hu.elte.txtuml.api;

/*
 * TODO how to use variable types 
 */
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

	@Override
	public String toString() {
		return value.toString();
	}
}
