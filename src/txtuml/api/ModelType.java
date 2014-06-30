package txtuml.api;

public class ModelType {
	protected ModelType(Object val) {
		value = val;
	}
	protected ModelType() {
		this(null);
	}
	void setValue(Object val) {
		value = val;
	}
	Object getValue(Object val) {
		return value;
	}
	public String toString() {
		return value.toString();
	}
		
	private Object value;
}

