package txtuml.api;

public class ModelString extends ModelType<String> {
	public ModelString(String val, boolean literal) {
		super(val, literal);
	}

	public ModelString(String val, boolean literal, String expression) {
		super(val, literal, expression);
	}

	public ModelString(String val) {
		super(val, true);
	}

	public ModelString() {
		this("", false);
	}

	@Override
	public ModelString toMString() {
		return this;
	}

	public ModelString concat(ModelString val) {
		return new ModelString(getValue() + val.getValue(), false);
	}

}
