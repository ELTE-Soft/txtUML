package hu.elte.txtuml.api;

public class ModelString extends ModelType<String> {

	public ModelString(String val) {
		super(val);
	}

	public ModelString() {
		this("");
	}

	@Override
	public ModelString toMString() {
		return this;
	}

	public ModelString concat(ModelString val) {
		return new ModelString(getValue() + val.getValue());
	}

}
